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
package com.jaspersoft.studio.data.fields;

import java.util.List;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public interface IFieldsProvider {

	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig);

	/**
	 * Returns the fields that are available from a query of a specific language The provider can use the passed in report
	 * to extract some additional configuration information such as report properties. The IReportConnection object can be
	 * used to execute the query.
	 * 
	 * @param con
	 *          the DataAdapter
	 * @param the
	 *          JRDataset that will be filled using the data source created by this provider. The passed in report can be
	 *          null. That means that no compiled report is available yet.
	 * @param parameters
	 *          map containing the interpreted default value of each parameter
	 * @throws UnsupportedOperationException
	 *           is the method is not supported
	 * @throws JRException
	 *           if an error occurs.
	 */
	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset jDataset)
			throws JRException, UnsupportedOperationException;

}
