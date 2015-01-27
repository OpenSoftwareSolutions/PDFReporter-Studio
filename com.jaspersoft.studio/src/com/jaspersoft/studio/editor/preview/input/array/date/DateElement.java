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
package com.jaspersoft.studio.editor.preview.input.array.date;

import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;

public class DateElement extends ADateElement {

	@Override
	public Class<?> getSupportedType() {
		return Date.class;
	}

	@Override
	protected int getStyle() {
		return CDT.DATE_SHORT;
	}

	@Override
	protected Date getDate() {
		return date.getSelection();
	}

}
