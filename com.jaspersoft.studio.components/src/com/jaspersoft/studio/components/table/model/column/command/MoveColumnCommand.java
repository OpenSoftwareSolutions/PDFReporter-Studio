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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.ColumnCell;
import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.model.ANode;

public class MoveColumnCommand extends Command {
	private JasperDesign jDesign;
	protected int oldIndex, newIndex = -1;

	protected StandardBaseColumn srcColumn;

	private StandardColumnGroup pdestColGroup;
	protected StandardColumnGroup psrcColGroup;

	private List<Integer> deltas;

	protected StandardTable jrTable;
	protected TableManager tbManager;
	private boolean resize = true;

	public MoveColumnCommand(MColumn src, MColumn dest, boolean resize) {
		this(src, dest);
		this.resize = resize;
	}

	public MoveColumnCommand(MColumn src, ANode dest) {
		super(Messages.ReorderColumnCommand_reorder_columns);
		jDesign = src.getJasperDesign();
		tbManager = src.getMTable().getTableManager();
		jrTable = TableManager.getTable(src);
		srcColumn = src.getValue();

		ANode srcparent = src.getParent();
		if (srcparent instanceof AMCollection)
			oldIndex = jrTable.getColumns().indexOf(srcColumn);
		else if (srcparent.getValue() instanceof StandardColumnGroup) {
			psrcColGroup = (StandardColumnGroup) srcparent.getValue();
			oldIndex = psrcColGroup.getColumns().indexOf(srcColumn);
		}
		if (dest == null)
			return;
		ANode destparent = dest.getParent();
		if (dest instanceof MColumnGroup) {
			pdestColGroup = (StandardColumnGroup) dest.getValue();
		} else if (destparent != null) {
			if (destparent instanceof AMCollection) {
				newIndex = jrTable.getColumns().indexOf(dest.getValue());
			} else if (destparent.getValue() instanceof StandardColumnGroup) {
				pdestColGroup = (StandardColumnGroup) destparent.getValue();
				newIndex = pdestColGroup.getColumns().indexOf(dest.getValue());
			}
		} 
	}
	
	public void setNewIndex(int index){
		newIndex = index;
	}

	@Override
	public void execute() {
		if (resize && pdestColGroup != null && deltas == null)
			getDeltas(tbManager);

		delColumn(psrcColGroup, srcColumn);
		addColumn(pdestColGroup, newIndex, srcColumn);
		if (resize) {
			tbManager.initMaps();
			if (pdestColGroup != null) {
				tbManager.setRowsHeight(deltas);
				setMinusDelta();
				tbManager.setColumnHeight(srcColumn, deltas);
			}
		}
		tbManager.refresh();
	}

	@Override
	public void undo() {
		if (resize)
			tbManager.initMaps();
		delColumn(pdestColGroup, srcColumn);
		addColumn(psrcColGroup, oldIndex, srcColumn);
		if (resize && pdestColGroup != null) {
			tbManager.setRowsHeight(deltas);
			setMinusDelta();
			tbManager.setColumnHeight(srcColumn, deltas);
		}
		tbManager.refresh();
	}

	private void setMinusDelta() {
		for (int i = 0; i < deltas.size(); i++)
			deltas.set(i, -deltas.get(i));
	}

	protected void delColumn(StandardColumnGroup colGroup, StandardBaseColumn col) {
		if (colGroup != null) {
			colGroup.removeColumn(col);
			updateColumnWidth();
		} else
			jrTable.removeColumn(col);
	}

	protected void addColumn(StandardColumnGroup colGroup, int index, StandardBaseColumn col) {
		if (colGroup != null) {
			if (index >= 0 && index < colGroup.getColumns().size())
				colGroup.addColumn(index, col);
			else
				colGroup.addColumn(col);
			updateColumnWidth();
		} else {
			if (index >= 0 && index < jrTable.getColumns().size())
				jrTable.addColumn(index, col);
			else
				jrTable.addColumn(col);
		}
	}

	private void updateColumnWidth() {
		for (BaseColumn bc : jrTable.getColumns())
			if (bc instanceof StandardColumnGroup)
				fixWidth((StandardColumnGroup) bc);
	}

	private int fixWidth(StandardColumnGroup group) {
		int w = 0;
		for (BaseColumn bc : group.getColumns()) {
			if (bc instanceof StandardColumnGroup) {
				int gw = fixWidth((StandardColumnGroup) bc);
				if (gw != bc.getWidth())
					((StandardColumnGroup) bc).setWidth(gw);
			}
			w += bc.getWidth();
		}
		return w;
	}

	private void getDeltas(TableManager tb) {
		deltas = new ArrayList<Integer>();
		addDelta(tb, TableUtil.TABLE_HEADER, "");
		addDelta(tb, TableUtil.COLUMN_HEADER, "");
		addDelta(tb, TableUtil.COLUMN_DETAIL, "");
		addDelta(tb, TableUtil.COLUMN_FOOTER, "");
		addDelta(tb, TableUtil.TABLE_FOOTER, "");
		List<?> groupsList = TableUtil.getGroupList(jrTable, jDesign);
		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.next();
				addDelta(tb, TableUtil.COLUMN_GROUP_HEADER, jrGroup.getName());
				addDelta(tb, TableUtil.COLUMN_GROUP_FOOTER, jrGroup.getName());
			}
	}

	public void addDelta(TableManager tb, int type, String grName) {
		int srch = tb.getRowHeight(new ColumnCell(type, grName, srcColumn));
		int dsth = tb.getRowHeight(new ColumnCell(type, grName, pdestColGroup));
		if (type != TableUtil.COLUMN_DETAIL)
			dsth -= tb.getYhcolumn(type, grName, pdestColGroup).height;
		else
			dsth = srch;
		deltas.add(srch - dsth);
	}

}
