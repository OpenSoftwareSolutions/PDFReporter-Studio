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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.NonResizableHandleKit;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gef.tools.SelectEditPartTracker;

import com.jaspersoft.studio.editor.gef.parts.FigureEditPart;
import com.jaspersoft.studio.editor.gef.selection.ColoredSquareHandles;

public class ElementResizableEditPolicy extends ResizableEditPolicy {

	/**
	 * Instantiates a new band resizable edit policy.
	 */
	public ElementResizableEditPolicy() {
		super();
	}

	/**
	 * This method override the ResizableEditPolicy createSelectionHandle(). It's identical to 3.7 and later methods, but
	 * different from previous versions. In this way the behavior of this handle under eclipse 3.6 and eclipse 3.7+ is
	 * uniformed
	 */
	@Override
	protected List createSelectionHandles() {
		if (getResizeDirections() == PositionConstants.NONE) {
			// non resizable, so delegate to super implementation
			return super.createSelectionHandles();
		}

		// resizable in at least one direction
		List list = new ArrayList();
		createMoveHandle(list);
		createResizeHandle(list, PositionConstants.NORTH);
		createResizeHandle(list, PositionConstants.EAST);
		createResizeHandle(list, PositionConstants.SOUTH);
		createResizeHandle(list, PositionConstants.WEST);
		createResizeHandle(list, PositionConstants.SOUTH_EAST);
		createResizeHandle(list, PositionConstants.SOUTH_WEST);
		createResizeHandle(list, PositionConstants.NORTH_WEST);
		createResizeHandle(list, PositionConstants.NORTH_EAST);
		return list;
	}

	/**
	 * Shows or updates feedback for a change bounds request.
	 * 
	 * @param request
	 *          the request
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {

		IFigure feedback = getDragSourceFeedbackFigure();

		PrecisionRectangle rect = new PrecisionRectangle(getInitialFeedbackBounds().getCopy());
		getHostFigure().translateToAbsolute(rect);
		rect.translate(request.getMoveDelta());
		rect.resize(request.getSizeDelta());

		// Calculate changes for the figure...
		String s = "";
		int scaleH = 0;
		int scaleW = 0;
		if (getHost() instanceof FigureEditPart
				&& ((FigureEditPart) getHost()).getModelNode().getValue() instanceof JRDesignElement) {
			JRDesignElement jrElement = (JRDesignElement) ((FigureEditPart) getHost()).getModelNode().getValue();
			Rectangle oldBounds = new Rectangle(jrElement.getX(), jrElement.getY(), jrElement.getWidth(),
					jrElement.getHeight());

			PrecisionRectangle rect2 = new PrecisionRectangle(new Rectangle(request.getMoveDelta().x,
					request.getMoveDelta().y, request.getSizeDelta().width, request.getSizeDelta().height));
			getHostFigure().translateToRelative(rect2);

			oldBounds.translate(rect2.x, rect2.y);
			oldBounds.resize(rect2.width, rect2.height);

			s += oldBounds.x + ", " + oldBounds.y + ", " + oldBounds.width + ", " + oldBounds.height;
			if (oldBounds.width != 0)
				scaleW = rect.width / oldBounds.width - 1;
			if (oldBounds.height != 0)
				scaleH = rect.height / oldBounds.height - 1;
		}

		feedback.translateToRelative(rect);

		((ElementFeedbackFigure) feedback).setText(s);

		feedback.setBounds(rect.resize(-scaleW, -scaleH));
	}

	protected void createMoveHandle(List handles) {
		if (isDragAllowed()) {
			// display 'move' handle to allow dragging
			ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), handles, getDragTracker(), Cursors.SIZEALL);
		} else {
			// display 'move' handle only to indicate selection
			ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), handles, getSelectTracker(), SharedCursors.ARROW);
		}
	}

	protected void createDragHandle(List handles, int direction) {
		if (isDragAllowed()) {
			// display 'resize' handles to allow dragging (drag tracker)
			NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), handles, direction, getDragTracker(),
					SharedCursors.SIZEALL);
		} else {
			// display 'resize' handles to indicate selection only (selection
			// tracker)
			NonResizableHandleKit.addHandle((GraphicalEditPart) getHost(), handles, direction, getSelectTracker(),
					SharedCursors.ARROW);
		}
	}

	protected void createResizeHandle(List handles, int direction) {
		if ((getResizeDirections() & direction) == direction) {
			ColoredSquareHandles handle = new ColoredSquareHandles((GraphicalEditPart) getHost(), direction);
			handle.setDragTracker(getResizeTracker(direction));
			handle.setCursor(Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored()));
			handles.add(handle);
		} else {
			// display 'resize' handle to allow dragging or indicate selection
			// only
			createDragHandle(handles, direction);
		}
	}

	/**
	 * Creates the figure used for feedback.
	 * 
	 * @return the new feedback figure
	 */
	protected IFigure createDragSourceFeedbackFigure() {
		// Use a ghost rectangle for feedback
		RectangleFigure r = new ElementFeedbackFigure();

		// FigureUtilities.makeGhostShape(r);
		r.setLineStyle(Graphics.LINE_DOT);
		r.setForegroundColor(ColorConstants.black);
		r.setBounds(getInitialFeedbackBounds().resize(-1, -1));// new Rectangle(ifb.x, ifb.y, ifb.width -100,
																														// ifb.height));
		addFeedback(r);
		return r;
	}

	// ==== BACK COMPATIBILITY METHODS ==== //

	/**
	 * Returns a drag tracker to use by a resize handle.
	 * 
	 * @return a new {@link SearchParentDragTracker}
	 * @since 3.7
	 */
	protected DragEditPartsTracker getDragTracker() {
		return new SearchParentDragTracker(getHost());
	}

	/**
	 * Returns a selection tracker to use by a selection handle.
	 * 
	 * @return a new {@link SelectEditPartTracker}
	 * @since 3.7
	 */
	protected SelectEditPartTracker getSelectTracker() {
		return new SelectEditPartTracker(getHost());
	}

	/**
	 * Returns a resize tracker for the given direction to be used by a resize handle.
	 * 
	 * @param direction
	 *          the resize direction for the {@link JSSCompoundResizeTracker}.
	 * @return a new {@link JSSCompoundResizeTracker}
	 * @since 3.7
	 */
	protected ResizeTracker getResizeTracker(int direction) {
		return new JSSCompoundResizeTracker((GraphicalEditPart) getHost(), direction);
	}

	// =================================== //

	public List getHandles() {
		return handles;
	}
}
