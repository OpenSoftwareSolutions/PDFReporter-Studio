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
package com.jaspersoft.studio.data.sql.ui.gef;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;

/*
 * 
 * @author Chicu Veaceslav
 */
public class SQLDesignerRootEditPart extends ScalableFreeformRootEditPart {

	/**
	 * Instantiates a new main designer root edit part.
	 */
	public SQLDesignerRootEditPart() {
		super();
		// set zoom manager
		ZoomManager zoomManager = getZoomManager();
		zoomManager.setZoomAnimationStyle(ZoomManager.ANIMATE_ZOOM_IN_OUT);
		zoomManager.setZoomLevels(new double[] { 0.25, 0.35, 0.45, 0.5, 0.6, 0.7, 0.75, 0.8, 0.85, 0.95, 1.0, 1.1, 1.2, 1.25, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0 });
		List<String> zoomLevels = new ArrayList<String>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		zoomManager.setZoomLevelContributions(zoomLevels);
	}

	@Override
	protected GridLayer createGridLayer() {
		return new com.jaspersoft.studio.editor.gef.figures.layers.GridLayer();
	}

}
