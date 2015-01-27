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
package com.jaspersoft.studio.editor.preview;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.stats.Statistics;
import com.jaspersoft.studio.editor.preview.view.APreview;

public class MultiPageContainer {
	protected LinkedHashMap<String, APreview> pmap = new LinkedHashMap<String, APreview>();
	private List<String> keys;
	private Composite composite;
	private Statistics stats;

	public APreview getViewer(String key) {
		return pmap.get(key);
	}

	public void switchView(String key) {
		switchView(stats, pmap.get(key));
	}

	public void switchView(Statistics stats, String key) {
		switchView(stats, pmap.get(key));
	}

	public void switchView(Statistics stats, APreview view) {
		this.stats = stats;
		if (composite.isDisposed())
			return;
		((StackLayout) composite.getLayout()).topControl = view.getControl();
		composite.layout();
		// here should update exporters properties
	}

	public void afterSwitchView() {

	}

	public List<String> getKeys() {
		if (keys == null)
			keys = new ArrayList<String>(pmap.keySet());
		return keys;
	}

	public void populate(Composite composite, LinkedHashMap<String, APreview> pmap) {
		this.composite = composite;
		this.pmap = pmap;
	}

	public void dispose() {
		for (APreview p : pmap.values()) {
			if (p != null)
				p.dispose();
		}
	}

	public void setEnabled(boolean enabled) {
		for (APreview p : pmap.values()) {
			if (p != null)
				p.setEnabled(enabled);
		}
	}
}
