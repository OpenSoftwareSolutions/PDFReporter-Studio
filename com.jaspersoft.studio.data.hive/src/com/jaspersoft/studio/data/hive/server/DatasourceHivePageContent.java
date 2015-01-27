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
package com.jaspersoft.studio.data.hive.server;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.data.hive.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceCustom;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.UIUtil;

public class DatasourceHivePageContent extends APageContent {

	private Text tname;

	public DatasourceHivePageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public DatasourceHivePageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.datasource.hive";
	}

	@Override
	public String getName() {
		return Messages.RDDatasourceHivePage_title;
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, Messages.HiveDataAdapterComposite_labelurl);

		tname = new Text(composite, SWT.BORDER);
		tname.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		rebind();

		return composite;
	}

	@Override
	protected void rebind() {
		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MRDatasourceCustom.PROP_DATASOURCE_CUSTOM_PROPERTY_MAP, res.getValue().getProperties());

		resprop = ResourceDescriptorUtil.getProperty(MRDatasourceHadoopHive.JDBC_URL, resprop.getProperties());

		bindingContext.bindValue(SWTObservables.observeText(tname, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.adapter_hive";
	}
}
