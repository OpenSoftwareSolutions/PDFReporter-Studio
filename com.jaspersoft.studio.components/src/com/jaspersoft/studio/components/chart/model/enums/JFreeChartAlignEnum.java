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

import org.jfree.ui.Align;

public enum JFreeChartAlignEnum implements JREnum {

	CENTER((byte) 0, "Center"), TOP((byte) 1, "Top"), BOTTOM((byte) 2, "Bottom"), LEFT((byte) 3, "Left"), TOP_LEFT((byte) 4, "Top Left"), BOTTOM_LEFT((byte) 5, "Bottom Left"), RIGHT((byte) 6, "Right"), TOP_RIGHT(
			(byte) 7, "Top Right"), BOTTOM_RIGHT((byte) 8, "Bottom Right"), FIT((byte) 9, "Fit"), FIT_HORIZONTAL((byte) 10, "Fit Horizontal"), FIT_VERTICAL((byte) 11, "Fit Vertical");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private JFreeChartAlignEnum(byte value, String name) {
		this.value = value;
		this.name = name;
	}

	public static JFreeChartAlignEnum getValue(Integer ha) {
		if (ha != null)
			switch (ha) {
			case Align.BOTTOM:
				return JFreeChartAlignEnum.BOTTOM;
			case Align.BOTTOM_LEFT:
				return JFreeChartAlignEnum.BOTTOM_LEFT;
			case Align.BOTTOM_RIGHT:
				return JFreeChartAlignEnum.BOTTOM_RIGHT;
			case Align.CENTER:
				return JFreeChartAlignEnum.CENTER;
			case Align.EAST:
				return JFreeChartAlignEnum.RIGHT;
			case Align.FIT:
				return JFreeChartAlignEnum.FIT;
			case Align.FIT_HORIZONTAL:
				return JFreeChartAlignEnum.FIT_HORIZONTAL;
			case Align.FIT_VERTICAL:
				return JFreeChartAlignEnum.FIT_VERTICAL;
			case Align.LEFT:
				return JFreeChartAlignEnum.LEFT;
			case Align.NORTH:
				return JFreeChartAlignEnum.TOP;
			case Align.NORTH_EAST:
				return JFreeChartAlignEnum.TOP_RIGHT;
			case Align.NORTH_WEST:
				return JFreeChartAlignEnum.TOP_LEFT;
			}
		return JFreeChartAlignEnum.TOP_LEFT;
	}

	public static Integer getJFreeChartValue(int value) {
		if (value == 0)
			return Align.CENTER;
		if (value == 1)
			return Align.TOP;
		if (value == 2)
			return Align.BOTTOM;
		if (value == 3)
			return Align.LEFT;
		if (value == 4)
			return Align.TOP_LEFT;
		if (value == 5)
			return Align.BOTTOM_LEFT;
		if (value == 6)
			return Align.RIGHT;
		if (value == 7)
			return Align.TOP_RIGHT;
		if (value == 8)
			return Align.BOTTOM_RIGHT;
		if (value == 9)
			return Align.FIT;
		if (value == 10)
			return Align.FIT_HORIZONTAL;
		if (value == 11)
			return Align.FIT_VERTICAL;

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
	public static JFreeChartAlignEnum getByName(String name) {
		return (JFreeChartAlignEnum) EnumUtil.getByName(values(), name);
	}

	/**
	 *
	 */
	public static JFreeChartAlignEnum getByValue(Byte value) {
		return (JFreeChartAlignEnum) EnumUtil.getByValue(values(), value);
	}

	/**
	 *
	 */
	public static JFreeChartAlignEnum getByValue(byte value) {
		return getByValue(new Byte(value));
	}

}
