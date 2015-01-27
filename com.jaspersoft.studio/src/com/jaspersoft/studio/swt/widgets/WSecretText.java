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
package com.jaspersoft.studio.swt.widgets;

import java.util.UUID;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.SecureStorageUtils;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.util.SecretsUtil;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.security.storage.StorageException;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * A custom text widget that allows to deal with information saved 
 * inside the secure preferences.
 * <p>
 * 
 * The secret is stored in the secure preferences using a UUID as key.
 * The widget must be properly configured and initialized using the {@link #loadSecret(String, String)} method.
 * The information can be persisted invoking the method {@link #persistSecret()}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class WSecretText extends Text { 

	private JasperReportsContext jrContext;
	private String secretCategory;
	private String key;
	private UUID uuid;
	
	public WSecretText(Composite parent, int style) {
		super(parent, style);
	}

	private void setSecretCategory(String secretCategory) {
		this.secretCategory = secretCategory;
	}
	
	private void setKey(String key) {
		this.key = key;
	}
	
	public String getUUIDKey() {
		Assert.isNotNull(this.uuid);
		return this.uuid.toString();
	}
	
	/**
	 * Store the secret information inside the secure preferences.
	 */
	public void persistSecret() {
		// We need to store the UUID as key and the text as value
		// in the secure preferences.
		try {
			String uuidStr = uuid.toString();
			String widgetText = getText();
			if(!uuidStr.equals(widgetText)){
				SecureStorageUtils.saveToDefaultSecurePreferences(secretCategory, uuidStr, widgetText);
			}
		} catch (StorageException e) {
			UIUtils.showError(e);
		};
	}
	
	/**
	 * Initialize the secret text widget with the essential information.
	 * It could be invoked only once.
	 * 
	 * @param secretCategory the secret category
	 * @param key the secret key
	 */
	public void loadSecret(String secretCategory, String key) {
		if(!isWidgetConfigured()) {
			Assert.isNotNull(secretCategory);
			Assert.isNotNull(key);
			setSecretCategory(secretCategory);
			setKey(key);
			SecretsUtil sInstance = SecretsUtil.getInstance(getJRContext());
			String secret = sInstance.getSecret(secretCategory, key);
			if(secret.equals(key)) {
				// back-compatibility problem: information was clear text
				// we need to generate a new UUID to be used as key
				uuid = UUID.randomUUID();
				setText(secret);
			}
			else {
				// the key is the previous generated UUID
				uuid = UUID.fromString(key);
			}
		}
		else {
			throw new RuntimeException("Widget can be initialized only once!");
		}
	}
	
	/**
	 * Checks if the secret text widget is configured.
	 * This could happen only when the {@link #loadSecret(String, String)} method 
	 * has been invoked.
	 * 
	 * @return <code>true</code> if the widget is configured, <code>false</code> otherwise
	 */
	public boolean isWidgetConfigured() {
		return this.secretCategory != null &&	this.key != null;
	}

	/*
	 * Returns the JasperReports context suitable for the SecretUtil instance.
	 */
	private JasperReportsContext getJRContext() {
		if(jrContext!=null){
			return jrContext;
		}
		else {
			// default fallback solution
			return JasperReportsConfiguration.getDefaultJRConfig();
		}
	}
	
	@Override
	protected void checkSubclass() {
		// allow sub-classing
	}
	
}
