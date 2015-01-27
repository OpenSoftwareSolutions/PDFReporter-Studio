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
package com.jaspersoft.studio.property.section.report;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class PageColumnsSection extends AbstractSection {
	
	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.parent,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, Messages.MReport_columns_category, true, 2);
		section = (ExpandableComposite)parent.getParent();
		
		createWidget4Property(parent, JasperDesign.PROPERTY_COLUMN_COUNT);

		createWidget4Property(parent, JasperDesign.PROPERTY_COLUMN_WIDTH);

		createWidget4Property(parent, JasperDesign.PROPERTY_COLUMN_SPACING);

		createWidget4Property(parent, JasperDesign.PROPERTY_PRINT_ORDER);

	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JasperDesign.PROPERTY_COLUMN_COUNT,  Messages.MReport_column_count);
		addProvidedProperties(JasperDesign.PROPERTY_COLUMN_WIDTH, Messages.MReport_column_width);
		addProvidedProperties(JasperDesign.PROPERTY_COLUMN_SPACING, Messages.MReport_column_space);
		addProvidedProperties(JasperDesign.PROPERTY_PRINT_ORDER, Messages.MReport_print_order);
	}


}
