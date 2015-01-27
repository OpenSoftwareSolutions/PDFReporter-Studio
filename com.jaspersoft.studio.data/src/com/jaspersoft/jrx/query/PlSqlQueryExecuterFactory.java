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
package com.jaspersoft.jrx.query;

import java.util.Arrays;
import java.util.Map;

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRQueryExecuter;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;

/**
 * Query executer factory for Oracle queries, both inline SQL and stored
 * procedures. The normal JRJdbcQueryExecuterFactory can be used with Oracle for
 * inline SQL, but not stored procedures.
 * <p/>
 * To use with an oracle stored procedure that returns results via a REF CURSOR
 * you declare a parameter of type java.sql.ResultSet, and pass that to the
 * stored procedure. For example, if you have a stored procedure named
 * "do_stuff" that takes a string as the first parameter and returns results via
 * the second parameter you would use a query that looks like:<br>
 * <code>{call do_stuff($P{the_string_param}, $P{the_result_set_param})}</code>
 * <p/>
 * This factory creates Oracle query executers for SQL queries.
 * 
 * @author Barry Klawans (bklawans@users.sourceforge.net) based off of work by
 *         Lucian Chirita (lucianc@users.sourceforge.net) in
 *         JRJdbcQueryExecuterFactory.java
 * @see net.sf.jasperreports.engine.query.JROracleQueryExecuter
 */
public class PlSqlQueryExecuterFactory implements QueryExecuterFactory {
	/**
	 * Property specifying whether field descriptions should be used to
	 * determine the mapping between the fields and the query return values.
	 */
	public static final String PROPERTY_JDBC_FETCH_SIZE = JRPropertiesUtil.PROPERTY_PREFIX
			+ "jdbc.fetch.size";

	/**
	 * Built-in parameter holding the Oracle RefCursor needed to return values
	 * from a stored procedure.
	 */
	public static final String PARAMETER_ORACLE_REF_CURSOR = "ORACLE_REF_CURSOR";

	private final static Object[] ORACLE_BUILT_IN_PARAMETERS = {
			PARAMETER_ORACLE_REF_CURSOR, java.sql.ResultSet.class };

	/**
	 * SQL query language.
	 */
	public static final String QUERY_LANGUAGE_PLSQL = "plsql";

	private static final String[] queryParameterClassNames;

	static {
		queryParameterClassNames = new String[] {
				java.lang.Object.class.getName(),
				java.lang.Boolean.class.getName(),
				java.lang.Byte.class.getName(),
				java.lang.Double.class.getName(),
				java.lang.Float.class.getName(),
				java.lang.Integer.class.getName(),
				java.lang.Long.class.getName(),
				java.lang.Short.class.getName(),
				java.math.BigDecimal.class.getName(),
				java.lang.String.class.getName(),
				java.util.Date.class.getName(),
				java.sql.Timestamp.class.getName(),
				java.sql.Time.class.getName(),
				java.sql.ResultSet.class.getName() };

		Arrays.sort(queryParameterClassNames);
	}

	public JRQueryExecuter createQueryExecuter(JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters)
			throws JRException {
		return new PlSqlQueryExecuter(
				DefaultJasperReportsContext.getInstance(), dataset, parameters);
	}

	public Object[] getBuiltinParameters() {
		return ORACLE_BUILT_IN_PARAMETERS;
	}

	public boolean supportsQueryParameterType(String className) {
		return Arrays.binarySearch(queryParameterClassNames, className) >= 0;
	}

	public JRQueryExecuter createQueryExecuter(
			JasperReportsContext jasperReportsContext, JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters)
			throws JRException {
		return new PlSqlQueryExecuter(jasperReportsContext, dataset, parameters);
	}
}
