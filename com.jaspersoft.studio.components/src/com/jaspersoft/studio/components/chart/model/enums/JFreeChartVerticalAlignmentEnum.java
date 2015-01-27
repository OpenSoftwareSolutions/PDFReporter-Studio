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

import org.jfree.ui.VerticalAlignment;

public enum JFreeChartVerticalAlignmentEnum implements JREnum {

	TOP((byte) 0, "Top"), CENTER((byte) 1, "Center"), BOTTOM((byte) 2, "Bottom");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private JFreeChartVerticalAlignmentEnum(byte value, String name) {
		this.value = value;
		this.name = name;
	}

	public static JFreeChartVerticalAlignmentEnum getValue(VerticalAlignment ha) {
		if (ha != null) {
			if (ha.equals(VerticalAlignment.CENTER))
				return CENTER;
			if (ha.equals(VerticalAlignment.TOP))
				return TOP;
			if (ha.equals(VerticalAlignment.BOTTOM))
				return BOTTOM;
		}
		return TOP;
	}

	public VerticalAlignment getJFreeChartValue() {
		if (value == 0)
			return VerticalAlignment.TOP;
		if (value == 1)
			return VerticalAlignment.CENTER;
		if (value == 2)
			return VerticalAlignment.BOTTOM;
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
	public static JFreeChartVerticalAlignmentEnum getByName(String name) {
		return (JFreeChartVerticalAlignmentEnum) EnumUtil.getByName(values(), name);
	}

	/**
	 *
	 */
	public static JFreeChartVerticalAlignmentEnum getByValue(Byte value) {
		return (JFreeChartVerticalAlignmentEnum) EnumUtil.getByValue(values(), value);
	}

	/**
	 *
	 */
	public static JFreeChartVerticalAlignmentEnum getByValue(byte value) {
		return getByValue(new Byte(value));
	}

}
