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
package com.jaspersoft.studio.components.crosstab;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRChild;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.utils.ModelUtils;

public class CrosstabManager {

	private JRDesignCrosstab crosstab;

	public CrosstabManager(JRDesignCrosstab crosstab) {
		this.crosstab = crosstab;
		init(crosstab);
	}

	public void refresh() {
		crosstab.preprocess();
		init(crosstab);
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
		for (Rectangle r : matrix.getCells().values()) {
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

	public CrosstabCell getCell(Point location) {
		Map<CrosstabCell, Rectangle> cellmap = matrix.getCells();
		for (CrosstabCell cell : cellmap.keySet()) {
			Rectangle r = cellmap.get(cell);
			if (r.x <= location.x && r.x + r.width >= location.x && r.y <= location.y && r.y + r.height >= location.y)
				return cell;
		}
		return null;
	}

	public Rectangle getCellBounds(CrosstabCell cell) {
		return matrix.getBounds(cell);
	}

	public static int getHW(int hw) {
		return getHW(hw, 0);
	}

	public static int getHW(int hw, int def) {
		if (hw < 0)
			return def;
		return hw;
	}

	private CrosstabMatrix matrix = new CrosstabMatrix();

	public void init(JRDesignCrosstab crosstab) {
		matrix.fill(crosstab);
		setSize();
	}

	public void setCellRow(int y, String rowTotal) {
		JRCrosstabCell[][] cells = crosstab.getCells();

		for (int i = cells.length - 1; i >= 0; i--) {
			for (int j = cells[i].length - 1; j >= 0; j--) {
				JRCrosstabCell jrCrosstabCell = cells[i][j];
				if (jrCrosstabCell != null
						&& ((jrCrosstabCell.getRowTotalGroup() != null && jrCrosstabCell.getRowTotalGroup().equals(rowTotal)) || (rowTotal == null && jrCrosstabCell.getRowTotalGroup() == null))) {
					Rectangle r = matrix.getBounds(new CrosstabCell((JRDesignCellContents) jrCrosstabCell.getContents()));
					if (r != null)
						r.setLocation(r.x, y);

				}
			}
		}
	}

	public void setCellColumn(int x, String colTotal) {
		JRCrosstabCell[][] cells = crosstab.getCells();

		for (int i = cells.length - 1; i >= 0; i--) {
			for (int j = cells[i].length - 1; j >= 0; j--) {
				JRCrosstabCell jrCrosstabCell = cells[i][j];
				if (jrCrosstabCell != null
						&& ((jrCrosstabCell.getColumnTotalGroup() != null && jrCrosstabCell.getColumnTotalGroup().equals(colTotal)) || (colTotal == null && jrCrosstabCell.getColumnTotalGroup() == null))) {
					Rectangle r = matrix.getBounds(new CrosstabCell((JRDesignCellContents) jrCrosstabCell.getContents()));
					r.setLocation(x, r.y);
				}
			}
		}
	}

	public Rectangle getBounds(CrosstabCell cell) {
		return matrix.getBounds(cell);

	}

	public void setWidth(JRDesignCellContents cell, int width) {
		JRCrosstabCell[][] cells = crosstab.getCells();
		List<?> colGroupsList = crosstab.getColumnGroupsList();
		List<?> rowGroupsList = crosstab.getRowGroupsList();
		String rowGroupName = cell.getOrigin().getRowGroupName();
		switch (cell.getOrigin().getType()) {
		case JRCrosstabOrigin.TYPE_DATA_CELL:
			for (int i = cells.length - 1; i >= 0; i--) {
				for (int j = cells[i].length - 1; j >= 0; j--) {
					JRDesignCrosstabCell jrCrosstabCell = (JRDesignCrosstabCell) cells[i][j];
					if (jrCrosstabCell != null && jrCrosstabCell.getContents() == cell) {
						jrCrosstabCell.setWidth(width);

						for (int k = 0; k < cells.length; k++) {
							if (cells[k][j] != null) {
								((JRDesignCrosstabCell) cells[k][j]).setWidth(width);
							}
						}
					}
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_HEADER_CELL:
		case JRCrosstabOrigin.TYPE_COLUMN_GROUP_CROSSTAB_HEADER:
			if (!rowGroupsList.isEmpty()) {
				JRDesignCrosstabRowGroup p = (JRDesignCrosstabRowGroup) rowGroupsList.get(rowGroupsList.size() - 1);
				int delta = width - cell.getWidth();
				setCellWidth(p, p.getWidth(), p.getWidth() + delta);
			}
			break;
		case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
			for (int i = 0; i < rowGroupsList.size(); i++) {
				JRDesignCrosstabRowGroup p = (JRDesignCrosstabRowGroup) rowGroupsList.get(i);
				if (p.getName().equals(rowGroupName)) {
					setCellWidth(p, p.getWidth(), width);
					break;
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
			rowGroupsList = crosstab.getRowGroupsList();
			for (int i = 0; i < rowGroupsList.size(); i++) {
				JRDesignCrosstabRowGroup p = (JRDesignCrosstabRowGroup) rowGroupsList.get(i);
				if (p.getName().equals(rowGroupName)) {
					int delta = width - cell.getWidth();
					setCellWidth(p, p.getWidth(), width = p.getWidth() + delta);
					break;
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
			boolean calculated = false;
			String colGroupName = cell.getOrigin().getColumnGroupName();
			for (int i = cells.length - 1; i >= 0; i--) {
				for (int j = cells[i].length - 1; j >= 0; j--) {
					JRDesignCrosstabCell jrCrosstabCell = (JRDesignCrosstabCell) cells[i][j];
					if (jrCrosstabCell.getColumnTotalGroup() != null && colGroupName != null && jrCrosstabCell.getColumnTotalGroup().equals(colGroupName)) {
						if (!calculated) {
							width = jrCrosstabCell.getWidth() + width - cell.getWidth();
							calculated = true;
						}
						if (width >= 0)
							jrCrosstabCell.setWidth(width);
						else
							return;
					}
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
			colGroupName = cell.getOrigin().getColumnGroupName();
			for (int i = 0; i < colGroupsList.size(); i++) {
				JRDesignCrosstabColumnGroup rg = (JRDesignCrosstabColumnGroup) colGroupsList.get(i);
				if (rg.getName().equals(colGroupName)) {
					if (i == colGroupsList.size() - 1) {
						if (i < cells.length)
							setWidth((JRDesignCellContents) cells[cells.length - 1][cells[i].length - 1].getContents(), width);

					} else {
						int delta = width - cell.getWidth();
						JRDesignCrosstabColumnGroup rgNext = (JRDesignCrosstabColumnGroup) colGroupsList.get(i + 1);
						if (rgNext.getTotalPositionValue().equals(CrosstabTotalPositionEnum.END)) {
							JRDesignCellContents totalHeader = (JRDesignCellContents) rgNext.getTotalHeader();
							setWidth(totalHeader, totalHeader.getWidth() + delta);
						} else {
							JRDesignCellContents header = (JRDesignCellContents) rgNext.getHeader();
							setWidth(header, header.getWidth() + delta);
						}
						break;
					}
				}
			}
			break;
		}
	}

	public void setHeight(JRDesignCellContents cell, int height) {
		JRCrosstabCell[][] cells = crosstab.getCells();
		List<?> colGroupsList = crosstab.getColumnGroupsList();
		List<?> rowGroupsList = crosstab.getRowGroupsList();
		String columnGroupName = cell.getOrigin().getColumnGroupName();
		switch (cell.getOrigin().getType()) {
		case JRCrosstabOrigin.TYPE_DATA_CELL:
			if (height >= 0)
				for (int i = cells.length - 1; i >= 0; i--) {
					for (int j = cells[i].length - 1; j >= 0; j--) {
						JRDesignCrosstabCell jrCrosstabCell = (JRDesignCrosstabCell) cells[i][j];
						if (jrCrosstabCell.getContents() == cell) {
							jrCrosstabCell.setHeight(height);

							for (int k = 0; k < cells[i].length; k++) {
								if (cells[i][k] != null) {
									((JRDesignCrosstabCell) cells[i][k]).setHeight(height);
								}
							}
						}
					}
				}
			break;
		case JRCrosstabOrigin.TYPE_HEADER_CELL:
			if (!colGroupsList.isEmpty()) {
				JRDesignCrosstabColumnGroup p = (JRDesignCrosstabColumnGroup) colGroupsList.get(colGroupsList.size() - 1);
				setCellHeight(p, p.getHeight(), p.getHeight() + height - cell.getHeight());
			}
			break;
		case JRCrosstabOrigin.TYPE_COLUMN_GROUP_CROSSTAB_HEADER:
			if (!colGroupsList.isEmpty()) {
				JRDesignCrosstabColumnGroup p = (JRDesignCrosstabColumnGroup) colGroupsList.get(colGroupsList.size() - 1);
				setCellHeight(p, p.getHeight(), height);
			}
			break;
		case JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER:
			for (int i = 0; i < colGroupsList.size(); i++) {
				JRDesignCrosstabColumnGroup p = (JRDesignCrosstabColumnGroup) colGroupsList.get(i);
				if (p.getName().equals(columnGroupName)) {
					setCellHeight(p, p.getHeight(), height);
					break;
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER:
			for (int i = 0; i < colGroupsList.size(); i++) {
				JRDesignCrosstabColumnGroup p = (JRDesignCrosstabColumnGroup) colGroupsList.get(i);
				if (p.getName().equals(columnGroupName)) {
					setCellHeight(p, p.getHeight(), p.getHeight() + height - cell.getHeight());
					break;
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER:
			boolean calculated = false;
			String rowGroupName = cell.getOrigin().getRowGroupName();
			for (int i = cells.length - 1; i >= 0; i--) {
				for (int j = cells[i].length - 1; j >= 0; j--) {
					JRDesignCrosstabCell jrCrosstabCell = (JRDesignCrosstabCell) cells[i][j];
					if (jrCrosstabCell != null && jrCrosstabCell.getRowTotalGroup() != null && rowGroupName != null && jrCrosstabCell.getRowTotalGroup().equals(rowGroupName)) {
						if (!calculated) {
							height = jrCrosstabCell.getHeight() + height - cell.getHeight();
							calculated = true;
						}
						if (height >= 0)
							jrCrosstabCell.setHeight(height);
						else
							return;
					}
				}
			}
			break;
		case JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER:
			rowGroupName = cell.getOrigin().getRowGroupName();
			for (int i = 0; i < rowGroupsList.size(); i++) {
				JRDesignCrosstabRowGroup rg = (JRDesignCrosstabRowGroup) rowGroupsList.get(i);
				if (rg.getName().equals(rowGroupName)) {
					if (i == rowGroupsList.size() - 1) {
						setHeight((JRDesignCellContents) cells[cells.length - 1][cells[i].length - 1].getContents(), height);
					} else {
						int delta = height - cell.getHeight();
						JRDesignCrosstabRowGroup rgNext = (JRDesignCrosstabRowGroup) rowGroupsList.get(i + 1);
						if (rgNext.getTotalPositionValue().equals(CrosstabTotalPositionEnum.END)) {
							JRDesignCellContents totalHeader = (JRDesignCellContents) rgNext.getTotalHeader();
							setHeight(totalHeader, totalHeader.getHeight() + delta);
						} else {
							JRDesignCellContents header = (JRDesignCellContents) rgNext.getHeader();
							setHeight(header, header.getHeight() + delta);
						}
						break;
					}
				}
			}
			break;
		}
	}

	private void setCellHeight(JRDesignCrosstabColumnGroup p, int oldValue, int height) {
		if (height >= 0) {
			p.setHeight(height);
			p.getEventSupport().firePropertyChange(JRDesignCrosstabColumnGroup.PROPERTY_HEIGHT, oldValue, height);
		}
	}

	private void setCellWidth(JRDesignCrosstabRowGroup p, int oldValue, int width) {
		if (width > 0) {
			p.setWidth(width);
			p.getEventSupport().firePropertyChange(JRDesignCrosstabRowGroup.PROPERTY_WIDTH, oldValue, width);
		}
	}

	public Dimension getCellPackSize(CrosstabCell cc) {
		cc = matrix.getCrosstabCell(cc);
		if (cc == null)
			return null;
		Guide g = cc.getEast();
		int w = -g.getY();
		for (CrosstabCell c : g.getPrev()) {
			if (c.cell != null) {
				List<JRChild> cells = c.cell.getChildren();
				if (!cells.isEmpty()) {
					int width = ModelUtils.getContainerSize(cells, new Dimension(0, 0)).width;

					w = Math.max(w, width - c.cell.getWidth());
				}
			}
		}
		g = cc.getSouth();
		int h = 0;
		if (g != null) {
			h = -g.getY();
			for (CrosstabCell c : g.getPrev()) {
				if (c.cell != null) {
					List<JRChild> cells = c.cell.getChildren();
					if (!cells.isEmpty()) {
						int height = ModelUtils.getContainerSize(cells, new Dimension(0, 0)).height;

						h = Math.max(h, height - c.cell.getHeight());
					}
				}
			}
		}
		Rectangle b = cc.getBounds();

		return new Dimension(b.width + w, b.height + h);
	}

	public List<CrosstabCell> getLeftOf(CrosstabCell cell) {
		cell = matrix.getCrosstabCell(cell);
		Guide g = cell.getWest();
		return g.getPrev();
	}

	public List<CrosstabCell> getTopOf(CrosstabCell cell) {
		cell = matrix.getCrosstabCell(cell);
		Guide g = cell.getNorth();
		return g.getPrev();
	}

	public List<CrosstabCell> getBottomOf(CrosstabCell cell) {
		cell = matrix.getCrosstabCell(cell);
		Guide g = cell.getSouth();
		return g.getPrev();
	}

	public List<CrosstabCell> getRightOf(CrosstabCell cell) {
		cell = matrix.getCrosstabCell(cell);
		Guide g = cell.getEast();
		return g.getPrev();
	}
}
