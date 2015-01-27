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
package com.jaspersoft.studio.property.descriptor.propexpr.dialog;

import org.eclipse.jface.wizard.Wizard;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.propexpr.PropertyExpressionsDTO;

public class JRPropertyExpressionEditor extends Wizard {
	private PropertyExpressionsDTO value;
	private JRPropertyExpressionPage page0;

	public PropertyExpressionsDTO getValue() {
		if (page0 != null)
			return page0.getValue();
		return value;
	}

	public void setValue(PropertyExpressionsDTO value) {
		if (page0 != null)
			page0.setValue(value);
		this.value = value;
	}

	public JRPropertyExpressionEditor() {
		super();
		setWindowTitle(Messages.common_properties);
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		page0 = new JRPropertyExpressionPage("jrproperties"); //$NON-NLS-1$
		page0.setValue(value);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
