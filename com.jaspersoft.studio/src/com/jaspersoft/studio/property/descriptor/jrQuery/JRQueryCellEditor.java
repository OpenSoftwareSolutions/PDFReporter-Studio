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
package com.jaspersoft.studio.property.descriptor.jrQuery;

import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.property.dataset.dialog.DatasetDialog;
import com.jaspersoft.studio.property.descriptor.ATextDialogRWCellEditor;
import com.jaspersoft.studio.property.descriptor.NullEnum;

public class JRQueryCellEditor extends ATextDialogRWCellEditor {

	/**
	 * Creates a new color cell editor parented under the given control. The cell editor value is black (
	 * <code>RGB(0,0,0)</code>) initially, and has no validator.
	 * 
	 * @param parent
	 *          the parent control
	 */
	public JRQueryCellEditor(Composite parent) {
		this(parent, SWT.NONE);
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
	public JRQueryCellEditor(Composite parent, int style) {
		super(parent, style);
	}

	@Override
	protected Object openDialogBox(Control cellEditorWindow) {
		MQuery mquery = (MQuery) getValue();
		MDataset mdataset = mquery.getMdataset();
		Shell shell = cellEditorWindow.getShell();
		DatasetDialog dlg = new DatasetDialog(shell, mdataset, mquery.getJasperConfiguration(), null);
		if (dlg.open() == Window.OK)
			return dlg.getCommand();

		return null;
	}

	private LabelProvider labelProvider;

	@Override
	protected void updateContents(Object value) {
		if (getDefaultLabel() == null) {
			return;
		}
		if (labelProvider == null)
			labelProvider = new JRQueryLabelProvider(NullEnum.NULL);
		String text = labelProvider.getText(value);
		getDefaultLabel().setText(text);
	}

	@Override
	protected Object doGetValue() {
		Object val = super.doGetValue();
		if (isDirty() && val instanceof MQuery) {
			final MQuery m = (MQuery) val;
			JRDesignQuery dexpr = (JRDesignQuery) m.getValue();
			if (dexpr == null) {
				dexpr = new JRDesignQuery();
				dexpr.setLanguage("sql");
				m.setValue(dexpr);
			}
			final JRDesignQuery e = dexpr;
			Display.getCurrent().asyncExec(new Runnable() {

				public void run() {
					// m.setPropertyValue(JRDesignExpression.PROPERTY_TEXT, text.getText());
					e.setText(text.getText());
				}
			});
			return new MQuery(dexpr, m.getMdataset());
		}
		return val;
	}

	@Override
	protected void doSetValue(Object value) {
		super.doSetValue(value);
		if (value instanceof MQuery) {
			MQuery expression = (MQuery) value;

			text.removeModifyListener(getModifyListener());
			String pvalue = (String) expression.getPropertyValue(JRDesignQuery.PROPERTY_TEXT);
			if (pvalue == null)
				pvalue = "";
			text.setText(pvalue);
			text.addModifyListener(getModifyListener());
		}
	}

}
