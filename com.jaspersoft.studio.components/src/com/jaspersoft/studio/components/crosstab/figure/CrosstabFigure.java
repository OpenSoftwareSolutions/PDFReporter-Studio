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
package com.jaspersoft.studio.components.crosstab.figure;

import java.awt.Graphics2D;

import net.sf.jasperreports.crosstabs.JRCrosstab;
import net.sf.jasperreports.engine.JRElement;

import org.eclipse.draw2d.XYLayout;

import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.editor.gef.figures.FrameFigure;
import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;

public class CrosstabFigure extends FrameFigure {
	
	/**
	 * Instantiates a new text field figure.
	 */
	public CrosstabFigure(MCrosstab crosstabModel) {
		super(crosstabModel);
		setLayoutManager(new XYLayout());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.editor.gef.figures.ComponentFigure#draw(net.sf.
	 * jasperreports.engine.export.draw.DrawVisitor,
	 * net.sf.jasperreports.engine.JRElement)
	 */
	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (model != null){
			if (cachedGraphics == null || model.hasChangedProperty()){
				Graphics2D oldGraphics = drawVisitor.getGraphics2d();
				cachedGraphics = new StackGraphics2D(oldGraphics);
				drawVisitor.setGraphics2D(cachedGraphics);
				drawVisitor.visitCrosstab((JRCrosstab) jrElement);
				drawVisitor.setGraphics2D(oldGraphics);
				model.setChangedProperty(false);
			}
			cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
			cachedGraphics.paintStack();
		}
	}

}
