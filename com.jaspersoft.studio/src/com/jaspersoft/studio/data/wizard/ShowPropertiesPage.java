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
package com.jaspersoft.studio.data.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizardPage;

/**
 * Page of the wizard used to import the properties from a iReport configuration file into
 * Jaspersoft Studio. At this time only the properties with prefix net.sf.jasperreports. are read
 * 
 * @author Orlandin Marco
 *
 */
public class ShowPropertiesPage extends JSSWizardPage {

	/**
	 * List of the selected properties
	 */
	protected List<String> propertiesSelected = new ArrayList<String>();

	/**
	 * Table where the selected properties are shown
	 */
	protected Table table;

	/**
	 * Viewer of the  table, used to show the available properties that can be imported
	 */
	protected TableViewer leftTView;
	
	protected Label informationLabel;

	protected Composite buttonPanel;
	
	/**
	 * Properties map that contains the key\value pair for every properties available for 
	 * the import
	 */
	protected Properties prop = null;
	
	protected ShowPropertiesPage() {
		super("addcategories"); //$NON-NLS-1$
		setTitle(Messages.PropertiesPage_title);
		setDescription(Messages.PropertiesPage_description);
		setPageComplete(true);
	}
	
	
	@Override
	public void createControl(Composite parent) {
		
		Composite panel = new Composite(parent, SWT.NONE);
		panel.setLayout(new GridLayout(1,false));
		setControl(panel);
		
		Composite composite = new Composite(panel, SWT.NONE);	
		composite.setLayout(new GridLayout(1, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		informationLabel = new Label(composite, SWT.WRAP);
		informationLabel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		table = new Table(composite, SWT.CHECK | SWT.V_SCROLL | SWT.SINGLE | SWT.BORDER);
		table.setHeaderVisible(true);
		TableColumn[] col = new TableColumn[2];
		col[0] = new TableColumn(table, SWT.NONE);
		col[0].setText("Attributes Names");
		col[0].pack();
		col[1] = new TableColumn(table, SWT.NONE);
		col[1].setText("Values");
		col[1].pack();
		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(82, true));
		tlayout.addColumnData(new ColumnWeightData(15, true));
		table.setLayout(tlayout);
		table.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				storeSettings();
			}
		});
		
		GridData tableData = new GridData(SWT.FILL, SWT.FILL, true, true);
		tableData.minimumWidth = 400;
		tableData.minimumHeight = 300;
		table.setLayoutData(tableData);
		
		leftTView = new TableViewer(table);
		leftTView.setContentProvider(new ListContentProvider());
		ColumnViewerToolTipSupport.enableFor(leftTView, ToolTip.NO_RECREATE); 
		setLabelProvider(leftTView);
		
		buttonPanel = new Composite(composite, SWT.NONE);
		buttonPanel.setLayout(new GridLayout(2,true));
		GridData buttonData = new GridData(SWT.FILL, SWT.DEFAULT, true, false);
		buttonData.minimumHeight = 50;
		buttonData.heightHint = 60;
		buttonPanel.setLayoutData(buttonData);
		Button selectAllButton = new Button(buttonPanel, SWT.NONE);
		selectAllButton.setText("Select All");
		selectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAllAction(true);
			}
		});
		selectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));
		
		Button deselectAllButton = new Button(buttonPanel, SWT.NONE);
		deselectAllButton.setText("Deselect All");
		deselectAllButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				selectAllAction(false);
			}
		});
		deselectAllButton.setLayoutData(new GridData(SWT.FILL, SWT.DEFAULT, false, false));
	}
	
	private void selectAllAction(boolean selectionValue){
		for(TableItem item : table.getItems()){
			item.setChecked(selectionValue);
		}
		storeSettings();
	}
	
	
	/**
	 * When the page is shown update the left list with the available properties 
	 * and clear the second one
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible){
			leftTView.setInput(getInFields());
			informationLabel.setText("ATTENTION: Some properties may be dependent by the execution environment, i.e. they may require specific jars being in the classpath or point to paths in the filesystem that may not be longer valid.");
			buttonPanel.layout();
			informationLabel.getParent().layout();
		}
	}
	
	/**
	 * Return the keys of all the properties available for the import
	 * 
	 * @return a not null list of keys
	 */
	protected Collection<Object> getInFields(){
		List<Object> readKeys = new ArrayList<Object>();
		IReportDescriptor selectedConfig = ((ImportDataAdapterWizard)getWizard()).getSelectedConfiguration();
		prop = selectedConfig.getConfiguration();

		for(Entry<Object, Object> entry : prop.entrySet()){
			String key = entry.getKey().toString();
			if (key.startsWith("net.sf.jasperreports")){ //$NON-NLS-1$
				readKeys.add(key);
			} 
		}
		return readKeys;
	}
	
	/**
	 * Save the selected properties key into a list
	 */
	public void storeSettings()
	{
		propertiesSelected.clear();
		for(TableItem item : table.getItems()){
			if (item.getChecked()) propertiesSelected.add(item.getData().toString());
		}
	}
	
	/**
	 * Return the list of the selected properties keys
	 * 
	 * @return a not void list of properties keys to import
	 */
	public List<String> getProperties(){
		return propertiesSelected;
	}
	
	/**
	 * Set the label provider for the viewer of the tables. This 
	 * provider show as cell value the key of every propery and 
	 * as tooltip the string property_key=property_value
	 * 
	 * @param tableViewer
	 */
	protected void setLabelProvider(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				if (cell.getColumnIndex() == 0)
					cell.setText( cell.getElement().toString());
				else cell.setText(prop.getProperty(cell.getElement().toString()).toString());
			}

			
		  @Override
		  public String getToolTipText(Object element) {
		    return element.toString().concat("=").concat(prop.getProperty(element.toString())); //$NON-NLS-1$
		  }

		  @Override
		  public Point getToolTipShift(Object object) {
		    return new Point(5, 5);
		  }

		  @Override
		  public int getToolTipDisplayDelayTime(Object object) {
		    return 100; //msec
		  }

		  @Override
		  public int getToolTipTimeDisplayed(Object object) {
		    return 5000; //msec
		  }
		});
	}

	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_IMPORT_SELECT_PROPERTIES;
	}

}
