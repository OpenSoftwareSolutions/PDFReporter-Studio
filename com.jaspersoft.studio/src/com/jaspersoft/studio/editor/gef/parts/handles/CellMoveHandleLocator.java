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
package com.jaspersoft.studio.editor.gef.parts.handles;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.handles.MoveHandleLocator;

import com.jaspersoft.studio.editor.gef.parts.IContainerPart;

public class CellMoveHandleLocator extends MoveHandleLocator {
	private GraphicalEditPart editPart;

	public CellMoveHandleLocator(GraphicalEditPart editPart) {
		super(editPart.getFigure());
		this.editPart = editPart;
	}

	@Override
	public void relocate(IFigure target) {
		super.relocate(target);
		if (editPart instanceof IContainerPart) {
			Rectangle b = target.getBounds();
			Dimension d = ((IContainerPart) editPart).getContaierSize();

			ZoomManager zm = (ZoomManager) editPart.getViewer().getProperty(ZoomManager.class.toString());

			double zoom = zm.getZoom();

			b.y = (int) Math.floor(CellMoveHandle.Y_OFFSET * zoom) - CellMoveHandle.Y_OFFSET - 3;
			b.x = (int) Math.floor(CellMoveHandle.Y_OFFSET * zoom) - CellMoveHandle.Y_OFFSET - 3;

			b.height = d.height;
			b.width = d.width;

			Rectangle copy = new PrecisionRectangle(b.getCopy());

			getReference().translateToAbsolute(copy);
			target.translateToRelative(copy);
			b.height = CellMoveHandle.Y_OFFSET * 2 + 6 + copy.height;
			b.width = CellMoveHandle.Y_OFFSET * 2 + 6 + copy.width;

			target.setBounds(b);
		}
	}

}
