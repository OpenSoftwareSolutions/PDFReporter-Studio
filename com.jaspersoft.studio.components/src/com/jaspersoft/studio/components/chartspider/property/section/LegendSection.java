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

import net.sf.jasperreports.components.spiderchart.StandardChartSettings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class LegendSection extends AbstractSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSectionTitle(parent,
				Messages.LegendSection_0, true, 6, 1);
		section = (ExpandableComposite)group.getParent();

		GridData gd = new GridData();
		gd.horizontalSpan = 5;

		createWidget4Property(group, StandardChartSettings.PROPERTY_SHOW_LEGEND)
				.getControl().setLayoutData(gd);

		getWidgetFactory().createCLabel(group, Messages.LegendSection_1);
		createWidget4Property(group,
				StandardChartSettings.PROPERTY_LEGEND_POSITION, false);

		getWidgetFactory().createCLabel(group, Messages.LegendSection_2);
		createWidget4Property(group,
				StandardChartSettings.PROPERTY_LEGEND_COLOR, false);

		getWidgetFactory().createCLabel(group, Messages.LegendSection_3);
		createWidget4Property(group,
				StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR, false);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 6;
		createWidget4Property(group,
				StandardChartSettings.PROPERTY_LEGEND_FONT, false).getControl()
				.setLayoutData(gd);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(StandardChartSettings.PROPERTY_SHOW_LEGEND, Messages.MChart_show_legend);
		addProvidedProperties(StandardChartSettings.PROPERTY_LEGEND_POSITION, Messages.MChart_legend_position);
		addProvidedProperties(StandardChartSettings.PROPERTY_LEGEND_COLOR, Messages.MChart_legend_color);
		addProvidedProperties(StandardChartSettings.PROPERTY_LEGEND_BACKGROUND_COLOR, Messages.MChart_legend_background_color);
		addProvidedProperties(StandardChartSettings.PROPERTY_LEGEND_FONT, Messages.MChart_legend_font);
	}

}
