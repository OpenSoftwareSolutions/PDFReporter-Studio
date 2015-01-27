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

import net.sf.jasperreports.charts.design.JRDesignDataRange;
import net.sf.jasperreports.charts.design.JRDesignMeterPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class MeterPlot extends APlot {

	@Override
	public void createControls(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		section.createWidget4Property(parent, JRDesignMeterPlot.PROPERTY_UNITS);

		section.createWidget4Property(parent, JRDesignMeterPlot.PROPERTY_SHAPE);

		Composite group = section.getWidgetFactory().createSection(parent,
				Messages.common_tick, false, 2, 2);

		section.createWidget4Property(group,
				JRDesignMeterPlot.PROPERTY_TICK_COLOR);

		section.createWidget4Property(group,
				JRDesignMeterPlot.PROPERTY_TICK_INTERVAL);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(group,
				JRDesignMeterPlot.PROPERTY_TICK_LABEL_FONT, false).getControl()
				.setLayoutData(gd);

		section.createWidget4Property(parent,
				JRDesignMeterPlot.PROPERTY_NEEDLE_COLOR);

		section.createWidget4Property(parent,
				JRDesignMeterPlot.PROPERTY_METER_BACKGROUND_COLOR);

		section.createWidget4Property(parent,
				JRDesignMeterPlot.PROPERTY_METER_ANGLE);

		group = section.getWidgetFactory().createSection(parent, "Value",
				false, 2, 2);

		section.createWidget4Property(group,
				JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
						+ JRDesignValueDisplay.PROPERTY_COLOR);

		section.createWidget4Property(group,
				JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
						+ JRDesignValueDisplay.PROPERTY_MASK);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(group,
				JRDesignMeterPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
						+ JRDesignValueDisplay.PROPERTY_FONT, false)
				.getControl().setLayoutData(gd);

		section.createWidget4Property(parent,
				JRDesignMeterPlot.PROPERTY_DATA_RANGE + "."
						+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION);

		section.createWidget4Property(parent,
				JRDesignMeterPlot.PROPERTY_DATA_RANGE + "."
						+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(parent,
				JRDesignMeterPlot.PROPERTY_INTERVALS, false).getControl()
				.setLayoutData(gd);
	}
}
