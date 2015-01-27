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

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * JDBC query executer for Oracle inline queries and stored procedures. If you
 * are only going to run inline SQL you should use the normal
 * JRJdbcQueryExecuter instead.
 * <p/>
 * This query executer implementation offers built-in support for calls to
 * Oracle stored procedures and SQL queries.
 * 
 * @author Barry Klawans (bklawans@users.sourceforge.net) based off of work by
 *         Teodor Danciu (teodord@users.sourceforge.net) in JRJdbcQueryExecuter.
 */
public class PlSqlQueryExecuter extends JRAbstractQueryExecuter {
	private static final Log log = LogFactory.getLog(PlSqlQueryExecuter.class);
	private static final int ORACLE_CURSOR_TYPE = -10; // oracle.jdbc.OracleTypes.CURSOR

	private Connection connection;

	/**
	 * The statement used to fire the query.
	 */
	private PreparedStatement statement;

	private ResultSet resultSet;

	private int cursorParameter = -1;

	private boolean isStoredProcedure = false;

	public PlSqlQueryExecuter(JasperReportsContext jContext, JRDataset dataset,
			Map<String, ? extends JRValueParameter> parameters) {
		super(jContext, dataset, parameters);

		connection = (Connection) getParameterValue(JRParameter.REPORT_CONNECTION);

		if (connection == null) {
			if (log.isWarnEnabled())
				log.warn("The supplied java.sql.Connection object is null.");
		}

		parseQuery();
	}

	protected String getParameterReplacement(String parameterName) {
		return "?";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.util.JRQueryExecuter#createDatasource()
	 */
	public JRDataSource createDatasource() throws JRException {
		JRDataSource dataSource = null;

		createStatement();

		if (statement != null) {
			try {
				Integer reportMaxCount = (Integer) getParameterValue(JRParameter.REPORT_MAX_COUNT);
				if (reportMaxCount != null) {
					statement.setMaxRows(reportMaxCount.intValue());
				}

				if (isStoredProcedure) {
					CallableStatement cstmt = (CallableStatement) statement;
					cstmt.execute();
					if (cursorParameter > 0) {
						resultSet = (java.sql.ResultSet) cstmt
								.getObject(cursorParameter);
					}
				} else {
					resultSet = statement.executeQuery();
				}

				dataSource = new JRResultSetDataSource(resultSet);
			} catch (SQLException e) {
				throw new JRException("Error executing SQL statement for : "
						+ dataset.getName(), e);
			}
		}

		return dataSource;
	}

	/**
	 * Check to see if the current query is actuall a call to an oracle stored
	 * procedure.
	 * 
	 * Currently we only detect queries of the form "{call stored_procedure...}
	 * as invocation of a stored procedure.
	 * 
	 * @param queryString
	 *            the query to execute.
	 * 
	 * @return true if the query in an invocation of a stored procedure on
	 *         oracle, false otherwise.
	 */
	private boolean isOracleStoredProcedure(String queryString)
			throws SQLException {
		/* If we don't have a connection we can't tell the database vendor. */
		if (connection == null)
			return false;

		/* Check that its Oracle. If not, it can't be an Oracle stored procedure */
		String dbVendor = connection.getMetaData().getDatabaseProductName()
				.toLowerCase();
		if (!"oracle".equals(dbVendor)) {
			return false;
		}

		// Its Oracle, check for a stored procedure.
		// Make lower case copy for easier parsing.
		String sql = queryString.trim().toLowerCase();
		if (sql.charAt(0) == '{' && sql.substring(1).trim().startsWith("call "))
			return true;

		return false;
	}

	private void createStatement() throws JRException {
		String queryString = getQueryString();

		if (connection != null && queryString != null
				&& queryString.trim().length() > 0) {

			try {
				isStoredProcedure = isOracleStoredProcedure(queryString);

				if (isStoredProcedure) {
					statement = connection.prepareCall(queryString);
				} else {
					statement = connection.prepareStatement(queryString);
				}

				int fetchSize = JRPropertiesUtil.getInstance(
						getJasperReportsContext()).getIntegerProperty(
						dataset.getPropertiesMap(),
						JRJdbcQueryExecuterFactory.PROPERTY_JDBC_FETCH_SIZE, 0);
				if (fetchSize > 0) {
					statement.setFetchSize(fetchSize);
				}

				List<String> parameterNames = getCollectedParameterNames();
				if (!parameterNames.isEmpty()) {
					for (int i = 0; i < parameterNames.size(); i++) {
						String parameterName = (String) parameterNames.get(i);
						JRValueParameter parameter = getValueParameter(parameterName);
						Class<?> clazz = parameter.getValueClass();
						Object parameterValue = parameter.getValue();

						if (clazz.equals(java.lang.Object.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.JAVA_OBJECT);
							} else {
								statement.setObject(i + 1, parameterValue);
							}
						} else if (clazz.equals(java.lang.Boolean.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.BIT);
							} else {
								statement.setBoolean(i + 1,
										((Boolean) parameterValue)
												.booleanValue());
							}
						} else if (clazz.equals(java.lang.Byte.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.TINYINT);
							} else {
								statement.setByte(i + 1,
										((Byte) parameterValue).byteValue());
							}
						} else if (clazz.equals(java.lang.Double.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.DOUBLE);
							} else {
								statement
										.setDouble(i + 1,
												((Double) parameterValue)
														.doubleValue());
							}
						} else if (clazz.equals(java.lang.Float.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.FLOAT);
							} else {
								statement.setFloat(i + 1,
										((Float) parameterValue).floatValue());
							}
						} else if (clazz.equals(java.lang.Integer.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.INTEGER);
							} else {
								statement.setInt(i + 1,
										((Integer) parameterValue).intValue());
							}
						} else if (clazz.equals(java.lang.Long.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.BIGINT);
							} else {
								statement.setLong(i + 1,
										((Long) parameterValue).longValue());
							}
						} else if (clazz.equals(java.lang.Short.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.SMALLINT);
							} else {
								statement.setShort(i + 1,
										((Short) parameterValue).shortValue());
							}
						} else if (clazz.equals(java.math.BigDecimal.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.DECIMAL);
							} else {
								statement.setBigDecimal(i + 1,
										(BigDecimal) parameterValue);
							}
						} else if (clazz.equals(java.lang.String.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.VARCHAR);
							} else {
								statement.setString(i + 1,
										parameterValue.toString());
							}
						} else if (clazz.equals(java.util.Date.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.DATE);
							} else {
								statement
										.setDate(
												i + 1,
												new java.sql.Date(
														((java.util.Date) parameterValue)
																.getTime()));
							}
						} else if (clazz.equals(java.sql.Timestamp.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.TIMESTAMP);
							} else {
								statement.setTimestamp(i + 1,
										(java.sql.Timestamp) parameterValue);
							}
						} else if (clazz.equals(java.sql.Time.class)) {
							if (parameterValue == null) {
								statement.setNull(i + 1, Types.TIME);
							} else {
								statement.setTime(i + 1,
										(java.sql.Time) parameterValue);
							}
						} else if (clazz.equals(java.sql.ResultSet.class)) {
							if (!isStoredProcedure) {
								throw new JRException(
										"OUT paramater used in non-stored procedure call : "
												+ parameterName + " class "
												+ clazz.getName());
							} else if (cursorParameter > 0) {
								throw new JRException(
										"A stored procedure can have at most one cursor parameter : "
												+ parameterName + " class "
												+ clazz.getName());
							}

							((CallableStatement) statement)
									.registerOutParameter(i + 1,
											ORACLE_CURSOR_TYPE);
							cursorParameter = i + 1;
						} else {
							throw new JRException(
									"Parameter type not supported in query : "
											+ parameterName + " class "
											+ clazz.getName());
						}
					}
				}
			} catch (SQLException e) {
				throw new JRException(
						"Error preparing statement for executing the report query : "
								+ "\n\n" + queryString + "\n\n", e);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.util.JRQueryExecuter#close()
	 */
	public synchronized void close() {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.error("Error while closing result set.", e);
			} finally {
				resultSet = null;
			}
		}

		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.error("Error while closing statement.", e);
			} finally {
				statement = null;
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see net.sf.jasperreports.engine.util.JRQueryExecuter#cancelQuery()
	 */
	public synchronized boolean cancelQuery() throws JRException {
		if (statement != null) {
			try {
				statement.cancel();
				return true;
			} catch (Throwable t) {
				throw new JRException("Error cancelling SQL statement", t);
			}
		}

		return false;
	}
}
