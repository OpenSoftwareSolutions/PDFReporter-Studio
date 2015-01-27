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
package com.jaspersoft.studio.preferences;

/**
 * Implementors will add support for a tooltip text to show.
 * 
 * @author Massimo Rabbi
 *
 */
public interface ITooltipSupport {

	/**
	 * Sets the tooltip text to show.
	 * 
	 * @param text the tooltip text
	 */
	void setTooltipText(String text);
	
}
