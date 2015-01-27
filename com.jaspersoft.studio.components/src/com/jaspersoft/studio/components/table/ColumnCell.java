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

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.util.TableUtil;

import org.eclipse.draw2d.geometry.Rectangle;

public class ColumnCell {
	public int type;
	public String grName;
	public BaseColumn column;
	public DesignCell cell;

	public ColumnCell(int type, String grName, BaseColumn column) {
		super();
		this.type = type;
		this.grName = grName;
		this.column = column;
	}

	public void setCell(DesignCell cell) {
		this.cell = cell;
	}

	private Guide north;
	private Guide south;
	private Guide west;
	private Guide east;

	public Guide getWest() {
		return west;
	}

	public void setWest(Guide west) {
		this.west = west;
	}

	public Guide getEast() {
		return east;
	}

	public void setEast(Guide east) {
		this.east = east;
	}

	public Guide getNorth() {
		return north;
	}

	public void setNorth(Guide north) {
		this.north = north;
	}

	public Guide getSouth() {
		return south;
	}

	public void setSouth(Guide south) {
		this.south = south;
	}

	public Rectangle getBounds() {
		int h = 20;
		int w = east.getY() - west.getY();
		if (south != null && north != null) {
			if (south.getY() < north.getY()) {
				Guide tmp = north;
				north = south;
				south = tmp;
			}
			h = south.getY() - north.getY();
		}
		if (north == null || south == null)
			return new Rectangle(0, 0, w, 10);
		return new Rectangle(west.getY(), north.getY(), w, h);
	}

	@Override
	public String toString() {
		Cell c = TableUtil.getCell(column, type, grName);

		String ctype = column instanceof StandardColumnGroup ? "GR" : "COL";

		String str = "[" + type + ":" + ctype + ":" + hashCode() + "]";
		if (c != null)
			str += "[h:" + c.getHeight() + "]";
		else
			str += "[++++]";
		return str;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ColumnCell) {
			ColumnCell toObj = (ColumnCell) obj;
			return this.type == toObj.type && grName.equals(toObj.grName)
					&& this.column == toObj.column;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return grName.hashCode() ^ type ^ column.hashCode();
	}
}
