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

import java.util.Collection;
import java.util.EnumSet;

import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignVariable;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionContext.Visibility;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.SPExpression;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

public class ParameterSection extends AbstractSection {
	
	private Composite cmp;
	
	private Composite rootComposite;
	
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);
		parent.setLayout(new GridLayout(1, false));
		
		rootComposite = getWidgetFactory().createComposite(parent);
		GridLayout rootLayout = new GridLayout(2,false);
		//rootLayout.horizontalSpacing = 0;
		rootLayout.marginHeight=0;
		rootLayout.marginWidth=0;
		//rootLayout.verticalSpacing=0;
		rootComposite.setLayout(rootLayout);
		GridData rootData = new GridData(GridData.FILL_BOTH);
		rootData.minimumHeight = 150;
		rootComposite.setLayoutData(rootData);

		cmp = getWidgetFactory().createComposite(rootComposite);
		GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		cmp.setLayout(layout);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		cmp.setLayoutData(gd);

		createWidget4Property(cmp, JRDesignParameter.PROPERTY_NESTED_TYPE_NAME);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		createWidget4Property(rootComposite, JRDesignParameter.PROPERTY_DESCRIPTION).getControl().setLayoutData(gd);
		if (getElement() instanceof MParameter) {
			MParameter p = (MParameter) getElement();
			if (p.isMainDataset())
				createWidget4Property(rootComposite, JRDesignParameter.PROPERTY_FOR_PROMPTING);
		}
		createWidget4Property(rootComposite, JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME,  Messages.MParameter_nested_type_name);
		addProvidedProperties(JRDesignParameter.PROPERTY_DESCRIPTION, Messages.common_description);
		addProvidedProperties(JRDesignParameter.PROPERTY_FOR_PROMPTING, Messages.MParameter_is_for_prompting);
		addProvidedProperties(JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION, Messages.MParameter_default_value_expression);
	}
	
	/**
	 * Show or hide the composite with the nested class type
	 */
	private void setCompVisible(boolean visible){
		if (cmp.isVisible() != visible){
			cmp.setVisible(visible);
			((GridData)cmp.getLayoutData()).exclude = !visible;
			rootComposite.layout(true,true);
		}
	}
	
	/**
	 * Take a qualified class name and return true if it represent a class 
	 * subtype of Collection, false otherwise
	 */
	private boolean isClassCollection(String className){
		try {
			return Collection.class.isAssignableFrom(Class.forName(className));
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	
	/**
	 * When the data is refreshed is checked if the class name is a collection, in this
	 * case the controls to define the nested type are shown, otherwise they are hidden.
	 */
	@Override
	public void refresh() {
		setRefreshing(true);
		APropertyNode element = getElement();
		if (element != null) {
			element.getPropertyDescriptors();
			for (Object key : widgets.keySet()) {
				if (key.equals(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME)){
					String type = element.getPropertyValue(JRDesignVariable.PROPERTY_VALUE_CLASS_NAME).toString();
					if (isClassCollection(type)) setCompVisible(true);
					else setCompVisible(false);
				}
				widgets.get(key).setData(element, element.getPropertyValue(key));
				if (key.equals(JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION)){
					// fix the visibilities mask: allows only PARAMETERS
					ExpressionContext expContext = ((WTextExpression) ((SPExpression) widgets.get(key)).getControl()).getExpressionContext();
					if(expContext!=null){
						expContext.setVisibilities(EnumSet.of(Visibility.SHOW_PARAMETERS));
					}
				}
			}
		}
		setRefreshing(false);
	}
}
