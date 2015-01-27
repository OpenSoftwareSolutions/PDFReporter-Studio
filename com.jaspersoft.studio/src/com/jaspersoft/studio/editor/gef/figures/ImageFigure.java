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

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRImage;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.JRPrintElement;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.jasper.LazyImageConverter;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.image.MImage;
/*
 * The Class ChartFigure.
 */
public class ImageFigure extends FrameFigure {

	
	/**
	 * Instantiates a new ImageFigure.
	 */
	public ImageFigure(MImage imageModel) {
		super(imageModel);
	}

	protected void visitElement(JSSDrawVisitor drawVisitor, MGraphicElement model) {
		JRElement element = model.getValue();
		JRPrintElement printElement = LazyImageConverter.getInstance().convertImage(drawVisitor.getReportConverter(), model);
		try {
			printElement.accept(drawVisitor.getDrawVisitor(), JSSDrawVisitor.elementOffset(element));
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.editor.gef.figures.GenericFigure#draw(net.sf.jasperreports.engine.export.draw.DrawVisitor,
	 * net.sf.jasperreports.engine.JRElement)
	 */
	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (cachedGraphics == null || model.hasChangedProperty()){
			Graphics2D oldGraphics = drawVisitor.getGraphics2d();
			cachedGraphics = new StackGraphics2D(oldGraphics);
			drawVisitor.setGraphics2D(cachedGraphics);
			visitElement(drawVisitor, model);
			drawVisitor.setGraphics2D(oldGraphics);
			model.setChangedProperty(false);
		} else {
			//If the image dosen't need to be reloaded than a recheck is launched to see if it was updated
			LazyImageConverter.getInstance().convertImage(drawVisitor.getReportConverter(), model);
		}
		cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
		cachedGraphics.paintStack();
	}

	protected JRPen getLinePen() {
		return ((JRImage) getJrElement()).getLinePen();
	}

	protected Rectangle calcBorder(JRLineBox jrLineBox) {
		int x = (int) Math.ceil(jrLineBox.getLeftPen().getLineWidth() / 2);
		int y = (int) Math.ceil(jrLineBox.getTopPen().getLineWidth() / 2);
		int w = (int) Math.ceil(jrLineBox.getRightPen().getLineWidth() / 2);
		int h = (int) Math.ceil(jrLineBox.getBottomPen().getLineWidth() / 2);

		if (x == 0 && y == 0 && w == 0 && h == 0) {
			JRPen jrPen = getLinePen();
			x = (int) Math.ceil(jrPen.getLineWidth() / 2);
			y = x;
			w = x;
			h = x;
		}
		return new Rectangle(x, y, w, h);
	}

}
