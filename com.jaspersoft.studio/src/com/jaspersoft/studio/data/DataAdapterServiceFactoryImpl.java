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
package com.jaspersoft.studio.data;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.DefaultDataAdapterServiceFactory;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBand.java 4319 2011-05-17 09:22:14Z teodord $
 */
public class DataAdapterServiceFactoryImpl extends DefaultDataAdapterServiceFactory {

	/**
	 *
	 */
	private static final DataAdapterServiceFactoryImpl INSTANCE = new DataAdapterServiceFactoryImpl();

	/**
	 *
	 */
	public static DataAdapterServiceFactoryImpl getInstance() {
		return INSTANCE;
	}

	/**
	 *
	 */
	public DataAdapterService getDataAdapterService(JasperReportsContext jasperReportsContext, DataAdapter dataAdapter) {
		DataAdapterService dataAdapterService = null;

		DataAdapterFactory daf = DataAdapterManager.findFactoryByDataAdapterClass(dataAdapter.getClass().getName());
		if (daf != null)
			dataAdapterService = daf.createDataAdapterService(dataAdapter);
		if (daf == null)
			return super.getDataAdapterService(jasperReportsContext, dataAdapter);
		return dataAdapterService;
	}

}
