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

import net.sf.jasperreports.engine.design.JRDesignScriptlet;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

public class ScriptletSystemSection extends AbstractSection {
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, "Scriptlet Properties", false, 3);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		ASPropertyWidget w = createWidget4Property(parent, JRDesignScriptlet.PROPERTY_NAME);
		w.getControl().setLayoutData(gd);
		w.setReadOnly(true);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		w = createWidget4Property(parent, JRDesignScriptlet.PROPERTY_DESCRIPTION);
		w.getControl().setLayoutData(gd);
		w.setReadOnly(true);

		w = createWidget4Property(parent, JRDesignScriptlet.PROPERTY_VALUE_CLASS_NAME);
		w.setReadOnly(true);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignScriptlet.PROPERTY_NAME, Messages.common_name);
		addProvidedProperties(JRDesignScriptlet.PROPERTY_DESCRIPTION, Messages.common_description);
		addProvidedProperties(JRDesignScriptlet.PROPERTY_VALUE_CLASS_NAME, Messages.common_class);
	}

}
