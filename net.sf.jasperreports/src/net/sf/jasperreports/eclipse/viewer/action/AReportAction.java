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
package net.sf.jasperreports.eclipse.viewer.action;

import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.eclipse.viewer.IReportViewerListener;
import net.sf.jasperreports.eclipse.viewer.ReportViewerEvent;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.services.IDisposable;

public abstract class AReportAction extends Action implements IReportViewerListener, IDisposable {
	protected IReportViewer rviewer;

	public AReportAction(IReportViewer rviewer) {
		super();
		this.rviewer = rviewer;
		rviewer.addReportViewerListener(this);
		setEnabled(isActionEnabled());
	}

	public abstract boolean isActionEnabled();

	public void viewerStateChanged(ReportViewerEvent evt) {
		setEnabled(isActionEnabled());
	}

	public void dispose() {
		rviewer.removeReportViewerListener(this);
	}
}
