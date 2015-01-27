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

import net.sf.jasperreports.components.barcode4j.Interleaved2Of5Component;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class Interleaved2Of5Section extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent,
				"Interleaved2Of5", false, 2);

		createWidget4Property(group,
				Interleaved2Of5Component.PROPERTY_CHECKSUM_MODE);
		createWidget4Property(group,
				Interleaved2Of5Component.PROPERTY_WIDE_FACTOR);
		createWidget4Property(group,
				Interleaved2Of5Component.PROPERTY_DISPLAY_CHECKSUM);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(Interleaved2Of5Component.PROPERTY_CHECKSUM_MODE, Messages.common_checksum_mode);
		addProvidedProperties(Interleaved2Of5Component.PROPERTY_WIDE_FACTOR, Messages.common_wide_factor);
		addProvidedProperties(Interleaved2Of5Component.PROPERTY_DISPLAY_CHECKSUM, Messages.common_display_checksum);
	}
}
