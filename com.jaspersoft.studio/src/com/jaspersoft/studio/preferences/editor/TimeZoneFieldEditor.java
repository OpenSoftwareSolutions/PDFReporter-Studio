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
package com.jaspersoft.studio.preferences.editor;

import java.util.Arrays;
import java.util.TimeZone;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class TimeZoneFieldEditor extends ComboFieldEditor {

	public TimeZoneFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, getTimeZones(), parent);
	}

	private static String[][] tzs;

	private static String[][] getTimeZones() {
		if (tzs == null) {
			String[] tzones = TimeZone.getAvailableIDs();
			Arrays.sort(tzones);
			tzs = new String[tzones.length][2];
			for (int i = 0; i < tzs.length; i++) {
				tzs[i][0] = tzones[i];// TimeZone.getTimeZone(tzones[i]).getDisplayName();
				tzs[i][1] = tzones[i];
			}
		}
		return tzs;
	}

}
