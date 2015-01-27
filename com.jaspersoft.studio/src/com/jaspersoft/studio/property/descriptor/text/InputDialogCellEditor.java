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
package com.jaspersoft.studio.property.descriptor.text;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.ICellEditorValidator;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;

public class InputDialogCellEditor extends DialogCellEditor {

	/**
	 * Gap between between image and text in pixels.
	 */
	private static final int GAP = 6;

	/**
	 * The composite widget containing the color and RGB label widgets
	 */
	private Composite composite;
	private String title;
	/**
	 * The label widget showing the RGB values.
	 */
	private Label textLabel;

	/**
	 * Internal class for laying out this cell editor.
	 */
	private class CellLayout extends Layout {
		public Point computeSize(Composite editor, int wHint, int hHint, boolean force) {
			if (wHint != SWT.DEFAULT && hHint != SWT.DEFAULT) {
				return new Point(wHint, hHint);
			}
			Point rgbSize = textLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			return new Point(GAP + rgbSize.x, rgbSize.y);
		}

		public void layout(Composite editor, boolean force) {
			Rectangle bounds = editor.getClientArea();
			Point rgbSize = textLabel.computeSize(SWT.DEFAULT, SWT.DEFAULT, force);
			int ty = (bounds.height - rgbSize.y) / 2;
			if (ty < 0) {
				ty = 0;
			}
			textLabel.setBounds(GAP - 1, ty, bounds.width - GAP, bounds.height);
		}
	}

	/**
	 * Creates a new color cell editor parented under the given control. The cell editor value is black (
	 * <code>RGB(0,0,0)</code>) initially, and has no validator.
	 * 
	 * @param parent
	 *          the parent control
	 */
	public InputDialogCellEditor(Composite parent, String title) {
		this(parent, SWT.NONE, title);
		setValue(new JRDesignQuery());
	}

	/**
	 * Creates a new color cell editor parented under the given control. The cell editor value is black (
	 * <code>RGB(0,0,0)</code>) initially, and has no validator.
	 * 
	 * @param parent
	 *          the parent control
	 * @param style
	 *          the style bits
	 */
	public InputDialogCellEditor(Composite parent, int style, String title) {
		super(parent, style);
		doSetValue(""); //$NON-NLS-1$
	}

	@Override
	protected Control createContents(Composite cell) {
		Color bg = cell.getBackground();
		composite = new Composite(cell, getStyle());
		composite.setBackground(bg);
		composite.setLayout(new CellLayout());
		textLabel = new Label(composite, SWT.LEFT);
		textLabel.setBackground(bg);
		textLabel.setFont(cell.getFont());
		return composite;
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		ValueHandler valueHandler = new ValueHandler();
		InputDialog dialog = new InputDialog(cellEditorWindow.getShell(), title, "", (String) getValue(), valueHandler); //$NON-NLS-1$
		return dialog.open() == Window.OK ? dialog.getValue() : null;
	}

	@Override
	protected void updateContents(Object value) {
		String jrQuery = (String) value;
		// XXX: We don't have a value the first time this method is called".
		if (jrQuery == null) {
			jrQuery = ""; //$NON-NLS-1$
		}

		textLabel.setText(jrQuery);
	}

	protected static class ValueHandler implements ICellEditorValidator, IInputValidator {

		public ValueHandler() {
		}

		public String isValid(Object object) {
			// try {
			// ;
			// } catch (Exception exception) {
			// return "error";
			// }
			return null;
		}

		public String isValid(String text) {
			return isValid((Object) text);
		}

	}
}
