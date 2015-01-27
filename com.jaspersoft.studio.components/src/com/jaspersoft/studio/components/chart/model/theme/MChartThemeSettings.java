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
package com.jaspersoft.studio.components.chart.model.theme;

import net.sf.jasperreports.chartthemes.simple.ChartThemeSettings;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MRoot;

public class MChartThemeSettings extends ANode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MChartThemeSettings(MRoot root, ChartThemeSettings cts) {
		super(root, -1);
		setValue(cts);
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public ChartThemeSettings getValue() {
		return (ChartThemeSettings) super.getValue();
	}

	@Override
	public String getDisplayText() {
		return "Chart Theme";
	}

}
