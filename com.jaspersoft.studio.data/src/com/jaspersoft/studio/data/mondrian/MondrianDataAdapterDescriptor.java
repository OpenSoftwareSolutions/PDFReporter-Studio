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
package com.jaspersoft.studio.data.mondrian;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;

import net.sf.jasperreports.data.mondrian.MondrianDataAdapterImpl;
import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.AWizardDataEditorComposite;
import com.jaspersoft.studio.data.DataAdapterEditor;
import com.jaspersoft.studio.data.jdbc.JDBCDataAdapterDescriptor;
import com.jaspersoft.studio.data.ui.WizardQueryEditorComposite;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * @author gtoffoli
 *
 */
public class MondrianDataAdapterDescriptor extends JDBCDataAdapterDescriptor {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MondrianDataAdapterDescriptor() {
		setDataAdapter(new MondrianDataAdapterImpl());
	}

	@Override
	public DataAdapterEditor getEditor() {
		return new MondrianDataAdapterEditor();
	}

	@Override
	public void getFieldProvider() {
		if (fprovider == null)
			fprovider = new MondrianFieldsProvider();
	}

	@Override
	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		getFieldProvider();
		return fprovider.supportsGetFieldsOperation(jConfig);
	}
	
	@Override
	public AWizardDataEditorComposite createDataEditorComposite(Composite parent, WizardPage page) {
		return new WizardQueryEditorComposite(parent, page, this, "mdx");
	}

}
