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
package com.jaspersoft.studio.model.command;

import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignFrame;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MElementGroup;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteElementGroupCommand extends Command {

	/** The jr group. */
	private JRElementGroup jrGroup;

	/** The jr element. */
	private JRDesignElementGroup jrElement;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete element group command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteElementGroupCommand(ANode destNode, MElementGroup srcNode) {
		super();
		this.jrElement = (JRDesignElementGroup) srcNode.getValue();
		this.jrGroup = jrElement.getElementGroup();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrGroup.getChildren().indexOf(jrElement);
		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).removeElementGroup(jrElement);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).removeElementGroup(jrElement);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrGroup == null || jrElement == null)
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
		if (jrGroup instanceof JRDesignElementGroup) {
			if (elementPosition > ((JRDesignElementGroup) jrGroup).getChildren().size())
				((JRDesignElementGroup) jrGroup).addElementGroup(jrElement);
			else
				((JRDesignElementGroup) jrGroup).addElementGroup(elementPosition, jrElement);
		} else if (jrGroup instanceof JRDesignFrame) {
			if (elementPosition > ((JRDesignFrame) jrGroup).getChildren().size())
				((JRDesignFrame) jrGroup).addElementGroup(jrElement);
			else
				((JRDesignFrame) jrGroup).addElementGroup(elementPosition, jrElement);
		}
	}
}
