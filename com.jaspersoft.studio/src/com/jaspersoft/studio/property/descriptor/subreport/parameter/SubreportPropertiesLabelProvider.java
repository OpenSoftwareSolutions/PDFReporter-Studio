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
package com.jaspersoft.studio.property.descriptor.subreport.parameter;

import java.util.Collection;
import java.util.Map;

import net.sf.jasperreports.engine.JRPropertiesMap;

import org.eclipse.jface.viewers.LabelProvider;

import com.jaspersoft.studio.messages.Messages;

/*
 * @author Chicu Veaceslav
 */
public class SubreportPropertiesLabelProvider extends LabelProvider {

	public SubreportPropertiesLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (element instanceof JRPropertiesMap)
			return Messages.SubreportPropertiesLabelProvider_numbers_of_parameters
					+ ((JRPropertiesMap) element).getPropertyNames().length;
		if (element instanceof Collection)
			return Messages.SubreportPropertiesLabelProvider_numbers_of_parameters + ((Collection<?>) element).size();
		if (element instanceof Map)
			return Messages.SubreportPropertiesLabelProvider_numbers_of_parameters + ((Map<?, ?>) element).size();
		if (element.getClass().isArray())
			return Messages.SubreportPropertiesLabelProvider_numbers_of_parameters + ((Object[]) element).length;
		return element.toString();
	}

}
