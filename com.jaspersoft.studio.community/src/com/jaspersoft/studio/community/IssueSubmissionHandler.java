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
package com.jaspersoft.studio.community;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.community.messages.Messages;
import com.jaspersoft.studio.community.wizards.IssueCreationWizard;

/**
 * Creates the Issue submission dialog and opens it. 
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class IssueSubmissionHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IssueCreationWizard newWizard = IssueCreationWizard.createWizard();
		if(newWizard!=null){
			newWizard.setNeedsProgressMonitor(true);
			Shell mainShell = UIUtils.getShell();
			WizardDialog issueCreationDialog = new WizardDialog(mainShell, newWizard){
				@Override
				protected void setShellStyle(int newShellStyle) {
					super.setShellStyle(SWT.SHELL_TRIM | SWT.MODELESS);
				}
			};
			issueCreationDialog.open();
		}
		else {
			MessageDialog.openWarning(UIUtils.getShell(),
					Messages.IssueSubmissionHandler_WarningTitle,
					Messages.IssueSubmissionHandler_WarningMsg);
		}
		return null;
	}

}
