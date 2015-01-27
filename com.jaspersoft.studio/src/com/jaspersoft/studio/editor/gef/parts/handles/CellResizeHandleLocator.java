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
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RelativeLocator;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.HandleBounds;

public class CellResizeHandleLocator extends RelativeLocator {
	private double relativeY;
	private double relativeX;
	private int direction;

	/**
	 * Constructs a RelativeLocator with the given reference figure and relative location. The location is a constant from
	 * {@link PositionConstants} used as a convenient and readable way to set both the relativeX and relativeY values.
	 * 
	 * @param reference
	 *          the reference figure
	 * @param location
	 *          one of NORTH, NORTH_EAST, etc.
	 * @since 2.0
	 */
	public CellResizeHandleLocator(GraphicalEditPart editPart, int location) {
		setReferenceFigure(editPart.getFigure());
		switch (location & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			relativeY = 0;
			break;
		case PositionConstants.SOUTH:
			relativeY = 1.0;
			break;
		default:
			relativeY = 0.5;
		}
		switch (location & PositionConstants.EAST_WEST) {
		case PositionConstants.WEST:
			relativeX = 0;
			break;
		case PositionConstants.EAST:
			relativeX = 1.0;
			break;
		default:
			relativeX = 0.5;
		}
		this.direction = location;
	}

	@Override
	public void relocate(IFigure target) {
		IFigure reference = getReferenceFigure();
		Rectangle referenceBox = ((HandleBounds) reference).getHandleBounds();
		Rectangle targetBounds = new PrecisionRectangle(referenceBox.getResized(-1, -1));
		reference.translateToAbsolute(targetBounds);
		target.translateToRelative(targetBounds);
		targetBounds.resize(1, 1);

		int w = 2;
		int h = 2;
		switch (direction & PositionConstants.NORTH_SOUTH) {
		case PositionConstants.NORTH:
			w = targetBounds.width;
			targetBounds.y += (int) (targetBounds.height * relativeY - ((h / 2))) + 1;
			break;
		case PositionConstants.SOUTH:
			w = targetBounds.width;
			targetBounds.y += (int) (targetBounds.height * relativeY - (h / 2)) - 1;
			targetBounds.x += -1;
			break;
		}
		switch (direction & PositionConstants.EAST_WEST) {
		case PositionConstants.WEST:
			h = targetBounds.height;
			targetBounds.y += (int) relativeY - 1;
			targetBounds.x += (int) (targetBounds.width * relativeX - (w / 2)) - 1;
			break;
		case PositionConstants.EAST:
			h = targetBounds.height;
			targetBounds.y += (int) relativeY - 1;
			targetBounds.x += (int) (targetBounds.width * relativeX - (w / 2) - 1);
			break;
		}

		targetBounds.setSize(w, h);
		target.setBounds(targetBounds);
	}
}
