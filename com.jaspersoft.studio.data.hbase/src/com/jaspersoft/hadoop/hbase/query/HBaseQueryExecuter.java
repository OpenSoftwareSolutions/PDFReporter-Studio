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
package com.jaspersoft.hadoop.hbase.query;

import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.JRAbstractQueryExecuter;

import org.apache.hadoop.hbase.client.Result;
import org.apache.log4j.Logger;

import com.jaspersoft.hadoop.hbase.HBaseDataSource;
import com.jaspersoft.hadoop.hbase.connection.HBaseConnection;

/**
 * This implementation process report parameters to create a
 * {@link HBaseDataSource}
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseQueryExecuter extends JRAbstractQueryExecuter {

	private final static Logger logger = Logger.getLogger(HBaseQueryExecuter.class);

	private HBaseQueryWrapper wrapper;

	private Map<String, ? extends JRValueParameter> parameters;

	public HBaseQueryExecuter(JasperReportsContext jasperReportsContext, JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) {
		super(jasperReportsContext, dataset, parameters);
		this.parameters = parameters;
		parseQuery();
	}

	/**
	 * Method not implemented
	 */
	@Override
	public boolean cancelQuery() throws JRException {
		return false;
	}

	/**
	 * Closes internal structures
	 */
	@Override
	public void close() {
		if (wrapper != null) {
			wrapper.close();
		}
	}

	/**
	 * Creates a new {@link HBaseDataSource} from the report parameters
	 */
	@Override
	public JRDataSource createDatasource() throws JRException {
		HBaseConnection connection = (HBaseConnection) ((Map<?, ?>) getParameterValue(JRParameter.REPORT_PARAMETERS_MAP)).get(HBaseDataSource.CONNECTION);
		if (connection == null) {
			logger.error("No HBase connection");
			System.out.println("Testing as default connection");
			connection = (HBaseConnection) ((Map<?, ?>) getParameterValue(JRParameter.REPORT_PARAMETERS_MAP)).get(JRParameter.REPORT_CONNECTION);
			if (connection == null) {
				logger.error("No data source");
				return null;
			}
		}
		ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
		try {
			Thread.currentThread().setContextClassLoader(connection.getClassLoader());
			wrapper = new HBaseQueryWrapper(connection, getQueryString());
			boolean sorted = wrapper.sortFields != null && !wrapper.sortFields.isEmpty();
			if (sorted) {
				sortResults(wrapper);
				System.out.println("HBASE: Fields will be sorted");
			} else {
				System.out.println("HBASE: Record set is active");
			}
			return new HBaseDataSource(wrapper);
		} finally {
			Thread.currentThread().setContextClassLoader(currentClassLoader);
		}
	}

	private void sortResults(HBaseQueryWrapper wrapper) {
		String[] sortFieldsArray = wrapper.sortFields.split(",");
		wrapper.sortedMap = new TreeMap<String, Result>();
		int index;
		for (index = 0; index < sortFieldsArray.length; index++) {
			sortFieldsArray[index] = sortFieldsArray[index].trim();
		}
		StringBuilder sortStringBuilder = new StringBuilder();
		long count = 0;
		wrapper.moveNext();
		Object value;
		while (wrapper.currentResult != null) {
			sortStringBuilder.delete(0, sortStringBuilder.length());
			for (index = 0; index < sortFieldsArray.length; index++) {
				value = null;
				try {
					value = wrapper.getColumnValue(sortFieldsArray[index]);
				} catch (JRException e) {
					e.printStackTrace();
				}
				if (value != null) {
					sortStringBuilder.append(String.valueOf(value));
				}
			}
			wrapper.sortedMap.put(sortStringBuilder.toString().trim() + count, wrapper.currentResult);
			count++;
			wrapper.moveNext();
		}
		System.out.println("HBASE: Results sorted");
	}

	/**
	 * Replacement of parameters
	 */
	@Override
	protected String getParameterReplacement(String parameterName) {
		Object parameterValue = parameters.get(parameterName);
		if (parameterValue == null) {
			throw new JRRuntimeException("Parameter \"" + parameterName + "\" does not exist.");
		}
		if (parameterValue instanceof JRValueParameter) {
			parameterValue = ((JRValueParameter) parameterValue).getValue();
		}
		return String.valueOf(parameterValue);
	}

	public String getProcessedQueryString() {
		return getQueryString();
	}
}
