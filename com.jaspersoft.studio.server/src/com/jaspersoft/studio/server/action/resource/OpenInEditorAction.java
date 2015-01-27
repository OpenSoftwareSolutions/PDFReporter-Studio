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
package com.jaspersoft.studio.server.action.resource;

import java.io.FileNotFoundException;
import java.io.IOException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.WorkspaceJob;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.editor.JRSEditorContributor;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.export.ImageExporter;
import com.jaspersoft.studio.server.export.JrxmlExporter;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MJar;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.utils.SelectionHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class OpenInEditorAction extends Action {
	private static final String ID = "OPENINEDITOR"; //$NON-NLS-1$
	protected TreeViewer treeViewer;
	private boolean openInEditor = true;

	public OpenInEditorAction(TreeViewer treeViewer, boolean openInEditor) {
		this(treeViewer);
		this.openInEditor = openInEditor;

	}

	public OpenInEditorAction(TreeViewer treeViewer) {
		super();
		setId(ID);
		setText(Messages.OpenInEditorAction_title);
		setDescription(Messages.OpenInEditorAction_desc);
		setToolTipText(Messages.OpenInEditorAction_desc);
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && isDataResource();
	}

	private boolean isDataResource() {
		final TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		for (int i = 0; i < p.length; i++) {
			if (!isFileResource(p[i].getLastSegment()))
				return false;
		}
		return true;
	}

	protected boolean isFileResource(Object obj) {
		return (obj != null && (obj instanceof AFileResource && !(obj instanceof MReportUnit)));
	}

	protected boolean preDownload(AFileResource fres) {
		return true;
	}

	@Override
	public void run() {
		final TreeSelection s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		for (int i = 0; i < p.length; i++) {
			final Object obj = p[i].getLastSegment();
			if (isFileResource(obj)) {
				if (preDownload((AFileResource) obj)) {
					WorkspaceJob job = new WorkspaceJob("Open File In Editor") {
						public IStatus runInWorkspace(IProgressMonitor monitor) throws CoreException {
							try {
								monitor.beginTask("Open File In Editor", IProgressMonitor.UNKNOWN);
								dorun(obj, monitor);
							} catch (Throwable e) {
								UIUtils.showError(e);
							} finally {
								monitor.done();
							}
							return Status.OK_STATUS;
						}
					};
					job.setUser(true);
					job.schedule();
				}
				break;
			}
		}
	}

	protected IPath path;

	protected void dorun(final Object obj, IProgressMonitor monitor) throws Exception, FileNotFoundException, IOException {
		if (isFileResource(obj)) {
			AFileResource res = (AFileResource) obj;
			ResourceDescriptor rd = WSClientHelper.getResource(new NullProgressMonitor(), res, res.getValue());
			ANode parent = res.getParent();
			int index = parent.getChildren().indexOf(res);
			parent.removeChild(res);
			res = (AFileResource) ResourceFactory.getResource(parent, rd, index);
			WSClientHelper.fireResourceChanged(res);

			String fkeyname = ServerManager.getKey(res);
			if (fkeyname == null)
				return;
			String type = rd.getWsType();
			IFile f = null;
			if (type.equals(ResourceDescriptor.TYPE_JRXML)) {
				IFile file = new JrxmlExporter(path).exportToIFile(res, rd, fkeyname, monitor);
				if (file != null) {
					JasperReportsConfiguration.getDefaultJRConfig(file).getPrefStore().setValue(JRSEditorContributor.KEY_PUBLISH2JSS_SILENT, true);
					openEditor(file);
				}
				if (res.getParent() instanceof MReportUnit) {
					for (INode n : res.getParent().getChildren()) {
						if (n instanceof MJar) {
							MJar mjar = (MJar) n;
							fkeyname = ServerManager.getKey(mjar);
							rd = WSClientHelper.getResource(new NullProgressMonitor(), mjar, mjar.getValue());
							f = new AExporter(path).exportToIFile(mjar, rd, fkeyname, monitor);
							if (f != null)
								PublishUtil.savePath(f, mjar);
						}
					}
				}
				return;
			} else if (type.equals(ResourceDescriptor.TYPE_IMAGE))
				f = new ImageExporter(path).exportToIFile(res, rd, fkeyname, monitor);
			else
				f = new AExporter(path).exportToIFile(res, rd, fkeyname, monitor);

			if (f != null) {
				PublishUtil.savePath(f, res);
				openEditor(f);
			}
			path = null;
		}
	}

	private void openEditor(final IFile f) {
		if (!openInEditor)
			return;
		UIUtils.getDisplay().asyncExec(new Runnable() {

			public void run() {
				SelectionHelper.openEditor(f);
			}
		});
	}

}
