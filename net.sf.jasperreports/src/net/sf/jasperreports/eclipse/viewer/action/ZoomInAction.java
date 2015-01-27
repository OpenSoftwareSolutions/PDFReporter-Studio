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

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.viewer.IReportViewer;

public class ZoomInAction extends AReportAction {

	public static final String ID = "PreviewZoomInAction";
	
	public ZoomInAction(IReportViewer viewer) {
		super(viewer);
		setId(ID);
		setText("Zoom In"); //$NON-NLS-1$
		setToolTipText("Zoom in"); //$NON-NLS-1$
		setImageDescriptor(JasperReportsPlugin.getDefault().getImageDescriptor("icons/zoomin-16.png"));
		setDisabledImageDescriptor(JasperReportsPlugin.getDefault().getImageDescriptor("icons/zoomin-16.png"));
	}

	@Override
	public void run() {
		rviewer.zoomIn();
	}

	public boolean isActionEnabled() {
		return rviewer.canZoomIn();
	}
}
