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
package com.jaspersoft.studio.server.wizard.imp;

import java.lang.reflect.InvocationTargetException;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.restv2.ARestV2Connection;

public class ImportMetadataWizard extends Wizard {
	private ImportMetadataPage page0;
	private IConnection conn;

	public ImportMetadataWizard(IConnection conn) {
		super();
		setWindowTitle(Messages.FindResourceWizard_0);
		setNeedsProgressMonitor(true);
		this.conn = conn;
	}

	@Override
	public void addPages() {
		page0 = new ImportMetadataPage();
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		try {
			getContainer().run(true, true, new IRunnableWithProgress() {

				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
					monitor.beginTask(Messages.ImportMetadataWizard_0, IProgressMonitor.UNKNOWN);
					try {
						ImportOptions opt = page0.getValue();
						while (opt.getState() == null || opt.getState().getPhase().equals("inprogress")) { //$NON-NLS-1$
							StateDto state = conn.importMetaData(opt, monitor);

							monitor.setTaskName(state.getMessage());
							if (monitor.isCanceled())
								break;
						}
						if (opt.getState() != null)
							if (opt.getState().getErrorDescriptor() != null)
								UIUtils.showInformation(((ARestV2Connection) conn).getEh().buildMessage(monitor, "", opt.getState().getErrorDescriptor())); //$NON-NLS-1$
							else
								UIUtils.showInformation(opt.getState().getMessage());
					} catch (Exception e) {
						UIUtils.showError(e);
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
		return true;
	}
}
