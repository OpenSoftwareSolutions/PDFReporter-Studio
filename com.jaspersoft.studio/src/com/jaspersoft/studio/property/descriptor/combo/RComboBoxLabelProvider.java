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
package com.jaspersoft.studio.property.descriptor.combo;

import org.eclipse.jface.viewers.LabelProvider;
/*
 * @author Chicu Veaceslav
 * 
 */
public class RComboBoxLabelProvider extends LabelProvider {

	public RComboBoxLabelProvider(String[] labels) {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (element instanceof String) {
			if (element == null || element.equals("")) //$NON-NLS-1$
				return ""; //$NON-NLS-1$
			return (String) element;
		}
		return element.toString();
	}

}
