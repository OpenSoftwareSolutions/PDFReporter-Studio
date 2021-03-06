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
package com.jaspersoft.studio.data.hibernate;

import net.sf.jasperreports.data.hibernate.HibernateDataAdapter;
import net.sf.jasperreports.data.hibernate.HibernateDataAdapterImpl;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.data.AWizardDataEditorComposite;
import com.jaspersoft.studio.data.Activator;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;
import com.jaspersoft.studio.data.IWizardDataEditorProvider;
import com.jaspersoft.studio.data.ui.WizardQueryEditorComposite;

public class HibernateDataAdapterDescriptor extends DataAdapterDescriptor implements IWizardDataEditorProvider {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public HibernateDataAdapter getDataAdapter() {
		if (dataAdapter == null)
			dataAdapter = new HibernateDataAdapterImpl();
		return (HibernateDataAdapter) dataAdapter;
	}

	@Override
	public DataAdapterEditor getEditor() {
		return new HibernateDataAdapterEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#getIcon(int)
	 */
	@Override
	public Image getIcon(int size) {
		if (size == 16) {
			return Activator.getDefault().getImage("icons/hibernate.png"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public AWizardDataEditorComposite createDataEditorComposite(Composite parent, WizardPage page) {
		return new WizardQueryEditorComposite(parent, page, this, "sql");
	}
}
