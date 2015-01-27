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
package com.jaspersoft.translation.resources;

import java.util.List;

/**
 * A translation resource is something that contribute to translate a plugin. 
 * It can have a path to the resource, a name of the resource and a list of children 
 * 
 * @author Orlandin Marco
 *
 */
public interface ITranslationResource {

	/**
	 * Return the name of the resource
	 * 
	 * @return name of the resource
	 */
	public String getResourceName();
	
	/**
	 * Return a path to the resource if any
	 * 
	 * @return path to the resource
	 */
	public String getResourcePath();
	
	/**
	 * A resource can be also a container of other resources, 
	 * so this method return the children of the resource, if any.
	 * 
	 * @return a not null list of children
	 */
	public List<ITranslationResource> getChildren();
	
	/**
	 * Since getChildren() return always a list we can't know if 
	 * an empty list means a container with no children or simply 
	 * a resource that can not have children, like a file. So this
	 * method is used if a resource is a file or not
	 * 
	 * @return true if the resource represent a file without children, false otherwise
	 */
	public boolean isFile();
	
}
