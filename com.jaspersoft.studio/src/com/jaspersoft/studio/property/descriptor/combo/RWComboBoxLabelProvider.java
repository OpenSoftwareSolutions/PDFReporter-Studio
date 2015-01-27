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

import com.jaspersoft.studio.property.descriptor.NullEnum;
/*
 * @author Chicu Veaceslav
 * 
 */
public class RWComboBoxLabelProvider extends LabelProvider {
	private NullEnum canBeNull;

	public RWComboBoxLabelProvider(String[] labels, NullEnum canBeNull) {
		super();
		this.canBeNull = canBeNull;
	}

	@Override
	public String getText(Object element) {
		if (element == null || element.equals("")) //$NON-NLS-1$
			return canBeNull.getName();
		return element.toString();
	}

}
