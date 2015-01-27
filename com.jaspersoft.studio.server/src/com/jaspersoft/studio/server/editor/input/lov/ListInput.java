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
package com.jaspersoft.studio.server.editor.input.lov;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.server.editor.input.IInput;

public class ListInput implements IInput {

	private ListOfValuesInput dataInput;
	private Map<String, Object> params;
	private IParameter param;
	private Combo combo;
	private List<ListItem> items;

	public ListInput(ListOfValuesInput dataInput, IParameter param, Map<String, Object> params) {
		this.dataInput = dataInput;
		this.param = param;
		this.params = params;
	}

	public void createControl(Composite parent, int style) {
		combo = new Combo(parent, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 8;
		gd.minimumHeight = 100;
		combo.setLayoutData(gd);

		fillControl();

		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int ti = combo.getSelectionIndex();
				if (ti >= 0 && ti < items.size())
					dataInput.updateModel(items.get(ti).getValue());
			}
		};
		combo.addSelectionListener(listener);
		updateInput();
		listener.widgetSelected(null);
	}

	public void fillControl() {
		items = dataInput.getRd().getListOfValues();
		combo.removeAll();
		if (items != null) {
			List<Object> toSel = new ArrayList<Object>();
			String[] citems = new String[items.size()];
			for (int i = 0; i < items.size(); i++) {
				ListItem li = items.get(i);
				citems[i] = li.getLabel();
				if (li.isSelected())
					toSel.add(li.getValue());
			}
			params.put(param.getName(), toSel);
			combo.setItems(citems);
		}
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null) {
			if (value instanceof List && !((List<?>) value).isEmpty())
				value = ((List<?>) value).get(0);
			for (int i = 0; i < items.size(); i++) {
				if (items.get(i).getValue().equals(value)) {
					combo.select(i);
					break;
				}
			}
		}
	}

	public Control getControl() {
		return combo;
	}
}
