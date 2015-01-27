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

import com.jaspersoft.studio.model.scriptlet.MScriptlet;
import com.jaspersoft.studio.model.scriptlet.MScriptlets;
/*/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteScriptletCommand extends Command {

	/** The jr dataset. */
	private JRDesignDataset jrDataset;

	/** The jr scriptlet. */
	private JRDesignScriptlet jrScriptlet;

	/** The element position. */
	private int elementPosition = 0;

	/**
	 * Instantiates a new delete scriptlet command.
	 * 
	 * @param destNode
	 *          the dest node
	 * @param srcNode
	 *          the src node
	 */
	public DeleteScriptletCommand(MScriptlets destNode, MScriptlet srcNode) {
		super();
		this.jrDataset = (JRDesignDataset) destNode.getValue();
		this.jrScriptlet = (JRDesignScriptlet) srcNode.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrScriptlet.getName().equals("REPORT_SCRIPTLET"))
			jrDataset.setScriptletClass(null);
		else {
			elementPosition = jrDataset.getScriptletsList().indexOf(jrScriptlet);
			jrDataset.removeScriptlet(jrScriptlet);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrDataset == null || jrScriptlet == null)
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
		try {
			if (jrScriptlet.getName().equals("REPORT_SCRIPTLET"))
				jrDataset.setScriptletClass(jrScriptlet.getValueClassName());
			else {
				if (elementPosition < 0 || elementPosition > jrDataset.getScriptletsList().size())
					jrDataset.addScriptlet(jrScriptlet);
				else
					jrDataset.addScriptlet(elementPosition, jrScriptlet);
			}
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
