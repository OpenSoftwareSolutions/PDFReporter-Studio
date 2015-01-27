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
package com.jaspersoft.studio.help.ovverriders;

import java.util.Collections;
import java.util.List;

/**
 * This overrider use one or more regular expressions to identify the attributes
 * which link is override. It must match all the regular expressions
 * 
 * @author Orlandin Marco
 *
 */
public class RegularExpressionOverrider implements IHelpOverrider {

	/**
	 * A list of regular expressions
	 */
	protected List<String> regularExpressions;
	
	/**
	 * the substitution
	 */
	protected String substitutionString;
	
	/**
	 * Build an instance of the class
	 * 
	 * @param regEx the regular expression that will be matched with the attribute
	 * @param substitution substitution for the link, the substitution is done by concatenating 
	 * the standard prefix with the substitution String
	 */
	public RegularExpressionOverrider(String regEx, String substitution){
		this(Collections.singletonList(regEx),substitution);
	}
	
	public RegularExpressionOverrider(List<String> regEx, String substitution){
		substitutionString = substitution;
		regularExpressions = regEx;
	}
	
	@Override
	public String getPropertyURL(String propertyName) {
		return PREFIX.concat(substitutionString);
	}

	
	@Override
	public boolean isOverrided(String propertyName) {
		for(String expression : regularExpressions){
			if (!propertyName.matches(expression)) return false;
		}
		return true;
	}
	
}
