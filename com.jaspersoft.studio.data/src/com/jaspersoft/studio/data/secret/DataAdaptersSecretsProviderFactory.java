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
package com.jaspersoft.studio.data.secret;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.util.SecretsProvider;
import net.sf.jasperreports.util.SecretsProviderFactory;

/**
 * {@link SecretsProviderFactory} for the Data Adapters sensitive information.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class DataAdaptersSecretsProviderFactory implements SecretsProviderFactory {
	
	private static final List<String> categories;
	private static DataAdaptersSecretsProviderFactory instance;
	private DataAdaptersSecretsProvider dataAdaptersSecretsProvider;
	
	static {
		categories = new ArrayList<String>(1);
		categories.add(DataAdaptersSecretsProvider.SECRET_NODE_ID);
	}

	private DataAdaptersSecretsProviderFactory() {
	}

	public static DataAdaptersSecretsProviderFactory getInstance() {
		if (instance == null)
			instance = new DataAdaptersSecretsProviderFactory();
		return instance;
	}

	@Override
	public SecretsProvider getSecretsProvider(String category) {
		if(categories.contains(category)) {
			if (dataAdaptersSecretsProvider == null)
				dataAdaptersSecretsProvider = new DataAdaptersSecretsProvider();
			return dataAdaptersSecretsProvider;
		}
		return null;
	}

}
