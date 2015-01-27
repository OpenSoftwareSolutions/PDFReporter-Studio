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
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.components.chart.model.chartAxis.MChartAxes;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class ChartAxisGraphicSection extends AbstractSection {
	
	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent,
				Messages.GraphicSection_Detail_Section_Title, true, 2);
		section = (ExpandableComposite)parent.getParent();
		// parent.setLayout(new GridLayout(2, false));

		createWidget4Property(parent, JRDesignElement.PROPERTY_KEY)
				.getControl().setLayoutData(
						new GridData(GridData.FILL_HORIZONTAL));

		createWidget4Property(parent, JRDesignElement.PROPERTY_PARENT_STYLE);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES, false)
				.getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignElement.PROPERTY_REMOVE_LINE_WHEN_BLANK, false)
				.getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignElement.PROPERTY_PRINT_IN_FIRST_WHOLE_BAND, false)
				.getControl().setLayoutData(gd);

		parent = getWidgetFactory().createSection(parent,
				Messages.MGraphicElement_print_when, true, 3, 2);

		gd = new GridData();
		gd.horizontalSpan = 3;
		createWidget4Property(parent,
				JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS, false)
				.getControl().setLayoutData(gd);

		gd = new GridData();
		gd.horizontalSpan = 2;
		createWidget4Property(parent,
				JRDesignElement.PROPERTY_PRINT_WHEN_GROUP_CHANGES).getControl()
				.setLayoutData(gd);

		createWidget4Property(parent,
				JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignElement.PROPERTY_KEY, Messages.common_key);
		addProvidedProperties(JRDesignElement.PROPERTY_PARENT_STYLE, Messages.common_backcolor);
		addProvidedProperties(JRDesignElement.PROPERTY_PRINT_REPEATED_VALUES, Messages.common_parent_style);
		addProvidedProperties(JRDesignElement.PROPERTY_REMOVE_LINE_WHEN_BLANK, Messages.MGraphicElement_remove_line_when_blank);
		addProvidedProperties(JRDesignElement.PROPERTY_PRINT_IN_FIRST_WHOLE_BAND, Messages.MGraphicElement_print_in_first_whole_band);
		addProvidedProperties(JRDesignElement.PROPERTY_PRINT_WHEN_DETAIL_OVERFLOWS, Messages.MGraphicElement_print_when_detail_overflows);
		addProvidedProperties(JRDesignElement.PROPERTY_PRINT_WHEN_GROUP_CHANGES, Messages.MGraphicElement_print_when_group_changes);
		addProvidedProperties(JRDesignElement.PROPERTY_PRINT_WHEN_EXPRESSION, Messages.common_print_when_expression);
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
