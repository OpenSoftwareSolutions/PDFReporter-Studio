/*******************************************************************************
 * Copyright (C) 2010 - 2013 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, 
 * the following license terms apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jaspersoft Studio Team - initial API and implementation
 ******************************************************************************/
package com.jaspersoft.studio.components.table.model.table.command;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.GroupCell;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignGroup;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.TableComponentFactory;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.MTableDetail;
import com.jaspersoft.studio.components.table.model.MTableGroupFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupHeader;
import com.jaspersoft.studio.model.INode;

/**
 * Command to change the dataset of a table, it will also remove the old 
 * table groups and add the group defined in the new dataset
 * 
 * @author Orlandin Marco
 *
 */
public class SwitchDatasetCommand extends Command {

	/**
	 * The node of the table
	 */
	private MTable table;
	
	/**
	 * The list of the cell in the dataset group header (necessary for the undo)
	 */
	private List<GroupCell> headerCell;
	
	/**
	 * The list of the cell in the dataset group footer (necessary for the undo)
	 */
	private List<GroupCell> footerCell;
	
	/**
	 * The group node before the change
	 */
	private StandardColumnGroup oldGroup;
	
	/**
	 * The new name for the dataset
	 */
	private String newDatasetName;
	
	/**
	 * The old name for the dataset
	 */
	private String oldDatasetName;
	
	/**
	 * Create the command
	 * 
	 * @param table the table node 
	 * @param datasetName the new dataset name
	 */
	public SwitchDatasetCommand(MTable table, String datasetName){
		this.table = table;
		this.newDatasetName = datasetName;
	}
	
	/**
	 * Remove all the group node from the model and readd them according 
	 * to the groups defined on the current dataset
	 */
	private void reloadAvailableGroups(){
		MTableDetail detailNode = null;
		List<MTableGroupFooter> footerList = new ArrayList<MTableGroupFooter>();
		List<MTableGroupHeader> headerList = new ArrayList<MTableGroupHeader>();
		for(INode child : table.getChildren()){
			if (detailNode == null && child instanceof MTableDetail){
				detailNode = (MTableDetail) child;
			} else if (child instanceof MTableGroupFooter){
				MTableGroupFooter footer = (MTableGroupFooter)child;
				footerList.add(footer);
			} else if (child instanceof MTableGroupHeader){
				MTableGroupHeader header = (MTableGroupHeader)child;
				headerList.add(header);
			}
		}
		table.removeChildren(footerList);
		table.removeChildren(headerList);
		StandardTable jrTable = table.getStandardTable();
		int detailIndex = table.getChildren().indexOf(detailNode);
		List<?> groupsList = TableUtil.getGroupList(jrTable, table.getJasperDesign());
		List<MTableGroupHeader> grHeaders = new ArrayList<MTableGroupHeader>();
		List<MTableGroupFooter> grFooters = new ArrayList<MTableGroupFooter>();
		if (groupsList != null){
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.next();
				MTableGroupHeader newHeader = new MTableGroupHeader(table, (JRDesignComponentElement)table.getValue(), jrGroup, "");
				table.addChild(newHeader, detailIndex);
				grHeaders.add(newHeader);
				detailIndex++;
			}
			detailIndex++;
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.next();
				MTableGroupFooter newFooter = new MTableGroupFooter(table, (JRDesignComponentElement)table.getValue(), jrGroup, "");
				table.addChild(newFooter, detailIndex);
				grFooters.add(newFooter);
				detailIndex++;
			}
		}

		List<BaseColumn> columns = jrTable.getColumns();
		for (int i = 0; i < columns.size(); i++) {
			BaseColumn bc = columns.get(i);

			for (MTableGroupHeader mtgh : grHeaders){
				String groupName = mtgh.getJrDesignGroup().getName();
				TableComponentFactory.createCellGroupHeader(mtgh, bc, i + 1, groupName, i);
			}

			for (MTableGroupFooter mtgh : grFooters){
				String groupName = mtgh.getJrDesignGroup().getName();
				TableComponentFactory.createCellGroupFooter(mtgh, bc, i + 1, groupName, i);
			}
		}
	}
	
	/**
	 * Remove the group header and footer cells from the model
	 */
	private void removeGroup(){
		StandardTable jrTable = table.getStandardTable();
		List<BaseColumn> columns = jrTable.getColumns();
		if (columns.size() > 0 && columns.get(0) instanceof StandardColumnGroup){
			oldGroup = (StandardColumnGroup)columns.get(0);
			footerCell = new ArrayList<GroupCell>(oldGroup.getGroupFooters());
			for(GroupCell cell : footerCell)
				oldGroup.removeGroupFooter(cell);
			headerCell =  new ArrayList<GroupCell>(oldGroup.getGroupHeaders());
			for(GroupCell cell : headerCell)
				oldGroup.removeGroupHeader(cell);
		}
	}

	/**
	 * Restore the group header and footer cells on the model
	 */
	private void restoreGroup(){
		if (oldGroup != null){
			for(GroupCell cell : footerCell){
				oldGroup.addGroupFooter(cell);
			}
			for(GroupCell cell : headerCell){
				oldGroup.addGroupHeader(cell);
			}
			footerCell = null;
			headerCell = null;
			oldGroup = null;
		}
	}
	
	@Override
	public void execute() {
		removeGroup();
		JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getStandardTable().getDatasetRun();
		oldDatasetName = datasetRun.getDatasetName();
		datasetRun.setDatasetName(newDatasetName);
		reloadAvailableGroups();
		//Run an event on the table to force a grapghical refresh of the columns
		table.propertyChange(new PropertyChangeEvent(table.getValue(), StandardTable.PROPERTY_COLUMNS, null, null));
		table.setChangedProperty(true);
	}
	
	@Override
	public void undo() {
		JRDesignDatasetRun datasetRun = (JRDesignDatasetRun)table.getStandardTable().getDatasetRun();
		datasetRun.setDatasetName(oldDatasetName);
		oldDatasetName = null;
		restoreGroup();
		reloadAvailableGroups();
		//Run an event on the table to force a grapghical refresh of the columns
		table.propertyChange(new PropertyChangeEvent(table.getValue(), StandardTable.PROPERTY_COLUMNS, null, null));
		table.setChangedProperty(true);
	}
	
	@Override
	public boolean canExecute() {
		return table != null && newDatasetName != null;
	}
}
