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
package com.jaspersoft.studio.property.descriptor.subreport.parameter;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.model.subreport.MSubreport;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPSubreportParametersButton;

public class SubreportPropertiesPropertyDescriptor extends NTextPropertyDescriptor implements IPropertyDescriptorWidget {

	public SubreportPropertiesPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	public CellEditor createPropertyEditor(Composite parent) {
		cellEditor = new SubreportPropertiesCellEditor(parent);
		cellEditor.init(msubreport);
		return cellEditor;
	}

	private MSubreport msubreport;
	private SubreportPropertiesCellEditor cellEditor;

	public void init(MSubreport msubreport) {
		this.msubreport = msubreport;
		if (cellEditor != null)
			cellEditor.init(msubreport);
	}

	@Override
	public ILabelProvider getLabelProvider() {
		if (isLabelProviderSet())
			return super.getLabelProvider();
		return new SubreportPropertiesLabelProvider();
	}

	@Override
	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		return new SPSubreportParametersButton(parent, section, this, getDisplayName());
	}
}
