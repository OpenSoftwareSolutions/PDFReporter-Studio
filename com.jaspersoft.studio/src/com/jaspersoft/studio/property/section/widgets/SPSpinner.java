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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * 
 * An ASProperty widget that provide a spinner control
 * 
 * @author Orlandin Marco
 *
 */
public class SPSpinner extends ASPropertyWidget {

	/**
	 * The spinner control
	 */
	private Spinner controlSpinner;
	
	public SPSpinner(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}
	
	@Override
	protected void createComponent(Composite parent) {
		controlSpinner = new Spinner(parent, SWT.BORDER);
		controlSpinner.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				section.changeProperty(pDescriptor.getId(), controlSpinner.getSelection());
			}
		});
	}

	/**
	 * Set the data inside the spinner control, the value must be an integer 
	 * or a string that can be converted to integer
	 */
	@Override
	public void setData(APropertyNode pnode, Object value) {
		if (value != null){
			int intValue = Integer.parseInt(value.toString());
			controlSpinner.setSelection(intValue);
		} 

	}

	/**
	 * Return the spinner control
	 */
	@Override
	public Control getControl() {
		return controlSpinner;
	}

}
