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

import java.util.Arrays;
import java.util.List;

import com.jaspersoft.studio.data.querydesigner.sql.text.SQLScanner;

/**
 * Class implementing a simple fuzzy scanner for Hive-QL.
 * 
 */
public class CassandraQLScanner extends SQLScanner {

	private static List<String> hiveQLKeywords;

	@Override
	protected List<String> getSQLKeywords() {
		if (hiveQLKeywords == null) {
			hiveQLKeywords = Arrays.asList(new String[] { "between", "class",
					"delete", "desc", "distinct", "elements", "escape",
					"exists", "false", "fetch", "from", "full", "group",
					"having", "in", "indices", "inner", "insert", "into", "is",
					"join", "left", "like", "new", "not", "null", "or",
					"order", "outer", "properties", "right", "select", "set",
					"some", "true", "union", "update", "versioned", "where",
					"and", "or", "as", "on", "with", "by", "both", "empty",
					"leading", "member", "object", "of", "trailing" ,
					// standard sql92 functions
					"round", "floor", "ceil", "rand", "concat", "substr",
					"upper", "ucase", "lower", "lcase", "trim", "ltrim",
					"rtrim", "regexp_replace", "size", "cast", "from_unixtime",
					"to_date", "month", "year", "day",

					// misc functions - based on oracle dialect
					"get_json_object", "count", "sum", "avg", "min", "max",
					"min" });
		}
		return hiveQLKeywords;
	}

}
