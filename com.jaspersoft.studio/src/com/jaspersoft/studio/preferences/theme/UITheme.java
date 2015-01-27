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

import org.eclipse.jface.preference.ColorSelector;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.DataFormatException;
import org.eclipse.jface.resource.StringConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.DesignerPreferencePage;
import com.jaspersoft.studio.preferences.RulersGridPreferencePage;

public class UITheme extends ATheme {
	public UITheme() {
		addProperty(RulersGridPreferencePage.P_PAGE_GRID_COLOR, RulersGridPreferencePage.DEFAULT_GRIDCOLOR);
		addProperty(DesignerPreferencePage.P_CONTAINER_MARGIN_COLOR, DesignerPreferencePage.DEFAULT_MARGINCOLOR);
		addProperty(DesignerPreferencePage.P_PAGE_BACKGROUND, DesignerPreferencePage.DEFAULT_PAGE_BACKGROUND);
		addProperty(DesignerPreferencePage.P_PAGE_MARGIN_COLOR, DesignerPreferencePage.DEFAULT_MARGINCOLOR);
		addProperty(DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_COLOR,
				DesignerPreferencePage.DEFAULT_ELEMENT_DESIGN_BORDER_COLOR);
	}

	@Override
	public Composite createControl(Composite parent, IPreferenceStore store) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));

		createColorProperty(cmp, RulersGridPreferencePage.P_PAGE_GRID_COLOR,
				Messages.RulersGridPreferencePage_common_gridcolor, store);

		createColorProperty(cmp, DesignerPreferencePage.P_CONTAINER_MARGIN_COLOR,
				Messages.DesignerPreferencePage_common_bandmargincolor, store);

		createColorProperty(cmp, DesignerPreferencePage.P_PAGE_MARGIN_COLOR,
				Messages.DesignerPreferencePage_pageprintmargincolor, store);

		createColorProperty(cmp, DesignerPreferencePage.P_PAGE_BACKGROUND, Messages.DesignerPreferencePage_pagebackground,
				store);

		createColorProperty(cmp, DesignerPreferencePage.P_ELEMENT_DESIGN_BORDER_COLOR,
				Messages.DesignerPreferencePage_elementbordercolor, store);

		return cmp;
	}

	public void createColorProperty(Composite cmp, final String prop, String name, final IPreferenceStore store) {
		new Label(cmp, SWT.NONE).setText(name);

		final ColorSelector colorSelector = new ColorSelector(cmp);
		colorSelector.addListener(new IPropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent event) {
				properties.put(prop, StringConverter.asString(colorSelector.getColorValue()));
				save(store);
			}
		});
		try {
			colorSelector.setColorValue(StringConverter.asRGB(properties.get(prop)));
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
	}

}
