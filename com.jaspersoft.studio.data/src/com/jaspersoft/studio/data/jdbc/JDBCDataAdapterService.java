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
package com.jaspersoft.studio.data.jdbc;

import net.sf.jasperreports.data.jdbc.JdbcDataAdapter;
import net.sf.jasperreports.data.jdbc.JdbcDataAdapterService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;

/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JRBaseBand.java 4319 2011-05-17 09:22:14Z teodord $
 */
public class JDBCDataAdapterService extends JdbcDataAdapterService {
	
	JdbcDataAdapter jdbcDataAdapter = null;
	
	public JDBCDataAdapterService(JasperReportsContext jContext,
			JdbcDataAdapter jdbcDataAdapter) {
		super(jContext, jdbcDataAdapter);
		
		this.jdbcDataAdapter = jdbcDataAdapter;
	}

	@Override
	public String getPassword() throws JRException {
	
		System.out.println("Asking for password.... " + jdbcDataAdapter.getPassword());
		if (jdbcDataAdapter.getPassword() == null)
		{
			throw new JRException("FIXME: Password dialog not implemented!");
		}
		
		return jdbcDataAdapter.getPassword();
	}

	
}
