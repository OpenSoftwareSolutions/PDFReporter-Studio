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
import net.sf.jasperreports.engine.base.JRBaseChartPlot;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.components.chart.model.plot.MChartPlot;
import com.jaspersoft.studio.components.chart.property.section.plot.PlotFactory;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class ChartPlotSection extends AbstractRealValueSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));
		createCommon(parent);

		PlotFactory.createPlot(this, parent, tabbedPropertySheetPage);
	}
	

	public void createCommon(Composite parent) {
		Composite group = getWidgetFactory().createComposite(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		group.setLayoutData(gd);
		group.setLayout(new GridLayout(3, false));

		getWidgetFactory().createCLabel(group, Messages.MChartPlot_backcolor);

		createWidget4Property(group, JRBaseChartPlot.PROPERTY_BACKCOLOR, false)
				.getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA)
				.getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA)
				.getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, JRBaseChartPlot.PROPERTY_SERIES_COLORS)
				.getControl().setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, JRBaseChartPlot.PROPERTY_ORIENTATION)
				.getControl().setLayoutData(gd);
		;
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseChartPlot.PROPERTY_BACKCOLOR, Messages.MChartPlot_backcolor);
		addProvidedProperties(JRBaseChartPlot.PROPERTY_FOREGROUND_ALPHA, Messages.MChartPlot_foreground_alpha_percent);
		addProvidedProperties(JRBaseChartPlot.PROPERTY_BACKGROUND_ALPHA, Messages.MChartPlot_background_alpha_percent);
		addProvidedProperties(JRBaseChartPlot.PROPERTY_SERIES_COLORS, Messages.MChartPlot_series_colors);
		addProvidedProperties(JRBaseChartPlot.PROPERTY_ORIENTATION, Messages.MChartPlot_orientation);
	}

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MChartAxes) {
			md = (APropertyNode) md
					.getPropertyValue(JRDesignChartAxis.PROPERTY_CHART);
		}
		if (md instanceof MChart) {
			MChartPlot chartplot = (MChartPlot) md
					.getPropertyValue(MChart.PLOTPROPERTY);
			return chartplot;
		}
		return md;
	}

}
