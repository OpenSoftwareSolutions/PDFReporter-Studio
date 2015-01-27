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
package com.jaspersoft.studio.doc.handlers;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.studio.doc.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.publish.wizard.Publish2ServerWizard;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * This class start the upload of a report into an instance of JasperReports
 * Server
 * 
 * @author Orlandin Marco
 * 
 */
public class UploadReportCheatAction extends Action {

	@Override
	public void run() {
		ANode rootElement = HandlersUtil.getRootElement();
		if (rootElement != null) {
			JasperDesign design = rootElement.getJasperDesign();
			JasperReportsConfiguration config = rootElement.getJasperConfiguration();
			ANode servers = HandlersUtil.getServers();
			if (servers != null && servers.getChildren().size() > 0) {
				// Create a fake root to show only the server connection in the list
				Publish2ServerWizard wizard = new Publish2ServerWizard(design, config, 1);
				WizardDialog dialogToOpen = new WizardDialog(UIUtils.getShell(), wizard);
				dialogToOpen.create();
				dialogToOpen.open();
			} else
				MessageDialog.openWarning(UIUtils.getShell(), Messages.UploadReportCheatAction_no_server_title, Messages.UploadReportCheatAction_no_server_warning);
		} else
			MessageDialog.openWarning(UIUtils.getShell(), Messages.UploadReportCheatAction_warning_title, Messages.UploadReportCheatAction_warning_message);
	}

}
