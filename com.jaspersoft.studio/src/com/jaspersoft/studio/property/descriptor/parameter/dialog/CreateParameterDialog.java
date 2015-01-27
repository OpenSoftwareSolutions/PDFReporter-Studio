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
package com.jaspersoft.studio.property.descriptor.parameter.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 * This dialog is used to define a pair of parameter name and expression for 
 * the parameter of a dataset run. since the parameter must match an existing parameter
 * inside the original dataset then the selection of the name can be made only between
 * a fixed set of parameters
 * 
 * @author Orlandin Marco
 *
 */
public class CreateParameterDialog extends Dialog {

	/**
	 * Widget used to input the expression
	 */
	private WTextExpression expression;
	
	/**
	 * Widget where the parameter name can be selected
	 */
	private Combo paramterName;
	
	/**
	 * Available parameters name
	 */
	private String[] comboInput;
	
	/**
	 * Selected parameter name
	 */
	private String selectedItem = null;
	
	/**
	 * Selected expression
	 */
	private JRDesignExpression selectedExpression;
	
	/**
	 * Parameter used to fill the dialog controls when the dialog 
	 * is used to edit an existing parameter
	 */
	private JRDatasetParameter selectedParam = null;
	
	/**
	 * Actual expression context
	 */
	private ExpressionContext expContext;
	
	/**
	 * Create the dialog
	 * 
	 * @param parentShell parent shell
	 * @param comboInput names of the available parameters
	 */
	public CreateParameterDialog(Shell parentShell, String[] comboInput) {
		super(parentShell);
		this.comboInput = comboInput;
	}
	
	/**
	 * Create the dialog
	 * 
	 * @param parentShell parent shell
	 * @param comboInput names of the available parameters
	 * @param selectedParam parameter that will be edited
	 */
	public CreateParameterDialog(Shell parentShell, String[] comboInput, JRDatasetParameter selectedParam) {
		this(parentShell,comboInput);
		this.selectedParam = selectedParam;
	}
	
	/**
	 * Set the shell name
	 */
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Parameter definition dialog");
	}
	
	/**
	 * Set the expression context that will be used inside the expression editor if it is 
	 * opened 
	 * 
	 * @param expContext expression context
	 */
	public void setExpressionContext(ExpressionContext expContext){
		this.expContext = expContext;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(2,false));
		GridData mainData = new GridData(GridData.FILL_BOTH);
		mainData.widthHint = 400;
		mainData.heightHint = 100;
		mainComposite.setLayoutData(mainData);
		
		new Label(mainComposite, SWT.NONE).setText(Messages.ParameterPage_parameter);
		
		paramterName = new Combo(mainComposite, SWT.READ_ONLY);
		paramterName.setItems(comboInput);
		paramterName.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		paramterName.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (comboInput.length == 0 || paramterName.getSelectionIndex() == -1){
					getButton(IDialogConstants.OK_ID).setEnabled(false);
				} else {
					getButton(IDialogConstants.OK_ID).setEnabled(true);
				}
			}
		});
		
		new Label(mainComposite, SWT.NONE).setText(Messages.common_expression);
		expression = new WTextExpression(mainComposite, SWT.NONE);
		expression.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		if (expContext != null) expression.setExpressionContext(expContext);
		
		initializeFields();
		return mainComposite;
	}
	
	/**
	 * Disable ok button if there aren't parameter names to select
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		if (comboInput.length == 0 || paramterName.getSelectionIndex() == -1){
			getButton(IDialogConstants.OK_ID).setEnabled(false);
		}
	}
	
	/**
	 * Initialize the controls with the value of an edited JRParameter, if this is 
	 * defined
	 */
	private void initializeFields(){
		if (selectedParam != null){
			expression.setExpression((JRDesignExpression)selectedParam.getExpression());
			paramterName.setText(paramterName.getText());
			int selectionIndex = -1;
			boolean found = false;
			for(String value : paramterName.getItems()){
				selectionIndex++;
				if (value.equals(selectedParam.getName())){
					found = true;
					paramterName.select(selectionIndex);
					break;
				}
			}
			if (!found){
				List<String> newItems = new ArrayList<String>();
				newItems.add(selectedParam.getName());
				newItems.addAll(Arrays.asList(comboInput));
				comboInput = newItems.toArray(new String[newItems.size()]);
				paramterName.setItems(comboInput);
				paramterName.select(0);
			}
		}
	}
	
	@Override
	protected void okPressed() {
		if (paramterName.getSelectionIndex() != -1){
			selectedItem = comboInput[paramterName.getSelectionIndex()];
		}
		selectedExpression = expression.getExpression();
		super.okPressed();
	}

	/**
	 * Return the selected expression
	 * @return an expression
	 */
	public JRDesignExpression getSelectedExpression(){
		return selectedExpression;
	}
	
	/**
	 * Return the selected name
	 * 
	 * @return a parameter name
	 */
	public String getSelectedParamName(){
		return selectedItem;
	}
}
