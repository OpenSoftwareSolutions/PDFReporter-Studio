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

public class PropertyContainer implements Comparable<PropertyContainer>{
	
	private String name;
	
	private Object id;
	
	private Class<?> sectionType;
	
	public PropertyContainer(String name, Object id, Class<?> sectionType){
		this.name = name;
		this.id = id;
		this.sectionType = sectionType;
	}
	
	public String getName(){
		return name;
	}
	
	public Object getId(){
		return id;
	}
	
	public Class<?> getSectionType(){
		return sectionType;
	}
	

	@Override
	public int compareTo(PropertyContainer o) {
		return name.compareTo(o.getName());
	}
}
