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
package com.jaspersoft.studio.data;

import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignField;

public interface IFieldSetter {
	
	/**
	 * Sets the specified fields, old ones get discarded.
	 * 
	 * @param fields
	 */
	public void setFields(List<JRDesignField> fields);
	
	/**
	 * Adds new fields to existing ones, if any.
	 * 
	 * @param fields new fields to add
	 */
	public void addFields(List<JRDesignField> fields);
	
	/**
	 * Clears the list of existing fields.
	 */
	public void clearFields();
	
}
