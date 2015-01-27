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
package com.jaspersoft.studio.data.sample;

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRAbstractBeanDataSourceProvider;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;


public class PersonBeansDataSource extends JRAbstractBeanDataSourceProvider {

	public PersonBeansDataSource() {
		super(PersonBean.class);
	}

	public JRDataSource create(JasperReport arg0) throws JRException {
		
		ArrayList<PersonBean> list = new ArrayList<PersonBean>();
		list.add(new PersonBean("Chengan"));
		list.add(new PersonBean("Giulio"));
		list.add(new PersonBean("Slavic"));
		list.add(new PersonBean("Teodor"));
		
		return new JRBeanCollectionDataSource(list);
	}
	
	public void dispose(JRDataSource arg0) throws JRException {
		// nothing to do
	}
}
