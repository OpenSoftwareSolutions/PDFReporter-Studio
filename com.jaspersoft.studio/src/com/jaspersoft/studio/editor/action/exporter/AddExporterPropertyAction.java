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
package com.jaspersoft.studio.editor.action.exporter;

import java.util.ArrayList;

import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPart;

import com.jaspersoft.studio.editor.action.CustomSelectionAction;
import com.jaspersoft.studio.editor.action.IGlobalAction;
import com.jaspersoft.studio.editor.gef.decorator.csv.CSVElementDecorator;
import com.jaspersoft.studio.editor.gef.decorator.pdf.PDF508ElementDecorator;
import com.jaspersoft.studio.editor.gef.decorator.xls.XLSElementDecorator;

/**
 * This class implements an action that when run open a contextual menu on the pointer location.
 * This contextual menu will contains all the exporter related tags action  
 * 
 * @author Orlandin Marco
 *
 */
public class AddExporterPropertyAction extends SelectionAction implements IGlobalAction{
	
	/**
	 * Id of this action
	 */
	private final static String ID = "exporting_attributes_actions";

	/**
	 * Menu to open
	 */
	private Menu popupMenu;
	
	/**
	 * Manager that contain the structure of the menu
	 */
	private MenuManager manager;
	
	public AddExporterPropertyAction(IWorkbenchPart part) {
		super(part);
		setId(ID);
		popupMenu = null;
	}
	
	/**
	 * Recursive method to create a menu from the contribute items of a manger
	 * 
	 * @param menu actual menu
	 * @param items manager contributor items
	 */
	private void createMenu(Menu menu, IContributionItem[] items){
		for(IContributionItem item : items){
			if (item instanceof MenuManager){
				MenuManager manager = (MenuManager) item;
				MenuItem subMenuItem = new MenuItem(menu,SWT.CASCADE);
				subMenuItem.setText(manager.getMenuText());
				Menu subMenu = new Menu(Display.getCurrent().getActiveShell(), SWT.DROP_DOWN);
				subMenuItem.setMenu(subMenu);
				createMenu(subMenu, manager.getItems());
			} else if (item instanceof ActionContributionItem){
				ActionContributionItem actionItem = (ActionContributionItem) item;
				if (actionItem.getAction() instanceof CustomSelectionAction){
					final CustomSelectionAction action = (CustomSelectionAction)actionItem.getAction();
					MenuItem actionEnrty = new MenuItem(menu,SWT.CHECK);
					action.setSelection(getSelection());
					actionEnrty.setText(actionItem.getAction().getText());
					actionEnrty.setSelection(action.isChecked());
					actionEnrty.setEnabled(action.canExecute());
					actionEnrty.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							action.run();
						}
					});
				}
			}
		}
	}
	
	/**
	 * When the selection change the menu is rebuild
	 */
	@Override
	protected void setSelection(ISelection selection) {
		super.setSelection(selection);
		if (popupMenu != null && !popupMenu.isDisposed())
			popupMenu.dispose();
	}
	
	@Override
	protected boolean calculateEnabled() {
		return true;
	}
	
	/**
	 * Create the popoup menu about all the exporters. If the menu is already build
	 * this method do nothing.
	 */
	private void createPopupMenu(){
		if (popupMenu == null || popupMenu.isDisposed()){
			manager = new MenuManager();
			ActionRegistry registry = new ActionRegistry();
			//Create the PDF decorator
			PDF508ElementDecorator pdfDecorator = new PDF508ElementDecorator();
			pdfDecorator.registerActions(registry, new ArrayList<String>(), getWorkbenchPart());
			pdfDecorator.fillContextMenu(registry,manager);
			//Create the XLS decorator
			XLSElementDecorator xlsDecorator = new XLSElementDecorator();
			xlsDecorator.registerActions(registry, new ArrayList<String>(), getWorkbenchPart());
			xlsDecorator.fillContextMenu(registry,manager);
			//Create the CSV action
			CSVElementDecorator csvDecorator = new CSVElementDecorator();
			csvDecorator.registerActions(registry, new ArrayList<String>(), getWorkbenchPart());
			csvDecorator.fillContextMenu(registry,manager, (IStructuredSelection)getSelection());
			popupMenu = new Menu(Display.getCurrent().getActiveShell());
			createMenu(popupMenu, manager.getItems());
		}
	}
	
	/**
	 * Set the location of the popup menu on the mouse cursor
	 * @param menu
	 */
  protected void locatePopupMenu(Menu menu) {
    menu.setLocation(Display.getCurrent().getCursorLocation());
	}

	/**
	 * Show the popup menu and build it if it is necessary
	 */
	@Override
	public void run() {
		 createPopupMenu();
		 if (popupMenu.isVisible()) {
    	 popupMenu.setVisible(false);
     } else {
    	 locatePopupMenu(popupMenu);
       popupMenu.setVisible(true);
     }
	}

}
