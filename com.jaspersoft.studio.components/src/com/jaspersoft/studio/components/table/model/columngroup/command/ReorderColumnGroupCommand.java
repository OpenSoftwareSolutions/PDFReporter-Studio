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
package com.jaspersoft.studio.components.table.model.columngroup.command;

import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;

/*
 * The Class ReorderParameterCommand.
 */
public class ReorderColumnGroupCommand extends Command {

	private int oldIndex, newIndex;

	private StandardBaseColumn jrColumn;

	private StandardColumnGroup jrGroup;

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
	public ReorderColumnGroupCommand(MColumn child, MColumnGroup parent,
			int newIndex) {
		super(Messages.ReorderColumnGroupCommand_reorder_column_group);
		this.newIndex = Math.max(0, newIndex);
		this.jrGroup = (StandardColumnGroup) parent.getValue();
		this.jrColumn = (StandardBaseColumn) child.getValue();
	}

	public ReorderColumnGroupCommand(MColumn child, MColumnGroupCell parent,
			int newIndex) {
		super(Messages.ReorderColumnGroupCommand_reorder_column_group);
		this.newIndex = newIndex;
		this.jrGroup = (StandardColumnGroup) parent.getValue();
		this.jrColumn = child.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		oldIndex = jrGroup.getColumns().indexOf(jrColumn);

		jrGroup.removeColumn(jrColumn);
		if (newIndex >= 0 && newIndex < jrGroup.getColumns().size())
			jrGroup.addColumn(newIndex, jrColumn);
		else
			jrGroup.addColumn(jrColumn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		jrGroup.removeColumn(jrColumn);
		if (oldIndex >= 0 && oldIndex < jrGroup.getColumns().size())
			jrGroup.addColumn(oldIndex, jrColumn);
		else
			jrGroup.addColumn(jrColumn);

	}
}
