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
package com.jaspersoft.studio.utils.inputhistory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InputHistoryCache {
	private static Map<Object, Set<String>> cache = new HashMap<Object, Set<String>>();
	private static final String[] EMPTYARRAY = new String[0];

	public static String[] get(Object key) {
		Set<String> set = cache.get(key);
		if (set == null)
			return EMPTYARRAY;
		return set.toArray(new String[set.size()]);
	}

	public static void put(Object key, String value) {
		Set<String> set = cache.get(key);
		if (set == null) {
			set = new HashSet<String>();
			cache.put(key, set);
		}
		((HashSet<String>) set).add(value);
	}
}
