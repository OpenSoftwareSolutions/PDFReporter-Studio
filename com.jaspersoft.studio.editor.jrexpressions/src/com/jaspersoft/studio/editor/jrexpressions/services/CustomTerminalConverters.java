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
package com.jaspersoft.studio.editor.jrexpressions.services;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.ValueConverterException;
import org.eclipse.xtext.nodemodel.INode;

/**
 * Custom value converter service that allows to handle correctly
 * octet, hex, and long number strings.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CustomTerminalConverters extends DefaultTerminalConverters {
	
	private static final int RADIX_8 = 8;
	private static final int RADIX_16 = 16;

	@Override
	@ValueConverter(rule = "INT")
	public IValueConverter<Integer> INT() {
		return new IValueConverter<Integer>(){

			@Override
			public Integer toValue(String string, INode node)
					throws ValueConverterException {
				if(string.startsWith("0x") || string.startsWith("0X")){
					String stripped = string.substring(2, string.length());
					return Integer.parseInt(stripped,RADIX_16);
				}
				else if(string.startsWith("0")){
					return Integer.parseInt(string,RADIX_8);
				}
				else {
					return Integer.parseInt(string);
				}
			}

			@Override
			public String toString(Integer value)
					throws ValueConverterException {
				return value.toString();
			}
		};
	}
	
	@ValueConverter(rule = "LONG")
	public IValueConverter<Long> LONG() {
		return new IValueConverter<Long>(){

			@Override
			public Long toValue(String string, INode node)
					throws ValueConverterException {
				if(string.startsWith("0x") || string.startsWith("0X")){
					String stripped = string.substring(2, string.length()-1);
					return Long.parseLong(stripped,RADIX_16);
				}
				else if(string.startsWith("0")){
					String stripped = string.substring(0, string.length()-1);
					return Long.parseLong(stripped,RADIX_8);
				}
				else {
					String stripped = string.substring(0, string.length()-1);
					return Long.parseLong(stripped);
				}
			}

			@Override
			public String toString(Long value) throws ValueConverterException {
				return value.toString();
			}
			
		};
	}
	
	
}
