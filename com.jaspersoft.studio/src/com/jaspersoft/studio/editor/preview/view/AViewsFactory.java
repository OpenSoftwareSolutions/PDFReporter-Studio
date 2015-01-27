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
package com.jaspersoft.studio.editor.preview.view;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.Set;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.preferences.exporter.JRExporterPreferencePage;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class AViewsFactory {

	/**
	 * Return the available keys for the preview area, may contains separator
	 */
	public abstract Set<String> getKeys();

	protected abstract LinkedHashMap<String, Class<? extends APreview>> getMap();

	/**
	 * Return true if a key is invalid or a separator
	 */
	public boolean isSeparator(String key) {
		return getMap().get(key) == null;
	}

	public String getLabel(String key) {
		return key;
	}

	public LinkedHashMap<String, APreview> createPreviews(final Composite composite,
			final JasperReportsConfiguration jContext) {
		LinkedHashMap<String, APreview> pmap = new LinkedHashMap<String, APreview>() {
			public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

			@Override
			public APreview get(Object key) {
				APreview val = super.get(key);
				if (val == null && getMap().get(key) != null) {
					Class<? extends APreview> clazz = getMap().get(key);
					try {
						val = clazz.getConstructor(Composite.class, JasperReportsConfiguration.class).newInstance(composite,
								jContext);
					} catch (IllegalArgumentException e) {
						e.printStackTrace();
					} catch (SecurityException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						e.printStackTrace();
					}
					put((String) key, val);
				}
				return val;
			}
		};
		for (String key : getMap().keySet()) {
			if (key.equals("ExcelAPI")
					&& !jContext.getPropertyBoolean(JRExporterPreferencePage.COM_JASPERSOFT_STUDIO_EXPORTER_SHOW_EXCELAPI, false))
				continue;
			if (key.equals("xHTML")
					&& !jContext.getPropertyBoolean(JRExporterPreferencePage.COM_JASPERSOFT_STUDIO_EXPORTER_SHOW_XHTML, false))
				continue;
			if (key.equals("XLS Metadata")
					&& !jContext.getPropertyBoolean(
							JRExporterPreferencePage.COM_JASPERSOFT_STUDIO_EXPORTER_SHOW_EXCELAPI_METADATA, false))
				continue;
			pmap.put(key, null);
		}
		return pmap;
	}

}
