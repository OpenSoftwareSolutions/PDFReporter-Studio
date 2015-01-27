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
 <xsd:element name="DataSourceName" type="xsd:string" sql:field="DataSourceName" minOccurs="0" />
  <xsd:element name="DataSourceDescription" type="xsd:string" sql:field="DataSourceDescription" minOccurs="0" />
  <xsd:element name="URL" type="xsd:string" sql:field="URL" minOccurs="0" />
  <xsd:element name="DataSourceInfo" type="xsd:string" sql:field="DataSourceInfo" minOccurs="0" />
  <xsd:element name="ProviderName" type="xsd:string" sql:field="ProviderName" minOccurs="0" />
- <xsd:element name="ProviderType" sql:field="ProviderType" minOccurs="0">
- <xsd:complexType>
- <xsd:sequence maxOccurs="unbounded" minOccurs="0">
  <xsd:any processContents="lax" maxOccurs="unbounded" />
  </xsd:sequence>
  </xsd:complexType>
  </xsd:element>
  <xsd:element name="AuthenticationMode" type="xsd:string" sql:field="AuthenticationMode" minOccurs="0" />

 */

/**
 * Class that contains the information of the XMLA datasource
 * 
 * 
 * @author Orlandin Marco
 *
 */
public class DataSourceElement implements DataSourceTreeElement{

	/**
	 * The datasource name
	 */
	private String dataSourceName;
	
	/**
	 * The datasource description
	 */
	private String dataSourceDescription;
	
	/**
	 * The url of the server xmla
	 */
	private String URL;
	
	/**
	 * The provider name
	 */
	private String providerName;
	
	/**
	 * The provider type
	 */
	private String providerType;
	
	/**
	 * The authentication mode
	 */
	private String authenticationMode;
	
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
	 */
	public DataSourceElement(MetadataDiscover svm, SOAPElement rowNode) {
		parent = svm;

		dataSourceName = getRowValue(rowNode, "DataSourceName");
		dataSourceDescription = getRowValue(rowNode, "DataSourceDescription");
		URL = getRowValue(rowNode, "URL");
		providerName = getRowValue(rowNode, "ProviderName");
		providerType = getRowValue(rowNode, "ProviderType");
		authenticationMode = getRowValue(rowNode, "AuthenticationMode");

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
		return parent.getCatalogList(dataSourceName);
	}

	/**
	 * Return the textual representation of the element
	 */
	public String toString() {
		if (dataSourceName == null) {
			return "not initialized";
		} else {
			return "" + dataSourceName;
		}
	}

	/**
	 * Getter for the datasource name
	 * 
	 * @return the datasource name
	 */
	public String getDataSourceName() {
		return dataSourceName;
	}

	/**
	 * Getter for the datasource description
	 * 
	 * @return the datasource description
	 */
	public String getDatasourceDescription() {
		return dataSourceDescription;
	}

	/**
	 * Getter for the server url
	 * 
	 * @return the server url
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * Getter for the provider name
	 * 
	 * @return the provider name
	 */
	public String getProivderName() {
		return providerName;
	}

	/**
	 * Getter for the provider type
	 * 
	 * @return the provider type
	 */
	public String getProviderType() {
		return providerType;
	}

	/**
	 * Getter for the authentication mode
	 * 
	 * @return the authentication mode
	 */
	public String getAuthenticationMode() {
		return authenticationMode;
	}

	/**
	 * Getter for the datasource info, the parent datasource
	 * for the node. Since this is already the datasource it will
	 * be equals to the name of the element
	 * 
	 * @return the datasource name
	 */
	public String getDataSourceInfo() {
		return dataSourceName;
	}
}
