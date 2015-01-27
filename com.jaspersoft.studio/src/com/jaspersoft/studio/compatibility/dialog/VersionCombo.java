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
package com.jaspersoft.studio.compatibility.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;

public class VersionCombo {
	private String version;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
		for (int i = 0; i < versions.length; i++)
			if (versions[i][1].equals(version)) {
				combo.select(i);
				break;
			}
	}

	private static final String[][] versions = JRXmlWriterHelper.getVersions();
	private Combo combo;

	private String[] getItems() {
		String[] r = new String[versions.length];
		for (int i = 0; i < versions.length; i++)
			r[i] = versions[i][0];
		return r;
	}

	private int getVersionIndex() {
		for (int i = 0; i < versions.length; i++)
			if (versions[i][1].equals(version))
				return i;
		return 0;
	}

	public VersionCombo(Composite container) {
		combo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		combo.setItems(getItems());
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int ind = combo.getSelectionIndex();
				if (ind >= 0 && ind < versions.length)
					version = versions[ind][1];
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		combo.select(getVersionIndex());
	}

	public Combo getControl() {
		return combo;
	}

	public static String getJrVersion(String v) {
		for (int i = 0; i < versions.length; i++)
			if (versions[i][0].equals(v))
				return versions[i][1];
		return null;
	}

	public static String getLabelVersion(String v) {
		for (int i = 0; i < versions.length; i++)
			if (versions[i][1].equals(v))
				return versions[i][0];
		return null;
	}

}
