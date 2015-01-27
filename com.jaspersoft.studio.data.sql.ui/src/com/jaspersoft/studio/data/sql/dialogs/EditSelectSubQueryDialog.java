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
package com.jaspersoft.studio.data.sql.dialogs;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;
import net.sf.jasperreports.eclipse.ui.validator.ValidatorUtil;

import org.eclipse.core.databinding.Binding;
import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.sql.QueryWriter;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectSubQuery;
import com.jaspersoft.studio.data.sql.validator.ColumnAliasStringValidator;
import com.jaspersoft.studio.utils.UIUtil;

public class EditSelectSubQueryDialog extends ATitledDialog {
	private MSelectSubQuery value;
	private String alias;
	private String aliasKeyword;
	private Text talias;
	private Combo keyword;

	public EditSelectSubQueryDialog(Shell parentShell) {
		super(parentShell);
		setTitle("Sub Query Dialog");
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);

	}

	public void setValue(MSelectSubQuery value) {
		this.value = value;
		setAlias(value.getAlias());
		setAliasKeyword(value.getAliasKeyword());
	}

	public void setAliasKeyword(String aliasKeyword) {
		this.aliasKeyword = aliasKeyword;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getAliasKeyword() {
		return aliasKeyword;
	}

	public String getAlias() {
		return alias;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);
		cmp.setLayout(new GridLayout(3, false));

		Label lbl = new Label(cmp, SWT.NONE);
		lbl.setText("Sub Query");
		UIUtil.setBold(lbl);
		lbl.setToolTipText(QueryWriter.writeSubQuery(value));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 300;
		lbl.setLayoutData(gd);

		keyword = new Combo(cmp, SWT.READ_ONLY);
		keyword.setItems(AMKeyword.ALIAS_KEYWORDS);

		talias = new Text(cmp, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.minimumWidth = 100;
		gd.horizontalIndent = 8;
		talias.setLayoutData(gd);

		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(SWTObservables.observeText(talias, SWT.Modify), PojoObservables.observeValue(this, "alias")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(keyword), PojoObservables.observeValue(this, "aliasKeyword"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new ColumnAliasStringValidator()), null);
		return cmp;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control createButtonBar = super.createButtonBar(parent);

		DataBindingContext bindingContext = new DataBindingContext();
		Binding b = bindingContext.bindValue(SWTObservables.observeText(talias, SWT.Modify), PojoObservables.observeValue(this, "alias"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new ColumnAliasStringValidator()), null);
		bindingContext.bindValue(SWTObservables.observeSelection(keyword), PojoObservables.observeValue(this, "aliasKeyword")); //$NON-NLS-1$

		ValidatorUtil.controlDecorator(b, getButton(IDialogConstants.OK_ID));
		return createButtonBar;
	}
}
