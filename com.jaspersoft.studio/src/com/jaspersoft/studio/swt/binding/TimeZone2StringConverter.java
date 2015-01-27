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
package com.jaspersoft.studio.swt.binding;

import java.util.TimeZone;

import org.eclipse.core.databinding.conversion.Converter;

public class TimeZone2StringConverter extends Converter {
	public TimeZone2StringConverter() {
		super(TimeZone.class, String.class);
	}

	public Object convert(Object source) {
		if (source != null)
			return ((TimeZone) source).getDisplayName();
		return ""; //$NON-NLS-1$
	}

}
