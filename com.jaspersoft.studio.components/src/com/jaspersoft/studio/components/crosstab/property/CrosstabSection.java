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
package com.jaspersoft.studio.components.crosstab.property;

import net.sf.jasperreports.crosstabs.base.JRBaseCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

public class CrosstabSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(final Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));

		createWidget4Property(parent, JRBaseCrosstab.PROPERTY_RUN_DIRECTION);

		createWidget4Property(parent, JRDesignCrosstab.PROPERTY_PARAMETERS_MAP_EXPRESSION);
		createWidget4Property(parent, JRDesignCrosstab.PROPERTY_REPEAT_COLUMN_HEADERS);
		createWidget4Property(parent, JRDesignCrosstab.PROPERTY_REPEAT_ROW_HEADERS);
		createWidget4Property(parent, JRDesignCrosstab.PROPERTY_IGNORE_WIDTH);
		createWidget4Property(parent, JRBaseCrosstab.PROPERTY_HORIZONTAL_POSITION);
		createWidget4Property(parent, JRDesignCrosstab.PROPERTY_COLUMN_BREAK_OFFSET);
	}

	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseCrosstab.PROPERTY_RUN_DIRECTION, Messages.MCrosstab_run_direction);
		addProvidedProperties(JRDesignCrosstab.PROPERTY_PARAMETERS_MAP_EXPRESSION, Messages.MCrosstab_parameter_map_expression);
		addProvidedProperties(JRDesignCrosstab.PROPERTY_REPEAT_COLUMN_HEADERS, Messages.MCrosstab_repeat_column_headers);
		addProvidedProperties(JRDesignCrosstab.PROPERTY_REPEAT_ROW_HEADERS, Messages.MCrosstab_repeat_row_headers);
		addProvidedProperties(JRDesignCrosstab.PROPERTY_IGNORE_WIDTH, Messages.MCrosstab_ignore_witdh);
		addProvidedProperties(JRBaseCrosstab.PROPERTY_HORIZONTAL_POSITION, Messages.MCrosstab_horizontalposition);
		addProvidedProperties(JRDesignCrosstab.PROPERTY_COLUMN_BREAK_OFFSET, Messages.MCrosstab_column_break_offset);
	}
}
