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

import net.sf.jasperreports.charts.design.JRDesignPiePlot;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class PiePlot extends APlot {

	@Override
	public void createControls(AbstractSection section, Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		
		parent = section.getWidgetFactory().createSection(parent, "Labels",true, 2, 3);
		
		section.createWidget4Property(parent,
				JRDesignPiePlot.PROPERTY_SHOW_LABELS);

		section.createWidget4Property(parent, JRDesignPiePlot.PROPERTY_CIRCULAR);

		section.createWidget4Property(parent,
				JRDesignPiePlot.PROPERTY_LABEL_FORMAT);

		section.createWidget4Property(parent,
				JRDesignPiePlot.PROPERTY_LEGEND_LABEL_FORMAT);

		parent = section.getWidgetFactory().createSection(parent, "Font color",
				true, 4, 2);

		section.createWidget4Property(parent,
				JRDesignPiePlot.PROPERTY_ITEM_LABEL, false);
	}

}
