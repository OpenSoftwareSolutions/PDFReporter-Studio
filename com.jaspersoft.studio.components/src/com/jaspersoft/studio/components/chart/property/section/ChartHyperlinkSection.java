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
package com.jaspersoft.studio.components.chart.property.section;

import net.sf.jasperreports.charts.design.JRDesignCategorySeries;
import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.charts.design.JRDesignGanttSeries;
import net.sf.jasperreports.charts.design.JRDesignHighLowDataset;
import net.sf.jasperreports.charts.design.JRDesignPieDataset;
import net.sf.jasperreports.charts.design.JRDesignPieSeries;
import net.sf.jasperreports.charts.design.JRDesignTimePeriodSeries;
import net.sf.jasperreports.charts.design.JRDesignTimeSeries;
import net.sf.jasperreports.charts.design.JRDesignXySeries;
import net.sf.jasperreports.charts.design.JRDesignXyzSeries;

import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.components.chart.model.dataset.MChartHighLowDataset;
import com.jaspersoft.studio.components.chart.model.dataset.MChartPieDataset;
import com.jaspersoft.studio.components.chart.model.series.category.MCategorySeries;
import com.jaspersoft.studio.components.chart.model.series.gantt.MGanttSeries;
import com.jaspersoft.studio.components.chart.model.series.pie.MPieSeries;
import com.jaspersoft.studio.components.chart.model.series.time.MTimeSeries;
import com.jaspersoft.studio.components.chart.model.series.timeperiod.MTimePeriodSeries;
import com.jaspersoft.studio.components.chart.model.series.xyseries.MXYSeries;
import com.jaspersoft.studio.components.chart.model.series.xyzseries.MXYZSeries;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.obj.HyperlinkSection;

public class ChartHyperlinkSection extends HyperlinkSection {
	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MChartAxes)
			return (APropertyNode) md
					.getPropertyValue(JRDesignChartAxis.PROPERTY_CHART);
		if (md instanceof MChartHighLowDataset)
			return (APropertyNode) md
					.getPropertyValue(JRDesignHighLowDataset.PROPERTY_ITEM_HYPERLINK);
		if (md instanceof MChartPieDataset)
			return (APropertyNode) md
					.getPropertyValue(JRDesignPieDataset.PROPERTY_OTHER_SECTION_HYPERLINK);
		if (md instanceof MCategorySeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignCategorySeries.PROPERTY_ITEM_HYPERLINK);
		if (md instanceof MGanttSeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignGanttSeries.PROPERTY_ITEM_HYPERLINK);
		if (md instanceof MPieSeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignPieSeries.PROPERTY_SECTION_HYPERLINK);
		if (md instanceof MTimeSeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignTimeSeries.PROPERTY_ITEM_HYPERLINK);
		if (md instanceof MTimePeriodSeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignTimePeriodSeries.PROPERTY_ITEM_HYPERLINK);
		if (md instanceof MXYSeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignXySeries.PROPERTY_ITEM_HYPERLINK);
		if (md instanceof MXYZSeries)
			return (APropertyNode) md
					.getPropertyValue(JRDesignXyzSeries.PROPERTY_ITEM_HYPERLINK);
		return md;
	}
}
