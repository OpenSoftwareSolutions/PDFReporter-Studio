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

import net.sf.jasperreports.chartthemes.simple.TitleSettings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class TitleSettingsSection extends AbstractSection {
	
	private ExpandableComposite paddingSection;
	
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		Composite group = getWidgetFactory().createComposite(parent);
		group.setLayout(new GridLayout(2, false));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, TitleSettings.PROPERTY_showTitle, false).getControl().setLayoutData(gd);
		createWidget4Property(group, TitleSettings.PROPERTY_position);
		createWidget4Property(group, TitleSettings.PROPERTY_horizontalAlignment);
		createWidget4Property(group, TitleSettings.PROPERTY_verticalAlignment);
		createWidget4Property(group, TitleSettings.PROPERTY_foregroundPaint);
		createWidget4Property(group, TitleSettings.PROPERTY_backgroundPaint);

		Composite paddingComposite = PadUtil.createWidgets4Property(parent, "", com.jaspersoft.studio.messages.Messages.common_padding, this);
		paddingSection = (ExpandableComposite)paddingComposite.getParent();
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(TitleSettings.PROPERTY_showTitle, Messages.MTitleSettings_showTitleTitle);
		addProvidedProperties(TitleSettings.PROPERTY_position, Messages.MTitleSettings_positionTitle);
		addProvidedProperties(TitleSettings.PROPERTY_horizontalAlignment, Messages.MTitleSettings_horizontalAlignmentTitle);
		addProvidedProperties(TitleSettings.PROPERTY_verticalAlignment, Messages.MTitleSettings_verticalAlignementTitle);
		addProvidedProperties(TitleSettings.PROPERTY_foregroundPaint, Messages.MTitleSettings_foregroundColorTitle);
		addProvidedProperties(TitleSettings.PROPERTY_backgroundPaint, Messages.MTitleSettings_backgroundColorTitle);

		addProvidedProperties(PadUtil.PADDING_TOP, com.jaspersoft.studio.messages.Messages.common_top);
		addProvidedProperties(PadUtil.PADDING_BOTTOM, com.jaspersoft.studio.messages.Messages.common_bottom);
		addProvidedProperties(PadUtil.PADDING_LEFT, com.jaspersoft.studio.messages.Messages.common_left);
		addProvidedProperties(PadUtil.PADDING_RIGHT, com.jaspersoft.studio.messages.Messages.common_right);
	}

	private void expandSection(ExpandableComposite section){
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (propertyId.equals(PadUtil.PADDING_TOP)
				|| propertyId.equals(PadUtil.PADDING_BOTTOM)
					|| propertyId.equals(PadUtil.PADDING_LEFT)
						|| propertyId.equals(PadUtil.PADDING_RIGHT)) expandSection(paddingSection);
	}
}
