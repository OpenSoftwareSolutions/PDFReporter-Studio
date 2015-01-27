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
package com.jaspersoft.studio.components.chart.property.section.theme;

import net.sf.jasperreports.chartthemes.simple.ChartSettings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class ChartSettingsSection extends AbstractSection {
	
	private ExpandableComposite section0;
	
	private ExpandableComposite section1;
	
	private ExpandableComposite section2;
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		Composite group = getWidgetFactory().createComposite(parent);
		group.setLayout(new GridLayout());
		createWidget4Property(group, ChartSettings.PROPERTY_antiAlias, false);
		createWidget4Property(group, ChartSettings.PROPERTY_textAntiAlias, false);

		group = getWidgetFactory().createSection(parent, com.jaspersoft.studio.messages.Messages.common_borders, true, 2);
		section0 = (ExpandableComposite)group.getParent();
		
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(group, ChartSettings.PROPERTY_borderVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, ChartSettings.PROPERTY_borderPaint);
		createWidget4Property(group, ChartSettings.PROPERTY_borderStroke);

		group = getWidgetFactory().createSection(parent, com.jaspersoft.studio.messages.Messages.common_background, true, 3);
		section1 = (ExpandableComposite)group.getParent();
		
		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, ChartSettings.PROPERTY_backgroundPaint).getControl().setLayoutData(gd);
		createWidget4Property(group, ChartSettings.PROPERTY_backgroundImage);
		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(group, ChartSettings.PROPERTY_backgroundImageAlignment).getControl().setLayoutData(gd);
		createWidget4Property(group, ChartSettings.PROPERTY_backgroundImageAlpha);

		Composite paddingSection = PadUtil.createWidgets4Property(parent, "", com.jaspersoft.studio.messages.Messages.common_padding, this);
		section2 = (ExpandableComposite)paddingSection.getParent();
	}
	
	private void expandSection(ExpandableComposite section){
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (propertyId.equals(ChartSettings.PROPERTY_borderVisible)
				|| propertyId.equals(ChartSettings.PROPERTY_borderPaint)
					|| propertyId.equals(ChartSettings.PROPERTY_borderStroke)) expandSection(section0);
		else if (propertyId.equals(ChartSettings.PROPERTY_backgroundPaint)
				|| propertyId.equals(ChartSettings.PROPERTY_backgroundImage)
					|| propertyId.equals(ChartSettings.PROPERTY_backgroundImageAlignment)
						|| propertyId.equals(ChartSettings.PROPERTY_backgroundImageAlpha)) expandSection(section1);
		else if (propertyId.equals(PadUtil.PADDING_TOP)
				|| propertyId.equals(PadUtil.PADDING_BOTTOM)
					|| propertyId.equals(PadUtil.PADDING_LEFT)
						|| propertyId.equals(PadUtil.PADDING_RIGHT)) expandSection(section2);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(ChartSettings.PROPERTY_antiAlias, Messages.MChartSettings_antiAliasTitle);
		addProvidedProperties(ChartSettings.PROPERTY_textAntiAlias, Messages.MChartSettings_textAATitle);
		addProvidedProperties(ChartSettings.PROPERTY_borderVisible, Messages.MChartSettings_borderVisibleTitle);
		addProvidedProperties(ChartSettings.PROPERTY_borderPaint, Messages.MChartSettings_borderColorTitle);
		addProvidedProperties(ChartSettings.PROPERTY_borderStroke, Messages.MChartSettings_borderStrokeTitle);
		addProvidedProperties(ChartSettings.PROPERTY_backgroundPaint, Messages.MChartSettings_paintTitle);
		addProvidedProperties(ChartSettings.PROPERTY_backgroundImage, Messages.MChartSettings_backgroundImageTitle);
		addProvidedProperties(ChartSettings.PROPERTY_backgroundImageAlignment, Messages.MChartSettings_imageAlignTitle);
		addProvidedProperties(ChartSettings.PROPERTY_backgroundImageAlpha, Messages.MChartSettings_imageAlphaTitle);
		addProvidedProperties(PadUtil.PADDING_TOP, com.jaspersoft.studio.messages.Messages.common_top);
		addProvidedProperties(PadUtil.PADDING_BOTTOM, com.jaspersoft.studio.messages.Messages.common_bottom);
		addProvidedProperties(PadUtil.PADDING_LEFT, com.jaspersoft.studio.messages.Messages.common_left);
		addProvidedProperties(PadUtil.PADDING_RIGHT, com.jaspersoft.studio.messages.Messages.common_right);
	}
}
