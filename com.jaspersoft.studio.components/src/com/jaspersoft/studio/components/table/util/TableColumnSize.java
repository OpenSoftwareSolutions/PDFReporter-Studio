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
package com.jaspersoft.studio.components.table.util;

import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;

import com.jaspersoft.studio.components.table.model.MTableColumnFooter;
import com.jaspersoft.studio.components.table.model.MTableColumnHeader;
import com.jaspersoft.studio.components.table.model.MTableDetail;
import com.jaspersoft.studio.components.table.model.MTableFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupHeader;
import com.jaspersoft.studio.components.table.model.MTableHeader;

public class TableColumnSize {

	public static int getType(Class<?> c) {
		if (c.isAssignableFrom(MTableHeader.class))
			return TableUtil.TABLE_HEADER;
		if (c.isAssignableFrom(MTableFooter.class))
			return TableUtil.TABLE_FOOTER;
		if (c.isAssignableFrom(MTableColumnHeader.class))
			return TableUtil.COLUMN_HEADER;
		if (c.isAssignableFrom(MTableColumnFooter.class))
			return TableUtil.COLUMN_FOOTER;
		if (c.isAssignableFrom(MTableDetail.class))
			return TableUtil.COLUMN_DETAIL;
		if (c.isAssignableFrom(MTableGroupHeader.class))
			return TableUtil.COLUMN_GROUP_HEADER;
		if (c.isAssignableFrom(MTableGroupFooter.class))
			return TableUtil.COLUMN_GROUP_FOOTER;
		return -1;
	}

	public static int getInitGroupHeight(StandardTable jrTable,
			StandardColumnGroup jrGroup, int type, String grName) {
		int maxh = 0;
		if (jrGroup.getColumns().isEmpty()) {
			maxh = getInitTableHeight(jrTable, type, grName)
					- getGroupHeigh2Top(jrTable.getColumns(), jrGroup, type,
							grName);
		} else
			for (BaseColumn bc : jrGroup.getColumns()) {
				maxh = Math.max(maxh, getColumnHeight(bc, type, grName));
			}
		return maxh <= 0 ? 40 : maxh;
	}

	private static int getGroupHeigh2Top(List<BaseColumn> cols,
			StandardColumnGroup scg, int type, String grName) {
		for (BaseColumn bc : cols) {
			if (bc == scg) {
				return Math.max(0, getCellHeight(bc, type, grName));
			}
			if (bc instanceof StandardColumnGroup) {
				int hg = getGroupHeigh2Top(
						((StandardColumnGroup) bc).getColumns(), scg, type,
						grName);
				if (hg >= 0) {
					return Math.max(0, getCellHeight(bc, type, grName)) + hg;
				}
			}
		}
		return -1;
	}

	public static int setGroupWidth2Top(List<BaseColumn> cols,
			StandardColumnGroup scg, int delta) {
		for (BaseColumn bc : cols) {
			if (bc == scg) {
				((StandardBaseColumn) bc).setWidth(bc.getWidth() + delta);
				return 0;
			}
			if (bc instanceof StandardColumnGroup) {
				int hg = setGroupWidth2Top(
						((StandardColumnGroup) bc).getColumns(), scg, delta);
				if (hg == 0) {
					((StandardBaseColumn) bc).setWidth(bc.getWidth() + delta);
					return 0;
				}
			}
		}
		return -1;
	}

	public static int getInitTableHeight(StandardTable jrTable, int type,
			String grName) {
		int maxh = -1;
		for (BaseColumn bc : jrTable.getColumns()) {
			maxh = Math.max(maxh, getColumnHeight(bc, type, grName));
		}
		return maxh < 0 ? 30 : maxh;
	}

	public static int getColumnHeight(BaseColumn bc, int type, String grName) {
		int height = -1;
		if (!(bc instanceof StandardColumnGroup && type == TableUtil.COLUMN_DETAIL))
			height = getCellHeight(bc, type, grName);

		if (bc instanceof StandardColumnGroup) {
			int maxh = -1;
			for (BaseColumn bcg : ((StandardColumnGroup) bc).getColumns()) {
				int h = getColumnHeight(bcg, type, grName);
				if (h >= 0)
					maxh = Math.max(maxh, h);
			}
			if (maxh >= 0) {
				if (height < 0)
					height = 0;
				height += maxh;
			}
		}
		return height;
	}

	private static int getCellHeight(BaseColumn bc, int type, String grName) {
		Cell cell = null;
		switch (type) {
		case TableUtil.TABLE_HEADER:
			cell = bc.getTableHeader();
			break;
		case TableUtil.TABLE_FOOTER:
			cell = bc.getTableFooter();
			break;
		case TableUtil.COLUMN_HEADER:
			cell = bc.getColumnHeader();
			break;
		case TableUtil.COLUMN_FOOTER:
			cell = bc.getColumnFooter();
			break;
		case TableUtil.COLUMN_DETAIL:
			if (bc instanceof StandardColumn)
				cell = ((StandardColumn) bc).getDetailCell();
			break;
		case TableUtil.COLUMN_GROUP_HEADER:
			cell = bc.getGroupHeader(grName);
			break;
		case TableUtil.COLUMN_GROUP_FOOTER:
			cell = bc.getGroupFooter(grName);
			break;
		}
		if (cell != null)
			return cell.getHeight();
		return -1;
	}

	public static int setCellHeightDelta(BaseColumn bc, int type,
			String grName, int delta) {
		int dif = 0;
		Cell cell = TableUtil.getCell(bc, type, grName);
		if (cell != null) {
			DesignCell designCell = (DesignCell) cell;
			int height = cell.getHeight() + delta;
			if (height < 0) {
				dif = height;
				height = 0;
			}
			designCell.setHeight(height);
		}
		return dif;
	}

}
