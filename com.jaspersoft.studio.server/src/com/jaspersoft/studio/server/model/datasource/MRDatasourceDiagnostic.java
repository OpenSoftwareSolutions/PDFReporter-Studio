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
package com.jaspersoft.studio.server.model.datasource;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.server.ServerIconDescriptor;

public class MRDatasourceDiagnostic extends MRDatasourceCustom {

	public static final String CUSTOM_CLASS = "com.jaspersoft.jasperserver.api.logging.diagnostic.datasource.DiagnosticCustomDataSourceService";
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MRDatasourceDiagnostic(ANode parent, ResourceDescriptor rd, int index) {
		super(parent, rd, index);
	}

	private static IIconDescriptor iconDescriptor;

	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ServerIconDescriptor("datasource-diagnostic"); //$NON-NLS-1$
		return iconDescriptor;
	}

	@Override
	public IIconDescriptor getThisIconDescriptor() {
		return getIconDescriptor();
	}

	public static ResourceDescriptor createDescriptor(ANode parent) {
		ResourceDescriptor rd = MRDatasourceCustom.createDescriptor(parent);
		ResourceProperty rp = new ResourceProperty(MRDatasourceCustom.PROP_DATASOURCE_CUSTOM_PROPERTY_MAP);
		List<ResourceProperty> props = new ArrayList<ResourceProperty>();
		props.add(new ResourceProperty("_cds_name", "diagnosticCustomDataSource"));
		rp.setProperties(props);
		rd.setResourceProperty(rp);
		rp = new ResourceProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS, CUSTOM_CLASS);
		rd.setResourceProperty(rp);
		return rd;
	}
}
