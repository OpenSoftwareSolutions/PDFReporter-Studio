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

import net.sf.jasperreports.components.barcode4j.Code39Component;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class Code39Section extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent, "Code39",
				false, 2);

		createWidget4Property(group, Code39Component.PROPERTY_WIDE_FACTOR);

		createWidget4Property(group,
				Code39Component.PROPERTY_INTERCHAR_GAP_WIDTH);
		createWidget4Property(group, Code39Component.PROPERTY_DISPLAY_CHECKSUM);
		createWidget4Property(group,
				Code39Component.PROPERTY_DISPLAY_START_STOP);

		createWidget4Property(group,
				Code39Component.PROPERTY_EXTENDED_CHARSET_ENABLED);
		createWidget4Property(group, Code39Component.PROPERTY_CHECKSUM_MODE);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(Code39Component.PROPERTY_WIDE_FACTOR, Messages.MCode39_wide_factor_description);
		addProvidedProperties(Code39Component.PROPERTY_INTERCHAR_GAP_WIDTH, Messages.common_interchar_gap_width);
		addProvidedProperties(Code39Component.PROPERTY_DISPLAY_CHECKSUM, Messages.common_display_checksum);
		addProvidedProperties(Code39Component.PROPERTY_DISPLAY_START_STOP, Messages.MCode39_display_start_stop);
		addProvidedProperties(Code39Component.PROPERTY_EXTENDED_CHARSET_ENABLED, Messages.MCode39_extended_charset_enabled);
		addProvidedProperties(Code39Component.PROPERTY_CHECKSUM_MODE, Messages.common_checksum_mode);
	}
}
