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
package com.jaspersoft.studio.data.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.ParameterContributor;
import net.sf.jasperreports.engine.ParameterContributorContext;
import net.sf.jasperreports.engine.ParameterContributorFactory;
import net.sf.jasperreports.repo.DataAdapterResource;
import net.sf.jasperreports.repo.RepositoryUtil;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: DataAdapterParameterContributorFactory.java 4734 2011-10-21 12:13:21Z teodord $
 */
public final class DataAdapterParameterContributorFactory implements ParameterContributorFactory {

	public static final String PARAMETER_DATA_ADAPTER = "PARAMETER_DATA_ADAPTER";
	private static final DataAdapterParameterContributorFactory INSTANCE = new DataAdapterParameterContributorFactory();
	
	private DataAdapterParameterContributorFactory() {
	}

	/**
	 * 
	 */
	public static DataAdapterParameterContributorFactory getInstance() {
		return INSTANCE;
	}

	/**
	 *
	 */
	public List<ParameterContributor> getContributors(ParameterContributorContext context) throws JRException {
		List<ParameterContributor> contributors = new ArrayList<ParameterContributor>();

		DataAdapter dataAdapter = null;
		Object param = context.getParameterValues().get(PARAMETER_DATA_ADAPTER);
		if (param != null && param instanceof DataAdapter)
			dataAdapter = (DataAdapter) param;
		if (dataAdapter == null) {
			String dataAdapterUri = JRPropertiesUtil.getInstance(context.getJasperReportsContext()).getProperty(context.getDataset(), "net.sf.jasperreports.data.adapter");
			if (dataAdapterUri != null)
			{
				DataAdapterResource dataAdapterResource = RepositoryUtil.getInstance(context.getJasperReportsContext()).getResourceFromLocation(dataAdapterUri, DataAdapterResource.class);
				dataAdapter = dataAdapterResource.getDataAdapter();
			}
		}
		if (dataAdapter != null) {
			ParameterContributor dataAdapterService = DataAdapterServiceUtil.getInstance(context.getJasperReportsContext())
					.getService(dataAdapter);

			return Collections.singletonList(dataAdapterService);
		}
		return contributors;
	}
}
