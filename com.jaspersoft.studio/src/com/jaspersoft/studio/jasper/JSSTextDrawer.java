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
package com.jaspersoft.studio.jasper;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRPrintText;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.export.AwtTextRenderer;
import net.sf.jasperreports.engine.export.draw.TextDrawer;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.util.JRStyledText;

public class JSSTextDrawer extends TextDrawer {

	public JSSTextDrawer(JasperReportsContext jasperReportsContext, AwtTextRenderer textRenderer) {
		super(jasperReportsContext, textRenderer);
	}

	public void draw(Graphics2D grx, JRPrintText text, int offsetX, int offsetY) {
		textRenderer.initialize(grx, text, offsetX, offsetY);

		JRStyledText styledText = textRenderer.getStyledText();

		if (styledText == null) {
			return;
		}

		double angle = 0;

		switch (text.getRotationValue()) {
		case LEFT: {
			angle = -Math.PI / 2;
			break;
		}
		case RIGHT: {
			angle = Math.PI / 2;
			break;
		}
		case UPSIDE_DOWN: {
			angle = Math.PI;
			break;
		}
		case NONE:
		default: {
		}
		}

		grx.rotate(angle, textRenderer.getX(), textRenderer.getY());

		if (text.getModeValue() == ModeEnum.OPAQUE) {
			grx.setColor(text.getBackcolor());
			grx.fillRect(textRenderer.getX(), textRenderer.getY(), textRenderer.getWidth(), textRenderer.getHeight());
		}
		// else
		// {
		// /*
		// grx.setColor(text.getForecolor());
		// grx.setStroke(new BasicStroke(1));
		// grx.drawRect(x, y, width, height);
		// */
		// }

		String allText = textRenderer.getPlainText();
		if (allText.length() > 0) {
			grx.setColor(text.getForecolor());

			/*   */
			textRenderer.render();
		}

		grx.rotate(-angle, textRenderer.getX(), textRenderer.getY());

		/*   */
		drawBox(grx, text.getLineBox(), text, offsetX, offsetY);
	}

}
