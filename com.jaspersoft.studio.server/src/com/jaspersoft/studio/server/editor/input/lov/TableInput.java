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
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.server.editor.input.IInput;

public class TableInput implements IInput {
	private Table table;
	private ListOfValuesInput dataInput;
	private Map<String, Object> params;
	private IParameter param;

	public TableInput(ListOfValuesInput dataInput, IParameter param,
			Map<String, Object> params) {
		this.dataInput = dataInput;
		this.param = param;
		this.params = params;
	}

	public void createControl(Composite parent, int style) {
		table = new Table(parent, style | SWT.V_SCROLL | SWT.H_SCROLL
				| SWT.BORDER | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalIndent = 8;
		gd.minimumHeight = 100;
		table.setLayoutData(gd);
		fillControl();

		SelectionAdapter listener = new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem[] ti = table.getSelection();
				if (dataInput.getRd().getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX)
					doUpdateModel(com.jaspersoft.studio.server.editor.input.query.TableInput
							.getCheckedElements(table));
				else if (dataInput.getRd().getControlType() == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES) {
					doUpdateModel(ti);
				} else if (ti.length > 0) {
					dataInput.updateModel(ti[0].getData());
				}
			}
		};
		table.addSelectionListener(listener);
		updateInput();
		listener.widgetSelected(null);
	}

	private void doUpdateModel(TableItem[] ti) {
		List<Object> lst = new ArrayList<Object>();
		for (TableItem item : ti)
			lst.add(item.getData());
		dataInput.updateModel(lst);
	}

	public void fillControl() {
		List<ListItem> items = dataInput.getRd().getListOfValues();
		table.removeAll();

		List<Object> toSel = new ArrayList<Object>();
		for (ListItem item : items) {
			TableItem ti = new TableItem(table, SWT.NONE);
			ti.setText(item.getLabel());
			ti.setData(item.getValue());
			if (item.isSelected())
				toSel.add(item.getValue());
		}
		params.put(param.getName(), toSel);
		if (items.size() > 4)
			((GridData) table.getLayoutData()).heightHint = 100;

		table.getParent().layout();
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null) {
			byte ct = dataInput.getRd().getControlType();
			if (ct == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES
					|| ct == ResourceDescriptor.IC_TYPE_MULTI_SELECT_LIST_OF_VALUES_CHECKBOX) {
				if (value instanceof List) {
					List<TableItem> titems = new ArrayList<TableItem>();
					List<?> lst = (List<?>) value;
					for (TableItem ti : table.getItems())
						if (lst.contains(ti.getData()))
							titems.add(ti);
					table.setSelection(titems.toArray(new TableItem[titems
							.size()]));
				}
			} else {
				if (value instanceof List && !((List<?>) value).isEmpty())
					value = ((List<?>) value).get(0);
				for (TableItem ti : table.getItems()) {
					if (ti.getData().equals(value)) {
						table.setSelection(ti);
						break;
					}
				}
			}
		}
	}

	public Control getControl() {
		return table;
	}

}
