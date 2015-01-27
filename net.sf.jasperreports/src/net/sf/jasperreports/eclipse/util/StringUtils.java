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
package net.sf.jasperreports.eclipse.util;

import java.nio.charset.Charset;
import java.util.Set;
import java.util.SortedMap;
import java.util.regex.Pattern;

public final class StringUtils {

	private StringUtils() {
	}

	public static String replaceAllIns(String text, String regexp, String replacement) {
		return Pattern.compile(regexp, Pattern.CASE_INSENSITIVE).matcher(text).replaceAll(replacement);
	}

	// Constants
	public static final String EMPTY_STRING = "";
	public static final String SPACE = " ";
	public static final String TAB = "\t";
	public static final String SINGLE_QUOTE = "'";
	public static final String PERIOD = ".";
	public static final String DOUBLE_QUOTE = "\"";

	public static String toPackageName(String str) {
		String tmp = "";
		for (int i = 0; i < str.length(); i++) {
			char charAt = str.charAt(i);
			if (charAt == '-')
				charAt = '_';
			if (i == 0)
				if (Character.isJavaIdentifierStart(charAt)) {
					tmp += charAt;
					continue;
				} else
					tmp += "_";
			if (Character.isJavaIdentifierPart(charAt))
				tmp += charAt;
		}
		if (tmp.isEmpty())
			return "com";
		return tmp;
	}

	private static String[] encodings;

	public static String[] getEncodings() {
		if (encodings == null) {
			SortedMap<String, Charset> m = Charset.availableCharsets();
			Set<String> keySet = m.keySet();
			encodings = keySet.toArray(new String[keySet.size()]);
		}
		return encodings;
	}

	public static String xmlEncode(String text, String invalidCharReplacement) {
		if (text == null || text.length() == 0) {
			return text;
		}

		int length = text.length();
		StringBuffer ret = new StringBuffer();
		int last = 0;

		for (int i = 0; i < length; i++) {
			char c = text.charAt(i);
			if (Character.isISOControl(c) && c != '\t' && c != '\r' && c != '\n' && c != 0) {
				last = appendText(text, ret, i, last);
				if (invalidCharReplacement == null) {
					// the invalid character is preserved
					ret.append(c);
				} else if ("".equals(invalidCharReplacement)) {
					// the invalid character is removed
					continue;
				} else {
					// the invalid character is replaced
					ret.append(invalidCharReplacement);
				}
			}
		}
		appendText(text, ret, length, last);
		return ret.toString();
	}

	private static int appendText(String text, StringBuffer ret, int current, int old) {
		if (old < current) {
			ret.append(text.substring(old, current));
		}
		return current + 1;
	}
}
