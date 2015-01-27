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
package com.jaspersoft.studio.editor.action.pdf;

import java.util.HashSet;
import java.util.Iterator;

/**
 * Static class used to register a PDF properties, needed to have a common and easy
 * expandable list of properties. Knowing them it is important because when a new property 
 * is set for a model, all other pdf properties must be deleted (a model can have only one 
 * pdf property).
 * @author Orlandin Marco
 *
 */
public class PropertiesList {
	
	/**
	 * Contain the list of registered properties
	 */
	private static HashSet<String> propertyList = new HashSet<String>();
 
 /**
  * Register a new property
  * @param newItem id of the new property
  */
	public static void AddItem(String newItem){
		propertyList.add(newItem);
		
	}
	 
	
	/**
	 * Return an iterator to a property of the list
	 * @return Iterator to a property, use HasNex()t to know it there are
	 * an element to read and Next() to read it
	 */
	public static Iterator<String> GetIterator(){
		return propertyList.iterator();
	}
	
	/**
	 * Size of the list
	 * @return number of registered elements
	 */
	public static int Size(){
		return propertyList.size();
	}
	
	
 
}
