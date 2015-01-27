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
package com.jaspersoft.studio.editor.xml.scanners;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.wb.swt.ColorManager;

import com.jaspersoft.studio.editor.xml.IXMLColorConstants;
import com.jaspersoft.studio.editor.xml.rules.CDataRule;


public class CDataScanner extends RuleBasedScanner
{

	public IToken ESCAPED_CHAR;
	public IToken CDATA;
	
	public CDataScanner(ColorManager colorManager)
	{
		
		CDATA = new Token(new TextAttribute(colorManager.getColor(IXMLColorConstants.CDATA)));

		IRule[] rules = new IRule[2];

		// Add rule to pick up start of c section
		rules[0] = new CDataRule(CDATA, true);
		// Add a rule to pick up end of CDATA sections
		rules[1] = new CDataRule(CDATA, false);

		setRules(rules);
	}
	
	
	
	public IToken nextToken()
	{
		return super.nextToken();
	}
}
