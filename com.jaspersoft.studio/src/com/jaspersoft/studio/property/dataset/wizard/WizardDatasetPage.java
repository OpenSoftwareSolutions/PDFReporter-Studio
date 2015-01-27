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

import java.text.MessageFormat;

import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.dataset.MDatasetRun;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.AWizardNode;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardSelectionPage;


/**
 * Special dataset wizard page useful to pick up an existing dataset, create a new one
 * or just don't set any dataset at all.
 * 
 * @author gtoffoli
 *
 */
public class WizardDatasetPage extends JSSWizardSelectionPage {
	
	public static final int NEW_DATASET = 0;
	public static final int EXISTING_DATASET = 1;
	public static final int NO_DATASET = 2;
	
	protected int selectedOption = -1;
	protected String selectedDatasetName = null;
	protected boolean mainDataset = false;
	
	
	/**
	 * If the user has selected to use an existing dataset, this
	 * method gives it back..
	 * 
	 * @return the selected dataset name
	 */
	public String getSelectedDatasetName() {
		return selectedDatasetName;
	}

	/**
	 * Specify if the existing dataset selected is also the main dataset
	 * 
	 * @return
	 */
	public boolean isMainDataset() {
		return mainDataset;
	}

	/**
	 * Return the user selection. Possible values are:</br>
	 * </br>
	 * <ul>
	 * <li>WizardDatasetPage.NEW_DATASET</li>
	 * <li>WizardDatasetPage.EXISTING_DATASET</li>
	 * <li>WizardDatasetPage.NO_DATASET</li>
	 * </ul>
	 * 
	 * @param selectedOption
	 */
	public int getSelectedOption() {
		return selectedOption;
	}


	/**
	 * This method returns the final dataset selected by the user.
	 * If the dataset is an exising one, it is taken from the
	 * jasperdesign in the wizard configuration.
	 * If the dataset is empty, this method returns null.
	 * If the dataset is a new dataset, a temporary new dataset is returned. This last dataset
	 * is actually created by the sub-wizard to create the dataset.
	 * 
	 * @return
	 */
	public JRDesignDataset getSelectedDataset()
	{
			switch (getSelectedOption())
			{
				case NEW_DATASET:
				{
					// We ask for a dataset form the sub-wizard we just run...
					if (getSelectedNode() != null && getSelectedNode().getWizard() != null)
					{
						DatasetWizard dw = (DatasetWizard)getSelectedNode().getWizard();
						return dw.getDataset();
					}
					
					return null;
				}
				case EXISTING_DATASET:
				{
					if (isMainDataset()) return getJasperDesign().getMainDesignDataset();
					return (JRDesignDataset)getJasperDesign().getDatasetMap().get( getSelectedDatasetName() );
				}
				case NO_DATASET:
				default:
				{
					return null;
				}
			}
	}
	
	//private MDatasetRun datasetrun;
	
	
	private boolean acceptMainDataset = true;

	// UI components...
	private Combo comboBoxDatasets;
	private Button radioButtonAddDataset;
	private Button radioButtonUseDataset;
	//private Button radioButtonNoDataset;

	/**
	 * 
	 * @deprecated This method does not do anything anymore.
	 * 
	 * @param datasetrun
	 */
	public void setDataSetRun(MDatasetRun datasetrun) {
		
	}


	/**
	 * 
	 * @deprecated This method returns always null.
	 * 
	 * @param datasetrun
	 */
	public MDatasetRun getDataSetRun() {
		return null;
	}

	private String componentName = "component"; //$NON-NLS-1$

	/**
	 * Create a new WizardDatasetPage which allows to pick any dataset (including the main one).
	 * 
	 * @wbp.parser.constructor 
	 * 
	 * @param jConfig
	 * @param componentName
	 */
	public WizardDatasetPage(String componentName) {
		this(true, componentName);
	}

	/**
	 * Create a new WizardDatasetPage which allows to pick a dataset
	 * 
	 * @param jConfig
	 * @param acceptMainDataset - true to add the main dataset in the set of selectable datasets
	 * @param componentName
	 */
	public WizardDatasetPage(boolean acceptMainDataset, String componentName) {
		
		super("datasetpage"); //$NON-NLS-1$
		setTitle(Messages.common_dataset);
		setImageDescriptor(MDataset.getIconDescriptor().getIcon32());
		setDescription(Messages.WizardDatasetPage_description);
		this.acceptMainDataset = acceptMainDataset;
		this.componentName = componentName;
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_SELECT_TABLE_DATASET;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		setControl(composite);
		composite.setLayout(new FormLayout());

		radioButtonAddDataset = new Button(composite, SWT.RADIO);
		FormData fd_addDataset = new FormData();
		fd_addDataset.left = new FormAttachment(0, 5);
		fd_addDataset.top = new FormAttachment(0, 10);
		radioButtonAddDataset.setLayoutData(fd_addDataset);
	
		radioButtonAddDataset.setText(MessageFormat.format(Messages.WizardDatasetPage_createDataset, new Object[]{componentName}));
		radioButtonAddDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//datasetrun = null;
				handleOptionSelected();
			}

		});

		radioButtonUseDataset = new Button(composite, SWT.RADIO);
		FormData fd_selDataset = new FormData();
		fd_selDataset.top = new FormAttachment(0, 55);
		fd_selDataset.left = new FormAttachment(0, 5);
		radioButtonUseDataset.setLayoutData(fd_selDataset);
		radioButtonUseDataset.setText(MessageFormat.format(Messages.WizardDatasetPage_existingDataset, new Object[]{componentName}));
		radioButtonUseDataset.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setSelectedNode(null);
				comboBoxDatasets.setEnabled(radioButtonUseDataset.getSelection());
				setPageComplete(comboBoxDatasets.getSelectionIndex() >= 0);
			}

		});

		String[] dsNames = ModelUtils.getDataSets(getJasperDesign(), acceptMainDataset);

		comboBoxDatasets = new Combo(composite, SWT.READ_ONLY);
		FormData fd_datasets = new FormData();
		fd_datasets.right = new FormAttachment(100, -48);
		fd_datasets.top = new FormAttachment(0, 79);
		fd_datasets.left = new FormAttachment(0, 30);
		comboBoxDatasets.setLayoutData(fd_datasets);
		comboBoxDatasets.setItems(dsNames);

		comboBoxDatasets.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				handleComboBoxDatasetSelected(event);
			}
		});
		
		if (comboBoxDatasets.getItemCount() > 0)
		{
			comboBoxDatasets.select(0);
			handleComboBoxDatasetSelected(new Event());
		}
		else
		{
			radioButtonUseDataset.setEnabled(false);
		}
		
		
		if (dsNames.length > 0) {
			radioButtonUseDataset.setSelection(true);
			comboBoxDatasets.setEnabled(true);
		} else {
			radioButtonAddDataset.setSelection(true);
		}
		
		handleOptionSelected();
		
	}

	
	
	
	
	protected void handleOptionSelected() {
		
		storeSettings();
		comboBoxDatasets.setEnabled(radioButtonUseDataset.getSelection());
		if (radioButtonAddDataset.getSelection()) {
			setSelectedNode(new AWizardNode() {
				public IWizard createWizard() {
					IWizard pwizard = WizardDatasetPage.this.getWizard();
					DatasetWizard w = new DatasetWizard(pwizard, pwizard.getNextPage(WizardDatasetPage.this));
					if (pwizard instanceof JSSWizard)
						w.setConfig( ((JSSWizard) pwizard).getConfig() );
					return w;
				}
			});
			setPageComplete(true);
		}
	}
	
	
	
	private void handleComboBoxDatasetSelected(Event event) {
		
//		if (comboBoxDatasets.getSelectionIndex() == 0 && acceptMainDataset)
//		{
//			datasetrun.setPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME, null);
//		}
//		else if (comboBoxDatasets.getSelectionIndex() >= 0 && comboBoxDatasets.getSelectionIndex() < comboBoxDatasets.getItemCount())
//		{
//			datasetrun.setPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME,comboBoxDatasets.getItem(comboBoxDatasets.getSelectionIndex()));
//		}

		storeSettings();
	}

	
	/**
	 * Saves the local variables which hold the information provided by the user.
	 * 
	 */
	public void storeSettings()
	{
		mainDataset = false;
		selectedDatasetName = null;
		selectedOption = 0;
		
		if (radioButtonUseDataset.getSelection()) selectedOption = 1;
		//if (radioButtonNoDataset.getSelection()) selectedOption = 2;

		if (selectedOption == 1)
		{
			if (comboBoxDatasets.getSelectionIndex() >= 0)
			{
				if (comboBoxDatasets.getSelectionIndex() == 0 && acceptMainDataset)
				{
					mainDataset = true;
				}
				selectedDatasetName = comboBoxDatasets.getItem(comboBoxDatasets.getSelectionIndex());
			}
		}
	}
	
	
	
	/**
	 * Convenient method to look for a JasperDesign object inside the settings->configuration
	 * @return
	 */
	private JasperDesign getJasperDesign()
	{
	// Settings and JasperReportsConfiguration should be null only in case of wizard miss configuration..)
		if (getSettings() == null || getSettings().get( JSSWizard.JASPERREPORTS_CONFIGURATION ) == null) return null;
		return ((JasperReportsConfiguration)getSettings().get( JSSWizard.JASPERREPORTS_CONFIGURATION )).getJasperDesign();
	}
}
