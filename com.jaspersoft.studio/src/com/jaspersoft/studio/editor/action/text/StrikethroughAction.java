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
package com.jaspersoft.studio.editor.action.text;

import net.sf.jasperreports.engine.design.JRDesignStyle;

import org.eclipse.ui.IWorkbenchPart;

public class StrikethroughAction extends ABooleanPropertyAction {
	public static String ID = "com.jaspersoft.studio.editor.action.text.strike";

	public StrikethroughAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
	}

	@Override
	protected Object getPropertyName() {
		return JRDesignStyle.PROPERTY_STRIKE_THROUGH;
	}
}
