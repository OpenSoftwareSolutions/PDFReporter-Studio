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
package com.jaspersoft.studio.property.descriptor.subreport.returnvalue;

import java.util.List;

import org.eclipse.jface.viewers.LabelProvider;

import com.jaspersoft.studio.model.JReportsDTO;

/*
 * @author Chicu Veaceslav
 */
public class RVPropertiesLabelProvider extends LabelProvider {

	public RVPropertiesLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (element instanceof JReportsDTO)
			element = ((JReportsDTO) element).getValue();
		if (element instanceof List)
			return String.format("Number of Values: %d", ((List<?>) element).size());

		return element.toString();
	}
}
