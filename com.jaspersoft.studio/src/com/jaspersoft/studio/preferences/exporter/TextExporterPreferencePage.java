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

import net.sf.jasperreports.export.TextExporterConfiguration;
import net.sf.jasperreports.export.TextReportConfiguration;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.editor.number.FloatFieldEditor;
import com.jaspersoft.studio.preferences.editor.text.NStringFieldEditor;
import com.jaspersoft.studio.preferences.editor.text.TextFieldEditor;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.Misc;

/*
 * 
 */
public class TextExporterPreferencePage extends FieldEditorOverlayPage {

	public TextExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.TextExporterPreferencePage_6);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		FloatFieldEditor ffe = new FloatFieldEditor(TextReportConfiguration.PROPERTY_CHARACTER_WIDTH,
				Messages.TextExporterPreferencePage_7, getFieldEditorParent());
		addField(ffe);
		HelpSystem.setHelp(ffe.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + ffe.getPreferenceName());

		ffe = new FloatFieldEditor(TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT,
				Messages.TextExporterPreferencePage_8, getFieldEditorParent());
		addField(ffe);
		HelpSystem.setHelp(ffe.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + ffe.getPreferenceName());

		IntegerFieldEditor ife = new IntegerFieldEditor(TextReportConfiguration.PROPERTY_PAGE_WIDTH,
				Messages.TextExporterPreferencePage_9, getFieldEditorParent());
		addField(ife);
		HelpSystem.setHelp(ife.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + ife.getPreferenceName());

		ife = new IntegerFieldEditor(TextReportConfiguration.PROPERTY_PAGE_HEIGHT, Messages.TextExporterPreferencePage_10,
				getFieldEditorParent());
		addField(ife);
		HelpSystem.setHelp(ife.getTextControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + ife.getPreferenceName());

		NStringFieldEditor sfe = new NStringFieldEditor(TextExporterConfiguration.PROPERTY_LINE_SEPARATOR,
				Messages.TextExporterPreferencePage_11, 4, getFieldEditorParent());
		addField(sfe);

		TextFieldEditor te = new TextFieldEditor(TextExporterConfiguration.PROPERTY_PAGE_SEPARATOR,
				Messages.TextExporterPreferencePage_12, true, getFieldEditorParent());
		addField(te);

	}

	public static void getDefaults(IPreferenceStore store) {
		if (!store.contains(TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT)) {
			// we can't store null values in the store, but for this one we have null
			// a workaround is to remove the property for null values
			// so we initialise the default only if no properties are initialised
			store.setDefault(TextExporterConfiguration.PROPERTY_PAGE_SEPARATOR,
					Misc.nvl(PropertiesHelper.DPROP.getProperty(TextExporterConfiguration.PROPERTY_PAGE_SEPARATOR), "")); //$NON-NLS-1$
		}

		store.setDefault(TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(TextReportConfiguration.PROPERTY_CHARACTER_HEIGHT), "0")); //$NON-NLS-1$
		store.setDefault(TextReportConfiguration.PROPERTY_CHARACTER_WIDTH,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(TextReportConfiguration.PROPERTY_CHARACTER_WIDTH), "0")); //$NON-NLS-1$
		store.setDefault(TextReportConfiguration.PROPERTY_PAGE_HEIGHT,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(TextReportConfiguration.PROPERTY_PAGE_HEIGHT), "0")); //$NON-NLS-1$
		store.setDefault(TextReportConfiguration.PROPERTY_PAGE_WIDTH,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(TextReportConfiguration.PROPERTY_PAGE_WIDTH), "0")); //$NON-NLS-1$

		store.setDefault(TextExporterConfiguration.PROPERTY_LINE_SEPARATOR, "\n"); //$NON-NLS-1$ 
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
		return "com.jaspersoft.studio.preferences.exporter.TextExporterPreferencePage.property"; //$NON-NLS-1$
	}

}
