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
import net.sf.jasperreports.engine.JRSubreport;

import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;
import com.jaspersoft.studio.model.subreport.MSubreport;
/*
 * The Class SubreportFigure.
 * 
 * @author Chicu Veaceslav
 */
public class SubreportFigure extends FrameFigure {

	/**
	 * Instantiates a crosstab figure.
	 */
	public SubreportFigure(MSubreport model) {
		super(model);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.editor.gef.figures.ComponentFigure#draw(net.sf.jasperreports.engine.export.draw.DrawVisitor,
	 * net.sf.jasperreports.engine.JRElement)
	 */
	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (cachedGraphics == null || model.hasChangedProperty()){
			model.setChangedProperty(false);
			Graphics2D oldGraphics = drawVisitor.getGraphics2d();
			cachedGraphics = new StackGraphics2D(oldGraphics);
			drawVisitor.setGraphics2D(cachedGraphics);
			drawVisitor.visitSubreport((JRSubreport) jrElement);
			drawVisitor.setGraphics2D(oldGraphics);
		}
		cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
		cachedGraphics.paintStack();
	}
}
