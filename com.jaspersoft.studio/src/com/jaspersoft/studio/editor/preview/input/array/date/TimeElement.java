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

import java.sql.Time;
import java.util.Date;

import org.eclipse.nebula.widgets.cdatetime.CDT;

public class TimeElement extends ADateElement {

	@Override
	public Class<?> getSupportedType() {
		return Time.class;
	}

	@Override
	protected int getStyle() {
		return CDT.TIME_MEDIUM;
	}

	@Override
	protected Date getDate() {
		Date sdate = date.getSelection();
		return sdate != null ? new java.sql.Time(sdate.getTime()) : null;
	}

}
