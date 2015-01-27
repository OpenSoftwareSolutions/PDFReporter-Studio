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
package com.jaspersoft.studio.components.chart.property.section;

import net.sf.jasperreports.charts.design.JRDesignChartAxis;
import net.sf.jasperreports.engine.base.JRBaseChart;
import net.sf.jasperreports.engine.design.JRDesignChart;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class SubTitleSection extends AbstractRealValueSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent,
				com.jaspersoft.studio.messages.Messages.SubTitleSection_Subtitle_Label, true, 2);
		section = (ExpandableComposite)group.getParent();
		
		getWidgetFactory().createCLabel(group,
				com.jaspersoft.studio.messages.Messages.SubTitleSection_Expression_Label);
		createWidget4Property(group,
				JRDesignChart.PROPERTY_SUBTITLE_EXPRESSION, false);

		getWidgetFactory().createCLabel(group,
				com.jaspersoft.studio.messages.Messages.SubTitleSection_Color_Label);
		createWidget4Property(group, JRBaseChart.PROPERTY_SUBTITLE_COLOR, false);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(group, JRDesignChart.PROPERTY_SUBTITLE_FONT,
				false).getControl().setLayoutData(gd);
	}

	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignChart.PROPERTY_SUBTITLE_EXPRESSION, Messages.MChart_subtitle_expression);
		addProvidedProperties(JRBaseChart.PROPERTY_SUBTITLE_COLOR, Messages.MChart_subtitle_color);
		addProvidedProperties(JRDesignChart.PROPERTY_SUBTITLE_FONT, Messages.MChart_subtitle_font);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MChartAxes)
			return (APropertyNode) md
					.getPropertyValue(JRDesignChartAxis.PROPERTY_CHART);
		return md;
	}

}
