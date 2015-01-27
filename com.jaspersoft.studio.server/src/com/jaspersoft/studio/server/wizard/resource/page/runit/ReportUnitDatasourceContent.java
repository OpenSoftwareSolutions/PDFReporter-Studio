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
package com.jaspersoft.studio.server.wizard.resource.page.runit;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.Version;
import com.jaspersoft.studio.server.publish.wizard.page.DatasourceSelectionComposite;
import com.jaspersoft.studio.server.publish.wizard.page.DatasourceSelectionListener;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;

public class ReportUnitDatasourceContent extends APageContent implements DatasourceSelectionListener {
	private boolean mandatory = false;
	private DatasourceSelectionComposite datasourceSelectionCmp;

	public ReportUnitDatasourceContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ReportUnitDatasourceContent(ANode parent, MResource resource, boolean mandatory) {
		this(parent, resource);
		this.mandatory = mandatory;
	}

	public ReportUnitDatasourceContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getName() {
		return Messages.SelectorDatasource_TabTitle;
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.runit.datasource";
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editReportUnitDSContent";
	}

	public static String[] getExcludedTypes(MResource r) {
		if (r != null && r.getWsClient() != null) {
			if (Version.isXMLACoonnectionSupported(r.getWsClient()))
				return new String[] { ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION };
		}
		return null;
	}

	@Override
	public Control createContent(Composite parent) {
		datasourceSelectionCmp = new SelectorDatasource().createDatasource(parent, pnode, res, mandatory, getExcludedTypes(res));
		datasourceSelectionCmp.addDatasourceSelectionListener(this);
		rebind();
		return datasourceSelectionCmp;
	}

	@Override
	protected void rebind() {

	}

	@Override
	public boolean isPageComplete() {
		return datasourceSelectionCmp != null && datasourceSelectionCmp.isDatasourceSelectionValid();
	}

	@Override
	public void datasourceSelectionChanged() {
		setPageComplete(isPageComplete());
	}
}
