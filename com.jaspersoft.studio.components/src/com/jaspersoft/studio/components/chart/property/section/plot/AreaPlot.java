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
package com.jaspersoft.studio.components.chart.property.section.plot;

import net.sf.jasperreports.charts.design.JRDesignAreaPlot;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class AreaPlot extends APlot {

	@Override
	public void createControls(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		createCategory(section, parent, tabbedPropertySheetPage);

		createValue(section, parent, tabbedPropertySheetPage);
	}

	private void createCategory(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		parent = section.getWidgetFactory().createSectionTitle(parent,
				Messages.AreaPlot_categoryAxis, true, 2, 2);

		section.createWidget4Property(parent,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_LINE_COLOR);

		Composite group = section.getWidgetFactory().createSection(parent,
				Messages.common_label, false, 2, 2); //$NON-NLS-1$

		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_LABEL_EXPRESSION);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_LABEL_FONT, false)
				.getControl().setLayoutData(gd);

		group = section.getWidgetFactory().createSection(parent, Messages.AreaPlot_tick, false,
				4, 2);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_TICK_LABEL_FONT, false)
				.getControl().setLayoutData(gd);

		section.getWidgetFactory().createCLabel(group, Messages.AreaPlot_color);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_TICK_LABEL_COLOR, false);

		section.getWidgetFactory().createCLabel(group, Messages.AreaPlot_mask);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_TICK_LABEL_MASK, false);

		section.getWidgetFactory().createCLabel(group, Messages.AreaPlot_rotation);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_TICK_LABEL_ROTATION,
				false);

		section.getWidgetFactory().createCLabel(group, Messages.AreaPlot_verticalTick);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_CATEGORY_AXIS_VERTICAL_TICK_LABELS,
				false);

		section.createWidget4Property(parent,
				JRDesignAreaPlot.PROPERTY_DOMAIN_AXIS_MINVALUE_EXPRESSION);
		section.createWidget4Property(parent,
				JRDesignAreaPlot.PROPERTY_DOMAIN_AXIS_MAXVALUE_EXPRESSION);
	}

	private void createValue(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		parent = section.getWidgetFactory().createSectionTitle(parent,
				Messages.AreaPlot_valueAxis, true, 2, 2);

		section.createWidget4Property(parent,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_LINE_COLOR);

		Composite group = section.getWidgetFactory().createSection(parent,
				Messages.common_label, false, 2, 2); //$NON-NLS-1$

		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_LABEL_EXPRESSION);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_LABEL_FONT, false)
				.getControl().setLayoutData(gd);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_LABEL_COLOR);

		group = section.getWidgetFactory().createSection(parent, Messages.common_tick, false, //$NON-NLS-1$
				2, 2);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_FONT, false)
				.getControl().setLayoutData(gd);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_COLOR);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_TICK_LABEL_MASK);
		section.createWidget4Property(group,
				JRDesignAreaPlot.PROPERTY_VALUE_AXIS_VERTICAL_TICK_LABELS);
		section.createWidget4Property(parent,
				JRDesignAreaPlot.PROPERTY_RANGE_AXIS_MINVALUE_EXPRESSION);
		section.createWidget4Property(parent,
				JRDesignAreaPlot.PROPERTY_RANGE_AXIS_MAXVALUE_EXPRESSION);
	}

}
