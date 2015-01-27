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
package com.jaspersoft.studio.editor.xml.xml;

import java.util.ArrayList;
import java.util.List;

public class XMLTree {

	private XMLElement rootElement;
	private List<XMLElement> allElements = new ArrayList<XMLElement>();
	private List<String> allAttributes = new ArrayList<String>();

	public XMLTree() {

		super();
		rootElement = new XMLElement("jasperReport");
		rootElement.addChildAttribute(new XMLAttribute("name"));
		rootElement.addChildAttribute(new XMLAttribute("language"));
		rootElement.addChildAttribute(new XMLAttribute("pageWidth"));
		rootElement.addChildAttribute(new XMLAttribute("pageHeight"));
		
		XMLElement property = newDTDElement("property");
		rootElement.addChildElement(property);
		property.addChildAttribute(new XMLAttribute("name"));
		addAttribute("name");

		property.addChildAttribute(new XMLAttribute("value"));
//		addAttribute("value");

		XMLElement style = newDTDElement("style");
		rootElement.addChildElement(style);
		style.addChildAttribute(new XMLAttribute("name"));
//		addAttribute("name");
		style.addChildAttribute(new XMLAttribute("mode"));
//		addAttribute("mode");
		style.addChildAttribute(new XMLAttribute("forecolor"));
//		addAttribute("forecolor");
		style.addChildAttribute(new XMLAttribute("backcolor"));
//		addAttribute("backcolor");
		style.addChildAttribute(new XMLAttribute("fontName"));
//		addAttribute("fontName");
		style.addChildAttribute(new XMLAttribute("fontSize"));
//		addAttribute("fontSize");
		style.addChildAttribute(new XMLAttribute("isBold"));
//		addAttribute("isBold");
		style.addChildAttribute(new XMLAttribute("isItalic"));
//		addAttribute("isItalic");
		style.addChildAttribute(new XMLAttribute("isUnderline"));
//		addAttribute("isUnderline");
		style.addChildAttribute(new XMLAttribute("isStrikeThrough"));
//		addAttribute("isStrikeThrough");
		style.addChildAttribute(new XMLAttribute("isItalic"));
//		addAttribute("isItalic");
		style.addChildAttribute(new XMLAttribute("pdfFontName"));
//		addAttribute("pdfFontName");
		style.addChildAttribute(new XMLAttribute("rotation"));
//		addAttribute("rotation");
		style.addChildAttribute(new XMLAttribute("hAlign"));
//		addAttribute("hAlign");
		style.addChildAttribute(new XMLAttribute("vAlign"));
//		addAttribute("vAlign");

		XMLElement conditionalStyle = newDTDElement("conditionalStyle");
		style.addChildElement(conditionalStyle);
		XMLElement conditionExpression = newDTDElement("conditionExpression");
		conditionalStyle.addChildElement(conditionExpression);

	}

	private XMLElement newDTDElement(String elementName) {
		XMLElement element = new XMLElement(elementName);
		allElements.add(element);
		return element;
	}

	private void addAttribute(String attributeName) {
		if (!allAttributes.contains(attributeName)) {
			allAttributes.add(attributeName);
		}
	}

	public List<XMLElement> getAllElements() {
		return allElements;
	}

	public List<String> getAllAttributes() {
		return allAttributes;
	}

	public XMLElement getRootElement() {
		return rootElement;
	}

	public void setRootElement(XMLElement rootElement) {
		this.rootElement = rootElement;
	}
}
