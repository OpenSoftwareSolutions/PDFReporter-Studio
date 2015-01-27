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

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRLineBox;

import org.eclipse.draw2d.LineBorder;

import com.jaspersoft.studio.editor.gef.figures.FrameFigure;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;

public class CellFigure extends FrameFigure {
	private DesignCell cell;
	private StandardBaseColumn column;

	public CellFigure() {
		super();
		setBorder(new LineBorder(1));
	}

	public void setJRElement(DesignCell cell, StandardBaseColumn column, JSSDrawVisitor drawVisitor) {
		this.cell = cell;
		this.column = column;
		super.setJRElement(null, drawVisitor);
		setSize(getElementWidth() + 3, getElementHeight() + 3);
	}

	@Override
	protected JRLineBox getLineBox() {
		JRLineBox box = null;
		box = cell.getLineBox();
		if (box == null && cell.getStyle() != null)
			box = cell.getStyle().getLineBox();

		return box;
	}

	@Override
	protected int getElementHeight() {
		return cell.getHeight();
	}

	@Override
	protected int getElementWidth() {
		return column.getWidth();
	}

	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {

	}
}
