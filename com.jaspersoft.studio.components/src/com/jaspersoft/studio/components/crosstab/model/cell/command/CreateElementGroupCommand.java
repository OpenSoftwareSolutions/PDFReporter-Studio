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
package com.jaspersoft.studio.components.crosstab.model.cell.command;

import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.model.MElementGroup;

public class CreateElementGroupCommand extends Command {
	private MElementGroup srcNode;
	private JRDesignElementGroup jrElement;

	private JRDesignCellContents jrCell;

	private Point location;

	private int index;

	/**
	 * Instantiates a new creates the element command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateElementGroupCommand(MCell destNode, MElementGroup srcNode, int index) {
		super();
		this.jrElement = (JRDesignElementGroup) srcNode.getValue();
		this.jrCell = (JRDesignCellContents) destNode.getValue();
		this.index = index;
		this.srcNode = srcNode;
	}

	/**
	 * Creates the object.
	 */
	protected void createObject() {
		if (jrElement == null) {
			jrElement = srcNode.createJRElementGroup();

			if (jrElement != null) {
				if (location == null)
					location = new Point(0, 0);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		createObject();
		if (jrElement != null) {
			if (index >= 0 && index <= jrCell.getChildren().size())
				jrCell.addElementGroup(index, jrElement);
			else
				jrCell.addElementGroup(jrElement);
		}
		// if (firstTime) {
		// SelectionHelper.setSelection(jrElement, false);
		// firstTime = false;
		// }
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrCell == null || jrElement == null)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrCell.removeElementGroup(jrElement);
	}

}
