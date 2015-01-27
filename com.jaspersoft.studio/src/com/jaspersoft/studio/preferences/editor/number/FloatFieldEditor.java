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

import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

public class FloatFieldEditor extends StringFieldEditor {
	private float minValidValue = 0;

	private float maxValidValue = Float.MAX_VALUE;

	private static final int DEFAULT_TEXT_LIMIT = 10;

	/**
	 * Creates a new integer field editor
	 */
	protected FloatFieldEditor() {
	}

	/**
	 * Creates an integer field editor.
	 * 
	 * @param name
	 *          the name of the preference this field editor works on
	 * @param labelText
	 *          the label text of the field editor
	 * @param parent
	 *          the parent of the field editor's control
	 */
	public FloatFieldEditor(String name, String labelText, Composite parent) {
		this(name, labelText, parent, DEFAULT_TEXT_LIMIT);
	}

	/**
	 * Creates an integer field editor.
	 * 
	 * @param name
	 *          the name of the preference this field editor works on
	 * @param labelText
	 *          the label text of the field editor
	 * @param parent
	 *          the parent of the field editor's control
	 * @param textLimit
	 *          the maximum number of characters in the text.
	 */
	public FloatFieldEditor(String name, String labelText, Composite parent, int textLimit) {
		init(name, labelText);
		setTextLimit(textLimit);
		setEmptyStringAllowed(false);
		setErrorMessage(JFaceResources.getString("FloatFieldEditor.errorMessage"));//$NON-NLS-1$
		createControl(parent);
	}

	/**
	 * Sets the range of valid values for this field.
	 * 
	 * @param min
	 *          the minimum allowed value (inclusive)
	 * @param max
	 *          the maximum allowed value (inclusive)
	 */
	public void setValidRange(float min, float max) {
		minValidValue = min;
		maxValidValue = max;
		setErrorMessage(JFaceResources.format("FloatFieldEditor.errorMessage", //$NON-NLS-1$
				new Object[] { new Float(min), new Float(max) }));
	}

	/*
	 * (non-Javadoc) Method declared on StringFieldEditor. Checks whether the entered String is a valid integer or not.
	 */
	protected boolean checkState() {

		Text text = getTextControl();

		if (text == null) {
			return false;
		}

		String numberString = text.getText();
		try {
			int number = Float.valueOf(numberString).intValue();
			if (number >= minValidValue && number <= maxValidValue) {
				clearErrorMessage();
				return true;
			}

			showErrorMessage();
			return false;

		} catch (NumberFormatException e1) {
			showErrorMessage();
		}

		return false;
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoad() {
		Text text = getTextControl();
		if (text != null) {
			float value = getPreferenceStore().getFloat(getPreferenceName());
			text.setText("" + value);//$NON-NLS-1$
			oldValue = "" + value; //$NON-NLS-1$
		}

	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doLoadDefault() {
		Text text = getTextControl();
		if (text != null) {
			float value = getPreferenceStore().getDefaultFloat(getPreferenceName());
			text.setText("" + value);//$NON-NLS-1$
		}
		valueChanged();
	}

	/*
	 * (non-Javadoc) Method declared on FieldEditor.
	 */
	protected void doStore() {
		Text text = getTextControl();
		if (text != null) {
			Float i = new Float(text.getText());
			getPreferenceStore().setValue(getPreferenceName(), i.floatValue());
		}
	}

	/**
	 * Returns this field editor's current value as an integer.
	 * 
	 * @return the value
	 * @exception NumberFormatException
	 *              if the <code>String</code> does not contain a parsable integer
	 */
	public float getFloatValue() throws NumberFormatException {
		return new Float(getStringValue()).floatValue();
	}
}
