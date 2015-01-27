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
package net.sf.jasperreports.eclipse.util.query;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.data.AbstractClasspathAwareDataAdapterService;
import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.query.DefaultQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.engine.query.QueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRSingletonCache;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
import net.sf.jasperreports.extensions.ExtensionsRegistry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * @author sanda zaharia (shertage@users.sourceforge.net)
 * @version $Id: EmptyQueryExecuterFactoryBundle.java 920 2014-07-23 16:22:43Z mrabbi $
 */
public final class EmptyQueryExecuterFactoryBundle implements JRQueryExecuterFactoryBundle {
	private final Log log = LogFactory.getLog(EmptyQueryExecuterFactory.class);

	private static final JRSingletonCache cache = new JRSingletonCache(QueryExecuterFactory.class);

	private static final EmptyQueryExecuterFactoryBundle INSTANCE = new EmptyQueryExecuterFactoryBundle();
	private JasperReportsContext jasperReportsContext;

	private EmptyQueryExecuterFactoryBundle() {
		this(DefaultJasperReportsContext.getInstance());
	}

	private EmptyQueryExecuterFactoryBundle(JasperReportsContext jasperReportsContext) {
		this.jasperReportsContext = jasperReportsContext;
	}

	/**
	 * 
	 */
	public static EmptyQueryExecuterFactoryBundle getInstance() {
		return INSTANCE;
	}

	public static EmptyQueryExecuterFactoryBundle getInstance(JasperReportsContext jasperReportsContext) {
		return new EmptyQueryExecuterFactoryBundle(jasperReportsContext);
	}

	/**
	 * 
	 */
	public String[] getLanguages() {
		ExtensionsRegistry extensionsRegistry = ExtensionsEnvironment.getExtensionsRegistry();
		if (extensionsRegistry != oldExtensionsRegistry)
			init(extensionsRegistry);
		return languages;
	}

	protected void init(ExtensionsRegistry extensionsRegistry) {
		ClassLoader originalClassLoader = Thread.currentThread().getContextClassLoader();
		Object cl = jasperReportsContext.getValue(AbstractClasspathAwareDataAdapterService.CURRENT_CLASS_LOADER);
		if (cl != null && cl instanceof ClassLoader)
			Thread.currentThread().setContextClassLoader((ClassLoader) cl);
		try {
			oldExtensionsRegistry = extensionsRegistry;
			Set<String> langs = new HashSet<String>();
			mqef = new HashMap<String, QueryExecuterFactory>();
			List<JRQueryExecuterFactoryBundle> bundles = ExtensionsEnvironment.getExtensionsRegistry().getExtensions(JRQueryExecuterFactoryBundle.class);
			for (JRQueryExecuterFactoryBundle bundle : bundles) {
				if (!bundle.getClass().equals(this.getClass())) {
					if (bundle instanceof DefaultQueryExecuterFactoryBundle)
						bundle = DefaultQueryExecuterFactoryBundle.getInstance(jasperReportsContext);

					String[] l = bundle.getLanguages();
					for (String lang : l) {
						langs.add(lang);
						if (!mqef.containsKey(lang))
							try {
								mqef.put(lang, bundle.getQueryExecuterFactory(lang));
							} catch (JRException e) {
								e.printStackTrace();
							}
					}
				}
			}
			languages = langs.toArray(new String[langs.size()]);
		} finally {
			Thread.currentThread().setContextClassLoader(originalClassLoader);
		}
	}

	private String[] languages;
	private Map<String, QueryExecuterFactory> mqef;
	private ExtensionsRegistry oldExtensionsRegistry;

	/**
	 * 
	 */
	public QueryExecuterFactory getQueryExecuterFactory(String language) throws JRException {
		ExtensionsRegistry extensionsRegistry = ExtensionsEnvironment.getExtensionsRegistry();
		if (extensionsRegistry != oldExtensionsRegistry)
			init(extensionsRegistry);
		QueryExecuterFactory qef = mqef.get(language);
		if (qef == null) {
			if (log.isWarnEnabled())
				log.warn(MessageFormat.format(Messages.EmptyQueryExecuterFactoryBundle_NoFactoryClassRegistered, new Object[] { language }));

			qef = (QueryExecuterFactory) cache.getCachedInstance(EmptyQueryExecuterFactory.class.getName());
		}
		return qef;
	}
}
