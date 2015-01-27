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
package com.jaspersoft.studio.preferences.editor.sorttable.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

public class TableViewerSorterHandler extends SelectionAdapter {

	private Table table;

	private TableViewerSorter sorter;

	public TableViewerSorterHandler(Table table, TableViewerSorter sorter) {
		this.table = table;
		this.sorter = sorter;
		this.registerColumns();
	}

	public void dispose() {
		this.unregisterColumns();
	}

	public void widgetSelected(SelectionEvent event) {
		int columnIndex = this.table.indexOf((TableColumn) event.widget);
		this.sort(columnIndex);
	}

	public void sort(int columnIndex) {
		this.sort(columnIndex, !this.sorter.isAscending());
	}

	public void sort(int columnIndex, boolean ascending) {
		this.sorter.setSortingColumn(columnIndex);
		this.sorter.setAscending(ascending);
		this.sorter.sort();

		TableColumn column = this.table.getColumn(columnIndex);
		this.table.setSortColumn(column);
		this.table.setSortDirection(sorter.isAscending() ? SWT.UP : SWT.DOWN);
	}

	private void registerColumns() {
		TableColumn[] columns = this.table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].addSelectionListener(this);
		}
	}

	private void unregisterColumns() {
		TableColumn[] columns = this.table.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].removeSelectionListener(this);
		}
	}
}
