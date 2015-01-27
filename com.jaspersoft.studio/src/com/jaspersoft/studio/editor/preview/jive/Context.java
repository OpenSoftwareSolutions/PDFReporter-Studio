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
package com.jaspersoft.studio.editor.preview.jive;

import java.util.HashMap;
import java.util.Map;

public class Context {
	private static final Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();

	public static Map<String, Object> getContext(String key) {
		if (key != null)
			return map.get(key);
		return null;
	}

	public static void putContext(String key, Map<String, Object> value) {
		map.put(key, value);
	}

	public static void unsetContext(String key) {
		if (key != null)
			map.remove(key);
	}
}
