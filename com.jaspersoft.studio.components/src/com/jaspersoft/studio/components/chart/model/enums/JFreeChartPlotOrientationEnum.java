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

import org.jfree.chart.plot.PlotOrientation;

public enum JFreeChartPlotOrientationEnum implements JREnum {

	HORIZONTAL((byte) 0, "Horizontal"), VERTICAL((byte) 1, "Vertical");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private JFreeChartPlotOrientationEnum(byte value, String name) {
		this.value = value;
		this.name = name;
	}

	public static JFreeChartPlotOrientationEnum getValue(PlotOrientation ha) {
		if (ha != null) {
			if (ha.equals(PlotOrientation.HORIZONTAL))
				return HORIZONTAL;
			if (ha.equals(PlotOrientation.VERTICAL))
				return VERTICAL;
		}
		return VERTICAL;
	}

	public PlotOrientation getJFreeChartValue() {
		if (value == 0)
			return PlotOrientation.HORIZONTAL;
		if (value == 1)
			return PlotOrientation.VERTICAL;
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
	public static JFreeChartPlotOrientationEnum getByName(String name) {
		return (JFreeChartPlotOrientationEnum) EnumUtil.getByName(values(), name);
	}

	/**
	 *
	 */
	public static JFreeChartPlotOrientationEnum getByValue(Byte value) {
		return (JFreeChartPlotOrientationEnum) EnumUtil.getByValue(values(), value);
	}

	/**
	 *
	 */
	public static JFreeChartPlotOrientationEnum getByValue(byte value) {
		return getByValue(new Byte(value));
	}

}
