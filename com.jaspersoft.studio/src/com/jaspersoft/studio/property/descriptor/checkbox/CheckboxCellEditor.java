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
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.property.descriptor.NullEnum;

public class CheckboxCellEditor extends CellEditor {
	private NullEnum canBeNull = NullEnum.NOTNULL;
	private Boolean value = Boolean.FALSE;

	public CheckboxCellEditor() {
		super();
	}

	public CheckboxCellEditor(Composite parent, int style, NullEnum canBeNull) {
		super(parent, style);
		this.canBeNull = canBeNull;
	}

	public CheckboxCellEditor(Composite parent) {
		super(parent);
	}

	@Override
	protected Control createControl(Composite parent) {
		return new Composite(parent, SWT.NONE);
	}

	@Override
	protected Object doGetValue() {
		return value;
	}

	boolean focused = false;

	@Override
	protected void doSetFocus() {
		focused = true;
	}

	@Override
	protected void focusLost() {
		focused = false;
		super.focusLost();
	}

	@Override
	protected void doSetValue(Object value) {
		if (value instanceof Boolean)
			this.value = (Boolean) value;
	}

	/**
	 * The <code>CheckboxCellEditor</code> implementation of this <code>CellEditor</code> framework method simulates the
	 * toggling of the checkbox control and notifies listeners with <code>ICellEditorListener.applyEditorValue</code>.
	 */
	@Override
	public void activate() {
		if (focused) {
			if (canBeNull != NullEnum.NOTNULL) {
				if (value == null)
					value = true;
				else if (value)
					value = !value;
				else
					value = null;
			} else
				value = !value;
			fireApplyEditorValue();
		}
	}

	@Override
	public void activate(ColumnViewerEditorActivationEvent activationEvent) {
		if (activationEvent.eventType != ColumnViewerEditorActivationEvent.TRAVERSAL) {
			super.activate(activationEvent);
		}
	}
}
