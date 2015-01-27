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
package com.jaspersoft.studio.properties.internal;

import java.util.ArrayList;
import java.util.List;

/**
 * Descriptor for a property associated with a widget, it can simply have a 
 * name and a list of alias, like the description for example
 * 
 * @author Orlandin Marco
 *
 */
public class WidgetDescriptor {
	
	/**
	 * Name of the property
	 */
	private String name;
	
	/**
	 * List of aliases, with this names the property match as its standard name
	 */
	private List<String> aliases;
	
	public WidgetDescriptor(String name){
		this.name = name;
		aliases = new ArrayList<String>();
	}
	
	public String getName(){
		return name;
	}
	
	public void addAlias(String alias){
		aliases.add(alias);
	}
	
	public List<String> getAliases(){
		return aliases;
	}
}
