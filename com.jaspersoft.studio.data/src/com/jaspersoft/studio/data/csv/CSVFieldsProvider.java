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
package com.jaspersoft.studio.data.csv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.csv.CsvDataAdapter;
import net.sf.jasperreports.eclipse.util.StringUtils;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRCsvDataSource;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.query.JRCsvQueryExecuter;
import net.sf.jasperreports.engine.query.JRCsvQueryExecuterFactory;

import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.utils.parameter.ParameterUtil;

public class CSVFieldsProvider implements IFieldsProvider {

	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset reportDataset) throws JRException, UnsupportedOperationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REPORT_PARAMETERS_MAP", new HashMap<String, Object>());
		con.contributeParameters(parameters);
		ParameterUtil.setParameters(jConfig, reportDataset, parameters);
		parameters.put(JRParameter.REPORT_MAX_COUNT, 2);

		JRCsvDataSource ds = null;

		CsvDataAdapter da = (CsvDataAdapter) ((AbstractDataAdapterService) con).getDataAdapter();
		if (da.isQueryExecuterMode()) {
			if (reportDataset.getQuery() == null) {
				JRDesignQuery query = new JRDesignQuery();
				query.setLanguage("csv");
				((JRDesignDataset) reportDataset).setQuery(query);
			}
			JRCsvQueryExecuter qe = (JRCsvQueryExecuter) new JRCsvQueryExecuterFactory().createQueryExecuter(jConfig, reportDataset, ParameterUtil.convertMap(parameters, reportDataset));
			ds = (JRCsvDataSource) qe.createDatasource();
		} else {
			ds = (JRCsvDataSource) parameters.get(JRParameter.REPORT_DATA_SOURCE);
		}
		List<JRDesignField> columns = new ArrayList<JRDesignField>();
		if (da.getColumnNames() != null && !da.getColumnNames().isEmpty()) {
			for (String key : da.getColumnNames())
				createColumn(columns, key);
			return columns;
		}
		if (ds != null) {
			ds.setUseFirstRowAsHeader(true);
			ds.next();
			Map<String, Integer> map = ds.getColumnNames();
			for (String key : map.keySet())
				createColumn(columns, key);
			return columns;
		}
		return null;
	}

	private void createColumn(List<JRDesignField> columns, String key) {
		JRDesignField field = new JRDesignField();
		field.setName(StringUtils.xmlEncode(key, null));
		field.setValueClass(String.class);
		columns.add(field);
	}

	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		return true;
	}

}
