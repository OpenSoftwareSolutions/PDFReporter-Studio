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
package com.jaspersoft.studio.property.section.text;

import net.sf.jasperreports.engine.base.JRBaseStyle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class AlignSection extends AbstractRealValueSection {
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(4, false));

		getWidgetFactory().createCLabel(parent, Messages.AlignSection_common_align, SWT.RIGHT);

		createWidget4Property(parent, JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT, false);

		getWidgetFactory().createCLabel(parent, "", SWT.SEPARATOR | SWT.VERTICAL);

		createWidget4Property(parent, JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, false);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT, Messages.common_horizontal_alignment);
		addProvidedProperties(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, Messages.common_vertical_alignment);
	}
	

}
