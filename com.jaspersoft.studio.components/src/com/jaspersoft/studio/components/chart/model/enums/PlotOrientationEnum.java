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

public enum PlotOrientationEnum implements JREnum {
	/**
	 * Specifies that the element is opaque.
	 */
	HORIZONTAL((byte) 1, "Horizontal"),

	/**
	 * Specifies that the element is transparent.
	 */
	VERTICAL((byte) 2, "Vertical");

	/**
	 *
	 */
	private static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private final transient byte value;
	private final transient String name;

	private PlotOrientationEnum(byte value, String name) {
		this.value = value;
		this.name = name;
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
	public static PlotOrientationEnum getByName(String name) {
		return (PlotOrientationEnum) EnumUtil.getByName(values(), name);
	}

	/**
	 *
	 */
	public static PlotOrientationEnum getByValue(Byte value) {
		return (PlotOrientationEnum) EnumUtil.getByValue(values(), value);
	}

	/**
	 *
	 */
	public static PlotOrientationEnum getByValue(byte value) {
		return getByValue(new Byte(value));
	}

}
