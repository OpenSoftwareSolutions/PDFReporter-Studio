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
import java.text.ParseException;

import org.eclipse.core.databinding.conversion.Converter;

public class StringToObjectFomatConverter extends Converter {
	private Format formater;

	public StringToObjectFomatConverter(Object toType, Format formater) {
		super(String.class, toType);
		this.formater = formater;
	}

	@Override
	public Object convert(Object fromObject) {
		try {
			return formater.parseObject((String) fromObject);
		} catch (ParseException e) {
			return null;
		}
	}

}
