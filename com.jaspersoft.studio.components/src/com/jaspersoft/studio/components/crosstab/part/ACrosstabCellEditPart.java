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
package com.jaspersoft.studio.components.crosstab.part;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.part.editpolicy.CrosstabCellMoveEditPolicy;
import com.jaspersoft.studio.components.crosstab.part.editpolicy.CrosstabCellResizableEditPolicy;
import com.jaspersoft.studio.editor.gef.parts.IContainerPart;
import com.jaspersoft.studio.editor.gef.parts.APrefFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.ElementEditPolicy;
import com.jaspersoft.studio.editor.gef.rulers.ReportRuler;
import com.jaspersoft.studio.model.IContainer;

public abstract class ACrosstabCellEditPart extends APrefFigureEditPart
		implements IContainerPart, IContainer {

	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		return getParent().getAdapter(key);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ElementEditPolicy());
		installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE,
				new CrosstabCellResizableEditPolicy() {
					@Override
					protected void showSelection() {
						super.showSelection();
						updateRulers();
					}
				});
	}

	public EditPolicy getEditPolicy() {
		return new CrosstabCellMoveEditPolicy();
	}

	@Override
	protected abstract void setupFigure(IFigure rect);

	@Override
	public boolean isSelectable() {
		return true;
	}

	public Object getConstraintFor(ChangeBoundsRequest request,
			GraphicalEditPart child) {
		return new Rectangle(0, 0, request.getSizeDelta().width,
				request.getSizeDelta().height);
	}

	public static final int X_OFFSET = 10;
	public static final int Y_OFFSET = 10;

	@Override
	public void updateRulers() {
		// get mtable
		// get max size (table and tablemanager.size)
		MCrosstab table = getCrosstab();
		if (table != null) {
			Dimension d = table.getCrosstabManager().getSize();

			int dh = Math.max(d.height, (Integer) table
					.getPropertyValue(JRDesignElement.PROPERTY_HEIGHT));
			int dw = Math.max(d.width, (Integer) table
					.getPropertyValue(JRDesignElement.PROPERTY_WIDTH));

			getViewer().setProperty(ReportRuler.PROPERTY_HOFFSET, X_OFFSET);
			getViewer().setProperty(ReportRuler.PROPERTY_VOFFSET, Y_OFFSET);
			getViewer().setProperty(ReportRuler.PROPERTY_HEND, dw);
			getViewer().setProperty(ReportRuler.PROPERTY_VEND, dh);

			getViewer().setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN,
					new Point(X_OFFSET, Y_OFFSET));
		}
	}

	private Dimension containerSize;

	public Dimension getContaierSize() {
		return containerSize;
	}

	protected void updateContainerSize() {
		MCrosstab table = getCrosstab();
		if (table != null) {
			Dimension d = table.getCrosstabManager().getSize();
			d.height = Math.max(d.height, (Integer) table
					.getPropertyValue(JRDesignElement.PROPERTY_HEIGHT));
			d.width = Math.max(d.width, (Integer) table
					.getPropertyValue(JRDesignElement.PROPERTY_WIDTH));
			containerSize = d;
		} else
			containerSize = null;
	}

	protected abstract MCrosstab getCrosstab();

}
