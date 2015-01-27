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
package com.jaspersoft.studio.property.descriptor.subreport.parameter.dialog;

import net.sf.jasperreports.engine.JRSubreportParameter;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.wizards.JSSWizard;

public class SubreportPropertyEditor extends JSSWizard {
	private JRSubreportParameter[] value;
	private SubreportPropertyPage page0;

	public JRSubreportParameter[] getValue() {
		if (page0 != null)
			return page0.getValue();
		return value;
	}

	public void setValue(JRSubreportParameter[] value, MSubreport msubrpt) {
		setConfig(msubrpt.getJasperConfiguration());
		if (page0 != null)
			page0.setValue(value);
		this.value = value;
	}

	public SubreportPropertyEditor() {
		super();
		setWindowTitle(Messages.common_properties);
		setNeedsProgressMonitor(false);
	}

	@Override
	public void addPages() {
		page0 = new SubreportPropertyPage("subreportproperties"); //$NON-NLS-1$
		page0.setValue(value);
		addPage(page0);
	}

	@Override
	public boolean performFinish() {
		return true;
	}

}
