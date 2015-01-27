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
package com.jaspersoft.studio.data;

import net.sf.jasperreports.engine.JasperReportsContext;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

/*
 * A IReportConnectionEditor class provides a complete custom GUI for customizing a target IReportConnection.<br> Each
 * IReportConnectionEditor should inherit from the java.awt.Component class so it can be instantiated inside an AWT
 * dialog or panel.<br> Each IReportConnectionEditor should have a null constructor.<br>
 * 
 * @author gtoffoli
 */
public interface DataAdapterEditor {

	/**
	 * Set the DataAdapter to edit. Actually it is a copy of the original DataAdapter. It can be modifed by the user
	 * interface.<br>
	 * <br>
	 * 
	 * The copy of an DataAdapter is done instancing a new class of the same type and loading the properties stored by the
	 * first object
	 * 
	 * @param dataAdapter
	 *          DataAdapter to edit
	 */
	public void setDataAdapter(DataAdapterDescriptor dataAdapter);

	/**
	 * This method is called when the user completes to edit the datasource or when a datasource test is required.
	 * 
	 * @return IReportConnection modified. IT can be the same instance get in input with setIReportConnection or a new
	 *         one.
	 */
	public DataAdapterDescriptor getDataAdapter();

	/**
	 * This method allows to provide a UI component to edit the data adapter. The WizardPage reference is convenient for
	 * calling specific methods from WizardPage class like setMessage() method but this is not mandatory.
	 * 
	 * @param parent
	 * @param style
	 * @param wizardPage
	 *          can be null
	 * @return composite
	 */
	public ADataAdapterComposite getComposite(Composite parent, int style, WizardPage wizardPage,
			JasperReportsContext jrContext);

	/**
	 * This method returns the help context ID for the composite returned by getComposite()
	 * 
	 * @return String context ID, i.e:
	 * 
	 *         As possible default, the context id "com.jaspersoft.studio.doc.dataAdapters_wizard_list" can be return.
	 * 
	 */
	public String getHelpContextId();
}
