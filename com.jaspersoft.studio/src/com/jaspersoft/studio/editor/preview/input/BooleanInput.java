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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class BooleanInput extends ADataInput {
	private Button bbuton;

	public boolean isForType(Class<?> valueClass) {
		return Boolean.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter prm, Map<String, Object> params) {
		super.createInput(parent, prm, params);
		if (isForType(prm.getValueClass())) {
			bbuton = new Button(parent, SWT.CHECK);
			bbuton.setText(prm.getLabel());
			bbuton.setToolTipText(prm.getDescription());
			bbuton.addFocusListener(focusListener);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalIndent = 8;
			bbuton.setLayoutData(gd);
			bbuton.setBackground(parent.getBackground());
			SelectionAdapter listener = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateModel(new Boolean(bbuton.getSelection()));
					setDecoratorNullable(param);
				}
			};
			bbuton.addSelectionListener(listener);
			setNullable(prm, bbuton);
			updateInput();
		}
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null && value instanceof Boolean)
			bbuton.setSelection((Boolean) value);
		else
			bbuton.setSelection(false);
		setDecoratorNullable(param);
	}

	@Override
	public boolean isLabeled() {
		return true;
	}
}
