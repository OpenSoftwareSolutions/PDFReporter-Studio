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
package com.jaspersoft.studio.data.sql.ui.metadata;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.jaspersoft.studio.utils.Misc;

public class SchemaUtil {
	public static String[] getSchemaPath(Connection c) {
		if (c == null)
			return null;
		try {
			Method m = c.getClass().getMethod("getSchema", new Class<?>[0]);
			if (m != null) {
				String schema = (String) m.invoke(c, new Object[0]);
				if (!Misc.isNullOrEmpty(schema))
					return new String[] { schema };
			}
		} catch (Throwable e) {
		}
		String[] res = null;
		try {
			String dbproduct = c.getMetaData().getDatabaseProductName();
			System.out.println(dbproduct);
			if (dbproduct.equalsIgnoreCase("Oracle"))
				return runSchemaQuery(c, "select sys_context('USERENV', 'CURRENT_SCHEMA') from dual");
			// else if (dbproduct.equalsIgnoreCase("SQL Anywhere"))
			// return runSchemaQuery(c, "select db_name()");
			else if (dbproduct.equalsIgnoreCase("PostgreSQL")) {
				Statement stmt = c.createStatement();
				ResultSet rs = stmt.executeQuery("SHOW search_path");
				while (rs.next()) {
					String str = rs.getString(1);
					res = str.split(",");
				}
				rs.close();
				stmt.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}

	protected static String[] runSchemaQuery(Connection c, String query) throws SQLException {
		List<String> paths = new ArrayList<String>();
		Statement stmt = c.createStatement();
		ResultSet rs = stmt.executeQuery(query);
		while (rs.next()) {
			String str = rs.getString(1);
			paths.add(str);
			System.out.println(str);
		}
		rs.close();
		stmt.close();
		if (!paths.isEmpty())
			return paths.toArray(new String[paths.size()]);
		return null;
	}

	public static void close(Connection c) {
		if (c != null)
			try {
				c.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}

	public static void close(ResultSet rs) {
		if (rs != null)
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
}
