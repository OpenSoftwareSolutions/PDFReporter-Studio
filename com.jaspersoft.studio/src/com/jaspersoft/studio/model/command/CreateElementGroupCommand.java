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
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class CreateElementGroupCommand extends Command {
	
	/** The jr element. */
	private JRDesignElementGroup jrElement;
	
	/** The jr group. */
	private JRElementGroup jrGroup;
	
	/** The index. */
	private int index;

	/**
	 * Instantiates a new creates the element group command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 * @param index
	 *          the index
	 */
	public CreateElementGroupCommand(ANode destNode, MElementGroup srcNode, int index) {
		this.jrElement = (JRDesignElementGroup) srcNode.getValue();
		if (jrElement == null)
			jrElement = srcNode.createJRElementGroup();
		this.jrGroup = (JRElementGroup) destNode.getValue();
		this.index = index;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrElement != null) {
			// set on JRElementGroup ...
			if (index < 0)
				index = jrGroup.getChildren().size();
			if (jrGroup instanceof JRDesignElementGroup) {
				if (index < 0 || index > jrGroup.getChildren().size())
					((JRDesignElementGroup) jrGroup).addElementGroup(jrElement);
				else
					((JRDesignElementGroup) jrGroup).addElementGroup(index, jrElement);
			} else if (jrGroup instanceof JRDesignFrame) {
				if (index < 0 || index > jrGroup.getChildren().size())
					((JRDesignFrame) jrGroup).addElementGroup(jrElement);
				else
					((JRDesignFrame) jrGroup).addElementGroup(index, jrElement);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrGroup == null || jrElement == null)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (jrGroup instanceof JRDesignElementGroup)
			((JRDesignElementGroup) jrGroup).removeElementGroup(jrElement);
		else if (jrGroup instanceof JRDesignFrame)
			((JRDesignFrame) jrGroup).removeElementGroup(jrElement);
	}
}
