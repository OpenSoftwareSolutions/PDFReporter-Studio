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
package com.jaspersoft.studio.property.descriptor.checkbox;

import org.eclipse.swt.widgets.Composite;

public class BooleanCellEditor extends StandardComboBoxCellEditor {
	protected static final int TRUE_INDEX = 0, FALSE_INDEX = 1;

	public BooleanCellEditor(Composite parent) {
		super(parent, new String[] { "true", "false" }, new Object[] { Boolean.TRUE, Boolean.FALSE });
	}

	/**
	 * Return an error message if this is not a valid boolean
	 */
	@Override
	protected String isCorrectObject(Object value) {
		if (value == null || value instanceof Boolean)
			return null;

		return "warning";
	}

}
