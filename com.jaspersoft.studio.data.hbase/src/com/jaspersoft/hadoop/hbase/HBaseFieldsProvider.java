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
import java.util.Collections;
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

import org.apache.hadoop.hbase.KeyValue;
import org.apache.log4j.Logger;

import com.jaspersoft.hadoop.hbase.connection.HBaseConnection;
import com.jaspersoft.hadoop.hbase.query.HBaseParameter;
import com.jaspersoft.hadoop.hbase.query.HBaseQueryExecuter;
import com.jaspersoft.hadoop.hbase.query.HBaseQueryWrapper;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseFieldsProvider {

	private static final Logger logger = Logger.getLogger(HBaseFieldsProvider.class);

	public static List<JRDesignField> getFields(JasperReportsContext jasperReportsContext, JRDataset dataset, Map<String, Object> parameters, HBaseConnection connection) throws JRException {
		HBaseQueryExecuter queryExecuter = null;
		HBaseQueryWrapper wrapper = null;
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

			queryExecuter = new HBaseQueryExecuter(jasperReportsContext, dataset, newValueParameters);
			wrapper = new HBaseQueryWrapper(connection, queryExecuter.getProcessedQueryString());

			Map<String, Class<?>> fieldsMap = new TreeMap<String, Class<?>>();
			if (wrapper.qualifierJrField != null && wrapper.valueJrField != null) {
				fieldsMap.put(wrapper.qualifierJrField, String.class);
				fieldsMap.put(wrapper.valueJrField, Object.class);
			}

			wrapper.moveNext();
			if (wrapper.currentResult != null) {
				byte[] rowID = wrapper.currentResult.getRow();
				if (rowID == null) {
					return Collections.emptyList();
				}
				fieldsMap.put(wrapper.getAliasForColumn(wrapper.idField), wrapper.deserializer.deserializeRowId(rowID).getClass());
			}
			for (int index = 0; index < wrapper.rowsToProcess && wrapper.currentResult != null; index++) {
				processFieldsForCurrentResult(wrapper, fieldsMap, true);
				wrapper.moveNext();
			}

			List<JRDesignField> fields = new ArrayList<JRDesignField>();
			for (String fieldName : fieldsMap.keySet()) {
				fields.add(createField(fieldName, fieldsMap.get(fieldName)));
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void processFieldsForCurrentResult(HBaseQueryWrapper wrapper, Map fieldsMap, boolean includeClass) {
		List<KeyValue> valuesList = wrapper.currentResult.list();
		boolean pivot = wrapper.pivotPattern != null;
		for (int listIndex = 0; listIndex < valuesList.size(); listIndex++) {
			KeyValue keyValue = valuesList.get(listIndex);
			String columnFamily = wrapper.deserializer.deserializeColumnFamily(keyValue.getFamily());
			String qualifier = wrapper.deserializer.deserializeQualifier(keyValue.getQualifier());
			String fieldName = wrapper.getAliasForColumn(columnFamily + wrapper.getColumnSeparator() + qualifier);
			if (fieldsMap.containsKey(fieldName)) {
				continue;
			}
			if (pivot) {
				if (wrapper.pivotPattern.matcher(fieldName).matches()) {
					if (includeClass) {
						continue;
					}
				} else {
					if (!includeClass) {
						continue;
					}
				}
			}
			byte[] value = wrapper.currentResult.getValue(keyValue.getFamily(), keyValue.getQualifier());
			if (value == null) {
				logger.warn("Null value");
				continue;
			}
			Object deserializedValue = wrapper.deserializer.deserializeValue(wrapper.tableName, columnFamily, qualifier, value);
			if (deserializedValue == null) {
				logger.error("Value cannot be deserialized");
				continue;
			}
			fieldsMap.put(fieldName, includeClass ? deserializedValue.getClass() : deserializedValue);
		}
	}

	private static JRDesignField createField(String fieldName, Class<?> _class) {
		JRDesignField field = new JRDesignField();
		field.setName(fieldName);
		field.setValueClass(_class);
		field.setDescription(null);
		return field;
	}
}
