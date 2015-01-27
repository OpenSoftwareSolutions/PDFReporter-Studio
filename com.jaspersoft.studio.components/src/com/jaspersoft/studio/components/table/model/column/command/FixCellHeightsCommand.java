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

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.util.TableUtil;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.commands.Command;

import com.jaspersoft.studio.components.table.ColumnCell;
import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.TableMatrix;
import com.jaspersoft.studio.components.table.model.column.MColumn;

public class FixCellHeightsCommand extends Command {
	private TableManager tbManager;

	public FixCellHeightsCommand(MColumn mcolumn) {
		this(mcolumn.getMTable().getTableManager());
	}
	
	public FixCellHeightsCommand(TableManager manager) {
		super("Fix Table Cells Height");
		tbManager = manager;
	}

	private Map<DesignCell, Integer> hmap;
	private Map<StandardColumnGroup, Integer> wmap;

	@Override
	public void execute() {
		tbManager.initMaps();
		if (hmap == null)
			hmap = new HashMap<DesignCell, Integer>();
		hmap.clear();
		if (wmap == null)
			wmap = new HashMap<StandardColumnGroup, Integer>();
		wmap.clear();
		TableMatrix mh = tbManager.getMatrixHelper();
		Map<ColumnCell, Rectangle> map = mh.getCells();
		for (ColumnCell cc : map.keySet()) {
			Rectangle b = cc.getBounds();
			if (cc.column instanceof StandardColumnGroup) {
				if (b.width != cc.column.getWidth()) {
					wmap.put((StandardColumnGroup) cc.column,
							cc.column.getWidth());
					((StandardColumnGroup) cc.column).setWidth(b.width);
				}
				continue;
			}
			Cell dc = TableUtil.getCell(cc.column, cc.type, cc.grName);
			if (dc == null)
				continue;

			int oldh = dc.getHeight();
			if (b.height != dc.getHeight()) {
				((DesignCell) dc).setHeight(b.height);
			} else
				continue;
			hmap.put((DesignCell) dc, oldh);
		}
		tbManager.initMaps();
		tbManager.refresh();
	}

	@Override
	public void undo() {
		for (DesignCell dc : hmap.keySet())
			dc.setHeight(hmap.get(dc));
		for (StandardColumnGroup bc : wmap.keySet())
			bc.setWidth(wmap.get(bc));
		tbManager.initMaps();
		tbManager.refresh();
	}
}
