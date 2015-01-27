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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.utils.Misc;

public class StringElement extends AWElement {

	protected Text text;

	@Override
	public Class<?> getSupportedType() {
		return String.class;
	}

	protected int getStyle() {
		return SWT.BORDER;
	}

	@Override
	public Control createControl(Composite parent) {
		text = new Text(parent, getStyle());
		text.setText(Misc.nvl(getValue(), ""));
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setValue(convertString(text.getText()));
			}
		});

		return text;
	}

	protected Object convertString(String str) {
		return Misc.nvl(str);
	}

}
