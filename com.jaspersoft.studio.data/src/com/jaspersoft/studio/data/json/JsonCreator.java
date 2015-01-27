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
package com.jaspersoft.studio.data.json;

import java.util.Locale;
import java.util.TimeZone;

import net.sf.jasperreports.data.json.JsonDataAdapterImpl;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;

/**
 * Creator to build a JSS Json data adapter from the xml definition of an iReport Json 
 * data adapter
 * 
 * @author Orlandin Marco
 */
public class JsonCreator implements IDataAdapterCreator {

	@Override
	public DataAdapterDescriptor buildFromXML(Document docXML) {
		JsonDataAdapterImpl result = new JsonDataAdapterImpl();
		
		NamedNodeMap rootAttributes = docXML.getChildNodes().item(0).getAttributes();
		String connectionName = rootAttributes.getNamedItem("name").getTextContent();
		result.setName(connectionName);
		
		NodeList children = docXML.getChildNodes().item(0).getChildNodes();
		String localeVariant = null;
		String localeLanguage = null;
		String localeCountry = null;
		for(int i=0; i<children.getLength(); i++){
			Node node = children.item(i);
			if (node.getNodeName().equals("connectionParameter")){
		
				String paramName = node.getAttributes().getNamedItem("name").getTextContent();
				
				if (paramName.equals("Locale_country"))localeCountry = node.getTextContent();
				if (paramName.equals("Locale_variant"))localeVariant = node.getTextContent();
				if (paramName.equals("Locale_language"))localeLanguage = node.getTextContent();
				if (paramName.equals("timeZone")) result.setTimeZone(TimeZone.getTimeZone(node.getTextContent())) ;
				if (paramName.equals("NumberPattern")) result.setNumberPattern(node.getTextContent());
				if (paramName.equals("UseConnection")) result.setUseConnection(node.getTextContent().equals("true"));
				if (paramName.equals("Filename")) result.setFileName(node.getTextContent());
				if (paramName.equals("DatePattern")) result.setDatePattern(node.getTextContent());				
				if (paramName.equals("SelectExpression")) result.setSelectExpression(node.getTextContent());
			}
		}

		if (localeCountry != null && localeLanguage != null){
			Locale locale = new Locale(localeLanguage, localeCountry, localeVariant);
			result.setLocale(locale);
		}
		JsonDataAdapterDescriptor desc = new JsonDataAdapterDescriptor();
		desc.setDataAdapter(result);
		return desc;
	}

	@Override
	public String getID() {
		return "com.jaspersoft.jrx.json.JsonDataSourceConnection";
	}


}
