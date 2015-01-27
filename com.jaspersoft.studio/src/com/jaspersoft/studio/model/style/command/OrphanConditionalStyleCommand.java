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
/*/*
 * The Class OrphanConditionalStyleCommand.
 * 
 * @author Chicu Veaceslav
 */
public class OrphanConditionalStyleCommand extends Command {
	
	/** The index. */
	private int index;
	
	/** The jr conditional style. */
	private JRDesignConditionalStyle jrConditionalStyle;
	
	/** The jr style. */
	private JRDesignStyle jrStyle;

	/**
	 * Instantiates a new orphan conditional style command.
	 * 
	 * @param parent
	 *          the parent
	 * @param child
	 *          the child
	 */
	public OrphanConditionalStyleCommand(MStyle parent, MConditionalStyle child) {
		super(Messages.common_orphan_child);
		this.jrStyle = (JRDesignStyle) parent.getValue();
		this.jrConditionalStyle = (JRDesignConditionalStyle) child.getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		index = jrStyle.getConditionalStyleList().indexOf(jrConditionalStyle);
		jrStyle.removeConditionalStyle(jrConditionalStyle);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		if (index < 0 || index > jrStyle.getConditionalStyleList().size())
			jrStyle.addConditionalStyle(jrConditionalStyle);
		else
			jrStyle.addConditionalStyle(index, jrConditionalStyle);
	}

}
