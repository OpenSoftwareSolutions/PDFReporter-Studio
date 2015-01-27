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

import org.eclipse.jface.fieldassist.ContentProposal;

/**
 * Container for a property proposal, essentially composed from the name of 
 * the property and its id
 * 
 * @author Orlandin Marco
 *
 */
public class PropertyContentProposal extends ContentProposal implements Comparable<PropertyContentProposal>{

	/**
	 * property id
	 */
	private Object propertyId;
	
	private Class<?> sectionType;
	
	/**
	 * Create a property proposal
	 * 
	 * @param content name of the property
	 * @param propertyId id of the property
	 */
	public PropertyContentProposal(String content, Object propertyId, Class<?> parentSectionType) {
		super(content);
		this.propertyId = propertyId;
		this.sectionType = parentSectionType;
	}
	
	/**
	 * return the id of the property
	 *
	 */
	public Object getPropertyId(){
		return propertyId;
	}
	
	public Class<?> getSectionType() {
		return sectionType;
	}

	@Override
	public int compareTo(PropertyContentProposal o) {
		return getContent().compareTo(o.getContent());
	}

}
