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

import net.sf.jasperreports.engine.JRExpression;

import org.eclipse.jface.viewers.LabelProvider;

/*
 * @author Chicu Veaceslav
 */
public class JRExpressionLabelProvider extends LabelProvider {

	public JRExpressionLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element != null && element instanceof String)
			return (String) element;
		if (element != null && element instanceof JRExpression) {
			String text = ((JRExpression) element).getText();
			return text;
		}
		return ""; //$NON-NLS-1$
	}

}
