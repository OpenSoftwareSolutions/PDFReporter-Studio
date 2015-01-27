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
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;

public class SP3Boolean extends ASPropertyWidget {
	private Combo cmb3Bool;

	public SP3Boolean(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return cmb3Bool;
	}

	public void createComponent(Composite parent) {
		cmb3Bool = section.getWidgetFactory().createCombo(parent, SWT.READ_ONLY);
		cmb3Bool.setItems(new String[] { Messages.SP3Boolean_Undefined_Value, Messages.SP3Boolean_True_Value, Messages.SP3Boolean_False_Value });
		cmb3Bool.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Boolean bval = null;
				switch (cmb3Bool.getSelectionIndex()) {
				case 1:
					bval = Boolean.TRUE;
					break;
				case 2:
					bval = Boolean.FALSE;
					break;
				}

				section.changeProperty(pDescriptor.getId(), bval);
			}
		});
		cmb3Bool.setToolTipText(pDescriptor.getDescription());
	}

	public void setData(APropertyNode pnode, Object b) {
		cmb3Bool.setEnabled(pnode.isEditable());
		if (b == null)
			cmb3Bool.select(0);
		else if ((Boolean) b)
			cmb3Bool.select(1);
		else
			cmb3Bool.select(2);
	}
}
