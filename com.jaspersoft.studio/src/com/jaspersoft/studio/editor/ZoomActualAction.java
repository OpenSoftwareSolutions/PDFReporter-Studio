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

import net.sf.jasperreports.eclipse.JasperReportsPlugin;

import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ZoomInAction;

/**
 * Set the zoom to 100% on the inner zoom manager
 */
public class ZoomActualAction extends ZoomInAction {

	public static final String ID = "EditorZoomActualAction";
	
	/**
	 * Constructor for ZoomOutAction.
	 * 
	 * @param zoomManager
	 *            the zoom manager
	 */
	public ZoomActualAction(ZoomManager zoomManager) {
		super(zoomManager);
		setId(ID);
		setText("Zoom Actual");
		setImageDescriptor(JasperReportsPlugin.getDefault().getImageDescriptor("icons/zoomactual.gif"));
		setToolTipText("Set the zoom level to 100%");
	}

	/**
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run() {
		zoomManager.setZoom(1.0d);
	}

	/**
	 * @see org.eclipse.gef.editparts.ZoomListener#zoomChanged(double)
	 */
	public void zoomChanged(double zoom) {
		Double zoomLevel = 1.0d;
		setEnabled(zoomLevel.equals(zoomManager.getZoom()));
	}

}
