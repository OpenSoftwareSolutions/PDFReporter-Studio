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
package com.jaspersoft.studio.property.descriptor.checkbox;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.property.descriptor.NullEnum;

public class BCheckBoxCellEditor extends ComboBoxCellEditor {
	/**
	 * The custom combo box control.
	 */
	CCombo comboBox;
	private NullEnum canBeNull = NullEnum.NOTNULL;

	/**
	 * Creates a new cell editor with no control and no st of choices. Initially, the cell editor has no cell validator.
	 * 
	 * @since 2.1
	 * @see CellEditor#setStyle
	 * @see CellEditor#create
	 * @see ComboBoxCellEditor#setItems
	 * @see CellEditor#dispose
	 */
	public BCheckBoxCellEditor() {
		super();
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of choices and parented under the given control.
	 * The cell editor value is the zero-based index of the selected item. Initially, the cell editor has no cell
	 * validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *          the parent control
	 * @param items
	 *          the list of strings for the combo box
	 */
	public BCheckBoxCellEditor(Composite parent, NullEnum canBeNull) {
		this(parent, canBeNull, SWT.NONE);
	}

	/**
	 * Creates a new cell editor with a combo containing the given list of choices and parented under the given control.
	 * The cell editor value is the zero-based index of the selected item. Initially, the cell editor has no cell
	 * validator and the first item in the list is selected.
	 * 
	 * @param parent
	 *          the parent control
	 * @param items
	 *          the list of strings for the combo box
	 * @param style
	 *          the style bits
	 * @since 2.1
	 */
	public BCheckBoxCellEditor(Composite parent, NullEnum canBeNull, int style) {
		super(parent, getStringItems(canBeNull), style);
		this.canBeNull = canBeNull;
	}

	private static String[] getStringItems(NullEnum canBeNull) {
		if (canBeNull.equals(NullEnum.NOTNULL))
			return new String[] { "true", "false" };
		if (canBeNull.equals(NullEnum.INHERITED))
			return new String[] { NullEnum.INHERITED.getName(), "true", "false" };
		return new String[] { NullEnum.NULL.getName(), "true", "false" };
	}

	/*
	 * (non-Javadoc) Method declared on CellEditor.
	 */
	@Override
	protected Control createControl(Composite parent) {
		comboBox = (CCombo) super.createControl(parent);
		return comboBox;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method returns the
	 * zero-based index of the current selection.
	 * 
	 * @return the zero-based index of the current selection wrapped as an <code>Integer</code>
	 */
	@Override
	protected Object doGetValue() {
		int selectionIndex = comboBox.getSelectionIndex();
		if (selectionIndex < 0) {
			selectionIndex = 0;
			comboBox.setItems(getItems());
			comboBox.select(selectionIndex);
		}
		if (canBeNull.equals(NullEnum.NOTNULL))
			return selectionIndex == 0;
		if (selectionIndex == 0)
			return null;
		return selectionIndex == 1;
	}

	public CCombo getComboBox() {
		return comboBox;
	}

	/**
	 * The <code>ComboBoxCellEditor</code> implementation of this <code>CellEditor</code> framework method accepts a
	 * zero-based index of a selection.
	 * 
	 * @param value
	 *          the zero-based index of the selection wrapped as an <code>Integer</code>
	 */
	@Override
	protected void doSetValue(Object value) {
		if (comboBox != null) {
			if (value == null && !canBeNull.equals(NullEnum.NOTNULL))
				super.doSetValue(new Integer(0));
			else if (value instanceof Boolean) {
				Boolean v = (Boolean) value;
				if (canBeNull.equals(NullEnum.NOTNULL)) {
					if (v)
						super.doSetValue(new Integer(0));
					else
						super.doSetValue(new Integer(1));
				} else {
					if (v)
						super.doSetValue(new Integer(1));
					else
						super.doSetValue(new Integer(2));
				}
			}
		}
	}
}
