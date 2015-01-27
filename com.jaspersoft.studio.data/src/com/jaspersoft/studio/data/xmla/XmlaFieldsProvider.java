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
package com.jaspersoft.studio.data.xmla;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.olap.result.JROlapResult;
import net.sf.jasperreports.olap.xmla.JRXmlaQueryExecuter;

import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.data.mondrian.OlapFieldsProviderSupport;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.utils.parameter.ParameterUtil;

/**
 * Class that retrieve the fields from an XmlaDataset. It simply build
 * the parameters map and request the execution of the XMLA query. 
 * Then its result is used by the OlapFieldsProvder support for the generation of the fields.
 * 
 * @author Sherman Wood & Marco Orlandin
 *
 */
public class XmlaFieldsProvider implements IFieldsProvider {

	@Override
	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		return true;
	}

	@Override
	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset jDataset) throws JRException, UnsupportedOperationException {
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("REPORT_PARAMETERS_MAP", new HashMap<String, Object>());
		try {
			//Here there is the password of the connection
			con.contributeParameters(parameters);
		} catch (JRException e) {
			e.printStackTrace();
		}
		ParameterUtil.setParameters(jConfig, jDataset, parameters);
		parameters.put(JRParameter.REPORT_MAX_COUNT, 2);
		Map<String,? extends JRValueParameter> tmpMap  = ParameterUtil.convertMap(parameters, jDataset);

		JRXmlaQueryExecuter qe = new JRXmlaQueryExecuter(jConfig, jDataset, tmpMap);
		JROlapResult result = qe.getResult();
		return OlapFieldsProviderSupport.getFieldsFromResult(result);
	}
}
