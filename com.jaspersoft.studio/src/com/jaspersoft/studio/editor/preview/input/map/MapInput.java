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
package com.jaspersoft.studio.editor.preview.input.map;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.editor.preview.input.ADataInput;
import com.jaspersoft.studio.editor.preview.input.IParameter;

public class MapInput extends ADataInput {
	private Button bbuton;
	private Label label;

	public boolean isForType(Class<?> valueClass) {
		return Map.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter param, Map<String, Object> params) {
		super.createInput(parent, param, params);
		if (isForType(param.getValueClass())) {
			Composite cmp = new Composite(parent, SWT.NONE);
			GridLayout layout = new GridLayout(2, false);
			layout.marginWidth = 0;
			layout.marginHeight = 0;
			cmp.setLayout(layout);
			cmp.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

			label = new Label(cmp, SWT.BORDER | SWT.CENTER | SWT.WRAP);
			label.setToolTipText(param.getDescription());
			label.setLayoutData(new GridData(GridData.FILL_BOTH));

			bbuton = new Button(cmp, SWT.PUSH);
			bbuton.setText("...");
			bbuton.setToolTipText(param.getDescription());
			bbuton.addFocusListener(focusListener);
			bbuton.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					Map<String, Object> p = MapInput.this.params;
					Object value = p.get(param.getName());
					if (value == null) {
						try {
							value = param.getValueClass().newInstance();
						} catch (InstantiationException ex) {
							if (param.getValueClass().isAssignableFrom(Map.class))
								value = new HashMap();
						} catch (IllegalAccessException ex) {
						}
					}
					MapDialog d = new MapDialog(bbuton.getShell(), (Map<Object, Object>) value, param);
					if (d.open() == Dialog.OK) {
						updateModel(d.getValue());
						updateInput();
					}
				}
			});
			updateInput();
		}
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null && value instanceof Map)
			label.setText(((Map) value).size() + " elements");
		else
			label.setText("No elements");
	}

}
