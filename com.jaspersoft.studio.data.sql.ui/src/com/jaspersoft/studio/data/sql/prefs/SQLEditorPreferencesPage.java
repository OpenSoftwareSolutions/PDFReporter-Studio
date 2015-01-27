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
package com.jaspersoft.studio.data.sql.prefs;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;

public class SQLEditorPreferencesPage extends FieldEditorOverlayPage {
	public static final String P_USE_JDBC_QUOTE = "com.jaspersoft.studio.data.sql.prefs.USEJDBCQUOTE"; //$NON-NLS-1$
	public static final String P_IDENTIFIER_QUOTE = "com.jaspersoft.studio.data.sql.prefs.IDENTIFIER_QUOTE"; //$NON-NLS-1$
	public static final String P_IDENTIFIER_QUOTEONLYEXCEPTIONS = "com.jaspersoft.studio.data.sql.prefs.QUOTE_ONLY_EXCEPTIONS"; //$NON-NLS-1$

	public SQLEditorPreferencesPage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.SQLEditorPreferencesPage_dialogTitle);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(P_IDENTIFIER_QUOTEONLYEXCEPTIONS, Messages.SQLEditorPreferencesPage_0, getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_USE_JDBC_QUOTE, Messages.SQLEditorPreferencesPage_1, getFieldEditorParent()));
		addField(new ComboFieldEditor(P_IDENTIFIER_QUOTE, Messages.SQLEditorPreferencesPage_comboLabel,
				new String[][] { { "id", "" }, { "\"id\"", "\"" }, { "`id`", "`" }, { "[id]", "[" } }, getFieldEditorParent()));  //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(P_IDENTIFIER_QUOTE, ""); //$NON-NLS-1$
		store.setDefault(P_USE_JDBC_QUOTE, true); //$NON-NLS-1$
		store.setDefault(P_IDENTIFIER_QUOTEONLYEXCEPTIONS, true); //$NON-NLS-1$
	}

	public static final String PAGE_ID = "com.jaspersoft.studio.data.sql.prefs.SQLEditorPreferencesPage.property"; //$NON-NLS-1$

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}
}
