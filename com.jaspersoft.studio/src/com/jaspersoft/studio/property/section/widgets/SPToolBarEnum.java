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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;

public class SPToolBarEnum extends ASPropertyWidget {
	private ToolItem[] toolItems;
	private ToolBar toolBar;

	public SPToolBarEnum(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, Image[] images,
			boolean showText) {
		super(parent, section, pDescriptor);
		if (images != null)
			for (int i = 0; i < toolItems.length; i++) {
				toolItems[i].setImage(images[i]);
				if (!showText)
					toolItems[i].setText("");
			}
	}

	public SPToolBarEnum(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, Image[] images) {
		super(parent, section, pDescriptor);
		if (images != null)
			for (int i = 0; i < toolItems.length; i++)
				toolItems[i].setImage(images[i]);
	}

	@Override
	public Control getControl() {
		return toolBar;
	}

	protected void createComponent(Composite parent) {
		toolBar = new ToolBar(parent, SWT.FLAT | SWT.WRAP | SWT.RIGHT);

		final JSSEnumPropertyDescriptor pd = (JSSEnumPropertyDescriptor) pDescriptor;
		toolItems = new ToolItem[pd.getJrEnums().length];
		for (int i = 0; i < pd.getJrEnums().length; i++) {
			final int index = i;
			toolItems[i] = new ToolItem(toolBar, SWT.CHECK);
			toolItems[i].setText(pd.getJrEnums()[i].getName());
			toolItems[i].setToolTipText(pd.getJrEnums()[i].getName());
			toolItems[i].addSelectionListener(new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int val = index;
					if (pd.getType() != NullEnum.NOTNULL)
						val++;
					propertyChange(section, pd.getId(), toolItems[index].getSelection() ? val : null);
				}
			});
			// bindToHelp(pd, toolItems[i].getControl());
		}
	}

	public void propertyChange(AbstractSection section, Object property, Integer value) {
		section.changeProperty(property, value);
	}

	public void setData(APropertyNode pnode, Object b) {
		toolBar.setEnabled(pnode.isEditable());
		int index = 0;
		if (b != null)
			index = ((Number) b).intValue();
		final JSSEnumPropertyDescriptor pd = (JSSEnumPropertyDescriptor) pDescriptor;
		if (pd.getType() != NullEnum.NOTNULL)
			index--;
		for (int i = 0; i < toolItems.length; i++)
			toolItems[i].setSelection(i == index);

	}
}
