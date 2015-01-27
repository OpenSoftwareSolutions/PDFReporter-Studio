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

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class SizeSection extends AbstractRealValueSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, Messages.SizeSection_sizeSectionTitle, true, 4);
		section = (ExpandableComposite)parent.getParent();
		
		ASPropertyWidget hw = createWidget4Property(parent, JRDesignElement.PROPERTY_WIDTH);
		CLabel lbl = hw.getLabel();
		lbl.setText(Messages.SizeSection_widthLabel);
		lbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		hw = createWidget4Property(parent, JRDesignElement.PROPERTY_HEIGHT);
		lbl = hw.getLabel();
		lbl.setText(Messages.SizeSection_heightLabel);
		lbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		ASPropertyWidget w = createWidget4Property(parent, JRDesignElement.PROPERTY_STRETCH_TYPE);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		w.getControl().setLayoutData(gd);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignElement.PROPERTY_WIDTH, Messages.MGraphicElement_width);
		addProvidedProperties(JRDesignElement.PROPERTY_HEIGHT, Messages.common_height);
		addProvidedProperties(JRDesignElement.PROPERTY_STRETCH_TYPE, Messages.common_stretch_type);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	

}
