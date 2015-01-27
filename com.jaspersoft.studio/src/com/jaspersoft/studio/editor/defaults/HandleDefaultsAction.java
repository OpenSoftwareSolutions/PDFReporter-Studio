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
package com.jaspersoft.studio.editor.defaults;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

/**
 * Action to provide the menus to create, import, delete, edit or 
 * select the Teamplte Sets
 * 
 * @author Orlandin Marco
 *
 */
public class HandleDefaultsAction extends Action implements IMenuCreator {

	/**
	 * Id of the action
	 */
  public static final String ID = "HandleDefaultsAction"; //$NON-NLS-1$
  
  /**
   * List of the current available template sets
   */
  private List<String> values;
  
  /**
   * Flag used to request a recreation of the sew menu that list all the 
   * template sets
   */
  private boolean refreshItemList = true;
  
	/**
	 * The last menu generated
	 */
	private Menu menu;
  
  /**
   * Selection listener called when the button to delete the template set is pressed
   */
  private SelectionAdapter deleteTemplateSet = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			final Object data = e.widget.getData();
			if (data != null){
				
				String resourceName = getItemText(data.toString());
				String message = MessageFormat.format(Messages.HandleDefaultsAction_deleteMessage, new Object[]{resourceName});
				MessageDialog dialog = new MessageDialog(UIUtils.getShell(), Messages.HandleDefaultsAction_deleteTitle, null, message, MessageDialog.QUESTION, new String[]{Messages.HandleDefaultsAction_delteMenu, Messages.HandleDefaultsAction_deleteAll, Messages.HandleDefaultsAction_Abort}, 2); 
				int result = dialog.open();
				if (result == 0 || result == 1){		
					DefaultManager.INSTANCE.removeDefaultFile(data.toString());					
				}
				if (result == 1){
					IPath path = new Path(data.toString());
					IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
					IFile reportFile = 	workspace.getRoot().getFileForLocation(path);
					if (reportFile.exists()){
						try {
							
							reportFile.delete(true, new NullProgressMonitor());
						} catch (CoreException e1) {
							e1.printStackTrace();
						}
					}
				}
			}
		}
	};
	
  /**
   * Selection listener called when the button to edit the template set is pressed
   */
	private SelectionAdapter editTemplateSet = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			final Object data = e.widget.getData();
			if (data != null){
				Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						try {
							IPath path = new Path(data.toString());
							IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
							IFile reportFile = 	workspace.getRoot().getFileForLocation(path);
							if (reportFile.exists()){
								IDE.openEditor(page, reportFile, true);
							}
						} catch (PartInitException e) {
							e.printStackTrace();
						}
					}
				});
			}
		}
	};
	
  /**
   * Selection listener called when the button to select the template set is pressed
   */
	private SelectionAdapter useTemplateSet = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			Object data = e.widget.getData();
			if (data != null){
					refreshItemList = true;
					DefaultManager.INSTANCE.setDefaultFile(data.toString());
			}
		}
	};
	
  /**
   * Selection listener called when the button to import the template set is pressed
   */
	private SelectionAdapter importTemplateSet = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			JrxmlFilterDialog filterDialog = new JrxmlFilterDialog(UIUtils.getShell(), false);
			if (filterDialog.open() == WizardDialog.OK){
				for(Object result : filterDialog.getResult()){
					IFile file = (IFile) result;
					DefaultManager.INSTANCE.addDefaultFile(file.getRawLocation().toOSString(), false);
				}
				refreshItemList = true;
			}
		}
		
	};
	
  /**
   * Selection listener called when the button to create the template set is pressed
   */
	private SelectionAdapter createTemplateSet = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			DefaultNewWizard newWizard = new DefaultNewWizard();
			WizardDialog dialog = new WizardDialog(Display.getDefault().getActiveShell(), newWizard);
			if (dialog.open() == WizardDialog.OK){
				IFile templateFile = newWizard.getReportFile();
				String templatePath = templateFile.getRawLocation().makeAbsolute().toOSString();
				DefaultManager.INSTANCE.addDefaultFile(templatePath, false);
				createMenuElement(templatePath, false);
				refreshItemList = true;
			}
		}
		
	};
	
  /**
   * Selection listener called when the button to deselect the used template set
   */
	private SelectionAdapter deselectTemplateSet = new SelectionAdapter() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
				DefaultManager.INSTANCE.unsetDefaultFile();
				refreshItemList = true;
		}
	};
  

	public HandleDefaultsAction() {
		setText(Messages.HandleDefaultsAction_actionText);
		setMenuCreator(this);
		setId(ID);
	}
	
	/**
	 * If the last menu generated is not null
	 * the it is disposed
	 */
	@Override
	public void dispose() {
		if (menu != null){
			menu.dispose();
			menu = null;
		}
	}
	
	/**
	 * Build and return the menu for a control
	 */
	@Override
	public Menu getMenu(Control parent) {
		Menu rootMenu = new Menu(parent);
		getMenu(rootMenu);
		menu = rootMenu;
		return menu;
	}
	
	/**
	 * Generate the contextual menu to manage the template set. The menu is empty and
	 * it is initialized the first time it's opened
	 */
	@Override
	public Menu getMenu(Menu parent) {
		MenuItem root = new MenuItem(parent, SWT.CASCADE);
		menu = new Menu (parent);
		root.setMenu (menu);
		root.setText(Messages.HandleDefaultsAction_menuTitle);

		//Menu listener to show the current adapter highlighted on show
		menu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				if (refreshItemList || menu.getItemCount() == 0 || !DefaultManager.INSTANCE.getAvailableElements().equals(values)){
					for(MenuItem item : menu.getItems()){
						item.dispose();
					}
					initilizeItems();
					refreshItemList = false;
				}
			}
		});
		return menu;
	}
	
	/**
	 * Return the name of the template set from its complete path.
	 * The name is only the filename
	 * 
	 * @param fileName the complete path of the template set file
	 * @return the name of the template set
	 */
	private String getItemText(String fileName){
		File currentFile = new File(fileName);
		return currentFile.getName();
	}
	
	/**
	 * fill the contextual menu with the standard action (creation and import of a Template Set)
	 * plus a list of submenu for each template set.
	 */
	private void initilizeItems(){
		createDefaultElements();
		values = DefaultManager.INSTANCE.getAvailableElements();
		//The item to deselect all the template set is available only if there is 
		//at least one template set
		if (values.size()>0){
			MenuItem item = new MenuItem(menu, SWT.CHECK);
			item.setText(Messages.HandleDefaultsAction_unsetTemplate);
			item.addSelectionListener(deselectTemplateSet);
		}

		for(String fileName : values){
			boolean isDefault = DefaultManager.INSTANCE.isCurrentDefault(fileName);
			createMenuElement(fileName, isDefault);
		}
	}
	
	/**
	 * Create in the menu the standard action to import and create a new template
	 * set, these actions are always available
	 */
	private void createDefaultElements(){
		MenuItem item = new MenuItem(menu, SWT.CHECK);
		item.setText(Messages.HandleDefaultsAction_createTemplate);
		item.addSelectionListener(createTemplateSet);
		
		item = new MenuItem(menu, SWT.CHECK);
		item.setText(Messages.HandleDefaultsAction_importTemplate);
		item.addSelectionListener(importTemplateSet);
		
		if ( DefaultManager.INSTANCE.getAvailableElements().size()>0){
			new MenuItem(menu, SWT.SEPARATOR);
		}
	}
	
	/**
	 * Create the submenu item for a template set
	 * 
	 * @param fileName the name of the template set
	 * @param isSelected true if the template set is currently selected, false otherwise
	 */
	private void createMenuElement(String fileName, boolean isSelected){
		Menu itemSubmenu = new Menu(menu);
		MenuItem item = new MenuItem(menu, SWT.CASCADE);
		item.setMenu(itemSubmenu);
		String itemName = getItemText(fileName);
		if (isSelected){
			item.setImage(ResourceManager.getImage(JaspersoftStudioPlugin.getInstance().getImageDescriptor("icons/resources/check-16.png"))); //$NON-NLS-1$
			itemName+=Messages.HandleDefaultsAction_actualTemplateTag;
		}
		item.setText(itemName);
		item.setData(fileName);
		createMenuElementSubmenu(fileName, itemSubmenu, isSelected);
	}	
	
	/**
	 * Create the actions for a submenu of a template set to edit, select or delete the 
	 * template set. The select action is available only if the template set is currently
	 * not selected
	 * 
	 * @param fileName path of the template set file
	 * @param itemSubmenu the submenu where the actions will be added
	 * @param isSelected true if the template set is selected, false otherwise
	 */ 
	private void createMenuElementSubmenu(String fileName, Menu itemSubmenu, boolean isSelected){
		
		if (!isSelected){
			MenuItem selectDefault = new MenuItem(itemSubmenu, SWT.NONE);
			selectDefault.setText(Messages.HandleDefaultsAction_useTemplate);
			selectDefault.setData(fileName);
			selectDefault.addSelectionListener(useTemplateSet);
		}
		
		MenuItem openForEdit = new MenuItem(itemSubmenu, SWT.NONE);
		openForEdit.setText(Messages.HandleDefaultsAction_editTemplate);
		openForEdit.setData(fileName);
		openForEdit.addSelectionListener(editTemplateSet);
		
		MenuItem delete = new MenuItem(itemSubmenu, SWT.NONE);
		delete.setText(Messages.HandleDefaultsAction_deleteTemplate);
		delete.setData(fileName);
		delete.addSelectionListener(deleteTemplateSet);
	}
}
