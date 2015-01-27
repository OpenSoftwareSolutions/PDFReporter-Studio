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
package com.jaspersoft.studio.callout;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * 
 * @author sanda zaharia
 * 
 */
public class CalloutFigure extends RectangleFigure {
	private String[] text;

	/**
	 * Instantiates a new map figure.
	 */
	public CalloutFigure() {
		super();
		setOpaque(true);
		setBackgroundColor(ColorConstants.yellow);
		setForegroundColor(ColorConstants.black);
		setBorder(new LineBorder(ColorConstants.orange, 1));
	}

	public void setText(String text) {
		if (text == null)
			text = "";
		this.text = text.split("\n");
	}

	@Override
	protected void fillShape(Graphics graphics) {
		super.fillShape(graphics);

		int h = graphics.getFontMetrics().getHeight();

		Rectangle r = getBounds();
		if (text != null) {
			graphics.setForegroundColor(getForegroundColor());
			int y = r.y + 2;
			int x = r.x + 2;
			for (String txt : text) {
				graphics.drawText(txt, x, y);
				y += h + 3;
			}
		}
	}
}
