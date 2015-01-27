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
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;
import com.jaspersoft.studio.components.table.util.TableColumnSize;
/*
 * link nodes & together.
 * 
 * @author Chicu Veaceslav
 */
public class DeleteColumnFromGroupCommand extends Command {

	private StandardColumnGroup jrGroup;
	private StandardBaseColumn jrColumn;
	private StandardTable jrTable;

	/** The element position. */
	private int elementPosition = 0;

	public DeleteColumnFromGroupCommand(MColumnGroup destNode, MColumn srcNode) {
		super();
		this.jrGroup = (StandardColumnGroup) destNode.getValue();
		this.jrColumn = (StandardBaseColumn) srcNode.getValue();
		this.jrTable = TableManager.getTable(destNode.getMTable());
		elementPosition = jrGroup.getColumns().indexOf(jrColumn);
	}

	public DeleteColumnFromGroupCommand(MColumnGroupCell destNode, MColumn srcNode) {
		super();
		this.jrGroup = (StandardColumnGroup) destNode.getValue();
		this.jrColumn = (StandardBaseColumn) srcNode.getValue();
		this.jrTable = TableManager.getTable(destNode.getMTable());
		elementPosition = jrGroup.getColumns().indexOf(jrColumn);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		jrGroup.removeColumn(jrColumn);
		TableColumnSize.setGroupWidth2Top(jrTable.getColumns(), jrGroup, -jrColumn.getWidth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		if (jrGroup == null || jrColumn == null)
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
		if (elementPosition < 0 || elementPosition > jrGroup.getColumns().size())
			jrGroup.addColumn(jrColumn);
		else
			jrGroup.addColumn(elementPosition, jrColumn);
		jrGroup.setWidth(jrGroup.getWidth() + jrColumn.getWidth());
		TableColumnSize.setGroupWidth2Top(jrTable.getColumns(), jrGroup, jrColumn.getWidth());
	}

}
