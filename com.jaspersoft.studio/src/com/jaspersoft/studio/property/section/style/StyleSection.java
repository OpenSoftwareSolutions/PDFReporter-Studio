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
package com.jaspersoft.studio.property.section.style;

import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

public class StyleSection extends AbstractRealValueSection {

	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));

		if (!(getElement() instanceof MConditionalStyle))
			createWidget4Property(parent, JRDesignStyle.PROPERTY_NAME);

		createWidget4Property(parent, JRDesignStyle.PROPERTY_PARENT_STYLE);

		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		if (!(getElement() instanceof MConditionalStyle))
			createWidget4Property(parent, JRDesignStyle.PROPERTY_DEFAULT, false).getControl().setLayoutData(gd);
		
		createWidget4Property(parent, JRDesignStyle.PROPERTY_BLANK_WHEN_NULL);

		createWidget4Property(parent, JRDesignStyle.PROPERTY_PATTERN);

		createWidget4Property(parent, JRBaseStyle.PROPERTY_FILL);
		createWidget4Property(parent, JRBaseStyle.PROPERTY_RADIUS);
		createWidget4Property(parent, JRBaseStyle.PROPERTY_SCALE_IMAGE);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignStyle.PROPERTY_NAME, Messages.common_name);
		addProvidedProperties(JRDesignStyle.PROPERTY_PARENT_STYLE, Messages.common_parent_style);
		addProvidedProperties(JRDesignStyle.PROPERTY_DEFAULT, Messages.MStyle_default_style);
		addProvidedProperties(JRDesignStyle.PROPERTY_BLANK_WHEN_NULL, Messages.common_blank_when_null);
		addProvidedProperties(JRDesignStyle.PROPERTY_PATTERN, Messages.common_pattern);
		addProvidedProperties(JRBaseStyle.PROPERTY_FILL, Messages.common_fill);
		addProvidedProperties(JRBaseStyle.PROPERTY_RADIUS, Messages.common_radius);
		addProvidedProperties(JRBaseStyle.PROPERTY_SCALE_IMAGE, Messages.MStyle_scale);
	}
}
