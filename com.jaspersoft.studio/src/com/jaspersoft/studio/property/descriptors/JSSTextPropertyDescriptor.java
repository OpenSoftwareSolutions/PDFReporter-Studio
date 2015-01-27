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
package com.jaspersoft.studio.property.descriptors;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.help.IHelp;
import com.jaspersoft.studio.help.IHelpRefBuilder;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPText;

public class JSSTextPropertyDescriptor extends TextPropertyDescriptor implements IPropertyDescriptorWidget, IHelp {

	/**
	 * Field to check if the widget should be read only
	 */
	protected boolean readOnly;
	private int style = SWT.NONE;

	public JSSTextPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		readOnly = false;
	}

	public JSSTextPropertyDescriptor(Object id, String displayName, int style) {
		super(id, displayName);
		readOnly = false;
		this.style = style;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = super.createPropertyEditor(parent);
		HelpSystem.bindToHelp(this, editor.getControl());
		return editor;
	}

	public void setReadOnly(boolean value) {
		readOnly = value;
	}

	public int getStyle() {
		return style;
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		ASPropertyWidget textWidget = new SPText(parent, section, this);
		textWidget.setReadOnly(readOnly);
		return textWidget;
	}

	private IHelpRefBuilder refBuilder;

	@Override
	public void setHelpRefBuilder(IHelpRefBuilder refBuilder) {
		this.refBuilder = refBuilder;
	}

	@Override
	public String getHelpReference() {
		if (refBuilder != null)
			return refBuilder.getHelpReference();
		return null;
	}
}
