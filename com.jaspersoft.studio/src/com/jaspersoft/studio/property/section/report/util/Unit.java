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
package com.jaspersoft.studio.property.section.report.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class Unit {
	private int dpi = 72;

	public static final String MM = "mm";
	public static final String CM = "cm";
	public static final String METER = "m";
	public static final String INCH = "inch";
	public static final String PX = "pixel";

	private static final Map<String, BigDecimal> units = new LinkedHashMap<String, BigDecimal>();
	static {
		units.put(PX, new BigDecimal(1));
		units.put(MM, new BigDecimal("25.4"));
		units.put(CM, new BigDecimal("2.54"));
		units.put(METER, new BigDecimal("0.0254"));
		units.put(INCH, new BigDecimal(1));
	}

	/**
	 * Map of the alias of a unit, because there can be more ways to require a unit
	 */
	private static final Map<String, String> alias = new LinkedHashMap<String, String>();
	static {
		alias.put("pixel", PX);
		alias.put("pixels", PX);
		alias.put("px", PX);
		alias.put(PX, PX);

		alias.put("cm", CM);
		alias.put("centimeter", CM);
		alias.put("centimeters", CM);
		alias.put(CM, CM);

		alias.put("mm", MM);
		alias.put("millimeter", MM);
		alias.put("millimeters", MM);
		alias.put(MM, MM);

		alias.put("inches", INCH);
		alias.put("inch", INCH);
		alias.put("''", INCH);
		alias.put("\"", INCH);
		alias.put(INCH, INCH);

		alias.put("meter", METER);
		alias.put("meters", METER);
		alias.put("m", METER);
		alias.put(METER, METER);
	}

	// value in pixel
	private int value = 0;
	private String unit = PX;

	public Unit(double value, String unit, JasperReportsConfiguration jConfig) {
		super();
		setValue(value, unit);
		dpi = Misc.nvl(jConfig.getPropertyInteger("net.sf.jasperreports.image.dpi"), dpi);
	}

	private static String[] unitsArrays;

	public boolean setUnit(String unit) {
		if (this.unit.equals(unit))
			return false;
		if (units.get(unit) != null) {
			this.unit = unit;
			return true;
		}
		return false;
	}

	private int toValue(double value, BigDecimal c) {
		int pixel = new BigDecimal(value * dpi).divide(c, 0, RoundingMode.FLOOR).intValue();
		// System.out.println("TO -> [" + unit + "] Value: " + value + " C: " + c + " pixel:" + pixel);
		return pixel;
	}

	public double pixel2unit(int val) {
		if (unit.equals(PX))
			return val;
		BigDecimal c = units.get(unit);
		if (c != null)
			return c.multiply(new BigDecimal(val)).divide(new BigDecimal(dpi), 4, RoundingMode.CEILING).doubleValue();
		return val;
	}

	private double fromValue(BigDecimal c) {
		double uval = c.multiply(new BigDecimal(value)).divide(new BigDecimal(dpi), 4, RoundingMode.CEILING).doubleValue();
		// System.out.println("FROM -> Value: " + value + " C: " + c + " REZULT:" + uval + "[" + unit + "]");
		return uval;
	}

	public void setValue(double value, String unit) {
		if (unit.equals(PX))
			this.value = (int) value;
		else {
			BigDecimal c = units.get(unit);
			if (c != null) {
				this.value = toValue(value, c);
				this.unit = unit;
			}
		}
	}

	public double getValue(String unit) {
		if (unit.equals(PX))
			return value;
		BigDecimal c = units.get(unit);
		if (c != null) {
			return fromValue(c);
		}
		return 0.0f;
	}

	public int getPxValue() {
		return value;
	}

	public String getUnit() {
		return unit;
	}

	public void setDPI(int dpi) {
		this.dpi = dpi;
	}

	public static int getUnitIndex(String key) {
		int ind = 0;
		for (int i = 0; i < unitsArrays.length; i++)
			if (unitsArrays[i].equals(key))
				return i;
		return ind;
	}

	public static String[] getUnits() {
		if (unitsArrays == null)
			unitsArrays = units.keySet().toArray(new String[units.keySet().size()]);
		return unitsArrays;
	}

	/**
	 * Given an alias return its value
	 * 
	 * @param aliasValue
	 *          the alias
	 * @return the key value, or null if the alias is not recognized
	 */
	public static String getKeyFromAlias(String aliasValue) {
		return alias.get(aliasValue);
	}

	/**
	 * Add a new alias to the map
	 * 
	 * @param aliasName
	 *          the alias name
	 * @param key
	 *          the key corresponding to the alias
	 */
	public static void addAlias(String aliasName, String key) {
		alias.put(aliasName, key);
	}

	/**
	 * Return a list of all the alias
	 * 
	 * @return list of the alias names, useful for the autocomplete
	 */
	public static String[] getAliasList() {
		ArrayList<String> result = new ArrayList<String>();
		for (String key : alias.keySet())
			result.add(key);
		return result.toArray(new String[result.size()]);
	}

	public static String[][] getUnits2() {
		String[][] res = new String[getUnits().length][2];
		for (int i = 0; i < res.length; i++) {
			res[i][0] = unitsArrays[i];
			res[i][1] = unitsArrays[i];
		}
		return res;
	}
}
