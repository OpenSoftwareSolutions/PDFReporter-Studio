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
package com.jaspersoft.studio.data.mongodb;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterService;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.mongodb.adapter.MongoDbDataAdapter;
import com.jaspersoft.mongodb.adapter.MongoDbDataAdapterImpl;
import com.jaspersoft.mongodb.adapter.MongoDbDataAdapterService;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;
import com.jaspersoft.studio.data.mongodb.messages.Messages;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * @author gtoffoli
 * 
 */
public class MongoDbDataAdapterFactory implements DataAdapterFactory {

    /*
     * (non-Javadoc)
     * 
     * @see com.jaspersoft.studio.data.DataAdapterFactory#createDataAdapter()
     */
    public DataAdapterDescriptor createDataAdapter() {
        MongoDbDataAdapterDescriptor descriptor = new MongoDbDataAdapterDescriptor();
        descriptor.getDataAdapter().setMongoURI("mongodb://HOST:27017/DB_NAME"); //$NON-NLS-1$
        descriptor.getDataAdapter().setUsername(""); //$NON-NLS-1$
        descriptor.getDataAdapter().setPassword(""); //$NON-NLS-1$
        return descriptor;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * com.jaspersoft.studio.data.DataAdapterFactory#getDataAdapterClassName()
     */
    public String getDataAdapterClassName() {
        return MongoDbDataAdapterImpl.class.getName();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jaspersoft.studio.data.DataAdapterFactory#getDescription()
     */
    public String getLabel() {
        return Messages.MongoDbDataAdapterFactory_label;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jaspersoft.studio.data.DataAdapterFactory#getDescription()
     */
    public String getDescription() {
        return Messages.MongoDbDataAdapterFactory_description;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.jaspersoft.studio.data.DataAdapterFactory#getIcon(int)
     */
    public Image getIcon(int size) {
        if (size == 16) {
            return Activator.getDefault().getImage(Activator.ICON_NAME);
        }
        return null;
    }

    public DataAdapterService createDataAdapterService(DataAdapter dataAdapter) {
        if (dataAdapter instanceof MongoDbDataAdapter)
            return new MongoDbDataAdapterService(JasperReportsConfiguration.getDefaultJRConfig(), (MongoDbDataAdapter) dataAdapter);
        return null;
    }

	@Override
	public IDataAdapterCreator iReportConverter() {
		return new MongoDBCreator();
	}

	@Override
	public boolean isDeprecated() {
		return false;
	}
}
