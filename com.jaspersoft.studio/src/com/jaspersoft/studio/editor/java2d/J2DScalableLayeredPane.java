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
package com.jaspersoft.studio.editor.java2d;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScalableLayeredPane;
/*/*
 * The Class J2DScalableLayeredPane.
 */
public class J2DScalableLayeredPane extends ScalableLayeredPane {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.ScalableLayeredPane#paintClientArea(org.eclipse.draw2d.Graphics)
	 */
	protected final void paintClientArea(Graphics paramGraphics) {
		if (getChildren().isEmpty())
			return;
		double d = getScale();
		if (Double.compare(d, 1.0) == 0) {
			super.paintClientArea(paramGraphics);
			return;
		}
		J2DGraphics g2 = (J2DGraphics) paramGraphics;

		int i = ((getBorder() == null) || (getBorder().isOpaque())) ? 1 : 0;
		if (i == 0) {
			g2.clipRect(getBounds().getCropped(getInsets()));
		}

		g2.scale(d);
		g2.pushState();
		paintChildren(g2);
		g2.popState();
		g2.scale(1.0D / d);
	}
}
