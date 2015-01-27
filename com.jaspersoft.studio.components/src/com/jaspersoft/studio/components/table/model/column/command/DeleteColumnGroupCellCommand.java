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

import java.util.List;

import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.MTableColumnFooter;
import com.jaspersoft.studio.components.table.model.MTableColumnHeader;
import com.jaspersoft.studio.components.table.model.MTableFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupHeader;
import com.jaspersoft.studio.components.table.model.MTableHeader;
import com.jaspersoft.studio.components.table.model.column.MCell;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;


/**
 * Delete a cell for an MColoumnGrop
 * 
 * @author Orlandin Marco
 *
 */
public class DeleteColumnGroupCellCommand extends JSSCompoundCommand {

	private StandardBaseColumn jrColumn;
	private Class<?> type;
	private String groupName;
	private Cell jrCell;

	@SuppressWarnings("unchecked")
	public DeleteColumnGroupCellCommand(ANode parent, MColumnGroupCell srcNode) {
		super(parent);
		if (parent instanceof MColumnGroup)
			type = ((MColumnGroup) parent).getSection().getClass();
		else if (parent instanceof MColumnGroupCell)
			type = ((MColumnGroupCell) parent).getSection().getClass();
		else
			type = (Class<ANode>) parent.getClass();

		if (parent instanceof MTableGroupHeader)
			groupName = ((MTableGroupHeader) parent).getJrDesignGroup().getName();
		if (parent instanceof MTableGroupFooter)
			groupName = ((MTableGroupFooter) parent).getJrDesignGroup().getName();
		this.jrColumn = (StandardBaseColumn) srcNode.getValue();
		if (otherGroupCellOnLevel(srcNode.getParent(), srcNode)){
			setCellHeightDelta(srcNode.getChildren(), -srcNode.getCell().getHeight());
		} else {
			setCellHeightDelta(getRow(srcNode).getChildren(), -srcNode.getCell().getHeight());
		}
		add(new FixCellHeightsCommand(srcNode));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (type.isAssignableFrom(MTableHeader.class)) {
			jrCell = jrColumn.getTableHeader();
			jrColumn.setTableHeader(null);
		} else if (type.isAssignableFrom(MTableFooter.class)) {
			jrCell = jrColumn.getTableFooter();
			jrColumn.setTableFooter(null);
		} else if (type.isAssignableFrom(MTableColumnHeader.class)) {
			jrCell = jrColumn.getColumnHeader();
			jrColumn.setColumnHeader(null);
		} else if (type.isAssignableFrom(MTableColumnFooter.class)) {
			jrCell = jrColumn.getColumnFooter();
			jrColumn.setColumnFooter(null);

		} else if (type.isAssignableFrom(MTableGroupHeader.class)) {
			jrCell = jrColumn.getGroupHeader(groupName);
			jrColumn.setGroupHeader(groupName, null);
		} else if (type.isAssignableFrom(MTableGroupFooter.class)) {
			jrCell = jrColumn.getGroupFooter(groupName);
			jrColumn.setGroupFooter(groupName, null);
		}
		super.execute();
	}
	
	protected Cell createCell() {
		DesignCell cell = new DesignCell();
		cell.setHeight(10);
		return cell;
	}
	
	/**
	 * Return the Node of the entire row where the passed node is contained
	 * 
	 * @param cell the node
	 * @return the row where the node is
	 */
	private ANode getRow(ANode cell){
		ANode parent = cell.getParent();
		if (parent instanceof MTable) return cell;
		else return getRow(parent);
	}
	
	/**
	 * Search if there are other group cells on the same level
	 * 
	 * @param parent the parent, the search of the group cell will be inside its children
	 * @return true if a group cell is found, false otherwise
	 */
	private boolean otherGroupCellOnLevel(ANode parent, MColumnGroupCell srcNode){
		for(INode child : parent.getChildren()){
			if (child != srcNode && child instanceof MColumnGroupCell) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Generate recursively a series of commands to add a delta to the height of 
	 * every MCell found
	 * 
	 * @param children the current children
	 * @param newHeightDelta the delta to apply to the height (can be positive or negative)
	 */
	private void setCellHeightDelta(List<INode> children, int newHeightDelta){
		for(INode child : children){
			if (child.getClass().equals(MCell.class)){
				add(new AddCellDeltaHeightCommand((MCell)child, newHeightDelta));
			}
			setCellHeightDelta(child.getChildren(), newHeightDelta);
		}
	}
	
	@Override
	public boolean canUndo() {
		return true;
	}
	
	@Override
	public void redo() {
		execute();
	}

	@Override
	public void undo() {
		if (type.isAssignableFrom(MTableHeader.class))
			jrColumn.setTableHeader(jrCell);
		else if (type.isAssignableFrom(MTableFooter.class))
			jrColumn.setTableFooter(jrCell);
		else if (type.isAssignableFrom(MTableColumnHeader.class))
			jrColumn.setColumnHeader(jrCell);
		else if (type.isAssignableFrom(MTableColumnFooter.class))
			jrColumn.setColumnFooter(jrCell);

		else if (type.isAssignableFrom(MTableGroupHeader.class))
			jrColumn.setGroupHeader(groupName, jrCell);
		else if (type.isAssignableFrom(MTableGroupFooter.class))
			jrColumn.setGroupFooter(groupName, jrCell);

		super.undo();
	}
}
