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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.CrosstabColumnCell;
import net.sf.jasperreports.crosstabs.JRCellContents;
import net.sf.jasperreports.crosstabs.JRCrosstabCell;
import net.sf.jasperreports.crosstabs.JRCrosstabColumnGroup;
import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRCrosstabOrigin;
import net.sf.jasperreports.crosstabs.design.JRDesignCellContents;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.type.RunDirectionEnum;

import org.eclipse.draw2d.geometry.Rectangle;

public class CrosstabMatrix {
	private List<Guide> hGuides = new ArrayList<Guide>();
	private List<Guide> vGuides = new ArrayList<Guide>();
	private Map<CrosstabCell, Rectangle> map = new HashMap<CrosstabCell, Rectangle>();

	public Map<CrosstabCell, Rectangle> getCells() {
		return map;
	}

	public CrosstabCell getCrosstabCell(CrosstabCell colcel) {
		for (CrosstabCell cc : map.keySet())
			if (cc.equals(colcel))
				return cc;
		return null;
	}

	public Rectangle getBounds(CrosstabCell cc) {
		Rectangle r = map.get(cc);
		if (r != null)
			return r.getCopy();
		return null;
	}

	private void fixCells() {
		for (CrosstabCell cc : map.keySet())
			map.put(cc, cc.getBounds());
	}

	public void fill(JRDesignCrosstab crosstab) {
		hGuides.clear();
		vGuides.clear();
		map.clear();

		Guide north = new Guide(0);
		hGuides.add(north);

		Guide south = fillTitleCellH(north, addNext(north, hGuides), crosstab);
		north = south;
		south = fillHeaderCellH(north, addNext(north, hGuides), crosstab);

		fillColumnGroupH(north, south, crosstab);

		Guide gnorth = south;

		fillRowGroupH(south, addNext(south, hGuides), crosstab);

		fillDetailsH(gnorth, crosstab);

		fillVertical(crosstab);

		fixCells();
		// print();
	}

	public void fillVertical(JRDesignCrosstab crosstab) {
		Guide west = new Guide(0);
		vGuides.add(west);
		Guide gwest = null;

		Guide east = fillTitleCellV(west, addNext(west, vGuides), crosstab);

		Guide heast = fillHeaderCellV(west, addNext(west, vGuides), crosstab);

		fillRowGroupV(west, heast, crosstab);

		gwest = heast;
		fillColumnGroupV(heast, east, crosstab);

		fillDetailsV(gwest, crosstab);

		if (crosstab.getRunDirectionValue() == RunDirectionEnum.RTL)
			mirrorV();
	}

	public void mirrorV() {
		int maxy = vGuides.get(vGuides.size() - 1).getY();
		Collections.reverse(vGuides);
		for (Guide g : vGuides) {
			g.setY(maxy - g.getY());
			g.mirrorV();
		}

	}

	public Guide fillRowGroupV(Guide west, Guide east, JRDesignCrosstab crosstab) {
		JRCrosstabRowGroup[] rows = crosstab.getRowGroups();
		for (int i = 0; i < rows.length; i++) {
			JRCrosstabRowGroup cg = rows[i];
			CrosstabCell cc = null;
			if (cg.hasTotal())
				cc = addCell4ColTotal(west, east, cg);

			cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER);
			west.addEast(cc);

			Guide teast = null;
			if (i == rows.length - 1)
				teast = east;
			else
				teast = addNext(west, vGuides);
			teast.addWest(cc);
			teast.setX(west, cc.cell);
			west = teast;
		}
		return east;
	}

	public Guide fillColumnGroupV(Guide west, Guide east, JRDesignCrosstab crosstab) {
		JRCrosstabColumnGroup[] cols = crosstab.getColumnGroups();
		CrosstabCell hcc = createCell((JRDesignCellContents) crosstab.getHeaderCell(), JRCrosstabOrigin.TYPE_HEADER_CELL);
		for (int i = 0; i < cols.length; i++) {
			JRCrosstabColumnGroup cg = cols[i];

			CrosstabCell cc = createCell((JRDesignCellContents) cg.getCrosstabHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_CROSSTAB_HEADER);
			cc.setEast(hcc.getEast());
			cc.setWest(hcc.getWest());

			if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.START) {
				cc = createCell((JRDesignCellContents) cg.getTotalHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER);
				west.addEast(cc);

				Guide teast = cg.hasTotal() ? addNext(west, vGuides) : east;
				teast.addWest(cc);
				teast.setX(west, cc.cell);

				cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER);
				teast.addEast(cc);
				east.addWest(cc);
				east.setX(teast, cc.cell);

				west = teast;
			} else if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.END) {
				cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER);
				west.addEast(cc);

				Guide teast = cg.hasTotal() ? addNext(west, vGuides) : east;
				teast.addWest(cc);
				teast.setX(west, cc.cell);

				cc = createCell((JRDesignCellContents) cg.getTotalHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER);
				teast.addEast(cc);
				east.addWest(cc);
				east.setX(teast, cc.cell);

				east = teast;
			} else {
				cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER);
				west.addEast(cc);
				east.addWest(cc);
				east.setX(west, cc.cell);
			}
		}
		return east;
	}

	public void fillDetailsV(Guide gwest, JRDesignCrosstab crosstab) {
		JRCrosstabCell[][] cells = crosstab.getCells();
		if (cells != null)
			for (int i = cells.length - 1; i >= 0; i--) {
				JRCrosstabCell[] rowcells = cells[i];
				Guide west = gwest;
				Guide east = vGuides.get(vGuides.size() - 1);

				List<JRCrosstabColumnGroup> cols = crosstab.getColumnGroupsList();
				for (int k = 0; k < cols.size(); k++) {
					JRCrosstabColumnGroup cg = cols.get(k);
					boolean last = k == cols.size() - 1;
					if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.END) {
						Guide twest = vGuides.get(vGuides.indexOf(east) - 1);
						addDetailCellV(rowcells, twest, east, cg.getName());
						east = twest;
					} else if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.START) {
						Guide teast = vGuides.get(vGuides.indexOf(west) + 1);
						addDetailCellV(rowcells, west, teast, cg.getName());
						west = teast;
					} else if (!last)
						continue;
					if (last)
						addDetailCellV(rowcells, west, east, null);
				}
			}
	}

	private void addDetailCellV(JRCrosstabCell[] rowcells, Guide west, Guide east, String name) {
		for (int j = 0; j < rowcells.length; j++) {
			JRCrosstabCell rc = rowcells[j];
			if (rc == null)
				continue;
			String ctg = rc.getColumnTotalGroup();
			if ((ctg != null && ctg.equals(name)) || (ctg == null && name == null)) {
				CrosstabCell cc = createCell((JRDesignCellContents) rc.getContents(), JRCrosstabOrigin.TYPE_DATA_CELL);
				west.addEast(cc);
				east.addWest(cc);
				break;
			}
		}
	}

	public void fillDetailsH(Guide gnorth, JRDesignCrosstab crosstab) {
		JRCrosstabCell[][] cells = crosstab.getCells();
		if (cells != null)
			for (int i = cells[0].length - 1; i >= 0; i--) {
				Guide north = gnorth;
				Guide south = hGuides.get(hGuides.size() - 1);

				List<JRCrosstabRowGroup> rows = crosstab.getRowGroupsList();
				for (int k = 0; k < rows.size(); k++) {
					JRCrosstabRowGroup cg = rows.get(k);
					boolean last = k == rows.size() - 1;
					if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.END) {
						Guide tnorth = hGuides.get(hGuides.indexOf(south) - 1);
						addDetailCellH(cells, i, tnorth, south, cg.getName());
						south = tnorth;
					} else if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.START) {
						Guide tsouth = hGuides.get(hGuides.indexOf(north) + 1);
						addDetailCellH(cells, i, north, tsouth, cg.getName());
						north = tsouth;
					} else if (!last)
						continue;
					if (last)
						addDetailCellH(cells, i, north, south, null);
				}
			}
	}

	private void addDetailCellH(JRCrosstabCell[][] cells, int i, Guide north, Guide south, String name) {
		for (int j = 0; j < cells.length; j++) {
			JRCrosstabCell rc = cells[j][i];
			if (rc != null) {
				String rtg = rc.getRowTotalGroup();
				if ((rtg != null && rtg.equals(name)) || (rtg == null && name == null)) {
					CrosstabCell cc = createCell((JRDesignCellContents) rc.getContents(), JRCrosstabOrigin.TYPE_DATA_CELL);
					north.addSouth(cc);
					south.addNorth(cc);
					break;
				}
			}
		}
	}

	public Guide fillColumnGroupH(Guide north, Guide south, JRDesignCrosstab crosstab) {
		JRCrosstabColumnGroup[] columns = crosstab.getColumnGroups();
		for (int i = 0; i < columns.length; i++) {
			JRCrosstabColumnGroup cg = columns[i];
			CrosstabCell cc = null;
			if (cg.hasTotal())
				cc = addCell4ColTotal(north, south, cg);

			cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_HEADER);
			north.addSouth(cc);

			Guide tsouth = null;
			if (i == columns.length - 1)
				tsouth = south;
			else
				tsouth = addNext(north, hGuides);
			tsouth.addNorth(cc);
			tsouth.setY(north, cc.cell);
			north = tsouth;

			CrosstabCell hcc = createCell((JRDesignCellContents) cg.getCrosstabHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_CROSSTAB_HEADER);
			hcc.setSouth(cc.getSouth());
			hcc.setNorth(cc.getNorth());
		}
		return south;
	}

	public Guide fillRowGroupH(Guide north, Guide south, JRDesignCrosstab crosstab) {
		JRCrosstabRowGroup[] rows = crosstab.getRowGroups();
		for (int i = 0; i < rows.length; i++) {
			JRCrosstabRowGroup cg = rows[i];
			if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.START) {
				CrosstabCell cc = createCell((JRDesignCellContents) cg.getTotalHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER);
				north.addSouth(cc);

				Guide tsouth = cg.hasTotal() ? addNext(north, hGuides) : south;
				tsouth.addNorth(cc);
				tsouth.setY(north, cc.cell);

				cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER);
				tsouth.addSouth(cc);
				south.addNorth(cc);
				south.setY(tsouth, cc.cell);

				north = tsouth;
			} else if (cg.getTotalPositionValue() == CrosstabTotalPositionEnum.END) {
				CrosstabCell cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER);
				north.addSouth(cc);

				Guide tsouth = cg.hasTotal() ? addNext(north, hGuides) : south;
				tsouth.addNorth(cc);
				tsouth.setY(north, cc.cell);

				cc = createCell((JRDesignCellContents) cg.getTotalHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER);
				tsouth.addSouth(cc);
				south.addNorth(cc);
				south.setY(tsouth, cc.cell);

				south = tsouth;
			} else {
				CrosstabCell cc = createCell((JRDesignCellContents) cg.getHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_HEADER);
				north.addSouth(cc);
				south.addNorth(cc);
				south.setY(north, cc.cell);
			}
		}
		return south;
	}

	public Guide fillTitleCellH(Guide north, Guide south, JRDesignCrosstab crosstab) {
		CrosstabColumnCell c = crosstab.getTitleCell();
		JRCellContents cell = c != null ? c.getCellContents() : null;
		CrosstabCell cc = createCell((JRDesignCellContents) cell, JRCrosstabOrigin.TYPE_TITLE_CELL);
		north.addSouth(cc);
		south.addNorth(cc);
		south.setY(north, cell);
		return south;
	}

	public Guide fillTitleCellV(Guide west, Guide east, JRDesignCrosstab crosstab) {
		CrosstabColumnCell c = crosstab.getTitleCell();
		JRCellContents cell = c != null ? c.getCellContents() : null;
		CrosstabCell cc = createCell((JRDesignCellContents) cell, JRCrosstabOrigin.TYPE_TITLE_CELL);
		west.addEast(cc);
		east.addWest(cc);
		east.setX(west, cell);
		return east;
	}

	public Guide fillHeaderCellH(Guide north, Guide south, JRDesignCrosstab crosstab) {
		JRCellContents c = crosstab.getHeaderCell();
		CrosstabCell cc = createCell((JRDesignCellContents) c, JRCrosstabOrigin.TYPE_HEADER_CELL);
		north.addSouth(cc);
		south.addNorth(cc);
		south.setY(north, c);
		return south;
	}

	public Guide fillHeaderCellV(Guide west, Guide east, JRDesignCrosstab crosstab) {
		JRCellContents c = crosstab.getHeaderCell();
		CrosstabCell cc = createCell((JRDesignCellContents) c, JRCrosstabOrigin.TYPE_HEADER_CELL);
		west.addEast(cc);
		east.addWest(cc);
		east.setX(west, c);
		return east;
	}

	public CrosstabCell addCell4ColTotal(Guide north, Guide south, JRCrosstabColumnGroup cg) {
		CrosstabCell cc = createCell((JRDesignCellContents) cg.getTotalHeader(), JRCrosstabOrigin.TYPE_COLUMN_GROUP_TOTAL_HEADER);
		north.addSouth(cc);
		south.addNorth(cc);
		south.setY(north, cc.cell);
		return cc;
	}

	public CrosstabCell addCell4ColTotal(Guide west, Guide east, JRCrosstabRowGroup cg) {
		CrosstabCell cc = createCell((JRDesignCellContents) cg.getTotalHeader(), JRCrosstabOrigin.TYPE_ROW_GROUP_TOTAL_HEADER);
		west.addEast(cc);
		east.addWest(cc);
		east.setX(west, cc.cell);
		return cc;
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

	public CrosstabCell createCell(JRDesignCellContents cell, byte type) {
		CrosstabCell cc = new CrosstabCell(cell, type);
		if (map.containsKey(cc)) {
			for (CrosstabCell key : map.keySet())
				if (key.equals(cc))
					return key;
		}
		map.put(cc, null);
		return cc;
	}

	public void print() {
		System.out.println("\n\n-- NEW TABLE--------------------");
		// for (int i = 0; i < hGuides.size(); i++) {
		// Guide g = hGuides.get(i);
		// System.out.println("row:" + i + " " + g.toString() + "\n");
		// }
		System.out.println("-- VERTICAL --------------------");
		for (int i = 0; i < vGuides.size(); i++) {
			Guide g = vGuides.get(i);
			System.out.println("col:" + i + "\n" + g.toString());
		}
	}
}
