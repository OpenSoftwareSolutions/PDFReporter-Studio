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
package com.jaspersoft.studio.property.descriptor.jrQuery;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.PropertyDescriptor;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.help.IHelp;
import com.jaspersoft.studio.help.IHelpRefBuilder;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPQueryButton;

/**
 * Descriptor for a button to edit a query of a data adapter
 * 
 * @author Orlandin Marco
 * 
 */
public class JRQueryButtonPropertyDescriptor extends PropertyDescriptor implements IPropertyDescriptorWidget, IHelp {

	private NullEnum canBeNull;

	/**
	 * Text on the button
	 */
	private String buttonText;

	public JRQueryButtonPropertyDescriptor(Object id, String displayName, NullEnum canBeNull, String buttonText) {
		super(id, displayName);
		this.canBeNull = canBeNull;
		this.buttonText = buttonText;
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		JRQueryCellEditor editor = new JRQueryCellEditor(parent);
		HelpSystem.bindToHelp(this, editor.getControl());
		return editor;
	}

	@Override
	public ILabelProvider getLabelProvider() {
		if (isLabelProviderSet()) {
			return super.getLabelProvider();
		}
		return new JRQueryLabelProvider(canBeNull);
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		return new SPQueryButton(parent, section, this, buttonText);
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
