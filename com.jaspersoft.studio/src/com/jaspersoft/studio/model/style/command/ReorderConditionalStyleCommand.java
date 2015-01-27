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

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.style.MConditionalStyle;
import com.jaspersoft.studio.model.style.MStyle;

/*
 * /* The Class ReorderConditionalStyleCommand.
 */
public class ReorderConditionalStyleCommand extends Command {

	/** The new index. */
	private int oldIndex, newIndex;

	/** The jr conditional style. */
	private JRDesignConditionalStyle jrConditionalStyle;

	/** The jr style. */
	private JRDesignStyle jrStyle;

	/**
	 * Instantiates a new reorder conditional style command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderConditionalStyleCommand(MConditionalStyle child, MStyle parent, int newIndex) {
		super(Messages.common_reorder_elements);
		this.newIndex = Math.max(0, newIndex);
		this.jrStyle = (JRDesignStyle) parent.getValue();
		this.jrConditionalStyle = (JRDesignConditionalStyle) child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = jrStyle.getConditionalStyleList().indexOf(jrConditionalStyle);

		jrStyle.removeConditionalStyle(jrConditionalStyle);
		if (newIndex < 0 || newIndex > jrStyle.getConditionalStyleList().size())
			jrStyle.addConditionalStyle(jrConditionalStyle);
		else
			jrStyle.addConditionalStyle(newIndex, jrConditionalStyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrStyle.removeConditionalStyle(jrConditionalStyle);
		if (oldIndex < 0 || oldIndex > jrStyle.getConditionalStyleList().size())
			jrStyle.addConditionalStyle(jrConditionalStyle);
		else
			jrStyle.addConditionalStyle(oldIndex, jrConditionalStyle);
	}

}
