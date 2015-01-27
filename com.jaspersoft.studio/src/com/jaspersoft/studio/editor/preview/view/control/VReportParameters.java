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
package com.jaspersoft.studio.editor.preview.view.control;

import net.sf.jasperreports.engine.JRParameter;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.messages.MessagesByKeys;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class VReportParameters extends VParameters {

	public VReportParameters(Composite parent, JasperReportsConfiguration jContext) {
		super(parent, jContext);
		isSystem = true;
	}

	@Override
	protected boolean isParameterToShow(JRParameter p) {
		return p.isSystemDefined() && !p.getName().equals(JRParameter.REPORT_TEMPLATES)
				&& !p.getName().equals(JRParameter.SORT_FIELDS) && !p.getName().equals(JRParameter.REPORT_PARAMETERS_MAP);
	}

	@Override
	protected void setupLabel(Label lbl, IParameter pres) {
		lbl.setText(MessagesByKeys.getString(pres.getLabel()));
	}
}
