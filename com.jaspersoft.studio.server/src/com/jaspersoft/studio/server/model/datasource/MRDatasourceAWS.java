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

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.server.ServerIconDescriptor;
import com.jaspersoft.studio.server.model.MResource;

public class MRDatasourceAWS extends MResource {

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String TYPE_AWS = "aws";

	public static final String PROP_DATASOURCE_AWS_ACCESS_KEY = "PROP_DATASOURCE_AWS_ACCESS_KEY";
	public static final String PROP_DATASOURCE_AWS_SECRET_KEY = "PROP_DATASOURCE_AWS_SECRET_KEY";
	public static final String PROP_DATASOURCE_AWS_ROLE_ARN = "PROP_DATASOURCE_AWS_ROLE_ARN";
	public static final String PROP_DATASOURCE_AWS_REGION = "PROP_DATASOURCE_AWS_REGION";
	public static final String PROP_DATASOURCE_AWS_DB_NAME = "PROP_DATASOURCE_AWS_DB_NAME";
	public static final String PROP_DATASOURCE_AWS_DB_SERVICE = "PROP_DATASOURCE_AWS_DB_SERVICE";
	public static final String PROP_DATASOURCE_AWS_DB_INSTANCE_IDENTIFIER = "PROP_DATASOURCE_AWS_DB_INSTANCE_IDENTIFIER";

	public MRDatasourceAWS(ANode parent, ResourceDescriptor rd, int index) {
		super(parent, rd, index);
	}

	private static IIconDescriptor iconDescriptor;

	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ServerIconDescriptor("datasource-aws"); //$NON-NLS-1$
		return iconDescriptor;
	}

	@Override
	public IIconDescriptor getThisIconDescriptor() {
		return getIconDescriptor();
	}

	public static ResourceDescriptor createDescriptor(ANode parent) {
		ResourceDescriptor rd = MResource.createDescriptor(parent);
		rd.setWsType(MRDatasourceAWS.TYPE_AWS);

		ResourceProperty rp = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_REGION, "");
		rd.getProperties().add(rp);
		rp = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_DB_NAME, "");
		rd.getProperties().add(rp);
		rp = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_DB_SERVICE, "");
		rd.getProperties().add(rp);
		rp = new ResourceProperty(MRDatasourceAWS.PROP_DATASOURCE_AWS_DB_INSTANCE_IDENTIFIER, "");
		rd.getProperties().add(rp);
		return rd;
	}
}
