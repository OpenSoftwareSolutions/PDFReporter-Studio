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

import java.lang.reflect.Field;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class TComboEditor extends ComboFieldEditor {
	private Combo fCombo;

	public TComboEditor(String name, String labelText, String[][] entryNamesAndValues, Composite parent) {
		super(name, labelText, entryNamesAndValues, parent);
	}

	@Override
	protected void createControl(Composite parent) {
		super.createControl(parent);
		for (Control c : parent.getChildren()) {
			if (c instanceof Combo) {
				fCombo = (Combo) c;
				break;
			}
		}
	}

	public void addSelectionListener(SelectionListener listener) {
		fCombo.addSelectionListener(listener);
	}

	public void removeSelectionListener(SelectionListener listener) {
		fCombo.removeSelectionListener(listener);
	}

	public String getValue() {
		return fCombo.getText();
	}

	public void refresh(String[][] tmatrix) {
		try {
			Field f = ComboFieldEditor.class.getDeclaredField("fEntryNamesAndValues");
			f.setAccessible(true);
			f.set(this, tmatrix);

			String[] items = new String[tmatrix.length];
			for (int i = 0; i < tmatrix.length; i++)
				items[i] = tmatrix[i][0];
			fCombo.setItems(items);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public void setSelection(String item) {
		int i = 0;
		for (String key : fCombo.getItems()) {
			if (key.equals(item))
				break;
			i++;
		}
		fCombo.select(i);
	}
}
