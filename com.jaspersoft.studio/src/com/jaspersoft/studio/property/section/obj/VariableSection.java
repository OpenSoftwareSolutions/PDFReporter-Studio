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
package com.jaspersoft.studio.property.section.obj;

import net.sf.jasperreports.engine.design.JRDesignVariable;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.SPIncrementType;
import com.jaspersoft.studio.property.section.widgets.SPResetType;

public class VariableSection extends AbstractSection {
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(3, false));

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent, JRDesignVariable.PROPERTY_CALCULATION).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent, JRDesignVariable.PROPERTY_EXPRESSION).getControl().setLayoutData(gd);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		createWidget4Property(parent, JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION).getControl().setLayoutData(gd);

		IPropertyDescriptor pd = getPropertyDesriptor(JRDesignVariable.PROPERTY_INCREMENT_TYPE);
		IPropertyDescriptor gpd = getPropertyDesriptor(JRDesignVariable.PROPERTY_INCREMENT_GROUP);
		getWidgetFactory().createCLabel(parent, pd.getDisplayName());
		SPIncrementType winctype = new SPIncrementType(parent, this, pd, gpd);
		gd = new GridData();
		gd.horizontalSpan = 2;
		winctype.getControl().setLayoutData(gd);
		widgets.put(pd.getId(), winctype);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		createWidget4Property(parent, JRDesignVariable.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME).getControl().setLayoutData(
				gd);

		pd = getPropertyDesriptor(JRDesignVariable.PROPERTY_RESET_TYPE);
		gpd = getPropertyDesriptor(JRDesignVariable.PROPERTY_RESET_GROUP);
		getWidgetFactory().createCLabel(parent, pd.getDisplayName());
		SPResetType wrestype = new SPResetType(parent, this, pd, gpd);
		gd = new GridData();
		gd.horizontalSpan = 2;
		wrestype.getControl().setLayoutData(gd);
		widgets.put(pd.getId(), wrestype);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignVariable.PROPERTY_CALCULATION, Messages.MVariable_calculation);
		addProvidedProperties(JRDesignVariable.PROPERTY_EXPRESSION, Messages.common_expression);
		addProvidedProperties(JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION, Messages.MVariable_initial_value_expression);
		addProvidedProperties(JRDesignVariable.PROPERTY_INCREMENT_TYPE, Messages.common_increment_type);
		addProvidedProperties(JRDesignVariable.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME, Messages.MVariable_incrementer_factory_class_name);
		addProvidedProperties(JRDesignVariable.PROPERTY_RESET_TYPE, Messages.common_reset_type);
	}
}
