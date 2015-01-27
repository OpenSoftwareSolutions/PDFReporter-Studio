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
package com.jaspersoft.studio.html.properties;

import net.sf.jasperreports.components.html.HtmlComponent;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.html.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.SPEvaluationTime;

public class HtmlSection extends AbstractSection {

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent,
			TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));

		createWidget4Property(parent, HtmlComponent.PROPERTY_SCALE_TYPE);
		createWidget4Property(parent, HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW);
		createWidget4Property(parent, HtmlComponent.PROPERTY_HORIZONTAL_ALIGN);
		createWidget4Property(parent, HtmlComponent.PROPERTY_VERTICAL_ALIGN);

		IPropertyDescriptor pd = getPropertyDesriptor(HtmlComponent.PROPERTY_EVALUATION_TIME);
		IPropertyDescriptor gpd = getPropertyDesriptor(HtmlComponent.PROPERTY_EVALUATION_GROUP);
		getWidgetFactory().createCLabel(parent, pd.getDisplayName());
		widgets.put(pd.getId(), new SPEvaluationTime(parent, this, pd, gpd));

		createWidget4Property(parent,
				HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(HtmlComponent.PROPERTY_SCALE_TYPE, Messages.MHtml_scaletype);
		addProvidedProperties(HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW, Messages.MHtml_cliponoverflow);
		addProvidedProperties(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN, Messages.MHtml_horizontalalign);
		addProvidedProperties(HtmlComponent.PROPERTY_VERTICAL_ALIGN, Messages.MHtml_verticalalign);
		addProvidedProperties(HtmlComponent.PROPERTY_EVALUATION_TIME, Messages.MHtml_evaluation_time);
		addProvidedProperties(HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION, Messages.MHtml_content_expression);
	}

}
