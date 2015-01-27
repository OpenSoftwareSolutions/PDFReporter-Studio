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

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.MResource;

public class DeleteResourceAction extends Action {
	private TreeViewer treeViewer;

	public DeleteResourceAction(TreeViewer treeViewer) {
		super();
		setId(ActionFactory.DELETE.getId());
		setAccelerator(SWT.DEL);
		setText(Messages.common_delete);
		setToolTipText(Messages.common_delete);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		setImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE));
		setDisabledImageDescriptor(sharedImages.getImageDescriptor(ISharedImages.IMG_TOOL_DELETE_DISABLED));
		this.treeViewer = treeViewer;
	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		boolean b = firstElement != null && (firstElement instanceof MResource);
		if (b) {
			MResource mres = (MResource) firstElement;
			int pmask = mres.getValue().getPermissionMask(mres.getWsClient());
			b = b && (pmask == 1 || (pmask & 16) == 16);
		}
		return b;
	}

	@Override
	public void run() {
		TreeSelection s = (TreeSelection) treeViewer.getSelection();
		final TreePath[] p = s.getPaths();
		if (!UIUtils.showDeleteConfirmation())
			return;
		ProgressMonitorDialog pm = new ProgressMonitorDialog(UIUtils.getShell());
		try {
			pm.run(true, true, new IRunnableWithProgress() {
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask("Deleting", p.length);
					try {
						for (int i = 0; i < p.length; i++) {
							final Object obj = p[i].getLastSegment();
							if (obj instanceof MResource) {
								try {
									monitor.subTask(((MResource) obj).getDisplayText());
									WSClientHelper.deleteResource(monitor, (MResource) obj);
									UIUtils.getDisplay().asyncExec(new Runnable() {

										public void run() {
											treeViewer.refresh(true);
										}
									});
								} catch (Throwable e) {
									UIUtils.showError(e);
								}
							}
						}
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
}
