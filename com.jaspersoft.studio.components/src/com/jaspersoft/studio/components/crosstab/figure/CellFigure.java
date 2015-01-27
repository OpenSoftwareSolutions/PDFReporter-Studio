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

import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRLineBox;

import com.jaspersoft.studio.editor.gef.figures.FrameFigure;
import com.jaspersoft.studio.jasper.JSSDrawVisitor;

public class CellFigure extends FrameFigure {
	protected JRDesignCellContents cell;

	public CellFigure() {
		super();
	}

	public void setJRElement(JRDesignCellContents jrElement, JSSDrawVisitor drawVisitor) {
		this.cell = jrElement;
		super.setJRElement(null, drawVisitor);
		if (cell != null)
			setSize(getElementWidth(), getElementHeight());
	}

	@Override
	protected JRLineBox getLineBox() {
		JRLineBox box = null;
		if (cell != null)
			box = cell.getLineBox();
		if (box == null && cell != null && cell.getStyle() != null)
			box = cell.getStyle().getLineBox();

		return box;
	}

	@Override
	protected int getElementHeight() {
		if (cell != null)
			return cell.getHeight();
		return getSize().width;
	}

	@Override
	protected int getElementWidth() {
		if (cell != null)
			return cell.getWidth();
		return getSize().width;
	}

	@Override
	protected void draw(JSSDrawVisitor drawVisitor, JRElement jrElement) {
	}
}
