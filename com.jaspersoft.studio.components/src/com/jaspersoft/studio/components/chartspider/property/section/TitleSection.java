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
public class TitleSection extends AbstractSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSectionTitle(parent, Messages.TitleSection_0,
				true, 4, 1);
		section = (ExpandableComposite)group.getParent();

		getWidgetFactory().createCLabel(group, Messages.TitleSection_1);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(group,
				StandardChartSettings.PROPERTY_TITLE_EXPRESSION, false)
				.getControl().setLayoutData(gd);

		createWidget4Property(group,
				StandardChartSettings.PROPERTY_TITLE_POSITION);

		createWidget4Property(group, StandardChartSettings.PROPERTY_TITLE_COLOR);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		createWidget4Property(group, StandardChartSettings.PROPERTY_TITLE_FONT,
				false).getControl().setLayoutData(gd);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(StandardChartSettings.PROPERTY_TITLE_EXPRESSION, Messages.MChart_title_expression);
		addProvidedProperties(StandardChartSettings.PROPERTY_TITLE_POSITION, Messages.MChart_title_position);
		addProvidedProperties(StandardChartSettings.PROPERTY_TITLE_COLOR, Messages.MChart_title_color);
		addProvidedProperties(StandardChartSettings.PROPERTY_TITLE_FONT, Messages.MChart_title_font);
	}

}
