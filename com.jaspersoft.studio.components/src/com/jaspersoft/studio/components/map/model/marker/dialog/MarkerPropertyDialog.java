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
package com.jaspersoft.studio.components.map.model.marker.dialog;

import java.util.List;

import net.sf.jasperreports.components.map.StandardItemProperty;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.property.infoList.ElementDescription;
import com.jaspersoft.studio.property.infoList.SelectableComposite;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.Misc;

public class MarkerPropertyDialog extends Dialog {
	private StandardItemProperty value;
	private Composite vexp;
	private Composite vcmp;
	private StackLayout stackLayout;
	private Text tvalue;
	private Button buseexpr;
	private WTextExpression evalue;
	private Text cprop;
	private ExpressionContext expContext;
	private String propsDescFile;

	public MarkerPropertyDialog(Shell parentShell, String propsDescFile) {
		super(parentShell);
		this.propsDescFile = propsDescFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.MarkerPropertyDialog_DialogTitle);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.MarkerPropertyDialog_PropertyName);

		cprop = new Text(composite, SWT.BORDER);
		cprop.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		cprop.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				value.setName(cprop.getText());
			}
		});

		buseexpr = new Button(composite, SWT.CHECK);
		buseexpr.setText(Messages.MarkerPropertyDialog_UseExpression);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		buseexpr.setLayoutData(gd);

		stackComposite = new Composite(composite, SWT.NONE);
		stackLayout = new StackLayout();
		stackComposite.setLayout(stackLayout);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		stackComposite.setLayoutData(gd);

		vcmp = createValueControl(stackComposite);

		vexp = createValueExpressionControl(stackComposite);

		stackLayout.topControl = vcmp;
		buseexpr.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				changeValueOrExpression();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetDefaultSelected(e);
			}
		});

		List<ElementDescription> propertyDescriptions = ElementDescription.getPropertiesInformation(propsDescFile);
		if(!propertyDescriptions.isEmpty()) {
			final SelectableComposite infoPanel = new SelectableComposite(composite);
			infoPanel.setItems(propertyDescriptions);
			GridData infoGD = new GridData(SWT.FILL,SWT.FILL,true,true,2,1);
			infoGD.heightHint = 200;
			infoGD.widthHint = 500;
			infoGD.verticalIndent=5;
			infoPanel.setLayoutData(infoGD);
			infoPanel.SetDoubleClickListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					cprop.setText(infoPanel.getSelectedElement().getName());
				}
			});
		}
		
		fillValue(value);
		return composite;
	}

	private Composite createValueExpressionControl(Composite cmp) {
		Composite composite = new Composite(cmp, SWT.NONE);
		composite.setLayout(new GridLayout());

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.MarkerPropertyDialog_ValueExpression);

		evalue = new WTextExpression(composite, SWT.NONE, 1);
		evalue.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		evalue.addModifyListener(new ExpressionModifiedListener() {
			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				if (!isRefresh)
					value.setValueExpression(evalue.getExpression());
			}
		});

		return composite;
	}

	private Composite createValueControl(Composite cmp) {
		Composite composite = new Composite(cmp, SWT.NONE);
		composite.setLayout(new GridLayout());

		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.MarkerPropertyDialog_Value);

		tvalue = new Text(composite, SWT.BORDER);
		tvalue.setLayoutData(new GridData(GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL));
		tvalue.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!isRefresh) {
					value.setValue(tvalue.getText());
					value.setValueExpression(null);
				}
			}
		});
		return composite;
	}

	private boolean isMandatory = false;

	public void setValue(StandardItemProperty value, ExpressionContext expContext, boolean isMandatory) {
		this.value = value;
		this.expContext = expContext;
		this.isMandatory = isMandatory;
	}

	private boolean isRefresh = false;
	private Composite stackComposite;

	private void fillValue(StandardItemProperty value) {
		isRefresh = true;
		if (isMandatory)
			cprop.setEnabled(false);
		evalue.setExpressionContext(expContext);
		cprop.setText(Misc.nvl(value.getName()));
		if (value.getValueExpression() != null) {
			buseexpr.setSelection(true);
			evalue.setExpression((JRDesignExpression) value.getValueExpression());
		} else {
			buseexpr.setSelection(false);
			tvalue.setText(Misc.nvl(value.getValue()));
		}
		changeValueOrExpression();
		isRefresh = false;
	}

	public void changeValueOrExpression() {
		stackLayout.topControl = buseexpr.getSelection() ? vexp : vcmp;
		stackComposite.layout();
		if (buseexpr.getSelection()) {
			value.setValueExpression(evalue.getExpression());
		} else
			value.setValue(tvalue.getText());
	}

}
