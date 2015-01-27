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


import java.util.Iterator;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;


/*ROW NODE STRUCTURE:
 <row>
  <CATALOG_NAME>FoodMart 2000</CATALOG_NAME>
  <DESCRIPTION />
  <ROLES />
  <DATE_MODIFIED>2003-04-09T00:01:10</DATE_MODIFIED>
  </row>
*/

/**
 * Class that contains the information of the XMLA Catalog
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class CatalogElement implements DataSourceTreeElement{
	
	/**
	 * The datasource parent of this node
	 */
	private String dataSourceInfo;
	
	/**
	 * The name of the catalog
	 */
	private String catalogName;
	
	/**
	 * The catalog description
	 */
	private String description;
	
	/**
	 * The roles
	 */
	private String roles;
	
	/**
	 * The catalog modified date
	 */
	private String dateModified;
	
	/**
	 * A metadata discover, used to execute the query to get the children
	 */
	private MetadataDiscover parent;

	/**
	 * 
	 * @param svm  A metadata discover, used to execute the query to get the children. Typically the 
	 * metadata discover is where this element is build, so in a sort of way it is the parent
	 * @param rowNode SOAPElement with the information used to build one of this node, must have
	 * the structure written above
	 * @param myDataSourceInfo the name of the parent datasource
	 */
	public CatalogElement(MetadataDiscover svm, SOAPElement rowNode, String myDataSourceInfo) {
		parent = svm;
		dataSourceInfo = myDataSourceInfo;

		catalogName = getRowValue(rowNode, "CATALOG_NAME");
		description = getRowValue(rowNode, "DESCRIPTION");
		roles = getRowValue(rowNode, "ROLES");
		dateModified = getRowValue(rowNode, "DATE_MODIFIED");

	}

	/**
	 * Return a vale of a node that is the children of the cell element
	 * 
	 * @param cellElement parent node
	 * @param tagName name of the children node
	 * @return value of the children node
	 */
	private String getRowValue(SOAPElement cellElement, String tagName) {
		Iterator<?> dimensionNameNode;
		try {
			dimensionNameNode = cellElement.getChildElements(parent.getSoapFactory().createName(tagName, "", MetadataDiscover.ROW_URI));
			String value = null;
			if (dimensionNameNode.hasNext()) {
				SOAPElement valueElement = (SOAPElement) dimensionNameNode.next();
				value = valueElement.getValue();
				return value;
			}
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Calculate the children of the element
	 */
	public DataSourceTreeElement[] getChildren() {
		return parent.getCubeList(dataSourceInfo, catalogName);
	}

	/**
	 * Return the textual representation of the element
	 */
	public String toString() {
		if (catalogName == null) {
			return "not initialized";
		} else {
			return "" + catalogName;
		}
	}

	/**
	 * Getter for the description
	 * @return the description of the catalog
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Getter for the roles
	 * @return the roles of the catalog
	 */
	public String getRoles() {
		return roles;
	}

	/**
	 * Getter for the data modified
	 * @return the data modified of the catalog
	 */
	public String getDateModified() {
		return dateModified;
	}

	/**
	 * Getter for the datasource name
	 * @return the name of the parent datasource
	 */
	public String getDataSourceInfo() {
		return dataSourceInfo;
	}
}
