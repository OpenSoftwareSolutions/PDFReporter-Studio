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
package com.jaspersoft.studio.data.xls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.xls.XlsDataAdapter;
import net.sf.jasperreports.eclipse.util.StringUtils;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.data.JRXlsDataSource;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.query.JRXlsQueryExecuter;
import net.sf.jasperreports.engine.query.JRXlsQueryExecuterFactory;

import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.utils.parameter.ParameterUtil;

public class XLSFieldsProvider implements IFieldsProvider {

	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset reportDataset) throws JRException, UnsupportedOperationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REPORT_PARAMETERS_MAP", new HashMap<String, Object>());
		con.contributeParameters(parameters);
		ParameterUtil.setParameters(jConfig, reportDataset, parameters);
		parameters.put(JRParameter.REPORT_MAX_COUNT, 2);

		JRXlsDataSource ds = null;

		XlsDataAdapter da = (XlsDataAdapter) ((AbstractDataAdapterService) con).getDataAdapter();
		if (da.isQueryExecuterMode()) {
			JRXlsQueryExecuter qe = (JRXlsQueryExecuter) new JRXlsQueryExecuterFactory().createQueryExecuter(jConfig, reportDataset, ParameterUtil.convertMap(parameters, reportDataset));
			ds = (JRXlsDataSource) qe.createDatasource();
		} else {
			ds = (JRXlsDataSource) parameters.get(JRParameter.REPORT_DATA_SOURCE);
		}
		if (ds != null) {
			ds.setUseFirstRowAsHeader(da.isUseFirstRowAsHeader());
			ds.next();
			Map<String, Integer> map = ds.getColumnNames();
			List<JRDesignField> columns = new ArrayList<JRDesignField>(map.keySet().size());
			for (String key : map.keySet()) {
				JRDesignField field = new JRDesignField();
				field.setName(StringUtils.xmlEncode(key, null));
				field.setValueClass(String.class);
				columns.add(field);
			}
			return columns;
		}
		return null;
	}

	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		return true;
	}

}
