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
package com.jaspersoft.studio.editor.gef.util;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;

public class GEFUtil {
	public static double getZoom(Rectangle hbounds, IFigure figure) {
		double zoom = hbounds.x;
		Rectangle copy = hbounds.getCopy();
		figure.translateToAbsolute(copy);
		zoom = copy.x / zoom;
		return zoom;
	}

	public static double getZoom(GraphicalEditPart editPart) {
		ZoomManager zm = (ZoomManager) editPart.getViewer().getProperty(ZoomManager.class.toString());
		return zm.getZoom();
	}
}
