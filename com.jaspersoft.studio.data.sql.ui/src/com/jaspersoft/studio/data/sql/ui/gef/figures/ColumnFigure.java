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
package com.jaspersoft.studio.data.sql.ui.gef.figures;

import org.eclipse.draw2d.ButtonModel;
import org.eclipse.draw2d.ChangeEvent;
import org.eclipse.draw2d.ChangeListener;
import org.eclipse.draw2d.CheckBox;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.data.sql.text2model.ConvertUtil;

public class ColumnFigure extends Figure {
	private CheckBox checkbox;

	/**
	 * Constructs a CheckBox with the passed text in its label.
	 * 
	 * @param text
	 *          The label text
	 * @since 2.0
	 */
	public ColumnFigure(String text, Image image) {
		setLayoutManager(new FlowLayout(true));
		checkbox = new CheckBox();
		checkbox.addChangeListener(new ChangeListener() {

			@Override
			public void handleStateChanged(ChangeEvent event) {
				if (event.getPropertyName().equals(ButtonModel.SELECTED_PROPERTY))
					handleSelectionChanged();
			}
		});
		add(checkbox);
		Label lbl = new Label(ConvertUtil.cleanDbNameFull(text), image);
		lbl.setTextPlacement(PositionConstants.WEST);
		add(lbl);
	}

	@Override
	public Insets getInsets() {
		return SqlTableFigure.INSETS;
	}

	/**
	 * Adjusts CheckBox's icon depending on selection status.
	 * 
	 * @since 2.0
	 */
	protected void handleSelectionChanged() {

	}

	public boolean isSelected() {
		return checkbox.isSelected();
	}

	public void setSelected(boolean sel) {
		checkbox.setSelected(sel);
	}
}
