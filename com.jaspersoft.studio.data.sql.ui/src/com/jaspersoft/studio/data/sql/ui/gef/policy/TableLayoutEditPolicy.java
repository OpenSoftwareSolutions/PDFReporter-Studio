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
package com.jaspersoft.studio.data.sql.ui.gef.policy;

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.ui.gef.command.JoinCommand;
import com.jaspersoft.studio.data.sql.ui.gef.parts.ColumnEditPart;
import com.jaspersoft.studio.data.sql.ui.gef.parts.TableEditPart;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.NoSelectionEditPolicy;

public class TableLayoutEditPolicy extends FlowLayoutEditPolicy {
	private RectangleFigure targetFeedback;

	@Override
	protected void eraseLayoutTargetFeedback(Request request) {
		super.eraseLayoutTargetFeedback(request);
		if (targetFeedback != null) {
			if (getFeedbackLayer().getChildren().contains(targetFeedback)) {
				removeFeedback(targetFeedback);
				targetFeedback = null;
			}
		}
	}

	@Override
	protected void showLayoutTargetFeedback(Request request) {
		if (request instanceof ChangeBoundsRequest) {
			eraseLayoutTargetFeedback(request);

			ChangeBoundsRequest r = (ChangeBoundsRequest) request;
			EditPart child = null;
			List<?> editParts = r.getEditParts();
			for (int i = 0; i < editParts.size();) {
				child = (EditPart) editParts.get(i);
				break;
			}
			GraphicalEditPart after = (GraphicalEditPart) getInsertionReference(request);

			if (after == null || child == null)
				return;
			if (after.getParent() == child.getParent())
				return;
			if (targetFeedback == null) {
				targetFeedback = new RectangleFigure();
				targetFeedback.setFill(true);
				targetFeedback.setBackgroundColor(ColorConstants.gray);
				targetFeedback.setAlpha(50);

				IFigure hostFigure = after.getFigure();
				Rectangle bounds = hostFigure.getBounds();
				if (hostFigure instanceof HandleBounds)
					bounds = ((HandleBounds) hostFigure).getHandleBounds();
				Rectangle rect = new PrecisionRectangle(bounds);
				hostFigure.translateToAbsolute(rect);
				getFeedbackLayer().translateToRelative(rect);

				targetFeedback.setBounds(rect.shrink(-4, -4));
				// targetFeedback.setBorder(new LineBorder(ColorConstants.gray, 2));
				addFeedback(targetFeedback);
			}
		} else
			super.showLayoutTargetFeedback(request);
	}

	/**
	 * Creates command to transfer child column to after column (in another table)
	 */
	protected Command createAddCommand(EditPart child, EditPart after) {
		if (!(child instanceof ColumnEditPart))
			return null;
		if (!(after instanceof ColumnEditPart))
			return null;
		// if (child.getParent() == after.getParent())
		// return null;

		MSQLColumn toMove = ((ColumnEditPart) child).getModel();
		MFromTable srcTbl = ((ColumnEditPart) child).getParent().getModel();
		MSQLColumn afterModel = ((ColumnEditPart) after).getModel();
		MFromTable destTbl = ((ColumnEditPart) after).getParent().getModel();

		SQLQueryDesigner designer = ((TableEditPart) getHost()).getDesigner();

		return new JoinCommand(toMove, srcTbl, afterModel, destTbl, designer);
	}

	/**
	 * Creates command to transfer child column to after specified column (within
	 * table)
	 */
	protected Command createMoveChildCommand(EditPart child, EditPart after) {
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getCreateCommand(CreateRequest request) {
		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

	@Override
	protected EditPolicy createChildEditPolicy(EditPart child) {
		if (child instanceof ColumnEditPart)
			return new NoSelectionEditPolicy();

		return super.createChildEditPolicy(child);
	}
}
