/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;
import com.jaspersoft.studio.components.customvisualization.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPEvaluationTime;

/**
 * Properties section for the main details of the Custom Visualization Component element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CVSection extends AbstractSection{

	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		
		parent.setLayout(new GridLayout(3,false));
		
		IPropertyDescriptor pd = getPropertyDesriptor(CVDesignComponent.PROPERTY_EVALUATION_TIME);
		IPropertyDescriptor gpd = getPropertyDesriptor(CVDesignComponent.PROPERTY_EVALUATION_GROUP);
		getWidgetFactory().createCLabel(parent, pd.getDisplayName());
		SPEvaluationTime eval = new SPEvaluationTime(parent, this, pd, gpd);
		eval.getControl().setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 2, 1));
		widgets.put(pd.getId(), eval);
		
		createWidget4Property(parent, CVDesignComponent.PROPERTY_ON_ERROR_TYPE);
		
		getWidgetFactory().createCLabel(parent, Messages.CVSection_CVItemProperties).setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		ASPropertyWidget itemPropsW = createWidget4Property(parent, CVDesignComponent.PROPERTY_ITEM_PROPERTIES,false);
		itemPropsW.getControl().setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		
		getWidgetFactory().createCLabel(parent, Messages.CVSection_CVItemData).setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		ASPropertyWidget itemDataW = createWidget4Property(parent, CVDesignComponent.PROPERTY_ITEM_DATA,false);
		itemDataW.getControl().setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false,3,1));
		
		createWidget4Property(parent, CVDesignComponent.PROPERTY_PROCESSING_CLASS);
	}

	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(CVDesignComponent.PROPERTY_EVALUATION_TIME, Messages.CVSection_EvalTime);
		addProvidedProperties(CVDesignComponent.PROPERTY_EVALUATION_GROUP, Messages.CVSection_EvalGroup);
		addProvidedProperties(CVDesignComponent.PROPERTY_PROCESSING_CLASS, Messages.CVSection_ProcessingClass);
		addProvidedProperties(CVDesignComponent.PROPERTY_ITEM_PROPERTIES, Messages.CVSection_CVItemPropertiesDesc);
		addProvidedProperties(CVDesignComponent.PROPERTY_ITEM_DATA, Messages.CVSection_InnerConfiguration);
		addProvidedProperties(CVDesignComponent.PROPERTY_ON_ERROR_TYPE, Messages.CVSection_OnErrorType);
	}
}
