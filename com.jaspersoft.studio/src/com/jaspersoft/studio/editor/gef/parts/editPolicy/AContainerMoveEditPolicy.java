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

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionHandlesEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.jaspersoft.studio.editor.gef.parts.handles.CellResizeHandle;

/*
 * The Class BandMoveEditPolicy.
 * 
 * @author Chicu Veaceslav
 */
public abstract class AContainerMoveEditPolicy extends SelectionHandlesEditPolicy {

	@Override
	public GraphicalEditPart getHost() {
		return (GraphicalEditPart) super.getHost();
	}

	@Override
	protected List<Handle> createSelectionHandles() {
		List<Handle> list = new ArrayList<Handle>();
		GraphicalEditPart geditPart = getHost();
		list.add(new CellResizeHandle(geditPart, PositionConstants.SOUTH));
		list.add(new CellResizeHandle(geditPart, PositionConstants.EAST));
		return list;
	}

	@Override
	protected void showSelection() {
	}

	@Override
	protected void hideSelection() {
	}

	@Override
	public void showSourceFeedback(Request request) {
	}

	@Override
	public void showTargetFeedback(Request request) {
		super.showTargetFeedback(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	@Override
	public void activate() {
		super.activate();
		addSelectionHandles();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	@Override
	public void deactivate() {
		removeSelectionHandles();
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
	 */
	@Override
	public boolean understandsRequest(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			return true;
		if (REQ_MOVE.equals(request.getType()))
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request)
	 */
	@Override
	public void eraseSourceFeedback(Request request) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	@Override
	public Command getCommand(Request request) {
		if (REQ_RESIZE.equals(request.getType()))
			return getResizeCommand((ChangeBoundsRequest) request);
		if (REQ_MOVE.equals(request.getType()))
			return getResizeCommand((ChangeBoundsRequest) request);
		return null;
	}

	/**
	 * Gets the resize command.
	 * 
	 * @param request
	 *          the request
	 * @return the resize command
	 */
	protected abstract Command getResizeCommand(ChangeBoundsRequest request);

}
