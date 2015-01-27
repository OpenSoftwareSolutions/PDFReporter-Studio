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
package com.jaspersoft.studio.editor.action;

import java.util.ArrayList;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.data.storage.FileDataAdapterStorage;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.dataset.dialog.DataQueryAdapters;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Action to switch the main data adapter of the report. This is not a real 
 * action since the run method is empty. It instead create a separated menu 
 * with the appropriated selection listener to do the switch. However it 
 * must extend action to be contributed
 * 
 * @author Orlandin Marco
 * 
 */
public class SelectDataAdapterAction extends Action implements IMenuCreator {

	/**
	 * Configuration of the current report
	 */
	private JasperReportsConfiguration jConfig;
	
	/**
	 * Cached list of the available DataAdapters
	 */
	private DataAdapterDescriptor[] values = null;
	
	/**
	 * Has the same size of values and this string are simply the human readable
	 * name of the data adapter mapped 1:1 with values
	 */
	private String[] names = null;
	
	/**
	 * The last menu generated
	 */
	private Menu menu;

	/**
	 * Id of the action
	 */
  public static final String ID = "SelectDataAdapterAction"; //$NON-NLS-1$
	
  /**
   * If the DataAdapter array is null it initialize both it and the names array,
   * otherwise it dosen't do nothing
   * 
   */
	private void initAvailableValues(){
		if (values == null){
			IFile file = (IFile) jConfig.get(FileUtils.KEY_FILE);
			ADataAdapterStorage[] dastorages = DataAdapterManager.getDataAdapter(file);
			ArrayList<String> namesList = new ArrayList<String>();
			ArrayList<DataAdapterDescriptor> valuesList = new ArrayList<DataAdapterDescriptor>();
			for (int i = 0; i < dastorages.length; i++) {
				final ADataAdapterStorage s = dastorages[i];
				for (DataAdapterDescriptor d : s.getDataAdapterDescriptors()) {
					valuesList.add(d);
					if (s instanceof FileDataAdapterStorage)
						namesList.add(d.getName() + " - [" + s.getUrl(d) + "]"); //$NON-NLS-1$ //$NON-NLS-2$
					else
						namesList.add(d.getName());

				}
			}
			values = valuesList.toArray(new DataAdapterDescriptor[valuesList.size()]);
			names = namesList.toArray(new String[namesList.size()]);
		}
	}


	/**
	 * Method to create the No Data Adapter option, it is actually
	 * unused
	 * 
	 * @param menu menu where the item is placed
	 */
	@SuppressWarnings("unused")
	private void createEmptyItem(Menu menu){
		MenuItem item = new MenuItem(menu, SWT.CHECK);
		item.setText(" -- No Data Adapter -- "); //$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				jConfig.getJasperDesign().removeProperty(DataQueryAdapters.DEFAULT_DATAADAPTER);
			}
		});
	}
	
	/**
	 * Build the action
	 * 
	 * @param jConfig the jasper configuration of the current report
	 */
	public SelectDataAdapterAction(JasperReportsConfiguration jConfig) {
		super();
		setText(Messages.SelectDataAdapterAction_actionTitle); 
		setId(ID);
		this.jConfig = jConfig;
		setEnabled(false);
	}

	/**
	 * Set the main data adapter for the current report
	 * 
	 * @param desc adapter to set
	 */
	public void setSelectedAdapter(DataAdapterDescriptor desc){
		jConfig.getJasperDesign().setProperty(DataQueryAdapters.DEFAULT_DATAADAPTER, desc.getName());
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
	 * Generate the contextual menu that list all the available data 
	 * adapters and when one of them is choose then it is set as main
	 * data adapter on the current report. The selected one is also
	 * shown highlighted inside the list
	 */
	@Override
	public Menu getMenu(Menu parent) {
		MenuItem root = new MenuItem(parent, SWT.CASCADE);
		menu = new Menu (parent);
		root.setMenu (menu);
		root.setText(Messages.SelectDataAdapterAction_actionTitle);
		initAvailableValues();
		//createEmptyItem(subMenu);
		for(int i=0; i<values.length;i++){
			DataAdapterDescriptor desc = values[i];
			MenuItem item = new MenuItem(menu, SWT.CHECK);
			item.setText(names[i]);
			item.setImage(JaspersoftStudioPlugin.getInstance().getImage(desc.getIcon16()));
			item.addSelectionListener(new SelectionAdapter() {
				
				@Override
				public void widgetSelected(SelectionEvent e) {
					Object data = e.widget.getData();
					if (data != null && data instanceof DataAdapterDescriptor)
						setSelectedAdapter((DataAdapterDescriptor)data);
				}
			});
			item.setData(desc);
		}
		
		//Menu listener to show the current adapter highlighted on show
		menu.addMenuListener(new MenuAdapter() {
			@Override
			public void menuShown(MenuEvent e) {
				String selectedtem = jConfig.getJasperDesign().getProperty(DataQueryAdapters.DEFAULT_DATAADAPTER);
				//if (selectedtem == null){
				//	subMenu.getItems()[0].setSelection(true);
				//} else {
					for(MenuItem item : menu.getItems()){
						item.setSelection(item.getData() != null && item.getData() instanceof DataAdapterDescriptor && ((DataAdapterDescriptor)item.getData()).getName().equals(selectedtem));
					}
				//}
			}
		});
		return menu;
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
}
