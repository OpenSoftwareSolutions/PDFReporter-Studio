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
package com.jaspersoft.studio.editor.gef.parts.handles;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.ResizeHandle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.editor.gef.parts.IPrefEditPart;
import com.jaspersoft.studio.editor.gef.parts.editPolicy.JSSCompoundResizeTracker;

/*
 * The Class BandResizeHandle.
 */
public class CellResizeHandle extends ResizeHandle {

	/** The tracker. */
	CellResizeTracker tracker = null;
	private int cursorDirection = 0;

	/**
	 * Constructor for SectionResizeHandle.
	 * 
	 * @param owner
	 *          the owner
	 * @param direction
	 *          the direction
	 */
	public CellResizeHandle(GraphicalEditPart owner, int direction) {
		super(owner, direction);
		setLocator(new CellResizeHandleLocator(owner, direction));
		setPreferredSize(1, 1);
		cursorDirection = direction;
	}

	@Override
	protected Color getBorderColor() {
		if (getOwner() instanceof IPrefEditPart)
			return ((IPrefEditPart) getOwner()).getMarginColor();
		return SWTResourceManager.getColor(IPrefEditPart.DEFAULT_MARGINCOLOR);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.SquareHandle#getFillColor()
	 */
	@Override
	protected Color getFillColor() {
		if (getOwner().getFigure().getBounds().height == 0)
			return ColorConstants.red;
		return ColorConstants.blue;
	}

	protected DragTracker createDragTracker() {
		return new JSSCompoundResizeTracker(getOwner(), cursorDirection) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			protected List createOperationSet() {
				ArrayList res = new ArrayList();
				res.add(getOwner());
				return res;
			}
		};
	}
}
