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
package com.jaspersoft.studio.data.hive;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterService;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.connectors.hive.adapter.HiveDataAdapter;
import com.jaspersoft.connectors.hive.adapter.HiveDataAdapterImpl;
import com.jaspersoft.connectors.hive.adapter.HiveDataAdapterService;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;
import com.jaspersoft.studio.data.hive.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * @author gtoffoli
 * 
 */
public class HiveDataAdapterFactory implements DataAdapterFactory {

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#createDataAdapter()
	 */
	public DataAdapterDescriptor createDataAdapter() {
		HiveDataAdapterDescriptor descriptor = new HiveDataAdapterDescriptor();
		descriptor.getDataAdapter().setUrl("jdbc:hive://localhost:10000/default"); //$NON-NLS-1$
		return descriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.jaspersoft.studio.data.DataAdapterFactory#getDataAdapterClassName()
	 */
	public String getDataAdapterClassName() {
		return HiveDataAdapterImpl.class.getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#getDescription()
	 */
	public String getLabel() {
		return Messages.HiveDataAdapterFactory_label;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#getDescription()
	 */
	public String getDescription() {
		return Messages.HiveDataAdapterFactory_description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#getIcon(int)
	 */
	public Image getIcon(int size) {
		if (size == 16)
			return Activator.getDefault().getImage("icons/hive.png"); //$NON-NLS-1$ 
		return null;
	}

	public DataAdapterService createDataAdapterService(DataAdapter dataAdapter) {
		if (dataAdapter instanceof HiveDataAdapter)
			return new HiveDataAdapterService(JasperReportsConfiguration.getDefaultJRConfig(), (HiveDataAdapter) dataAdapter);
		return null;
	}

	@Override
	public IDataAdapterCreator iReportConverter() {
		return new HiveCreator();
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}
}
