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
package com.jaspersoft.studio.components.barcode.property;

import net.sf.jasperreports.components.barcode4j.BarcodeComponent;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class Barcode4jSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent,
				"Barcode 4J", false, 2);

		createWidget4Property(group,
				BarcodeComponent.PROPERTY_PATTERN_EXPRESSION);

		createWidget4Property(group, BarcodeComponent.PROPERTY_QUIET_ZONE);
		createWidget4Property(group, BarcodeComponent.PROPERTY_MODULE_WIDTH);
		createWidget4Property(group,
				BarcodeComponent.PROPERTY_VERTICAL_QUIET_ZONE);

		createWidget4Property(group, BarcodeComponent.PROPERTY_ORIENTATION);
		createWidget4Property(group, BarcodeComponent.PROPERTY_TEXT_POSITION);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(BarcodeComponent.PROPERTY_PATTERN_EXPRESSION, Messages.MBarcode4j_pattern_expression);
		addProvidedProperties(BarcodeComponent.PROPERTY_QUIET_ZONE, Messages.MBarcode4j_quiet_zone);
		addProvidedProperties(BarcodeComponent.PROPERTY_MODULE_WIDTH, Messages.MBarcode4j_module_width);
		addProvidedProperties(BarcodeComponent.PROPERTY_VERTICAL_QUIET_ZONE, Messages.MBarcode4j_vertical_quiet_zone);
		addProvidedProperties(BarcodeComponent.PROPERTY_ORIENTATION, Messages.MBarcode4j_orientation);
		addProvidedProperties(BarcodeComponent.PROPERTY_TEXT_POSITION, Messages.MBarcode4j_text_position);
	}
}
