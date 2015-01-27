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
package com.jaspersoft.studio.editor.gef.rulers.component;

import java.util.Iterator;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.gef.internal.ui.rulers.GuideFigure;
import org.eclipse.gef.internal.ui.rulers.GuidePlaceHolder;
import org.eclipse.gef.requests.ChangeBoundsRequest;

public class JDDragGuidePolicy extends GraphicalEditPolicy {

	private List<?> attachedEditParts = null;
	private IFigure dummyGuideFigure, dummyLineFigure;
	private boolean dragInProgress = false;

	protected IFigure createDummyLineFigure() {
		return new Figure();
	}

	protected GuideFigure createDummyGuideFigure() {
		return new GuidePlaceHolder(getGuideEditPart().isHorizontal());
	}

	/*
	 * Fix for Bug65885 If you undo guide creation while dragging that guide, it was leaving behind drag feedback. This
	 * was because by the time eraseSourceFeedback() was being called, the guide edit part had been deactivated (and hence
	 * eraseSourceFeedback is never called on this policy). So we make sure that this policy cleans up when it is
	 * deactivated.
	 */
	public void deactivate() {
		removeFeedback();
		super.deactivate();
	}

	private void eraseAttachedPartsFeedback(Request request) {
		if (attachedEditParts != null) {
			ChangeBoundsRequest req = new ChangeBoundsRequest(request.getType());
			req.setEditParts(attachedEditParts);

			Iterator<?> i = attachedEditParts.iterator();

			while (i.hasNext())
				((EditPart) i.next()).eraseSourceFeedback(req);
			attachedEditParts = null;
		}
	}

	public void eraseSourceFeedback(Request request) {
		getGuideEditPart().updateLocationOfFigures(getGuideEditPart().getZoomedPosition());
		getHostFigure().setVisible(true);
		getGuideEditPart().getGuideLineFigure().setVisible(true);
		removeFeedback();
		getGuideEditPart().setCurrentCursor(null);
		dragInProgress = false;

		eraseAttachedPartsFeedback(request);
	}

	private List<?> getAttachedEditParts() {
		if (attachedEditParts == null)
			attachedEditParts = getGuideEditPart().getRulerProvider().getAttachedEditParts(getHost().getModel(),
					((JDRulerEditPart) getHost().getParent()).getDiagramViewer());
		return attachedEditParts;
	}

	public Command getCommand(Request request) {
		Command cmd = null;
		final ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		if (isDeleteRequest(req)) {
			cmd = getGuideEditPart().getRulerProvider().getDeleteGuideCommand(getHost().getModel());
		} else {
			int pDelta = 0;
			if (getGuideEditPart().isHorizontal()) {
				pDelta = req.getMoveDelta().y;
			} else {
				pDelta = req.getMoveDelta().x;
			}
			if (isMoveValid(getGuideEditPart().getZoomedPosition() + pDelta)) {
				ZoomManager zoomManager = getGuideEditPart().getZoomManager();
				if (zoomManager != null) {
					pDelta = (int) Math.round(pDelta / zoomManager.getZoom());
				}
				cmd = getGuideEditPart().getRulerProvider().getMoveGuideCommand(getHost().getModel(), pDelta);
			} else {
				cmd = UnexecutableCommand.INSTANCE;
			}
		}
		return cmd;
	}

	protected IFigure getDummyGuideFigure() {
		if (dummyGuideFigure == null) {
			dummyGuideFigure = createDummyGuideFigure();
		}
		return dummyGuideFigure;
	}

	protected IFigure getDummyLineFigure() {
		if (dummyLineFigure == null) {
			dummyLineFigure = createDummyLineFigure();
		}
		return dummyLineFigure;
	}

	protected JDGuideEditPart getGuideEditPart() {
		return (JDGuideEditPart) getHost();
	}

	protected boolean isDeleteRequest(ChangeBoundsRequest req) {
		int pos, max, min = 0;
		if (getGuideEditPart().isHorizontal()) {
			pos = req.getLocation().x;
			Rectangle zone = getHostFigure().getBounds().getExpanded(JDGuideEditPart.DELETE_THRESHOLD, 0);
			min = zone.x;
			max = min + zone.width;
		} else {
			pos = req.getLocation().y;
			Rectangle zone = getHostFigure().getBounds().getExpanded(0, JDGuideEditPart.DELETE_THRESHOLD);
			min = zone.y;
			max = min + zone.height;
		}
		return pos < min || pos > max;
	}

	protected boolean isMoveValid(int zoomedPosition) {
		boolean result = true;
		ZoomManager zoomManager = getGuideEditPart().getZoomManager();
		int position = zoomedPosition;
		if (zoomManager != null) {
			position = (int) Math.round(position / zoomManager.getZoom());
		}
		Iterator<?> guides = getGuideEditPart().getRulerProvider().getGuides().iterator();
		while (guides.hasNext()) {
			Object guide = guides.next();
			if (guide != getGuideEditPart().getModel()) {
				int guidePos = getGuideEditPart().getRulerProvider().getGuidePosition(guide);
				if (Math.abs(guidePos - position) < JDGuideEditPart.MIN_DISTANCE_BW_GUIDES) {
					result = false;
					break;
				}
			}
		}

		return result;
	}

	private void removeFeedback() {
		if (getDummyGuideFigure().getParent() != null) {
			getDummyGuideFigure().getParent().remove(getDummyGuideFigure());
		}
		if (getDummyLineFigure().getParent() != null) {
			getDummyLineFigure().getParent().remove(getDummyLineFigure());
		}
	}

	private void showAttachedPartsFeedback(ChangeBoundsRequest request) {
		ChangeBoundsRequest req = new ChangeBoundsRequest(request.getType());
		req.setEditParts(getAttachedEditParts());

		if (getGuideEditPart().isHorizontal())
			req.setMoveDelta(new Point(0, request.getMoveDelta().y));
		else
			req.setMoveDelta(new Point(request.getMoveDelta().x, 0));

		Iterator<?> i = getAttachedEditParts().iterator();

		while (i.hasNext())
			((EditPart) i.next()).showSourceFeedback(req);
	}

	public void showSourceFeedback(Request request) {
		if (!dragInProgress) {
			dragInProgress = true;
			// add the placeholder guide figure to the ruler
			getHostFigure().getParent().add(getDummyGuideFigure(), 0);
			((GraphicalEditPart) getHost().getParent()).setLayoutConstraint(getHost(), getDummyGuideFigure(), new Integer(
					getGuideEditPart().getZoomedPosition()));
			getDummyGuideFigure().setBounds(getHostFigure().getBounds());
			// add the invisible placeholder line figure to the primary viewer
			getGuideEditPart().getGuideLayer().add(getDummyLineFigure(), 0);
			getGuideEditPart().getGuideLayer().setConstraint(getDummyLineFigure(),
					new Boolean(getGuideEditPart().isHorizontal()));
			getDummyLineFigure().setBounds(getGuideEditPart().getGuideLineFigure().getBounds());
			// move the guide being dragged to the last index so that it's drawn
			// on
			// top of other guides
			List<IFigure> children = getHostFigure().getParent().getChildren();
			children.remove(getHostFigure());
			children.add(getHostFigure());
		}
		ChangeBoundsRequest req = (ChangeBoundsRequest) request;
		if (isDeleteRequest(req)) {
			getHostFigure().setVisible(false);
			getGuideEditPart().getGuideLineFigure().setVisible(false);
			getGuideEditPart().setCurrentCursor(SharedCursors.ARROW);
			eraseAttachedPartsFeedback(request);
		} else {
			int newPosition;
			if (getGuideEditPart().isHorizontal()) {
				newPosition = getGuideEditPart().getZoomedPosition() + req.getMoveDelta().y;
			} else {
				newPosition = getGuideEditPart().getZoomedPosition() + req.getMoveDelta().x;
			}
			getHostFigure().setVisible(true);
			getGuideEditPart().getGuideLineFigure().setVisible(true);
			if (isMoveValid(newPosition)) {
				getGuideEditPart().setCurrentCursor(null);
				getGuideEditPart().updateLocationOfFigures(newPosition);
				showAttachedPartsFeedback(req);
			} else {
				getGuideEditPart().setCurrentCursor(SharedCursors.NO);
				getGuideEditPart().updateLocationOfFigures(getGuideEditPart().getZoomedPosition());
				eraseAttachedPartsFeedback(request);
			}
		}
	}

	public boolean understandsRequest(Request req) {
		return req.getType().equals(REQ_MOVE);
	}

}
