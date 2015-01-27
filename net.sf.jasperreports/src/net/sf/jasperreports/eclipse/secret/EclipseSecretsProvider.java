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
package net.sf.jasperreports.eclipse.secret;

import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.JasperReportsPlugin;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.SecureStorageUtils;
import net.sf.jasperreports.util.SecretsProvider;

import org.eclipse.equinox.security.storage.StorageException;

/**
 * {@link SecretsProvider} for the generic Jaspersoft Studio sensitive information.
 * 
 */
public class EclipseSecretsProvider implements SecretsProvider {
	
	public static final String SECRET_NODE_ID = "net.sf.jasperreports.jss"; //$NON-NLS-1$

	@Override
	public String getSecret(String key) {
		// Looks in the secure storage for the specified key.
		// If nothing is found, the key is returned in order to ensure back-compatibility.
		try {
			String value = SecureStorageUtils.readFromDefaultSecurePreferences(getSecretNodeId(), key);
			return value!=null ? value : key;
		} catch (StorageException ex) {
			JasperReportsPlugin.getDefault().logError(
					MessageFormat.format("Unable to recover the value for key {0} inside the secure storage ''{1}''.",new Object[]{key,getSecretNodeId()}), ex);
			UIUtils.showError(ex);
		}
		return key;
	}

	@Override
	public boolean hasSecret(String key) {
		try {
			return SecureStorageUtils.readFromDefaultSecurePreferences(getSecretNodeId(), key)!=null;
		} catch (StorageException ex) {
			JasperReportsPlugin.getDefault().logError(
					MessageFormat.format("Unable to recover the value for key {0} inside the secure storage ''{1}''.",new Object[]{key,getSecretNodeId()}), ex);
			UIUtils.showError(ex);
		}
		return false;
	}
	
	/**
	 * Returns the node id corresponding to this {@link SecretsProvider}.
	 * It allows to find out the node inside the default Equinox secure preferences.
	 * 
	 * @return the node id for the secure preferences storage
	 */
	public String getSecretNodeId() {
		return SECRET_NODE_ID;
	}
	
}
