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
import net.sf.jasperreports.engine.base.JRBaseChart;
import net.sf.jasperreports.engine.design.JRDesignChart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class LegendSection extends AbstractRealValueSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent,
				com.jaspersoft.studio.messages.Messages.common_legend, true, 2);
		section = (ExpandableComposite)group.getParent();
		createWidget4Property(group, JRBaseChart.PROPERTY_SHOW_LEGEND);

		getWidgetFactory().createCLabel(group,
				com.jaspersoft.studio.messages.Messages.LegendSection_Position_Label);
		createWidget4Property(group, JRBaseChart.PROPERTY_LEGEND_POSITION,
				false);

		GridData gd = new GridData();
		gd.horizontalSpan = 2;

		Composite colorComposite = new Composite(group, SWT.NONE);
		colorComposite.setLayout(new GridLayout(4, false));
		colorComposite.setLayoutData(gd);

		getWidgetFactory().createCLabel(colorComposite,
				com.jaspersoft.studio.messages.Messages.LegendSection_Forecolor_Label);
		createWidget4Property(colorComposite,
				JRBaseChart.PROPERTY_LEGEND_COLOR, false);

		getWidgetFactory().createCLabel(colorComposite,
				com.jaspersoft.studio.messages.Messages.LegendSection_Backcolor_Label);
		createWidget4Property(colorComposite,
				JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR, false);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, JRDesignChart.PROPERTY_LEGEND_FONT, false)
				.getControl().setLayoutData(gd);
	}
	
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseChart.PROPERTY_SHOW_LEGEND, Messages.MChart_show_legend);
		addProvidedProperties(JRBaseChart.PROPERTY_LEGEND_POSITION, Messages.MChart_legend_position);
		addProvidedProperties(JRBaseChart.PROPERTY_LEGEND_COLOR, Messages.MChart_legend_color);
		addProvidedProperties(JRBaseChart.PROPERTY_LEGEND_BACKGROUND_COLOR, Messages.MChart_legend_background_color);
		addProvidedProperties(JRDesignChart.PROPERTY_LEGEND_FONT, Messages.MChart_legend_font);
	}

	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MChartAxes)
			return (APropertyNode) md
					.getPropertyValue(JRDesignChartAxis.PROPERTY_CHART);
		return md;
	}

}
