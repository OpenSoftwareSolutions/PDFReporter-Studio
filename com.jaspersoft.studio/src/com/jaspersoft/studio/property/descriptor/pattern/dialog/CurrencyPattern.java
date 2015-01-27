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
package com.jaspersoft.studio.property.descriptor.pattern.dialog;

import java.text.NumberFormat;
import java.util.ArrayList;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;

public class CurrencyPattern extends NumericPattern {

	private static String[] patterns = {
		"\u00A4#,##0.##;\u00A4-##0.##",
		"#,##0.##\u00A4;#,##0.##- \u00A4",
		"#,##0.##\u00A4;(#,##0.##) \u00A4",
		"\u00A4#,##0.###;\u00A4(-#,##0.###)",
		"\u00A4#,##0.###;\u00A4(#,##0.###-)"
	};
	
	public CurrencyPattern(Composite parent, String value) {
		super(parent, NumberFormat.getCurrencyInstance(), value);
		setDescription(Messages.CurrencyPattern_description);
	}
	
	@Override
	protected java.util.List<String> getDefaults() {
		if (dList == null) {
			dList = new ArrayList<String>();
			
			for(String pattern : patterns){
				dList.add(pattern);
				//pattern.replace("\u00A4", currencySymbol);
			}
			
			setPattern(dList.get(0));
		}
		return dList;
	}
}
