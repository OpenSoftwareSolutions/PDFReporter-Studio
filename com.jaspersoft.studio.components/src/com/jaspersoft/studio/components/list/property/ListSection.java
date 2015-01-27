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
package com.jaspersoft.studio.components.list.property;

import net.sf.jasperreports.components.list.DesignListContents;
import net.sf.jasperreports.components.list.StandardListComponent;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.list.messages.Messages;
import com.jaspersoft.studio.components.list.model.ListSizePropertyDescriptor;
import com.jaspersoft.studio.components.list.model.MList;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class ListSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(final Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));

		createWidget4Property(parent, StandardListComponent.PROPERTY_IGNORE_WIDTH);
		createWidget4Property(parent, MList.PREFIX + DesignListContents.PROPERTY_HEIGHT);
		createWidget4Property(parent, MList.PREFIX + DesignListContents.PROPERTY_WIDTH);
		
		Composite buttonContainer = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(1,false);
		layout.verticalSpacing = 0;
		layout.horizontalSpacing = 0;
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttonContainer.setLayout(layout);
		GridData data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 2;
		buttonContainer.setLayoutData(data);
		
		createWidget4Property(buttonContainer, ListSizePropertyDescriptor.PROPERTY_ID, false);
		createWidget4Property(parent,StandardListComponent.PROPERTY_PRINT_ORDER);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(StandardListComponent.PROPERTY_IGNORE_WIDTH, Messages.MList_ignore_width);
		addProvidedProperties(MList.PREFIX + DesignListContents.PROPERTY_HEIGHT, Messages.MList_cell_height);
		addProvidedProperties(MList.PREFIX + DesignListContents.PROPERTY_WIDTH, Messages.MList_cell_width);
		addProvidedProperties(StandardListComponent.PROPERTY_PRINT_ORDER, Messages.MList_print_order);
	}
	
}
