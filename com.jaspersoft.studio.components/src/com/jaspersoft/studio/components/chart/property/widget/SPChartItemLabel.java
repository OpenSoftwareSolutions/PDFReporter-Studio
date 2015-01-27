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
package com.jaspersoft.studio.components.chart.property.widget;

import net.sf.jasperreports.charts.design.JRDesignItemLabel;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.chart.model.MChartItemLabel;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPColor;
import com.jaspersoft.studio.property.section.widgets.SPFont;

public class SPChartItemLabel extends ASPropertyWidget {

	public SPChartItemLabel(Composite parent, AbstractSection section,
			IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return null;
	}

	protected void createComponent(Composite parent) {
		ml = new MChartItemLabel(new JRDesignItemLabel(null, null));

		IPropertyDescriptor pd = ml
				.getPropertyDescriptor(JRDesignItemLabel.PROPERTY_COLOR);

		section.getWidgetFactory().createCLabel(parent, pd.getDisplayName());

		ilColor = new SPColor(parent, section, pd);

		pd = ml.getPropertyDescriptor(JRDesignItemLabel.PROPERTY_BACKGROUND_COLOR);
		section.getWidgetFactory().createCLabel(parent, pd.getDisplayName());

		ilBGColor = new SPColor(parent, section, pd);

		pd = ml.getPropertyDescriptor(JRDesignItemLabel.PROPERTY_FONT);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 4;
		ilFont = new SPFont(parent, section, pd);
		ilFont.getControl().setLayoutData(gd);
	}

	private SPColor ilColor;
	private SPColor ilBGColor;
	private SPFont ilFont;
	private MChartItemLabel ml;

	public void setData(APropertyNode pnode, Object value) {
		ml = (MChartItemLabel) value;
		if (value != null) {
			ilColor.setData(pnode,
					ml.getPropertyValue(JRDesignItemLabel.PROPERTY_COLOR));
			ilBGColor
					.setData(
							pnode,
							ml.getPropertyValue(JRDesignItemLabel.PROPERTY_BACKGROUND_COLOR));
			ilFont.setData(pnode,
					ml.getPropertyValue(JRDesignItemLabel.PROPERTY_FONT));
		}
	}
}
