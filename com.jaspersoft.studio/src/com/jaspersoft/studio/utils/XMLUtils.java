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
package com.jaspersoft.studio.utils;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jasperreports.data.xml.XmlDataAdapter;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class XMLUtils {
	public static Document parseNoValidation(InputStream io) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setIgnoringComments(true);
		dbf.setFeature("http://xml.org/sax/features/namespaces", false);
		dbf.setFeature("http://xml.org/sax/features/validation", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
		dbf.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

		DocumentBuilder db = dbf.newDocumentBuilder();

		return db.parse(io);
	}
	
	public static boolean isNamespaceAware(XmlDataAdapter xmlDataAdapter, JasperDesign jdesign) {
		if(xmlDataAdapter!=null){
			return xmlDataAdapter.isNamespaceAware();
		}
		if(jdesign != null) {
			String detectNamespaces = jdesign.getProperty("net.sf.jasperreports.xml.detect.namespaces");
			if(detectNamespaces!=null && "true".equals(detectNamespaces)){
				return true;
			}
			else {
				return false;
			}
		}
		return false;
	}
}
