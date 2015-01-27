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
package com.jaspersoft.studio.components.crosstab.model.nodata;

import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.editor.gef.figures.APageFigure;
import com.jaspersoft.studio.model.ANode;

public class MCrosstabWhenNoDataCell extends MCell {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MCrosstabWhenNoDataCell() {
		super();
	}

	public MCrosstabWhenNoDataCell(ANode parent, JRCellContents jfRield,
			int index) {
		super(parent, jfRield,
				Messages.MCrosstabWhenNoDataCell_when_no_data_cell, index);
	}

	@Override
	public Color getForeground() {
		if (getValue() == null)
			return ColorConstants.lightGray;
		return ColorConstants.black;
	}

	@Override
	public Rectangle getBounds() {
		MCrosstab mc = getMCrosstab();
		if (mc != null) {
			Dimension d = mc.getCrosstabManager().getSize();

			return new Rectangle(0, d.height + 4 * APageFigure.PAGE_BORDER.top,
					getValue().getWidth() + 2, getValue().getHeight() + 2);
		}
		return null;
	}
}
