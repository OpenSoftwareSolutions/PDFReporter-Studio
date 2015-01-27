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

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptors.JSSComboPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;

public class SPReadCombo extends ASPropertyWidget {
	protected Combo combo;

	public SPReadCombo(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	@Override
	public Control getControl() {
		return combo;
	}

	protected void createComponent(Composite parent) {
		final JSSComboPropertyDescriptor pd = (JSSComboPropertyDescriptor) pDescriptor;

		combo = section.getWidgetFactory().createCombo(parent, SWT.READ_ONLY);
		combo.setItems(pd.getLabels());
		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				handlePropertyChange();
			}
		});
		combo.setToolTipText(pDescriptor.getDescription());
	}

	public void setData(APropertyNode pnode, Object b) {
		int index = 0;
		if (b != null)
			index = ((Number) b).intValue();
		combo.select(index);
	}

	protected void handlePropertyChange() {
		int index = combo.getSelectionIndex();
		section.changeProperty(pDescriptor.getId(), index);
	}

}
