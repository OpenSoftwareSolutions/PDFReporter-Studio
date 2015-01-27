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
package com.jaspersoft.studio.data.cassandra.querydesigner;

import com.jaspersoft.studio.data.querydesigner.sql.SimpleSQLQueryDesigner;
import com.jaspersoft.studio.data.querydesigner.sql.text.SQLLineStyler;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

/**
 * Simple query designer for Hive-QL that provides syntax highlighting.
 * 
 */
public class CassandraQLQueryDesigner extends SimpleSQLQueryDesigner {

	@Override
	protected SQLLineStyler getSQLBasedLineStyler() {
		return new CassandraQLLineStyler();
	}

	@Override
	public String getContextHelpId() {
		return ContextHelpIDs.PREFIX.concat("query_CQL");
	}
}
