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
package com.jaspersoft.studio.components.chart.model.theme.stroke;

import java.awt.BasicStroke;
import java.awt.Graphics2D;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.java2d.J2DLightweightSystem;

public class StrokeWidget extends Composite {
	private Figure parentFigure;
	private Canvas square;
	private J2DLightweightSystem lws;

	public StrokeWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		square = new Canvas(this, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);

		lws = new J2DLightweightSystem();
		lws.setControl(square);
		parentFigure = new RectangleFigure() {

			@Override
			public void paint(Graphics graphics) {
				if (stroke != null) {
					int offset = (int) stroke.getLineWidth();
					int x = getBounds().x + offset;
					int y = getBounds().y + offset;
					int w = getBounds().width - 2 * offset - 10;
					int h = getBounds().height - 2 * offset - 10;
					graphics.setForegroundColor(ColorConstants.black);
					graphics.setBackgroundColor(ColorConstants.lightGray);

					Graphics2D g = ComponentFigure.getG2D(graphics);
					if (g != null) {
						g.setStroke(stroke);
						g.drawRect(x, y, w, h);
					}
				}
			}
		};
		lws.setContents(parentFigure);
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				setTBounds();
			}
		});
	}

	public Control getCanvas() {
		return square;
	}

	private BasicStroke stroke;

	public void setStroke(BasicStroke stroke) {
		this.stroke = stroke;
	}

	public void setTBounds() {
		if (!isDisposed()) {
			Rectangle r = getBounds();
			parentFigure.setSize(r.width, r.height);
			parentFigure.invalidate();
			square.setSize(r.width, r.height);
			square.redraw();
			lws.getUpdateManager().performUpdate();
		}
	}
}
