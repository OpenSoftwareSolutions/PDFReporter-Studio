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

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactoryBundle;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.extensions.SingletonExtensionRegistry;

/*
 * @author veaceslav chicu (schicu@jaspersoft.com) 
 */
public class JrxExecuterExtensionsRegistryFactory implements
		ExtensionsRegistryFactory {
	private static final ExtensionsRegistry defaultExtensionsRegistry = new SingletonExtensionRegistry<JRQueryExecuterFactoryBundle>(
			JRQueryExecuterFactoryBundle.class,
			JrxQueryExecuterFactoryBundle.getInstance());

	public ExtensionsRegistry createRegistry(String registryId,
			JRPropertiesMap properties) {
		return defaultExtensionsRegistry;
	}
}
