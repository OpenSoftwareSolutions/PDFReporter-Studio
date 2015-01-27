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
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.ToolTip;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.events.ChangeEvent;
import com.jaspersoft.studio.swt.events.ChangeListener;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.MoveT2TButtons;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizardPage;

/**
 * Page of the wizard used to import the properties from a iReport configuration file into
 * Jaspersoft Studio. At this time only the properties with prefix net.sf.jasperreports. are read
 * 
 * @author Orlandin Marco
 *
 */
public class PropertiesPage extends JSSWizardPage {

	/**
	 * List of the selected properties
	 */
	protected List<String> propertiesSelected = new ArrayList<String>();

	/**
	 * Table where the selected properties are shown
	 */
	protected Table rightTable;

	/**
	 * Viewer of the left table, used to show the available properties that can be imported
	 */
	protected TableViewer leftTView;
	
	/**
	 * Viewer of the right table, used to show the properties to import
	 */
	private TableViewer rightTView ;
	
	/**
	 * Set of buttons to manage the list...
	 */
	private MoveT2TButtons mt2t = null;
	
	/**
	 * Properties map that contains the key\value pair for every properties available for 
	 * the import
	 */
	protected Properties prop = null;
	
	protected PropertiesPage() {
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
		composite.setLayout(new GridLayout(4, false));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Composite leftPanel = new Composite(composite, SWT.NONE);
		
		GridLayout leftLayout = new GridLayout(2,false);
		leftLayout.horizontalSpacing = 0;
		leftLayout.verticalSpacing = 5;
		leftLayout.marginHeight = 0;
		leftLayout.marginWidth = 0;
		
		leftPanel.setLayout(leftLayout);
		leftPanel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		Table leftTable = new Table(leftPanel, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		gd.widthHint = 300;
		leftTable.setLayoutData(gd);
		leftTable.setHeaderVisible(true);

		TableColumn[] col = new TableColumn[1];
		col[0] = new TableColumn(leftTable, SWT.NONE);
		col[0].setText(Messages.PropertiesPage_availableColName);
		col[0].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		leftTable.setLayout(tlayout);
		
		
		leftTView = new TableViewer(leftTable);
		leftTView.setContentProvider(new ListContentProvider());
		ColumnViewerToolTipSupport.enableFor(leftTView, ToolTip.NO_RECREATE); 
		setLabelProvider(leftTView);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		// -----------------------------------
		rightTable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 300;
		rightTable.setLayoutData(gd);
		rightTable.setHeaderVisible(true);

		createColumns();

		rightTView = new TableViewer(rightTable);
		rightTView.setContentProvider(new ListContentProvider());
		ColumnViewerToolTipSupport.enableFor(rightTView, ToolTip.NO_RECREATE); 
		setLabelProvider(rightTView);
		
		mt2t = new MoveT2TButtons();
		mt2t.createButtons(bGroup, leftTView, rightTView);
		
		// Add listener to check for changes in the list...
		mt2t.addChangeListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event) {
					storeSettings();
			}
		});
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
			rightTView.setInput(new ArrayList<String>());
			rightTView.refresh();
		}
	}
	
	/**
	 * Return the keys of all the properties available for the import
	 * 
	 * @return a not null list of keys
	 */
	protected List<String> getInFields(){
		List<String> readKeys = new ArrayList<String>();
		IReportDescriptor selectedConfig = ((ImportDataAdapterWizard)getWizard()).getSelectedConfiguration();
		prop = selectedConfig.getConfiguration();
		Set<String> storedKeys = prop.stringPropertyNames();
		for(String key :storedKeys){
			if (key.startsWith("net.sf.jasperreports")){ //$NON-NLS-1$
				readKeys.add(key);
			}
		}
		return readKeys;
	}
	
	/**
	 * Save the selected properties key into a listt
	 */
	public void storeSettings()
	{
		propertiesSelected.clear();
		for(TableItem item : rightTable.getItems()){
			propertiesSelected.add(item.getData().toString());
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
	
	protected void createColumns() {
		TableColumn[] col;
		TableLayout tlayout;
		col = new TableColumn[1];
		col[0] = new TableColumn(rightTable, SWT.NONE);
		col[0].setText(Messages.PropertiesPage_importColName);
		col[0].pack();

		tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		rightTable.setLayout(tlayout);
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
				cell.setText( cell.getElement().toString());
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
