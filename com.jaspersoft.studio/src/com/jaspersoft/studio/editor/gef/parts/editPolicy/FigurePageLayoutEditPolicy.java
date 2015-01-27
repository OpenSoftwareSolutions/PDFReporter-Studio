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
package com.jaspersoft.studio.editor.gef.parts.editPolicy;

import java.util.Collection;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.handles.HandleBounds;
import org.eclipse.gef.requests.CreateRequest;

import com.jaspersoft.studio.callout.CalloutEditPart;
import com.jaspersoft.studio.callout.command.CalloutSetConstraintCommand;
import com.jaspersoft.studio.callout.pin.PinEditPart;
import com.jaspersoft.studio.callout.pin.command.PinSetConstraintCommand;
import com.jaspersoft.studio.editor.gef.figures.ReportPageFigure;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.style.MStyle;

/*
 * The Class PageLayoutEditPolicy.
 */
public class FigurePageLayoutEditPolicy extends PageLayoutEditPolicy {
	private RectangleFigure targetFeedback;

	protected void eraseLayoutTargetFeedback(Request request) {
		super.eraseLayoutTargetFeedback(request);
		if (targetFeedback != null) {
			removeFeedback(targetFeedback);
			targetFeedback = null;
		}
	}

	protected IFigure getLayoutTargetFeedback(Request request) {
		if (request.getType().equals(RequestConstants.REQ_CREATE) && request instanceof CreateRequest) {
			CreateRequest cbr = (CreateRequest) request;
			if (cbr.getNewObject() instanceof Collection<?>) {
				Collection<?> c = (Collection<?>) cbr.getNewObject();
				if (!c.isEmpty()) {
					Object obj = c.iterator().next();
					if (obj instanceof MStyle) {
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

							targetFeedback.setBounds(rect.shrink(4, 4));
							targetFeedback.setBorder(new LineBorder(ColorConstants.lightBlue, 2));
							addFeedback(targetFeedback);
						}
						return targetFeedback;
					}
				}
			}
		}
		return null;
	}

	protected void showLayoutTargetFeedback(Request request) {
		super.showLayoutTargetFeedback(request);
		getLayoutTargetFeedback(request);
	}

	@Override
	protected Command getCreateCommand(ANode parent, Object obj, Rectangle constraint, int index) {
		Rectangle rect = ((Rectangle) constraint).getCopy();
		rect = rect.getTranslated(-ReportPageFigure.PAGE_BORDER.left, -ReportPageFigure.PAGE_BORDER.right);
		return super.getCreateCommand(parent, obj, rect, index);
	}

	@Override
	protected Command createAddCommand(EditPart child, Object constraint) {
		if (child instanceof CalloutEditPart) {
			return new CalloutSetConstraintCommand(((CalloutEditPart) child).getModel(), adaptConstraint(constraint));
		} else if (child instanceof PinEditPart) {
			return new PinSetConstraintCommand(((PinEditPart) child).getModel(), adaptConstraint(constraint));
		}
		return null;
	}

}
