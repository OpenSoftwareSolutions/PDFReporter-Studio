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
package com.jaspersoft.studio.preferences.editor.properties;

import java.util.Comparator;

import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;

public class PropertyComparator implements Comparator<PropertySuffix> {

	public int compare(PropertySuffix o1, PropertySuffix o2) {
		String k1 = o1.getKey();
		String k2 = o2.getKey();
		return k1.compareTo(k2);
	}

}
