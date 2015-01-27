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
package com.jaspersoft.jrx.query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;

public class JrxQueryExecuterFactoryBundle implements
		JRQueryExecuterFactoryBundle {
	private static final JRSingletonCache<QueryExecuterFactory> cache = new JRSingletonCache<QueryExecuterFactory>(
			QueryExecuterFactory.class);

	private static final JrxQueryExecuterFactoryBundle INSTANCE = new JrxQueryExecuterFactoryBundle();

	private JrxQueryExecuterFactoryBundle() {
	}

	/**
	 * 
	 */
	public static JrxQueryExecuterFactoryBundle getInstance() {
		return INSTANCE;
	}

	public String[] getLanguages() {
		return new String[] { PlSqlQueryExecuterFactory.QUERY_LANGUAGE_PLSQL,
				JRXPathQueryExecuterFactory.QUERY_LANGUAGE };
	}

	public QueryExecuterFactory getQueryExecuterFactory(String language)
			throws JRException {
		if (language
				.equalsIgnoreCase(PlSqlQueryExecuterFactory.QUERY_LANGUAGE_PLSQL))
			return cache.getCachedInstance(PlSqlQueryExecuterFactory.class
					.getName());
		if (language
				.equalsIgnoreCase(JRXPathQueryExecuterFactory.QUERY_LANGUAGE))
			return cache.getCachedInstance(JRXPathQueryExecuterFactory.class
					.getName());

		return null;
	}

}
