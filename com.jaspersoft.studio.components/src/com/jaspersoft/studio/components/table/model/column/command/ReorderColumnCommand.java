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
package com.jaspersoft.studio.components.table.model.column.command;

import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardTable;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.MColumn;

/*
 * The Class ReorderParameterCommand.
 */
public class ReorderColumnCommand extends Command {

	private int oldIndex, newIndex;

	private StandardBaseColumn jrColumn;

	private StandardTable jrTable;

	/**
	 * Instantiates a new reorder parameter command.
	 * 
	 * @param child
	 *            the child
	 * @param parent
	 *            the parent
	 * @param newIndex
	 *            the new index
	 */
	public ReorderColumnCommand(MColumn child, MTable parent, int newIndex) {
		super(Messages.ReorderColumnCommand_reorder_columns);

		this.newIndex = Math.max(0, newIndex);
		this.jrTable = TableManager.getTable(parent);
		this.jrColumn = child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = jrTable.getColumns().indexOf(jrColumn);

		jrTable.removeColumn(jrColumn);
		if (newIndex >= 0 && newIndex < jrTable.getColumns().size())
			jrTable.addColumn(newIndex, jrColumn);
		else
			jrTable.addColumn(jrColumn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrTable.removeColumn(jrColumn);
		if (oldIndex >= 0 && oldIndex < jrTable.getColumns().size())
			jrTable.addColumn(oldIndex, jrColumn);
		else
			jrTable.addColumn(jrColumn);

	}
}
