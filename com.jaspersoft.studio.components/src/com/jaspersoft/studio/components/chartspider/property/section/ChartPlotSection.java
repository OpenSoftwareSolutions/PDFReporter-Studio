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
package com.jaspersoft.studio.components.chartspider.property.section;

import net.sf.jasperreports.components.spiderchart.StandardSpiderPlot;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class ChartPlotSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));
		
		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_BACKCOLOR);
		createWidget4Property(parent,
				StandardSpiderPlot.PROPERTY_BACKGROUND_ALPHA);
		createWidget4Property(parent,
				StandardSpiderPlot.PROPERTY_FOREGROUND_ALPHA);
		createWidget4Property(parent,
				StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR);
		createWidget4Property(parent,
				StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH);

		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_LABEL_COLOR);
		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_LABEL_FONT);

		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_LABEL_GAP);

		createWidget4Property(parent,
				StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION);
		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_WEB_FILLED);
		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_TABLE_ORDER);

		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_START_ANGLE);
		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_HEAD_PERCENT);
		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_INTERIOR_GAP);

		createWidget4Property(parent, StandardSpiderPlot.PROPERTY_ROTATION);
	}
	
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(StandardSpiderPlot.PROPERTY_BACKCOLOR, Messages.MChartPlot_backcolor);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_BACKGROUND_ALPHA, Messages.MChartPlot_background_alpha_percent);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_FOREGROUND_ALPHA, Messages.MChartPlot_foreground_alpha_percent);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_AXIS_LINE_COLOR, Messages.MSpiderChart_axisLineColorTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_AXIS_LINE_WIDTH, Messages.MSpiderChart_axisLineWidthTitle);
		
		addProvidedProperties(StandardSpiderPlot.PROPERTY_MAX_VALUE_EXPRESSION, Messages.MSpiderChart_maxValueExpTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_WEB_FILLED, Messages.MSpiderChart_webFilledTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_TABLE_ORDER, Messages.MSpiderChart_tableOrderTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_START_ANGLE, Messages.MSpiderChart_startAngleTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_HEAD_PERCENT, Messages.MSpiderChart_headPercentTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_INTERIOR_GAP, Messages.MSpiderChart_interiorGapTitle);
		addProvidedProperties(StandardSpiderPlot.PROPERTY_ROTATION, Messages.MChart_title_position);
	}

}
