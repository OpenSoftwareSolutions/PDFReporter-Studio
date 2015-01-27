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
package com.jaspersoft.studio.components.list.part;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Dimension;

import com.jaspersoft.studio.components.list.model.MList;
import com.jaspersoft.studio.editor.gef.figures.APageFigure;
import com.jaspersoft.studio.editor.gef.figures.ContainerPageFigure;
import com.jaspersoft.studio.editor.gef.parts.PageEditPart;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.property.dataset.dialog.IDatasetDialogSupport;

public class ListPageEditPart extends PageEditPart implements
		IDatasetDialogSupport {


	@Override
	protected void setupPageFigure(APageFigure figure2) {
		updateContainerSize();
		((ContainerPageFigure) figure2).setContainerSize(getContaierSize());
		setupPagePreferences(figure2);
	}

	private Dimension containerSize;

	public Dimension getContaierSize() {
		return containerSize;
	}

	private void updateContainerSize() {
		MList table = null;
		for (INode n : getPage().getChildren()) {
			if (n instanceof MList) {
				table = (MList) n;
				break;
			}
		}
		if (table != null) {
			Dimension d = table.getContainerSize();
			d.height = Math.max(d.height, (Integer) table
					.getPropertyValue(JRDesignElement.PROPERTY_HEIGHT));
			d.width = Math.max(d.width, (Integer) table
					.getPropertyValue(JRDesignElement.PROPERTY_WIDTH));
			containerSize = d;
		} else
			containerSize = null;
	}
}
