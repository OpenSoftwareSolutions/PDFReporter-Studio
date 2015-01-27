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
package com.jaspersoft.studio.data.xml;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.preferences.util.FieldEditorOverlayPage;

/**
 * XML query editor preference page.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class XMLQueryEditorPreferencePage extends FieldEditorOverlayPage {

	public static final String P_USE_RECURSIVE_RETRIEVAL = "xmlChildrenRecursiveRetrieval";//$NON-NLS-1$
	public static final String PAGE_ID = "com.jaspersoft.studio.data.preferences.XMLQueryEditorPreferencePage.property"; //$NON-NLS-1$

	public XMLQueryEditorPreferencePage() {
		super(GRID);
		setPreferenceStore(JaspersoftStudioPlugin.getInstance().getPreferenceStore());
		setDescription(Messages.XMLQueryEditorPreferencePage_Description);
	}

	@Override
	public void init(IWorkbench workbench) {

	}

	@Override
	protected String getPageId() {
		return PAGE_ID;
	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(P_USE_RECURSIVE_RETRIEVAL,
				Messages.XMLQueryEditorPreferencePage_RecursiveReadFields, getFieldEditorParent()));
	}

	public static void getDefaults(IPreferenceStore store) {
		store.setDefault(P_USE_RECURSIVE_RETRIEVAL, new Boolean(false)); //$//$NON-NLS-1$
	}
}
