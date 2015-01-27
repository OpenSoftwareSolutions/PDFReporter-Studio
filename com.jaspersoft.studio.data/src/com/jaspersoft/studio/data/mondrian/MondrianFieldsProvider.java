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
package com.jaspersoft.studio.data.mondrian;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.olap.JRMondrianQueryExecuter;
import net.sf.jasperreports.olap.mondrian.JRMondrianResult;

import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.utils.parameter.ParameterUtil;
import com.jaspersoft.studio.utils.parameter.SimpleValueParameter;

public class MondrianFieldsProvider implements IFieldsProvider {
	
	@Override
	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		return true;
	}

	@Override
	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset jDataset) throws JRException, UnsupportedOperationException {
		Map<String, Object> parameters = new HashMap<String, Object>();
		con.contributeParameters(parameters);

		ParameterUtil.setParameters(jConfig, jDataset, parameters);
		parameters.put(JRParameter.REPORT_MAX_COUNT, 0);

		// JasperReports query executer instances require
		// REPORT_PARAMETERS_MAP parameter to be defined and not null
		Map<String, JRValueParameter> tmpMap = ParameterUtil.convertMap(parameters, jDataset);
		tmpMap.put(JRParameter.REPORT_PARAMETERS_MAP, new SimpleValueParameter(new HashMap<String, JRValueParameter>()));

		JRMondrianQueryExecuter qe = new JRMondrianQueryExecuter(jConfig, jDataset, tmpMap);
		qe.createDatasource();

		JRMondrianResult result = new JRMondrianResult(qe.getResult());
		return OlapFieldsProviderSupport.getFieldsFromResult(result);
	}

}
