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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Spinner;

import com.jaspersoft.studio.editor.preview.view.control.VParameters;
import com.jaspersoft.studio.utils.Misc;

/**
 * 
 * This is a custom DataInput control done for edit the maximum number of record to display. This combine a checkbox to
 * say if or not consider this maximum, and a spinner that can be used if the checkbox is selected, to choose the
 * maximum amount of record to display. When the checkbox is not selected all the available records are show
 * 
 * @author Orlandin Marco
 * 
 */
public class BooleanNumericInput extends ADataInput {
	private Button bbuton;
	private Spinner num;
	private int min;
	private int max;

	public boolean isForType(Class<?> valueClass) {
		return true;
	}

	@Override
	public void createInput(Composite parent, final IParameter param, final Map<String, Object> params) {
		super.createInput(parent, param, params);
		if (Number.class.isAssignableFrom(param.getValueClass())) {
			min = 0;
			max = Integer.MAX_VALUE;

			Composite container = new Composite(parent, SWT.NONE);
			container.setLayout(new GridLayout(2, false));
			bbuton = new Button(container, SWT.CHECK);
			bbuton.setText("Limit the number of records to");

			num = new Spinner(container, SWT.BORDER);
			num.addFocusListener(focusListener);
			num.setToolTipText(VParameters.createToolTip(param));
			updateInput();

			final SelectionAdapter adapter1 = new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					if (!bbuton.getSelection()) {
						params.remove(param.getName());
						num.setEnabled(false);
					} else {
						Number n = getNumber();
						updateModel(n);
						num.setEnabled(true);
					}
					setDecoratorNullable(param);
				}
			};
			bbuton.addSelectionListener(adapter1);

			final ModifyListener listener2 = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					num.removeModifyListener(this);
					textModifyEvent();
					num.addModifyListener(this);
				}
			};
			num.addModifyListener(listener2);

			if (param.getMinValue() != null) {
				int minval = new Integer(param.getMinValue()).intValue();
				if (!param.isStrictMin())
					minval++;
				num.setMinimum(minval);
				min = minval;
			}
			if (param.getMaxValue() != null) {
				int maxval = new Integer(param.getMaxValue()).intValue();
				if (!param.isStrictMax())
					maxval--;
				num.setMaximum(maxval);
				max = maxval;
			}

			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.horizontalIndent = 2;
			gd.widthHint = 40;
			num.setLayoutData(gd);

			setMandatory(param, num);
			setNullable(param, bbuton);
		}
	}

	private Number getNumber() {
		Number n = null;
		String text = num.getText();
		if (!text.trim().isEmpty()) {
			if (param.getValueClass().equals(Integer.class)) {
				n = new Integer(Misc.nvl(num.getText()));
			} else if (param.getValueClass().equals(Byte.class)) {
				n = new Byte(Misc.nvl(num.getText()));
			} else if (param.getValueClass().equals(Short.class)) {
				n = new Short(Misc.nvl(num.getText()));
			}
		}
		return n;
	}

	private void textModifyEvent() {
		Number n = getNumber();
		if (n != null) {
			updateModel(n);
		}
		updateInput();
	}

	protected int compareTo(Number n1, Number n2) {
		if (param.getValueClass().equals(Long.class)) {
			return ((Long) n1).compareTo((Long) n2);
		} else if (param.getValueClass().equals(BigInteger.class)) {
			return ((BigInteger) n1).compareTo((BigInteger) n2);
		} else if (param.getValueClass().equals(Float.class)) {
			return ((Float) n1).compareTo((Float) n2);
		} else if (param.getValueClass().equals(Double.class)) {
			return ((Double) n1).compareTo((Double) n2);
		} else if (param.getValueClass().equals(Integer.class)) {
			return ((Integer) n1).compareTo((Integer) n2);
		} else if (param.getValueClass().equals(Short.class)) {
			return ((Short) n1).compareTo((Short) n2);
		} else if (param.getValueClass().equals(Byte.class)) {
			return ((Byte) n1).compareTo((Byte) n2);
		} else if (param.getValueClass().equals(BigDecimal.class)) {
			return ((BigDecimal) n1).compareTo((BigDecimal) n2);
		}
		return 0;
	}

	protected Number getNumber(String number) throws NumberFormatException {
		if (param.getValueClass().equals(Long.class)) {
			return new Long(number);
		} else if (param.getValueClass().equals(BigInteger.class)) {
			return new BigInteger(number);
		} else if (param.getValueClass().equals(Float.class)) {
			return new Float(number);
		} else if (param.getValueClass().equals(Double.class)) {
			return new Double(number);
		} else if (param.getValueClass().equals(Integer.class)) {
			return new Integer(number);
		} else if (param.getValueClass().equals(Short.class)) {
			return new Short(number);
		} else if (param.getValueClass().equals(Byte.class)) {
			return new Byte(number);
		} else if (param.getValueClass().equals(BigDecimal.class)) {
			return new BigDecimal(number);
		}
		return null;
	}

	public void updateInput() {
		Object value = params.get(param.getName());
		if (value != null && value instanceof Number) {
			if (!value.equals(getNumber())) {
				int val = ((Number) value).intValue();
				num.setValues(val, min, max, 0, 1, 10);
				num.setSelection(num.getText().length());
				bbuton.setSelection(true);
				num.setEnabled(true);
			}
		} else {
			bbuton.setSelection(false);
			num.setEnabled(false);
		}
		setDecoratorNullable(param);
	}

}
