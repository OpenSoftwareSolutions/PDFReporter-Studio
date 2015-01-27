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
package com.jaspersoft.studio.data.sql.action.select;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.window.Window;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.action.table.CreateTable;
import com.jaspersoft.studio.data.sql.dialogs.FromTableColumnsDialog;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;

public class CreateColumn extends AAction {

	private CreateTable ct;
	private SQLQueryDesigner designer;

	public CreateColumn(SQLQueryDesigner designer, TreeViewer treeViewer) {
		super("&Add Column", treeViewer);
		this.designer = designer;
	}

	@Override
	public boolean calculateEnabled(Object[] selection) {
		super.calculateEnabled(selection);
		return selection != null && selection.length == 1 && isInSelect(selection[0]);
	}

	public static boolean isInSelect(Object element) {
		return element instanceof MSelect || (element instanceof ANode && ((ANode) element).getParent() instanceof MSelect);
	}

	@Override
	public void run() {
		FromTableColumnsDialog dialog = new FromTableColumnsDialog(UIUtils.getShell());
		dialog.setSelection((ANode) selection[0]);
		if (dialog.open() == Window.OK)
			run(dialog.getColumns());
	}

	public void run(Map<MSQLColumn, MFromTable> cols) {
		Object sel = selection[0];
		for (MSQLColumn t : cols.keySet()) {
			MFromTable mftable = cols.get(t);
			if (sel instanceof MSelect)
				sel = run(t, mftable, (MSelect) sel, 0);
			else if (sel instanceof ANode && ((ANode) sel).getParent() instanceof MSelect) {
				MSelect msel = (MSelect) ((ANode) sel).getParent();
				int index = msel.getChildren().indexOf(sel) + 1;
				sel = run(t, mftable, msel, index);
			}
		}
		selectInTree(sel);
	}

	public void run(Collection<MSQLColumn> nodes) {
		Object sel = selection[0];
		List<MFromTable> tbls = Util.getFromTables((ANode) sel);
		for (MSQLColumn t : nodes) {
			MSqlTable tbl = (MSqlTable) t.getParent();
			MFromTable mftable = null;
			for (MFromTable ft : tbls) {
				if (ft.getValue().equals(tbl)) {
					mftable = ft;
					break;
				}
			}
			if (mftable == null) {
				if (ct == null)
					ct = new CreateTable(designer, treeViewer);
				ANode r = Util.getQueryRoot((ANode) sel);
				for (INode n : r.getChildren()) {
					if (n instanceof MFrom) {
						mftable = ct.run(tbl, (MFrom) n, -1);
						tbls = Util.getFromTables((ANode) sel);
						break;
					}
				}
			}

			if (sel instanceof MSelect)
				sel = run(t, mftable, (MSelect) sel, 0);
			else if (sel instanceof MSelectColumn)
				sel = run(t, mftable, (MSelectColumn) sel);
		}
		selectInTree(sel);
	}

	public MSelectColumn run(MSQLColumn sCol, MFromTable mfTable) {
		MSelect mSelect = Util.getKeyword(mfTable, MSelect.class);
		MSelectColumn msCol = run(sCol, mfTable, mSelect, -1);
		selectInTree(msCol);
		return msCol;
	}

	protected MSelectColumn run(MSQLColumn sCol, MFromTable mfTable, MSelectColumn mSelCol) {
		MSelect mSelect = Util.getKeyword(mfTable, MSelect.class);
		return run(sCol, mfTable, mSelect, mSelect.getChildren().indexOf(mSelCol) + 1);
	}

	public MSelectColumn run(MSQLColumn node, MFromTable mfTable, MSelect select, int index) {
		return new MSelectColumn(select, node, mfTable, index);
	}

}
