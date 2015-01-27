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
package com.jaspersoft.studio.preferences.editor.number;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

public class SpinnerFieldEditor extends FieldEditor {

	private Spinner spinnerCtrl;
	private int digits = 0;
	private int oldValue;

	public SpinnerFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, parent);
	}

	public SpinnerFieldEditor(String name, String labelText, Composite parent, int digits) {
		super(name, labelText, parent);
		this.digits = digits;
	}

	protected void valueChanged() {
		setPresentsDefaultValue(false);

		int newValue = spinnerCtrl.getSelection();
		if (newValue != oldValue) {
			fireValueChanged(VALUE, new Integer(oldValue), new Integer(newValue));
			oldValue = newValue;
		}
	}

	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) spinnerCtrl.getLayoutData();
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
	}

	protected void doFillIntoGrid(Composite parent, int numColumns) {
		getLabelControl(parent);

		spinnerCtrl = new Spinner(parent, SWT.BORDER);
		spinnerCtrl.setDigits(digits);
		spinnerCtrl.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				valueChanged();
			}

			public void widgetSelected(SelectionEvent e) {
				valueChanged();
			}
		});

		spinnerCtrl.setDigits(digits);
		GridData gd = new GridData();
		gd.horizontalSpan = numColumns - 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		spinnerCtrl.setLayoutData(gd);
	}

	protected void doLoad() {
		if (spinnerCtrl != null) {
			int value = getPreferenceStore().getInt(getPreferenceName());
			spinnerCtrl.setSelection(value);
			oldValue = value;
		}
	}

	protected void doLoadDefault() {
		if (spinnerCtrl != null) {
			int defaultValue = getPreferenceStore().getDefaultInt(getPreferenceName());
			spinnerCtrl.setSelection(defaultValue);
		}
		valueChanged();
	}

	protected void doStore() {
		getPreferenceStore().setValue(getPreferenceName(), spinnerCtrl.getSelection());
	}

	public int getNumberOfControls() {
		if (getLabelControl() == null) return 1;
		return 2;
	}

	public void setIncrement(int increment) {
		spinnerCtrl.setIncrement(increment);
	}

	public int getIncrement() {
		return (spinnerCtrl.getIncrement());
	}

	public void setPageIncrement(int increment) {
		spinnerCtrl.setPageIncrement(increment);
	}

	public int getPageIncrement() {
		return (spinnerCtrl.getPageIncrement());
	}

	public void setMaximum(int maximum) {
		spinnerCtrl.setMaximum(maximum);
	}

	public int getMaximum() {
		return (spinnerCtrl.getMaximum());
	}

	public void setMinimum(int minimum) {
		spinnerCtrl.setMinimum(minimum);
	}

	public int getMinimum() {
		return (spinnerCtrl.getMinimum());
	}

	public int getIntValue() {
		return (spinnerCtrl.getSelection());
	}

	public void setEnabled(boolean enabled, Composite parent) {
		super.setEnabled(enabled, parent);
		spinnerCtrl.setEnabled(enabled);
	}

	public Spinner getSpinnerControl() {
		return spinnerCtrl;
	}
}
