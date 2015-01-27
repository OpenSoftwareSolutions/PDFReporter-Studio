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
package com.jaspersoft.studio.property.section.report;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class PageMarginSection extends AbstractSection {
	
	private ExpandableComposite section;
	
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.parent,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		Composite group = getWidgetFactory().createSection(parent, Messages.PageMarginSection_margin, true, 2);
		section = (ExpandableComposite)parent.getParent();
		
		CLabel l = getWidgetFactory().createCLabel(group, "", SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_top.gif")); //$NON-NLS-1$

		createWidget4Property(group, JasperDesign.PROPERTY_TOP_MARGIN, false);

		l = getWidgetFactory().createCLabel(group, "", SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_bottom.png")); //$NON-NLS-1$ 

		createWidget4Property(group, JasperDesign.PROPERTY_BOTTOM_MARGIN, false);

		l = getWidgetFactory().createCLabel(group, "", SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_left.gif")); //$NON-NLS-1$ 

		createWidget4Property(group, JasperDesign.PROPERTY_LEFT_MARGIN, false);

		l = getWidgetFactory().createCLabel(group, "", SWT.RIGHT);
		l.setImage(JaspersoftStudioPlugin.getInstance().getImage("icons/resources/eclipse/border_right.gif")); //$NON-NLS-1$ 

		createWidget4Property(group, JasperDesign.PROPERTY_RIGHT_MARGIN, false);
	}
	
	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JasperDesign.PROPERTY_TOP_MARGIN,  Messages.MReport_top_margin);
		addProvidedProperties(JasperDesign.PROPERTY_BOTTOM_MARGIN, Messages.MReport_bottom_margin);
		addProvidedProperties(JasperDesign.PROPERTY_LEFT_MARGIN, Messages.MReport_left_margin);
		addProvidedProperties(JasperDesign.PROPERTY_RIGHT_MARGIN, Messages.MReport_right_margin);
	}


}
