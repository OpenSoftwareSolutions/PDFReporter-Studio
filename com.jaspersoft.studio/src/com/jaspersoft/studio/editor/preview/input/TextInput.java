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
package com.jaspersoft.studio.editor.preview.input;

import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.editor.preview.view.control.VParameters;

public class TextInput extends ADataInput {
	private Text txt;

	public boolean isForType(Class<?> valueClass) {
		return String.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter param, final Map<String, Object> params) {
		super.createInput(parent, param, params);
		if (isForType(param.getValueClass())) {
			txt = new Text(parent, SWT.BORDER);
			txt.setToolTipText(VParameters.createToolTip(param));
			txt.addFocusListener(focusListener);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalIndent = 8;
			txt.setLayoutData(gd);
			setMandatory(param, txt);

			ModifyListener listener = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					if (!isRefresh)
						updateModel(txt.getText());
				}
			};
			txt.addModifyListener(listener);
			updateInput();
			setNullable(param, txt);
		}
	}

	private boolean isRefresh = false;

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null && value instanceof String)
			txt.setText((String) value);
		else {
			isRefresh = true;
			txt.setText("");
			isRefresh = false;
		}
		setDecoratorNullable(param);
	}
}
