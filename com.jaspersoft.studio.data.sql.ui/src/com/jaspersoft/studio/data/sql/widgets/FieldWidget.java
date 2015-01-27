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
package com.jaspersoft.studio.data.sql.widgets;

import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.sql.dialogs.FromTableColumnsDialog;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.operand.FieldOperand;
import com.jaspersoft.studio.data.sql.text2model.ConvertUtil;

public class FieldWidget extends AOperandWidget<FieldOperand> {

	private Text txt;

	public FieldWidget(Composite parent, FieldOperand operand) {
		super(parent, SWT.NONE, operand);
	}

	@Override
	protected void createWidget(Composite parent) {
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 2;
		layout.marginWidth = 0;
		layout.horizontalSpacing = 0;
		layout.verticalSpacing = 3;
		setLayout(layout);

		final FieldOperand v = getValue();

		txt = new Text(this, SWT.READ_ONLY | SWT.BORDER);
		txt.setText(ConvertUtil.cleanDbNameFull(v.toSQLString()));
		txt.setToolTipText(ConvertUtil.cleanDbNameFull(v.toSQLString()));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 200;
		txt.setLayoutData(gd);

		Button b = new Button(this, SWT.PUSH);
		b.setText("...");
		b.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FromTableColumnsDialog dialog = new FromTableColumnsDialog(UIUtils.getShell(), SWT.SINGLE);
				dialog.setSelection(v.getExpression());
				if (dialog.open() == Dialog.OK) {
					Map<MSQLColumn, MFromTable> cmap = dialog.getColumns();
					for (MSQLColumn t : cmap.keySet())
						v.setValue(t, cmap.get(t));
				}
				txt.setText(ConvertUtil.cleanDbNameFull(v.toSQLString()));
				txt.setToolTipText(v.toSQLString());
			}
		});
	}

}
