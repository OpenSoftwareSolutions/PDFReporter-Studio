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
package com.jaspersoft.studio.swt.widgets;

import java.util.Arrays;
import java.util.Locale;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

public class WLocale extends Composite {
	private Combo combo;
	private static Locale[] locales;
	private static String[] strLocales;

	public WLocale(Composite parent, int style) {
		super(parent, SWT.NONE);
		combo = new Combo(this, style);
		combo.setItems(getLocales());
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		setLayout(layout);
		combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
	}

	@Override
	public void setToolTipText(String string) {
		super.setToolTipText(string);
		combo.setToolTipText(string);
	}

	public void addSelectionListener(SelectionListener m) {
		combo.addSelectionListener(m);
	}

	public void removeSelectionListener(SelectionListener m) {
		combo.removeSelectionListener(m);
	}

	public Combo getCombo() {
		return combo;
	}

	private String[] getLocales() {
		if (locales == null) {
			locales = Locale.getAvailableLocales();
			strLocales = new String[locales.length];
			for (int i = 0; i < strLocales.length; i++)
				strLocales[i] = locales[i].getDisplayName();
			Arrays.sort(strLocales);
		}
		return strLocales;
	}

	public void setSelection(Locale locale) {
		int index;
		if (locale == null)
			index = getIndexFromLocale(Locale.getDefault());
		else
			index = getIndexFromLocale(locale);
		combo.select(index);
	}

	public Locale getLocale() {
		int selectionIndex = combo.getSelectionIndex();
		if (selectionIndex < 0)
			return Locale.getDefault();
		else {
			String strLocale = strLocales[combo.getSelectionIndex()];
			for (int i = 0; i < locales.length; i++) {
				if (locales[i].getDisplayName().equals(strLocale))
					return locales[i];
			}
			return Locale.getDefault();
		}
	}

	/**
	 * This returns the list index for a given locale.
	 * 
	 * @param locale
	 * @return int index
	 */
	private int getIndexFromLocale(Locale locale) {
		int returnedIndex = -1;
		if (locale != null) {
			for (int i = 0; i < strLocales.length; i++) {
				if (strLocales[i].equals(locale.getDisplayName()))
					returnedIndex = i;
			}
		}
		return returnedIndex;
	}
}
