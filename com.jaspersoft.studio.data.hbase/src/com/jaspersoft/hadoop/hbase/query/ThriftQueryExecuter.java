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

import org.apache.hadoop.hbase.thrift.generated.TRowResult;
import org.apache.log4j.Logger;

import com.jaspersoft.hadoop.hbase.ThriftDataSource;
import com.jaspersoft.hadoop.hbase.connection.ThriftConnection;
import com.jaspersoft.hadoop.hbase.connection.ThriftQueryWrapper;

/**
 * This implementation process a JSON like query and parameters to create a
 * {@link ThriftDataSource}
 * 
 * @author Eric Diaz
 * 
 */
public class ThriftQueryExecuter extends JRAbstractQueryExecuter {

	private final static Logger logger = Logger.getLogger(ThriftQueryExecuter.class);

	private ThriftQueryWrapper wrapper;

	private Map<String, ? extends JRValueParameter> parameters;

	public ThriftQueryExecuter(JasperReportsContext jasperReportsContext, JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) {
		super(jasperReportsContext, dataset, parameters);
		this.parameters = parameters;
		parseQuery();
	}

	/**
	 * Method not implemented
	 */
	public boolean cancelQuery() throws JRException {
		return false;
	}

	/**
	 * Closes internal structures
	 */
	public void close() {
		if (wrapper != null) {
			wrapper.close();
			wrapper = null;
		}
	}

	/**
	 * Creates a new {@link ThriftDataSource} from the query and parameters
	 */
	public JRDataSource createDatasource() throws JRException {
		ThriftConnection connection = (ThriftConnection) ((Map<?, ?>) getParameterValue(JRParameter.REPORT_PARAMETERS_MAP)).get(ThriftDataSource.CONNECTION);
		if (connection == null) {
			logger.error("No Thrift connection");
			System.out.println("Testing as default connection");
			connection = (ThriftConnection) ((Map<?, ?>) getParameterValue(JRParameter.REPORT_PARAMETERS_MAP)).get(JRParameter.REPORT_CONNECTION);
			if (connection == null) {
				logger.error("No data source");
				return null;
			}
		}
		wrapper = new ThriftQueryWrapper(connection, getQueryString());
		boolean sorted = wrapper.sortFields != null && !wrapper.sortFields.isEmpty();
		if (sorted) {
			System.out.println("THRIFT: Fields will be sorted");
			sortResults(wrapper);
		} else {
			System.out.println("THRIFT: Record set is active");
		}
		return new ThriftDataSource(wrapper);
	}

	private void sortResults(ThriftQueryWrapper wrapper) {
		String[] sortFieldsArray = wrapper.sortFields.split(",");
		wrapper.sortedMap = new TreeMap<String, TRowResult>();
		int index;
		for (index = 0; index < sortFieldsArray.length; index++) {
			sortFieldsArray[index] = sortFieldsArray[index].trim();
		}

		StringBuilder sortStringBuilder = new StringBuilder();
		long count = 0;
		Object value;
		String fieldName;
		wrapper.moveNext();
		while (wrapper.currentResult != null) {
			sortStringBuilder.delete(0, sortStringBuilder.length());
			for (index = 0; index < sortFieldsArray.length; index++) {
				fieldName = sortFieldsArray[index];
				value = null;
				try {
					value = wrapper.getColumnFieldValue(fieldName);
				} catch (JRException e) {
					e.printStackTrace();
				}
				if (value != null) {
					sortStringBuilder.append(value);
				}
			}
			wrapper.sortedMap.put(sortStringBuilder.toString().trim() + count, wrapper.currentResult);
			count++;
			wrapper.moveNext();
		}
		System.out.println("THRIFT: Results sorted");
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
