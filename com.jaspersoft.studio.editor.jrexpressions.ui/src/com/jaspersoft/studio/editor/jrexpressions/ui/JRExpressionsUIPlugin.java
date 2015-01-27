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
package com.jaspersoft.studio.editor.jrexpressions.ui;

import org.eclipse.jface.preference.IPreferenceStore;
import org.osgi.framework.BundleContext;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.preferences.ExpressionEditorPreferencePage;


/**
 * Plug-in activator that extends that generated one {@link JRExpressionsActivator}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JRExpressionsUIPlugin extends JRExpressionsActivator {

	/** Plug-in identifier */
	public static final String PLUGIN_ID = "com.jaspersoft.studio.editor.jrexpressions.ui"; //$NON-NLS-1$

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		initDefaultPreferences();
	}

	/*
	 * Initializes preferences information.
	 */
	private void initDefaultPreferences() {
		// Override already defined default in ExpressionEditorPreferencePage class
		IPreferenceStore store=JaspersoftStudioPlugin.getInstance().getPreferenceStore();
		store.setDefault(ExpressionEditorPreferencePage.P_INCLUDE_FUCTIONS_LIBRARY_IMPORTS, true); //$//$NON-NLS-1$
	}
	
	
}
