/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.model;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.osgi.util.NLS;

import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;
import com.jaspersoft.studio.components.customvisualization.messages.Messages;

/**
 * Label provider for the {@link CVDesignComponent#PROPERTY_ITEM_DATA}
 * property in the Advanced property tab.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class CVItemDataLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof List<?>) {
			return NLS.bind(Messages.CVItemDataLabelProvider_TextStr,((List<?>) element).size());
		}
		return super.getText(element);
	}
	
}
