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
package com.jaspersoft.studio.components.table.model.table.command.wizard;

import java.util.Map;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.property.dataset.wizard.WizardFieldsPage;
import com.jaspersoft.studio.wizards.JSSWizard;

public class TableWizardFieldsPage extends WizardFieldsPage {

	/**
	 * Key used to store the selected columns
	 */
	public static final String TABLE_COLUMNS = "TABLE_COLUMNS"; //$NON-NLS-1$
	
	
	public TableWizardFieldsPage()
	{
		super("tablewizard_columnfields"); //$NON-NLS-1$
		
		setTitle(Messages.TableWizardFieldsPage_title);
		setDescription(Messages.TableWizardFieldsPage_description);
	}
	
	
	/**
	 * This procedure initialize the dialog page with the list of available objects.
	 * This implementation looks for object set in the map as DISCOVERED_FIELDS.
	 * 
	 */
	public void loadSettings() {
		
		if (getSettings() == null) return;
		
		if (getWizard() instanceof TableWizard)
		{
			setAvailableFields(((TableWizard)getWizard()).getDataset().getFieldsList());
		} else
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
				
				settings.put(TableWizardFieldsPage.TABLE_COLUMNS,  getSelectedFields() ); 
			}
		
	}
}
