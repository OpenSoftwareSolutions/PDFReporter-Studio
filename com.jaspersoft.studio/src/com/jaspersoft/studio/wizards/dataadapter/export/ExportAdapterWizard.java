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
package com.jaspersoft.studio.wizards.dataadapter.export;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.pde.internal.ui.wizards.exports.BaseExportWizardPage;
import org.eclipse.pde.internal.ui.wizards.exports.PluginExportWizard;
import org.eclipse.ui.IWorkbench;

/**
 * Wizard to export the data adapter plugin as a jar. It uses 
 * a custom store for the setting and a custom page
 * 
 * @author Orlandin Marco
 *
 */
@SuppressWarnings("restriction")
public class ExportAdapterWizard extends PluginExportWizard {
	
	/**
	 * The settings store id
	 */
	private static final String STORE_SECTION = "AdapterExportWizard"; //$NON-NLS-1$
	
	public ExportAdapterWizard(IWorkbench workbench, IStructuredSelection selection){
		super();
		init(workbench, selection);
	}
	
	/**
	 * Create a custom page instead of the standard one
	 */
	protected BaseExportWizardPage createPage1() {
		return new ExportAdapterWizardPage(getSelection());
	}
	
	/**
	 * Return the settings store id
	 */
	protected String getSettingsSectionName() {
		return STORE_SECTION;
	}

}
