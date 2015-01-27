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

import org.eclipse.swt.widgets.Menu;

/**
 * Interface used to declare that a class can provide a menu where will be attached and 
 * help listener 
 * 
 * @author Orlandin Marco
 *
 */
public interface IMenuProvider {
	
	/**
	 * Menu where the help listener will be attached
	 * @return a menu
	 */
	public Menu getMenu();
	
}
