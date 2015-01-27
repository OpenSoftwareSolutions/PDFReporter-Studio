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
package com.jaspersoft.studio.editor.action.create;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.model.util.IIconDescriptor;
/*
 * The Class CreateElementAction.
 */
public abstract class CreateElementAction extends Action {

	/** The cmd. */
	private Command cmd;

	/** The parent. */
	private Object parent;

	/** The location. */
	private Rectangle location;

	/** The index. */
	private int index;

	/**
	 * Instantiates a new creates the element action.
	 * 
	 * @param iconDescriptor
	 *          the icon descriptor
	 */
	public CreateElementAction(IIconDescriptor iconDescriptor) {
		super(iconDescriptor.getTitle(), iconDescriptor.getIcon16());
		setToolTipText(iconDescriptor.getDescription());
		setDescription(iconDescriptor.getDescription());
	}

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		return null;
	}

	/**
	 * Sets the command.
	 * 
	 * @param cmd
	 *          the new command
	 */
	public void setCommand(Command cmd) {
		this.cmd = cmd;
	}

	/**
	 * Gets the command.
	 * 
	 * @return the command
	 */
	public Command getCommand() {
		return cmd;
	}

	/**
	 * Drop into.
	 * 
	 * @param parent
	 *          the parent
	 * @param location
	 *          the location
	 * @param index
	 *          the index
	 */
	public void dropInto(Object parent, Rectangle location, int index) {
		this.parent = parent;
		this.location = location;
		this.index = index;
	}

	/**
	 * Gets the index.
	 * 
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * Gets the parent.
	 * 
	 * @return the parent
	 */
	public Object getParent() {
		return parent;
	}

	/**
	 * Gets the location.
	 * 
	 * @return the location
	 */
	public Rectangle getLocation() {
		return location;
	}

}
