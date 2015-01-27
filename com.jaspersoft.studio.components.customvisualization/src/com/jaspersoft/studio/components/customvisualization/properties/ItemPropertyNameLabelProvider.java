/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.properties;

import org.eclipse.jface.viewers.ColumnLabelProvider;

import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;

/**
 * Label provider for the column name of a table containing a list of
 * {@link CVItemProperty}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ItemPropertyNameLabelProvider extends ColumnLabelProvider {

	@Override
	public String getText(Object element) {
		if (element instanceof CVItemProperty) {
			return ((CVItemProperty) element).getName();
		}
		return super.getText(element);
	}

}
