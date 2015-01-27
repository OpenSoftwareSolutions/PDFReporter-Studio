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
package com.jaspersoft.studio.utils;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AContributorAction extends Action {

	protected JasperReportsConfiguration jrConfig;

	public AContributorAction(String id, String text) {
		super();
		setId(id);
		setText(text);
	}

	public void setJrConfig(JasperReportsConfiguration jrConfig) {
		this.jrConfig = jrConfig;
	}

	protected JasperDesign getJasperDesignCopy() throws JRException {
		return ModelUtils.copyJasperDesign(jrConfig, jrConfig.getJasperDesign());
	}
}
