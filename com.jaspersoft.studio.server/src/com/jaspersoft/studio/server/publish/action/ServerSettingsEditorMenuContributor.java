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
package com.jaspersoft.studio.server.publish.action;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;

import com.jaspersoft.studio.editor.gef.ui.actions.IEditorSettingsMenuContributor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ServerSettingsEditorMenuContributor implements IEditorSettingsMenuContributor {

	@Override
	public void registerActions(ActionRegistry actionRegistry, JasperReportsConfiguration jConfig) {
		actionRegistry.registerAction(new ShowPublishDialogAction(jConfig));
	}

	private static List<String> actions = new ArrayList<String>();
	static {
		actions.add(ShowPublishDialogAction.ID);
	}

	@Override
	public List<String> getActionIds() {
		return actions;
	}
}
