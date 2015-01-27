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
package com.jaspersoft.studio.components.table.properties;

import net.sf.jasperreports.components.table.DesignCell;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class TableCellSection extends AbstractSection {
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, "Cell Properties",
				false, 2);

		createWidget4Property(parent, DesignCell.PROPERTY_HEIGHT);

		createWidget4Property(parent, DesignCell.PROPERTY_ROW_SPAN);
		createWidget4Property(parent, DesignCell.PROPERTY_STYLE);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(DesignCell.PROPERTY_HEIGHT, Messages.MCell_height);
		addProvidedProperties(DesignCell.PROPERTY_ROW_SPAN, Messages.MCell_rowspan);
		addProvidedProperties(DesignCell.PROPERTY_STYLE, Messages.MCell_parent_style);
	}
	
}
