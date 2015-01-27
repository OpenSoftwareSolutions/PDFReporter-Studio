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
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.validator.routines.BigDecimalValidator;
import org.apache.commons.validator.routines.ByteValidator;
import org.apache.commons.validator.routines.DoubleValidator;
import org.apache.commons.validator.routines.FloatValidator;
import org.apache.commons.validator.routines.IntegerValidator;
import org.apache.commons.validator.routines.ShortValidator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.editor.preview.view.control.VParameters;

public class BigNumericInput extends ADataInput {
	private Text num;
	private Number min;
	private Number max;

	public boolean isForType(Class<?> valueClass) {
		return Number.class.isAssignableFrom(valueClass);
		// return (Long.class.isAssignableFrom(valueClass) || BigInteger.class.isAssignableFrom(valueClass)
		// || BigDecimal.class.isAssignableFrom(valueClass) || Float.class.isAssignableFrom(valueClass)
		// || Double.class.isAssignableFrom(valueClass) || Integer.class.isAssignableFrom(valueClass)
		// || Short.class.isAssignableFrom(valueClass) || Byte.class.isAssignableFrom(valueClass) || Number.class
		// .isAssignableFrom(valueClass));
	}

	@Override
	public void createInput(Composite parent, final IParameter param, final Map<String, Object> params) {
		super.createInput(parent, param, params);
		if (Number.class.isAssignableFrom(param.getValueClass())) {
			num = new Text(parent, SWT.BORDER | SWT.RIGHT);
			setMandatory(param, num);

			// setError(num, "");
			// hideError(num);

			num.setToolTipText(VParameters.createToolTip(param));
			num.addFocusListener(focusListener);
			updateInput();
			num.addListener(SWT.Verify, new Listener() {

				public void handleEvent(Event e) {
					try {
						hideError(num);
						String number = e.text;
						String oldText = ((Text) e.widget).getText();
						if (e.start != e.end)
							oldText = oldText.substring(0, e.start) + oldText.substring(e.end);
						number = oldText.substring(0, e.start) + e.text;
						if (oldText.length() - 1 > e.start + 1)
							number += oldText.substring(e.start + 1);

						if (number.equals("-")) //$NON-NLS-1$
							number = "-0";//$NON-NLS-1$
						if (number.equals(".")) //$NON-NLS-1$
							number = "0.";//$NON-NLS-1$

						if (number.isEmpty()) {
							e.doit = true;
							return;
						}

						if (param.getValueClass().equals(Long.class)) {
							Long.parseLong(number);
						} else if (param.getValueClass().equals(BigInteger.class)) {
							new BigInteger(number);
						} else if (param.getValueClass().equals(Float.class)) {
							e.doit = FloatValidator.getInstance().isValid(number, Locale.US);
						} else if (param.getValueClass().equals(Double.class)) {
							e.doit = DoubleValidator.getInstance().isValid(number, Locale.US);
						} else if (param.getValueClass().equals(Integer.class)) {
							e.doit = IntegerValidator.getInstance().isValid(number, Locale.US);
						} else if (param.getValueClass().equals(Short.class)) {
							e.doit = ShortValidator.getInstance().isValid(number, Locale.US);
						} else if (param.getValueClass().equals(Byte.class)) {
							e.doit = ByteValidator.getInstance().isValid(number, Locale.US);
						} else if (param.getValueClass().equals(BigDecimal.class)) {
							e.doit = BigDecimalValidator.getInstance().isValid(number, Locale.US);
						}
						if (e.doit) {
							if (min != null)
								if (param.isStrictMin()) {
									if (compareTo(getNumber(number), min) <= 0)
										setError(num, "Value can not be smaller than: " + min);
								} else if (compareTo(getNumber(number), min) < 0) {
									setError(num, "Value can not be smaller than: " + min);
								}
							if (max != null) {
								if (param.isStrictMax()) {
									if (compareTo(getNumber(number), max) >= 0)
										setError(num, "Value can not be greater than: " + max);
								} else if (compareTo(getNumber(number), max) > 0)
									setError(num, "Value can not be greater than: " + max);
							}
						}
					} catch (NumberFormatException ne) {
						e.doit = false;
					}
				}
			});
			if (param.getMinValue() != null)
				min = getNumber(param.getMinValue());
			if (param.getMaxValue() != null)
				max = getNumber(param.getMaxValue());
			ModifyListener listener = new ModifyListener() {

				public void modifyText(ModifyEvent e) {
					try {
						updateModel(getNumber(num.getText()));
					} catch (NumberFormatException ne) {
					}
				}
			};
			num.addModifyListener(listener);
			GridData gd = new GridData();
			gd.horizontalIndent = 8;
			gd.widthHint = 25 * getCharWidth(num) - 22;
			num.setLayoutData(gd);
			setNullable(param, num);
		}
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
		if (num.isDisposed())
			return;
		Object value = params.get(param.getName());
		if (value != null && value instanceof Number) {
			NumberFormat nformat = NumberFormat.getInstance(Locale.US);
			nformat.setGroupingUsed(false);
			num.setText(nformat.format(value));
		} else
			num.setText("");
		setDecoratorNullable(param);
	}

}
