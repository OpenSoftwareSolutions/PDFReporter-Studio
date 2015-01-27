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
package com.jaspersoft.studio.property.descriptor.properties.dialog;

import net.sf.jasperreports.engine.JRPropertiesMap;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.messages.Messages;

public class JRPropertyEditor extends Wizard {
	private JRPropertiesMap value;
	private JRPropertyPage page0;

	public JRPropertiesMap getValue() {
		if (page0 != null)
			return page0.getValue();
		return value;
	}

	public void setValue(JRPropertiesMap value) {
		if (page0 != null)
			page0.setValue(value);
		this.value = value;
	}

	public JRPropertyEditor() {
		super();
		setWindowTitle(Messages.common_properties);
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		page0 = new JRPropertyPage("jrproperties"); //$NON-NLS-1$
		page0.setValue(value);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
