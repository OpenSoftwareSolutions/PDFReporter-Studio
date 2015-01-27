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

import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.swt.graphics.Color;

import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.FrameFigureEditPart;
import com.jaspersoft.studio.editor.gef.parts.IRulerUpdatable;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainer;

public class FigureSelectionEditPolicy extends SelectionEditPolicy {

	/**
	 * Color of the feedback when the cursor is on the figure
	 */
	public static Color mouseEnterColor = ColorConstants.orange;

	@Override
	protected void showSelection() {
		EditPart host = getHost();
		if (host instanceof IRulerUpdatable)
			((IRulerUpdatable) host).updateRulers();
		ANode n = (ANode) getHost().getModel();
		List<EditPart> eparts = getHost().getParent().getChildren();
		int mindepth = Integer.MAX_VALUE;
		// EditPart eparent = null;
		for (EditPart ep : eparts) {
			if (ep instanceof IContainer) {
				ANode cn = (ANode) ep.getModel();
				int depth = n.findParent(cn);
				if (depth != -1) {
					if (mindepth > depth) {
						mindepth = depth;
						// eparent = ep;
					}
				}
			}
		}
	}

	@Override
	protected void hideSelection() {
	}

	/**
	 * If the figure is a frame and it has already a feedback, this one will not displayed
	 * 
	 * @param hostFigure
	 * @return true if the target figure it's a frame that has a feedback, otherwise false
	 */
	protected boolean hasAlreadyColoredBorder(EditPart hostFigure) {
		if (hostFigure instanceof FrameFigureEditPart)
			return ((FrameFigureEditPart) hostFigure).hasTargetFeedBack();
		return false;
	}

	@Override
	public void showTargetFeedback(Request request) {
		EditPart host = getHost();
		if (host instanceof FigureEditPart && host.getSelected() == EditPart.SELECTED_NONE) {
			// Check if the figure is a frame that already has a feedback
			if (!hasAlreadyColoredBorder(((FigureEditPart) host))) {
				((FigureEditPart) host).getFigure().setBorder(new HighlightBorder(mouseEnterColor, 2));
				super.showTargetFeedback(request);
			} else {
				eraseTargetFeedback(request);
			}
		}
	}

	@Override
	public void eraseTargetFeedback(Request request) {
		EditPart host = getHost();
		if (host instanceof FigureEditPart) {
			FigureEditPart feditpart = (FigureEditPart) host;
			feditpart.setPrefsBorder(feditpart.getFigure());
		}
		super.eraseTargetFeedback(request);
	}

}
