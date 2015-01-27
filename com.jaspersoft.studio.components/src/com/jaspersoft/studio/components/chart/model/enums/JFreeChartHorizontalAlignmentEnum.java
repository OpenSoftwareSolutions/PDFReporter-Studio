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

import org.jfree.ui.HorizontalAlignment;

public enum JFreeChartHorizontalAlignmentEnum implements JREnum {

	CENTER((byte) 0, "Center"), LEFT((byte) 1, "Left"), RIGHT((byte) 2, "Right");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private JFreeChartHorizontalAlignmentEnum(byte value, String name) {
		this.value = value;
		this.name = name;
	}

	public static JFreeChartHorizontalAlignmentEnum getValue(HorizontalAlignment ha) {
		if (ha != null) {
			if (ha.equals(HorizontalAlignment.CENTER))
				return CENTER;
			if (ha.equals(HorizontalAlignment.LEFT))
				return LEFT;
			if (ha.equals(HorizontalAlignment.RIGHT))
				return RIGHT;
		}
		return LEFT;
	}

	public HorizontalAlignment getJFreeChartValue() {
		if (value == 0)
			return HorizontalAlignment.CENTER;
		if (value == 1)
			return HorizontalAlignment.LEFT;
		if (value == 2)
			return HorizontalAlignment.RIGHT;
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
	public static JFreeChartHorizontalAlignmentEnum getByName(String name) {
		return (JFreeChartHorizontalAlignmentEnum) EnumUtil.getByName(values(), name);
	}

	/**
	 *
	 */
	public static JFreeChartHorizontalAlignmentEnum getByValue(Byte value) {
		return (JFreeChartHorizontalAlignmentEnum) EnumUtil.getByValue(values(), value);
	}

	/**
	 *
	 */
	public static JFreeChartHorizontalAlignmentEnum getByValue(byte value) {
		return getByValue(new Byte(value));
	}

}
