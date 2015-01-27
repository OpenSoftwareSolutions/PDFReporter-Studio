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
package com.jaspersoft.studio.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.toolitems.ToolItemsPreferencePage;
import com.jaspersoft.studio.preferences.execution.ReportExecutionPreferencePage;
import com.jaspersoft.studio.preferences.exporter.CSVExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.DOCXExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.ExcelExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.G2DExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.HTMLExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.JRExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.ODSExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.PDFExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.TextExporterPreferencePage;
import com.jaspersoft.studio.preferences.exporter.XMLExporterPreferencePage;
import com.jaspersoft.studio.preferences.theme.ThemesPreferencePage;

/*
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = JaspersoftStudioPlugin.getInstance().getPreferenceStore();

		initDefaultProperties(store);
	}

	public static void initDefaultProperties(IPreferenceStore store) {
		GlobalPreferencePage.getDefaults(store);
		PropertiesPreferencePage.getDefaults(store);

		DesignerPreferencePage.getDefaults(store);
		ExpressionEditorPreferencePage.getDefaults(store);
		RulersGridPreferencePage.getDefaults(store);
		JRExporterPreferencePage.getDefaults(store);
		CSVExporterPreferencePage.getDefaults(store);
		ExcelExporterPreferencePage.getDefaults(store);
		HTMLExporterPreferencePage.getDefaults(store);
		PDFExporterPreferencePage.getDefaults(store);
		XMLExporterPreferencePage.getDefaults(store);
		TextExporterPreferencePage.getDefaults(store);

		ODSExporterPreferencePage.getDefaults(store);
		DOCXExporterPreferencePage.getDefaults(store);
		G2DExporterPreferencePage.getDefaults(store);

		StudioPreferencePage.getDefaults(store);
		ReportExecutionPreferencePage.getDefaults(store);
		ToolItemsPreferencePage.getDefaults(store);

		ThemesPreferencePage.getDefaults(store);
	}

}
