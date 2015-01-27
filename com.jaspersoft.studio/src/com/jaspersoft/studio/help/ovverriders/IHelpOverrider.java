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

/**
 * Interface defined to provide the method to know if the help reference of an attribute 
 * is override and in this case return a correct reference to the help for that attribute.
 * 
 * @author Orlandin Marco
 *
 */
public interface IHelpOverrider {
	/**
	 * Standard prefix of the help document
	 */
	public static final String PREFIX = "net.sf.jasperreports.doc/docs/config.reference.html?cp=0_2#";
	
	/**
	 * Check if an attribute is override
	 * 
	 * @param propertyName the name of the attribute
	 * @return true if the attribute is override by this overrider, otherwise false
	 */
	public boolean isOverrided(String propertyName);
	
	/**
	 * if isOverrided has returned true then this method can be used to get the correct help reference
	 * 
	 * @param propertyName the name of the attribute
	 * @return an URL on the correct hel reference for the attribute
	 */
	public String getPropertyURL(String propertyName);	
}
