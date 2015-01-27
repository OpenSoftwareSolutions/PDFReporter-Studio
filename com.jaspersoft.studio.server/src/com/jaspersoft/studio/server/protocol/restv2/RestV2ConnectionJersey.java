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
import java.net.IDN;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.jasperreports.eclipse.util.FileExtension;

import org.apache.http.client.HttpResponseException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.glassfish.jersey.SslConfigurator;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;

import com.jaspersoft.ireport.jasperserver.ws.FileContent;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.InputControlQueryDataRow;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.jasperserver.dto.authority.ClientUser;
import com.jaspersoft.jasperserver.dto.permissions.RepositoryPermission;
import com.jaspersoft.jasperserver.dto.permissions.RepositoryPermissionListWrapper;
import com.jaspersoft.jasperserver.dto.reports.ReportParameter;
import com.jaspersoft.jasperserver.dto.reports.ReportParameters;
import com.jaspersoft.jasperserver.dto.reports.inputcontrols.InputControlOption;
import com.jaspersoft.jasperserver.dto.reports.inputcontrols.InputControlState;
import com.jaspersoft.jasperserver.dto.reports.inputcontrols.ReportInputControl;
import com.jaspersoft.jasperserver.dto.reports.inputcontrols.ReportInputControlsListWrapper;
import com.jaspersoft.jasperserver.dto.resources.ClientFile;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceListWrapper;
import com.jaspersoft.jasperserver.dto.resources.ClientResourceLookup;
import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.ExportTaskDto;
import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.AttachmentDescriptor;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ExportDescriptor;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.OutputResourceDescriptor;
import com.jaspersoft.jasperserver.jaxrs.client.dto.reports.ReportExecutionDescriptor;
import com.jaspersoft.jasperserver.jaxrs.report.InputControlStateListWrapper;
import com.jaspersoft.jasperserver.jaxrs.report.ReportExecutionRequest;
import com.jaspersoft.studio.server.AFinderUI;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.editor.input.InputControlsManager;
import com.jaspersoft.studio.server.model.MReportUnitOptions;
import com.jaspersoft.studio.server.model.datasource.filter.DatasourcesAllFilter;
import com.jaspersoft.studio.server.model.datasource.filter.IDatasourceFilter;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.ReportExecution;
import com.jaspersoft.studio.server.protocol.Version;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.server.utils.HttpUtils;
import com.jaspersoft.studio.server.utils.Pass;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.exp.ExportOptions;
import com.jaspersoft.studio.server.wizard.imp.ImportOptions;
import com.jaspersoft.studio.server.wizard.permission.PermissionOptions;
import com.jaspersoft.studio.utils.Misc;

public class RestV2ConnectionJersey extends ARestV2ConnectionJersey {

	@Override
	public boolean connect(IProgressMonitor monitor, ServerProfile sp) throws Exception {
		monitor.subTask("Trying RESTv2");
		super.connect(monitor, sp);
		this.eh = new RESTv2ExceptionHandler(this);

		ClientConfig clientConfig = new ClientConfig();
		// values are in milliseconds
		// clientConfig.property(ClientProperties.READ_TIMEOUT, sp.getTimeout());
		clientConfig.property(ClientProperties.CONNECT_TIMEOUT, sp.getTimeout());
		if (sp.isChunked())
			clientConfig.property(ClientProperties.CHUNKED_ENCODING_SIZE, 1024);
		clientConfig.property(ApacheClientProperties.PREEMPTIVE_BASIC_AUTHENTICATION, true);

		// config your ssl for apache connector
		SslConfigurator sslConfig = SslConfigurator.newInstance(true);
		clientConfig.property(ApacheClientProperties.SSL_CONFIG, sslConfig);

		connector = new JSSApacheConnector(clientConfig);
		clientConfig.connector(connector);
		HttpUtils.setupProxy(clientConfig, sp.getURL().toURI());

		Client client = ClientBuilder.newBuilder().withConfig(clientConfig).build();
		// String user = sp.getUser();
		// if (!Misc.isNullOrEmpty(sp.getOrganisation()))
		// user += "|" + sp.getOrganisation();
		// client.register(new HttpBasicAuthFilter(user,
		// Pass.getPass(sp.getPass())));
		String url = sp.getUrl().trim();
		if (url.endsWith("/services/repository/"))
			url = url.substring(0, url.lastIndexOf("/services/repository/"));
		else if (url.endsWith("services/repository"))
			url = url.substring(0, url.lastIndexOf("/services/repository"));
		if (!url.endsWith("/"))
			url += "/";
		try {
			target = client.target(url + "j_spring_security_check");
			target = target.queryParam("forceDefaultRedirect", "false");
			if (sp.isUseSSO()) {
				String token = CASUtil.getToken(sp, monitor);
				target = target.queryParam("ticket", token);
			} else {
				target = target.queryParam("j_username", sp.getUser());
				target = target.queryParam("j_password", Pass.getPass(sp.getPass()));
			}
			target = target.queryParam("orgId", sp.getOrganisation());
			if (!Misc.isNullOrEmpty(sp.getLocale()))
				target = target.queryParam("userLocale", "true");
			if (!Misc.isNullOrEmpty(sp.getTimeZone()))
				target = target.queryParam("userTimezone", "true");

			Builder req = target.request();
			toObj(connector.get(req, monitor), String.class, monitor);
		} finally {
			// ok, now check others
			target = client.target(IDN.toASCII(url + SUFFIX));
		}
		getServerInfo(monitor);
		return serverInfo != null && serverInfo.getVersion().compareTo("5.5") >= 0;
	}

	@Override
	public ServerInfo getServerInfo(IProgressMonitor monitor) throws Exception {
		if (serverInfo != null)
			return serverInfo;
		Builder req = target.path("serverInfo").request();
		serverInfo = toObj(connector.get(req, monitor), ServerInfo.class, monitor);
		if (serverInfo != null) {
			serverInfo.setTimeFormatPattern(((SimpleDateFormat) getTimeFormat()).toPattern());
			dateFormat = new SimpleDateFormat(serverInfo.getDateFormatPattern());
			timestampFormat = new SimpleDateFormat(serverInfo.getDatetimeFormatPattern());
			sp.setJrVersion(Version.setJRVersion(serverInfo));
		}
		return serverInfo;
	}

	@Override
	public List<ResourceDescriptor> list(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		List<ResourceDescriptor> rds = new ArrayList<ResourceDescriptor>();
		if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT)) {
			rd = parent.get(monitor, rd, null);
			return rd.getChildren();
		} else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DOMAIN_TOPICS)) {
			rd = parent.get(monitor, rd, null);
			return rd.getChildren();
		} else if (rd.getWsType().equals(ResourceDescriptor.TYPE_ADHOC_DATA_VIEW)) {
			return getInputControls(rd.getParentFolder() + "/" + rd.getName(), monitor);
		} else if (rd.getWsType().equals(ResourceDescriptor.TYPE_DASHBOARD)) {
			return getInputControls(rd.getParentFolder() + "/" + rd.getName(), monitor);
		} else if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORT_OPTIONS)) {
			return getInputControls(rd.getParentFolder() + "/" + rd.getName(), monitor);
		} else {
			WebTarget tgt = target.path("resources");
			tgt = tgt.queryParam("folderUri", rd.getUriString());
			tgt = tgt.queryParam("recursive", "false");
			tgt = tgt.queryParam("sortBy", "label");
			tgt = tgt.queryParam("limit", 0);

			Builder req = tgt.request();
			ClientResourceListWrapper resources = toObj(connector.get(req, monitor), ClientResourceListWrapper.class, monitor);
			if (resources != null) {
				boolean isPublic = false;
				for (ClientResourceLookup crl : resources.getResourceLookups()) {
					if (!isPublic)
						isPublic = crl.getUri().equals("/public");
					ResourceDescriptor nrd = Rest2Soap.getRDLookup(this, crl);
					rds.add(nrd);
					if (nrd.getWsType().equals(ResourceDescriptor.TYPE_CONTENT_RESOURCE)) {
						String name = nrd.getUriString().toLowerCase();
						if (FileExtension.isImage(name))
							nrd.setWsType(ResourceDescriptor.TYPE_IMAGE);
						if (FileExtension.isFont(name))
							nrd.setWsType(ResourceDescriptor.TYPE_FONT);
						else if (name.endsWith(".xml"))
							nrd.setWsType(ResourceDescriptor.TYPE_XML_FILE);
						else if (name.endsWith(FileExtension.PointJRXML))
							nrd.setWsType(ResourceDescriptor.TYPE_JRXML);
						else if (name.endsWith(".jar"))
							nrd.setWsType(ResourceDescriptor.TYPE_CLASS_JAR);
						else if (name.endsWith(FileExtension.PointJRTX))
							nrd.setWsType(ResourceDescriptor.TYPE_STYLE_TEMPLATE);
						else if (name.endsWith(ResourceDescriptor.TYPE_CSS_FILE))
							nrd.setWsType(ResourceDescriptor.TYPE_CSS_FILE);
						else if (name.endsWith(".properties"))
							nrd.setWsType(ResourceDescriptor.TYPE_RESOURCE_BUNDLE);
					}
				}
				// workaround
				if (rd.getUriString().equals("/") && !isPublic) {
					try {
						ResourceDescriptor pub = new ResourceDescriptor();
						pub.setUriString("/public");
						pub.setWsType(ResourceDescriptor.TYPE_FOLDER);
						rds.add(parent.get(monitor, pub, null));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return rds;
	}

	@Override
	public ResourceDescriptor get(IProgressMonitor monitor, ResourceDescriptor rd, File f) throws Exception {
		if (rd.getUriString() == null || rd.getUriString().contains("<"))
			throw new Exception("wrong url");
		String uri = rd.getUriString();
		if (uri.startsWith("repo://"))
			uri = uri.substring(5);
		if (!uri.startsWith("/"))
			uri = "/" + uri;

		WebTarget tgt = target.path("resources" + uri);
		tgt = tgt.queryParam("expanded", "true");

		String rtype = WsTypes.INST().toRestType(rd.getWsType());
		Builder req = tgt.request("application/repository." + rtype + "+" + FORMAT);
		ClientResource<?> crl = toObj(connector.get(req, monitor), WsTypes.INST().getType(rtype), monitor);
		if (crl != null) {
			if (f != null && crl instanceof ClientFile) {
				ClientFile cf = (ClientFile) crl;
				tgt = target.path("resources" + uri);
				try {
					req = tgt.request(cf.getType().getMimeType()).header("Accept", cf.getType().getMimeType());
					readFile(connector.get(req, monitor), f, monitor);
				} catch (HttpResponseException e) {
					if (e.getStatusCode() == 500)
						;// jrs 5.5 returns 500 if file is not existing, a bug
					// for newer versions, we should show the error
				}
			}
			return Rest2Soap.getRD(this, crl);
		}
		return null;
	}

	@Override
	public ResourceDescriptor move(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		String wsType = rd.getWsType();
		String rtype = WsTypes.INST().toRestType(wsType);

		WebTarget tgt = target.path("resources" + destFolderURI);
		tgt = tgt.queryParam("overwrite", "true");
		tgt = tgt.queryParam("createFolders", "true");

		Builder req = tgt.request().header("Content-Location", rd.getUriString());// .header("Content-Length",
																																							// "0");
		Response r = connector.put(req, Entity.entity("", MediaType.APPLICATION_XML_TYPE), monitor);
		ClientResource<?> crl = toObj(r, WsTypes.INST().getType(rtype), monitor);
		if (crl != null) {
			rd = new ResourceDescriptor();
			rd.setWsType(wsType);
			rd.setUriString(crl.getUri());
			return doGet(monitor, rd, crl);
		}
		return null;
	}

	@Override
	public ResourceDescriptor copy(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		String wsType = rd.getWsType();
		String rtype = WsTypes.INST().toRestType(wsType);

		WebTarget tgt = target.path("resources" + destFolderURI);
		tgt = tgt.queryParam("overwrite", "true");
		tgt = tgt.queryParam("createFolders", "true");

		Builder req = tgt.request().header("Content-Location", rd.getUriString());
		Response r = connector.post(req, Entity.entity("", MediaType.APPLICATION_XML_TYPE), monitor);
		ClientResource<?> crl = toObj(r, WsTypes.INST().getType(rtype), monitor);
		if (crl != null) {
			rd = new ResourceDescriptor();
			rd.setWsType(wsType);
			rd.setUriString(crl.getUri());
			return doGet(monitor, rd, crl);
		}
		return null;
	}

	@Override
	public ResourceDescriptor addOrModifyResource(IProgressMonitor monitor, ResourceDescriptor rd, File inFile) throws Exception {
		prepareResource(monitor, rd, inFile);

		String rtype = WsTypes.INST().toRestType(rd.getWsType());
		ClientResource<?> cr = Soap2Rest.getResource(this, rd);
		Response r = null;
		if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORT_OPTIONS)) {
			ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MReportUnitOptions.PROP_RU_URI, rd.getProperties());
			WebTarget tgt = target.path("reports" + resprop.getValue() + "/options");
			tgt = tgt.queryParam("label", rd.getLabel());
			tgt = tgt.queryParam("overwrite", "true");

			ReportParameters rprms = new ReportParameters(new ArrayList<ReportParameter>());

			Builder req = tgt.request();

			r = connector.post(req, Entity.entity(rprms, MediaType.APPLICATION_XML_TYPE), monitor);

			try {
				toObj(r, String.class, monitor);
			} catch (HttpResponseException e) {
				if (e.getStatusCode() == 409) {
					rd.setVersion(parent.get(monitor, rd, null).getVersion());
					return addOrModifyResource(monitor, rd, inFile);
				} else
					throw e;
			}
			return doGet(monitor, rd, cr);
		}
		WebTarget tgt = target.path("resources" + rd.getUriString());
		tgt = tgt.queryParam("createFolders", "true");
		tgt = tgt.queryParam("overwrite", "true");

		Builder req = tgt.request();
		r = connector.put(req, Entity.entity(cr, "application/repository." + rtype + "+" + FORMAT), monitor);

		ClientResource<?> crl = null;
		try {
			crl = toObj(r, WsTypes.INST().getType(rtype), monitor);
		} catch (HttpResponseException e) {
			if (e.getStatusCode() == 409) {
				rd.setVersion(parent.get(monitor, rd, null).getVersion());
				return addOrModifyResource(monitor, rd, inFile);
			} else
				throw e;
		}
		if (crl != null && !monitor.isCanceled())
			rd = doGet(monitor, rd, crl);

		return rd;
	}

	private void prepareResource(IProgressMonitor monitor, ResourceDescriptor rd, File inFile) throws Exception {
		if (!rd.getIsNew() && rd.getChildren() != null) {
			for (ResourceDescriptor r : rd.getChildren()) {
				if (!r.getIsNew() && r.isDirty1()) {
					addOrModifyResource(monitor, r, null);
					r.setDirty(false);
				} else
					prepareResource(monitor, r, null);
			}
		}
	}

	private ResourceDescriptor doGet(IProgressMonitor monitor, ResourceDescriptor rd, ClientResource<?> crl) throws Exception, ParseException {
		boolean refresh = false;
		if (WsTypes.INST().isContainerType(crl.getClass()))
			refresh = true;
		if (monitor.isCanceled())
			return rd;
		if (refresh)
			rd = parent.get(monitor, rd, null);
		else
			rd = Rest2Soap.getRD(this, crl, rd);
		return rd;
	}

	@Override
	public ResourceDescriptor modifyReportUnitResource(IProgressMonitor monitor, ResourceDescriptor runit, ResourceDescriptor rd, File inFile) throws Exception {
		if (rd.getIsReference() && (rd.hasDirtyChildren() || rd.getHasData())) {
			// ResourceDescriptor r = new ResourceDescriptor();
			// r.setUriString(Misc.nvl(rd.getReferenceUri(), rd.getUriString()));
			// r.setWsType(rd.getWsType());
			// ResourceDescriptor ref = parent.get(monitor, r, null);
			// ref.setHasData(true);
			// ref.setData(rd.getData());
			return addOrModifyResource(monitor, rd, inFile);
		}
		runit = parent.get(monitor, runit, null);
		PublishUtil.setChild(runit, rd);
		return addOrModifyResource(monitor, runit, inFile);
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		WebTarget tgt = target.path("resources" + rd.getUriString());
		Response res = connector.delete(tgt.request(), monitor);
		try {
			switch (res.getStatus()) {
			case 204:
				System.out.println("Deleted");
				break;
			default:
				eh.handleException(res, monitor);
			}
		} finally {
			res.close();
		}
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd, ResourceDescriptor runit) throws Exception {
		ResourceDescriptor rdrem = null;
		for (ResourceDescriptor r : runit.getChildren())
			if (r.getUriString().equals(rd.getUriString())) {
				rdrem = r;
				break;
			}
		if (rdrem != null) {
			runit.getChildren().remove(rdrem);
			addOrModifyResource(monitor, runit, null);
		}
	}

	@Override
	public ReportExecution runReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
		Map<String, FileContent> map = new HashMap<String, FileContent>();
		ReportExecutionDescriptor res = null;
		WebTarget tgt = null;
		Builder req = null;
		if (repExec.getRequestId() != null && !repExec.getStatus().equals("refresh")) {
			// if (repExec.getStatus().equals("refresh")) {
			// tgt = target.path("reportExecutions/" + repExec.getRequestId() +
			// "/exports");
			// req = tgt.request();
			// ExportExecutionOptions ed = new ExportExecutionOptions();
			// ed.setAttachmentsPrefix("");
			// ed.setPages("1");
			// for (Argument arg : repExec.getArgs()) {
			// if (arg.getName().equals(Argument.RUN_OUTPUT_FORMAT)) {
			// ed.setOutputFormat(arg.getValue());
			// break;
			// }
			// }
			// Response r = connector.post(req, Entity.entity(ed,
			// MediaType.APPLICATION_XML_TYPE), monitor);
			// res = toObj(r, ReportExecutionExport.class, monitor);
			// } else {
			tgt = target.path("reportExecutions/" + repExec.getRequestId());
			req = tgt.request();
			Response r = connector.get(req, monitor);
			res = toObj(r, ReportExecutionDescriptor.class, monitor);
			// }
		} else {
			ReportExecutionRequest rer = new ReportExecutionRequest();
			rer.setReportUnitUri(repExec.getReportURI());
			rer.setAsync(true);
			rer.setFreshData(true);
			rer.setIgnorePagination(false);
			rer.setAllowInlineScripts(true);
			rer.setInteractive(true);
			rer.setAttachmentsPrefix("");
			String format = null;
			for (Argument arg : repExec.getArgs()) {
				if (arg.getName().equals(Argument.RUN_OUTPUT_FORMAT)) {
					format = arg.getValue();
					break;
				}
			}
			if (format == null)
				format = Argument.RUN_OUTPUT_FORMAT_JRPRINT;
			if (format.equals(Argument.RUN_OUTPUT_FORMAT_JRPRINT))
				rer.setPages("1");
			rer.setOutputFormat(format);

			// rer.setTransformerKey(transformerKey);
			rer.setSaveDataSnapshot(false);
			Map<String, Object> prm = repExec.getPrm();
			if (prm != null && !prm.isEmpty()) {
				List<ReportParameter> list = new ArrayList<ReportParameter>();
				for (String key : prm.keySet()) {
					ReportParameter rprm = new ReportParameter();
					rprm.setName(key);
					Object item = prm.get(key);
					List<String> vals = new ArrayList<String>();
					if (item instanceof Collection<?>) {
						Collection<?> c = (Collection<?>) item;
						for (Object obj : c)
							vals.add(toRestString(obj));
					} else
						vals.add(toRestString(item));
					rprm.setValues(vals);
					list.add(rprm);
				}
				rer.setParameters(new ReportParameters(list));
			}
			// if (!getServerInfo().getVersion().equals("5.6.0"))
			if (rer.getOutputFormat().equals(Argument.RUN_OUTPUT_FORMAT_HTML)) {
				String rourl = "reports" + repExec.getReportURI() + ".html";
				if (rer.getParameters() != null) {
					String del = "?";
					for (ReportParameter rp : rer.getParameters().getReportParameters()) {
						rourl += del;
						rourl += rp.getName() + "=";
						for (String v : rp.getValues()) {
							rourl += v;
						}
						del = "&";
					}
				}
				repExec.setBaseUrl(target.getUri().toASCIIString());
				repExec.setPathUrl(rourl);
				repExec.setReportOutputCookie(connector.getCookieStore().getCookies());
				repExec.setStatus("ready");
				return repExec;
			}

			tgt = target.path("reportExecutions");
			req = tgt.request();
			Response r = connector.post(req, Entity.entity(rer, MediaType.APPLICATION_XML_TYPE), monitor);
			res = toObj(r, ReportExecutionDescriptor.class, monitor);
		}
		if (res != null && res.getErrorDescriptor() == null) {
			if (res.getExports() != null) {
				int i = 0;
				for (ExportDescriptor ee : res.getExports()) {
					// System.out.println(ee.getOutputResource());
					if (ee.getStatus().equals("queued"))
						continue;

					tgt = target.path("reportExecutions/" + res.getRequestId() + "/exports/" + ee.getId() + "/outputResource");
					if (ee.getOutputResource() != null)
						req = tgt.request(ee.getOutputResource().getContentType());
					else
						req = tgt.request();
					Response r = connector.get(req, monitor);
					if (ee.getOutputResource() == null) {
						OutputResourceDescriptor or = new OutputResourceDescriptor();
						or.setContentType(r.getHeaderString("Content-Type"));
						or.setFileName("file");
						ee.setOutputResource(or);
					}
					byte[] d = readFile(r, monitor);
					addFileContent(d, map, ee.getOutputResource(), "attachment-" + i++, "report");
					if (ee.getAttachments() != null)
						for (AttachmentDescriptor ror : ee.getAttachments()) {
							tgt = target.path("reportExecutions/" + res.getRequestId() + "/exports/" + ee.getId() + "/attachments/" + ror.getFileName());
							req = tgt.request(ror.getContentType());
							d = readFile(connector.get(req, monitor), monitor);
							addFileContent(d, map, ror, "attachment-" + i++, ror.getFileName());
						}
				}
			} else {
				tgt = target.path("reportExecutions/" + res.getRequestId() + "/exports/" + Argument.RUN_OUTPUT_FORMAT_JRPRINT + "/outputResource");
				req = tgt.request(MediaType.APPLICATION_OCTET_STREAM_TYPE);
				byte[] d = readFile(connector.get(req, monitor), monitor);
				FileContent content = new FileContent();
				content.setData(d);
				content.setMimeType(MediaType.APPLICATION_OCTET_STREAM_TYPE.toString());
				content.setName("attachment-0");

				map.put("jasperPrint", content);
				// addFileContent(d, map, , "attachment-0", "jasperPrint");
			}
		}
		repExec.setFiles(map);
		repExec.setCurrentPage(res.getCurrentPage());
		repExec.setErrorDescriptor(res.getErrorDescriptor());
		repExec.setRequestId(res.getRequestId());
		repExec.setStatus(res.getStatus());
		repExec.setTotalPages(res.getTotalPages());
		// System.out.println(res.getStatus() + " : " + res.getTotalPages() + " : "
		// + map.size());
		return repExec;
	}

	@Override
	public void cancelReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
		WebTarget tgt = target.path("reportExecutions/" + repExec.getRequestId() + "/status");
		Builder req = tgt.request();
		connector.put(req, Entity.entity("<status>cancelled</status>", MediaType.APPLICATION_XML_TYPE), monitor);
	}

	private void addFileContent(byte[] d, Map<String, FileContent> map, AttachmentDescriptor r, String id, String key) {
		if (d != null) {
			FileContent content = new FileContent();
			content.setData(d);
			content.setMimeType(r.getContentType());
			content.setName(Misc.nvl(r.getFileName(), id));

			map.put(key, content);
		}
	}

	private void addFileContent(byte[] d, Map<String, FileContent> map, OutputResourceDescriptor r, String id, String key) {
		if (d != null) {
			FileContent content = new FileContent();
			content.setData(d);
			content.setMimeType(r.getContentType());
			content.setName(Misc.nvl(r.getFileName(), id));

			map.put(key, content);
		}
	}

	@Override
	public List<ResourceDescriptor> listDatasources(IProgressMonitor monitor, IDatasourceFilter f) throws Exception {
		List<ResourceDescriptor> rds = new ArrayList<ResourceDescriptor>();
		WebTarget tgt = target.path("resources");
		if (f == null)
			f = new DatasourcesAllFilter();
		for (String type : f.getFilterTypes())
			tgt = tgt.queryParam("type", WsTypes.INST().toRestType(type));
		tgt = tgt.queryParam("sortBy", "label");
		tgt = tgt.queryParam("limit", 0);

		Builder req = tgt.request();
		ClientResourceListWrapper resources = toObj(connector.get(req, monitor), ClientResourceListWrapper.class, monitor);
		if (resources != null)
			for (ClientResourceLookup crl : resources.getResourceLookups())
				rds.add(Rest2Soap.getRDLookup(this, crl));
		return rds;
	}

	@Override
	public void findResources(IProgressMonitor monitor, AFinderUI callback) throws Exception {
		connector.closeLastRequest();
		if (callback.getText() == null) {
			callback.showResults(null);
			return;
		}
		WebTarget tgt = target.path("resources");
		tgt = tgt.queryParam("q", callback.getText());
		for (String type : callback.getTypes())
			tgt = tgt.queryParam("type", type);
		tgt = tgt.queryParam("sortBy", "label");
		tgt = tgt.queryParam("limit", 0);

		Builder req = tgt.request();
		ClientResourceListWrapper resources = toObj(connector.get(req, monitor), ClientResourceListWrapper.class, monitor);
		callback.showResults(resources != null ? resources.getResourceLookups() : null);
	}

	@Override
	public boolean isSupported(Feature f) {
		switch (f) {
		case SEARCHREPOSITORY:
		case UPDATEDATE:
		case TIMEZONE:
		case PERMISSION:
		case DATASOURCENAME:
		case INPUTCONTROLS_ORDERING:
		case MAXLENGHT:
		case IMPORTMETADATA:
		case EXPORTMETADATA:
			return true;
		}
		return super.isSupported(f);
	}

	@Override
	public void getBundle(Map<String, String> map, String name, IProgressMonitor monitor) throws Exception {
		Builder req = target.path("bundles/" + name).request(MediaType.APPLICATION_JSON_TYPE);
		try {
			GenericType<Map<String, String>> type = new GenericType<Map<String, String>>() {
			};
			Map<String, String> m = toObj(connector.get(req, monitor), type, monitor);
			if (m != null)
				map.putAll(m);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public List<ResourceDescriptor> getInputControls(String uri, IProgressMonitor monitor) throws Exception {
		List<ResourceDescriptor> rds = new ArrayList<ResourceDescriptor>();
		Builder req = target.path("reports/" + uri.replaceFirst("/", "") + "/inputControls").request();
		try {
			ReportInputControlsListWrapper m = toObj(connector.get(req, monitor), ReportInputControlsListWrapper.class, monitor);
			if (m != null) {
				for (ReportInputControl ric : m.getInputParameters())
					rds.add(Rest2Soap.getInputControl(this, ric, new ResourceDescriptor()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rds;
	}

	@Override
	public void reorderInputControls(String uri, List<ResourceDescriptor> rds, IProgressMonitor monitor) throws Exception {
		Builder req = target.path("reports" + uri + "/inputControls").request();
		Response res = connector.get(req, monitor);
		ReportInputControlsListWrapper crl = toObj(res, ReportInputControlsListWrapper.class, monitor);
		if (crl != null) {
			List<ReportInputControl> ics = new ArrayList<ReportInputControl>();
			for (ResourceDescriptor r : rds) {
				String ruri = "repo:" + r.getUriString();
				for (ReportInputControl ric : crl.getInputParameters()) {
					if (ruri.equals(ric.getUri()))
						ics.add(ric);
				}
			}
			ReportInputControlsListWrapper wrapper = new ReportInputControlsListWrapper(ics);
			req = target.path("reports" + uri + "/inputControls").request();
			Response r = connector.put(req, Entity.entity(wrapper, MediaType.APPLICATION_XML_TYPE), monitor);
			toObj(r, ReportInputControlsListWrapper.class, monitor);
		}
	}

	@Override
	public ResourceDescriptor initInputControls(String uri, IProgressMonitor monitor) throws Exception {
		uri = WSClientHelper.getReportUnitUri(uri);
		ResourceDescriptor rdunit = new ResourceDescriptor();
		rdunit.setUriString(uri);
		rdunit.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
		rdunit = parent.get(monitor, rdunit, null);
		if (monitor.isCanceled())
			return rdunit;
		Builder req = target.path("reports" + uri + "/inputControls").request();
		Response r = connector.get(req, monitor);
		ReportInputControlsListWrapper crl = toObj(r, ReportInputControlsListWrapper.class, monitor);
		if (crl != null)
			for (ResourceDescriptor rd : rdunit.getChildren()) {
				if (rd.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL)) {
					for (ReportInputControl ric : crl.getInputParameters()) {
						InputControlState ics = ric.getState();
						rd.setMasterInputControls(ric.getMasterDependencies());
						if (ics.getId().equals(rd.getName())) {
							setInputControlState(rd, ics);
							break;
						}
					}
				}
			}
		return rdunit;
	}

	private void setInputControlState(ResourceDescriptor rd, InputControlState ics) {
		if (ics.getValue() != null)
			rd.setValue(ics.getValue());
		else if (ics.getOptions() != null) {
			if (InputControlsManager.isICQuery(rd)) {
				List<InputControlQueryDataRow> qvalues = new ArrayList<InputControlQueryDataRow>();
				for (InputControlOption ico : ics.getOptions()) {
					InputControlQueryDataRow dr = new InputControlQueryDataRow();
					dr.setValue(ico.getValue());
					List<String> cols = new ArrayList<String>();
					for (String s : ico.getLabel().split("\\s\\|\\s"))
						cols.add(s);
					dr.setColumnValues(cols);
					dr.setSelected(ico.isSelected());
					qvalues.add(dr);
				}
				rd.setQueryData(qvalues);
			} else if (InputControlsManager.isICListOfValues(rd)) {
				List<ListItem> qvalues = new ArrayList<ListItem>();
				for (InputControlOption ico : ics.getOptions()) {
					ListItem dr = new ListItem();
					dr.setValue(ico.getValue());
					dr.setLabel(ico.getLabel());
					dr.setSelected(ico.isSelected());
					qvalues.add(dr);
				}
				rd.setListOfValues(qvalues);
			}
		}
	}

	@Override
	public List<ResourceDescriptor> cascadeInputControls(ResourceDescriptor runit, List<ResourceDescriptor> ics, IProgressMonitor monitor) throws Exception {
		if (ics.isEmpty())
			return ics;
		String ctrls = "";
		String del = "";
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		List<ReportParameter> lrp = new ArrayList<ReportParameter>();
		for (ResourceDescriptor rd : ics) {
			Map<String, Object> icMap = rd.getIcValues();
			for (String key : icMap.keySet())
				setMap(key, map, icMap.get(key));
			ctrls += del + rd.getName();
			del = ";";
		}
		for (String key : map.keySet()) {
			ReportParameter r = new ReportParameter();
			r.setName(key);
			r.setValues(map.get(key));
			lrp.add(r);
		}
		Builder req = target.path("reports" + runit.getUriString() + "/inputControls/" + ctrls + "/values").request();
		Response r = connector.post(req, Entity.entity(new ReportParameters(lrp), MediaType.APPLICATION_XML_TYPE), monitor);
		InputControlStateListWrapper crl = toObj(r, InputControlStateListWrapper.class, monitor);
		if (crl != null) {
			for (ResourceDescriptor rd : ics) {
				if (rd.getWsType().equals(ResourceDescriptor.TYPE_INPUT_CONTROL))
					for (InputControlState state : crl.getInputControlStateList()) {
						if (state.getId().equals(rd.getName())) {
							setInputControlState(rd, state);
							break;
						}
					}
			}
		}
		return ics;
	}

	private void setMap(String key, Map<String, List<String>> map, Object value) {
		List<String> vals = map.get(key);
		if (vals == null) {
			vals = new ArrayList<String>();
			map.put(key, vals);
		}
		if (value instanceof Collection) {
			for (Object obj : (Collection<?>) value) {
				String str = val2String(obj);
				if (!vals.contains(str))
					vals.add(str);
			}
		} else {
			String str = val2String(value);
			if (!vals.contains(str))
				vals.add(str);
		}
	}

	private String val2String(Object val) {
		if (val instanceof java.sql.Date)
			return getDateFormat().format(val);
		if (val instanceof java.sql.Timestamp)
			return getTimestampFormat().format(val);
		if (val instanceof java.sql.Time)
			return getTimeFormat().format(val);
		return val.toString();
	}

	@Override
	public StateDto importMetaData(ImportOptions options, IProgressMonitor monitor) throws Exception {
		if (options.getState() != null) {
			WebTarget tgt = target.path("import/" + options.getState().getId() + "/state");

			Builder req = tgt.request();
			Response r = connector.get(req, monitor);
			StateDto state = toObj(r, StateDto.class, monitor);
			options.setState(state);
		} else {
			WebTarget tgt = target.path("import");
			tgt = tgt.queryParam("update", options.isUpdate());
			if (options.isUpdate())
				tgt = tgt.queryParam("skipUserUpdate", options.isSkipUserUpdates());
			tgt = tgt.queryParam("includeAccessEvents", options.isInclAccessEvents());
			tgt = tgt.queryParam("includeAuditEvents", options.isInclAuditEvents());
			tgt = tgt.queryParam("includeMonitoringEvents", options.isInclMonitorEvents());
			tgt = tgt.queryParam("includeServerSettings", options.isInclSrvSettings());

			File file = new File(options.getFile());

			Builder req = tgt.request();
			Response r = connector.post(req, Entity.entity(file, "application/zip"), monitor);
			StateDto state = toObj(r, StateDto.class, monitor);
			options.setState(state);
		}
		return options.getState();
	}

	@Override
	public StateDto exportMetaData(ExportOptions options, IProgressMonitor monitor) throws Exception {
		if (options.getState() != null) {
			WebTarget tgt = target.path("export/" + options.getState().getId() + "/state");

			Builder req = tgt.request();
			Response r = connector.get(req, monitor);
			StateDto state = toObj(r, StateDto.class, monitor);
			options.setState(state);
			if (state.getPhase().equals("finished")) {
				tgt = target.path("export/" + options.getState().getId() + "/export.zip");

				req = tgt.request();
				r = connector.get(req, monitor);
				monitor.subTask("Writing File: " + options.getFile());
				File file = new File(options.getFile());
				readFile(r, file, monitor);
			}
		} else {
			WebTarget tgt = target.path("export");

			ExportTaskDto taskDTO = new ExportTaskDto();
			List<String> parameters = options.getParameters();
			if (!parameters.isEmpty())
				taskDTO.setParameters(parameters);
			if (!options.getRoles().isEmpty())
				taskDTO.setRoles(options.getRoles());
			if (!options.getJobs().isEmpty())
				taskDTO.setScheduledJobs(options.getJobs());
			if (!options.getUsers().isEmpty())
				taskDTO.setUsers(options.getUsers());
			if (!options.getPaths().isEmpty())
				taskDTO.setUris(options.getPaths());

			Builder req = tgt.request();
			Response r = connector.post(req, Entity.entity(taskDTO, MediaType.APPLICATION_JSON_TYPE), monitor);
			StateDto state = toObj(r, StateDto.class, monitor);
			options.setState(state);
		}
		return options.getState();
	}

	@Override
	public List<RepositoryPermission> getPermissions(ResourceDescriptor rd, IProgressMonitor monitor, PermissionOptions options) throws Exception {
		WebTarget tgt = target.path("permissions" + rd.getUriString());
		tgt = tgt.queryParam("effectivePermissions", options.isEffectivePermissions());
		tgt = tgt.queryParam("recipientType", options.isRecipientTypeUser() ? "user" : "role");
		if (options.getRecipientId() != null)
			tgt = tgt.queryParam("recipientId", options.getRecipientId());
		tgt = tgt.queryParam("resolveAll", options.isResolveAll());

		Builder req = tgt.request();
		Response r = connector.get(req, monitor);
		RepositoryPermissionListWrapper state = toObj(r, RepositoryPermissionListWrapper.class, monitor);

		return state.getPermissions();
	}

	@Override
	public ClientUser getUser(IProgressMonitor monitor) throws Exception {
		String path = "";
		if (!Misc.isNullOrEmpty(sp.getOrganisation()))
			path += "organizations/" + sp.getOrganisation() + "/";
		path += "users/" + sp.getUser();
		WebTarget tgt = target.path(path);

		Builder req = tgt.request();
		Response r = connector.get(req, monitor);
		return toObj(r, ClientUser.class, monitor);
	}

	@Override
	public List<RepositoryPermission> setPermissions(ResourceDescriptor rd, List<RepositoryPermission> perms, PermissionOptions options, IProgressMonitor monitor) throws Exception {
		for (RepositoryPermission rp : perms) {
			// System.out.println(rp);
			WebTarget tgt = target.path("permissions" + rd.getUriString());
			tgt = tgt.matrixParam("recipient", rp.getRecipient());

			Builder req = tgt.request();
			if (rp.getMask() == -1) {
				try {
					Response r = connector.delete(req, monitor);
					toObj(r, String.class, monitor);
				} catch (HttpResponseException e) {
					if (e.getStatusCode() != 404 && e.getStatusCode() != 204)
						throw e;
				}
			} else if (rp.getUri() != null && rd.getUriString().equals(rp.getUri())) {
				Response r = connector.put(req, Entity.entity(rp, MediaType.APPLICATION_XML_TYPE), monitor);
				toObj(r, RepositoryPermission.class, monitor);
			}
		}
		return getPermissions(rd, monitor, options);
	}
}
