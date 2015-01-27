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

import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRValueParameter;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;

/**
 * Query executer factory for Thrift queries. <br/>
 * This factory creates {@link ThriftQueryExecuter}
 * 
 * @author Eric Diaz
 * 
 */
public class ThriftQueryExecuterFactory implements QueryExecuterFactory {

	@Override
	public ThriftQueryExecuter createQueryExecuter(JasperReportsContext jasperReportsContext, JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) throws JRException {
		return new ThriftQueryExecuter(jasperReportsContext, dataset, parameters);
	}

	@Override
	public ThriftQueryExecuter createQueryExecuter(JRDataset dataset, Map<String, ? extends JRValueParameter> parameters) throws JRException {
		return new ThriftQueryExecuter(DefaultJasperReportsContext.getInstance(), dataset, parameters);
	}

	/**
	 * Method not implemented
	 */
	public Object[] getBuiltinParameters() {
		return null;
	}

	/**
	 * Method not implemented
	 */
	public boolean supportsQueryParameterType(String queryParameterType) {
		return true;
	}

}
