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
package com.jaspersoft.studio.jface.dialogs;

import java.text.MessageFormat;

import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 * This dialog allows the user to create / edit a dataset parameter. 
 * 
 * @author mrabbi
 *
 */
public class DatasetRunPameterDialog extends FormDialog implements IExpressionContextSetter {

	private JRDesignDatasetParameter datasetParameter;
	private JRDesignDataset dataset;
	private Combo comboParameterName;
	private WTextExpression parameterExpression;
	private ExpressionContext expContext;

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	public DatasetRunPameterDialog(JRDesignDatasetParameter datasetParameter, JRDesignDataset dataset, Shell parentShell) {
		super(parentShell);
		this.datasetParameter=datasetParameter;
		this.dataset=dataset;
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		Composite container = mform.getForm().getBody();
		container.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));	
		GridLayout containerLayout = new GridLayout(2, false);
		containerLayout.marginHeight=10;
		containerLayout.verticalSpacing=10;
		container.setLayout(containerLayout);
		
		Label lblParameterName = new Label(container, SWT.NONE);
		lblParameterName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblParameterName.setText(Messages.DatasetRunPameterDialog_ParameterNameLbl);
		
		comboParameterName = new Combo(container, SWT.READ_ONLY);
		comboParameterName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		JRParameter[] dsParameters = (JRParameter[])dataset.getParameters();
		String[] parameterNames=new String[dsParameters.length]; 
		for (int i=0;i<dsParameters.length;i++){
			parameterNames[i]=dsParameters[i].getName();
		}		
		comboParameterName.setItems(parameterNames);
		comboParameterName.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				int selIndex = comboParameterName.getSelectionIndex();
				if (selIndex>=0){
					datasetParameter.setName(comboParameterName.getItem(selIndex));
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});

		Label lblParameterExpression=new Label(container,SWT.NONE);
		lblParameterExpression.setText(Messages.DatasetRunPameterDialog_ValueExprLbl);
		lblParameterExpression.setLayoutData(new GridData(SWT.FILL,SWT.FILL,false,false));
		
		parameterExpression = new WTextExpression(container, SWT.NONE){
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				datasetParameter.setExpression(exp);
			}
		};
		parameterExpression.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		parameterExpression.setExpressionContext(expContext);
		
		// Initialize the widget values
		if (datasetParameter!=null){
			boolean found=false;
			for (int i=0;i<parameterNames.length;i++){
				if (parameterNames[i].equals(datasetParameter.getName())){
					comboParameterName.select(i);
					found=true;
					break;
				}
			}
			if (!found){
				MessageDialog.openWarning(getShell(), Messages.DatasetRunPameterDialog_ParamNotFoundErrTitle, 
						MessageFormat.format(Messages.DatasetRunPameterDialog_ParamNotFoundErrMsg,new Object[]{datasetParameter.getName()}));
			}
			else {
				parameterExpression.setExpression((JRDesignExpression)datasetParameter.getExpression());				
			}
		}
		else {
			// Initialize an empty dataset parameter
			datasetParameter=new JRDesignDatasetParameter();			
		}		
		
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(470, 250);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}
	
	/** 
	 * Gets the modified dataset parameter.
	 * 
	 * @return the edited parameter
	 */
	public JRDesignDatasetParameter getModifiedDatasetParameter(){
		return datasetParameter;
	}

	@Override
	protected void okPressed() {
		if (comboParameterName.getSelectionIndex()==-1){
			MessageDialog.openError(getShell(), Messages.DatasetRunPameterDialog_NoParamSelectedErrTitle, Messages.DatasetRunPameterDialog_NoParamSelectedErrMsg);
			return;
		}
		super.okPressed();
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.DatasetRunPameterDialog_Title);
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jaspersoft.studio.editor.expression.IExpressionContextSetter#setExpressionContext(com.jaspersoft.studio.editor.expression.ExpressionContext)
	 */
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
	}
	
}
