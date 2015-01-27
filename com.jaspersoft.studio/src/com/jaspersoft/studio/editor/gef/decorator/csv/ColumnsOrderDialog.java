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
package com.jaspersoft.studio.editor.gef.decorator.csv;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

/**
 * A dialog to display all the csv column defined and to change their order
 * 
 * @author Orlandin Marco
 *
 */
public class ColumnsOrderDialog extends Dialog {

	/**
	 * The column names are shown in this table. the table has a single column with a cell 
	 * for each name.
	 */
	private Table orderTable;
	
	/**
	 * A textual representation of the column names
	 */
	private String[] columnNames;
	
	/**
	 * The result returned when the ok button is pressed. It a string with the sequence of the 
	 * columns as it were in the table, comma separated
	 */
	private String result;
	
	/**
	 * The button pressed to move a column up
	 */
	private Button upButton;
	
	/**
	 * The button pressed to move a column down
	 */
	private Button downButton;
	
	/**
	 * Image painted on the up button
	 */
	private static Image upImage;
	
	/**
	 * Image painted on the down button
	 */
	private static Image downImage;
	
	/**
	 * Listener used when the up or the down buttons are pressed
	 * 
	 * @author Orlandin Marco
	 *
	 */
	private class ButtonPress extends SelectionAdapter{
		
		/**
		 * Used to check if this adapter must perform an action of 
		 * up or down
		 */
		private boolean actionUp;
		
		/**
		 * Create the adapter
		 * @param actionUp true if the adapter is used to move a column up of a position,
		 * false otherwise
		 */
		public ButtonPress(boolean actionUp){
			this.actionUp = actionUp;
		}
		
		/**
		 * Move the selected column up or down of a position
		 */
		@Override
		public void widgetSelected(SelectionEvent e) {
				int index = orderTable.getSelectionIndex();
				int substitutionIndex = 0;
				if (actionUp) substitutionIndex = index - 1;
				else substitutionIndex = index + 1;
				TableItem substitutionItem = orderTable.getItem(substitutionIndex);
				String temp = substitutionItem.getText();
				TableItem originalItem = orderTable.getItem(index);
				substitutionItem.setText(originalItem.getText());
				originalItem.setText(temp);
				orderTable.setSelection(substitutionIndex);
				changeButtonSelectionState(substitutionIndex);
		}
	}
	
	/**
	 * Build the dialog
	 * @param parentShell
	 * @param columnNames list of the column names, comma separated
	 */
	public ColumnsOrderDialog(Shell parentShell, String columnNames) {
		super(parentShell);
		if (columnNames != null) this.columnNames = columnNames.split(","); 
		else this.columnNames = new String[0];
		result = ""; 
		if (upImage == null || downImage == null) {
			upImage = ResourceManager.getPluginImage(JaspersoftStudioPlugin.PLUGIN_ID, "icons/resources/arrow-curve-up.png"); //$NON-NLS-1$
			downImage =  ResourceManager.getPluginImage(JaspersoftStudioPlugin.PLUGIN_ID, "icons/resources/arrow-curve-down.png"); //$NON-NLS-1$
		}
	}
	
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(getDialogTitle());
	}
	
	/**
	 * Given a table item return its index in the table
	 * @param searchedItem a searched TableItem
	 * @return numerical index of the table item, -1 if it is not found
	 */
	private int indexByItem(TableItem searchedItem){
		for(int i=0; i<orderTable.getItemCount(); i++)
			if (orderTable.getItem(i) == searchedItem) return i;
		return -1;
	}
	
	/**
	 * Change the enabled state of the up an down buttons according the 
	 * Index of the selected column on the table. This is done for example 
	 * to disable the up button when the selected column is already on top, or 
	 * the down button when it is on the bottom
	 * @param selectedIndex index of the selected row (that correspond to a column) on 
	 * the table
	 */
	private void changeButtonSelectionState(int selectedIndex){
		if (selectedIndex == 0) {
			upButton.setEnabled(false);
			downButton.setEnabled(true);
		}
		if (selectedIndex == orderTable.getItemCount()-1){
			upButton.setEnabled(true);
			downButton.setEnabled(false);
		}
		if (selectedIndex > 0 && selectedIndex<orderTable.getItemCount()-1){
			upButton.setEnabled(true);
			downButton.setEnabled(true);
		}
	}

	/**
	 * When the ok button is pressed, before to dispose the widget, 
	 * the order of the column are converted from the rows of the table
	 * to string. In this string there are the ordered sequence of column name, 
	 * comma separated
	 */
	@Override
	protected void okPressed() {
		result = ""; //$NON-NLS-1$
		for(int i=0; i<orderTable.getItemCount(); i++){
			TableItem selectedItem = orderTable.getItem(i);
			result += selectedItem.getText();
			if (i!=orderTable.getItemCount()-1)
				result+=","; //$NON-NLS-1$
		}
		super.okPressed();
	};
	
	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		
		orderTable = new Table(container, SWT.BORDER);
		GridData tableData = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		tableData.widthHint = 300;
		orderTable.setLayoutData(tableData);
		orderTable.setRedraw(false);
		orderTable.setHeaderVisible(false);
		for(String colName : columnNames){
			TableItem item = new TableItem(orderTable, SWT.BORDER);
			item.setText(colName);
		}
		orderTable.setRedraw(true);
		orderTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				TableItem selectedItem = (TableItem)e.item;
				int selectedIndex = indexByItem(selectedItem);
				changeButtonSelectionState(selectedIndex);
			}
		});
		
		Composite buttonComposite = new Composite(container, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1,false));
		GridData buttonData = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		buttonData.widthHint = 35;
		buttonComposite.setLayoutData(buttonData);
		
		upButton = new Button(buttonComposite,SWT.NONE);
		upButton.setImage(upImage);
		upButton.setEnabled(false);
		upButton.addSelectionListener(new ButtonPress(true));
		downButton = new Button(buttonComposite,SWT.NONE);
		downButton.setImage(downImage);
		downButton.setEnabled(false);
		downButton.addSelectionListener(new ButtonPress(false));
		return area;
	}
	
	/**
	 * Ovveride of the createButton method, disable the ok button when it is created if the 
	 * list of columns is void (so if there aren't columns)
	 */
	@Override
	protected Button createButton(Composite parent, int id, String label, boolean defaultButton) {
		Button createdButton = super.createButton(parent, id, label, defaultButton);
		if (id == Dialog.OK && columnNames.length==0) createdButton.setEnabled(false);
		return createdButton;
	};
	
	/**
	 * return the chosen column order
	 * @return In this string there are the ordered sequence of column name, 
	 * comma separated
	 */
	public String getOrders(){
		return result;
	}
	
	/**
	 * @return the title for the dialog
	 */
	protected String getDialogTitle(){
		return Messages.ColumnsOrderDialog_ColumOrderDialog;
	}
}
