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
package com.jaspersoft.studio.editor.preview.input.array;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.utils.Misc;

public class BooleanElement extends AWElement {

	private Button bbuton;

	@Override
	public Class<?> getSupportedType() {
		return Boolean.class;
	}

	@Override
	public Control createControl(Composite parent) {
		bbuton = new Button(parent, SWT.CHECK);
		bbuton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setValue(bbuton.getSelection());
				updateLabel();
			}
		});
		if (getValue() != null && getValue() instanceof Boolean)
			bbuton.setSelection((Boolean) Misc.nvl(getValue(), Boolean.FALSE));
		updateLabel();
		return bbuton;
	}

	private void updateLabel() {
		bbuton.setText(new Boolean(bbuton.getSelection()).toString());
	}

}
