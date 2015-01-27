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
package com.jaspersoft.studio.components.chart.model.enums;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.type.EnumUtil;
import net.sf.jasperreports.engine.type.JREnum;

import org.jfree.chart.axis.AxisLocation;

public enum JFreeChartAxisLocationEnum implements JREnum {

	TOP_OR_LEFT((byte) 0, "Top Or Left"), TOP_OR_RIGHT((byte) 1, "Top Or Right"), BOTTOM_OR_LEFT((byte) 2, "Bottom Or Left"), BOTTOM_OR_RIGHT((byte) 3, "Bottom Or Right");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private JFreeChartAxisLocationEnum(byte value, String name) {
		this.value = value;
		this.name = name;
	}

	public static JFreeChartAxisLocationEnum getValue(AxisLocation ha) {
		if (ha != null) {
			if (ha.equals(AxisLocation.TOP_OR_LEFT))
				return TOP_OR_LEFT;
			if (ha.equals(AxisLocation.TOP_OR_RIGHT))
				return TOP_OR_RIGHT;
			if (ha.equals(AxisLocation.BOTTOM_OR_LEFT))
				return BOTTOM_OR_LEFT;
			if (ha.equals(AxisLocation.BOTTOM_OR_RIGHT))
				return BOTTOM_OR_RIGHT;
		}
		return BOTTOM_OR_RIGHT;
	}

	public AxisLocation getJFreeChartValue() {
		if (value == 0)
			return AxisLocation.TOP_OR_LEFT;
		if (value == 1)
			return AxisLocation.TOP_OR_RIGHT;
		if (value == 2)
			return AxisLocation.BOTTOM_OR_LEFT;
		if (value == 3)
			return AxisLocation.BOTTOM_OR_RIGHT;
		return null;
	}

	/**
	 *
	 */
	public Byte getValueByte() {
		return new Byte(value);
	}

	/**
	 *
	 */
	public final byte getValue() {
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
	public static JFreeChartAxisLocationEnum getByName(String name) {
		return (JFreeChartAxisLocationEnum) EnumUtil.getByName(values(), name);
	}

	/**
	 *
	 */
	public static JFreeChartAxisLocationEnum getByValue(Byte value) {
		return (JFreeChartAxisLocationEnum) EnumUtil.getByValue(values(), value);
	}

	/**
	 *
	 */
	public static JFreeChartAxisLocationEnum getByValue(byte value) {
		return getByValue(new Byte(value));
	}

}
