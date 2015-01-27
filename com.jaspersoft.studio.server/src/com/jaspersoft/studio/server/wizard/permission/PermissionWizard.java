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
package com.jaspersoft.studio.server.wizard.permission;

import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;

public class PermissionWizard extends Wizard implements SelectionListener {
	private MResource res;
	private PermissionPage page0;

	public PermissionWizard(MResource res) {
		super();
		setWindowTitle(Messages.PermissionWizard_0 + res.getValue().getUriString());
		setNeedsProgressMonitor(true);
		setHelpAvailable(false);
		this.res = res;
	}

	@Override
	public void addPages() {
		page0 = new PermissionPage(res);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		page0.setPermissions();
	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		widgetSelected(e);
	}

}
