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

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Handle;
import org.eclipse.gef.Request;
import org.eclipse.gef.SharedCursors;
import org.eclipse.gef.editpolicies.NonResizableEditPolicy;
import org.eclipse.gef.handles.ResizableHandleKit;
import org.eclipse.gef.tools.DragEditPartsTracker;
import org.eclipse.gef.tools.ResizeTracker;
import org.eclipse.gef.tools.SelectEditPartTracker;

public class NoSelectionEditPolicy extends NonResizableEditPolicy {
	@Override
	protected List<?> createSelectionHandles() {
		List<?> list = new ArrayList<Handle>();
		createMoveHandle(list);
		return list;
	}

	@Override
	public boolean understandsRequest(Request request) {
		if (REQ_MOVE.equals(request.getType()))
			return isDragAllowed();
		return false;
	}
	
	/**
	 * Creates a 'move' handle, which uses a {@link DragEditPartsTracker} in
	 * case {@link #isDragAllowed()} returns true, and a
	 * {@link SelectEditPartTracker} otherwise.
	 * <p>
	 * 
	 * <i>NOTE</i>: <b>BACKCOMPABILITY SOLUTION FOR ECLIPSE 3.6.x</b>
	 * 
	 * @param handles
	 *            The list of handles to add the move handle to.
	 * @since 3.7 
	 */
	protected void createMoveHandle(List handles) {
		if (isDragAllowed()) {
			// display 'move' handle to allow dragging
			ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(),
					handles, getDragTracker(), Cursors.SIZEALL);
		} else {
			// display 'move' handle only to indicate selection
			ResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(),
					handles, getSelectTracker(), SharedCursors.ARROW);
		}
	}
	
	/**
	 * Returns a selection tracker to use by a selection handle.
	 * <p>
	 * 
	 * <i>NOTE</i>: <b>BACKCOMPABILITY SOLUTION FOR ECLIPSE 3.6.x</b>
	 * 
	 * @return a new {@link ResizeTracker}
	 * @since 3.7
	 */
	protected SelectEditPartTracker getSelectTracker() {
		return new SelectEditPartTracker(getHost());
	}

	/**
	 * Returns a drag tracker to use by a resize handle.
	 * <p>
	 * 
	 * <i>NOTE</i>: <b>BACKCOMPABILITY SOLUTION FOR ECLIPSE 3.6.x</b>
	 * 
	 * @return a new {@link ResizeTracker}
	 * @since 3.7
	 */
	protected DragEditPartsTracker getDragTracker() {
		return new DragEditPartsTracker(getHost());
	}


}
