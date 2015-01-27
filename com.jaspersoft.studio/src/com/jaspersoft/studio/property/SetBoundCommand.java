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
package com.jaspersoft.studio.property;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.APropertyNode;

/*
 * The Class SetValueCommand.
 */
public class SetBoundCommand extends Command {

	/** The property value. */
	protected Rectangle boundNewValue;

	/** The undo value. */
	protected Rectangle undoValue;


	/** The target. */
	protected APropertyNode target;

	/**
	 * Instantiates a new sets the value command.
	 * 
	 * @param propLabel
	 *          the prop label
	 */
	public SetBoundCommand() {
		super("Set Bound Command");
		undoValue = null;
		boundNewValue = null;
		target = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	@Override
	public boolean canExecute() {
		return (boundNewValue != null && target != null && target.getValue() != null);
	}

	/**
	 * Sets the property id.
	 * 
	 * @param pName
	 *          the new property id
	 */
	public void setContext(APropertyNode target, Rectangle newBound) {
		this.target = target;
		this.boundNewValue = newBound;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		JRDesignElement element = (JRDesignElement)target.getValue();
		undoValue = new Rectangle(element.getX(), element.getY(), element.getWidth(), element.getHeight());
		
		target.setPropertyValue(JRDesignElement.PROPERTY_X, boundNewValue.x);
		target.setPropertyValue(JRDesignElement.PROPERTY_Y, boundNewValue.y);
		target.setPropertyValue(JRDesignElement.PROPERTY_WIDTH, boundNewValue.width);
		target.setPropertyValue(JRDesignElement.PROPERTY_HEIGHT, boundNewValue.height);	
	}
	
	@Override
	public boolean canUndo() {
		return undoValue != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (canUndo()){
			target.setPropertyValue(JRDesignElement.PROPERTY_X, undoValue.x);
			target.setPropertyValue(JRDesignElement.PROPERTY_Y, undoValue.y);
			target.setPropertyValue(JRDesignElement.PROPERTY_WIDTH, undoValue.width);
			target.setPropertyValue(JRDesignElement.PROPERTY_HEIGHT, undoValue.height);	
		}
	}

}
