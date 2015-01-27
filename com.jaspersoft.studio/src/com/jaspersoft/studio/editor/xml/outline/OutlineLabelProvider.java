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
package com.jaspersoft.studio.editor.xml.outline;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.editor.xml.xml.XMLElement;
 
public class OutlineLabelProvider implements ILabelProvider {

	public OutlineLabelProvider() {
		super();
	}

	public Image getImage(Object element) {
		return null;
	}

	public String getText(Object element) {
		if (element instanceof XMLElement) {
			XMLElement dtdElement = (XMLElement) element;
			String textToShow = dtdElement.getName();

			String nameAttribute = dtdElement.getAttributeValue("name");
			if (nameAttribute != null)
				textToShow += " " + nameAttribute;

			return textToShow;
		}
		return null;
	}

	public void addListener(ILabelProviderListener listener) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	public void removeListener(ILabelProviderListener listener) {
	}

}
