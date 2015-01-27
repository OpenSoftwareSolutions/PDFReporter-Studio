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

import com.jaspersoft.studio.JaspersoftStudioPlugin;

import net.sf.jasperreports.eclipse.secret.EclipseSecretsProvider;
import net.sf.jasperreports.util.SecretsProvider;

/**
 * {@link SecretsProvider} for the JasperReports Server sensitive information.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JRServerSecretsProvider extends EclipseSecretsProvider {
	
	public static final String SECRET_NODE_ID = "com.jaspersoft.studio.jrserver";
	
	@Override
	public boolean hasSecret(String key) {
		// Ensure back-compatibility
		return true;
	}
	
	@Override
	public String getSecretNodeId() {
		return SECRET_NODE_ID;
	}
	
	@Override
	public String getSecret(String key) {
		return JaspersoftStudioPlugin.shouldUseSecureStorage() ? super.getSecret(key) : key;
	}
}
