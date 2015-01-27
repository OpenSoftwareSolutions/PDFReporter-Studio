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
package com.jaspersoft.studio.callout.pin;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.gef.Request;

import com.jaspersoft.studio.editor.gef.parts.editPolicy.ElementResizableEditPolicy;

public class PinMoveEditPolicy extends ElementResizableEditPolicy {
	public PinMoveEditPolicy() {
		super();
	}

	@Override
	protected List createSelectionHandles() {
		if (getResizeDirections() == PositionConstants.NONE) {
			// non resizable, so delegate to super implementation
			return super.createSelectionHandles();
		}

		// resizable in at least one direction
		List list = new ArrayList();
		createMoveHandle(list);
		return list;
	}

	@Override
	public boolean understandsRequest(Request request) {
		if (REQ_MOVE.equals(request.getType()))
			return isDragAllowed();
		return false;
	}

	@Override
	protected void createResizeHandle(List handles, int direction) {
	}
}
