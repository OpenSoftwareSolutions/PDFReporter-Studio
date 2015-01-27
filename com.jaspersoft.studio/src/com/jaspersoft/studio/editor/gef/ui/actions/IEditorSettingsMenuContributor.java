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

import java.util.List;

import org.eclipse.gef.ui.actions.ActionRegistry;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public interface IEditorSettingsMenuContributor {
	public void registerActions(ActionRegistry actionRegistry, JasperReportsConfiguration jConfig);

	public List<String> getActionIds();
}
