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
package com.jaspersoft.studio.property.descriptor.jrQuery;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.jface.viewers.LabelProvider;

import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.property.descriptor.NullEnum;
/*
 * @author Chicu Veaceslav
 * 
 */
public class JRQueryLabelProvider extends LabelProvider {
	private NullEnum canBeNull;

	public JRQueryLabelProvider(NullEnum canBeNull) {
		super();
		this.canBeNull = canBeNull;
	}

	@Override
	public String getText(Object element) {
		if (element != null && element instanceof MQuery) {
			MQuery mQuery = (MQuery) element;
			String lang = (String) mQuery.getPropertyValue(JRDesignQuery.PROPERTY_LANGUAGE);
			if (lang == null)
				lang = "";
			else
				lang = "<" + lang + ">";
			String txt = (String) mQuery.getPropertyValue(JRDesignQuery.PROPERTY_TEXT);
			if (txt == null)
				txt = "";
			return lang + txt; //$NON-NLS-1$
		}
		if (element == null || !(element instanceof JRDesignQuery))
			return canBeNull.getName();
		JRDesignQuery query = (JRDesignQuery) element;
		return query.getText();
	}

}
