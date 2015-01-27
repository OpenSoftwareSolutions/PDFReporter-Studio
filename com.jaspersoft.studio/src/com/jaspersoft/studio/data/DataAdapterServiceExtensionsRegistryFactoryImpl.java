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
package com.jaspersoft.studio.data;

import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.data.DataAdapterServiceFactory;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;


/**
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: GovernorExtensionsRegistryFactory.java 3034 2009-08-27 11:58:04Z teodord $
 */
public class DataAdapterServiceExtensionsRegistryFactoryImpl implements ExtensionsRegistryFactory
{
	private static final ExtensionsRegistry extensionsRegistry = 
		new ExtensionsRegistry()
		{
			public <T> List<T> getExtensions(Class<T> extensionType) 
			{
				if (DataAdapterServiceFactory.class.equals(extensionType))
				{
					return (List<T>) Collections.singletonList(DataAdapterServiceFactoryImpl.getInstance());
				}
				return null;
			}

		};
	
	public ExtensionsRegistry createRegistry(String registryId, JRPropertiesMap properties) 
	{
		return extensionsRegistry;
	}
}
