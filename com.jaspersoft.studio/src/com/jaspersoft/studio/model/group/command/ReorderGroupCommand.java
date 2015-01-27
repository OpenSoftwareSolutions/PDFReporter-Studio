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
package com.jaspersoft.studio.model.group.command;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.group.MGroup;
import com.jaspersoft.studio.model.group.MGroups;
/*/*
 * The Class ReorderGroupCommand.
 */
public class ReorderGroupCommand extends Command {
	
	/** The new index. */
	private int oldIndex, newIndex;
	
	/** The jr group. */
	private JRDesignGroup jrGroup;
	
	/** The jr dataset. */
	private JRDesignDataset jrDataset;

	/**
	 * Instantiates a new reorder group command.
	 * 
	 * @param child
	 *          the child
	 * @param parent
	 *          the parent
	 * @param newIndex
	 *          the new index
	 */
	public ReorderGroupCommand(MGroup child, MGroups parent, int newIndex) {
		super(Messages.common_reorder_elements);

		this.newIndex = Math.max(0, newIndex);
		this.jrDataset = (JRDesignDataset) parent.getValue();
		this.jrGroup = (JRDesignGroup) child.getValue();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		try {
			oldIndex = jrDataset.getFieldsList().indexOf(jrGroup);
			jrDataset.removeGroup(jrGroup);
			if (newIndex < 0 || newIndex > jrDataset.getGroupsList().size())
				jrDataset.addGroup(jrGroup);
			else
				jrDataset.addGroup(newIndex, jrGroup);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		try {
			jrDataset.removeGroup(jrGroup);
			if (oldIndex < 0 || oldIndex > jrDataset.getGroupsList().size())
				jrDataset.addGroup(jrGroup);
			else
				jrDataset.addGroup(oldIndex, jrGroup);
		} catch (JRException e) {
			e.printStackTrace();
		}
	}
}
