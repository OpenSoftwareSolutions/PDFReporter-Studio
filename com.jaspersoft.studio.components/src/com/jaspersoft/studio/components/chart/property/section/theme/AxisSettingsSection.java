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

import net.sf.jasperreports.chartthemes.simple.AxisSettings;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.theme.MAxisSettings;
import com.jaspersoft.studio.components.chart.model.theme.util.PadUtil;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class AxisSettingsSection extends AbstractSection {
	@Override
	public void createControls(Composite parent, TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		Composite group = getWidgetFactory().createComposite(parent);
		group.setLayout(new GridLayout(2, false));

		createWidget4Property(group, AxisSettings.PROPERTY_visible, false);
		createWidget4Property(group, AxisSettings.PROPERTY_axisIntegerUnit, false);
		createWidget4Property(group, AxisSettings.PROPERTY_location);

		group = getWidgetFactory().createSection(parent, "Line", true, 2);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, AxisSettings.PROPERTY_lineVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, AxisSettings.PROPERTY_linePaint);

		group = getWidgetFactory().createSection(parent, "Tick", true, 4);

		createWidget4Property(group, AxisSettings.PROPERTY_tickCount);
		createWidget4Property(group, AxisSettings.PROPERTY_tickInterval);

		group = getWidgetFactory().createSection(parent, "Tick Mark", true, 2);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, AxisSettings.PROPERTY_tickMarksVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, AxisSettings.PROPERTY_tickMarksInsideLength);
		createWidget4Property(group, AxisSettings.PROPERTY_tickMarksOutsideLength);
		createWidget4Property(group, AxisSettings.PROPERTY_tickMarksPaint);

		group = getWidgetFactory().createSection(parent, "Tick Label", true, 2);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, AxisSettings.PROPERTY_tickLabelsVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, AxisSettings.PROPERTY_tickLabelPaint);
		PadUtil.createWidgets4Property(parent, "", "Tick Label Padding", this);

		group = getWidgetFactory().createSection(parent, Messages.common_label, true, 2);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, AxisSettings.PROPERTY_labelVisible, false).getControl().setLayoutData(gd);
		createWidget4Property(group, AxisSettings.PROPERTY_labelAngle);
		createWidget4Property(group, AxisSettings.PROPERTY_labelPaint);
		PadUtil.createWidgets4Property(parent, MAxisSettings.PROP_LABEL, "Label Padding", this);

	}
}
