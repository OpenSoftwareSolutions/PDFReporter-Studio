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
package com.jaspersoft.studio.editor.gef.parts.band;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RelativeLocator;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;

public class BandResizeHandleLocator extends RelativeLocator {
	private double relativeY;

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
	public BandResizeHandleLocator(IFigure reference, int location) {
		setReferenceFigure(reference);
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
	}

	@Override
	public void relocate(IFigure target) {
		IFigure reference = getReferenceFigure();
		Rectangle targetBounds = new PrecisionRectangle(getReferenceBox().getResized(-1, -1));
		reference.translateToAbsolute(targetBounds);
		target.translateToRelative(targetBounds);
		targetBounds.resize(1, 1);

		Dimension targetSize = target.getPreferredSize();

		targetBounds.x += 7;

		targetBounds.y += (int) (targetBounds.height * relativeY - ((targetSize.height + 1) / 2));
		targetBounds.setSize(targetBounds.width - 14, 7);
		target.setBounds(targetBounds);
	}
}
