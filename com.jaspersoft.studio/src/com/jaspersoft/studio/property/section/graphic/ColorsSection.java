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
package com.jaspersoft.studio.property.section.graphic;

import net.sf.jasperreports.engine.base.JRBaseStyle;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class ColorsSection extends AbstractRealValueSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, Messages.ColorsSection_colorSectionTitle, true, 4);
		section = (ExpandableComposite)parent.getParent();
		//parent.setLayout(new GridLayout(4, false));
		
		createWidget4Property(parent, JRBaseStyle.PROPERTY_FORECOLOR);
		createWidget4Property(parent, JRBaseStyle.PROPERTY_BACKCOLOR);
		
		Composite transparencyComp = new Composite(parent, SWT.NONE);
		GridLayout transparencyLayout = new GridLayout(2,false);
		transparencyLayout.marginWidth = 0;
		transparencyComp.setLayout(transparencyLayout);
		GridData gd = new GridData();
		gd.horizontalSpan = 4;
		transparencyComp.setLayoutData(gd);
		createWidget4Property(transparencyComp, JRBaseStyle.PROPERTY_MODE,false);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseStyle.PROPERTY_FORECOLOR, Messages.common_forecolor);
		addProvidedProperties(JRBaseStyle.PROPERTY_BACKCOLOR, Messages.common_backcolor);
		addProvidedProperties(JRBaseStyle.PROPERTY_MODE, Messages.common_mode);
	}

}
