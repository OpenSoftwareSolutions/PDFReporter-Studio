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
package com.jaspersoft.studio.preferences.editor.sorttable;

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.studio.preferences.editor.sorttable.model.ITableContentProvider;
import com.jaspersoft.studio.preferences.editor.sorttable.model.TableViewerSorter;
import com.jaspersoft.studio.preferences.editor.sorttable.model.TableViewerSorterHandler;


public class TableFieldEditor extends FieldEditor {

	private Table table;

	private TableViewer viewer;

	private IStructuredContentProvider contentProvider;

	private ITableLabelProvider labelProvider;

	private Object input;

	private String[] columnHeaders;

	private TableViewerSorter sorter;

	private TableViewerSorterHandler sorterHandler;

	private int selectionColumn;

	private Object oldValue;

	public TableFieldEditor(String name, String labelText, Composite parent, IStructuredContentProvider contentProvider,
			ITableLabelProvider labelProvider, String[] columnHeaders, Object input) {
		this.contentProvider = contentProvider;
		this.labelProvider = labelProvider;
		this.columnHeaders = columnHeaders;
		this.input = input;
		this.init(name, labelText);
		this.createControl(parent);
	}  
	public int getNumberOfControls() {
		return (1);
	}  
	public void setSelectionColumn(int columnIndex) {
		this.selectionColumn = columnIndex;
	}  
	public int getSelectionColumn() {
		return (this.selectionColumn);
	}  
	public String getSelection() {
		IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
		if (selection.isEmpty()) { 
			return ("");
		} else if (this.selectionColumn == -1) { 
			return (selection.getFirstElement().toString());
		} else { 
			return (this.labelProvider.getColumnText(selection.getFirstElement(), this.selectionColumn));
		}
	} 
	public void setSortingEnabled(boolean enabled) {
		if (this.contentProvider instanceof ITableContentProvider) {
			if (enabled) {
				this.sorter = new TableViewerSorter(this.viewer, (ITableContentProvider) this.contentProvider);
				this.sorterHandler = new TableViewerSorterHandler(this.table, this.sorter);
				this.viewer.setSorter(sorter);
			} else {
				this.sorter = null;
				this.sorterHandler = null;
				this.viewer.setSorter(null);
			}
		}
	} 
	public boolean isSortingEnabled() {
		return (this.sorterHandler != null);
	}  
	public void sort(int columnIndex, boolean ascending) {
		if (this.isSortingEnabled()) {
			this.sorterHandler.sort(columnIndex, ascending);
		}
	} 
	public int getSortingColumn() {
		if (this.isSortingEnabled()) {
			return (this.sorter.getSortingColumn());
		} else {
			return (-1);
		}
	} 
	public boolean isSortAscending() {
		if (this.isSortingEnabled()) {
			return (this.sorter.isAscending());
		} else {
			return (false);
		}
	} 
	public void setColumnWidth(int columnIndex, int width) {
		if (columnIndex >= 0 && columnIndex < this.columnHeaders.length) {
			TableColumn column = this.table.getColumn(columnIndex);
			column.setWidth(width);
		}
	} 
	public int getColumnWidth(int columnIndex) {
		if (columnIndex >= 0 && columnIndex < this.columnHeaders.length) {
			TableColumn column = this.table.getColumn(columnIndex);
			return (column.getWidth());
		} else {
			return (0);
		}
	}  
	protected void adjustForNumColumns(int numColumns) {
		GridData gd = (GridData) this.table.getLayoutData();
		gd.horizontalSpan = numColumns - 1;
		gd.grabExcessHorizontalSpace = gd.horizontalSpan <= 1;
	}  
	protected void doFillIntoGrid(Composite parent, int numColumns) {
		this.getLabelControl(parent);

		this.table = new Table(parent, SWT.FULL_SELECTION | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		this.table.setHeaderVisible(true);
		this.table.setLinesVisible(false);
		this.table.addSelectionListener(new SelectionListener() {
			public void widgetDefaultSelected(SelectionEvent e) {
				valueChanged();
			}  
			public void widgetSelected(SelectionEvent e) {
				valueChanged();
			}  
		});

		this.initializeColumns();
		this.initializeViewer();

		GridData gd = new GridData();
		gd.horizontalSpan = numColumns - 1;
		gd.horizontalAlignment = GridData.FILL;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		this.table.setLayoutData(gd);
	} 
	protected void doLoad() {
		String value = this.getPreferenceStore().getString(this.getPreferenceName());
		this.setSelection(value);
		this.oldValue = value;
	} 
	protected void doLoadDefault() {
		String defaultValue = this.getPreferenceStore().getDefaultString(this.getPreferenceName());
		this.setSelection(defaultValue);
		this.valueChanged();
	}  
	protected void doStore() {
		this.getPreferenceStore().setValue(this.getPreferenceName(), this.getSelection());
	} 
	protected void valueChanged() {
		this.setPresentsDefaultValue(false);

		IStructuredSelection selection = (IStructuredSelection) this.viewer.getSelection();
		String newValue;
		if (selection.isEmpty()) {
			newValue = "";
		} else if (this.selectionColumn == -1) {
			newValue = selection.getFirstElement().toString();
		} else {
			newValue = this.labelProvider.getColumnText(selection.getFirstElement(), this.selectionColumn);
		}
		if (newValue.equals(oldValue)) {
			this.fireValueChanged(VALUE, oldValue, newValue);
			oldValue = newValue;
		}
	} 
	private void initializeViewer() {
		this.viewer = new TableViewer(this.table);
		this.viewer.setContentProvider(this.contentProvider);
		this.viewer.setLabelProvider(this.labelProvider);

		this.viewer.setColumnProperties(this.columnHeaders);

		this.viewer.setInput(this.input);
 
		TableColumn column;
		for (int i = 0; i < this.columnHeaders.length; i++) {
			column = this.table.getColumn(i);
			column.pack();
		}
	} 
	private void initializeColumns() {
		TableColumn column;
		for (int i = 0; i < this.columnHeaders.length; i++) {
			column = new TableColumn(this.table, SWT.LEFT);
			column.setText(this.columnHeaders[i]);
			column.setToolTipText(this.columnHeaders[i]);
		}
	}

	private void setSelection(String selectionStr) {
		if (this.viewer != null) {
			Object[] items = this.contentProvider.getElements(this.viewer.getInput());
			boolean selected = false;
			if (this.selectionColumn == -1) {
				for (int i = 0; i < items.length && !selected; i++) {
					if (selectionStr.equals(items[i].toString())) {
						StructuredSelection selection = new StructuredSelection(items[i]);
						this.viewer.setSelection(selection);
						selected = true;
					}
				}
			} else {
				for (int i = 0; i < items.length && !selected; i++) {
					if (selectionStr.equals(this.labelProvider.getColumnText(items[i], this.selectionColumn))) {
						StructuredSelection selection = new StructuredSelection(items[i]);
						this.viewer.setSelection(selection);
						selected = true;
					}
				}
			}
		}
	}

}
