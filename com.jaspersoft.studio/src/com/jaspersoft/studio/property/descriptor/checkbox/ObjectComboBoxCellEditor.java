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

import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.widgets.Composite;

public abstract class ObjectComboBoxCellEditor extends ComboBoxCellEditor {

	protected static final int NO_SELECTION = -1;

	private static final String[] EMPTY_ITEMS = new String[0];

	// Keep starting index and value around (thru doSetValue()) so that on
	// doGetValue() if current index is starting index we don't bother creating
	// a new value, we just return starting value. That way if you click on something
	// else and then click back it won't cause a change to occur by returning a different value.
	private int startingIndex = NO_SELECTION;
	private Object startingValue;

	/**
	 * @since 1.0.0
	 */
	public ObjectComboBoxCellEditor() {
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
	 * @param style
	 *          SWT style for combobox. They are CCombo controls styles.
	 * 
	 * @see CCombo
	 */
	public ObjectComboBoxCellEditor(Composite parent, String[] items, int style) {
		super(parent, items != null ? items : EMPTY_ITEMS, style);
	}

	/**
	 * Default for subclasses that just want an empty readonly combobox to be filled in later.
	 * 
	 * @param parent
	 * 
	 * @since 1.0.0
	 */
	public ObjectComboBoxCellEditor(Composite parent) {
		this(parent, null, SWT.READ_ONLY);
	}

	/**
	 * Create a celleditor with the given items and a READ_ONLY combobox.
	 * 
	 * @param parent
	 * @param items
	 * 
	 * @since 1.0.0
	 */
	public ObjectComboBoxCellEditor(Composite parent, String[] items) {
		this(parent, items, SWT.READ_ONLY);
	}

	/**
	 * Ask if this is the correct object, and if not set the msg, else let the validators handle it.
	 */
	@Override
	protected final boolean isCorrect(Object value) {
		String eMsg = isCorrectObject(value);
		if (eMsg == null || eMsg.length() == 0)
			return super.isCorrect(value); // Let validator give it a try.

		setErrorMessage(eMsg);
		return false;
	}

	/**
	 * Return an error message if this is not a valid value. This is a test in addition to the validators. In other words
	 * there are some basic criteria of what constitutes valid value. That is what isCorrectObject does. Then the
	 * validators will refine this and say whether it is valid in addition.
	 */
	protected abstract String isCorrectObject(Object value);

	/**
	 * Subclasses need to implement returning the object that the index represents. This is called when editing and a
	 * selection from the combobox is sent in and we need to send the object that it represents up to the validators. The
	 * index to convert will be passed in.
	 * 
	 * @param index
	 *          the index of the current selection. NO_SELECTION can be sent in if nothing selected.
	 */
	protected abstract Object doGetObject(int index);

	/**
	 * Return the value of the
	 */
	@Override
	protected Object doGetValue() {
		int selectedIndex = ((Integer) super.doGetValue()).intValue();
		// If the same as the starting value, don't bother getting a new value, just return the original.
		if (selectedIndex == startingIndex)
			return startingValue;
		// We now have a NEW starting value and index.
		startingIndex = selectedIndex;
		startingValue = doGetObject(selectedIndex);
		return startingValue;
	}

	/**
	 * The object is being passed in, return the index to be used in the editor.
	 * 
	 * It should return NO_SELECTION if the value can't be converted to an index. The errormsg will have already been set
	 * in this case.
	 */
	protected abstract int doGetIndex(Object value);

	/**
	 * This sets the index of the selection that is to go into the editor. This can be used to set a select at any time.
	 * Typically this isn't necessary, it is handle by doSetValue.
	 */
	protected final void doSetEditorSelection(int selection) {
		super.doSetValue(new Integer(selection));
	}

	/**
	 * Return the currently selected index.
	 * 
	 * @return the current selection index or <code>NO_SELECTION</code> if nothing selected.
	 * 
	 * @since 1.0.0
	 */
	protected int getSelectionIndex() {
		return ((Integer) super.doGetValue()).intValue();
	}

	/**
	 * This is called when a doSetValue has been called.
	 * 
	 * This is not abstract, but a default implementation of doSetObject. It does nothing. Implementers may do something
	 * else with it, such as build list when the value changes.
	 */
	protected void doSetObject(Object value) {
	}

	/**
	 * A new value is being set into the editor. doSetObject will be called to allow the implementers to do something when
	 * a new value is sent in. It is final so that startingIndex and startingValue are correctly set. doSetObject() can be
	 * used to apply customization.
	 */
	@Override
	protected final void doSetValue(Object value) {
		doSetObject(value); // Let implementers do something with it.
		startingIndex = doGetIndex(value);
		startingValue = value;
		doSetEditorSelection(startingIndex);
	}

}
