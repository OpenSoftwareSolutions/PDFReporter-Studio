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
package com.jaspersoft.studio.components.chart.model.dataset;

import net.sf.jasperreports.charts.design.JRDesignCategoryDataset;
import net.sf.jasperreports.charts.design.JRDesignGanttDataset;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodDataset;
import net.sf.jasperreports.charts.design.JRDesignTimeSeriesDataset;
import net.sf.jasperreports.charts.design.JRDesignValueDataset;
import net.sf.jasperreports.charts.design.JRDesignXyDataset;
import net.sf.jasperreports.charts.design.JRDesignXyzDataset;
import net.sf.jasperreports.engine.JRChartDataset;
import net.sf.jasperreports.engine.design.JRDesignChart;

import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.dataset.MElementDataset;

public class ChartDatasetFactory {
	public static MElementDataset getChartDataset(ANode parent,
			JRChartDataset jrObject, int newIndex) {
		if (jrObject == null)
			return null;
		if (parent instanceof MChart) {
			JRDesignChart chart = (JRDesignChart) parent.getValue();
			if (chart.getChartType() == JRDesignChart.CHART_TYPE_MULTI_AXIS)
				return null;
		}
		if (jrObject instanceof JRDesignCategoryDataset)
			return new MChartCategoryDataset(parent,
					(JRDesignCategoryDataset) jrObject,
					parent.getJasperDesign());
		if (jrObject instanceof JRDesignGanttDataset)
			return new MChartGanttDataset(parent,
					(JRDesignGanttDataset) jrObject, parent.getJasperDesign());
		if (jrObject instanceof JRDesignHighLowDataset)
			return new MChartHighLowDataset(parent,
					(JRDesignHighLowDataset) jrObject, parent.getJasperDesign());
		if (jrObject instanceof JRDesignPieDataset)
			return new MChartPieDataset(parent, (JRDesignPieDataset) jrObject,
					parent.getJasperDesign());
		if (jrObject instanceof JRDesignTimePeriodDataset)
			return new MChartTimePeriodDataset(parent,
					(JRDesignTimePeriodDataset) jrObject,
					parent.getJasperDesign());
		if (jrObject instanceof JRDesignTimeSeriesDataset)
			return new MChartTimeSeriesDataset(parent,
					(JRDesignTimeSeriesDataset) jrObject,
					parent.getJasperDesign());
		if (jrObject instanceof JRDesignValueDataset)
			return new MChartValueDataset(parent,
					(JRDesignValueDataset) jrObject, parent.getJasperDesign());
		if (jrObject instanceof JRDesignXyDataset)
			return new MChartXYDataset(parent, (JRDesignXyDataset) jrObject,
					parent.getJasperDesign());
		if (jrObject instanceof JRDesignXyzDataset)
			return new MChartXYZDataset(parent, (JRDesignXyzDataset) jrObject,
					parent.getJasperDesign());

		return new MChartDataset(parent, jrObject, parent.getJasperDesign());
	}
}
