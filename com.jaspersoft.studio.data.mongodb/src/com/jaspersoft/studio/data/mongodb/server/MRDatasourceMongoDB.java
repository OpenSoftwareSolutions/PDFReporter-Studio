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
package com.jaspersoft.studio.data.mongodb.server;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.data.mongodb.MongoDBIconDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.server.model.datasource.MRDatasourceCustom;
import com.jaspersoft.studio.server.protocol.restv2.DiffFields;

public class MRDatasourceMongoDB extends MRDatasourceCustom {

	public static final String PASSWORD = "password";
	public static final String MONGO_URI = "mongoURI";
	public static final String USERNAME = "username";
	public static final String CUSTOM_CLASS = "com.jaspersoft.mongodb.jasperserver.MongoDbDataSourceService";
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MRDatasourceMongoDB(ANode parent, ResourceDescriptor rd, int index) {
		super(parent, rd, index);
	}

	private static IIconDescriptor iconDescriptor;

	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new MongoDBIconDescriptor("datasource-mongo"); //$NON-NLS-1$
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
		props.add(new ResourceProperty(USERNAME, USERNAME));
		props.add(new ResourceProperty(MONGO_URI, "mongodb://hostname:27017/database"));
		props.add(new ResourceProperty("_cds_name", "MongoDbDataSource"));
		props.add(new ResourceProperty(PASSWORD, PASSWORD));
		rp.setProperties(props);
		rd.setResourceProperty(rp);
		rp = new ResourceProperty(ResourceDescriptor.PROP_DATASOURCE_CUSTOM_SERVICE_CLASS, CUSTOM_CLASS);
		rd.setResourceProperty(rp);
		rd.setResourceProperty(DiffFields.DATASOURCENAME, "MongoDbDataSource");
		return rd;
	}
}
