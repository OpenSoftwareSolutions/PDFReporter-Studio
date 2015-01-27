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

import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.components.spiderchart.StandardChartSettings;
import net.sf.jasperreports.engine.design.JRDesignChart;

import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.components.chartspider.model.MSpiderChart;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.graphic.BookmarkSection;

/**
 * 
 * This class extends the bookmark section to provide the correct property ids
 * for the chart elements
 * 
 * @author Orlandin Marco
 * 
 */
public class ChartBookmarkSection extends BookmarkSection {

	@Override
	public String getAnchorNameProperty() {
		if (getElement() instanceof MSpiderChart){
			return StandardChartSettings.PROPERTY_ANCHOR_NAME_EXPRESSION;	
		} else if (getElement().getPropertyDescriptor(JRDesignChart.PROPERTY_ANCHOR_NAME_EXPRESSION) != null){
			return JRDesignChart.PROPERTY_ANCHOR_NAME_EXPRESSION;
		} else {
			return null;
		}
	}

	@Override
	public String getBookmarkLevelProperty() {
		if (getElement() instanceof MSpiderChart){
			return StandardChartSettings.PROPERTY_BOOKMARK_LEVEL;
		} else if (getElement().getPropertyDescriptor(JRDesignChart.PROPERTY_BOOKMARK_LEVEL) != null){
			return JRDesignChart.PROPERTY_BOOKMARK_LEVEL;
		} else return null;
	}

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MChartAxes)
			return (APropertyNode) md.getPropertyValue(JRDesignChartAxis.PROPERTY_CHART);
		return md;
	}
}
