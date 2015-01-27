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

import net.sf.jasperreports.engine.JRConstants;

public enum AgregationFunctionEnum {
	UNIQUE((int) 0, "Unique"), YEAR((int) 1, "Year"), MONTH((int) 2, "Month"), WEEK(
			(int) 3, "Week"), DAY((int) 4, "Day");

	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient int value;
	private final transient String name;

	private AgregationFunctionEnum(int value, String name) {
		this.value = value;
		this.name = name;
	}

	/**
	 *
	 */
	public int getValueInt() {
		return value;
	}

	/**
	 *
	 */
	public final int getValue() {
		return value;
	}

	/**
	 *
	 */
	public String getName() {
		return name;
	}

	/**
	 *
	 */
	public static AgregationFunctionEnum getByName(String name) {
		for (AgregationFunctionEnum v : values())
			if (v.getName().equals(name))
				return v;
		return null;
	}

	/**
	 *
	 */
	public static AgregationFunctionEnum getByValue(int value) {
		for (AgregationFunctionEnum v : values())
			if (v.getValue() == value)
				return v;
		return null;
	}

	/**
	 *
	 */
	public static AgregationFunctionEnum getByValue(Integer value) {
		return getByValue(value.intValue());
	}

	public static String[] getStringValues() {
		AgregationFunctionEnum[] vals = values();
		String[] svalues = new String[vals.length];
		for (int i = 0; i < svalues.length; i++)
			svalues[i] = vals[i].getName();
		return svalues;
	}
}
