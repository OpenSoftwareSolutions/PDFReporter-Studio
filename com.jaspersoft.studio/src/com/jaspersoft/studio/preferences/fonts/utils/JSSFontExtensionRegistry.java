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
package com.jaspersoft.studio.preferences.fonts.utils;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.SimpleFontExtensionHelper;
import net.sf.jasperreports.extensions.ExtensionsRegistry;

import org.eclipse.jface.util.IPropertyChangeListener;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.preferences.fonts.FontsPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JSSFontExtensionRegistry implements ExtensionsRegistry {
	private List<FontFamily> lst;
	private boolean fill = true;

	private PreferenceListener preferenceListener;
	private JasperReportsContext jrContext;

	private final class PreferenceListener implements IPropertyChangeListener {

		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			if (event.getProperty().equals(FontsPreferencePage.FPP_FONT_LIST)) {
				fill = true;
			}
		}
	}

	public JSSFontExtensionRegistry(JRPropertiesMap properties) {
		preferenceListener = new PreferenceListener();
		JaspersoftStudioPlugin.getInstance().getPreferenceStore().addPropertyChangeListener(preferenceListener);
		setJrContext(JasperReportsConfiguration.getDefaultJRConfig());
	}

	public void setJrContext(JasperReportsContext jrContext) {
		this.jrContext = jrContext;
	}

	public <T> List<T> getExtensions(Class<T> extensionType) {
		if (extensionType != FontFamily.class)
			return null;
		if (lst == null)
			lst = new ArrayList<FontFamily>();
		if (fill) {
			String strprop = jrContext.getProperty(FontsPreferencePage.FPP_FONT_LIST);
			if (strprop != null) {
				lst.clear();
				List<FontFamily> fonts = SimpleFontExtensionHelper.getInstance().loadFontFamilies(jrContext,
						new ByteArrayInputStream(strprop.getBytes()));
				if (fonts != null && !fonts.isEmpty())
					lst.addAll(fonts);
			}
			fill = false;
		}

		return (List<T>) lst;
	}
}
