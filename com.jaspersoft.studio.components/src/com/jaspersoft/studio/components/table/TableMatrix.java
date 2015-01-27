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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRGroup;

import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.utils.Misc;

public class TableMatrix {
	private List<Guide> hGuides = new ArrayList<Guide>();
	private List<Guide> vGuides = new ArrayList<Guide>();

	private Map<ColumnCell, Rectangle> map = new HashMap<ColumnCell, Rectangle>();
	private List<JRGroup> groupsList;

	public Map<ColumnCell, Rectangle> getCells() {
		return map;
	}

	public void setY(Guide north, int y) {
		int delta = y - north.getY();
		for (int i = hGuides.indexOf(north); !(north instanceof RowGuide); i--) {
			north = hGuides.get(i);
			north.setY(north.getY() + delta);
		}
	}

	public Rectangle getYHColumn(BaseColumn bc, int type, String grName) {
		return map.get(new ColumnCell(type, Misc.nvl(grName), bc));
	}

	public ColumnCell getColumnCell(ColumnCell colcel) {
		for (ColumnCell cc : map.keySet())
			if (cc.equals(colcel))
				return cc;
		return null;
	}

	public int getSectionHeight(ColumnCell cc) {
		Guide north = cc.getNorth();
		Guide south = cc.getSouth();

		for (int i = hGuides.indexOf(north); !(north instanceof RowGuide); i--)
			north = hGuides.get(i);
		for (int i = hGuides.indexOf(south); !(south instanceof RowGuide); i++)
			south = hGuides.get(i);

		return south.getY() - north.getY();
	}

	public int getRowHeight(ColumnCell cc) {
		int h = 0;
		if (cc != null) {
			Guide g = getRowGuide(cc);
			if (g != null) {
				if (cc.getNorth() == null)
					cc = getColumnCell(cc);
				if (cc != null) {
					if (TableManager.isBottomOfTable(cc.type))
						return cc.getSouth().getY() - g.getY();
					return g.getY() - cc.getNorth().getY();
				}
			}
		}
		return h;
	}

	public Guide getRowGuide(ColumnCell cc) {
		if (cc != null) {
			if (TableManager.isBottomOfTable(cc.type)) {
				Guide s = cc.getSouth();
				for (int i = hGuides.indexOf(s) - 1; i < hGuides.size(); i--) {
					Guide g = hGuides.get(i);
					if (g instanceof RowGuide)
						return g;
				}
			} else {
				Guide n = cc.getNorth();
				for (int i = hGuides.indexOf(n) + 1; i < hGuides.size(); i++) {
					Guide g = hGuides.get(i);
					if (g instanceof RowGuide)
						return g;
				}
			}
		}
		return null;
	}

	private void fixCells() {
		for (ColumnCell cc : map.keySet())
			map.put(cc, cc.getBounds());
	}

	public void fillMatrix(StandardTable table, TableUtil tableUtil) {
		hGuides.clear();
		vGuides.clear();
		map.clear();

		Guide north = new Guide(0);
		hGuides.add(north);
		Guide south = fillRowTop(north, addNextRow(north), table.getColumns(),
				TableUtil.TABLE_HEADER, "");

		south = fillRowTop(south, addNextRow(south), table.getColumns(),
				TableUtil.COLUMN_HEADER, "");

		groupsList = (List<JRGroup>) tableUtil.getGroupList();
		if (groupsList != null)
			for (Iterator<?> it = groupsList.iterator(); it.hasNext();) {
				JRGroup jrGroup = (JRGroup) it.next();
				south = fillRowTop(south, addNextRow(south),
						table.getColumns(), TableUtil.COLUMN_GROUP_HEADER,
						jrGroup.getName());
			}
		south = fillRowDetail(south, addNextRow(south), table.getColumns());

		int offset = hGuides.indexOf(south);

		north = new Guide(0);
		hGuides.add(north);
		south = fillRowTop(north, addNextRow(north), table.getColumns(),
				TableUtil.TABLE_FOOTER, "");

		south = fillRowTop(south, addNextRow(south), table.getColumns(),
				TableUtil.COLUMN_FOOTER, "");

		if (groupsList != null)
			for (ListIterator<?> it = groupsList
					.listIterator(groupsList.size()); it.hasPrevious();) {
				JRGroup jrGroup = (JRGroup) it.previous();
				south = fillRowTop(south, addNextRow(south),
						table.getColumns(), TableUtil.COLUMN_GROUP_FOOTER,
						jrGroup.getName());
			}
		mirrorBottom(offset);

		fillVertical(table);
		fixCells();
	}

	public void mirrorBottom(int offset) {
		int size = hGuides.size();
		int maxy = 0;
		for (int i = offset + 1; i < size; i++) {
			maxy = Math.max(maxy, hGuides.get(i).getY());
		}
		int y = hGuides.get(offset).getY();
		for (int i = offset + 1; i < size; i++) {
			Guide g = hGuides.get(i);
			g.setY(y + maxy - g.getY());
		}
		Guide g = hGuides.get(offset);
		Guide lg = hGuides.get(size - 1);

		g.setNext(lg.getPrev());

		for (int i = offset + 1; i < size; i++) {
			hGuides.get(i).mirror();
		}

		hGuides.remove(lg);
		size = hGuides.size() - 1;

		int s = size - offset;
		for (int i = 0; i < s; i++) {
			int tind = offset + 1 + i;
			int bind = size - i;
			if (tind >= bind)
				break;
			Guide tmp = hGuides.get(tind);
			hGuides.set(tind, hGuides.get(bind));
			hGuides.set(bind, tmp);
		}
		Guide fg = hGuides.get(offset + 1);
		for (ColumnCell cc : fg.getPrev()) {
			cc.setSouth(g);
		}

	}

	private Guide fillRowDetail(Guide north, Guide south, List<BaseColumn> cols) {
		for (BaseColumn bc : cols) {
			if (bc instanceof StandardColumnGroup) {
				fillRowDetail(north, south,
						((StandardColumnGroup) bc).getColumns());
			} else {
				ColumnCell cell = createCell(bc, TableUtil.COLUMN_DETAIL, "");
				north.addSouth(cell);
				south.addNorth(cell);
				south.setY(north, cell.cell);
			}
		}
		return south;
	}

	private Guide fillRowTop(Guide north, Guide south, List<BaseColumn> cols,
			int type, String grName) {
		for (BaseColumn bc : cols) {
			ColumnCell cell = createCell(bc, type, grName);
			north.addSouth(cell);
			if (bc instanceof StandardColumnGroup) {
				Guide gn = addNext(north, hGuides);
				gn.addNorth(cell);
				gn.setY(north, cell.cell);
				south.setY(Math.max(gn.getY(), south.getY()));
				fillRowTop(gn, south, ((StandardColumnGroup) bc).getColumns(),
						type, grName);
			} else {
				south.addNorth(cell);
				south.setY(north, cell.cell);
			}
		}
		return south;
	}

	private void fillVertical(StandardTable table) {
		Guide west = new Guide(0);
		vGuides.add(west);
		Guide east = addNext(west, vGuides);

		fillVertical(west, east, table.getColumns());
	}

	private Guide fillVertical(Guide west, Guide east, List<BaseColumn> cols) {
		for (int i = 0; i < cols.size(); i++) {
			BaseColumn bc = cols.get(i);
			if (bc instanceof StandardColumnGroup) {
				east = fillVertical(west, east,
						((StandardColumnGroup) bc).getColumns());
			} else {
				east.setY(west.getY() + bc.getWidth());
				setVGuides(west, east, bc, TableUtil.COLUMN_DETAIL, "");
			}
			setVGuides(west, east, bc, TableUtil.TABLE_HEADER, "");
			setVGuides(west, east, bc, TableUtil.COLUMN_HEADER, "");
			setVGuides(west, east, bc, TableUtil.COLUMN_FOOTER, "");
			setVGuides(west, east, bc, TableUtil.TABLE_FOOTER, "");
			if (groupsList != null)
				for (JRGroup jrGroup : groupsList) {
					setVGuides(west, east, bc, TableUtil.COLUMN_GROUP_HEADER,
							jrGroup.getName());
					setVGuides(west, east, bc, TableUtil.COLUMN_GROUP_FOOTER,
							jrGroup.getName());
				}
			if (i < cols.size() - 1) {
				west = east;
				east = addNext(west, vGuides);
			}
		}
		return east;
	}

	public void setVGuides(Guide west, Guide east, BaseColumn bc, int type,
			String grName) {
		ColumnCell cc = getColumnCell(new ColumnCell(type, grName, bc));
		west.addEast(cc);
		east.addWest(cc);
	}

	private Guide addNext(Guide north, List<Guide> guides) {
		Guide south = new Guide(north.getY());
		int i = guides.indexOf(north) + 1;
		if (i <= guides.size())
			guides.add(i, south);
		else
			guides.add(south);
		return south;
	}

	private Guide addNextRow(Guide north) {
		Guide south = new RowGuide(north.getY());
		hGuides.add(south);
		return south;
	}

	public ColumnCell createCell(BaseColumn bc, int type, String grName) {
		ColumnCell cc = new ColumnCell(type, grName, bc);
		cc.setCell((DesignCell) TableUtil.getCell(bc, type, grName));
		map.put(cc, null);
		return cc;
	}

	public void print() {
		System.out.println("-- NEW TABLE--------------------");
		for (int i = 0; i < hGuides.size(); i++) {
			Guide g = hGuides.get(i);
			System.out.println("row:" + i + "\n" + g.toString());
		}
		// System.out.println("-- VERTICAL --------------------");
		// for (int i = 0; i < vGuides.size(); i++) {
		// Guide g = vGuides.get(i);
		// System.out.println("col:" + i + "\n" + g.toString());
		// }
	}
}
