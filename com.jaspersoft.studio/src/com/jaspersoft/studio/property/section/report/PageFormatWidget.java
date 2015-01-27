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
package com.jaspersoft.studio.property.section.report;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.studio.editor.gef.figures.borders.ShadowBorder;
import com.jaspersoft.studio.editor.java2d.J2DLightweightSystem;

public class PageFormatWidget extends Composite {
	private Figure parentFigure;
	private Canvas square;
	private RectangleFigure borderPreview;
	private J2DLightweightSystem lws;

	public PageFormatWidget(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout());
		square = new Canvas(this, SWT.NO_REDRAW_RESIZE | SWT.NO_BACKGROUND);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		square.setLayoutData(gd);

		lws = new J2DLightweightSystem();
		lws.setControl(square);
		parentFigure = new Figure();
		parentFigure.setLayoutManager(new XYLayout());
		lws.setContents(parentFigure);

		borderPreview = new RectangleFigure() {

			@Override
			public void paint(Graphics graphics) {
				Dimension psize = parentFigure.getSize();
				float zoom = Math.max((float) pwidth / (float) (psize.width - 20), (float) pheight
						/ (float) (psize.height - 20));

				int x = getBounds().x + 10 + Math.round(lmargin / zoom);
				int y = getBounds().y + 10 + Math.round(tmargin / zoom);
				int w = getBounds().width - 20 - Math.round((rmargin) / zoom) - Math.round(lmargin / zoom);
				int h = getBounds().height - 20 - Math.round((bmargin) / zoom) - Math.round(tmargin / zoom);
				graphics.setForegroundColor(ColorConstants.blue);
				graphics.setBackgroundColor(ColorConstants.lightGray);
				graphics.setLineWidthFloat(0.1f);
				graphics.drawRectangle(x, y, w, h);

				int sw = Math.round(space / zoom);
				w = Math.round((cwidth) / zoom);
				for (int i = 1; i < cols; i++) {
					x += w;
					graphics.drawLine(x, y, x, y + h);
					graphics.fillRectangle(x, y, sw, h);
					x += sw;
					graphics.drawLine(x, y, x, y + h);
				}
				paintBorder(graphics);
			}
		};
		borderPreview.setBorder(new ShadowBorder());
		parentFigure.add(borderPreview);
		Display.getCurrent().asyncExec(new Runnable() {

			public void run() {
				setTBounds();
			}
		});
	}

	public void setPwidth(int pwidth) {
		this.pwidth = pwidth;
	}

	public void setPheight(int pheight) {
		this.pheight = pheight;
	}

	public void setSpace(int space) {
		this.space = space;
	}

	public void setCwidth(int cwidth) {
		this.cwidth = cwidth;
	}

	public void setCols(int cols) {
		this.cols = cols;
	}

	public void setLmargin(int lmargin) {
		this.lmargin = lmargin;
	}

	public void setTmargin(int tmargin) {
		this.tmargin = tmargin;
	}

	public void setRmargin(int rmargin) {
		this.rmargin = rmargin;
	}

	public void setBmargin(int bmargin) {
		this.bmargin = bmargin;
	}

	public Control getCanvas() {
		return square;
	}

	private int pwidth;
	private int pheight;
	private int space;
	private int cwidth;
	private int cols;
	private int lmargin;
	private int tmargin;
	private int rmargin;
	private int bmargin;

	public void setTBounds() {
		if (!isDisposed()) {
			Dimension psize = parentFigure.getSize();

			float zoom = Math.max((float) pwidth / (float) (psize.width + 10), (float) pheight / (float) (psize.height + 10));

			int w = Math.max(22, Math.round(pwidth / zoom));
			int h = Math.max(22, Math.round(pheight / zoom));
			borderPreview.setSize(w, h);
			int x = psize.width / 2 - w / 2;
			int y = psize.height / 2 - h / 2;

			borderPreview.setLocation(new org.eclipse.draw2d.geometry.Point(x, y));
			parentFigure.invalidate();

			square.redraw();
			lws.getUpdateManager().performUpdate();
		}
	}

}
