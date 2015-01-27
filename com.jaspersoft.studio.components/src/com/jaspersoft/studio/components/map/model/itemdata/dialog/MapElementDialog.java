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
package com.jaspersoft.studio.components.map.model.itemdata.dialog;

import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.StandardItemProperty;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
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
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.ElementsListWidgetConfiguration;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;

/**
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapElementDialog extends Dialog implements IExpressionContextSetter{

	private Button useExpressionCheckbox;
	private Text propertyValue;
	private WTextExpression propertyValueExpression;
	private ExpressionContext expContext;
	private StandardItemProperty pname;
	private Composite dialogArea;
	private ElementsListWidgetConfiguration wconfig;
	
	public MapElementDialog(Shell parentShell, ItemProperty pname, ElementsListWidgetConfiguration wconfig) {
		super(parentShell);
		this.pname = (StandardItemProperty) pname;
		this.wconfig = wconfig;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		dialogArea = (Composite) super.createDialogArea(parent);
		dialogArea.setLayout(new GridLayout(1, false));
		
		useExpressionCheckbox = new Button(dialogArea, SWT.CHECK);
		useExpressionCheckbox.setText(Messages.MapElementDialog_UseExpression);
		useExpressionCheckbox.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
			
		Label lblPropertyValue=new Label(dialogArea,SWT.NONE);
		lblPropertyValue.setText(Messages.MapElementDialog_PropertyValue);
		lblPropertyValue.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		
		propertyValue = new Text(dialogArea,SWT.BORDER);
		GridData gd_propertyValue = new GridData(SWT.FILL,SWT.FILL,true,false);
		propertyValue.setLayoutData(gd_propertyValue);
		
		propertyValueExpression = new WTextExpression(dialogArea, SWT.NONE);
		GridData gd_propertyValueExpression = new GridData(SWT.FILL,SWT.FILL,true,false);
		gd_propertyValueExpression.heightHint = 50;
		gd_propertyValueExpression.widthHint = 250;
		propertyValueExpression.setLayoutData(gd_propertyValueExpression);
		propertyValueExpression.setExpressionContext(this.expContext);
		
		initWidgets();
		addListeners();
		
		return dialogArea;
	}
	
	private void initWidgets() {
		if(pname.getValue()!=null){
			useExpressionCheckbox.setSelection(false);
			propertyValue.setText(pname.getValue());
			propertyValueExpression.setVisible(false);
			propertyValueExpression.setEnabled(false);
			propertyValueExpression.setExpression(null);
			((GridData)propertyValueExpression.getLayoutData()).exclude=true;
		}
		else{
			useExpressionCheckbox.setSelection(true);
			propertyValueExpression.setExpression((JRDesignExpression)pname.getValueExpression());
			propertyValue.setVisible(false);
			propertyValue.setEnabled(false);
			((GridData)propertyValue.getLayoutData()).exclude=true;
		}
	}
	
	private void addListeners() {
		propertyValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				pname.setValue(propertyValue.getText());
			}
		});
		propertyValueExpression.addModifyListener(new ExpressionModifiedListener() {
			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				pname.setValueExpression(event.modifiedExpression);
			}
		});
		useExpressionCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(useExpressionCheckbox.getSelection()){
					// hide normal textbox
					propertyValue.setText(""); //$NON-NLS-1$
					pname.setValue(null);
					propertyValue.setVisible(false);
					propertyValue.setEnabled(false);
					((GridData)propertyValue.getLayoutData()).exclude=true;
					// and show expression widget
					propertyValueExpression.setVisible(true);
					propertyValueExpression.setEnabled(true);
					((GridData)propertyValueExpression.getLayoutData()).exclude=false;
				}
				else{
					// hide the expression widget
					propertyValueExpression.setVisible(false);
					propertyValueExpression.setEnabled(false);
					propertyValueExpression.setExpression(null);
					((GridData)propertyValueExpression.getLayoutData()).exclude=true;
					// and show the normal textbox
					propertyValue.setText(""); //$NON-NLS-1$
					propertyValue.setVisible(true);
					propertyValue.setEnabled(true);
					((GridData)propertyValue.getLayoutData()).exclude=false;
				}
				dialogArea.layout();
			}
		});	
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(NLS.bind(Messages.MapElementDialog_DialogTitle,wconfig.getElementTxt()));
		UIUtils.resizeAndCenterShell(newShell, 300, 180);
	}
	
	@Override
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}

	public ItemProperty getElementName() {
		return this.pname;
	}
	
	@Override
	protected void okPressed() {
		if(wconfig.isElementNameMandatory()) {
			String value = pname.getValue();
			JRExpression valueExpression = pname.getValueExpression();
			if ((value == null || value.isEmpty())
					&& (valueExpression == null
							|| valueExpression.getText() == null || valueExpression
							.getText().isEmpty())) {
				MessageDialog.openError(UIUtils.getShell(), Messages.MapElementDialog_ErrorDialogTitle, 
						NLS.bind(Messages.MapElementDialog_ErrorDialogMsg,wconfig.getElementTxt()));
				return;
			}
		}
		super.okPressed();
	}
}
