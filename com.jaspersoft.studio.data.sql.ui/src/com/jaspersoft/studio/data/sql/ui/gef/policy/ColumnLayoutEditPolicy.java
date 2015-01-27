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

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.FlowLayoutEditPolicy;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.CreateRequest;

public class ColumnLayoutEditPolicy extends FlowLayoutEditPolicy {
	private RectangleFigure targetFeedback;

	@Override
	protected void eraseLayoutTargetFeedback(Request request) {
		super.eraseLayoutTargetFeedback(request);
		if (targetFeedback != null) {
			removeFeedback(targetFeedback);
			targetFeedback = null;
		}
	}

	@Override
	protected void showLayoutTargetFeedback(Request request) {
		if (targetFeedback == null) {
			targetFeedback = new RectangleFigure();
			targetFeedback.setFill(false);

			IFigure hostFigure = getHostFigure();
			Rectangle bounds = hostFigure.getBounds();
			if (hostFigure instanceof HandleBounds)
				bounds = ((HandleBounds) hostFigure).getHandleBounds();
			Rectangle rect = new PrecisionRectangle(bounds);
			getHostFigure().translateToAbsolute(rect);
			getFeedbackLayer().translateToRelative(rect);

			targetFeedback.setBounds(rect.shrink(-2, -2));
			targetFeedback.setBorder(new LineBorder(ColorConstants.lightBlue, 1));
			addFeedback(targetFeedback);
		}
	}

	/**
	 * Creates command to transfer child column to after column (in another table)
	 */
	protected Command createAddCommand(EditPart child, EditPart after) {
		// if (!(child instanceof ColumnEditPart))
		// return null;
		// if (!(after instanceof ColumnEditPart))
		// return null;
		//
		// MSQLColumn toMove = ((ColumnEditPart) child).getModel();
		// MFromTable srcTbl = ((ColumnEditPart) child).getParent().getModel();
		// MSQLColumn afterModel = ((ColumnEditPart) after).getModel();
		// MFromTable destTbl = ((ColumnEditPart) after).getParent().getModel();
		//
		// SQLQueryDesigner designer = ((TableEditPart) getHost()).getDesigner();
		//
		// return new JoinCommand(toMove, srcTbl, afterModel, destTbl, designer);
		return null;
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
		// System.out.println(request);

		return null;
	}

	/**
	 * @param request
	 * @return
	 */
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}

}
