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
package com.jaspersoft.studio.model.datasource.jdbc;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.datasource.AMDatasource;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;

public class MJDBCDataSource extends AMDatasource {
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("datasourceJDBC"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public MJDBCDataSource() {
		super(null, -1);
	}

	public MJDBCDataSource(ANode parent, int index) {
		super(parent, index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		NTextPropertyDescriptor driverClassD = new NTextPropertyDescriptor(PROPERTY_DRIVERCLASS,
				Messages.common_driver_class);
		desc.add(driverClassD);

		NTextPropertyDescriptor jdbcURLD = new NTextPropertyDescriptor(PROPERTY_JDBC_URL, Messages.common_jdbc_url);
		desc.add(jdbcURLD);

		NTextPropertyDescriptor usernameD = new NTextPropertyDescriptor(PROPERTY_USERNAME, Messages.common_username);
		desc.add(usernameD);

		NTextPropertyDescriptor passwordD = new NTextPropertyDescriptor(PROPERTY_PASSWORD, Messages.common_password);
		desc.add(passwordD);

		NTextPropertyDescriptor jarD = new NTextPropertyDescriptor(PROPERTY_JAR, Messages.MJDBCDataSource_classpath);
		desc.add(jarD);

		NTextPropertyDescriptor connectionD = new NTextPropertyDescriptor(PROPERTY_CONNECTION, Messages.common_connection);
		desc.add(connectionD);

	}

	public static final String PROPERTY_DRIVERCLASS = "PROPERTY_DRIVERCLASS"; //$NON-NLS-1$
	private String driverclass;

	public static final String PROPERTY_JDBC_URL = "PROPERTY_JDBC_URL"; //$NON-NLS-1$
	private String jdbcURL;

	public static final String PROPERTY_USERNAME = "PROPERTY_USERNAME"; //$NON-NLS-1$
	private String username;

	public static final String PROPERTY_PASSWORD = "PROPERTY_PASSWORD"; //$NON-NLS-1$
	private String password;

	public static final String PROPERTY_JAR = "PROPERTY_JAR"; //$NON-NLS-1$
	private String jar;

	public static final String PROPERTY_CONNECTION = "PROPERTY_CONNECTION"; //$NON-NLS-1$
	private Connection connection;

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(PROPERTY_DRIVERCLASS)) {
			return driverclass;
		}
		if (id.equals(PROPERTY_JAR)) {
			return jar;
		}
		if (id.equals(PROPERTY_JDBC_URL)) {
			return jdbcURL;
		}
		if (id.equals(PROPERTY_USERNAME)) {
			return username;
		}
		if (id.equals(PROPERTY_PASSWORD)) {
			return password;
		}
		if (id.equals(PROPERTY_CONNECTION)) {
			return connection;
		}
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(PROPERTY_DRIVERCLASS)) {
			driverclass = (String) value;
		} else if (id.equals(PROPERTY_JDBC_URL)) {
			jdbcURL = (String) value;
		} else if (id.equals(PROPERTY_USERNAME)) {
			username = (String) value;
		} else if (id.equals(PROPERTY_JAR)) {
			jar = (String) value;
		} else if (id.equals(PROPERTY_PASSWORD)) {
			password = (String) value;
		} else if (id.equals(PROPERTY_CONNECTION)) {
			connection = (Connection) value;
		} else
			super.setPropertyValue(id, value);
	}
}
