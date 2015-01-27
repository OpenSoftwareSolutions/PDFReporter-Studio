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

import net.sf.jasperreports.engine.type.JREnum;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;

import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.help.IHelp;
import com.jaspersoft.studio.help.IHelpRefBuilder;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPReadComboEnum;
import com.jaspersoft.studio.utils.EnumHelper;

public class JSSEnumPropertyDescriptor extends ComboBoxPropertyDescriptor implements IPropertyDescriptorWidget, IHelp {
	private NullEnum type;
	private int start = 0;
	private JREnum[] jrEnums;

	public JSSEnumPropertyDescriptor(Object id, String displayName, Class<? extends JREnum> jrEnum, NullEnum type,
			int skipPositions) {
		super(id, displayName, EnumHelper.getEnumNames(jrEnum.getEnumConstants(), type, skipPositions));
		this.type = type;
		jrEnums = jrEnum.getEnumConstants();
		this.start = jrEnums[0].getValue();
	}

	public JSSEnumPropertyDescriptor(Object id, String displayName, Class<? extends JREnum> jrEnum, NullEnum type) {
		super(id, displayName, EnumHelper.getEnumNames(jrEnum.getEnumConstants(), type));
		this.type = type;
		jrEnums = jrEnum.getEnumConstants();
		this.start = jrEnums[0].getValue();
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
		CellEditor editor = super.createPropertyEditor(parent);
		HelpSystem.bindToHelp(this, editor.getControl());
		return editor;
	}

	public Integer getEnumValue(JREnum jrvalue) {
		return EnumHelper.getValue(jrvalue, start, type != NullEnum.NOTNULL);
	}

	public JREnum getEnumValue(Object value) {
		return EnumHelper.getSetValue(jrEnums, value, start, type != NullEnum.NOTNULL);
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		return new SPReadComboEnum(parent, section, this);
	}

	public NullEnum getType() {
		return type;
	}

	public int getStart() {
		return start;
	}

	public JREnum[] getJrEnums() {
		return jrEnums;
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
