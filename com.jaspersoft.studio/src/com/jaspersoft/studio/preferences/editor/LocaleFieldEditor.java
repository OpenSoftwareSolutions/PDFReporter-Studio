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
import java.util.Comparator;
import java.util.Locale;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class LocaleFieldEditor extends ComboFieldEditor {

	public LocaleFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, getLocales(), parent);
	}

	private static String[][] locs;

	private static String[][] getLocales() {
		if (locs == null) {
			Locale[] locales = Locale.getAvailableLocales();
			sortLocalesOnToString(locales);
			locs = new String[locales.length][2];
			for (int i = 0; i < locs.length; i++) {
				locs[i][0] = locales[i].getDisplayName();
				locs[i][1] = locales[i].toString();
			}
		}
		return locs;
	}

	public static void sortLocalesOnToString(Locale[] locales) {
		Comparator<Locale> localeComparator = new Comparator<Locale>() {
			public int compare(Locale locale1, Locale locale2) {
				return locale1.getDisplayName().compareTo(locale2.getDisplayName());
			}
		};
		Arrays.sort(locales, localeComparator);
	}
}
