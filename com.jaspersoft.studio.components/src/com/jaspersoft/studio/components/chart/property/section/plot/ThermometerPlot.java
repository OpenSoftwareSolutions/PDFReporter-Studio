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
import net.sf.jasperreports.charts.design.JRDesignThermometerPlot;
import net.sf.jasperreports.charts.design.JRDesignValueDisplay;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class ThermometerPlot extends APlot {

	@Override
	public void createControls(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_MERCURY_COLOR);

		Composite group = section.getWidgetFactory().createSection(parent,
				"Value", false, 2, 2);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		section.createWidget4Property(group,
				JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
						+ JRDesignValueDisplay.PROPERTY_FONT, false)
				.getControl().setLayoutData(gd);

		section.createWidget4Property(group,
				JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
						+ JRDesignValueDisplay.PROPERTY_COLOR);

		section.createWidget4Property(group,
				JRDesignThermometerPlot.PROPERTY_VALUE_DISPLAY + "." //$NON-NLS-1$
						+ JRDesignValueDisplay.PROPERTY_MASK);

		section.createWidget4Property(group,
				JRDesignThermometerPlot.PROPERTY_VALUE_LOCATION);

		dataRange(section, parent, tabbedPropertySheetPage);
		highRange(section, parent, tabbedPropertySheetPage);
		mediumRange(section, parent, tabbedPropertySheetPage);
		lowRange(section, parent, tabbedPropertySheetPage);

	}

	private void dataRange(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "." //$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION);

		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_DATA_RANGE + "."//$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION);
	}

	private void lowRange(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "." //$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION);

		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_LOW_RANGE + "."//$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION);
	}

	private void mediumRange(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "." //$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION);

		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_MEDIUM_RANGE + "."//$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION);
	}

	private void highRange(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "." //$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_LOW_EXPRESSION);

		section.createWidget4Property(parent,
				JRDesignThermometerPlot.PROPERTY_HIGH_RANGE + "."//$NON-NLS-1$
						+ JRDesignDataRange.PROPERTY_HIGH_EXPRESSION);
	}

}
