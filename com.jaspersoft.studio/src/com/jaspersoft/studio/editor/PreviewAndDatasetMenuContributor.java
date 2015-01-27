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
package com.jaspersoft.studio.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;

import com.jaspersoft.studio.editor.action.PreviewFormatDropDownAction;
import com.jaspersoft.studio.editor.action.SelectDataAdapterAction;
import com.jaspersoft.studio.editor.defaults.HandleDefaultsAction;
import com.jaspersoft.studio.editor.gef.ui.actions.IEditorSettingsMenuContributor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Contributor for the quick actions on the report editor, to give to the user
 * the possibility to select the preview format and the main data adapter quickly
 * 
 * @author Orlandin Marco
 *
 */
public class PreviewAndDatasetMenuContributor implements IEditorSettingsMenuContributor {

	private static List<String> actions = new ArrayList<String>();
	
	static {
		actions.add(SelectDataAdapterAction.ID);
		actions.add(PreviewFormatDropDownAction.ID);
		actions.add(HandleDefaultsAction.ID);
	}
	
	@Override
	public void registerActions(ActionRegistry actionRegistry, JasperReportsConfiguration jConfig) {
		actionRegistry.registerAction(new PreviewFormatDropDownAction(jConfig));
		actionRegistry.registerAction(new SelectDataAdapterAction(jConfig));
		actionRegistry.registerAction(new HandleDefaultsAction());
	}

	@Override
	public List<String> getActionIds() {
		return actions;
	}
}
