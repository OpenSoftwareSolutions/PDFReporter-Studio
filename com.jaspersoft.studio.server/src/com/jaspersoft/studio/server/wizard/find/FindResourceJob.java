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
package com.jaspersoft.studio.server.wizard.find;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.ServerProvider;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;

public class FindResourceJob {
	public static ResourceDescriptor doFindResource(MServerProfile msp, String[] in, String[] excl) {
		FindResourceWizard wizard = new FindResourceWizard(msp);
		wizard.setFilterTypes(in, excl);
		WizardDialog dialog = new FindWizardDialog(UIUtils.getShell(), wizard);
		dialog.setHelpAvailable(false);
		dialog.create();
		if (dialog.open() == Dialog.OK)
			return wizard.getValue();
		return null;
	}

	public static void doFindResource(ServerProvider sp, TreeViewer treeViewer) {
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		Object el = ts.getFirstElement();
		MServerProfile msp = null;
		if (el instanceof MServerProfile)
			msp = (MServerProfile) el;
		else if (el instanceof MResource) {
			INode n = ((MResource) el).getRoot();
			if (n != null && n instanceof MServerProfile)
				msp = (MServerProfile) n;
		}
		if (msp != null) {
			FindResourceWizard wizard = new FindResourceWizard(msp);
			WizardDialog dialog = new FindWizardDialog(UIUtils.getShell(), wizard);
			dialog.setHelpAvailable(false);
			dialog.create();
			if (dialog.open() == Dialog.OK) {
				ResourceDescriptor rd = wizard.getValue();
				if (rd != null)
					selectResource(sp, msp, rd, treeViewer);
			}
		}
	}

	public static void selectResource(final ServerProvider sp, final MServerProfile msp, final ResourceDescriptor rd, final TreeViewer treeViewer) {
		Job job = new Job(Messages.FindResourceJob_0) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				IStatus status = Status.OK_STATUS;
				try {
					final MResource mr = WSClientHelper.findSelected(monitor, rd, msp);
					if (mr != null) {
						UIUtils.getDisplay().asyncExec(new Runnable() {

							@Override
							public void run() {
								try {
									sp.setSkipLazyLoad(true);
									treeViewer.refresh(true);
									treeViewer.setSelection(new StructuredSelection(mr));
								} finally {
									sp.setSkipLazyLoad(false);
								}
							}
						});
					}
				} catch (Exception e) {
					UIUtils.showError(e);
				} finally {
					monitor.done();
				}
				return status;
			}
		};
		job.setPriority(Job.SHORT);
		job.setSystem(false);
		job.setUser(true);
		job.schedule();
	}

}
