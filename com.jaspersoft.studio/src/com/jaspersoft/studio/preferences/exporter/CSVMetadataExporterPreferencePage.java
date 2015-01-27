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

import net.sf.jasperreports.eclipse.viewer.BrowserUtils;
import net.sf.jasperreports.export.CsvExporterConfiguration;
import net.sf.jasperreports.export.CsvMetadataReportConfiguration;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.editor.text.NStringFieldEditor;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;

/*
 * 
 */
public class CSVMetadataExporterPreferencePage extends FieldEditorOverlayPage {

	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.exporter.CSVExporterPreferencePage.property"; //$NON-NLS-1$

	public CSVMetadataExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.CSVExporterPreferencePage_title);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		NStringFieldEditor tf = new NStringFieldEditor(CsvExporterConfiguration.PROPERTY_RECORD_DELIMITER,
				Messages.CSVExporterPreferencePage_3, 4, getFieldEditorParent());
		tf.setEmptyStringAllowed(false);
		tf.setTextLimit(10);
		addField(tf);
		HelpSystem.setHelp(tf.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + tf.getPreferenceName());

		tf = new NStringFieldEditor(CsvExporterConfiguration.PROPERTY_FIELD_DELIMITER,
				Messages.CSVExporterPreferencePage_4, 4, getFieldEditorParent());
		tf.setEmptyStringAllowed(false);
		tf.setTextLimit(10);
		addField(tf);
		HelpSystem.setHelp(tf.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + tf.getPreferenceName());

		Browser browser = BrowserUtils.getSWTBrowserWidget(getFieldEditorParent(), SWT.MULTI);
		browser.setText(Messages.CSVExporterPreferencePage_5);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		browser.setLayoutData(gd);

		BooleanFieldEditor bf = new BooleanFieldEditor(CsvMetadataReportConfiguration.PROPERTY_WRITE_HEADER,
				Messages.CSVMetadataExporterPreferencePage_writeHeaders, getFieldEditorParent());
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		tf = new NStringFieldEditor(CsvMetadataReportConfiguration.PROPERTY_COLUMN_NAMES_PREFIX,
				Messages.CSVMetadataExporterPreferencePage_columnNames, getFieldEditorParent());
		addField(tf);
		HelpSystem.setHelp(tf.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + tf.getPreferenceName() + ".{suffix}"); //$NON-NLS-1$
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(CsvExporterConfiguration.PROPERTY_RECORD_DELIMITER,
				PropertiesHelper.DPROP.getProperty(CsvExporterConfiguration.PROPERTY_RECORD_DELIMITER));
		store.setDefault(CsvExporterConfiguration.PROPERTY_FIELD_DELIMITER,
				PropertiesHelper.DPROP.getProperty(CsvExporterConfiguration.PROPERTY_FIELD_DELIMITER));

		store.setDefault(CsvMetadataReportConfiguration.PROPERTY_COLUMN_NAMES_PREFIX,
				PropertiesHelper.DPROP.getProperty(CsvMetadataReportConfiguration.PROPERTY_COLUMN_NAMES_PREFIX));
		store.setDefault(CsvMetadataReportConfiguration.PROPERTY_WRITE_HEADER,
				PropertiesHelper.DPROP.getProperty(CsvMetadataReportConfiguration.PROPERTY_WRITE_HEADER));
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
		return PAGE_ID; //$NON-NLS-1$
	}

}
