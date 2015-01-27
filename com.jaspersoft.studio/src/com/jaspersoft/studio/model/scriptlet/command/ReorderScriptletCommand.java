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
package com.jaspersoft.studio.model.scriptlet.command;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignScriptlet;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.scriptlet.MScriptlet;
import com.jaspersoft.studio.model.scriptlet.MScriptlets;

/*
 * The Class ReorderScriptletCommand.
 */
public class ReorderScriptletCommand extends Command {

	/** The new index. */
	private int oldIndex, newIndex;

	/** The jr scriptlet. */
	private JRDesignScriptlet jrScriptlet;

	/** The jr dataset. */
	private JRDesignDataset jrDataset;

	/**
	 * Instantiates a new reorder scriptlet command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderScriptletCommand(MScriptlet child, MScriptlets parent, int newIndex) {
		super(Messages.common_reorder_elements);

		this.newIndex = Math.max(0, newIndex);
		this.jrDataset = (JRDesignDataset) parent.getValue();
		this.jrScriptlet = (JRDesignScriptlet) child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrScriptlet.getName().equals("REPORT_SCRIPTLET"))
			return;
		oldIndex = jrDataset.getScriptletsList().indexOf(jrScriptlet);
		try {
			jrDataset.removeScriptlet(jrScriptlet);
			if (newIndex < 0 || newIndex > jrDataset.getScriptletsList().size())
				jrDataset.addScriptlet(jrScriptlet);
			else
				jrDataset.addScriptlet(newIndex, jrScriptlet);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		try {
			jrDataset.removeScriptlet(jrScriptlet);
			if (oldIndex < 0 || oldIndex > jrDataset.getScriptletsList().size())
				jrDataset.addScriptlet(jrScriptlet);
			else
				jrDataset.addScriptlet(oldIndex, jrScriptlet);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
