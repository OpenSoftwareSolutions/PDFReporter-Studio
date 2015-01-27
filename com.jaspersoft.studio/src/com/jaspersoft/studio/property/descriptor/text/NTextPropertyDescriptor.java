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
package com.jaspersoft.studio.property.descriptor.text;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.property.descriptors.JSSTextPropertyDescriptor;

public class NTextPropertyDescriptor extends JSSTextPropertyDescriptor {

	public NTextPropertyDescriptor(Object id, String displayName, int style) {
		super(id, displayName, style);
	}

	public NTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new TextCellEditor(parent) {
			@Override
			protected Object doGetValue() {
				String doGetValue = (String) super.doGetValue();
				if (doGetValue.equals("")) //$NON-NLS-1$
					doGetValue = null;
				else
					doGetValue = doGetValue.trim();
				return doGetValue;
			}

			@Override
			protected void doSetValue(Object value) {
				if (value == null)
					value = ""; //$NON-NLS-1$
				else
					value = ((String) value).trim();
				super.doSetValue(value);
			}
		};
		if (getValidator() != null) {
			editor.setValidator(getValidator());
		}
		HelpSystem.bindToHelp(this, editor.getControl());
		return editor;
	}
}
