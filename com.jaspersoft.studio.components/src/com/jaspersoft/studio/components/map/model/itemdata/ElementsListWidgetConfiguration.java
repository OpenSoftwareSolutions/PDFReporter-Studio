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
package com.jaspersoft.studio.components.map.model.itemdata;

import net.sf.jasperreports.components.map.ItemData;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.components.map.model.path.SPMapPathsList;
import com.jaspersoft.studio.components.map.model.style.SPMapStylesList;

/**
 * This interface should be used by whose widgets that want to have access to
 * configuration information about specific properties implying the use
 * of {@link ItemData} elements.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 * @see SPMapPathsList
 * @see SPMapStylesList
 *
 */
public interface ElementsListWidgetConfiguration {

	String getElementsTabTitle();
	
	String getElementTxt();
	
	Image getAddNewElementIcon();
	
	String getWidgetPropertyID();
	
	String getElementPropertiesResourceLocation();
	
	boolean isElementNameMandatory();
}
