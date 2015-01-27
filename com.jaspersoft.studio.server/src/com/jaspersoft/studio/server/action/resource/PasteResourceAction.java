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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.IConnection;

public class PasteResourceAction extends Action {
	protected TreeViewer treeViewer;
	private TreeSelection s;
	protected Object contents;

	public PasteResourceAction(TreeViewer treeViewer) {
		super();
		setId(ActionFactory.PASTE.getId());
		setText(Messages.common_paste);
		setToolTipText(Messages.common_paste);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_PASTE_DISABLED));
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		boolean res = super.isEnabled();
		boolean iscut = false;
		contents = Clipboard.getDefault().getContents();
		if (res && contents != null && contents instanceof List<?>) {
			List<?> list = (List<?>) contents;
			ANode parent = getSelected();
			res = false;
			for (Object obj : list)
				if (obj instanceof MResource && obj instanceof ICopyable) {
					ICopyable c = (ICopyable) obj;
					if (c.isCopyable2(parent)) {
						iscut = ((MResource) obj).isCut();
						res = true;
						break;
					}
				}
		}
		if (res) {
			Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
			res = firstElement != null;
			if (res) {
				if (firstElement instanceof MResource) {
					MResource mres = (MResource) firstElement;
					int pmask = mres.getValue().getPermissionMask(mres.getWsClient());
					res = res && (pmask == 1 || (pmask & 8) == 8);
				}
			}
		}
		return res;
	}

	protected ANode getSelected() {
		s = (TreeSelection) treeViewer.getSelection();
		TreePath[] p = s.getPaths();
		for (int i = 0; i < p.length; i++) {
			Object obj = p[i].getLastSegment();
			if (obj instanceof MResource || obj instanceof MServerProfile)
				return (ANode) obj;
		}
		return null;
	}

	@Override
	public void run() {
		final ANode parent = getSelected();
		final List<?> list = (List<?>) Clipboard.getDefault().getContents();
		if (list == null)
			return;
		ProgressMonitorDialog pm = new ProgressMonitorDialog(UIUtils.getShell());
		try {
			pm.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					try {
						INode root = parent.getRoot();
						final String puri = parent instanceof MResource ? ((MResource) parent).getValue().getUriString() : ""; //$NON-NLS-1$
						doWork(monitor, parent, list);
						ANode p = parent;
						if (parent instanceof MResource)
							p = new ModelVisitor<ANode>(root) {

								@Override
								public boolean visit(INode n) {
									if (n instanceof MResource) {
										MResource mres = (MResource) n;
										if (mres.getValue() != null && mres.getValue().getUriString().equals(puri)) {
											setObject(mres);
											stop();
										}
									}
									return true;
								}
							}.getObject();
						if (!p.getChildren().isEmpty())
							p = (ANode) p.getChildren().get(0);
						s = new TreeSelection(new TreePath(new Object[] { p }));

						UIUtils.getDisplay().asyncExec(new Runnable() {

							public void run() {
								treeViewer.refresh(true);
								treeViewer.setSelection(s);
							}
						});
					} catch (Throwable e) {
						throw new InvocationTargetException(e);
					} finally {
						monitor.done();
					}
				}

			});
		} catch (InvocationTargetException e) {
			UIUtils.showError(e.getCause());
		} catch (InterruptedException e) {
			UIUtils.showError(e);
		}
	}

	private boolean existsAll = false;
	private boolean exists = false;
	private PasteDialog d;

	private void doWork(IProgressMonitor monitor, ANode parent, List<?> list) throws Exception {
		MServerProfile sp = (MServerProfile) parent.getRoot();
		String dURI = ((MResource) parent).getValue().getUriString();
		IConnection ws = sp.getWsClient(monitor);
		Set<ANode> toRefresh = new HashSet<ANode>();

		monitor.beginTask(Messages.PasteResourceAction_1 + dURI, list.size());
		for (Object obj : list) {
			if (obj instanceof MResource && obj instanceof ICopyable) {
				final MResource m = (MResource) obj;
				if (m.isCopyable2(parent)) {
					ResourceDescriptor origin = m.getValue();

					exists = false;
					ResourceDescriptor rd = new ResourceDescriptor();
					try {
						rd.setName(origin.getName());
						rd.setUriString(dURI + "/" + origin.getName());
						rd.setWsType(origin.getWsType());
						if (parent instanceof MReportUnit)
							rd.setUriString(rd.getUriString() + "_files");
						if (ws.get(monitor, rd, null) != null)
							exists = true;
					} catch (Exception e) {
					}
					if (exists && !existsAll) {
						UIUtils.getDisplay().syncExec(new Runnable() {

							@Override
							public void run() {
								d = new PasteDialog(UIUtils.getShell(), m);
								d.open();
							}
						});
						existsAll = d.getForAll();
						switch (d.getChoise()) {
						case PasteDialog.REPLACE:
							break;
						case PasteDialog.SKIP:
							continue;
						case PasteDialog.COPY:
							// create new resource
							break;
						}
					}
					if (origin.isMainReport())
						m.setCut(false);
					if (m.isCut())
						toRefresh.add(m.getParent());
					if (!isSameServer(parent, m)) {
						IConnection mc = m.getWsClient();
						File file = FileUtils.createTempFile("tmp", "file"); //$NON-NLS-1$ //$NON-NLS-2$
						try {
							origin = mc.get(monitor, origin, file);
							origin.setData(Base64.encodeBase64(net.sf.jasperreports.eclipse.util.FileUtils.getBytes(file)));
							origin.setHasData(origin.getData() != null);
						} catch (Throwable e) {
							file = null;
						}
						if (parent instanceof MFolder) {
							origin.setUriString(dURI + "/" + origin.getName()); //$NON-NLS-1$
							ws.addOrModifyResource(monitor, origin, file);
						} else if (parent instanceof MReportUnit)
							saveToReportUnit(monitor, parent, ws, origin);
						deleteIfCut(monitor, m);
					} else if (parent instanceof MFolder) {
						if (m.isCut()) {
							ws.move(monitor, origin, dURI);
							m.setCut(false);
						} else
							ws.copy(monitor, origin, dURI);
					} else if (parent instanceof MReportUnit) {
						if (!(m.getParent() instanceof MFolder) && m.getParent() instanceof MResource) {
							if (origin.getParentFolder() != null && !origin.getParentFolder().endsWith("_files")) //$NON-NLS-1$
								origin.setIsReference(true);
						}
						saveToReportUnit(monitor, parent, ws, origin);
						deleteIfCut(monitor, m);
					}
				}
			}
			monitor.worked(1);
			if (monitor.isCanceled())
				break;
		}
		toRefresh.add(parent);
		for (ANode n : toRefresh)
			refreshNode(n, monitor);
	}

	protected void deleteIfCut(IProgressMonitor monitor, MResource m) throws Exception {
		if (m.isCut()) {
			m.setCut(false);
			WSClientHelper.deleteResource(monitor, m);
		}
	}

	protected void saveToReportUnit(IProgressMonitor monitor, ANode parent, IConnection ws, ResourceDescriptor origin) throws IOException, Exception {
		ResourceDescriptor prd = (ResourceDescriptor) parent.getValue();
		ResourceDescriptor rd = null;
		File file = null;
		if (origin.getIsReference()) {
			rd = new ResourceDescriptor();
			rd.setName(origin.getName());
			rd.setLabel(origin.getLabel());
			rd.setDescription(origin.getDescription());
			rd.setIsNew(true);
			rd.setIsReference(true);
			rd.setReferenceUri(origin.getUriString());
			rd.setParentFolder(prd.getParentFolder() + "/" + prd.getName() + "_files"); //$NON-NLS-1$ //$NON-NLS-2$
			if (ResourceFactory.isFileResourceType(origin))
				rd.setWsType(ResourceDescriptor.TYPE_REFERENCE);
			else
				rd.setWsType(origin.getWsType());
			rd.setUriString(prd.getParentFolder() + "/" + prd.getName() + "_files/" + prd.getName()); //$NON-NLS-1$ //$NON-NLS-2$
		} else {
			file = FileUtils.createTempFile("tmp", "file"); //$NON-NLS-1$ //$NON-NLS-2$

			try {
				rd = ws.get(monitor, origin, file);
				rd.setData(Base64.encodeBase64(net.sf.jasperreports.eclipse.util.FileUtils.getBytes(file)));
				rd.setHasData(rd.getData() != null);

				rd = doPasteIntoReportUnit(prd, rd);
			} catch (Throwable e) {
				file = null;
			}
		}
		prd.getChildren().add(rd);
		ws.addOrModifyResource(monitor, prd, null);
	}

	private boolean isSameServer(ANode parent, MResource m) {
		IConnection mc = m.getWsClient();
		IConnection pc = null;
		if (parent instanceof MResource)
			pc = ((MResource) parent).getWsClient();
		else if (parent instanceof MServerProfile)
			pc = ((MServerProfile) parent).getWsClient();
		if (pc != null && mc != null)
			return mc.getServerProfile().getUrl().equals(pc.getServerProfile().getUrl());

		return true;
	}

	protected ResourceDescriptor doPasteIntoReportUnit(ResourceDescriptor prd, ResourceDescriptor origin) {
		String ruuri = prd.getUriString();
		origin.setParentFolder(ruuri + "_files"); //$NON-NLS-1$
		origin.setUriString(ruuri + "_files/" + origin.getName()); //$NON-NLS-1$
		origin.setIsNew(true);
		origin.setName(getRName(origin.getName(), prd.getChildren()));
		origin.setLabel(origin.getLabel());
		origin.setMainReport(false);
		return origin;
	}

	private String getRName(String name, List<?> children) {
		String n = name;
		int j = 0;
		for (int i = 0; i < children.size(); i++) {
			ResourceDescriptor r = (ResourceDescriptor) children.get(i);
			if (n.equals(r.getName())) {
				n = name + "_" + j; //$NON-NLS-1$
				i = 0;
				j++;
			}
		}
		return n;
	}

	private void refreshNode(INode p, IProgressMonitor monitor) throws Exception {
		if (p instanceof MResource)
			WSClientHelper.refreshResource((MResource) p, monitor);
		else if (p instanceof MServerProfile) {
			WSClientHelper.listFolder(((MServerProfile) p), ((MServerProfile) p).getWsClient(monitor), Messages.PasteResourceAction_15, monitor, 2);
		}
	}
}
