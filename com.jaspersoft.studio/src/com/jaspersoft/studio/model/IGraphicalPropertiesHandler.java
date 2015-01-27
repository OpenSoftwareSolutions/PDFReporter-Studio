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
package com.jaspersoft.studio.model;

import java.util.HashSet;
import java.util.List;

/**
 * Class used to support the refresh system of the elements only when 
 * one of its graphical property is changed. The hasChangedProperty method is used 
 * to know when an element must be refreshed and it can be set. It provide the method
 * to ask the element which are its graphical properties or the list of styles it uses.
 * This last one is to easily set an element for the update when a style used by it is changed  
 * 
 * @author Orlandin Marco
 *
 */
public interface IGraphicalPropertiesHandler {

	/**
	 * Set the actual state of the property change flag
	 */
	public void setChangedProperty(boolean value);
	
	/**
	 * True if some graphical property is changed for the element, false otherwise
	 */
	public boolean hasChangedProperty();
	
	/**
	 * Return the graphical property for the actual type of element. If the are stored 
	 * inside the cache then the cached version is returned. Otherwise they are calculated,
	 * cached an returned 
	 * 
	 * @return an hashset of string that contains the graphical properties of the actual type of element. 
	 * The graphical properties of an element are those properties that affect the appearance of an element
	 * when changed
	 */
	public HashSet<String> getGraphicalProperties();

	/**
	 * Return a list of used styles by the element. This is very useful in case of 
	 * and element like table or crosstab that can use many styles
	 * 
	 * @return a not null hashset of the names of all the styles used by this element
	 */
	public HashSet<String> getUsedStyles();
	
	/**
	 * Some element could have a partial or an empty model, to speed up the performances (crosstab and table e.g.). This in many cases works
	 * but sometime it can give issue since for example you need to inspect the content of the element to set listeners or check the children. 
	 * This method is used to ask explicitly to an element to create the complete model if it hasen't one, and then return the list of children.
	 * 
	 * @return the list of the real children of the element initialized
	 */
	public List<INode> initModel();
}
