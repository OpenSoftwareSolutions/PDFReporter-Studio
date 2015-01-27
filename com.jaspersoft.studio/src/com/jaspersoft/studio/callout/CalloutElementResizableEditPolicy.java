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
package com.jaspersoft.studio.callout;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Cursors;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.GraphicalEditPart;

import com.jaspersoft.studio.editor.gef.parts.editPolicy.ElementResizableEditPolicy;

public class CalloutElementResizableEditPolicy extends ElementResizableEditPolicy {
	@Override
	protected List<?> createSelectionHandles() {
		if (getResizeDirections() == PositionConstants.NONE) {
			// non resizable, so delegate to super implementation
			return super.createSelectionHandles();
		}

		// resizable in at least one direction
		List<?> list = new ArrayList();
		createMoveHandle(list);
		createResizeHandle(list, PositionConstants.SOUTH_EAST);
		return list;
	}

	@Override
	protected void createResizeHandle(List handles, int direction) {
		if ((getResizeDirections() & direction) == direction) {
			SothEastRectangleHandles handle = new SothEastRectangleHandles((GraphicalEditPart) getHost(), direction);
			handle.setDragTracker(getResizeTracker(direction));
			handle.setCursor(Cursors.getDirectionalCursor(direction, getHostFigure().isMirrored()));
			handles.add(handle);
		} else {
			// display 'resize' handle to allow dragging or indicate selection
			// only
			createDragHandle(handles, direction);
		}
	}
}
