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
package com.jaspersoft.studio.property.section.band;

import net.sf.jasperreports.engine.design.JRDesignBand;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.Section;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class BandSection extends AbstractSection {

	private Section section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		section = getWidgetFactory().createAndGetSection(parent, Messages.BandSection_title, false, 2);
		Composite container = (Composite)section.getClient();
		createWidget4Property(container, JRDesignBand.PROPERTY_HEIGHT);
		createWidget4Property(container, JRDesignBand.PROPERTY_SPLIT_TYPE);
		createWidget4Property(container, JRDesignBand.PROPERTY_PRINT_WHEN_EXPRESSION);
	}
	
	@Override
	public void aboutToBeShown() {
		super.aboutToBeShown();
		//The properties are not visible if the band is not created
		if (section != null) section.setVisible(getElement().getValue() != null);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignBand.PROPERTY_HEIGHT, Messages.common_height);
		addProvidedProperties(JRDesignBand.PROPERTY_SPLIT_TYPE, Messages.common_split_type);
		addProvidedProperties(JRDesignBand.PROPERTY_PRINT_WHEN_EXPRESSION, Messages.common_print_when_expression);
	}
}
