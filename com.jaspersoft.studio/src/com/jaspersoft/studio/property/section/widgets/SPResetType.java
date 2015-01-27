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
package com.jaspersoft.studio.property.section.widgets;

import net.sf.jasperreports.engine.type.JREnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.property.section.AbstractSection;

public class SPResetType extends SPGroupTypeCombo {

	public SPResetType(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor,
			IPropertyDescriptor gDescriptor) {
		super(parent, section, pDescriptor,gDescriptor);
	}

	@Override
	protected JREnum[] getEnumValues() {
		return ResetTypeEnum.values();
	}

	@Override
	protected JREnum getGroupEnum() {
		return ResetTypeEnum.GROUP;
	}

	@Override
	protected JREnum getByName(String name) {
		return ResetTypeEnum.getByName(name);
	}
}
