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
package com.jaspersoft.studio.property.descriptor.sortfield.dialog;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;

public class SortFieldPage extends WizardPage {
	private List<?> list;

	public List<?> getList() {
		return list;
	}

	public void setList(List<?> list) {
		this.list = list;
	}

	protected SortFieldPage(String pageName) {
		super(pageName);
		setTitle(Messages.common_sort_field_editor);
		setDescription(Messages.SortFieldPage_description);
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		// composite.setBackground(new Color(null, 125, 125, 125));
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		setControl(composite);

		createTable(composite);
		createButtons(composite);

	}

	private void createTable(Composite composite) {
		Table table = new Table(composite, SWT.SINGLE | SWT.FULL_SELECTION | SWT.HIDE_SELECTION);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		GridData gridData = new GridData(GridData.FILL_BOTH | GridData.HORIZONTAL_ALIGN_BEGINNING
				| GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.verticalSpan = 4;
		gridData.grabExcessVerticalSpace = true;
		table.setLayoutData(gridData);

		TableColumn column = new TableColumn(table, SWT.CENTER);
		column.setText(Messages.SortFieldPage_field);
		column.setWidth(100);
		column.pack();

		column = new TableColumn(table, SWT.CENTER);
		column.setWidth(100);
		column.setText(Messages.common_order);
		column.pack();

		TableViewer tableViewer = new TableViewer(table);
		tableViewer.setUseHashlookup(true);

		CellEditor[] editors = new CellEditor[2];

		ComboBoxCellEditor cbce = new ComboBoxCellEditor(table, new String[] { "BCDA", "ABCD" }, SWT.READ_ONLY); //$NON-NLS-1$ //$NON-NLS-2$

		TextCellEditor textEditor = new TextCellEditor(table);
		((Text) textEditor.getControl()).setTextLimit(60);

		editors[0] = cbce;
		editors[1] = textEditor;

		tableViewer.setCellEditors(editors);
		// tableViewer.setContentProvider(new SortFieldContentProvider());
		// tableViewer.setLabelProvider(new SortFieldLabelsProvider());
		// // The input for the table viewer is the instance of ExampleTaskList
		// tableViewer.setInput(getList());
		// Set the cell modifier for the viewer
		// tableViewer.setCellModifier(new ExampleCellModifier(this));
	}

	private void createButtons(Composite composite) {
		GridData gridData;
		Button add = new Button(composite, SWT.PUSH | SWT.CENTER);
		add.setText(Messages.common_add);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		add.setLayoutData(gridData);
		add.addSelectionListener(new SelectionAdapter() {

			// Add a task to the ExampleTaskList and refresh the view
			public void widgetSelected(SelectionEvent e) {
				// taskList.addTask();
			}
		});

		Button remove = new Button(composite, SWT.PUSH | SWT.CENTER);
		remove.setText(Messages.SortFieldPage_remove);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		remove.setLayoutData(gridData);
		remove.addSelectionListener(new SelectionAdapter() {

			// Add a task to the ExampleTaskList and refresh the view
			public void widgetSelected(SelectionEvent e) {
				// taskList.remove();
			}
		});

		Button up = new Button(composite, SWT.PUSH | SWT.CENTER);
		up.setText(Messages.common_up);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		up.setLayoutData(gridData);
		up.addSelectionListener(new SelectionAdapter() {

			// Add a task to the ExampleTaskList and refresh the view
			public void widgetSelected(SelectionEvent e) {
				// taskList.addTask();
			}
		});

		Button down = new Button(composite, SWT.PUSH | SWT.CENTER);
		down.setText(Messages.common_down);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		down.setLayoutData(gridData);
		down.addSelectionListener(new SelectionAdapter() {

			// Add a task to the ExampleTaskList and refresh the view
			public void widgetSelected(SelectionEvent e) {
				// taskList.addTask();
			}
		});
	}
}
