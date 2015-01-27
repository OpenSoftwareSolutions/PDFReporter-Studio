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
package com.jaspersoft.studio.property.descriptor.propexpr;

import org.eclipse.jface.viewers.LabelProvider;

import com.jaspersoft.studio.messages.Messages;

/*
 * @author Chicu Veaceslav
 */
public class JPropertyExpressionsLabelProvider extends LabelProvider {

	public JPropertyExpressionsLabelProvider() {
		super();
	}

	@Override
	public String getText(Object element) {
		if (element == null)
			return ""; //$NON-NLS-1$
		if (element instanceof PropertyExpressionsDTO) {
			PropertyExpressionsDTO dto = (PropertyExpressionsDTO) element;
			int expsize = 0;
			if (dto.getPropExpressions() != null)
				expsize += dto.getPropExpressions().length;
			int pmapsize = 0;
			if (dto.getPropMap() != null && dto.getPropMap().getPropertyNames() != null)
				pmapsize += dto.getPropMap().getPropertyNames().length;
			return "[" + Messages.common_properties + ": " + (expsize + pmapsize) + "]";
		} //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return element.toString();
	}

}
