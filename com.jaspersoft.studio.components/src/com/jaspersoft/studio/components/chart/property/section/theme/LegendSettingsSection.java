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

import net.sf.jasperreports.chartthemes.simple.LegendSettings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class LegendSettingsSection extends AbstractSection {
	
	private ExpandableComposite paddingSection;
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		Composite group = getWidgetFactory().createComposite(parent);
		group.setLayout(new GridLayout(2, false));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, LegendSettings.PROPERTY_showLegend, false).getControl().setLayoutData(gd);
		createWidget4Property(group, LegendSettings.PROPERTY_position);
		createWidget4Property(group, LegendSettings.PROPERTY_horizontalAlignment);
		createWidget4Property(group, LegendSettings.PROPERTY_verticalAlignment);
		createWidget4Property(group, LegendSettings.PROPERTY_foregroundPaint);
		createWidget4Property(group, LegendSettings.PROPERTY_backgroundPaint);

		Composite paddingComposite = PadUtil.createWidgets4Property(parent, LegendSettings.PROPERTY_blockFrame, "Block Frame", this);
		paddingSection = (ExpandableComposite)paddingComposite.getParent();
	}
	
	private void expandSection(ExpandableComposite section){
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (propertyId.toString().startsWith(LegendSettings.PROPERTY_blockFrame)) expandSection(paddingSection);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(LegendSettings.PROPERTY_showLegend, Messages.MLegendSettings_showLegendTitle);
		addProvidedProperties(LegendSettings.PROPERTY_position, Messages.MLegendSettings_positionTitle);
		addProvidedProperties(LegendSettings.PROPERTY_horizontalAlignment, Messages.MLegendSettings_legendHAlignTitle);
		addProvidedProperties(LegendSettings.PROPERTY_verticalAlignment, Messages.MLegendSettings_legendVAlignTitle);
		addProvidedProperties(LegendSettings.PROPERTY_foregroundPaint, Messages.MLegendSettings_legendForegroundColorTitle);
		addProvidedProperties(LegendSettings.PROPERTY_backgroundPaint, Messages.MLegendSettings_legendBackgroundColorTitle);
		addProvidedProperties(LegendSettings.PROPERTY_blockFrame+PadUtil.PADDING_TOP, com.jaspersoft.studio.messages.Messages.common_top);
		addProvidedProperties(LegendSettings.PROPERTY_blockFrame+PadUtil.PADDING_BOTTOM, com.jaspersoft.studio.messages.Messages.common_bottom);
		addProvidedProperties(LegendSettings.PROPERTY_blockFrame+PadUtil.PADDING_LEFT, com.jaspersoft.studio.messages.Messages.common_left);
		addProvidedProperties(LegendSettings.PROPERTY_blockFrame+PadUtil.PADDING_RIGHT, com.jaspersoft.studio.messages.Messages.common_right);
	}
}
