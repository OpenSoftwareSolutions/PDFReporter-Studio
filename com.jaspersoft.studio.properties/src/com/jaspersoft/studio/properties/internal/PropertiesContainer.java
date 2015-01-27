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
package com.jaspersoft.studio.properties.internal;

/**
 * 
 * Class that contains all the properties selectable 
 * for a specific type of item
 * 
 * @author Orlandin Marco
 *
 */
public class PropertiesContainer{
	
	private PropertyContainer[] properties;
	
	/**
	 * Create an instance of the class. The length of the two array must be the same
	 * since every properties name must have its id on the same position in of the 
	 * second array
	 * 
	 * @param labels list of the properties name available
	 * @param ids list of ids associated with the properties name
	 */
	public PropertiesContainer(PropertyContainer[] properties){
		this.properties = properties;
	}
	
	/**
	 * Build an empty container with no properties inside
	 */
	public PropertiesContainer(){
		this.properties = new PropertyContainer[0];
	}
	
	/**
	 * Return the number of properties
	 * 
	 * @return number of properties
	 */
	public int getSize(){
		//Its assumed that ids and labels have the same size
		return properties.length;
	}
	
	/**
	 * Return the array of stored labels
	 * 
	 * @return an array of string
	 */
	public PropertyContainer[] getPrperties(){
		return properties;
	}
}
