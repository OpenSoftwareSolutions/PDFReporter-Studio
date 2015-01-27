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
package com.jaspersoft.studio.property.combomenu;

/**
 * Item for a combo popup that represent a separator into the menu
 * @author Orlandin Marco
 *
 */
public class ComboItemSeparator extends ComboItem {

	/**
	 * Position of the separator, since the list is ordered this field is necessary
	 * to put the separator in the right position.
	 * @param order
	 */
	public ComboItemSeparator(int order){
		super(null, true, order, null, null);
	}
	
	/**
	 * Return always true because this element represent a separator
	 */
	@Override
	public boolean isSeparator() {
		return true;
	}
}
