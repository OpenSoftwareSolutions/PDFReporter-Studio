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
package com.jaspersoft.studio.server.action.server;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.wizard.ServerProfileWizard;
import com.jaspersoft.studio.server.wizard.ServerProfileWizardDialog;

public class EditServerAction extends Action {
	public static final String ID = "editServerAction"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public EditServerAction(TreeViewer treeViewer) {
		super();
		this.treeViewer = treeViewer;
		setId(ID);
		setText(Messages.EditServerAction_title);
		setDescription(Messages.EditServerAction_desc);
		setToolTipText(Messages.EditServerAction_desc);
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/server--pencil.png")); //$NON-NLS-1$
	}

	@Override
	public boolean isEnabled() {
		Object firstElement = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		return firstElement != null && (firstElement instanceof MServerProfile);
	}

	@Override
	public void run() {
		Object obj = ((TreeSelection) treeViewer.getSelection()).getFirstElement();
		if (obj instanceof MServerProfile) {
			MServerProfile mspold = (MServerProfile) obj;

			ServerProfileWizard wizard = new ServerProfileWizard(ServerManager.getMServerProfileCopy(mspold));
			ServerProfileWizardDialog dialog = new ServerProfileWizardDialog(UIUtils.getShell(), wizard);
			wizard.bindTestButton(dialog);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				MServerProfile msprof = wizard.getServerProfile();
				mspold.setValue(msprof.getValue());
				mspold.setWsClient(null);

				ServerManager.saveServerProfile(mspold);
				fillServerProfile(mspold, treeViewer);

			}

		}
	}

	public static void fillServerProfile(final MServerProfile mspold, final TreeViewer treeViewer) {
		Job job = new Job(Messages.EditServerAction_jobname) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					monitor.beginTask(Messages.EditServerAction_taskname, IProgressMonitor.UNKNOWN);
					mspold.removeChildren();
					new MDummy(mspold);
					showSelection();
					WSClientHelper.connectGetData(mspold, monitor);
					showSelection();
				} catch (InvocationTargetException e) {
					UIUtils.showError(e);
					return Status.CANCEL_STATUS;
				} catch (Exception e) {
					UIUtils.showError(e);
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}

			private void showSelection() {
				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						treeViewer.refresh(true);
						TreeSelection s = (TreeSelection) treeViewer.getSelection();
						if (!s.isEmpty()) {
							TreePath[] p = s.getPaths();
							treeViewer.expandToLevel(p[0], 1);
						}
					}
				});
			}
		};
		job.setSystem(false);
		job.schedule();
	}
}
