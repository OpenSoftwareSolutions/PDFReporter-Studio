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
package com.jaspersoft.hadoop.hbase.query;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRValueParameter;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseParameter implements JRValueParameter {

	private String name;

	private Object value;

	public HBaseParameter(String name, Object value) {
		this.name = name;
		this.value = value;
	}

	public Object clone() {
		return null;
	}

	@Override
	public boolean hasProperties() {
		return false;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public void setDescription(String description) {
	}

	@Override
	public Class<?> getValueClass() {
		if (value != null) {
			return value.getClass();
		}
		return null;
	}

	@Override
	public String getValueClassName() {
		if (value != null) {
			return value.getClass().getName();
		}
		return null;
	}

	@Override
	public boolean isSystemDefined() {
		return false;
	}

	@Override
	public boolean isForPrompting() {
		return false;
	}

	@Override
	public JRExpression getDefaultValueExpression() {
		return null;
	}

	@Override
	public Class<?> getNestedType() {
		return null;
	}

	@Override
	public String getNestedTypeName() {
		return null;
	}

	@Override
	public JRPropertiesMap getPropertiesMap() {
		return null;
	}

	@Override
	public JRPropertiesHolder getParentProperties() {
		return null;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public void setValue(Object value) {
	}
}
