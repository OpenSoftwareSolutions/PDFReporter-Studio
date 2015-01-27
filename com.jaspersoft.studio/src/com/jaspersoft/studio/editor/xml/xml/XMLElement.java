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
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.Position;

public class XMLElement {

	private List<XMLElement> elementChildren = new ArrayList<XMLElement>();
	private List<XMLAttribute> attributeChildren = new ArrayList<XMLAttribute>();

	private String name;
	private XMLElement parent;
	private Position position;

	public XMLElement(String name) {
		super();
		this.name = name;
	}

	public List<XMLElement> getChildrenDTDElements() {
		return elementChildren;
	}

	public XMLElement addChildElement(XMLElement element) {
		elementChildren.add(element);
		element.setParent(this);
		return this;
	}

	public void setParent(XMLElement element) {
		this.parent = element;
	}

	public XMLElement getParent() {
		return parent;
	}

	public XMLElement addChildAttribute(XMLAttribute attribute) {
		attributeChildren.add(attribute);
		return this;
	}

	public String getName() {
		return name;
	}

	public String getAttributeValue(String localName) {
		for (Iterator<XMLAttribute> iter = attributeChildren.iterator(); iter.hasNext();) {
			XMLAttribute attribute = (XMLAttribute) iter.next();
			if (attribute.getName().equals(localName))
				return attribute.getValue();
		}
		return null;
	}

	public void clear() {
		elementChildren.clear();
		attributeChildren.clear();
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public Position getPosition() {
		return position;
	}
}
