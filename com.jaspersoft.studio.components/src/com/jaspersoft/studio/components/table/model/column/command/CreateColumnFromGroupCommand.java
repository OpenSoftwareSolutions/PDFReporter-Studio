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

import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

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
public class CreateColumnFromGroupCommand extends Command {

	private StandardBaseColumn jrColumn;

	protected StandardColumnGroup jrGroup;
	protected StandardTable jrTable;
	protected JasperDesign jrDesign;

	private int index;

	public CreateColumnFromGroupCommand(MColumnGroup destNode, MColumn srcNode, int index) {
		super();
		this.jrGroup = (StandardColumnGroup) destNode.getValue();
		this.index = index;
		this.jrColumn = (StandardBaseColumn) srcNode.getValue();
		this.jrTable = TableManager.getTable(destNode.getMTable());
		this.jrDesign = destNode.getJasperDesign();
	}

	public CreateColumnFromGroupCommand(MColumnGroupCell destNode, MColumn srcNode, int index) {
		super();
		this.jrGroup = (StandardColumnGroup) destNode.getValue();
		this.index = index;
		this.jrColumn = (StandardBaseColumn) srcNode.getValue();
		this.jrTable = TableManager.getTable(destNode.getMTable());
		this.jrDesign = destNode.getJasperDesign();
	}

	protected StandardBaseColumn createColumn() {
		StandardColumn col = new StandardColumn();
		col.setWidth(40);

		DesignCell cell = new DesignCell();
		cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.TABLE_HEADER, null));
		col.setTableHeader(cell);

		cell = new DesignCell();
		cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.TABLE_FOOTER, null));
		col.setTableFooter(cell);

		cell = new DesignCell();
		cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.COLUMN_HEADER, null));
		col.setColumnHeader(cell);

		cell = new DesignCell();
		cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.COLUMN_FOOTER, null));
		col.setColumnFooter(cell);

		cell = new DesignCell();
		cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.COLUMN_DETAIL, null));
		col.setDetailCell(cell);

		List<?> groupsList = TableUtil.getGroupList(jrTable, jrDesign);
		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrDesignGroup = (JRDesignGroup) it.next();
				cell = new DesignCell();
				cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.COLUMN_GROUP_HEADER,
						jrDesignGroup.getName()));
				col.setGroupHeader(jrDesignGroup.getName(), cell);

				cell = new DesignCell();
				cell.setHeight(TableColumnSize.getInitGroupHeight(jrTable, jrGroup, TableUtil.COLUMN_GROUP_FOOTER,
						jrDesignGroup.getName()));
				col.setGroupFooter(jrDesignGroup.getName(), cell);
			}
		return col;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#execute()
	 */
	@Override
	public void execute() {
		if (jrColumn == null) {
			jrColumn = createColumn();
		}
		if (index >= 0 && index < jrGroup.getColumns().size())
			jrGroup.addColumn(index, jrColumn);
		else
			jrGroup.addColumn(jrColumn);
		TableColumnSize.setGroupWidth2Top(jrTable.getColumns(), jrGroup, jrColumn.getWidth());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canUndo()
	 */
	@Override
	public boolean canUndo() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#undo()
	 */
	@Override
	public void undo() {
		index = jrGroup.getColumns().indexOf(jrColumn);
		jrGroup.removeColumn(jrColumn);
		TableColumnSize.setGroupWidth2Top(jrTable.getColumns(), jrGroup, -jrColumn.getWidth());
	}
}
