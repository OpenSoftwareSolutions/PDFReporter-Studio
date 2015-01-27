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
package com.jaspersoft.studio.server.editor.action;

import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.server.editor.ReportUnitEditor;

public class RunStopAction extends Action {
	public static final String ID = "PREVIEWJRSACTION"; //$NON-NLS-1$
	private ReportUnitEditor editor;

	public RunStopAction(ReportUnitEditor editor) {
		super();
		this.editor = editor;
		setId(ID);
		setText(Messages.RunStopAction_runreport);
		setDescription(Messages.RunStopAction_runreport_desc);
		setToolTipText(Messages.RunStopAction_runreport_desc);
		setImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/start_task.gif")); //$NON-NLS-1$
		setDisabledImageDescriptor(
				JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/eclipse/start_task.gif")); //$NON-NLS-1$
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && editor.isNotRunning();
	}

	@Override
	public void run() {
		editor.runReport();
	}
}
