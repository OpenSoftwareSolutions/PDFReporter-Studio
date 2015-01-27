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
package com.jaspersoft.studio.server.model.datasource.filter;

import java.util.HashSet;
import java.util.Set;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceAWS;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceVDS;

public class DatasourcesAllFilter implements IDatasourceFilter {
	private final static Set<String> types = new HashSet<String>();
	static {
		types.add(ResourceDescriptor.TYPE_DATASOURCE);
		types.add(ResourceDescriptor.TYPE_DATASOURCE_BEAN);
		types.add(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM);
		types.add(ResourceDescriptor.TYPE_DATASOURCE_JNDI);
		types.add(ResourceDescriptor.TYPE_DATASOURCE_JDBC);
		types.add(MRDatasourceVDS.TYPE_DATASOURCE_VDS);
		types.add(MRDatasourceAWS.TYPE_AWS);
		types.add(ResourceDescriptor.TYPE_ADHOC_DATA_VIEW);
		types.add(ResourceDescriptor.TYPE_DATASOURCE_DOMAIN);
		types.add(ResourceDescriptor.TYPE_DATASOURCE_DOMAIN1);
		types.add(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION);
		types.add(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION);
		types.add(ResourceDescriptor.TYPE_OLAP_XMLA_CONNECTION);
	}

	public static Set<String> getTypes() {
		return types;
	}

	@Override
	public Set<String> getFilterTypes() {
		return types;
	}

	@Override
	public boolean isDatasource(ResourceDescriptor r) {
		return types.contains(r.getWsType());

		// return wsType.equals(ResourceDescriptor.TYPE_DATASOURCE) ||
		// wsType.equals(ResourceDescriptor.TYPE_DATASOURCE_BEAN) ||
		// wsType.equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM)
		// || wsType.equals(ResourceDescriptor.TYPE_DATASOURCE_JDBC) ||
		// wsType.equals(ResourceDescriptor.TYPE_DATASOURCE_JNDI) ||
		// wsType.equals(MRDatasourceVDS.TYPE_DATASOURCE_VDS)
		//				|| wsType.equals(MRDatasourceAWS.TYPE_AWS) || wsType.equals("Domain"); //$NON-NLS-1$
		//				|| (wsType.equals(ResourceDescriptor.TYPE_DATASOURCE_CUSTOM) //$NON-NLS-1$
		//						&& r.getResourcePropertyValue("PROP_RESOURCE_TYPE") != null && r //$NON-NLS-1$
		//						.getResourcePropertyValue("PROP_RESOURCE_TYPE") //$NON-NLS-1$
		//						.equals("com.jaspersoft.jasperserver.api.metadata.jasperreports.domain.CustomReportDataSource")); //$NON-NLS-1$
	}

}
