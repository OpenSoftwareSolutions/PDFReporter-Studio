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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.editparts.LayerManager;
import org.eclipse.gef.handles.MoveHandle;

import com.jaspersoft.studio.editor.gef.util.GEFUtil;

public class CellMoveHandle extends MoveHandle {
	public static final int Y_OFFSET = 10;
	private boolean row;
	private boolean col;

	public CellMoveHandle(GraphicalEditPart owner, Locator loc) {
		super(owner, loc);

	}

	public CellMoveHandle(GraphicalEditPart owner, boolean row, boolean col) {
		this(owner, new CellMoveHandleLocator(owner));
		this.row = row;
		this.col = col;
	}

	public CellMoveHandle(GraphicalEditPart owner, boolean row, boolean col, int offset) {
		this(owner, row, col);
		this.offset = offset;
	}

	private int offset = 0;

	@Override
	protected void initialize() {
		super.initialize();
		setOpaque(true);
		setBackgroundColor(ColorConstants.lightGray);
	}

	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.setAlpha(10);
		super.paintFigure(graphics);
	}

	private static final int LINEWIDTH = 6;
	private static final int VLINEWIDTH = 4;

	@Override
	protected void paintBorder(Graphics graphics) {
		graphics.setForegroundColor(ColorConstants.darkGray);
		Rectangle r = getBounds().getCopy();
		IFigure ofig = getOwner().getFigure();

		Rectangle rown = ofig.getBounds().getCopy();
		if (offset != 0)
			rown = rown.resize(offset, offset);

		ofig.translateToAbsolute(rown);
		IFigure feedback = LayerManager.Helper.find(getOwner()).getLayer(LayerConstants.FEEDBACK_LAYER);
		feedback.translateToRelative(rown);

		int off2 = Y_OFFSET / 2;

		if (row) {
			int y2 = r.y + off2;
			int x1 = rown.x + VLINEWIDTH / 2;
			int x2 = rown.x + rown.width - VLINEWIDTH / 2 + 1;
			drawHBorder(graphics, off2, y2, rown.x + off2, rown.x + rown.width - off2 + 1, x1, x2);

			drawHBorder(graphics, off2, r.y + r.height - off2, rown.x + off2, rown.x + rown.width - off2 + 1, x1, x2);
		}
		// if (col) {
		graphics.drawLine(r.x + off2, rown.y + off2, r.x + off2, rown.y + rown.height - off2 + 1);
		graphics.drawLine(r.x + r.width - off2, rown.y + off2, r.x + r.width - off2, rown.y + rown.height - off2 + 1);
		// }
	}

	public void drawHBorder(Graphics graphics, int off2, int y, int xx1, int xx2, int x1, int x2) {
		graphics.setLineWidth(LINEWIDTH);
		graphics.drawLine(xx1, y, xx2, y);

		 int y1 = y - off2;
		 int y2 = y + off2;
		 graphics.setLineWidth(VLINEWIDTH);
		 graphics.drawLine(x1, y1, x1, y2);
		 graphics.drawLine(x2, y1, x2, y2);
	}

	public boolean containsPoint(int x, int y) {
		if (!getBounds().contains(x, y))
			return false;
		Rectangle r = getBounds().getCopy();
		IFigure ofig = getOwner().getFigure();

		Rectangle rown = ofig.getBounds().getCopy();

		ofig.translateToAbsolute(rown);

		IFigure feedback = LayerManager.Helper.find(getOwner()).getLayer(LayerConstants.FEEDBACK_LAYER);
		feedback.translateToRelative(rown);

		int offset = getOffset();

		if (row && (x >= rown.x && x <= rown.x + rown.width && (y <= offset || y >= r.y + r.height - offset)))
			return true;
		if (col && (y >= rown.y && y <= rown.y + rown.height && (x <= offset || x >= r.x + r.width - offset)))
			return true;

		return false;
	}

	public int getOffset() {
		return (int) Math.floor(Y_OFFSET * GEFUtil.getZoom(getOwner()));
	}
}
