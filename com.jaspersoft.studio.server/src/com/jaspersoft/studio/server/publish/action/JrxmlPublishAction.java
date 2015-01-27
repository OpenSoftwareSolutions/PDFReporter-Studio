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
package com.jaspersoft.studio.server.publish.action;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AMJrxmlContainer;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.publish.FindResources;
import com.jaspersoft.studio.server.publish.Publish;
import com.jaspersoft.studio.server.publish.PublishUtil;
import com.jaspersoft.studio.server.publish.wizard.Publish2ServerWizard;
import com.jaspersoft.studio.utils.AContributorAction;

public class JrxmlPublishAction extends AContributorAction {
	private static final String ID = "PUBLISHJRXML"; //$NON-NLS-1$

	private int startpage = 1;
	private IProgressMonitor monitor;
	private boolean silent = false;

	public JrxmlPublishAction(int startpage, IProgressMonitor monitor) {
		this();
		this.startpage = startpage;
		this.monitor = monitor;
	}

	public JrxmlPublishAction() {
		super(ID, Messages.JrxmlPublishAction_title);
		setToolTipText(Messages.JrxmlPublishAction_tooltip);
		setImageDescriptor(Activator.getDefault().getImageDescriptor("icons/server--upload.png")); //$NON-NLS-1$
	}

	public void setSilent(boolean silent) {
		this.silent = silent;
	}

	@Override
	public void run() {
		try {
			if (monitor != null)
				doRun(monitor);
			else {
				Job job = new Job(Messages.FindReportUnit_jobname) {
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						monitor.beginTask("Publishing the Report", IProgressMonitor.UNKNOWN);
						try {
							status = doRun(monitor);
						} finally {
							monitor.done();
						}
						return status;
					}
				};
				job.setPriority(Job.LONG);
				job.schedule();
			}
		} catch (Exception e) {
			UIUtils.showError(e);
		}
	}

	private IStatus status = Status.OK_STATUS;

	private IStatus doRun(final IProgressMonitor monitor) {
		monitor.subTask(Messages.FindReportUnit_jobname);
		try {
			final JasperDesign jd = getJasperDesignCopy();
			UIUtils.getDisplay().syncExec(new Runnable() {

				public void run() {
					status = publishReportUnit(jd, startpage, monitor);
				}
			});
		} catch (Exception e) {
			UIUtils.showError(e);
		}
		return status;
	}

	public IStatus publishReportUnit(JasperDesign jd, int startpage, IProgressMonitor monitor) {
		IStatus status = Status.CANCEL_STATUS;
		IFile file = (IFile) jrConfig.get(FileUtils.KEY_FILE);
		try {
			if (silent) {
				// let's look if server exists, and url exists
				MServerProfile msrv = ServerManager.getServerProfile(jd, jrConfig);
				if (msrv != null) {
					msrv.setJasperConfiguration(jrConfig);
					ANode n = FindResources.findReportUnit(msrv, monitor, jd, file);
					n.setJasperConfiguration(jrConfig);
					if (n != null && n instanceof AMJrxmlContainer) {
						// let's check if there are new resources?
						try {
							boolean showdialog = false;
							List<?> resources = FindResources.findResources(monitor, (AMJrxmlContainer) n, jd);
							if (resources != null) {
								for (Object obj : resources) {
									if (obj instanceof MResource) {
										MResource mres = (MResource) obj;
										PublishUtil.loadPreferences(monitor, file, mres);
										if (mres.getPublishOptions().getOverwrite() == null) {
											showdialog = true;
											break;
										}
									}
								}
							}
							if (!showdialog) {
								// publish
								new Publish(jrConfig).publish((AMJrxmlContainer) n, jd, monitor);
								return Status.OK_STATUS;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			}
			
			Publish2ServerWizard wizard = new Publish2ServerWizard(jd, jrConfig, startpage);
			WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
			if (dialog.open() == Dialog.OK) {
				// ANode node = wizard.getNode();
				// if (node instanceof AMJrxmlContainer)
				// status = new Publish(jrConfig).publish((AMJrxmlContainer)
				// node, jd,
				// monitor);
				status = Status.OK_STATUS;
			}
		} finally {
			jrConfig.init(file);
		}
		return status;
	}
}
