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
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;

public class SPBooleanToggle extends ASPropertyWidget {
	private ToolItem cmb3Bool;
	
	private Composite parent;

	public SPBooleanToggle(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor, Image image) {
		super(parent, section, pDescriptor);
		if (image != null)
			cmb3Bool.setImage(image);
	}

	@Override
	public Control getControl() {
		return null;
	}
	
	@Override
	public Control getControlToBorder() {
		return parent;
	}
	
	protected PaintListener getPaintControlListener(){
		return DefaultWidgetsHighlighters.getWidgetForType(ToolItem.class);
	}
	

	public void createComponent(Composite parent) {
		this.parent = parent;
		cmb3Bool = new ToolItem((ToolBar) parent, SWT.CHECK);
		cmb3Bool.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				section.changeProperty(pDescriptor.getId(), new Boolean(cmb3Bool.getSelection()));
			}
		});
		cmb3Bool.setToolTipText(pDescriptor.getDescription());
	}

	public void setData(APropertyNode pnode, Object b) {
		cmb3Bool.setEnabled(pnode.isEditable());
		boolean v = false;
		if (b != null)
			v = (Boolean) b;
		cmb3Bool.setSelection(v);
	}
}
