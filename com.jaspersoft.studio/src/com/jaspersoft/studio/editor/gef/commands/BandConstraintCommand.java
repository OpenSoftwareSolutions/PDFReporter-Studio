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
package com.jaspersoft.studio.editor.gef.commands;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.messages.Messages;
/*/*
 * The Class BandConstraintCommand.
 */
public class BandConstraintCommand extends Command {
	
	/** The edit part. */
	private GraphicalEditPart editPart;
	
	/** The old bounds. */
	private Rectangle newBounds, oldBounds;

	/**
	 * Instantiates a new band constraint command.
	 * 
	 * @param editPart
	 *          the edit part
	 * @param constraint
	 *          the constraint
	 */
	public BandConstraintCommand(GraphicalEditPart editPart,
			Rectangle constraint) {
		this.editPart = editPart;
		this.newBounds = constraint;
		this.oldBounds = new Rectangle(editPart.getFigure().getBounds());
		this.newBounds.setSize( oldBounds.getSize());
		setLabel(Messages.BandConstraintCommand_band_resized);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	public void execute() {
		redo();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#redo()
	 */
	public void redo() {
		((GraphicalEditPart) editPart.getParent()).setLayoutConstraint(
				editPart, editPart.getFigure(), newBounds);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	public void undo() {
		((GraphicalEditPart) editPart.getParent()).setLayoutConstraint(
				editPart, editPart.getFigure(), oldBounds);
	}
}
