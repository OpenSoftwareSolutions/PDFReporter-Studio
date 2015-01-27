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
package com.jaspersoft.studio.editor.gef.parts.band;

import java.util.List;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;

import org.eclipse.draw2d.Cursors;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.swt.SWT;

/**
 * 
 * @author Chicu Veaceslav & Orlandin Marco
 *
 */
public class BandResizeHandle extends AbstractHandle {

	/** The tracker. */
	DragTracker tracker = null;
	
	/**
	 * Constructor for SectionResizeHandle.
	 * 
	 * @param owner
	 *          the owner
	 */
	public BandResizeHandle(GraphicalEditPart owner) {
		super(owner, new BandHandleLocator(owner.getFigure()));
		initialize();
	}

	/**
	 * Create the tracker if there isn't one, the the tracker will
	 * be returned
	 */
	protected DragTracker createDragTracker() {
		if (tracker == null)
		{
			tracker = new BandResizeTracker(getOwner());
		}
		return tracker;
	}
	
	/**
	 * Create a drag tracker for a specific owner
	 * @param owner the owner of the drag tracker 
	 * @return the drag tracker
	 */
	protected DragTracker createDragTracker(GraphicalEditPart owner) {
		return new BandResizeTracker(owner);
	}
	
	/**
	 * Return the drag tracker of this element if the element is visible (height>1), of if 
	 * the element is not visible but the left shif key is held. Otherwise it return the drag tracker
	 * of the band up to this element.
	 */
	public DragTracker getDragTracker()
	{	
		GraphicalEditPart actualChildren = getOwner();
		boolean shiftPressed = JasperReportsPlugin.isPressed(SWT.SHIFT);
		if (getOwner().getFigure().getBounds().height == 1 && !shiftPressed) {
			List<?> children = getOwner().getParent().getChildren();
			int index = children.indexOf(actualChildren);
			while(actualChildren.getFigure().getBounds().height == 1 && index != 0){
				index--;
				actualChildren = (GraphicalEditPart)children.get(index);
			}
			return createDragTracker(actualChildren);
		}
		else return createDragTracker();
	}

	/**
	 * Initializes the handle.  Sets the {@link DragTracker} and
	 * DragCursor.
	 */
	protected void initialize() {
		setOpaque(false);
		setCursor(Cursors.SIZES);
	}
	
}

