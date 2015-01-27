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
package com.jaspersoft.hadoop.hbase.query;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;

import com.jaspersoft.hadoop.hbase.HBaseDataSource;

/**
 * 
 * @author Eric Diaz
 * 
 */
public class HBaseQueryExecuterFactoryBundle implements JRQueryExecuterFactoryBundle {
	private static final JRSingletonCache<QueryExecuterFactory> cache = new JRSingletonCache<QueryExecuterFactory>(QueryExecuterFactory.class);

	private static final HBaseQueryExecuterFactoryBundle instance = new HBaseQueryExecuterFactoryBundle();

	private static final String[] languages = new String[] { HBaseDataSource.QUERY_LANGUAGE };

	private HBaseQueryExecuterFactoryBundle() {
	}

	public static HBaseQueryExecuterFactoryBundle getInstance() {
		return instance;
	}

	public String[] getLanguages() {
		return languages;
	}

	public QueryExecuterFactory getQueryExecuterFactory(String language) throws JRException {
		if (HBaseDataSource.QUERY_LANGUAGE.equals(language))
			return (QueryExecuterFactory) cache.getCachedInstance(HBaseQueryExecuterFactory.class.getName());
		return null;
	}
}
