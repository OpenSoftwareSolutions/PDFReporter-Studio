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
package com.jaspersoft.studio.server.publish;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.data.DataAdapterParameterContributorFactory;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.apache.http.client.HttpResponseException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.model.AMJrxmlContainer;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class Publish {
	private JasperReportsConfiguration jrConfig;
	private List<String> resources = new ArrayList<String>();

	public Publish(JasperReportsConfiguration jrConfig) {
		this.jrConfig = jrConfig;
	}

	public IStatus publish(AMJrxmlContainer node, JasperDesign jd, IProgressMonitor monitor) {
		try {
			publishResources(monitor, jd, node);
			if (monitor.isCanceled())
				return Status.CANCEL_STATUS;
			// UIUtils.showInformation(Messages.JrxmlPublishAction_successpublishing);

			String str = "Success!\nThe following resources where published on the JasperReports Server:\n";
			for (String mres : resources)
				str += mres + "\n";
			UIUtils.showInformation(str);

			// refresh
			jrConfig.get(PublishUtil.KEY_PUBLISH2JSS_DATA);
			// clean
			jrConfig.remove(PublishUtil.KEY_PUBLISH2JSS_DATA);
			if (node instanceof MJrxml)
				postProcessLocal((MJrxml) node);
			else if (node instanceof MReportUnit) {
				for (INode n : node.getChildren())
					if (n instanceof MJrxml) {
						postProcessLocal((MJrxml) n);
						break;
					}
			}
			ServerManager.selectIfExists(monitor, node);
		} catch (Exception e) {
			UIUtils.showError(e);
		}
		return Status.OK_STATUS;
	}

	private IStatus publishResources(IProgressMonitor monitor, JasperDesign jd, AMJrxmlContainer parent) throws Exception {
		MReportUnit mrunit = null;
		MJrxml jrxml = null;
		if (parent instanceof MReportUnit) {
			mrunit = (MReportUnit) parent;
			jrxml = new MJrxml(mrunit, PublishUtil.getMainReport(monitor, mrunit, jd), 0);
		} else if (parent.getParent() instanceof MReportUnit) {
			jrxml = (MJrxml) parent;
			mrunit = (MReportUnit) parent.getParent();
		} else if (parent.getParent() instanceof MFolder) {
			jrxml = (MJrxml) parent;
		} else
			return Status.CANCEL_STATUS;

		File file = FileUtils.createTempFile("jrsres", FileExtension.PointJRXML); //$NON-NLS-1$ 
		String version = ServerManager.getVersion(Misc.nvl(mrunit, jrxml));
		ResourceDescriptor rdjrxml = jrxml.getValue();
		if (rdjrxml.getParentFolder() != null && !rdjrxml.getParentFolder().endsWith("_files"))
			rdjrxml.setIsReference(true);

		List<MResource> resources = ((JasperReportsConfiguration) jrConfig).get(PublishUtil.KEY_PUBLISH2JSS_DATA, new ArrayList<MResource>());
		updSelectedResources(monitor, resources, version);
		FileUtils.writeFile(file, JRXmlWriterHelper.writeReport(jrConfig, jd, version));
		jrxml.setFile(file);

		IFile ifile = (IFile) jrConfig.get(FileUtils.KEY_FILE);
		PublishUtil.savePreferences(ifile, resources);

		if (mrunit != null && !jrxml.getValue().getIsReference()) {
			ResourceDescriptor oldRU = mrunit.getValue();
			ResourceDescriptor r = mrunit.getValue();
			try {
				r = mrunit.getWsClient().get(monitor, mrunit.getValue(), null);
				mrunit.setValue(r);
			} catch (HttpResponseException e) {
				if (e.getStatusCode() != 404)
					throw e;
			} catch (Exception e) {
			}
			// setup datasource
			ResourceDescriptor ds = null;
			for (ResourceDescriptor rd : oldRU.getChildren()) {
				if (rd != null && SelectorDatasource.isDatasource(rd)) {
					ds = rd;
					ds.setDirty(false);
					break;
				}
			}
			ResourceDescriptor newDs = null;
			for (ResourceDescriptor rd : r.getChildren()) {
				if (rd != null && SelectorDatasource.isDatasource(rd)) {
					newDs = rd;
					break;
				}
			}
			if (newDs != null)
				r.getChildren().remove(newDs);
			if (ds != null)
				r.getChildren().add(0, ds);

			// setup main jrxml
			boolean isMain = true;
			for (ResourceDescriptor rd : r.getChildren()) {
				if (rd.getUriString() == null)
					continue;
				String wsType = rd.getWsType();
				if (rd.getUriString().equals(rdjrxml.getUriString()) && (wsType.equals(ResourceDescriptor.TYPE_JRXML) || wsType.equals(ResourceDescriptor.TYPE_REFERENCE))) {
					isMain = rd.isMainReport();
					break;
				}
			}
			rdjrxml.setMainReport(isMain);
			PublishUtil.setChild(r, rdjrxml);
			for (MResource res : resources) {
				if (res.getPublishOptions().isOverwrite()) {
					ResourceDescriptor rd = res.getValue();
					if (rd.getData() != null && !rd.getParentFolder().endsWith("_files")) {
						mrunit.getWsClient().addOrModifyResource(monitor, rd, null);
					} else
						PublishUtil.setChild(r, rd);
				}
			}
			mrunit.getWsClient().addOrModifyResource(monitor, r, file);
			this.resources.add(r.getUriString());
			for (MResource res : resources)
				if (res.getPublishOptions().isOverwrite()) {
					PublishUtil.savePreferencesNoOverwrite(ifile, res);
					this.resources.add(res.getValue().getUriString());
				}
		} else {
			jrxml.setValue(saveResource(monitor, jrxml));
			for (MResource res : resources) {
				PublishOptions popt = res.getPublishOptions();
				if (popt.isOverwrite()) {
					saveResource(monitor, res);
					PublishUtil.savePreferencesNoOverwrite(ifile, res);
				}
				if (monitor.isCanceled())
					return Status.CANCEL_STATUS;
			}
		}
		return Status.OK_STATUS;
	}

	protected void updSelectedResources(IProgressMonitor monitor, List<MResource> files, String version) throws IOException, Exception {
		for (MResource res : files) {
			PublishOptions popt = res.getPublishOptions();
			if (popt.isOverwrite()) {
				if (popt.getjExpression() != null) {
					if (popt.getPublishMethod() == ResourcePublishMethod.REWRITEEXPRESSION)
						popt.getjExpression().setText(popt.getRepoExpression());
					else if (popt.getPublishMethod() == ResourcePublishMethod.LOCAL)
						popt.getjExpression().setText(popt.getExpression());
				} else if (popt.getDataset() != null) {
					String dauri = res.getValue().getUriString();
					if (popt.getPublishMethod() != null)
						if (popt.getPublishMethod() == ResourcePublishMethod.REFERENCE)
							dauri = popt.getReferencedResource().getUriString();
						else if (popt.getPublishMethod() == ResourcePublishMethod.RESOURCE) {
							if (popt.getReferencedResource() == null)
								continue;
							ResourceDescriptor rd = res.getValue();
							dauri = popt.getReferencedResource().getUriString() + rd.getName();
						}
					popt.getDataset().setProperty(DataAdapterParameterContributorFactory.PROPERTY_DATA_ADAPTER_LOCATION, "repo:" + dauri);
				}
				if (popt.getPublishMethod() != null)
					if (popt.getPublishMethod() == ResourcePublishMethod.REFERENCE) {
						ResourceDescriptor rd = res.getValue();
						ResourceDescriptor ref = new ResourceDescriptor();
						ref.setName(rd.getName());
						ref.setIsNew(true);
						ref.setLabel(rd.getLabel());
						ref.setDescription(rd.getDescription());
						ref.setIsReference(true);
						ref.setReferenceUri(popt.getReferencedResource().getUriString());
						ref.setParentFolder(rd.getParentFolder());
						ref.setUriString(rd.getUriString());
						ref.setWsType(rd.getWsType());// ResourceDescriptor.TYPE_REFERENCE);

						res.setValue(ref);
					} else if (popt.getPublishMethod() == ResourcePublishMethod.RESOURCE) {
						if (popt.getReferencedResource() == null)
							continue;
						ResourceDescriptor rd = res.getValue();
						rd.setParentFolder(popt.getReferencedResource().getUriString());
						rd.setUriString(rd.getParentFolder() + "/" + rd.getName());
					} else if (popt.getPublishMethod() == ResourcePublishMethod.REWRITEEXPRESSION) {
						;
					} else if (res instanceof MJrxml) {
						MJrxml mJrxml = (MJrxml) res;
						FileUtils.writeFile(mJrxml.getFile(), JRXmlWriterHelper.writeReport(jrConfig, mJrxml.getJd(), version));
					}
			}
		}
	}

	private ResourceDescriptor saveResource(IProgressMonitor monitor, MResource mres) throws Exception {
		String uri = mres.getValue().getUriString();
		ResourceDescriptor rd = WSClientHelper.save(monitor, mres);
		if (rd != null)
			resources.add(Misc.nvl(uri, rd.getUriString()));
		return rd;
	}

	private void postProcessLocal(final MJrxml node) {
		UIUtils.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				JasperDesign rpt = jrConfig.getJasperDesign();
				INode n = node.getRoot();
				if (n != null && n instanceof MServerProfile) {
					MServerProfile server = (MServerProfile) n;
					ServerProfile v = server.getValue();
					rpt.setProperty(AExporter.PROP_SERVERURL, v.getUrl());
					rpt.setProperty(AExporter.PROP_USER, v.getUser() + (v.getOrganisation() != null ? "|" + v.getOrganisation() : ""));
				}
				ResourceDescriptor rd = node.getValue();
				if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT))
					for (Object r : rd.getChildren())
						if (((ResourceDescriptor) r).getWsType().equals(ResourceDescriptor.TYPE_JRXML)) {
							rd = (ResourceDescriptor) r;
							break;
						}
				rpt.setProperty(AExporter.PROP_REPORTRESOURCE, rd.getUriString());
				if (node.getParent() instanceof MReportUnit) {
					MReportUnit mrunit = (MReportUnit) node.getParent();
					rpt.setProperty(AExporter.PROP_REPORTUNIT, mrunit.getValue().getUriString());
				}
				try {
					IFile iFile = (IFile) jrConfig.get(FileUtils.KEY_FILE);
					AExporter.setServerLocation(node, iFile);
					PublishUtil.savePath(iFile, node);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
