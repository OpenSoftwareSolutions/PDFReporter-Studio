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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Color;

public class Line1Border extends LineBorder {
	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		org.eclipse.swt.graphics.Color c = getColor();
		setColor(ColorConstants.white);
		super.paint(figure, graphics, insets);
		setColor(c);
		setWidth(getWidth() - 1);
		super.paint(figure, graphics, insets);
		setWidth(getWidth() + 1);
	}

	public Line1Border() {
		super();
	}

	public Line1Border(Color color, int width, int style) {
		super(color, width, style);
	}

	public Line1Border(Color color, int width) {
		super(color, width);
	}

	public Line1Border(Color color) {
		super(color);
	}

	public Line1Border(int width) {
		super(width);
	}
}
