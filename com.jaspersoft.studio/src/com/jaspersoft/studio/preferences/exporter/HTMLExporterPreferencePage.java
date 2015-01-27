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

import net.sf.jasperreports.export.HtmlExporterConfiguration;
import net.sf.jasperreports.export.HtmlReportConfiguration;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.editor.JSSComboFieldEditor;
import com.jaspersoft.studio.preferences.editor.text.TextFieldEditor;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.Misc;

/*
 * 
 */
public class HTMLExporterPreferencePage extends FieldEditorOverlayPage {
	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.exporter.HTMLExporterPreferencePage.property";

	public HTMLExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.HTMLExporterPreferencePage_14);
	}

	/**
	 *
	 */
	public void createFieldEditors() {

		CTabFolder tabFolder = new CTabFolder(getFieldEditorParent(), SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTabPageHTML(tabFolder);
		createTabPageHB(tabFolder);
		createTabPageBP(tabFolder);

		tabFolder.setSelection(0);
	}

	private void createTabPageHTML(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_15);

		Composite sc = new Composite(tabFolder, SWT.NONE);

		JSSComboFieldEditor cfe = new JSSComboFieldEditor(HtmlReportConfiguration.PROPERTY_SIZE_UNIT,
				Messages.HTMLExporterPreferencePage_16, new String[][] {
						{ Messages.HTMLExporterPreferencePage_17, Messages.HTMLExporterPreferencePage_18 },
						{ Messages.HTMLExporterPreferencePage_19, Messages.HTMLExporterPreferencePage_20 } }, sc);
		addField(cfe);
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		BooleanFieldEditor bf = new BooleanFieldEditor(HtmlReportConfiguration.PROPERTY_BORDER_COLLAPSE,
				Messages.HTMLExporterPreferencePage_21, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(HtmlExporterConfiguration.PROPERTY_FLUSH_OUTPUT,
				Messages.HTMLExporterPreferencePage_22, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(HtmlReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS,
				Messages.HTMLExporterPreferencePage_24, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(HtmlReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND,
				Messages.HTMLExporterPreferencePage_26, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(HtmlReportConfiguration.PROPERTY_WRAP_BREAK_WORD,
				Messages.HTMLExporterPreferencePage_27, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		ptab.setControl(sc);
	}

	private void createTabPageHB(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_28);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		sc.setLayout(new GridLayout());

		TextFieldEditor se = new TextFieldEditor(HtmlExporterConfiguration.PROPERTY_HTML_HEADER,
				Messages.HTMLExporterPreferencePage_29, sc);
		se.getTextControl(sc).setLayoutData(new GridData(GridData.FILL_BOTH));
		addField(se);

		TextFieldEditor scf = new TextFieldEditor(HtmlExporterConfiguration.PROPERTY_HTML_FOOTER,
				Messages.HTMLExporterPreferencePage_30, sc);
		scf.getTextControl(sc).setLayoutData(new GridData(GridData.FILL_BOTH));
		addField(scf);

		ptab.setControl(sc);
	}

	private void createTabPageBP(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.HTMLExporterPreferencePage_31);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		sc.setLayout(new GridLayout());

		TextFieldEditor scf = new TextFieldEditor(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML,
				Messages.HTMLExporterPreferencePage_32, sc);
		scf.getTextControl(sc).setLayoutData(new GridData(GridData.FILL_BOTH | GridData.GRAB_HORIZONTAL));
		addField(scf);

		ptab.setControl(sc);
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(HtmlReportConfiguration.PROPERTY_ACCESSIBLE,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(HtmlReportConfiguration.PROPERTY_ACCESSIBLE), "false")); //$NON-NLS-1$
		store.setDefault(HtmlExporterConfiguration.PROPERTY_FLUSH_OUTPUT,
				PropertiesHelper.DPROP.getProperty(HtmlExporterConfiguration.PROPERTY_FLUSH_OUTPUT));

		store.setDefault(HtmlReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(HtmlReportConfiguration.PROPERTY_REMOVE_EMPTY_SPACE_BETWEEN_ROWS), "false")); //$NON-NLS-1$
		store.setDefault(HtmlReportConfiguration.PROPERTY_SIZE_UNIT,
				PropertiesHelper.DPROP.getProperty(HtmlReportConfiguration.PROPERTY_SIZE_UNIT));

		store.setDefault(HtmlReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND,
				PropertiesHelper.DPROP.getProperty(HtmlReportConfiguration.PROPERTY_WHITE_PAGE_BACKGROUND));
		store.setDefault(HtmlReportConfiguration.PROPERTY_WRAP_BREAK_WORD,
				PropertiesHelper.DPROP.getProperty(HtmlReportConfiguration.PROPERTY_WRAP_BREAK_WORD));

		store.setDefault(HtmlExporterConfiguration.PROPERTY_HTML_HEADER,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(HtmlExporterConfiguration.PROPERTY_HTML_HEADER), "")); //$NON-NLS-1$
		store.setDefault(HtmlExporterConfiguration.PROPERTY_HTML_FOOTER,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(HtmlExporterConfiguration.PROPERTY_HTML_FOOTER), "")); //$NON-NLS-1$
		store.setDefault(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(HtmlExporterConfiguration.PROPERTY_BETWEEN_PAGES_HTML), "")); //$NON-NLS-1$
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
		return PAGE_ID;
	}

}
