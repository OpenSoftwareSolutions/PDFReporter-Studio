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

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.osgi.service.prefs.Preferences;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;

/**
 * Expression editor preference page.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class ExpressionEditorPreferencePage extends FieldEditorOverlayPage {

	public static final String P_USER_DEFINED_EXPRESSIONS = "userDefinedExpressions";//$NON-NLS-1$
	public static final String P_INCLUDE_FUCTIONS_LIBRARY_IMPORTS = "includeFunctionsLibraryImports";//$NON-NLS-1$

	public ExpressionEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.ExpressionEditorPreferencePage_subtitle);
	}

	@Override
	protected void createFieldEditors() {
		addField(new ExpressionListFieldEditor(P_USER_DEFINED_EXPRESSIONS, Messages.ExpressionEditorPreferencePage_userDefinedExpressions,
				getFieldEditorParent()));
		addField(new BooleanFieldEditor(P_INCLUDE_FUCTIONS_LIBRARY_IMPORTS,
				Messages.ExpressionEditorPreferencePage_includeStatic, getFieldEditorParent()));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {

	}

	/**
	 * @return the list of expressions defined by the user in the preferences
	 */
	public static List<String> getUserDefinedExpressionList() {
		Preferences preferences = PropertiesHelper.INSTANCE_SCOPE.getNode(JaspersoftStudioPlugin.getUniqueIdentifier());
		String expressionListStr = preferences.get(P_USER_DEFINED_EXPRESSIONS, null);
		ArrayList<String> v = new ArrayList<String>();
		if (expressionListStr != null) {
			StringTokenizer st = new StringTokenizer(expressionListStr, ExpressionListFieldEditor.EXPRESSION_SEP);
			while (st.hasMoreElements()) {
				v.add((String) st.nextElement());
			}
		}
		return v;
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(ExpressionEditorPreferencePage.P_INCLUDE_FUCTIONS_LIBRARY_IMPORTS, new Boolean(false)); //$//$NON-NLS-1$
	}

	public static final String PAGE_ID = "com.jaspersoft.studio.preferences.ExpressionEditorPreferencePage.property"; //$NON-NLS-1$

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}
}
