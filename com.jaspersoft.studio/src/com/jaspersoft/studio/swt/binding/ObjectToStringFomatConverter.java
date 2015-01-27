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

import java.text.Format;

import org.eclipse.core.databinding.conversion.Converter;

public class ObjectToStringFomatConverter extends Converter {
	private Format formater;

	public ObjectToStringFomatConverter(Object toType, Format formater) {
		super(toType, String.class);
		this.formater = formater;
	}

	@Override
	public Object convert(Object fromObject) {
		if (fromObject == null)
			return "";
		return formater.format(fromObject);
	}

}
