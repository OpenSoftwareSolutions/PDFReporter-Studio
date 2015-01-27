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
package com.jaspersoft.studio.server.model.server;

import java.io.IOException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.util.CastorUtil;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.server.ServerIconDescriptor;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.utils.Callback;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/* 
 * 
 * @author schicu
 *
 */
public class MServerProfile extends ANode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String MAPPINGFILE = "com/jaspersoft/studio/server/model/server/ServerProfileImpl.xml"; //$NON-NLS-1$

	public MServerProfile(ANode parent, ServerProfile server) {
		super(parent, -1);
		setValue(server);
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	@Override
	public ServerProfile getValue() {
		return (ServerProfile) super.getValue();
	}

	@Override
	public INode getRoot() {
		return this;
	}

	@Override
	public void setJasperConfiguration(JasperReportsConfiguration jConfig) {
		super.setJasperConfiguration(jConfig);
		if (getParent() != null)
			((ANode) getParent().getRoot()).setJasperConfiguration(jConfig);
	}

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ServerIconDescriptor("server"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		ServerProfile v = getValue();
		if (v != null && v.getName() != null && !v.getName().isEmpty())
			return v.getName();
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		ServerProfile v = getValue();
		if (v != null && v.getName() != null && !v.getName().isEmpty()) {
			String tt = v.getName();
			if (v.getUrl() != null)
				tt += "\n" + v.getUrl(); //$NON-NLS-1$
			if (v.getUser() != null)
				tt += Messages.MServerProfile_2 + v.getUser();
			String ci = getConnectionInfo();
			if (!Misc.isNullOrEmpty(ci))
				tt += "\n\n" + ci; //$NON-NLS-1$
			return tt;
		}
		return getIconDescriptor().getTitle();
	}

	public String getConnectionInfo() {
		String tt = ""; //$NON-NLS-1$
		if (wsClient != null) {
			try {
				ServerInfo info = wsClient.getServerInfo(null);
				tt += Messages.MServerProfile_5 + info.getVersion();
				tt += Messages.MServerProfile_6 + Misc.nvl(info.getEditionName()) + " " + (info.getEdition() != null ? info.getEdition() : ""); //$NON-NLS-2$ //$NON-NLS-3$
				tt += Messages.MServerProfile_9 + Misc.nvl(info.getBuild());
				tt += Messages.MServerProfile_10 + Misc.nvl(info.getLicenseType());
				tt += Messages.MServerProfile_11 + Misc.nvl(info.getExpiration());
				tt += Messages.MServerProfile_12 + Misc.nvl(info.getFeatures());
				tt += Messages.MServerProfile_13 + Misc.nvl(info.getDateFormatPattern());
				tt += Messages.MServerProfile_14 + Misc.nvl(info.getDatetimeFormatPattern());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return tt;
	}

	public String toXML() {
		return CastorUtil.write(getValue(), MAPPINGFILE);
	}

	private transient IConnection wsClient;

	public IConnection getWsClient(final Callback<IConnection> c) {
		if (wsClient == null) {
			Job job = new Job(Messages.MServerProfile_15) {
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask("", IProgressMonitor.UNKNOWN); //$NON-NLS-1$
					try {
						getWsClient(monitor);
					} catch (Exception e) {
						// UIUtils.showError(e);
					} finally {
						if (c != null) {
							UIUtils.getDisplay().asyncExec(new Runnable() {

								@Override
								public void run() {
									c.completed(wsClient);
								}
							});
						}
					}
					return Status.OK_STATUS;
				}
			};
			job.setPriority(Job.LONG);
			job.schedule();
		}
		return wsClient;
	}

	public IConnection getWsClient() {
		return wsClient;
	}

	public IConnection getWsClient(IProgressMonitor monitor) throws Exception {
		if (wsClient == null)
			WSClientHelper.connect(this, monitor);
		return wsClient;
	}

	public void setWsClient(IConnection wsClient) {
		this.wsClient = wsClient;
	}

	@Override
	public void setValue(Object value) {
		super.setValue(value);
		resetTmpPaths();
	}

	protected void resetTmpPaths() {
		tmpDir = null;
		AExporter.fileurimap.clear();
	}

	private transient IFolder tmpDir;

	public void setProjectPath(String projectPath) {
		getValue().setProjectPath(projectPath);
		resetTmpPaths();
	}

	public IFolder getTmpDir(IProgressMonitor monitor) throws IOException, CoreException {
		if (tmpDir == null || !tmpDir.exists()) {
			String prjpath = getValue().getProjectPath();
			if (prjpath != null && !prjpath.trim().isEmpty()) {
				String path = prjpath.trim();
				if (path.charAt(0) == '/')
					path = path.substring(1);
				int indx = path.indexOf("/"); //$NON-NLS-1$
				String ppath = indx >= 0 ? path.substring(0, indx) : path;
				String fpath = indx >= 0 ? path.substring(indx) : "/"; //$NON-NLS-1$

				IProject prj = ResourcesPlugin.getWorkspace().getRoot().getProject(ppath);
				tmpDir = prj.getFolder(fpath);
			} else
				tmpDir = FileUtils.getInProjectFolder(FileUtils.createTempDir(getValue().getName().replace(" ", "") + "-").toURI(), monitor); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (!tmpDir.exists())
				tmpDir.create(true, true, monitor);
		}
		return tmpDir;
	}

	public boolean isSupported(Feature f) {
		IConnection c = getWsClient();
		if (c != null)
			return c.isSupported(f);
		return false;
	}
}
