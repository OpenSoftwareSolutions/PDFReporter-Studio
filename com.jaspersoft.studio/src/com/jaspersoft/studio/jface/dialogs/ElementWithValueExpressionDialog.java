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

import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 * This generic dialog can be used to create or modify an element that has a name and a {@link JRDesignExpression} expression value
 * associated to it. Most common scenarios are the creation/modification of parameters or properties.
 * <p>
 * The user can customize the UI information of the dialog (title and labels) using the dedicated constructor.
 *  
 * @author mrabbi
 *
 */
public class ElementWithValueExpressionDialog extends FormDialog implements IExpressionContextSetter{
	
	// Dialog UI information
	private String dialogTitle=Messages.ElementWithValueExpressionDialog_Title;
	private String lblNameText=Messages.ElementWithValueExpressionDialog_NameLbl;
	private String lblValueExpressionText=Messages.ElementWithValueExpressionDialog_ValueExprLbl;
	
	// Information
	private String elementName;
	private JRDesignExpression elementValueExpr;
	private ExpressionContext expContext;
	
	// Widgets
	private Text name;
	private WTextExpression valueExpression;

	/**
	 * Create the dialog with all the default generic information.
	 * 
	 * @param parentShell
	 */
	public ElementWithValueExpressionDialog(Shell parentShell) {
		super(parentShell);
	}
	
	/**
	 * Creates the dialog with custom information for UI presentation.
	 * 
	 * @param dialogTitle new title
	 * @param labelName label text for the name field
	 * @param labelValueExpression label text for the value expression field
	 * @param currentName current name value
	 * @param currentExpression current value expression
	 * @param parentShell
	 */
	public ElementWithValueExpressionDialog(
			String dialogTitle, String labelName, String labelValueExpression, 
			String currentName, JRDesignExpression currentExpression, Shell parentShell){
		this(parentShell);
		Assert.isNotNull(dialogTitle,Messages.ElementWithValueExpressionDialog_AssertCustomTitleNotNull);
		Assert.isNotNull(labelName, Messages.ElementWithValueExpressionDialog_AssertCustomNameLblNotNull);
		Assert.isNotNull(labelValueExpression, Messages.ElementWithValueExpressionDialog_AssertCustomValueExprLblNotNull);
		this.dialogTitle=dialogTitle;
		this.lblNameText=labelName;
		this.lblValueExpressionText=labelValueExpression;
		this.elementName=currentName;
		this.elementValueExpr=currentExpression;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(dialogTitle);
	}

	@Override
	protected void createFormContent(IManagedForm mform) {
		Composite container = mform.getForm().getBody();
		container.setBackground(container.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		GridLayout containerLayout = new GridLayout(2, false);
		containerLayout.marginHeight=10;
		containerLayout.verticalSpacing=10;
		container.setLayout(containerLayout);
		
		Label lblName = new Label(container, SWT.NONE);
		lblName.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		lblName.setText(lblNameText);
		
		name = new Text(container,SWT.BORDER);
		GridData gd_name=new GridData(SWT.LEFT, SWT.TOP, false, false);
		gd_name.widthHint=150;
		name.setLayoutData(gd_name);
			
		Label lblValueExpression=new Label(container,SWT.NONE);
		lblValueExpression.setText(lblValueExpressionText);
		lblValueExpression.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		
		valueExpression = new WTextExpression(container, SWT.NONE);		
		GridData gd_valueExpression=new GridData(SWT.FILL, SWT.FILL, true, true);
		gd_valueExpression.widthHint=300;
		valueExpression.setLayoutData(gd_valueExpression);
		
		// Initialize the widget values
		if(elementName!=null){
			name.setText(elementName);
		}
		else{
			name.setText(""); //$NON-NLS-1$
		}
		valueExpression.setExpression(elementValueExpr);
		valueExpression.setExpressionContext(expContext);
	}

	@Override
	protected Point getInitialSize() {
		return super.getInitialSize();
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}
		
	@Override
	protected void okPressed() {
		if(name.getText().isEmpty()){
			MessageDialog.openError(getShell(), Messages.ElementWithValueExpressionDialog_EmptyNameErrTitle, Messages.ElementWithValueExpressionDialog_EmptyNameErrMsg);
		}
		else{
			elementName=name.getText();
			elementValueExpr=valueExpression.getExpression();
			super.okPressed();
		}
	}

	/**
	 * Gets the name of the element with value expression associated.
	 * 	
	 * @return the element name
	 */
	public String getElementName() {
		return elementName;
	}
	
	/**
	 * Gets the value expression modified/created.
	 * 
	 * @return the {@link JRDesignExpression} instance
	 */
	public JRDesignExpression getElementValueExpression(){
		return elementValueExpr;
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
		if(valueExpression!=null){
			valueExpression.setExpressionContext(this.expContext);
		}			
	}

}
