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
package com.jaspersoft.studio.editor.gef.figures.borders;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

public class TBLineBorder extends LineBorder {
	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle r = getPaintRectangle(figure, insets);

		graphics.setLineWidth(getWidth());
		graphics.setLineStyle(getStyle());
		if (getColor() != null)
			graphics.setForegroundColor(getColor());
		int y1 = r.y + getWidth() / 2;
		int x2 = r.x + r.width;
		graphics.drawLine(r.x, y1, x2, y1);
		y1 = r.y + r.height - getWidth() / 2;
		graphics.drawLine(r.x, y1, x2, y1);

		graphics.setLineWidth(1);
		graphics.drawLine(r.x - 10, r.y, r.x - 10, r.y + r.height);
		graphics.drawLine(r.x + r.width, r.y, r.x + r.width, r.y + r.height);
	}

	public TBLineBorder() {
		super();
	}

	public TBLineBorder(Color color, int width, int style) {
		super(color, width, style);
	}

	public TBLineBorder(Color color, int width) {
		super(color, width);
	}

	public TBLineBorder(Color color) {
		super(color);
	}

	public TBLineBorder(int width) {
		super(width);
	}

	@Override
	public Insets getInsets(IFigure figure) {
		return new Insets(getWidth(), 0, getWidth(), 0);
	}
}
