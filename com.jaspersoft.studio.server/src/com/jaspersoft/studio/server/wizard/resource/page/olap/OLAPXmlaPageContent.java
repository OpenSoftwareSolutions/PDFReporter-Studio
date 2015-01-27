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
package com.jaspersoft.studio.server.wizard.resource.page.olap;

import java.util.List;

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
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MROlapXmlaConnection;
import com.jaspersoft.studio.server.utils.ResourceDescriptorUtil;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.utils.UIUtil;

public class OLAPXmlaPageContent extends APageContent {

	private Text tdatasource;
	private Text tuser;
	private Text tpass;
	private Text tcatalog;
	private Text turi;

	public OLAPXmlaPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public OLAPXmlaPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.wizard.resource.page.olap.xmla"; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return MROlapXmlaConnection.getIconDescriptor().getTitle();
	}

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		UIUtil.createLabel(composite, Messages.OLAPXmlaPageContent_uti);

		turi = new Text(composite, SWT.BORDER);
		turi.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, Messages.OLAPXmlaPageContent_catalog);

		tcatalog = new Text(composite, SWT.BORDER);
		tcatalog.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, Messages.OLAPXmlaPageContent_datasource);

		tdatasource = new Text(composite, SWT.BORDER);
		tdatasource.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		UIUtil.createLabel(composite, Messages.OLAPXmlaPageContent_username);

		tuser = new Text(composite, SWT.BORDER);
		tuser.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// if (res.getValue().getIsNew()) {
		UIUtil.createLabel(composite, Messages.OLAPXmlaPageContent_pass);

		tpass = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		tpass.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		// }
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {
		List<ResourceProperty> props = res.getValue().getProperties();
		ResourceProperty resprop = ResourceDescriptorUtil.getProperty(MROlapXmlaConnection.PROP_XMLA_URI, props);

		bindingContext.bindValue(SWTObservables.observeText(turi, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MROlapXmlaConnection.PROP_XMLA_CATALOG, props);

		bindingContext.bindValue(SWTObservables.observeText(tcatalog, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MROlapXmlaConnection.PROP_XMLA_DATASOURCE, props);

		bindingContext.bindValue(SWTObservables.observeText(tdatasource, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MROlapXmlaConnection.PROP_XMLA_USERNAME, props);

		bindingContext.bindValue(SWTObservables.observeText(tuser, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$

		resprop = ResourceDescriptorUtil.getProperty(MROlapXmlaConnection.PROP_XMLA_PASSWORD, props);

		bindingContext.bindValue(SWTObservables.observeText(tpass, SWT.Modify), PojoObservables.observeValue(resprop, "value")); //$NON-NLS-1$
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.adapter_xmla";
	}
}
