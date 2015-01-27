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

import net.sf.jasperreports.components.barcode4j.FourStateBarcodeComponent;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class FourStateSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent, "FourState",
				false, 2);

		createWidget4Property(group,
				FourStateBarcodeComponent.PROPERTY_CHECKSUM_MODE);
		createWidget4Property(group,
				FourStateBarcodeComponent.PROPERTY_INTERCHAR_GAP_WIDTH);
		createWidget4Property(group,
				FourStateBarcodeComponent.PROPERTY_ASCENDER_HEIGHT);
		createWidget4Property(group,
				FourStateBarcodeComponent.PROPERTY_TRACK_HEIGHT);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(FourStateBarcodeComponent.PROPERTY_CHECKSUM_MODE, Messages.common_checksum_mode);
		addProvidedProperties(FourStateBarcodeComponent.PROPERTY_INTERCHAR_GAP_WIDTH, Messages.common_interchar_gap_width);
		addProvidedProperties(FourStateBarcodeComponent.PROPERTY_ASCENDER_HEIGHT, Messages.MFourStateBarcode_ascender_height);
		addProvidedProperties(FourStateBarcodeComponent.PROPERTY_TRACK_HEIGHT, Messages.MFourStateBarcode_track_height);
	}
}
