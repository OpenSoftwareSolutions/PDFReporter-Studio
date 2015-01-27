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
package com.jaspersoft.studio.property.descriptor.properties.dialog;

import com.jaspersoft.studio.model.ANode;

/*
 * @author Chicu Veaceslav
 */
public class PropertyDTO implements Cloneable {
	private ANode pnode;

	public ANode getPnode() {
		return pnode;
	}

	public void setPnode(ANode pnode) {
		this.pnode = pnode;
	}

	public PropertyDTO() {
		super();
	}

	public PropertyDTO(String property, String description, Object defValue) {
		super();
		this.property = property;
		this.description = description;
		this.defValue = defValue;
	}

	public PropertyDTO(String property, Object value) {
		super();
		this.property = property;
		this.value = value;
	}

	private String property;
	private String description;
	private Object defValue;
	private Object value;

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getDefValue() {
		return defValue;
	}

	public void setDefValue(String defValue) {
		this.defValue = defValue;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
