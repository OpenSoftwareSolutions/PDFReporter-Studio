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
package com.jaspersoft.studio.components.crosstab.model.crosstab.command.wizard;

import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;

public class Wrapper {
	private Object value;
	private AgregationFunctionEnum calculation = AgregationFunctionEnum.UNIQUE;
	private String oldExpText;
	private String label = null;

	public Wrapper(Object value) {
		super();
		this.value = value;
		if (value instanceof JRDesignCrosstabColumnGroup) {
			JRDesignCrosstabColumnGroup cg = (JRDesignCrosstabColumnGroup) value;
			oldExpText = cg.getBucket().getExpression().getText();
		} else if (value instanceof JRDesignCrosstabRowGroup) {
			JRDesignCrosstabRowGroup rg = (JRDesignCrosstabRowGroup) value;
			oldExpText = rg.getBucket().getExpression().getText();
		}
	}

	public String getOldExpText() {
		return oldExpText;
	}

	public Object getValue() {
		return value;
	}

	public void setCalculation(AgregationFunctionEnum calculation) {
		this.calculation = calculation;
	}

	public AgregationFunctionEnum getCalculation() {
		return calculation;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Wrapper)
			return value.equals(((Wrapper) obj).getValue());
		return super.equals(obj);
	}

	@Override
	public int hashCode() {
		return value.hashCode();
	}
	
	/**
	 * Return the name of the object without any special syntax like $ etc..
	 * The value comes from the old expression text
	 * which we assume is in the form $?{AAAAAA}
	 *
	 *  @return String
	 */
	public String getLabel() {
		
		String label = getOldExpText();
		if (label == null) return ""; // this case should never be true.
		
		label = label.substring(3, label.length() - 1);
		return label;
	}

}
