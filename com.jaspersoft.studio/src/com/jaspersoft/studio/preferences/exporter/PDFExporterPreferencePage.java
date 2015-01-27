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

import net.sf.jasperreports.export.PdfExporterConfiguration;
import net.sf.jasperreports.export.PdfReportConfiguration;
import net.sf.jasperreports.export.type.PdfaConformanceEnum;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PathEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.editor.JSSComboFieldEditor;
import com.jaspersoft.studio.preferences.editor.PDFPermissionFieldEditor;
import com.jaspersoft.studio.preferences.editor.text.TextFieldEditor;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * 
 */
public class PDFExporterPreferencePage extends FieldEditorOverlayPage {
	public static final String NSF_EXPORT_PDF_COLLAPSE_MISSING_BOOKMARK_LEVELS = "net.sf.jasperreports.export.pdf.collapse.missing.bookmark.levels"; //$NON-NLS-1$

	// fonts
	public static final String NSF_EXPORT_PDF_EMBEDDED = "net.sf.jasperreports.default.pdf.embedded"; //$NON-NLS-1$
	public static final String NSF_EXPORT_PDF_ENCODING = "net.sf.jasperreports.default.pdf.encoding"; //$NON-NLS-1$
	public static final String NSF_EXPORT_PDF_FONT_NAME = "net.sf.jasperreports.default.pdf.font.name"; //$NON-NLS-1$
	public static final String NSF_EXPORT_PDF_FONTDIR = "net.sf.jasperreports.export.pdf.fontdir"; //$NON-NLS-1$

	public PDFExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.PDFExporterPreferencePage_24);
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(PdfExporterConfiguration.PROPERTY_PDF_VERSION, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_PDF_VERSION),
				Messages.PDFExporterPreferencePage_25));
		store.setDefault(PdfExporterConfiguration.PROPERTY_COMPRESSED,
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_COMPRESSED));
		store.setDefault(PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS,
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS));
		store.setDefault(PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT),
				Messages.PDFExporterPreferencePage_26));
		store.setDefault(PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES,
				PropertiesHelper.DPROP.getProperty(PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES));
		store.setDefault(PdfExporterConfiguration.PROPERTY_PRINT_SCALING,
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_PRINT_SCALING));
		store.setDefault(PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE),
				Messages.PDFExporterPreferencePage_27));
		store.setDefault(NSF_EXPORT_PDF_COLLAPSE_MISSING_BOOKMARK_LEVELS,
				PropertiesHelper.DPROP.getProperty(NSF_EXPORT_PDF_COLLAPSE_MISSING_BOOKMARK_LEVELS));
		store.setDefault(PdfExporterConfiguration.PROPERTY_TAGGED, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_TAGGED),
				Messages.PDFExporterPreferencePage_28));
		// FONTS
		store.setDefault(NSF_EXPORT_PDF_EMBEDDED, PropertiesHelper.DPROP.getProperty(NSF_EXPORT_PDF_EMBEDDED));
		store.setDefault(NSF_EXPORT_PDF_ENCODING, PropertiesHelper.DPROP.getProperty(NSF_EXPORT_PDF_ENCODING));
		store.setDefault(NSF_EXPORT_PDF_FONT_NAME, PropertiesHelper.DPROP.getProperty(NSF_EXPORT_PDF_FONT_NAME));
		store.setDefault(NSF_EXPORT_PDF_FONTDIR,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_PDF_FONTDIR), Messages.PDFExporterPreferencePage_29));
		// SECURITY
		store.setDefault(PdfExporterConfiguration.PROPERTY_ENCRYPTED,
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_ENCRYPTED));
		store.setDefault(PdfExporterConfiguration.PROPERTY_128_BIT_KEY,
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_128_BIT_KEY));
		store.setDefault(PdfExporterConfiguration.PROPERTY_USER_PASSWORD, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_USER_PASSWORD),
				Messages.PDFExporterPreferencePage_30));
		store.setDefault(PdfExporterConfiguration.PROPERTY_OWNER_PASSWORD, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_OWNER_PASSWORD),
				Messages.PDFExporterPreferencePage_31));
		store.setDefault(PdfExporterConfiguration.PROPERTY_PERMISSIONS_ALLOWED, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_PERMISSIONS_ALLOWED),
				Messages.PDFExporterPreferencePage_32));
		// metadata
		store.setDefault(PdfExporterConfiguration.PROPERTY_METADATA_TITLE, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_METADATA_TITLE),
				Messages.PDFExporterPreferencePage_33));
		store.setDefault(PdfExporterConfiguration.PROPERTY_METADATA_AUTHOR, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_METADATA_AUTHOR),
				Messages.PDFExporterPreferencePage_34));
		store.setDefault(PdfExporterConfiguration.PROPERTY_METADATA_SUBJECT, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_METADATA_SUBJECT),
				Messages.PDFExporterPreferencePage_35));
		store.setDefault(PdfExporterConfiguration.PROPERTY_METADATA_KEYWORDS, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_METADATA_KEYWORDS),
				Messages.PDFExporterPreferencePage_36));
		store.setDefault(PdfExporterConfiguration.PROPERTY_METADATA_CREATOR, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_METADATA_CREATOR),
				Messages.PDFExporterPreferencePage_37));
		// PDF/A
		store.setDefault(PdfExporterConfiguration.PROPERTY_PDFA_CONFORMANCE, Misc.nvl(
				PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_PDFA_CONFORMANCE),
				PdfaConformanceEnum.NONE.getName()));
		store.setDefault(PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH), "")); //$NON-NLS-1$
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		CTabFolder tabFolder = new CTabFolder(getFieldEditorParent(), SWT.TOP);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));

		createTabCommons(tabFolder);
		createTabFonts(tabFolder);
		createTabMetadata(tabFolder);
		createTabSecurity(tabFolder);

		tabFolder.setSelection(0);
	}

	private void createTabMetadata(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.PDFExporterPreferencePage_38);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		ptab.setControl(sc);

		addField(new StringFieldEditor(PdfExporterConfiguration.PROPERTY_METADATA_TITLE,
				Messages.PDFExporterPreferencePage_39, sc));
		addField(new StringFieldEditor(PdfExporterConfiguration.PROPERTY_METADATA_AUTHOR,
				Messages.PDFExporterPreferencePage_40, sc));
		addField(new StringFieldEditor(PdfExporterConfiguration.PROPERTY_METADATA_SUBJECT,
				Messages.PDFExporterPreferencePage_41, sc));
		addField(new StringFieldEditor(PdfExporterConfiguration.PROPERTY_METADATA_KEYWORDS,
				Messages.PDFExporterPreferencePage_42, sc));
		addField(new StringFieldEditor(PdfExporterConfiguration.PROPERTY_METADATA_CREATOR,
				Messages.PDFExporterPreferencePage_43, sc));

		sc.setLayout(new GridLayout(3, false));
	}

	private void createTabSecurity(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.PDFExporterPreferencePage_44);

		Composite sc = new Composite(tabFolder, SWT.NONE);

		BooleanFieldEditor bf = new BooleanFieldEditor(PdfExporterConfiguration.PROPERTY_ENCRYPTED,
				Messages.PDFExporterPreferencePage_45, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(PdfExporterConfiguration.PROPERTY_128_BIT_KEY, Messages.PDFExporterPreferencePage_46,
				sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		StringFieldEditor se = new StringFieldEditor(PdfExporterConfiguration.PROPERTY_USER_PASSWORD,
				Messages.PDFExporterPreferencePage_47, sc);
		((Text) se.getTextControl(sc)).setEchoChar('*');
		addField(se);
		HelpSystem.setHelp(se.getTextControl(sc), StudioPreferencePage.REFERENCE_PREFIX + se.getPreferenceName());

		se = new StringFieldEditor(PdfExporterConfiguration.PROPERTY_OWNER_PASSWORD, Messages.PDFExporterPreferencePage_48,
				sc);
		((Text) se.getTextControl(sc)).setEchoChar('*');
		addField(se);
		HelpSystem.setHelp(se.getTextControl(sc), StudioPreferencePage.REFERENCE_PREFIX + se.getPreferenceName());

		addField(new PDFPermissionFieldEditor(PdfExporterConfiguration.PROPERTY_PERMISSIONS_ALLOWED,
				Messages.PDFExporterPreferencePage_49, sc));

		ptab.setControl(sc);
	}

	private void createTabFonts(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.PDFExporterPreferencePage_50);

		Composite sc = new Composite(tabFolder, SWT.NONE);
		ptab.setControl(sc);

		BooleanFieldEditor bf = new BooleanFieldEditor(NSF_EXPORT_PDF_EMBEDDED, Messages.PDFExporterPreferencePage_52, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		JSSComboFieldEditor cfe = new JSSComboFieldEditor(NSF_EXPORT_PDF_ENCODING, Messages.PDFExporterPreferencePage_51,
				ModelUtils.getPdfEncodings2(), sc);
		addField(cfe);
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		cfe = new JSSComboFieldEditor(NSF_EXPORT_PDF_FONT_NAME, Messages.PDFExporterPreferencePage_53,
				ModelUtils.getPDFFontNames2(), sc);
		addField(cfe);
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		Composite fdircompo = new Composite(sc, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 5;
		fdircompo.setLayoutData(gd);
		PathEditor pe = new PathEditor(NSF_EXPORT_PDF_FONTDIR, Messages.PDFExporterPreferencePage_54,
				Messages.PDFExporterPreferencePage_55, fdircompo);
		addField(pe);
		HelpSystem.setHelp(pe.getListControl(fdircompo), StudioPreferencePage.REFERENCE_PREFIX + pe.getPreferenceName());

		fdircompo.setLayout(new GridLayout(4, false));
	}

	private void createTabCommons(CTabFolder tabFolder) {
		CTabItem ptab = new CTabItem(tabFolder, SWT.NONE);
		ptab.setText(Messages.PDFExporterPreferencePage_56);

		Composite sc = new Composite(tabFolder, SWT.NONE);

		JSSComboFieldEditor cfe = new JSSComboFieldEditor(PdfExporterConfiguration.PROPERTY_PDF_VERSION,
				Messages.PDFExporterPreferencePage_57, new String[][] {
						{ Messages.PDFExporterPreferencePage_58, Messages.PDFExporterPreferencePage_59 },
						{ Messages.PDFExporterPreferencePage_60, Messages.PDFExporterPreferencePage_61 },
						{ Messages.PDFExporterPreferencePage_62, Messages.PDFExporterPreferencePage_63 },
						{ Messages.PDFExporterPreferencePage_64, Messages.PDFExporterPreferencePage_65 },
						{ Messages.PDFExporterPreferencePage_66, Messages.PDFExporterPreferencePage_67 },
						{ Messages.PDFExporterPreferencePage_68, Messages.PDFExporterPreferencePage_69 },
						{ Messages.PDFExporterPreferencePage_70, Messages.PDFExporterPreferencePage_71 } }, sc);
		addField(cfe);
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		cfe = new JSSComboFieldEditor(PdfExporterConfiguration.PROPERTY_PDFA_CONFORMANCE, Messages.PDFExporterPreferencePage_1,
 new String[][] { { PdfaConformanceEnum.NONE.getName(), "none" },
						{ PdfaConformanceEnum.PDFA_1A.getName(), "1A" }, { PdfaConformanceEnum.PDFA_1B.getName(), "1B" } }, sc); //$NON-NLS-1$ //$NON-NLS-2$
		addField(cfe);
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		Composite fcompo = new Composite(sc, SWT.NONE);
		fcompo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		FileFieldEditor ffe = new FileFieldEditor(PdfExporterConfiguration.PROPERTY_PDFA_ICC_PROFILE_PATH,
				Messages.PDFExporterPreferencePage_5, fcompo);
		ffe.setFileExtensions(new String[] { ".icc" }); //$NON-NLS-1$
		addField(ffe);
		HelpSystem.setHelp(ffe.getTextControl(fcompo), StudioPreferencePage.REFERENCE_PREFIX + ffe.getPreferenceName());

		BooleanFieldEditor bf = new BooleanFieldEditor(PdfExporterConfiguration.PROPERTY_COMPRESSED,
				Messages.PDFExporterPreferencePage_72, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(PdfExporterConfiguration.PROPERTY_CREATE_BATCH_MODE_BOOKMARKS,
				Messages.PDFExporterPreferencePage_73, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(NSF_EXPORT_PDF_COLLAPSE_MISSING_BOOKMARK_LEVELS, Messages.PDFExporterPreferencePage_74,
				sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(PdfReportConfiguration.PROPERTY_FORCE_SVG_SHAPES,
				Messages.PDFExporterPreferencePage_75, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		bf = new BooleanFieldEditor(PdfExporterConfiguration.PROPERTY_TAGGED, Messages.PDFExporterPreferencePage_77, sc);
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(sc), StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		cfe = new JSSComboFieldEditor(PdfExporterConfiguration.PROPERTY_PRINT_SCALING,
				Messages.PDFExporterPreferencePage_78, new String[][] { { Messages.PDFExporterPreferencePage_6, "default" }, { Messages.PDFExporterPreferencePage_8, "none" } }, sc); //$NON-NLS-2$ //$NON-NLS-4$
		addField(cfe); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
		HelpSystem.setHelp(cfe.getComboBoxControl(sc), StudioPreferencePage.REFERENCE_PREFIX + cfe.getPreferenceName());

		StringFieldEditor sfe = new StringFieldEditor(PdfExporterConfiguration.PROPERTY_TAG_LANGUAGE,
				Messages.PDFExporterPreferencePage_83, sc);
		addField(sfe);
		HelpSystem.setHelp(sfe.getTextControl(sc), StudioPreferencePage.REFERENCE_PREFIX + sfe.getPreferenceName());

		TextFieldEditor tfe = new TextFieldEditor(PdfExporterConfiguration.PROPERTY_PDF_JAVASCRIPT,
				Messages.PDFExporterPreferencePage_84, sc);
		addField(tfe);
		HelpSystem.setHelp(tfe.getTextControl(sc), StudioPreferencePage.REFERENCE_PREFIX + tfe.getPreferenceName());

		ptab.setControl(sc);
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
		return "com.jaspersoft.studio.preferences.exporter.PDFExporterPreferencePage.property"; //$NON-NLS-1$
	}

}
