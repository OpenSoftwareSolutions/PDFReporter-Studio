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
package com.jaspersoft.studio.components.table.figure;

import java.awt.Graphics2D;

import net.sf.jasperreports.engine.JRComponentElement;
import net.sf.jasperreports.engine.JRElement;

import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.editor.gef.figures.JRComponentFigure;
import com.jaspersoft.studio.editor.java2d.StackGraphics2D;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;

public class TableFigure extends JRComponentFigure {
	
	/**
	 * Instantiates a new text field figure.
	 */
	public TableFigure(MTable tableModel) {
		super(tableModel);
	}

	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
		if (model != null){
			if (cachedGraphics == null || model.hasChangedProperty()){
				Graphics2D oldGraphics = drawVisitor.getGraphics2d();
				cachedGraphics = new StackGraphics2D(oldGraphics);
				drawVisitor.setGraphics2D(cachedGraphics);
				drawVisitor.visitComponentElement((JRComponentElement) jrElement);
				drawVisitor.setGraphics2D(oldGraphics);
				model.setChangedProperty(false);
			}
			cachedGraphics.setRealDrawer(drawVisitor.getGraphics2d());
			cachedGraphics.paintStack();
		}
	}
}
