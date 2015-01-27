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

import net.sf.jasperreports.components.barcode4j.POSTNETComponent;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class POSTNETSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent, "POSTNET",
				false, 2);

		createWidget4Property(group, POSTNETComponent.PROPERTY_SHORT_BAR_HEIGHT);

		createWidget4Property(group,
				POSTNETComponent.PROPERTY_INTERCHAR_GAP_WIDTH);
		createWidget4Property(group, POSTNETComponent.PROPERTY_DISPLAY_CHECKSUM);
		createWidget4Property(group, POSTNETComponent.PROPERTY_CHECKSUM_MODE);

		createWidget4Property(group,
				POSTNETComponent.PROPERTY_BASELINE_POSITION);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(POSTNETComponent.PROPERTY_SHORT_BAR_HEIGHT, Messages.MPOSTNET_short_bar_height);
		addProvidedProperties(POSTNETComponent.PROPERTY_INTERCHAR_GAP_WIDTH, Messages.common_interchar_gap_width);
		addProvidedProperties(POSTNETComponent.PROPERTY_DISPLAY_CHECKSUM, Messages.common_display_checksum);
		addProvidedProperties(POSTNETComponent.PROPERTY_CHECKSUM_MODE, Messages.common_checksum_mode);
		addProvidedProperties(POSTNETComponent.PROPERTY_BASELINE_POSITION, Messages.MPOSTNET_baseline_position);
	}
	
}
