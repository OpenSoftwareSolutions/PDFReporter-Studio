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

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.help.IHelp;
import com.jaspersoft.studio.help.IHelpRefBuilder;
import com.jaspersoft.studio.jface.IntegerCellEditorValidator;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPPixel;

/**
 * 
 * @author Orlandin Marco
 * 
 */
public class PixelPropertyDescriptor extends TextPropertyDescriptor implements IPropertyDescriptorWidget, IHelp {

	/**
	 * Instantiates a new integer property descriptor.
	 * 
	 * @param id
	 *          the id
	 * @param displayName
	 *          the display name
	 */
	public PixelPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	private boolean readOnly = false;

	public void setReadOnly(boolean readOnly) {
		this.readOnly = readOnly;
	}

	public boolean isReadOnly() {
		return readOnly;
	}

	/**
	 * The <code>TextPropertyDescriptor</code> implementation of this <code>IPropertyDescriptor</code> method creates and
	 * returns a new <code>TextCellEditor</code>.
	 * <p>
	 * The editor is configured with the current validator if there is one.
	 * </p>
	 * 
	 * @param parent
	 *          the parent
	 * @return the cell editor
	 */
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = new TextCellEditor(parent) {
			@Override
			protected Object doGetValue() {
				String value = (String) super.doGetValue();
				if (value == null || value.equals("")) //$NON-NLS-1$
					return (Integer) null;
				return new Integer(value);
			}

			@Override
			protected void doSetValue(Object value) {
				if (value == null)
					super.doSetValue(""); //$NON-NLS-1$
				else {
					Assert.isTrue(text != null && (value instanceof Integer));
					super.doSetValue(((Integer) value).toString());
				}
			}
		};
		editor.setValidator(IntegerCellEditorValidator.instance());
		setValidator(IntegerCellEditorValidator.instance());

		HelpSystem.bindToHelp(this, editor.getControl());
		return editor;
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		SPPixel spNumber = new SPPixel(parent, section, this);
		return spNumber;
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
