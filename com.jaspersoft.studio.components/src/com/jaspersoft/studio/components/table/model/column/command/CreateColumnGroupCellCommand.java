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
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardGroupCell;

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
 * Create a cell for an MColoumnGrop
 * 
 * @author Orlandin Marco
 *
 */
public class CreateColumnGroupCellCommand extends JSSCompoundCommand {

	private StandardBaseColumn jrColumn;
	private Class<ANode> type;
	private String groupName;
	private Cell jrCell;
	private int height = 0;
	private List<INode> columns;
	private ANode parent;

	@SuppressWarnings("unchecked")
	public CreateColumnGroupCellCommand(ANode parent, MColumnGroup srcNode) {
		super(parent);
		columns = srcNode.getChildren();
		type = (Class<ANode>) parent.getClass();
		this.parent = parent;
		if (parent instanceof MTableGroupHeader){
			groupName = ((MTableGroupHeader) parent).getJrDesignGroup().getName();
		}
		if (parent instanceof MTableGroupFooter){
			groupName = ((MTableGroupFooter) parent).getJrDesignGroup().getName();
		}
		this.jrColumn = (StandardBaseColumn) srcNode.getValue();
		height = searchSuggestedHeight(srcNode.getParent());
		if (height == -1){
			//It's the first added group cell in the row,  need to increase space for 
			//all the cells outside this group
			height = MColumnGroup.DEFAULT_CELL_HEIGHT;
			setCellToIncrease(getRow(srcNode).getChildren(), srcNode, height);
		} else {
			//Need to decrease the cell height inside this group if they are bigger enough
			//otherwise we take the value most closer to the height
			height = getMinCellHeight(columns, height);
			setCellHeightDelta(columns, -height);
		}
	}
	
	private int getGroupIndex(ANode groupNode){
		if (groupNode instanceof MTableGroupHeader){
			int startIndex = -1;
			for(INode node : groupNode.getParent().getChildren()){
				if (startIndex == -1 && node instanceof MTableGroupHeader){
					startIndex = 0;
				}
				if (node == groupNode) break;
				else if (startIndex > -1) startIndex++;
			}
			return startIndex;
		} else if (groupNode instanceof MTableGroupFooter){
			int startIndex = -1;
			for(INode node : groupNode.getParent().getChildren()){
				if (startIndex == -1 && node instanceof MTableGroupFooter){
					startIndex = 0;
				}
				if (node == groupNode) break;
				else if (startIndex > -1) startIndex++;
			}
			return startIndex;
		}
		return -1;
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
	 * Generate recursively a series of commands to add a delta to the height of 
	 * every MCell found, except all the cell descendant of an excluded ancestor
	 * 
	 * @param children the current children
	 * @param exclusion the excluded ancestor
	 * @param newHeightDelta the delta to apply to the height (can be positive or negative)
	 */
	private void setCellToIncrease(List<INode> children, MColumnGroup exclusion, int newHeightDelta){
		for(INode child : children){
			if (child != exclusion){
				if (child.getClass().equals(MCell.class)){
					add(new AddCellDeltaHeightCommand((MCell)child, newHeightDelta));
				}
				setCellToIncrease(child.getChildren(), exclusion, newHeightDelta);
			}
		}
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
	
	/**
	 * If the commands has size zero it still can execute because this means that
	 * there are no resize of other cell needed, table with one column for example
	 */
	public boolean canExecute() {
		return getCommands().size() == 0 || super.canExecute();
	};
	
	/**
	 * Recursively search the minimum valid value to resize all the cell, to avoid to
	 * set a minimum height
	 * 
	 * @param children the analyzed children
	 * @param actualMin the comparison min
	 * @return the safe min value
	 */
	private int getMinCellHeight(List<INode> children, int actualMin){
		for(INode child : children){
			if (child.getClass().equals(MCell.class)){
				int cellHehight = ((MCell)child).getCell().getHeight();
				if (actualMin > cellHehight) actualMin = cellHehight;
			}
			int recursiveMin = getMinCellHeight(child.getChildren(), actualMin);
			if (recursiveMin<actualMin) return recursiveMin;
		}
		return actualMin;
	}
	
	/**
	 * Search if there are other group cells on the same level and at the first found the height is 
	 * returned. If it didn't find any group cell it return -1
	 * 
	 * @param parent the parent, the search of the group cell will be inside its children
	 * @return the height of the first group cell of -1 if no one can be found inside the children 
	 * of the parent
	 */
	private int searchSuggestedHeight(ANode parent){
		for(INode child : parent.getChildren()){
			if (child instanceof MColumnGroupCell) {
				MColumnGroupCell groupCell = (MColumnGroupCell)child;
				if (groupCell.getCell() != null)
					return groupCell.getCell().getHeight();
			}
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrCell == null) {
			jrCell = createCell();
		}
		if (type.isAssignableFrom(MTableHeader.class))
			jrColumn.setTableHeader(jrCell);
		else if (type.isAssignableFrom(MTableFooter.class))
			jrColumn.setTableFooter(jrCell);
		else if (type.isAssignableFrom(MTableColumnHeader.class))
			jrColumn.setColumnHeader(jrCell);
		else if (type.isAssignableFrom(MTableColumnFooter.class))
			jrColumn.setColumnFooter(jrCell);

		else if (type.isAssignableFrom(MTableGroupHeader.class)){
			int groupIndex = getGroupIndex(parent);
			List<GroupCell> groupHeaders = jrColumn.getGroupHeaders();
			if (groupIndex != -1 && groupHeaders.size()> groupIndex){
				StandardGroupCell groupCell = new StandardGroupCell(groupName, jrCell);
				groupHeaders.add(groupIndex, groupCell);
				jrColumn.getEventSupport().fireCollectionElementAddedEvent(StandardBaseColumn.PROPERTY_GROUP_HEADERS, groupCell, groupIndex);
			} else {
				jrColumn.setGroupHeader(groupName, jrCell);
			}

		}
		else if (type.isAssignableFrom(MTableGroupFooter.class)){
			int groupIndex = getGroupIndex(parent);
			List<GroupCell> groupFooters = jrColumn.getGroupFooters();
			if (groupIndex != -1 && groupFooters.size()>groupIndex){
				StandardGroupCell groupCell = new StandardGroupCell(groupName, jrCell);
				groupFooters.add(groupIndex, groupCell);
				jrColumn.getEventSupport().fireCollectionElementAddedEvent(StandardBaseColumn.PROPERTY_GROUP_FOOTERS, groupCell, groupIndex);
			} else {
				jrColumn.setGroupFooter(groupName, jrCell);
			}
		}
		
		super.execute();
	}

	protected Cell createCell() {
		DesignCell cell = new DesignCell();
		cell.setHeight(height);
		return cell;
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
			jrColumn.setTableHeader(null);
		else if (type.isAssignableFrom(MTableFooter.class))
			jrColumn.setTableFooter(null);
		else if (type.isAssignableFrom(MTableColumnHeader.class))
			jrColumn.setColumnHeader(null);
		else if (type.isAssignableFrom(MTableColumnFooter.class))
			jrColumn.setColumnFooter(null);

		else if (type.isAssignableFrom(MTableGroupHeader.class))
			jrColumn.setGroupHeader(groupName, null);
		else if (type.isAssignableFrom(MTableGroupFooter.class))
			jrColumn.setGroupFooter(groupName, null);
		super.undo();
	}
}
