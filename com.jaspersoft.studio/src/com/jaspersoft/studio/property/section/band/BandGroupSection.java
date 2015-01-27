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

import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.band.MBandGroupFooter;
import com.jaspersoft.studio.model.band.MBandGroupHeader;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class BandGroupSection extends AbstractSection {
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, "Group Band Properties", false, 2);

		createWidget4Property(parent, JRDesignGroup.PROPERTY_MIN_HEIGHT_TO_START_NEW_PAGE);

		createWidget4Property(parent, JRDesignGroup.PROPERTY_FOOTER_POSITION);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(parent, JRDesignGroup.PROPERTY_START_NEW_COLUMN, false).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(parent, JRDesignGroup.PROPERTY_START_NEW_PAGE, false).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(parent, JRDesignGroup.PROPERTY_REPRINT_HEADER_ON_EACH_PAGE, false).getControl()
				.setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(parent, JRDesignGroup.PROPERTY_RESET_PAGE_NUMBER, false).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 3;
		createWidget4Property(parent, JRDesignGroup.PROPERTY_KEEP_TOGETHER, false).getControl().setLayoutData(gd);
	}

	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MBandGroupHeader)
			return ((MBandGroupHeader) md).getMGroup();
		if (md instanceof MBandGroupFooter)
			return ((MBandGroupFooter) md).getMGroup();
		return md;
	}

	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignGroup.PROPERTY_MIN_HEIGHT_TO_START_NEW_PAGE, Messages.MGroup_minHeightTitle);
		addProvidedProperties(JRDesignGroup.PROPERTY_FOOTER_POSITION, Messages.MGroup_footerPosTitle);
		addProvidedProperties(JRDesignGroup.PROPERTY_START_NEW_COLUMN, Messages.MGroup_newColTitle);
		addProvidedProperties(JRDesignGroup.PROPERTY_START_NEW_PAGE, Messages.MGroup_newPageTitle);
		addProvidedProperties(JRDesignGroup.PROPERTY_REPRINT_HEADER_ON_EACH_PAGE, Messages.MGroup_reprintPosition);
		addProvidedProperties(JRDesignGroup.PROPERTY_RESET_PAGE_NUMBER, Messages.MGroup_pageNumberTitle);
		addProvidedProperties(JRDesignGroup.PROPERTY_KEEP_TOGETHER, Messages.MGroup_keepTitle);
	}
}
