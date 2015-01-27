/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.properties;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.Dialog;
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

import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;
import com.jaspersoft.jasperreports.customvisualization.design.CVDesignItemProperty;
import com.jaspersoft.studio.components.customvisualization.CustomVisualizationActivator;
import com.jaspersoft.studio.components.customvisualization.messages.Messages;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.property.infoList.ElementDescription;
import com.jaspersoft.studio.property.infoList.SelectableComposite;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.Misc;

/**
 * Dialog that allows editing the information associated to a {@link CVItemProperty} element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CVItemPropertyDialog extends Dialog implements IExpressionContextSetter{

	private Composite dialogArea;
	private Text propertyName;
	private Button useExpressionCheckbox;
	private Text propertyValue;
	private WTextExpression propertyValueExpression;
	private ExpressionContext expContext;
	private CVDesignItemProperty itemProperty;
	private SelectableComposite infoPanel;
	private String propertiesFileLocation;

	public CVItemPropertyDialog(Shell parentShell, CVItemProperty itemProperty, String propertiesFileLocation) {
		super(parentShell);
		this.itemProperty = (CVDesignItemProperty) itemProperty;
		this.propertiesFileLocation = propertiesFileLocation;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		dialogArea = (Composite) super.createDialogArea(parent);
		GridLayout layout = new GridLayout(1,false);
		layout.marginWidth=10;
		layout.marginHeight=10;
		dialogArea.setLayout(layout);
		
		Label lblPropertyName = new Label(dialogArea, SWT.NONE);
		lblPropertyName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblPropertyName.setText(Messages.CVItemPropertyDialog_PropertyName);
		propertyName = new Text(dialogArea, SWT.BORDER);
		propertyName.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		useExpressionCheckbox = new Button(dialogArea, SWT.CHECK);
		useExpressionCheckbox.setText(Messages.CVItemPropertyDialog_UseExpr);
		useExpressionCheckbox.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
			
		Label lblPropertyValue=new Label(dialogArea,SWT.NONE);
		lblPropertyValue.setText(Messages.CVItemPropertyDialog_PropertyValue);
		lblPropertyValue.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,false));
		
		propertyValue = new Text(dialogArea,SWT.BORDER);
		GridData gd_propertyValue = new GridData(SWT.FILL,SWT.FILL,true,false);
		propertyValue.setLayoutData(gd_propertyValue);
		
		propertyValueExpression = new WTextExpression(dialogArea, SWT.NONE);
		GridData gd_propertyValueExpression = new GridData(SWT.FILL,SWT.FILL,true,false);
		gd_propertyValueExpression.heightHint = 50;
		propertyValueExpression.setLayoutData(gd_propertyValueExpression);
		propertyValueExpression.setExpressionContext(this.expContext);
		
		if(propertiesFileLocation!=null) {
			infoPanel = new SelectableComposite(dialogArea);
			infoPanel.setItems(getPropertiesInformation());
			GridData infoGD = new GridData(SWT.FILL,SWT.FILL,true,true);
			infoGD.heightHint = 200;
			infoGD.verticalIndent=5;
			infoPanel.setLayoutData(infoGD);
			infoPanel.SetDoubleClickListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					propertyName.setText(infoPanel.getSelectedElement().getName());
				}
			});
		}
		
		initWidgets();
		addListeners();
		
		return dialogArea;
	}
	
	private List<ElementDescription> getPropertiesInformation() {
		List<ElementDescription> descriptions = new ArrayList<ElementDescription>();
		try {
			descriptions.addAll(
					ElementDescription.getPropertiesInformation(
							CustomVisualizationActivator.getDefault().getFileLocation(propertiesFileLocation)));
		} catch (Exception e) {
			UIUtils.showError(e);
		}  
		return descriptions;
	}

	private void initWidgets() {
		if(this.itemProperty==null){
			this.itemProperty = new CVDesignItemProperty("","",null); //$NON-NLS-1$ //$NON-NLS-2$
		}
		if(this.itemProperty.getValue()!=null){
			useExpressionCheckbox.setSelection(false);
			propertyName.setText(Misc.nvl(itemProperty.getName()));
			propertyValue.setText(itemProperty.getValue());
			propertyValueExpression.setVisible(false);
			propertyValueExpression.setEnabled(false);
			propertyValueExpression.setExpression(null);
			((GridData)propertyValueExpression.getLayoutData()).exclude=true;
		}
		else{
			useExpressionCheckbox.setSelection(true);
			propertyName.setText(Misc.nvl(itemProperty.getName()));
			propertyValueExpression.setExpression((JRDesignExpression)itemProperty.getValueExpression());
			propertyValue.setVisible(false);
			propertyValue.setEnabled(false);
			((GridData)propertyValue.getLayoutData()).exclude=true;
		}
	}

	private void addListeners() {
		propertyName.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				itemProperty.setName(propertyName.getText());
			}
		});
		propertyValue.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				itemProperty.setValue(propertyValue.getText());
			}
		});
		propertyValueExpression.addModifyListener(new ExpressionModifiedListener() {
			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				itemProperty.setValueExpression(event.modifiedExpression);
			}
		});
		useExpressionCheckbox.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if(useExpressionCheckbox.getSelection()){
					// hide normal textbox
					propertyValue.setText(""); //$NON-NLS-1$
					itemProperty.setValue(null);
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
		newShell.setText(Messages.CVItemPropertyDialog_Title);
		UIUtils.resizeAndCenterShell(newShell, 450, 250);
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}

	@Override
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}

	public CVItemProperty getItemProperty() {
		return this.itemProperty;
	}

}
