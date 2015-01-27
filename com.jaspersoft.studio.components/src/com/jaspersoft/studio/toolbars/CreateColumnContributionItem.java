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
package com.jaspersoft.studio.toolbars;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.components.table.model.column.action.CreateColumnAction;

/**
 * Create the toolbar button to add a column to the selected table. The places where
 * the column is created is defined by the action provided by the subclasses
 * 
 * @author Orlandin Marco
 *
 */
public abstract class CreateColumnContributionItem extends CommonToolbarHandler {

	/**
	 * The button to press to activate the action
	 */
	protected ToolItem button;

	/**
	 * Action that will be executed to add the column, executed when the button is pressed
	 */
	protected CreateColumnAction createColumnAction = getAction();
	
	/**
	 * Method defined from the subclasses to get the correct action to create the column before,
	 * after, at the begin or a the end
	 * 
	 * @return the action to create the column, must be not null
	 */
	protected abstract CreateColumnAction getAction();
	
	/**
	 * Selection listener that create the right command when a button is pushed
	 */
	protected SelectionAdapter pushButtonPressed = new SelectionAdapter() {
		
	
		public void widgetSelected(SelectionEvent e) {
			createColumnAction.setWorkbenchPart(getWorkbenchPart());
			createColumnAction.execute(getLastRawSelection());
		}
	};
	
	/**
	 * Set the state of the button depending on the actual selection
	 */
	protected void setEnablement(){
		if (getWorkbenchPart() != null){
			if (button != null && !button.isDisposed()){
				createColumnAction.setWorkbenchPart(getWorkbenchPart());
				button.setEnabled(createColumnAction.canExecute(getLastRawSelection()));
			}
		}
	}
	
	@Override
	public void dispose() {
		super.dispose();
		if (button != null){
			button.dispose(); 
			button = null;
		}
	}
	
	@Override
	protected Control createControl(Composite parent) {
		
		ToolBar buttons = new ToolBar(parent, SWT.FLAT | SWT.WRAP);
		
		button = new ToolItem(buttons, SWT.PUSH);
		button.setImage(ResourceManager.getImage(createColumnAction.getImageDescriptor()));
		button.setToolTipText(createColumnAction.getToolTipText());
		button.addSelectionListener(pushButtonPressed);
		
		setEnablement();
		return buttons;
	}
}
