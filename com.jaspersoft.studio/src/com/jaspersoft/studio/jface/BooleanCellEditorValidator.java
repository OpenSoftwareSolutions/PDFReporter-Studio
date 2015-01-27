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
package com.jaspersoft.studio.jface;

import org.eclipse.jface.viewers.ICellEditorValidator;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.NullEnum;

public class BooleanCellEditorValidator implements ICellEditorValidator {

	private NullEnum canBeNull;

	public BooleanCellEditorValidator(NullEnum canBeNull) {
		super();
		this.canBeNull = canBeNull;
	}

	public String isValid(Object value) {
		if (canBeNull != NullEnum.NOTNULL && value == null)
			return null;
		if (value instanceof Boolean)
			return null;
		else
			return Messages.BooleanCellEditorValidator_value_is_not_instance_of_boolean; 
	}
}
