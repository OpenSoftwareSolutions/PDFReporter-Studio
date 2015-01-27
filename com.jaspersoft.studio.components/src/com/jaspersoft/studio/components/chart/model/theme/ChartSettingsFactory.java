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
import net.sf.jasperreports.engine.design.JasperDesign;

import com.jaspersoft.studio.model.MRoot;

public class ChartSettingsFactory {
	public static MRoot createModel(ChartThemeSettings cts) {
		MRoot root = new MRoot(null, new JasperDesign());
		MChartThemeSettings n = new MChartThemeSettings(root, cts);
		new MChartSettings(n, cts.getChartSettings());
		new MTitleSettings(n, cts.getTitleSettings(), "Title");
		new MTitleSettings(n, cts.getSubtitleSettings(), "Subtitle");
		new MLegendSettings(n, cts.getLegendSettings());
		new MPlotSettings(n, cts.getPlotSettings());
		new MAxisSettings(n, cts.getDomainAxisSettings(), "Domain Axis");
		new MAxisSettings(n, cts.getRangeAxisSettings(), "Range Axis");
		return root;
	}
}
