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
package com.jaspersoft.studio.data.hbase;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterService;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.hadoop.hbase.adapter.HBaseDataAdapter;
import com.jaspersoft.hadoop.hbase.adapter.HBaseThriftDataAdapter;
import com.jaspersoft.hadoop.hbase.adapter.HBaseThriftDataAdapterImpl;
import com.jaspersoft.hadoop.hbase.adapter.HBaseThriftDataAdapterService;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;
import com.jaspersoft.studio.data.hbase.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseThriftDataAdapterFactory implements DataAdapterFactory {
	public HBaseThriftDataAdapterFactory() {
	}

	@Override
	public DataAdapterDescriptor createDataAdapter() {
		return new HBaseThriftDataAdapterDescriptor();
	}

	@Override
	public String getDataAdapterClassName() {
		return HBaseThriftDataAdapterImpl.class.getName();
	}

	@Override
	public String getLabel() {
		return Messages.HBaseThriftDataAdapterFactory_0;
	}

	@Override
	public String getDescription() {
		return Messages.HBaseThriftDataAdapterFactory_1;
	}

	@Override
	public Image getIcon(int size) {
		if (size == 16)
			return Activator.getDefault().getImage("icons/HBase_Logo.png"); //$NON-NLS-1$ 
		return null;
	}

	public DataAdapterService createDataAdapterService(DataAdapter dataAdapter) {
		if (dataAdapter instanceof HBaseDataAdapter)
			return new HBaseThriftDataAdapterService(JasperReportsConfiguration.getDefaultJRConfig(), (HBaseThriftDataAdapter) dataAdapter);
		return null;
	}

	@Override
	public IDataAdapterCreator iReportConverter() {
		return null;
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}
}
