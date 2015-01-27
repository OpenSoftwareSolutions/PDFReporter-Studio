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

/**
 * Class that contains the information of the XMLA Cube
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class CubeElement  implements DataSourceTreeElement{
	
	/**
	 * The datasource ancestor of this node
	 */
	private String dataSourceInfo;
	
	/**
	 * The name of the catalog parent of this node
	 */
	private String catalogName;
	
	/**
	 * The name of the cube
	 */
	private String cubeName;
	
	/**
	 * The type of the cube
	 */
	private String cubeType;
	
	/**
	 * The last schema update date
	 */
	private String lastSchemaUpdate;
	
	/**
	 * The last data update date
	 */
	private String lastDataUpdate;
	
	/**
	 * textual representation of a boolean to know 
	 * if the drill through is enabled
	 */
	private String isDrillthroughEnabled;
	
	/**
	 * textual representation of a boolean to know 
	 * if the cube is linkable
	 */
	private String isLinkable;
	
	/**
	 * textual representation of a boolean to know 
	 * if the write is enabled
	 */
	private String isWriteEnabled;
	
	/**
	 * textual representation of a boolean to know 
	 * if the SQL is enabled
	 */
	private String isSQLEnabled;
	
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
	public CubeElement(MetadataDiscover svm, SOAPElement rowNode, String myDataSourceInfo) {
		parent = svm;
		dataSourceInfo = myDataSourceInfo;

		catalogName = getRowValue(rowNode, "CATALOG_NAME");
		cubeName = getRowValue(rowNode, "CUBE_NAME");
		cubeType = getRowValue(rowNode, "CUBE_TYPE");
		lastSchemaUpdate = getRowValue(rowNode, "LAST_SCHEMA_UPDATE");
		lastDataUpdate = getRowValue(rowNode, "LAST_DATA_UPDATE");
		isDrillthroughEnabled = getRowValue(rowNode, "IS_DRILLTHROUGH_ENABLED");
		isLinkable = getRowValue(rowNode, "IS_LINKABLE");
		isWriteEnabled = getRowValue(rowNode, "IS_WRITE_ENABLED");
		isSQLEnabled = getRowValue(rowNode, "IS_SQL_ENABLED");
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
	 * Calculate the children of the element, since the cube element can't 
	 * have children it return null
	 */
	public DataSourceTreeElement[] getChildren() {
		return null;
	}

	/**
	 * Return the textual representation of the element
	 */
	public String toString() {
		if (cubeName == null) {
			return "not initialized";
		} else {
			return "" + cubeName;
		}
	}

	/**
	 * Getter for the cube type
	 * @return the type of the cube
	 */
	public String getCubeType() {
		return cubeType;
	}

	/**
	 * Getter for the cube last schema update date
	 * @return the last schema update date
	 */
	public String getLastSchemaUpdate() {
		return lastSchemaUpdate;
	}

	/**
	 * Getter for the cube last data update date
	 * @return the last data update date
	 */
	public String getLastDataUpdate() {
		return lastDataUpdate;
	}

	/**
	 * Getter for the drill through property of the cube
	 * @return boolean representation of the flag, can be null
	 */
	public String isDrillThroughEnable() {
		return isDrillthroughEnabled;
	}

	/**
	 * Getter for the linkable property of the cube
	 * @return boolean representation of the flag, can be null
	 */
	public String isLinkable() {
		return isLinkable;
	}

	/**
	 * Getter for the write enabled property of the cube
	 * @return boolean representation of the flag, can be null
	 */
	public String isWriteEnabled() {
		return isWriteEnabled;
	}

	/**
	 * Getter for the sql enabled property of the cube
	 * @return boolean representation of the flag, can be null
	 */
	public String isSqlEnabled() {
		return isSQLEnabled;
	}

	/**
	 * Getter for the cube ancestor datasource
	 * @return the name of the datasource ancestor of the cube
	 */
	public String getDataSourceInfo() {
		return dataSourceInfo;
	}

	/**
	 * Getter for the cube name
	 * @return the name of the cube
	 */
	public String getCubeName() {
		return cubeName;
	}

	/**
	 * Getter for the parent catalog name
	 * @return the parent catalog name
	 */
	public String getCatalogName() {
		return catalogName;
	}
}
