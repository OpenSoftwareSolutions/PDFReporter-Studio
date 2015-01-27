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

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.util.CastorUtil;

import org.eclipse.core.runtime.Status;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.IImportWizard;
import org.eclipse.ui.IWorkbench;
import org.w3c.dom.Node;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.util.PreferencesUtils;

/**
 * Wizard to import one of more data adapters definition from the previous installations of 
 * iReport
 * 
 * @author Orlandin Marco
 *
 */
public class ImportJSSAdapterWizard extends Wizard implements IImportWizard {

	/**
	 * Page that list the ireport installations
	 */
	private SelectWorkspacePage page0 = new SelectWorkspacePage();
	
	/**
	 * Page that list the available data adapters into a precise configurations
	 */
	private ShowJSSAdaptersPage page1 = new ShowJSSAdaptersPage();
	
	/**
	 * Page that list the available properties into a precise configuration
	 */
	private JSSPropertiesPage page2 = new JSSPropertiesPage();
	
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
	

	private void addAdapters() {
			try {
				List<?> adapterNodes = page1.getSelectedAdapter();
				for (int i = 0; i < adapterNodes.size(); ++i) {
					Node adapterNode = (Node)adapterNodes.get(i);

					if (adapterNode.getNodeType() == Node.ELEMENT_NODE) {
						// 1. Find out the class of this data adapter...
						String adapterClassName = adapterNode.getAttributes().getNamedItem("class").getNodeValue(); //$NON-NLS-1$

						DataAdapterFactory factory = DataAdapterManager.findFactoryByDataAdapterClass(adapterClassName);

						if (factory == null) {
							// we should at least log a warning here....
							JaspersoftStudioPlugin
									.getInstance()
									.getLog()
									.log(
											new Status(Status.WARNING, JaspersoftStudioPlugin.getUniqueIdentifier(), Status.OK,
													Messages.DataAdapterManager_nodataadapterfound + adapterClassName, null));
							continue;
						}

						DataAdapterDescriptor dataAdapterDescriptor = factory.createDataAdapter();
						DataAdapter dataAdapter = dataAdapterDescriptor.getDataAdapter();
						dataAdapter = (DataAdapter) CastorUtil.read(adapterNode, dataAdapter.getClass());
						dataAdapterDescriptor.setDataAdapter(dataAdapter);
						DataAdapterManager.getPreferencesStorage().addDataAdapter("",dataAdapterDescriptor);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
	}
	
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {}

	/**
	 * Get the XML definition of every data adapter selected into the step one, and 
	 * from this build the data adapter and add it to the configuration
	 */
	@Override
	public boolean performFinish() {
		addAdapters();
		List<String> proeprties = page2.getProperties();
		String[] keys = proeprties.toArray(new String[proeprties.size()]);
		String[] values = new String[proeprties.size()];
		for(int i=0;i<keys.length; i++){
			values[i] = page2.getProperyValue(keys[i]); 
		}
		PreferencesUtils.storeJasperReportsProperty(keys, values);
		return true;
	}

}
