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
package com.jaspersoft.studio.data.xmla;


/**
 * Interface for a node of the xmla metadata
 * 
 * @author Orlandin Marco
 *
 */
public interface DataSourceTreeElement {
	/**
	 * Return the children of the node
	 * 
	 * @return an array of children, can be null if there 
	 * aren't children
	 */
	public DataSourceTreeElement[] getChildren();

	/**
	 * Return the textual representation of the node
	 * 
	 * @return a not null string
	 */
	public String toString();

	/**
	 * Return the datasource name from where the node is (also indirectly, in 
	 * case of a children) was generated 
	 * 
	 * @return a not null datasource name
	 */
	public String getDataSourceInfo();
}
