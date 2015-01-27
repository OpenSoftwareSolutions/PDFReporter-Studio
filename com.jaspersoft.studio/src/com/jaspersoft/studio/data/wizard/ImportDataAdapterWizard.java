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

import java.util.List;
import java.util.Properties;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.data.adapter.ImportUtility;
import com.jaspersoft.studio.preferences.util.PreferencesUtils;

/**
 * Wizard to import one of more data adapters definition from the previous installations of 
 * iReport
 * 
 * @author Orlandin Marco
 *
 */
public class ImportDataAdapterWizard extends Wizard implements IImportWizard {

	/**
	 * Page that list the ireport installations
	 */
	ListInstallationPage page0 = new ListInstallationPage();
	
	/**
	 * Page that list the available data adapters into a precise configurations
	 */
	ShowAdaptersPage page1 = new ShowAdaptersPage();
	
	/**
	 * Page that list the available properties into a precise configuration
	 */
	ShowPropertiesPage page2 = new ShowPropertiesPage();
	
	@Override
	public void addPages() {
		addPage(page0);
		addPage(page1);
		addPage(page2);
	}
	
	/**
	 * Return the descriptor of the configuration selected into the first step
	 * 
	 * @return a configuration descriptor file
	 */
	public IReportDescriptor getSelectedConfiguration(){
		return page0.getSelection();
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}

	/**
	 * Get the XML definition of every data adapter selected into the step one, and 
	 * from this build the data adapter and add it to the configuration
	 */
	@Override
	public boolean performFinish() {
		List<?> documents = page1.getSelectedAdapter();
		for(Object adapter : documents){
			Document doc = (Document) adapter;
			NamedNodeMap rootAttributes = doc.getChildNodes().item(0).getAttributes();
			String connectionClass = rootAttributes.getNamedItem("connectionClass").getTextContent();
			DataAdapterDescriptor newAdapter = ImportUtility.getAdapter(doc, connectionClass);
			DataAdapterManager.getPreferencesStorage().addDataAdapter("",newAdapter);
		}
		List<String> proeprties = page2.getProperties();
		Properties prop = getSelectedConfiguration().getConfiguration();
		String[] keys = proeprties.toArray(new String[proeprties.size()]);
		String[] values = new String[proeprties.size()];
		for(int i=0;i<keys.length; i++){
			values[i] = prop.getProperty(keys[i]);
		}
		PreferencesUtils.storeJasperReportsProperty(keys, values);
		return true;
	}

}
