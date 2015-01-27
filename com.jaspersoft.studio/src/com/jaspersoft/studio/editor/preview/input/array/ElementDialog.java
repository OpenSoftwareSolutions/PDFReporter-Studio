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

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.editor.preview.input.ParameterJasper;
import com.jaspersoft.studio.editor.preview.input.array.date.DateElement;
import com.jaspersoft.studio.editor.preview.input.array.date.SqlDateElement;
import com.jaspersoft.studio.editor.preview.input.array.date.TimeElement;
import com.jaspersoft.studio.editor.preview.input.array.date.TimestampElement;
import com.jaspersoft.studio.editor.preview.input.array.number.BigDecimalElement;
import com.jaspersoft.studio.editor.preview.input.array.number.BigIntegerElement;
import com.jaspersoft.studio.editor.preview.input.array.number.ByteElement;
import com.jaspersoft.studio.editor.preview.input.array.number.DoubleElement;
import com.jaspersoft.studio.editor.preview.input.array.number.FloatElement;
import com.jaspersoft.studio.editor.preview.input.array.number.IntegerElement;
import com.jaspersoft.studio.editor.preview.input.array.number.LongElement;
import com.jaspersoft.studio.editor.preview.input.array.number.ShortElement;

public class ElementDialog extends Dialog {
	private IParameter prm;
	private Map<String, AWElement> map = new LinkedHashMap<String, AWElement>();
	private String[] cvalues = null;
	private StackLayout stack;
	private Combo cmb;
	private AWElement current;
	private Composite cstack;

	public Object getValue() {
		if (current != null)
			return current.getValue();
		return null;
	}

	public void setValue(Object v) {
		for (AWElement key : map.values()) {
			if (key.getSupportedType().isAssignableFrom(v.getClass())) {
				current = key;
				current.setValue(v);
				break;
			}
		}
	}

	public void setType(Object v) {
		for (AWElement key : map.values()) {
			if (key.getSupportedType().isAssignableFrom(v.getClass())) {
				current = key;
				break;
			}
		}
	}

	public ElementDialog(Shell parentShell, IParameter prm) {
		super(parentShell);
		this.prm = prm;
		putMap(new StringElement());

		putMap(new ByteElement());
		putMap(new ShortElement());
		putMap(new IntegerElement());
		putMap(new LongElement());
		putMap(new BigIntegerElement());
		putMap(new FloatElement());
		putMap(new DoubleElement());
		putMap(new BigDecimalElement());

		putMap(new SqlDateElement());
		putMap(new TimestampElement());
		putMap(new TimeElement());
		putMap(new DateElement());

		putMap(new BooleanElement());

		putMap(new FileElement());
		putMap(new ImageElement());
	}

	private void putMap(AWElement widget) {
		Class<?> c = widget.getSupportedType();
		map.put(c.getCanonicalName(), widget);
	}

	private String[] getTypes() {
		if (cvalues == null) {
			Set<String> keySet = map.keySet();
			cvalues = keySet.toArray(new String[keySet.size()]);
		}
		return cvalues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Parameter: " + prm.getName());
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);
		((GridLayout) cmp.getLayout()).numColumns = 2;
		((GridLayout) cmp.getLayout()).makeColumnsEqualWidth = false;

		Class<?> c = ((ParameterJasper) prm).getParam().getNestedType();
		if (c != null) {
			// let's look if we support the type
			for (AWElement key : map.values()) {
				if (key.getSupportedType().isAssignableFrom(c)) {
					// this means all are only of this type
					current = key;
					break;
				}
			}
		}

		if (c == null || current == null) {
			new Label(cmp, SWT.WRAP).setText("Type");

			cmb = new Combo(cmp, SWT.READ_ONLY | SWT.SINGLE);
			cmb.addSelectionListener(new SelectionAdapter() {

				@Override
				public void widgetSelected(SelectionEvent e) {
					handleTypeChanged();
				}
			});
			String[] types = getTypes();
			cmb.setItems(types);
			if (current != null) {
				for (int i = 0; i < types.length; i++)
					if (types[i].equals(current.getSupportedType().getCanonicalName())) {
						cmb.select(i);
						break;
					}
			} else
				cmb.select(0);
		}

		cstack = new Composite(cmp, SWT.NONE);
		stack = new StackLayout();
		stack.marginWidth = 0;
		stack.marginHeight = 0;
		cstack.setLayout(stack);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		cstack.setLayoutData(gd);

		if (c != null && current != null)
			current.create(cstack);
		else {
			for (AWElement awe : map.values())
				awe.create(cstack);
		}
		handleTypeChanged();
		return cmp;
	}

	private void handleTypeChanged() {
		AWElement awe = current;
		if (cmb != null)
			awe = map.get(cmb.getText());
		if (awe != null) {
			current = awe;
			stack.topControl = awe.getControl();
			cstack.layout();
			cstack.update();
		}
	}
}
