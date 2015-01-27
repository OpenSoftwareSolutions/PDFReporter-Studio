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
import java.util.List;

import net.sf.jasperreports.components.table.Cell;

public class Guide {
	private int y;
	private List<ColumnCell> prev = new ArrayList<ColumnCell>();
	private List<ColumnCell> next = new ArrayList<ColumnCell>();

	public Guide(int y) {
		this.y = y;
	}

	public void setNext(List<ColumnCell> next) {
		this.next = next;
	}

	public void setPrev(List<ColumnCell> prev) {
		this.prev = prev;
	}

	public List<ColumnCell> getPrev() {
		return prev;
	}

	public List<ColumnCell> getNext() {
		return next;
	}

	public void addWest(ColumnCell cell) {
		cell.setEast(this);
		prev.add(cell);
	}

	public void addEast(ColumnCell cell) {
		cell.setWest(this);
		next.add(cell);
	}

	public void addNorth(ColumnCell cell) {
		cell.setSouth(this);
		prev.add(cell);
	}

	public void addSouth(ColumnCell cell) {
		cell.setNorth(this);
		next.add(cell);
	}

	public void mirror() {
		switchCells(next);
		switchCells(prev);

		List<ColumnCell> tmp = next;
		next = prev;
		prev = tmp;
	}

	private void switchCells(List<ColumnCell> cols) {
		for (ColumnCell cc : cols) {
			Guide t = cc.getSouth();
			cc.setSouth(cc.getNorth());
			cc.setNorth(t);
		}
	}

	public int getY() {
		return y;
	}

	public void setY(Guide north, Cell c) {
		if (c != null)
			y = Math.max(y, north.getY() + c.getHeight());
	}

	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("y: " + y + " NORTH");
		for (ColumnCell cc : prev)
			res.append(cc.toString());
		res.append("\nSOUTH");
		for (ColumnCell cc : next)
			res.append(cc.toString());
		return res.toString();
	}

}
