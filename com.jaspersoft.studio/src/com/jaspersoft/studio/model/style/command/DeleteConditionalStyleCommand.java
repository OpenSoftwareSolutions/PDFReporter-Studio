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
package com.jaspersoft.studio.model.style.command;

import net.sf.jasperreports.engine.design.JRDesignConditionalStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteConditionalStyleCommand extends Command {
	
	/** The jr style. */
	private JRDesignStyle jrStyle;
	
	/** The jr conditional style. */
	private JRDesignConditionalStyle jrConditionalStyle;
	
	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete conditional style command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteConditionalStyleCommand(MStyle destNode, MConditionalStyle srcNode) {
		super();
		this.jrStyle = (JRDesignStyle) destNode.getValue();
		this.jrConditionalStyle = (JRDesignConditionalStyle) srcNode.getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		elementPosition = jrStyle.getConditionalStyleList().indexOf(jrConditionalStyle);
		jrStyle.removeConditionalStyle(jrConditionalStyle);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrStyle == null || jrConditionalStyle == null)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (elementPosition < 0 || elementPosition > jrStyle.getConditionalStyleList().size())
			jrStyle.addConditionalStyle(jrConditionalStyle);
		else
			jrStyle.addConditionalStyle(elementPosition, jrConditionalStyle);
	}
}
