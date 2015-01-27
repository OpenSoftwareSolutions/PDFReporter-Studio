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
package com.jaspersoft.studio.data.mongodb.messages;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.jaspersoft.studio.data.mongodb.messages.messages"; //$NON-NLS-1$
	public static String MongoDbDataAdapterComposite_labelPassword;
	public static String MongoDbDataAdapterComposite_labelURI;
	public static String MongoDbDataAdapterComposite_labelUsername;
	public static String MongoDbDataAdapterFactory_description;
	public static String MongoDbDataAdapterFactory_label;
	public static String RDDatasourceMongoDBPage_labelurl;
	public static String RDDatasourceMongoDBPage_pass;
	public static String RDDatasourceMongoDBPage_title;
	public static String RDDatasourceMongoDBPage_username;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
