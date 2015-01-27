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
package com.jaspersoft.studio.community.translationSender;

import java.io.File;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.community.wizards.TranslationSendWizard;
import com.jaspersoft.translation.resources.ISendTranslation;

/**
 * Extend the translation plugin to provide the action to publish a translation 
 * project on the community forum
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationCommunitySender implements ISendTranslation{

	/**
	 * Method called from the translation plugin when the send translation option
	 * is selected on a translation project
	 * 
	 * @param translationArchive java.io.File that point to a zip file with inside all the files of the project 
	 * of the translation to publish. The file is created into a temp. directory
	 * 
	 * @return true if the publish was done, false otherwise
	 */
	@Override
	public boolean sendTranslation(File translationArchive) {
		TranslationSendWizard newWizard = new TranslationSendWizard(translationArchive);
		newWizard.setNeedsProgressMonitor(true);
		Shell newShell = new Shell(UIUtils.getDisplay());
		WizardDialog translationSend = new WizardDialog(newShell, newWizard){
			@Override
			protected void setShellStyle(int newShellStyle) {
				super.setShellStyle(SWT.SHELL_TRIM | SWT.MODELESS);
			}
		};
		translationSend.open();
		// Dispose the shell after the dialog has been closed
		newShell.dispose();
		return true;
	}

}
