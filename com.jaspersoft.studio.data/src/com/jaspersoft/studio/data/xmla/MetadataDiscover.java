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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.soap.MessageFactory;
import javax.xml.soap.MimeHeaders;
import javax.xml.soap.Name;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;

/**
 * Class used to extract the metadata from an XMLA source
 * 
 * @author Orlandin marco
 *
 */
public class MetadataDiscover {

	public static final String XMLA_URI = "urn:schemas-microsoft-com:xml-analysis";
	public static final String ROW_URI = "urn:schemas-microsoft-com:xml-analysis:rowset";
		
	private static String soapMessageFactoryClass = null;
	private static String soapConnectionFactoryClass = null;
	public static void setAxisSOAPClientConfig()
	{
		try {
			if (soapMessageFactoryClass == null)
			{
				soapMessageFactoryClass = System.getProperty("javax.xml.soap.MessageFactory");
				if (soapMessageFactoryClass == null)
				{
					soapMessageFactoryClass = MessageFactory.newInstance().getClass().getName();
				}
			}
		} catch (SOAPException ex) {
		}

		try {
			if (soapConnectionFactoryClass == null)
			{
				soapConnectionFactoryClass = System.getProperty("javax.xml.soap.SOAPConnectionFactory");
				if (soapConnectionFactoryClass == null)
				{
					soapConnectionFactoryClass = SOAPConnectionFactory.newInstance().getClass().getName();
				}
			}
		} catch (SOAPException ex) {
		}

		System.setProperty("javax.xml.soap.MessageFactory","org.apache.axis.soap.MessageFactoryImpl");
		System.setProperty("javax.xml.soap.SOAPConnectionFactory","org.apache.axis.soap.SOAPConnectionFactoryImpl");
	}

	public static void restoreSOAPClientConfig()
	{
		if (soapMessageFactoryClass != null)
		{
			System.setProperty("javax.xml.soap.MessageFactory",soapMessageFactoryClass);
		}

		if (soapConnectionFactoryClass != null)
		{
			System.setProperty("javax.xml.soap.SOAPConnectionFactory",soapConnectionFactoryClass);
		}
	}

	/**
	 * The type of discover request
	 */
	private String requestType;
	
	/**
	 * The connection to the server
	 */
	private SOAPConnection connection;
	
	/**
	 * The node factory
	 */
	private SOAPFactory sf;
	
	/**
	 * the url of the server
	 */
	private String url;
	
	/**
	 * The username of the server
	 */
	private String username;
	
	/**
	 * The password of the server
	 */
	private String password;
	
	/**
	 * 
	 * @param url the url of the server
	 * @param username the username of the server
	 * @param password the password of the server
	 */
	public MetadataDiscover(String url, String username, String password){
		this.url = url;
		this.username = username;
		this.password = password;
	}
	
	/**
	 * Return the type of discover request 
	 * @return a string representing the discover request
	 */
	private String getRequestType(){
		return requestType == null ? "DISCOVER_DATASOURCES" : requestType;
	}

	/**
	 * Set the type of discover request
	 * @param requestType a string representing the type of discover request
	 */
	private void setRequestType(String requestType){
		this.requestType = requestType;
	}

	/**
	 * Create the query for the discover action
	 * 
	 * @param paraList parameters map where the key is the name of the parameter
	 * and the value is the value of the parameter
	 * @return the query as soap message
	 */
	protected SOAPMessage createQueryMessage(Map<String,String> paraList)
	{
		try
		{ 
			// Force the use of Axis as message factory...
			MessageFactory mf = MessageFactory.newInstance();

			SOAPMessage message = mf.createMessage();
			MimeHeaders mh = message.getMimeHeaders();
			mh.setHeader("SOAPAction", "\"urn:schemas-microsoft-com:xml-analysis:Discover\"");
			mh.setHeader("Content-Type", "text/xml; charset=utf-8");
			mh.setHeader("Accept", "application/soap+xml, application/dime, multipart/related, text/*");

			SOAPPart soapPart = message.getSOAPPart();
			SOAPEnvelope envelope = soapPart.getEnvelope();
			SOAPBody body = envelope.getBody();
			Name nDiscover = envelope.createName("Discover", "", XMLA_URI);

			SOAPElement eDiscover = body.addChildElement(nDiscover);

			// add the parameters

			// COMMAND parameter
			// <Command>
			// <Statement>queryStr</Statement>
			// </Command>
			Name nRequest = envelope.createName("RequestType", "", XMLA_URI);
			SOAPElement eRequest = eDiscover.addChildElement(nRequest);
			eRequest.addTextNode(getRequestType());

			Name nRestiction = envelope.createName("Restrictions", "", XMLA_URI);
			SOAPElement eRestriction = eDiscover.addChildElement(nRestiction);
			
			Name nRestictionList = envelope.createName("RestrictionList", "", XMLA_URI);
			eRestriction.addChildElement(nRestictionList);


			// <Properties>
			// <PropertyList>
			// <DataSourceInfo>dataSource</DataSourceInfo>
			// <Catalog>catalog</Catalog>
			// <Format>Multidimensional</Format>
			// <AxisFormat>TupleFormat</AxisFormat>
			// </PropertyList>
			// </Properties>
			paraList.put("Format", "Tabular");
			paraList.put("Content", "SchemaData");
			addParameterList(envelope, eDiscover, "Properties", "PropertyList", paraList);
			message.saveChanges();

			return message;
		}
		catch (SOAPException e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Add the all the specified parameters to the discover request message
	 *   
	 * @param envelope the envelope of the message 
	 * @param eParent the discover query content
	 * @param typeName the type of the parameter list (essentially can be or Properties or Restrictions)
	 * @param listName the name of the list parameter tag (essentially can be or PropertyList or RestrictionList)
	 * @param params parameters map where the key is the name of the parameter and the value is the value of the parameter
	 * @throws SOAPException
	 */
	protected void addParameterList(SOAPEnvelope envelope, SOAPElement eParent, String typeName, String listName, Map<String, String> params) throws SOAPException
	{
		Name nPara = envelope.createName(typeName, "", XMLA_URI);
		SOAPElement eType = eParent.addChildElement(nPara);
		nPara = envelope.createName(listName, "", XMLA_URI);
		SOAPElement eList = eType.addChildElement(nPara);
		if (params == null)
			return;

		Iterator<String> it = params.keySet().iterator();
		while (it.hasNext())
		{
			String tag = (String) it.next();
			String value = (String) params.get(tag);
			nPara = envelope.createName(tag, "", XMLA_URI);
			SOAPElement eTag = eList.addChildElement(nPara);
			eTag.addTextNode(value);
		}
	}
	
	/**
	 * Create a soap connection
	 * @return a soap connection, or null if something goes wrong
	 */
	protected SOAPConnection createSOAPConnection()
	{
		try
		{
			SOAPConnectionFactory scf = SOAPConnectionFactory.newInstance();
			SOAPConnection soapConnection = scf.createConnection();
			return soapConnection;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Create and execute the discover message
	 * 
	 * @param paraList parameters map where the key is the name of the parameter and the value is the value of the parameter, this 
	 * parameters will be used inside the query
	 * @return the result of the query
	 */
	private SOAPMessage executeDiscover(Map<String,String> paraList){
		try {

			try {
				setAxisSOAPClientConfig();

				sf = SOAPFactory.newInstance();
				connection = createSOAPConnection();
				SOAPMessage queryMessage = createQueryMessage(paraList);

				URL soapURL = new URL(getSoapUrl());
				SOAPMessage resultMessage = connection.call(queryMessage, soapURL);

				return resultMessage;
			}
			catch (MalformedURLException e)
			{
				e.printStackTrace();
			}
			catch (SOAPException e)
			{
				e.printStackTrace();
			}
			finally
			{
				restoreSOAPClientConfig();
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {}
		
		return null;
	}
	
	/**
	 * Return the complete url of the server, with the username and password inside
	 * 
	 * @return an url string
	 * @throws MalformedURLException
	 */
	protected String getSoapUrl() throws MalformedURLException
	{
		String soapUrl;
		if (username == null || username.length() == 0)
		{
			soapUrl = url;
		}
		else
		{
			URL urlObj = new URL(url);
			soapUrl = urlObj.getProtocol() + "://" + username;

			if (password != null && password.length() > 0)
			{
				soapUrl += ":" + password;
			}

			soapUrl += "@" + urlObj.getHost() + ":" + urlObj.getPort() + urlObj.getPath();
		}
		return soapUrl;
	}

	/**
	 * Extract from the replay message the node that contains the row
	 * 
	 * @param reply  the response
	 * @param soapEnvelope the envelope inside the response
	 * @return the node that contains the rows or null if it isn't possible to get it
	 */
	protected SOAPElement getRowsSet(SOAPMessage reply, SOAPEnvelope soapEnvelope) 
	{
		SOAPElement rootElement = null;
		try{
			SOAPBody soapBody = soapEnvelope.getBody();
			SOAPElement eElement = null;


			SOAPFault fault = soapBody.getFault();
			if (fault != null)
			{
				return null;
			}

			Name eName = soapEnvelope.createName("DiscoverResponse", "", XMLA_URI);

			// Get the ExecuteResponse-Node
			Iterator<?> responseElements = soapBody.getChildElements(eName);
			if (responseElements.hasNext())
			{
				Object eObj = responseElements.next();
				if (eObj == null)
				{
					return null;
				}
				eElement = (SOAPElement) eObj;
			}
			else
			{
				return null;
			}

			// Get the return-Node
			Name rName = soapEnvelope.createName("return", "", XMLA_URI);
			Iterator<?> returnElements = eElement.getChildElements(rName);
			SOAPElement returnElement = null;
			if (returnElements.hasNext())
			{
				Object eObj = returnElements.next();
				if (eObj == null)
				{
					return null;
				}
				returnElement = (SOAPElement) eObj;
			}
			else
			{
				// Should be old-Microsoft XMLA-SDK. Try without m-prefix
				Name rName2 = soapEnvelope.createName("return", "", "");
				returnElements = eElement.getChildElements(rName2);
				if (returnElements.hasNext())
				{
					Object eObj = returnElements.next();
					if (eObj == null)
					{
						return null;
					}
					returnElement = (SOAPElement) eObj;
				}
				else
				{
					return null;
				}
			}

			// Get the root-Node
			Name rootName = soapEnvelope.createName("root", "", ROW_URI);
			Iterator<?> rootElements = returnElement.getChildElements(rootName);
			if (rootElements.hasNext())
			{
				Object eObj = rootElements.next();
				if (eObj == null)
				{
					return null;
				}
				rootElement = (SOAPElement) eObj;
			}
			else
			{
				return null;
			}
			
		}catch(SOAPException ex){
			ex.printStackTrace();
		}
		return rootElement;
	}
	
	/**
	 * Return the actual soap elements factory
	 * 
	 * @return the soap factory
	 */
	public SOAPFactory getSoapFactory(){
		return sf;
	}


	/**
	 * Return an array of the datasources in the server, could be null
	 * @return array of datasources
	 */
	public DataSourceTreeElement[] getDatasources() {
		setRequestType("DISCOVER_DATASOURCES");
		SOAPMessage response = executeDiscover(new HashMap<String, String>());
		SOAPPart soapPart = response.getSOAPPart();
		SOAPEnvelope soapEnvelope;
		try {			 
			soapEnvelope = soapPart.getEnvelope();
			SOAPElement rowSet = getRowsSet(response, soapEnvelope);
			List<DataSourceTreeElement> result = new ArrayList<DataSourceTreeElement>();
			Name rowElement = soapEnvelope.createName("row", "", ROW_URI);
			Iterator<?> rowValuesElement = rowSet.getChildElements(rowElement);
			while (rowValuesElement.hasNext())
			{
				SOAPElement cellElement = (SOAPElement)rowValuesElement.next();
				result.add(new DataSourceElement(this, cellElement));
			}
			return result.toArray(new DataSourceTreeElement[result.size()]);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return an array of the catalogs in the server for a specific datasource, could be null
	 * @param datasourceName the name of the datasource
	 * @return array of catalogs
	 */
	public DataSourceTreeElement[] getCatalogList(String datasourceName) {
	    setRequestType("DBSCHEMA_CATALOGS");
	    HashMap<String, String> param = new HashMap<String, String>();
	    param.put("DataSourceInfo", datasourceName);
		SOAPMessage response = executeDiscover(param);
		SOAPPart soapPart = response.getSOAPPart();
		SOAPEnvelope soapEnvelope;
		try {
			soapEnvelope = soapPart.getEnvelope();
			SOAPElement rowSet = getRowsSet(response, soapEnvelope);
			List<DataSourceTreeElement> result = new ArrayList<DataSourceTreeElement>();
			Name rowElement = soapEnvelope.createName("row", "", ROW_URI);
			Iterator<?> rowValuesElement = rowSet.getChildElements(rowElement);
			while (rowValuesElement.hasNext())
			{
				SOAPElement cellElement = (SOAPElement)rowValuesElement.next();
				result.add(new CatalogElement(this, cellElement, datasourceName));
			}
			return result.toArray(new DataSourceTreeElement[result.size()]);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return an array of the cubes in the server for a specific datasource and catalog, could be null
	 * @param datasourceName the name of the datasource
	 * @param catalogName the name of the catalog
	 * @return array of cubes
	 */
	public DataSourceTreeElement[] getCubeList(String datasourceName, String catalogName) {
	    setRequestType("MDSCHEMA_CUBES");
	    HashMap<String, String> param = new HashMap<String, String>();
	    param.put("DataSourceInfo", datasourceName);
	    param.put("Catalog", catalogName);
		SOAPMessage response = executeDiscover(param);
		SOAPPart soapPart = response.getSOAPPart();
		SOAPEnvelope soapEnvelope;
		try {
			soapEnvelope = soapPart.getEnvelope();
			SOAPElement rowSet = getRowsSet(response, soapEnvelope);
			List<DataSourceTreeElement> result = new ArrayList<DataSourceTreeElement>();
			Name rowElement = soapEnvelope.createName("row", "", ROW_URI);
			Iterator<?> rowValuesElement = rowSet.getChildElements(rowElement);
			while (rowValuesElement.hasNext())
			{
				SOAPElement cellElement = (SOAPElement)rowValuesElement.next();
				result.add(new CubeElement(this, cellElement, datasourceName));
			}
			return result.toArray(new DataSourceTreeElement[result.size()]);
		} catch (SOAPException e) {
			e.printStackTrace();
		}
		return null;
	}
}
