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
package com.jaspersoft.studio.property.descriptor.expression;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.IPropertyDescriptorWidget;
import com.jaspersoft.studio.property.section.widgets.SPExpression;

public class JRExpressionPropertyDescriptor extends NTextPropertyDescriptor implements IPropertyDescriptorWidget,
		IExpressionContextSetter {
	private ExpressionContext expContext;

	private SPExpression expEditor;

	public JRExpressionPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		setLabelProvider(new JRExpressionLabelProvider());
	}

	public CellEditor createPropertyEditor(Composite parent) {
		return new JRExpressionCellEditor(parent, expContext);
	}

	@Override
	public ILabelProvider getLabelProvider() {
		if (isLabelProviderSet()) {
			return super.getLabelProvider();
		}
		return new JRExpressionLabelProvider();
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		expEditor = new SPExpression(parent, section, this);
		expEditor.setExpressionContext(expContext);
		return expEditor;
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
		if (expEditor != null)
			expEditor.setExpressionContext(expContext);
	}
}
