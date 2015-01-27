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
package com.jaspersoft.studio.server.secret;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.util.SecretsProvider;
import net.sf.jasperreports.util.SecretsProviderFactory;

/**
 * {@link SecretsProviderFactory} for the JasperReports Server sensitive information.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JRServerSecretsProviderFactory implements SecretsProviderFactory {
	
	private static final List<String> categories;
	private static JRServerSecretsProviderFactory instance;
	private JRServerSecretsProvider jRServerSecretsProvider;
	
	static {
		categories = new ArrayList<String>(1);
		categories.add(JRServerSecretsProvider.SECRET_NODE_ID);
	}

	private JRServerSecretsProviderFactory() {
	}

	public static JRServerSecretsProviderFactory getInstance() {
		if (instance == null)
			instance = new JRServerSecretsProviderFactory();
		return instance;
	}

	@Override
	public SecretsProvider getSecretsProvider(String category) {
		if(categories.contains(category)) {
			if (jRServerSecretsProvider == null)
				jRServerSecretsProvider = new JRServerSecretsProvider();
			return jRServerSecretsProvider;
		}
		return null;
	}

}
