/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.prm.dialog;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import bsh.util.JConsole;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.classname.ClassTypeCellEditor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ParameterDialog extends ATitledDialog {
	private int indx = -1;
	private JRDesignParameter prm;
	private Table table;
	private Text tname;
	private Text tdesc;
	private Text tclass;
	private Text tnest;
	private Button bclass;
	private Button bnest;
	private WTextExpression expr;
	private Button bPrompt;

	protected ParameterDialog(Shell parentShell, Table table) {
		this(parentShell, -1, new JRDesignParameter(), table);
	}

	protected ParameterDialog(Shell parentShell, int indx, JRDesignParameter prm, Table table) {
		super(parentShell);
		this.prm = prm;
		this.indx = indx;
		this.table = table;
		setTitle(Messages.ParameterDialog_0);
		setDescription(""); //$NON-NLS-1$
		setDefaultSize(600, 400);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(3, false));

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.common_name);

		tname = new Text(composite, SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		tname.setLayoutData(gd);
		tname.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setName();
			}
		});

		label = new Label(composite, SWT.NONE);
		label.setText(Messages.common_description);

		tdesc = new Text(composite, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 40;
		gd.horizontalSpan = 2;
		tdesc.setLayoutData(gd);
		tdesc.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				prm.setDescription(tdesc.getText());
			}
		});

		label = new Label(composite, SWT.NONE);
		label.setText(Messages.common_type);

		tclass = new Text(composite, SWT.BORDER);
		tclass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tclass.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				setValueClassName();
			}
		});

		bclass = new Button(composite, SWT.PUSH);
		bclass.setText("..."); //$NON-NLS-1$
		bclass.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String classname = ClassTypeCellEditor.getJavaClassDialog(UIUtils.getShell(), null);
				if (classname != null) {
					tclass.setText(classname);
					prm.setValueClassName(classname);
				}
			}
		});

		label = new Label(composite, SWT.NONE);
		label.setText(Messages.MParameter_nested_type_name);

		tnest = new Text(composite, SWT.BORDER);
		tnest.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		tnest.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				prm.setNestedTypeName(tnest.getText());
			}
		});

		bnest = new Button(composite, SWT.PUSH);
		bnest.setText("..."); //$NON-NLS-1$
		bnest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				String classname = ClassTypeCellEditor.getJavaClassDialog(UIUtils.getShell(), null);
				if (classname != null) {
					tnest.setText(classname);
					prm.setNestedTypeName(classname);
				}
			}
		});

		new Label(composite, SWT.NONE);

		bPrompt = new Button(composite, SWT.CHECK);
		bPrompt.setText(Messages.MParameter_is_for_prompting);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		bPrompt.setLayoutData(gd);
		bPrompt.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				prm.setForPrompting(bPrompt.getSelection());
			}
		});

		label = new Label(composite, SWT.NONE);
		label.setText(Messages.PropertiesComponent_1);
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));

		expr = new WTextExpression(composite, SWT.NONE, 1);
		expr.setExpressionContext(new ExpressionContext(JasperReportsConfiguration.getDefaultInstance()));
		expr.addModifyListener(new ExpressionModifiedListener() {
			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				prm.setDefaultValueExpression(expr.getExpression());
			}
		});
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		expr.setLayoutData(gd);

		fillValues();

		return composite;
	}

	public void fillValues() {

		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				setName();
				bPrompt.setSelection(prm.isForPrompting());
				tname.setText(Misc.nvl(prm.getName())); //$NON-NLS-1$
				tdesc.setText(Misc.nvl(prm.getDescription())); //$NON-NLS-1$
				tclass.setText(Misc.nvl(prm.getValueClassName())); //$NON-NLS-1$
				tnest.setText(Misc.nvl(prm.getNestedTypeName())); //$NON-NLS-1$ 
				expr.setExpression((JRDesignExpression) prm.getDefaultValueExpression());
			}
		});
	}

	public JRDesignParameter getPValue() {
		return prm;
	}

	protected void setValueClassName() {
		setValidationError(null);
		String pname = tclass.getText();
		if (pname.isEmpty()) {
			setValidationError(Messages.ParameterDialog_5);
			return;
		}
		prm.setValueClassName(pname);
	}

	protected void setName() {
		setValidationError(null);
		String pname = tname.getText();
		if (pname.isEmpty()) {
			setValidationError(Messages.ParameterDialog_6);
			return;
		}
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem ti = table.getItem(i);
			if (ti.getText(0).equals(pname) && i != indx) {
				setValidationError(Messages.ParameterDialog_7);
				return;
			}
		}
		prm.setName(pname);
	}

	private void setValidationError(String message) {
		getButton(IDialogConstants.OK_ID).setEnabled(message == null);
		setDescription(message);
	}
}
