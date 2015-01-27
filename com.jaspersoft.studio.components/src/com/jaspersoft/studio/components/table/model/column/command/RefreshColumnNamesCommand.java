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

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.util.TableColumnNumerator;
import com.jaspersoft.studio.model.ANode;

/**
 * Refresh the columns names of the model to reflect the actual structure of the table
 * 
 * @author Orlandin Marco
 *
 */
public class RefreshColumnNamesCommand extends Command{
	
	/**
	 * Parent table of the columns
	 */
	private MTable mTable;
	
	/**
	 * Flag to know if the refresh should be done on the undo operation
	 */
	private boolean renameOnUndo = false;
	
	/**
	 * Flag to know if the refresh should be done on the execute\redo operation
	 */
	private boolean renameOnExecute = false;
	
	/**
	 * Create a command to force the refresh of the columns names inside the model, to 
	 * reflect the actual structure of the table
	 * 
	 * @param tableNode a node inside the table, used to recover the MTable element
	 * @param renameOnExecute  true if the refresh should be done on the execute\redo operation, otherwise false
	 * @param renameOnUndo true if the refresh should be done on the undo operation, otherwise false
	 */
	public RefreshColumnNamesCommand(ANode tableNode, boolean renameOnExecute, boolean renameOnUndo){
		this(getTableFromNode(tableNode), renameOnExecute, renameOnUndo);
	}
	
	/**
	 * Create a command to force the refresh of the columns names inside the model, to 
	 * reflect the actual structure of the table
	 * 
	 * @param mTable the node of the table
	 * @param renameOnExecute  true if the refresh should be done on the execute\redo operation, otherwise false
	 * @param renameOnUndo true if the refresh should be done on the undo operation, otherwise false
	 */
	public RefreshColumnNamesCommand(MTable mTable, boolean renameOnExecute, boolean renameOnUndo){
		this.renameOnExecute = renameOnExecute;
		this.renameOnUndo = renameOnUndo;
		this.mTable = mTable;
	}
	
	/**
	 * Try to search a table node starting from a node (that should be 
	 * inside the table), going up trough the hierarchy
	 * 
	 * @param startingNode node inside the table
	 * @return an MTable if the passed node can bring to it, otherwise null
	 */
	private static MTable getTableFromNode(ANode startingNode){
		//Search the table node
		ANode parent = startingNode;
		while(parent != null && !(parent instanceof MTable)){
			parent = parent.getParent();
		}
		return (MTable)parent;
	}
	
	
	/**
	 * Refresh the columns names on the current table, if it was found
	 */
	private void refreshColumnNames(){
		if (mTable != null)
			TableColumnNumerator.renumerateColumnNames(mTable);
	}

	@Override
	public void execute() {
		if (renameOnExecute) refreshColumnNames();
	}
	
	@Override
	public void undo() {
		if (renameOnUndo) refreshColumnNames();
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}
	
	@Override
	public boolean canExecute() {
		return true;
	}
}
