/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * 
 * Unless you have purchased  a commercial license agreement from Jaspersoft,
 * the following license terms  apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.server.protocol.restv2;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.eclipse.core.runtime.IProgressMonitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.authority.ClientUser;
import com.jaspersoft.jasperserver.dto.permissions.RepositoryPermission;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceListWrapper;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceLookup;
import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.jasperserver.jaxrs.client.dto.common.ErrorDescriptor;
import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;
import com.jaspersoft.studio.server.AFinderUI;
import com.jaspersoft.studio.server.model.datasource.filter.IDatasourceFilter;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.protocol.ConnectionManager;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.ReportExecution;
import com.jaspersoft.studio.server.utils.HttpUtils;
import com.jaspersoft.studio.server.utils.Pass;
import com.jaspersoft.studio.server.wizard.exp.ExportOptions;
import com.jaspersoft.studio.server.wizard.imp.ImportOptions;
import com.jaspersoft.studio.server.wizard.permission.PermissionOptions;

public class RestV2Connection extends ARestV2Connection {

	private <T> T toObj(Request req, final Class<T> clazz, IProgressMonitor monitor) throws IOException {
		T obj = null;
		ConnectionManager.register(monitor, req);
		try {
			obj = exec.execute(req).handleResponse(new ResponseHandler<T>() {

				public T handleResponse(final HttpResponse response) throws IOException {
					HttpEntity entity = response.getEntity();
					InputStream in = null;
					try {
						StatusLine statusLine = response.getStatusLine();
						switch (statusLine.getStatusCode()) {
						case 200:
							in = getContent(entity);
							return mapper.readValue(in, clazz);
						case 204:
							return null;
						case 400:
						case 404:
							in = getContent(entity);
							ErrorDescriptor ed = mapper.readValue(in, ErrorDescriptor.class);
							if (ed != null)
								throw new ClientProtocolException(MessageFormat.format(ed.getMessage(), (Object[]) ed.getParameters()));
						default:
							throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
						}
					} finally {
						FileUtils.closeStream(in);
					}
				}

				protected InputStream getContent(HttpEntity entity) throws ClientProtocolException, IOException {
					if (entity == null)
						throw new ClientProtocolException("Response contains no content");
					return entity.getContent();
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			ConnectionManager.unregister(req);
		}
		return obj;
	}

	@Override
	public boolean connect(IProgressMonitor monitor, ServerProfile sp) throws Exception {
		this.sp = sp;

		URL url = sp.getURL();
		HttpHost host = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());

		exec = Executor.newInstance().auth(host, sp.getUser(), Pass.getPass(sp.getPass()));
		exec.authPreemptive(host);
		HttpUtils.setupProxy(exec, url.toURI());
		getServerInfo(monitor);

		return true;
	}

	private ObjectMapper mapper = FORMAT.equals("xml") ? JacksonHelper.getXMLMapper() : JacksonHelper.getJSONMapper();
	private Executor exec;

	@Override
	public ServerInfo getServerInfo(IProgressMonitor monitor) throws Exception {
		if (serverInfo != null)
			return serverInfo;
		serverInfo = toObj(HttpUtils.get(url("serverInfo"), sp), ServerInfo.class, monitor);
		if (serverInfo != null) {
			dateFormat = new SimpleDateFormat(serverInfo.getDateFormatPattern());
			timestampFormat = new SimpleDateFormat(serverInfo.getDatetimeFormatPattern());
		}
		return serverInfo;
	}

	@Override
	public List<ResourceDescriptor> list(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		List<ResourceDescriptor> rds = new ArrayList<ResourceDescriptor>();
		if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT)) {
			rd = get(monitor, rd, null);
			return rd.getChildren();
		} else {
			URIBuilder ub = new URIBuilder(url("resources"));
			ub.addParameter("folderUri", rd.getUriString());
			ub.addParameter("recursive", "false");
			ub.addParameter("sortBy", "label");
			ub.addParameter("limit", Integer.toString(Integer.MAX_VALUE));

			ClientResourceListWrapper resources = toObj(HttpUtils.get(ub.build().toASCIIString(), sp), ClientResourceListWrapper.class, monitor);
			if (resources != null)
				for (ClientResourceLookup crl : resources.getResourceLookups())
					rds.add(Rest2Soap.getRDLookup(this, crl));
		}
		return rds;
	}

	@Override
	public ResourceDescriptor get(IProgressMonitor monitor, ResourceDescriptor rd, File f) throws Exception {
		URIBuilder ub = new URIBuilder(url("resources" + rd.getUriString()));
		ub.addParameter("expanded", "true");
		Request req = HttpUtils.get(ub.build().toASCIIString(), sp);
		String rtype = WsTypes.INST().toRestType(rd.getWsType());
		req.setHeader("Accept", "application/repository." + rtype + "+" + FORMAT);
		ClientResource<?> crl = toObj(req, WsTypes.INST().getType(rtype), monitor);
		if (crl != null)
			return Rest2Soap.getRD(this, crl, rd);
		return null;
	}

	@Override
	public ResourceDescriptor move(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		URIBuilder ub = new URIBuilder(url("resources" + destFolderURI));
		ub.addParameter("overwrite", "true");
		ub.addParameter("createFolders", "true");
		Request req = HttpUtils.put(ub.build().toASCIIString(), sp);
		req.setHeader("Content-Location", rd.getUriString());
		String rtype = WsTypes.INST().toRestType(rd.getWsType());
		ClientResource<?> crl = toObj(req, WsTypes.INST().getType(rtype), monitor);
		if (crl != null)
			return Rest2Soap.getRD(this, crl, rd);
		return null;
	}

	@Override
	public ResourceDescriptor copy(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		URIBuilder ub = new URIBuilder(url("resources" + destFolderURI));
		ub.addParameter("overwrite", "true");
		ub.addParameter("createFolders", "true");
		Request req = HttpUtils.post(ub.build().toASCIIString(), sp);
		req.setHeader("Content-Location", rd.getUriString());
		String rtype = WsTypes.INST().toRestType(rd.getWsType());
		ClientResource<?> crl = toObj(req, WsTypes.INST().getType(rtype), monitor);
		if (crl != null)
			return Rest2Soap.getRD(this, crl, rd);
		return null;
	}

	@Override
	public ResourceDescriptor addOrModifyResource(IProgressMonitor monitor, ResourceDescriptor rd, File inputFile) throws Exception {
		URIBuilder ub = new URIBuilder(url("resources" + rd.getUriString()));
		ub.addParameter("createFolders", "true");
		ub.addParameter("overwrite", "true");
		Request req = HttpUtils.put(ub.build().toASCIIString(), sp);
		String rtype = WsTypes.INST().toRestType(rd.getWsType());
		ContentType ct = ContentType.create("application/repository." + rtype + "+" + FORMAT);
		req.bodyString(mapper.writeValueAsString(Soap2Rest.getResource(this, rd)), ct);
		ClientResource<?> crl = toObj(req, WsTypes.INST().getType(rtype), monitor);
		if (crl != null)
			return Rest2Soap.getRD(this, crl, rd);
		return null;
	}

	@Override
	public ResourceDescriptor modifyReportUnitResource(IProgressMonitor monitor, ResourceDescriptor runit, ResourceDescriptor rd, File inFile) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		Request req = HttpUtils.delete(url("resources" + rd.getUriString()), sp);
		if (exec.execute(req).returnResponse().getStatusLine().getStatusCode() == 204)
			System.out.println("Deleted");
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd, ResourceDescriptor runit) throws Exception {
		delete(monitor, rd);
	}

	@Override
	public ReportExecution runReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void cancelReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
	}

	@Override
	public List<ResourceDescriptor> listDatasources(IProgressMonitor monitor, IDatasourceFilter f) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void findResources(IProgressMonitor monitor, AFinderUI callback) throws Exception {
		throw new UnsupportedOperationException("Search not implemented for SOAP protocol.");
	}

	@Override
	public void getBundle(Map<String, String> map, String name, IProgressMonitor monitor) {

	}

	@Override
	public List<ResourceDescriptor> getInputControls(String uri, IProgressMonitor monitor) throws Exception {
		return null;
	}

	@Override
	public void reorderInputControls(String uri, List<ResourceDescriptor> rd, IProgressMonitor monitor) throws Exception {

	}

	@Override
	public ResourceDescriptor initInputControls(String uri, IProgressMonitor monitor) throws Exception {

		return null;
	}

	@Override
	public List<ResourceDescriptor> cascadeInputControls(ResourceDescriptor runit, List<ResourceDescriptor> ics, IProgressMonitor monitor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(IConnection parent) {
		// TODO Auto-generated method stub

	}

	@Override
	public StateDto importMetaData(ImportOptions options, IProgressMonitor monitor) throws Exception {
		return null;
	}

	@Override
	public StateDto exportMetaData(ExportOptions options, IProgressMonitor monitor) throws Exception {
		return null;
	}

	@Override
	public List<RepositoryPermission> getPermissions(ResourceDescriptor rd, IProgressMonitor monitor, PermissionOptions options) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientUser getUser(IProgressMonitor monitor) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<RepositoryPermission> setPermissions(ResourceDescriptor rd, List<RepositoryPermission> perms, PermissionOptions options, IProgressMonitor monitor) throws Exception {
		return perms;
	}

}
