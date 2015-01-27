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
package com.jaspersoft.studio.property.dataset.wizard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.dataset.TLabelProvider;
import com.jaspersoft.studio.swt.events.ChangeEvent;
import com.jaspersoft.studio.swt.events.ChangeListener;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.MoveT2TButtons;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardPage;

public class WizardFieldsPage extends JSSWizardPage {
	protected List<Object> inFields;
	protected List<Object> outFields;

	protected Table rightTable;
	private Table leftTable;
	protected TableViewer rightTView;
	protected TableViewer leftTView;
	protected Composite mainComposite;
	
	/**
	 * Set of buttons to manage the list...
	 */
	private MoveT2TButtons mt2t = null;
	private ListOrderButtons lob = null;

	public WizardFieldsPage(String key) {
		super(key); //$NON-NLS-1$
		setTitle(Messages.common_fields);
		setDescription(Messages.WizardFieldsPage_description);
	
		inFields = new ArrayList<Object>();
		outFields = new ArrayList<Object>();
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_SELECT_FIELDS;
	}

	/**
	 * @wbp.parser.constructor
	 * 
	 */
	public WizardFieldsPage() {
		this("tablepage"); //$NON-NLS-1$
	}

	public void createControl(Composite parent) {
		mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(4, false));
		setControl(mainComposite);

		leftTable = new Table(mainComposite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 300;
		leftTable.setLayoutData(gd);
		leftTable.setHeaderVisible(true);

		TableColumn[] col = new TableColumn[1];
		col[0] = new TableColumn(leftTable, SWT.NONE);
		col[0].setText(Messages.WizardFieldsPage_dataset_fields);
		col[0].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		leftTable.setLayout(tlayout);

		leftTView = new TableViewer(leftTable);
		leftTView.setContentProvider(new ListContentProvider());
		setLabelProvider(leftTView);

		Composite bGroup = new Composite(mainComposite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		// -----------------------------------
		rightTable = new Table(mainComposite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH);
		gd.minimumWidth = 300;
		rightTable.setLayoutData(gd);
		rightTable.setHeaderVisible(true);

		createColumns();

		rightTView = new TableViewer(rightTable);
		rightTView.setContentProvider(new ListContentProvider());
		setLabelProvider(rightTView);
		attachCellEditors(rightTView, rightTable);
		

		createOrderButtons(mainComposite);
		
		leftTView.setInput(inFields);
		rightTView.setInput(outFields);
		
		mt2t = new MoveT2TButtons();
		mt2t.createButtons(bGroup, leftTView, rightTView);
		
		// Add listener to check for changes in the list...
		mt2t.addChangeListener(new ChangeListener() {
			
			@Override
			public void changed(ChangeEvent event) {
					// When the set of selected fields changes, we need to
				  // reflect the selection in the settings
				  // stored by this page...
					storeSettings();
					fireChangeEvent();
			}
		});
		
	}

	protected void attachCellEditors(TableViewer viewer, Composite parent) {

	}

	protected void setLabelProvider(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new TLabelProvider());
	}

	protected void rightTView(TableViewer tableViewer) {
		tableViewer.setLabelProvider(new TLabelProvider());
	}

	protected void createColumns() {
		TableColumn[] col;
		TableLayout tlayout;
		col = new TableColumn[1];
		col[0] = new TableColumn(rightTable, SWT.NONE);
		col[0].setText(Messages.common_fields);
		col[0].pack();

		tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		rightTable.setLayout(tlayout);
	}

	private void createOrderButtons(Composite composite) {
		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		lob = new ListOrderButtons();
		
		lob.createOrderButtons(bGroup, rightTView);
		
		lob.addChangeListener(new ChangeListener() {
			
				@Override
				public void changed(ChangeEvent event) {
						// When the set of selected fields changes, we need to
					  // reflect the selection in the settings
					  // stored by this page...
						storeSettings();
						fireChangeEvent();
				}
			});
	}

	/**
	 * We use the setVisible(true) entry point to load the UI with loadSettings()...
	 */
	@Override
	public void setVisible(boolean visible) {
		super.setVisible(visible);
		if (visible == true)
		{
			loadSettings();
			// We also need to store the settings now
			// in case somethig has changed during loading
			storeSettings();
			fireChangeEvent();
		}
	}
	

	/**
	 * This procedure initialize the dialog page with the list of available objects.
	 * This implementation looks for object set in the map as DISCOVERED_FIELDS.
	 * 
	 */
	public void loadSettings() {
		
		if (getSettings() == null) return;
		
		if (getSettings().containsKey( WizardDataSourcePage.DISCOVERED_FIELDS))
		{
			setAvailableFields( (List<?>)(getSettings().get( WizardDataSourcePage.DISCOVERED_FIELDS )) );
		}
		else
		{
			setAvailableFields(null);
		}
	}
	
	
	/**
	 * Every time a new selection occurs, the selected fields are stored in the settings map
	 * with the key WizardDataSourcePage.DATASET_FIELDS
	 */
	public void storeSettings()
	{
		if (getWizard() instanceof JSSWizard &&
				getWizard() != null)
			{
				Map<String, Object> settings = ((JSSWizard)getWizard()).getSettings();
			
				if (settings == null) return;
				
				settings.put(WizardDataSourcePage.DATASET_FIELDS,  getSelectedFields() ); 
			}
		
	}
	
	
	/**
	 * Set the list of available fields.
	 * Right column fields with the current selection is preserved where possible...
	 * 
	 * @param inFields
	 */
	public void setAvailableFields(List<?> fields) {
		
		if (fields == null)
		{
			inFields.clear();
			outFields.clear();
		}
		else
		{
			// Check the selection and remove the object which are not in the inFields list...
			inFields.clear();
			inFields.addAll(fields);

			// check is performed by a special overrideable function..
			// We assume that objects with the same name are ok.
			for (int i=0; i<outFields.size(); ++i)
			{
				Object obj = findElement(outFields.get(i), inFields);
				
				if (obj != null)
				{
					inFields.remove(obj);
				}
				else
				{
					outFields.remove(i);
					i--;
				}
			}
		}
		
		rightTView.refresh();
		leftTView.refresh();
		
	}

	
	/**
	 * This function checks if a particular right element is in the provided list, 
	 * and which is the matching element in that list.
	 * 
	 * This implementation is based on the string value returned by
	 * left and right getText label providers
	 * 
	 * @param object
	 * @param fields
	 * @return
	 */
	protected Object findElement(Object object, List<?> fields) {
		
		String objName = ((LabelProvider)rightTView.getLabelProvider()).getText(object);
		for (Object obj : fields)
		{
			if (((LabelProvider)leftTView.getLabelProvider()).getText(obj).equals(objName))
			{
				return obj;
			}
		}
		return null;
	}

	/**
	 * @return the inFields
	 */
	public List<Object> getAvailableFields() {
		return new ArrayList<Object>( inFields );
	}

	/**
	 * Returns a copy of the selected fields;
	 * 
	 * @return the outFields
	 */
	public List<Object> getSelectedFields() {
		return new ArrayList<Object>( outFields );
	}

	
	
}
