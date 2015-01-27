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

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.jface.BooleanCellEditorValidator;
import com.jaspersoft.studio.property.descriptor.NullEnum;

/**
 * Represent a checkbox in the property view, but when viewed in tabular mode in advanced view is a 
 * combo box with the value null
 * @author Orlandin Marco
 *
 */
public class NullCheckBoxPropertyDescriptor extends CheckBoxPropertyDescriptor{
	
	public NullCheckBoxPropertyDescriptor(Object id, String displayName, NullEnum canBeNull) {
		super(id,displayName,canBeNull);
		setValidator(new BooleanCellEditorValidator(NullEnum.INHERITED));
	}
	
	public NullCheckBoxPropertyDescriptor(Object id, String displayName) {
		this(id, displayName, NullEnum.NOTNULL);
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
			CellEditor editor = new BCheckBoxCellEditor(parent, NullEnum.INHERITED);
			if (getValidator() != null)
				editor.setValidator(getValidator());
			return editor;
	}
	
}
