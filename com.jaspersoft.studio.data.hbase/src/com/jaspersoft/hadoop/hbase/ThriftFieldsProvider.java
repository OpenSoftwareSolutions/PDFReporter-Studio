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
package com.jaspersoft.hadoop.hbase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignField;

import com.jaspersoft.hadoop.hbase.connection.ThriftConnection;
import com.jaspersoft.hadoop.hbase.connection.ThriftQueryWrapper;
import com.jaspersoft.hadoop.hbase.query.HBaseParameter;
import com.jaspersoft.hadoop.hbase.query.ThriftQueryExecuter;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class ThriftFieldsProvider {

	public static List<JRDesignField> getFields(JasperReportsContext jasperReportsContext, JRDataset reportDataset, Map<String, Object> parameters, ThriftConnection connection) throws JRException {
		if (connection == null) {
			throw new JRException("No Thrift connection");
		}
		ThriftQueryWrapper wrapper = null;
		ThriftQueryExecuter queryExecuter = null;
		try {
			Map<String, JRValueParameter> newValueParameters = new HashMap<String, JRValueParameter>();
			for (String parameterName : parameters.keySet()) {
				Object parameterValue = parameters.get(parameterName);
				if (parameterValue == null && parameterName.equals(JRParameter.REPORT_PARAMETERS_MAP))
					parameterValue = new HashMap<String, Object>();

				HBaseParameter newParameter = new HBaseParameter(parameterName, parameterValue);
				newValueParameters.put(parameterName, newParameter);
			}
			parameters.put(JRParameter.REPORT_CONNECTION, connection);
			if (!newValueParameters.containsKey(JRParameter.REPORT_PARAMETERS_MAP))
				newValueParameters.put(JRParameter.REPORT_PARAMETERS_MAP, new HBaseParameter(JRParameter.REPORT_PARAMETERS_MAP, parameters));

			newValueParameters.put(JRParameter.REPORT_CONNECTION, new HBaseParameter(JRParameter.REPORT_CONNECTION, connection));
			if (!newValueParameters.containsKey(JRParameter.REPORT_MAX_COUNT))
				newValueParameters.put(JRParameter.REPORT_MAX_COUNT, new HBaseParameter(JRParameter.REPORT_MAX_COUNT, null));

			queryExecuter = new ThriftQueryExecuter(jasperReportsContext, reportDataset, newValueParameters);
			wrapper = new ThriftQueryWrapper(connection, queryExecuter.getProcessedQueryString());
			Map<String, Class<?>> fieldsMap = new TreeMap<String, Class<?>>();
			wrapper.moveNext();
			if (wrapper.currentResult != null) {
				fieldsMap.put(wrapper.idField, wrapper.deserializer.deserializeRowId(wrapper.currentResult.getRow()).getClass());
			}
			for (int index = 0; index < 10 && wrapper.currentResult != null; index++) {
				for (String fieldName : wrapper.cellFields.keySet()) {
					if (fieldsMap.containsKey(fieldName)) {
						continue;
					}
					Object value = wrapper.getColumnFieldValue(fieldName);
					if (value != null) {
						fieldsMap.put(fieldName, value.getClass());
					}
				}
				wrapper.moveNext();
			}
			JRDesignField field = null;
			List<JRDesignField> fields = new ArrayList<JRDesignField>();
			for (String fieldName : fieldsMap.keySet()) {
				field = new JRDesignField();
				field.setName(fieldName);
				field.setValueClass(fieldsMap.get(fieldName));
				field.setDescription(null);
				fields.add(field);
			}
			return fields;
		} finally {
			if (wrapper != null) {
				wrapper.close();
			}
			if (queryExecuter != null) {
				queryExecuter.close();
			}
		}
	}
}
