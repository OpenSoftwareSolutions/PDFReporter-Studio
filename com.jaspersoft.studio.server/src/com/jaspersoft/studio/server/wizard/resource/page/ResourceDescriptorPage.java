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
package com.jaspersoft.studio.server.wizard.resource.page;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.server.messages.Messages;

public class ResourceDescriptorPage extends WizardPage {

	public ResourceDescriptorPage() {
		super(Messages.ResourceDescriptorPage_id);
		setTitle(Messages.ResourceDescriptorPage_title);
		setDescription(Messages.ResourceDescriptorPage_description);
	}

	public void createControl(Composite parent) {
		setControl(new Button(parent, SWT.ARROW_DOWN));
	}
}
