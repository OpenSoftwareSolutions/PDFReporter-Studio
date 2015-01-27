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
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class LocationSection extends AbstractSection {

	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, Messages.LocationSection_locationLabel, true, 4);
		section = (ExpandableComposite)parent.getParent();

		ASPropertyWidget pw = createWidget4Property(parent, JRDesignElement.PROPERTY_X);
		CLabel lbl = pw.getLabel();
		lbl.setText(Messages.LocationSection_xCoordinateLabel);
		lbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		pw = createWidget4Property(parent, JRDesignElement.PROPERTY_Y);
		lbl = pw.getLabel();
		lbl.setText(Messages.LocationSection_yCoordinateLabel);
		lbl.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));

		ASPropertyWidget w = createWidget4Property(parent, JRDesignElement.PROPERTY_POSITION_TYPE);
		GridData gd = new GridData();
		gd.horizontalSpan = 3;
		w.getControl().setLayoutData(gd);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) {
			section.setExpanded(true);
		}
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignElement.PROPERTY_X, Messages.LocationSection_xCoordinateLabel);
		addProvidedProperties(JRDesignElement.PROPERTY_Y, Messages.LocationSection_yCoordinateLabel);
		addProvidedProperties(JRDesignElement.PROPERTY_POSITION_TYPE, Messages.common_position_type);
	}
	

}
