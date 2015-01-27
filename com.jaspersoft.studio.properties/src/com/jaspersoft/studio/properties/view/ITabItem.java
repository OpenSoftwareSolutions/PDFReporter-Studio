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
package com.jaspersoft.studio.properties.view;

import org.eclipse.jface.resource.ImageDescriptor;

/**
 * Represents a tab to be displayed in the tab list in the tabbed property sheet
 * page.
 * 
 * @author Anthony Hunter
 */
public interface ITabItem {

	/**
	 * Get the icon image for the tab.
	 * 
	 * @return the icon image for the tab.
	 */
	public ImageDescriptor getImage();

	/**
	 * Get the text label for the tab.
	 * 
	 * @return the text label for the tab.
	 */
	public String getText();

	/**
	 * Determine if this tab is selected.
	 * 
	 * @return <code>true</code> if this tab is selected.
	 */
	public boolean isSelected();

	/**
	 * Determine if this tab is indented.
	 * 
	 * @return <code>true</code> if this tab is indented.
	 */
	public boolean isIndented();
}
