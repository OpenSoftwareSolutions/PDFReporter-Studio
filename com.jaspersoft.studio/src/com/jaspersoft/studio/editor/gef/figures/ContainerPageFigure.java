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
package com.jaspersoft.studio.editor.gef.figures;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.parts.PageEditPart;

public class ContainerPageFigure extends APageFigure {
	private Dimension containerSize;

	public ContainerPageFigure(boolean viewMargins, PageEditPart page) {
		super(viewMargins, page);
	}

	public void setContainerSize(Dimension d) {
		this.containerSize = d;
		setSize(d);
	}

	@Override
	public void paintFigure(Graphics g) {
		if (viewMargins) {
			Rectangle clientArea = getClientArea();
			clientArea.x -= dx;
			clientArea.y -= dy;
			Rectangle rectangle = new Rectangle(clientArea.x, clientArea.y, containerSize.width, containerSize.height);
			g.setBackgroundColor(pageBackground);
			g.fillRectangle(rectangle);

			paintGrid(g, rectangle);
		}
		if (getBorder() != null)
			getBorder().paint(this, g, NO_INSETS);
	}

	@Override
	public Rectangle getHandleBounds() {
		Rectangle clientArea = getClientArea();
		clientArea.x -= dx;
		clientArea.y -= dy;
		Insets insets = getInsets();
		return new Rectangle(clientArea.x - insets.right, clientArea.y - insets.top, containerSize.width + insets.left
				+ insets.right, containerSize.height + insets.top + insets.bottom);
	}

	
	/**
	 * Override the original paintChildren to avoid to pain elements 
	 * that are marked as not visible inside the model
	 */
	protected void paintChildren(Graphics graphics) {
		for (int i = 0; i < getChildren().size(); i++) {
			IFigure child = (IFigure) getChildren().get(i);
			if (child.isVisible() && isFigurevisible(child)) {
				// determine clipping areas for child
				Rectangle[] clipping = null;
				if (getClippingStrategy() != null) {
					clipping = getClippingStrategy().getClip(child);
				} else {
					// default clipping behavior is to clip at bounds
					clipping = new Rectangle[] { child.getBounds() };
				}
				// child may now paint inside the clipping areas
				for (int j = 0; j < clipping.length; j++) {
					if (clipping[j].intersects(graphics.getClip(Rectangle.SINGLETON))) {
						graphics.clipRect(clipping[j]);
						child.paint(graphics);
						graphics.restoreState();
					}
				}
			}
		}
	}
}
