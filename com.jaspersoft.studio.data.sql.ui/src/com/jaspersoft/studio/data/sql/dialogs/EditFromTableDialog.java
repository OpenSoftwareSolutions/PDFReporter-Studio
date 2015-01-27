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
import org.eclipse.core.databinding.validation.ValidationStatus;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.text2model.ConvertUtil;
import com.jaspersoft.studio.data.sql.validator.TableAliasStringValidator;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;

public class EditFromTableDialog extends ATitledDialog {
	private MFromTable mFromTable;
	private String alias;
	private String aliasKeyword;
	private Text talias;
	private Combo keyword;

	public EditFromTableDialog(Shell parentShell) {
		super(parentShell);
		setTitle("Table Dialog");
	}

	public void setValue(MFromTable value) {
		this.mFromTable = value;
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

		Text lbl = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		lbl.setText(ConvertUtil.cleanDbNameFull(mFromTable.getValue().toSQLString()));
		lbl.setToolTipText(lbl.getText());
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		lbl.setLayoutData(gd);

		keyword = new Combo(cmp, SWT.READ_ONLY);
		keyword.setItems(AMKeyword.ALIAS_KEYWORDS);

		talias = new Text(cmp, SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		gd.horizontalIndent = 8;
		talias.setLayoutData(gd);

		return cmp;
	}

	@Override
	protected Control createButtonBar(Composite parent) {
		Control createButtonBar = super.createButtonBar(parent);

		DataBindingContext bindingContext = new DataBindingContext();
		Binding b = bindingContext.bindValue(SWTObservables.observeText(talias, SWT.Modify), PojoObservables.observeValue(this, "alias"), //$NON-NLS-1$
				new UpdateValueStrategy().setAfterConvertValidator(new TableAliasStringValidator() {
					@Override
					public IStatus validate(final Object value) {
						IStatus status = super.validate(value);
						if (status.equals(Status.OK_STATUS) && value != null && !((String) value).isEmpty()) {
							ModelVisitor<Boolean> mv = new ModelVisitor<Boolean>(mFromTable.getRoot()) {
								@Override
								public boolean visit(INode n) {
									if (n instanceof MFrom || n instanceof MFromTable) {
										if (n instanceof MFromTable && n != mFromTable) {
											String al = ((MFromTable) n).getAlias();
											if (al != null && al.equals(value)) {
												setObject(Boolean.TRUE);
												return false;
											}
										}
										return true;
									}
									return false;
								}
							};
							if (mv.getObject() != null && mv.getObject() == true)
								return ValidationStatus.error("This alias already exists in the FROM tables list. Please type another one.");
						}
						return status;
					}
				}), null);
		bindingContext.bindValue(SWTObservables.observeSelection(keyword), PojoObservables.observeValue(this, "aliasKeyword"));

		ValidatorUtil.controlDecorator(b, getButton(IDialogConstants.OK_ID));
		return createButtonBar;
	}
}
