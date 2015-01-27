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
package com.jaspersoft.studio.server.protocol.restv2;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;

public class DiffFields {

	public static final String TIMEZONE = "timezone";
	public static final String UPDATEDATE = "updateDate"; 

	public static final String DATASOURCENAME = "dataSourceName";

	public static final String MAXLENGHT = "maxLength";

	public static final String ACCESSKEY = "accessKey";
	public static final String SECRETKEY = "secretKey";
	public static final String ROLEARN = "roleArn";
	public static final String REGION = "region";
	public static final String DBNAME = "dbName";
	public static final String DBINSTANCEIDENTIFIER = "dbInstanceIdentifier";
	public static final String DBSERVICE = "dbService";

	public static void setSoapValue(ResourceDescriptor rd, String key, String v) {
		rd.setResourceProperty(new ResourceProperty(key, v));
	}

	public static void setSoapValue(ResourceDescriptor rd, String key, Integer v) {
		rd.setResourceProperty(key, v);
	}

	public static void setSoapValue(ResourceDescriptor rd, String key, Boolean v) {
		rd.setResourceProperty(key, v);
	}

	public static String getSoapValue(ResourceDescriptor rd, String key) {
		return rd.getResourcePropertyValue(key);
	}

	public static Integer getSoapValueInteger(ResourceDescriptor rd, String key) {
		return rd.getResourcePropertyValueAsInteger(key);
	}

	public static Boolean getSoapValueBoolean(ResourceDescriptor rd, String key) {
		return rd.getResourcePropertyValueAsBoolean(key);
	}

}
