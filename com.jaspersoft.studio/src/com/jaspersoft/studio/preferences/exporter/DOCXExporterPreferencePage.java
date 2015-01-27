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
package com.jaspersoft.studio.preferences.exporter;

import net.sf.jasperreports.export.DocxReportConfiguration;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;

/*
 * 
 */
public class DOCXExporterPreferencePage extends FieldEditorOverlayPage {

	public DOCXExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.DOCXExporterPreferencePage_title);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		BooleanFieldEditor bf = new BooleanFieldEditor(DocxReportConfiguration.PROPERTY_FRAMES_AS_NESTED_TABLES,
				Messages.DOCXExporterPreferencePage_3, getFieldEditorParent());
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(DocxReportConfiguration.PROPERTY_FLEXIBLE_ROW_HEIGHT,
				Messages.DOCXExporterPreferencePage_4, getFieldEditorParent());
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(DocxReportConfiguration.PROPERTY_FRAMES_AS_NESTED_TABLES,
				PropertiesHelper.DPROP.getProperty(DocxReportConfiguration.PROPERTY_FRAMES_AS_NESTED_TABLES));
		store.setDefault(DocxReportConfiguration.PROPERTY_FLEXIBLE_ROW_HEIGHT,
				PropertiesHelper.DPROP.getProperty(DocxReportConfiguration.PROPERTY_FLEXIBLE_ROW_HEIGHT));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {
	}

	@Override
	protected String getPageId() {
		return "com.jaspersoft.studio.preferences.exporter.DOCXExporterPreferencePage.property"; //$NON-NLS-1$
	}

}
