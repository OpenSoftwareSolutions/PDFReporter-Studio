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
package com.jaspersoft.studio.data.hibernate.spring;

import net.sf.jasperreports.data.hibernate.spring.SpringHibernateDataAdapter;
import net.sf.jasperreports.data.hibernate.spring.SpringHibernateDataAdapterImpl;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.data.Activator;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;

public class SpringHibernateDataAdapterDescriptor extends DataAdapterDescriptor {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public SpringHibernateDataAdapter getDataAdapter() {
		if (dataAdapter == null)
			dataAdapter = new SpringHibernateDataAdapterImpl();
		return (SpringHibernateDataAdapter) dataAdapter;
	}


	@Override
	public DataAdapterEditor getEditor() {
		return new SpringHibernateDataAdapterEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#getIcon(int)
	 */
	@Override
	public Image getIcon(int size) {
		if (size == 16) {
			return Activator.getDefault().getImage("icons/hibernate.png");
		}
		return null;
	}
}
