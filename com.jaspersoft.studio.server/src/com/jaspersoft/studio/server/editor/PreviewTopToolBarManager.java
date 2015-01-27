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
package com.jaspersoft.studio.server.editor;

import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.toolbar.ATopToolBarManager;
import com.jaspersoft.studio.server.editor.action.RunStopAction;

public class PreviewTopToolBarManager extends ATopToolBarManager {

	public PreviewTopToolBarManager(ReportUnitEditor container, Composite parent) {
		super(container, parent);
	}

	private RunStopAction vexecAction;

	protected void fillToolbar(IToolBarManager tbManager) {
		ReportUnitEditor pvcont = (ReportUnitEditor) container;

		if (vexecAction == null)
			vexecAction = new RunStopAction(pvcont);
		tbManager.add(vexecAction);

	}

}
