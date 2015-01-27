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

import com.jaspersoft.studio.editor.action.snap.ACheckResourcePrefAction;
import com.jaspersoft.studio.server.editor.JRSEditorContributor;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ShowPublishDialogAction extends ACheckResourcePrefAction {
	public static final String ID = JRSEditorContributor.KEY_PUBLISH2JSS_SILENT;

	public ShowPublishDialogAction(JasperReportsConfiguration jrConfig) {
		super(Messages.ShowPublishDialogAction_1, jrConfig);
		setText(Messages.ShowPublishDialogAction_2);
		setToolTipText(Messages.ShowPublishDialogAction_3);
		setId(ID);
	}

	@Override
	protected String getProperty() {
		return ID;
	}
}
