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
package com.jaspersoft.studio.components.table;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRChild;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.column.MColumn;
import com.jaspersoft.studio.components.table.util.TableColumnSize;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

public class TableManager {
	private StandardTable table;
	private TableUtil tableUtil;
	private TableMatrix mh = new TableMatrix();
	private JasperDesign jDesign;

	public TableMatrix getMatrixHelper() {
		return mh;
	}

	public Rectangle getYhcolumn(int type, String grName, BaseColumn bc) {
		return mh.getYHColumn(bc, type, Misc.nvl(grName));
	}

	public TableManager(JRDesignComponentElement tbl, JasperDesign jasperDesign) {
		this((StandardTable) tbl.getComponent(), jasperDesign);
	}

	public TableManager(StandardTable table, JasperDesign jDesign) {
		this.table = table;
		this.jDesign = jDesign;
		this.tableUtil = new TableUtil(table, jDesign);

		initMaps();

		tableUtil.init(table);
		setSize();
	}

	public void initMaps() {
		mh = new TableMatrix();
		mh.fillMatrix(table, tableUtil);
		// mh.print();
	}

	public void refresh() {
		tableUtil.refresh();
		setSize();
	}

	private Dimension size;

	public Dimension getSize() {
		return size;
	}

	public void setSize() {
		int xmin = 0;
		int xmax = 0;
		int ymin = 0;
		int ymax = 0;
		for (java.awt.Rectangle r : tableUtil.getCellBounds().values()) {
			if (xmin > r.x)
				xmin = r.x;
			if (xmax < r.x + r.width)
				xmax = r.x + r.width;
			if (ymin > r.y)
				ymin = r.y;
			if (ymax < r.y + r.height)
				ymax = r.y + r.height;
		}
		size = new Dimension(xmax - xmin, ymax - ymin);
	}

	public Cell getCell(Point location) {
		Map<Cell, java.awt.Rectangle> cellBounds = tableUtil.getCellBounds();
		for (Cell cell : cellBounds.keySet()) {
			Rectangle r = getAWT2SWTRectangle(cellBounds.get(cell));
			if (r.x <= location.x && r.x + r.width >= location.x
					&& r.y <= location.y && r.y + r.height >= location.y)
				return cell;
		}
		return null;
	}

	public Rectangle getCellBounds(Cell cell) {
		return getAWT2SWTRectangle(tableUtil.getCellBounds().get(cell));
	}

	public static Rectangle getAWT2SWTRectangle(java.awt.Rectangle rect) {
		if (rect != null)
			return new Rectangle(rect.x, rect.y, rect.width, rect.height);
		return null;
	}

	public static List<BaseColumn> getAllColumns(MTable mTable) {
		JRDesignComponentElement tbl = (JRDesignComponentElement) mTable
				.getValue();
		if (tbl.getComponent() instanceof StandardTable) {
			StandardTable table = (StandardTable) tbl.getComponent();
			return TableUtil.getAllColumns(table.getColumns());
		}
		return new ArrayList<BaseColumn>(0);
	}

	public Rectangle getBounds(StandardBaseColumn col, int type, String grName) {
		Rectangle p = mh.getYHColumn(col, type, grName);
		if (p == null)
			p = new Rectangle(0, 100, col.getWidth(), 0);
		return p;
	}

	public Rectangle getBounds(int width, Cell cell, StandardBaseColumn col) {
		Rectangle b = getAWT2SWTRectangle(tableUtil.getCellBounds().get(cell));
		if (b != null)
			return b;
		int w = col != null ? col.getWidth() : 0;
		int h = cell != null ? cell.getHeight() : 0;
		return new Rectangle(0, 0, w, h);
	}

	public void setWidth(StandardBaseColumn cell, int width) {
		if (width >= 0) {
			int delta = width - cell.getWidth();
			if (cell instanceof StandardColumn)
				setColumnWidth(table.getColumns(), cell, delta);
			else if (cell instanceof StandardColumnGroup)
				setColumnGroupWidth((StandardColumnGroup) cell, delta);
			cell.setWidth(width);
		}
	}

	private boolean setColumnGroupWidth(StandardColumnGroup cell, int delta) {
		List<BaseColumn> columns = cell.getColumns();
		for (ListIterator<BaseColumn> it = columns.listIterator(columns.size()); it
				.hasPrevious();) {
			StandardBaseColumn bc = (StandardBaseColumn) it.previous();
			boolean c = false;
			if (delta < 0 && Math.abs(delta) > bc.getWidth()) {
				delta += bc.getWidth();
				bc.setWidth(0);
				c = true;
			} else
				bc.setWidth(bc.getWidth() + delta);
			if (bc instanceof StandardColumnGroup)
				setColumnGroupWidth((StandardColumnGroup) bc, delta);
			if (!c)
				return true;
		}
		return false;
	}

	private boolean setColumnWidth(List<BaseColumn> col,
			StandardBaseColumn cell, int delta) {
		for (BaseColumn bc : col) {
			if (bc instanceof StandardColumnGroup) {
				StandardColumnGroup scg = (StandardColumnGroup) bc;
				boolean b = setColumnWidth(scg.getColumns(), cell, delta);
				if (b) {
					scg.setWidth(scg.getWidth() + delta);
					return true;
				}
			}
			if (cell == bc)
				return true;
		}
		return false;
	}

	public void setHeight(DesignCell cell, int height, BaseColumn col,
			int type, String grName) {
		if (height >= 0) {
			int delta = 0;
			if (cell == null)
				delta = height - getYhcolumn(type, grName, col).height;
			else
				delta = height - cell.getHeight();
			if (delta != 0)
				setRowHeight(table.getColumns(), delta, type, grName, col);
		}
	}

	private int setRowHeight(List<BaseColumn> columns, int delta, int type,
			String grName, BaseColumn col) {
		int dif = 0;
		for (BaseColumn bc : columns) {
			if (bc instanceof StandardColumn)
				dif = TableColumnSize.setCellHeightDelta(bc, type, grName,
						delta);
			else if (bc instanceof StandardColumnGroup) {
				if (col == bc) {
					TableColumnSize.setCellHeightDelta(bc, type, grName, delta);
				} else {
					dif = setRowHeight(((StandardColumnGroup) bc).getColumns(),
							delta, type, grName, col);
					if (delta < 0 && dif != 0)
						dif = TableColumnSize.setCellHeightDelta(bc, type,
								grName, dif);
				}
			}
		}
		return dif;
	}

	public void setRowsHeight(List<Integer> d) {
		int i = 0;
		List<?> groupsList = TableUtil.getGroupList(table, jDesign);
		setRowHeight(table.getColumns(), TableUtil.TABLE_HEADER, "", d.get(i++));
		setRowHeight(table.getColumns(), TableUtil.COLUMN_HEADER, "",
				d.get(i++));
		setRowHeight(table.getColumns(), TableUtil.COLUMN_DETAIL, "",
				d.get(i++));
		setRowHeight(table.getColumns(), TableUtil.COLUMN_FOOTER, "",
				d.get(i++));
		setRowHeight(table.getColumns(), TableUtil.TABLE_FOOTER, "", d.get(i++));
		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.next();
				setRowHeight(table.getColumns(), TableUtil.COLUMN_GROUP_HEADER,
						jrGroup.getName(), d.get(i++));
				setRowHeight(table.getColumns(), TableUtil.COLUMN_GROUP_FOOTER,
						jrGroup.getName(), d.get(i++));
			}
	}

	public void setRowHeight(List<BaseColumn> cols, int type, String grName,
			int h) {
		for (BaseColumn bc : cols) {
			ColumnCell cc = mh.getColumnCell(new ColumnCell(type, grName, bc));
			Guide g = mh.getRowGuide(cc);
			List<ColumnCell> cells = isBottomOfTable(type) ? g.getPrev() : g
					.getNext();
			for (ColumnCell c : cells) {
				setRowHeight(cols, h, type, grName, c.column);
				return;
			}
		}
	}

	public void setColumnHeight(BaseColumn bc, List<Integer> deltas) {
		int i = 0;
		setCellHeight(bc, TableUtil.TABLE_HEADER, "", deltas.get(i++));
		setCellHeight(bc, TableUtil.COLUMN_HEADER, "", deltas.get(i++));
		setCellHeight(bc, TableUtil.COLUMN_DETAIL, "", deltas.get(i++));
		setCellHeight(bc, TableUtil.COLUMN_FOOTER, "", deltas.get(i++));
		setCellHeight(bc, TableUtil.TABLE_FOOTER, "", deltas.get(i++));
		List<?> groupsList = TableUtil.getGroupList(table, jDesign);
		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRDesignGroup jrGroup = (JRDesignGroup) it.next();
				setCellHeight(bc, TableUtil.COLUMN_GROUP_HEADER,
						jrGroup.getName(), deltas.get(i++));
				setCellHeight(bc, TableUtil.COLUMN_GROUP_FOOTER,
						jrGroup.getName(), deltas.get(i++));
			}
	}

	public void setCellHeight(BaseColumn bc, int type, String grName, int delta) {
		DesignCell c = (DesignCell) TableUtil.getCell(bc, type, grName);
		if (c != null)
			c.setHeight(c.getHeight() + delta);
	}

	public static StandardTable getTable(ANode destNode) {
		if (destNode instanceof MTable)
			return ((MTable) destNode).getStandardTable();
		if (destNode instanceof MColumn)
			destNode = ((MColumn) destNode).getMTable();
		if (destNode.getValue() instanceof JRDesignComponentElement) {
			JRDesignComponentElement tbl = (JRDesignComponentElement) destNode
					.getValue();
			if (tbl.getComponent() instanceof StandardTable) {
				return (StandardTable) tbl.getComponent();
			}
		}
		return null;
	}

	public Dimension getCellPackSize(ColumnCell cc) {
		cc = mh.getColumnCell(cc);
		Guide g = cc.getEast();
		int w = -g.getY();
		for (ColumnCell c : g.getPrev()) {
			Cell cell = TableUtil.getCell(c.column, c.type, c.grName);
			if (cell != null) {
				List<JRChild> cells = cell.getChildren();
				if (!cells.isEmpty()) {
					int width = ModelUtils.getContainerSize(cells,
							new Dimension(0, 0)).width;

					w = Math.max(w, width - c.column.getWidth());
				}
			}
		}
		g = cc.getSouth();
		int h = -g.getY();
		for (ColumnCell c : g.getPrev()) {
			Cell cell = TableUtil.getCell(c.column, c.type, c.grName);
			if (cell != null) {
				List<JRChild> cells = cell.getChildren();
				if (!cells.isEmpty()) {
					int height = ModelUtils.getContainerSize(cells,
							new Dimension(0, 0)).height;

					h = Math.max(h, height - cell.getHeight());
				}
			}
		}
		Rectangle b = cc.getBounds();
		return new Dimension(b.width + w, b.height + h);
	}

	public void update() {
		initMaps();
		refresh();
	}

	public int getRowHeight(MColumn col) {
		return 100;
	}

	public int getRowHeight(ColumnCell ccel) {
		return mh.getRowHeight(mh.getColumnCell(ccel));
	}

	public static boolean isBottomOfTable(int type) {
		return type == TableUtil.COLUMN_FOOTER
				|| type == TableUtil.TABLE_FOOTER
				|| type == TableUtil.COLUMN_GROUP_FOOTER;
	}
}
