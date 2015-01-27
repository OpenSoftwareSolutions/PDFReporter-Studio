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
package com.jaspersoft.studio.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRExpression;

public class Misc {

	/**
	 * Return def if object is null, otherwise it return obj.toString().
	 * 
	 * @param obj
	 * @param def
	 * @return
	 */
	public static String nvl(Object obj, String def) {
		if (obj == null)
			return def;
		else
			return obj.toString();
	}

	public static String nvl(String obj) {
		return nvl(obj, "");
	}

	public static <T> T nvl(T obj, T def) {
		if (obj == null)
			return def;
		else
			return obj;
	}

	/**
	 * Don't use it. It does not work well in general, it just does the job for the purposes it has been created for. The
	 * function replaces with \n \r \t \\ sequences newlines, tabs etc...
	 * 
	 * @param str
	 * @return
	 */
	public static String addSlashesString(String str) {
		if (str == null)
			return str;

		String newStr = "";
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			switch (c) {
			case '\n':
				newStr += "\\n";
				break;
			case '\r':
				newStr += "\\r";
				break;
			case '\t':
				newStr += "\\t";
				break;
			case '\\':
				newStr += "\\\\";
				break;
			default:
				newStr += c;
			}
		}
		return newStr;
	}

	/**
	 * Don't use it. It does not work well in general, it just does the job for the purposes it has been created for. The
	 * function parse \n \r \t \\ characters replacing the sequences with real newline, tabs etc...
	 * 
	 * @param str
	 * @return
	 */
	public static String removeSlashesString(String str) {

		if (str == null)
			return str;

		String newStr = "";
		for (int i = 0; i < str.length(); ++i) {
			char c = str.charAt(i);
			if (c == '\\' && str.length() > i + 1) {
				i++;
				char c2 = str.charAt(i);
				switch (c2) {
				case 'n':
					newStr += "\n";
					break;
				case 'r':
					newStr += "\r";
					break;
				case 't':
					newStr += "\t";
					break;
				case '\\':
					newStr += "\\";
					break;
				default: {
					newStr += c;
					newStr += c2;
				}
				}
			} else {
				newStr += c;
			}
		}

		return newStr;
	}

	public static int indexOf(String[] array, String key) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equalsIgnoreCase(key))
				return i;
		return -1;
	}

	public static String nullValue(String value) {
		if (value != null && value.trim().isEmpty())
			return null;
		return value;
	}

	public static boolean isNullOrEmpty(String value) {
		return value == null || value.trim().isEmpty();
	}

	public static boolean isNullOrEmpty(Collection<?> value) {
		return value == null || value.isEmpty();
	}

	public static String nullIfEmpty(String value) {
		return value != null && value.isEmpty() ? null : value;
	}

	public static String strReplace(String s1, String s2, String s3) {
		String string = "";
		if (s2 == null || s3 == null || isNullOrEmpty(s2))
			return s3;

		int pos_i = 0; // posizione corrente.
		int pos_f = 0; // posizione corrente finale

		int len = s2.length();
		while ((pos_f = s3.indexOf(s2, pos_i)) >= 0) {
			string += s3.substring(pos_i, pos_f) + s1;
			// +string.substring(pos+ s2.length());
			pos_f = pos_i = pos_f + len;
		}
		return string + s3.substring(pos_i);
	}

	public static String getExpressionText(JRExpression exp) {
		if (exp == null || exp.getText() == null)
			return "";
		return exp.getText();
	}

	public static boolean compare(String a, String b, boolean caseSensitive) {
		if (caseSensitive)
			return a.equals(b);
		return a.equalsIgnoreCase(b);
	}

	public static String getBase(String resid) {
		if (resid.indexOf(".") > 0)
			return resid.substring(0, resid.indexOf("."));
		return resid;
	}

	public static String extract(String expr, String start, String end) {
		int sindx = expr.indexOf(start) + start.length();
		return expr.substring(sindx, expr.indexOf(end, sindx));
	}

	public static <K extends Comparable<K>, V extends Comparable<V>> LinkedHashMap<K, V> sortByValues(Map<K, V> map) {
		List<Map.Entry<K, V>> entries = new ArrayList<Map.Entry<K, V>>(map.entrySet());
		Collections.sort(entries, new Comparator<Map.Entry<K, V>>() {
			public int compare(Map.Entry<K, V> a, Map.Entry<K, V> b) {
				return a.getValue().compareTo(b.getValue());
			}
		});
		LinkedHashMap<K, V> sortedMap = new LinkedHashMap<K, V>();
		for (Map.Entry<K, V> entry : entries)
			sortedMap.put(entry.getKey(), entry.getValue());
		return sortedMap;
	}

	public static String quote(String value, String q, boolean onlyException) {
		if (onlyException) {
			boolean useQuote = false;
			for (char c : value.toCharArray()) {
				if (!(Character.isLetterOrDigit(c) || c == '_' || c == '-') || Character.isUpperCase(c)) {
					useQuote = true;
					break;
				}
			}
			if (!useQuote)
				return value;
		}
		String str = q + value;
		if (q.equals("["))
			q = "]";
		return str + q;
	}
}
