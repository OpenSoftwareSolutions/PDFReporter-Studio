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
package com.jaspersoft.studio.swt.widgets;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.ParameterLabelProvider;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterEditor;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.UIUtil;

/**
 * 
 * Class that define a custom widget for the editing of the properties of an element.
 * It consists in a readonly label with written inside the number of properties for the 
 * element. At the right of this text area there is a button that can be pressed 
 * to open a dialog where the user can create\delete\edit the properties
 * 
 * @author Orlandin Marco
 *
 */
public class WTParametersText  extends Composite{

	/**
	 * Element that contains all the parameters
	 */
	private ParameterDTO parameterDTO;
	
	/**
	 * Button that can be pressed to open the edit\add\remove dialog
	 */
	private Button btn;
	
	/**
	 * Readonly text where is written the number of defined parameters
	 */
	protected Text ftext;
	
	/**
	 * Property that identify the parameter map on the edited node
	 */
	private Object propertyId;
	
	/**
	 * The edited node
	 */
	private APropertyNode pNode;
	
	/**
	 * Selection adapter called when the add\remove\edit dialog is closed using the Ok button.
	 * Can be used to notify update of the parameters map at the outside
	 */
	private SelectionAdapter adapter = null;
	
	/**
	 * @param parent composite where this control will be placed 
	 * @param propertyId Property that identify the parameter map on the edited node
	 * @param pNode The edited node
	 */
	public WTParametersText(Composite parent, Object propertyId, APropertyNode pNode) {
		super(parent, SWT.NONE);
		this.propertyId = propertyId;
		this.pNode = pNode;
		createComponent();
	}
	
	/**
	 * Selection adapter called when the add\remove\edit dialog is closed using the Ok button.
	 * Can be used to notify update of the parameters map at the outside. If defined the method 
	 * widget selected is called when he user operate on the parameters dialog. Only one selection 
	 * adapter can be defined at the same time
	 * 
	 * @param adapter selection adapter to call to notify that that the parameter map could be changed
	 */
	public void setSelectionAdapter(SelectionAdapter adapter){
		this.adapter = adapter;
	}

	private ExpressionContext getExpressionContext(){
		JRDesignElement designEl = null;
		if (pNode.getValue() instanceof JRDesignElement) {
			designEl = (JRDesignElement) pNode.getValue();
		}
		ExpressionContext elementExpressionContext = ModelUtils.getElementExpressionContext(designEl, pNode);
		return elementExpressionContext;
	}
	
	protected void createComponent() {
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		setLayout(layout);
		ftext =  new Text(this, SWT.BORDER);
		ftext.setText("");
		setWidth(this, 15);
		ftext.setEnabled(false);
		ftext.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		btn = new Button(this, SWT.PUSH | SWT.FLAT | Window.getDefaultOrientation());
		btn.setText("...");
		
		btn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ParameterEditor wizard = new ParameterEditor();
				wizard.setValue(parameterDTO);
				wizard.setExpressionContext(getExpressionContext());
				WizardDialog dialog = new WizardDialog(ftext.getShell(), wizard);
				dialog.create();
				if (dialog.open() == Dialog.OK){
					pNode.setPropertyValue(propertyId, wizard.getValue());
					if (adapter != null) adapter.widgetSelected(e);
				}
			}
		});
	}

	/**
	 * Read the value of the parameters from the selected element using 
	 * the property name specified in the constructor. Then update the controls
	 * to show the actual state of the parameters
	 */
	public void updateData() {
		Object b = pNode.getPropertyActualValue(propertyId);
		parameterDTO = (ParameterDTO) b;
		setTextData((new ParameterLabelProvider()).getText(b));
	}
	
	/**
	 * Return the button
	 * 
	 * @return the button used to opend the add\remove\edit dialog
	 */
	public Control getButton(){
		return btn;
	}
	
	/**
	 * Return the textual control
	 * 
	 * @return the control used to show the parameters number
	 */
	public Control getControl() {
		return ftext;
	}

	protected void setWidth(Composite parent, int chars) {
		int w =  UIUtil.getCharWidth(ftext) * chars;
		if (parent.getLayout() instanceof RowLayout) {
			RowData rd = new RowData();
			rd.width = w;
			ftext.setLayoutData(rd);
		} else if (parent.getLayout() instanceof GridLayout) {
			GridData rd = new GridData(GridData.FILL_HORIZONTAL);
			rd.minimumWidth = w;
			rd.widthHint = w;
			ftext.setLayoutData(rd);
		}
	}

	/**
	 * Update the text area with the actual number of defined parameter 
	 * 
	 * @param b a string containing a human readable number of parameters
	 */
	public void setTextData(Object b) {
		if (b != null) {
			int oldpos = ftext.getLocation().x;
			ftext.setText(b.toString());
			if (b.toString().length() >= oldpos)
				ftext.setSelection(oldpos, oldpos);
		} else
			ftext.setText("");
	}


}
