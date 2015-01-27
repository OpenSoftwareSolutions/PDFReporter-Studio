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
package com.jaspersoft.studio.editor.gef.ui.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.ui.actions.ActionRegistry;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class EditorSettingsContributorManager {
	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor(
				JaspersoftStudioPlugin.PLUGIN_ID, "EditorSettingsMenu"); //$NON-NLS-1$  
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IEditorSettingsMenuContributor)
					nodeFactory.add((IEditorSettingsMenuContributor) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	private List<IEditorSettingsMenuContributor> nodeFactory = new ArrayList<IEditorSettingsMenuContributor>();

	public void registerActions(ActionRegistry registry, JasperReportsConfiguration jConfig) {
		for (IEditorSettingsMenuContributor f : nodeFactory)
			f.registerActions(registry, jConfig);
	}

	public List<String> getActionIDs() {
		List<String> ids = new ArrayList<String>();
		for (IEditorSettingsMenuContributor f : nodeFactory)
			ids.addAll(f.getActionIds());
		return ids;
	}
}
