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

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpSystem;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.StudioPreferencePage;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;
import com.jaspersoft.studio.utils.Misc;

/*
 * 
 */
public class XMLExporterPreferencePage extends FieldEditorOverlayPage {
	public static final String NSF_EXPORT_XML_VALIDATION = "net.sf.jasperreports.export.xml.validation"; //$NON-NLS-1$
	public static final String NSF_EXPORT_XML_IS_EMBEDDING_IMAGES = "net.sf.jasperreports.export.xml.is.embedding.images"; //$NON-NLS-1$
	public static final String NSF_EXPORT_XML_DTD_LOCATION = "net.sf.jasperreports.export.xml.dtd.location"; //$NON-NLS-1$

	public XMLExporterPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.XMLExporterPreferencePage_3);
	}

	/**
	 *
	 */
	public void createFieldEditors() {
		BooleanFieldEditor bf = new BooleanFieldEditor(NSF_EXPORT_XML_IS_EMBEDDING_IMAGES,
				Messages.XMLExporterPreferencePage_4, getFieldEditorParent());
		addField(bf);

		bf = new BooleanFieldEditor(NSF_EXPORT_XML_VALIDATION, Messages.XMLExporterPreferencePage_5, getFieldEditorParent());
		addField(bf);
		HelpSystem.setHelp(bf.getDescriptionControl(getFieldEditorParent()),
				StudioPreferencePage.REFERENCE_PREFIX + bf.getPreferenceName());

		FileFieldEditor ffe = new FileFieldEditor(NSF_EXPORT_XML_DTD_LOCATION, Messages.XMLExporterPreferencePage_6,
				getFieldEditorParent());
		ffe.setFileExtensions(new String[] { ".dtd" }); //$NON-NLS-1$
		addField(ffe);
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(NSF_EXPORT_XML_VALIDATION, PropertiesHelper.DPROP.getProperty(NSF_EXPORT_XML_VALIDATION));
		store.setDefault(NSF_EXPORT_XML_IS_EMBEDDING_IMAGES,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_XML_IS_EMBEDDING_IMAGES), "false")); //$NON-NLS-1$
		store.setDefault(NSF_EXPORT_XML_DTD_LOCATION,
				Misc.nvl(PropertiesHelper.DPROP.getProperty(NSF_EXPORT_XML_DTD_LOCATION), "")); //$NON-NLS-1$
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
		return "com.jaspersoft.studio.preferences.exporter.XMLExporterPreferencePage.property"; //$NON-NLS-1$
	}

}
