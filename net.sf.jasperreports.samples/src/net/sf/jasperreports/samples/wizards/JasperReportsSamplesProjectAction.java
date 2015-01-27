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
package net.sf.jasperreports.samples.wizards;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;

/**
 * Action to open the wizard for the creation of the JasperReports Sample
 * Project.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class JasperReportsSamplesProjectAction extends Action {

	@Override
	public void run() {
		SampleNewWizard wizard = new SampleNewWizard();
		wizard.init(PlatformUI.getWorkbench(), StructuredSelection.EMPTY);
		WizardDialog dialogToOpen = new WizardDialog(UIUtils.getShell(), wizard);
		dialogToOpen.create();
		dialogToOpen.open();
	}

}
