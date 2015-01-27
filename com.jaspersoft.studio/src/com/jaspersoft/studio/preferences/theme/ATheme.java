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
package com.jaspersoft.studio.preferences.theme;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;

public abstract class ATheme {
	protected Map<String, String> properties = new HashMap<String, String>();
	private String name;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void put(String key, String value) {
		properties.put(key, value);
	}

	protected void addProperty(String id, String value) {
		properties.put(id, value);
	}

	public static ATheme load(IPreferenceStore store, String n) {
		UITheme theme = new UITheme();
		theme.setName(n);
		String t = store.getString("com.jaspersoft.studio.theme.ui." + n);
		if (t == null || t.isEmpty())
			return null;
		String[] lines = t.split("\n");
		for (String line : lines) {
			String[] prop = line.split("=");
			if (prop != null && prop.length == 2)
				theme.put(prop[0], prop[1]);
		}
		return theme;
	}

	public void save(IPreferenceStore store) {
		String value = "";
		for (String key : properties.keySet()) {
			value += key + "=" + properties.get(key) + "\n";
		}
		store.setValue("com.jaspersoft.studio.theme.ui." + name, value);
	}

	public void apply(IPreferenceStore store) {
		for (String key : properties.keySet())
			store.setValue(key, properties.get(key));
	}

	public abstract Composite createControl(Composite parent, IPreferenceStore store);
}
