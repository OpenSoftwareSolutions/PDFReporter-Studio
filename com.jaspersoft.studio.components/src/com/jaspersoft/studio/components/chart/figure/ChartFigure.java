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
package com.jaspersoft.studio.components.chart.figure;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRChart;
import net.sf.jasperreports.engine.JRElement;

import com.jaspersoft.studio.components.chart.model.MChart;
import com.jaspersoft.studio.editor.gef.figures.FrameFigure;
import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
/*
 * The Class ChartFigure.
 */
public class ChartFigure extends FrameFigure {


	private MChart chartModel = null;
	
	private StackGraphics2D cachedGraphics = null;
	
	/**
	 * Instantiates a new static text figure.
	 */
	public ChartFigure(MChart model) {
		super();
		this.chartModel = model;
	}
	
	protected void fallbackDraw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		drawVisitor.visitChart((JRChart) jrElement);
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
		if (chartModel != null){
			if (cachedGraphics == null || chartModel.hasChangedProperty()){
				Graphics2D oldGraphics = drawVisitor.getGraphics2d();
				cachedGraphics = new StackGraphics2D(oldGraphics);
				drawVisitor.setGraphics2D(cachedGraphics);
				drawVisitor.visitChart((JRChart) jrElement);
				drawVisitor.setGraphics2D(oldGraphics);
				chartModel.setChangedProperty(false);
			}
			cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
			cachedGraphics.paintStack();
		} else {
			fallbackDraw(drawVisitor, jrElement);
		}
	}

}
