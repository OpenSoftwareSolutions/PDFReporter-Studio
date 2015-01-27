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
package com.jaspersoft.studio.preferences.util;

import java.io.IOException;
import java.util.Properties;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.osgi.util.NLS;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

/**
 * Utility class to work with Eclipse Preferences.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public final class PreferencesUtils {

	// Eclipse property key maintaining all the JasperReports Properties
	public static final String NET_SF_JASPERREPORTS_JRPROPERTIES = "net.sf.jasperreports.JRPROPERTIES"; //$NON-NLS-1$
	
	/**
	 * Saves the specified JasperReports property in the dedicated Eclipse one.
	 * 
	 * @param key the property key
	 * @param value the property value
	 */
	public static void storeJasperReportsProperty(String key, String value){
		Properties jrProperties = loadJasperReportsProperties();
		if(jrProperties!=null){
			jrProperties.setProperty(key, value);
			getJaspersoftStudioPrefStore().setValue(
					NET_SF_JASPERREPORTS_JRPROPERTIES, FileUtils.getPropertyAsString(jrProperties));
		}
		else{
			JaspersoftStudioPlugin.getInstance().logError(
					NLS.bind(Messages.PreferencesUtils_CannotStoreJRPropertyError, key),null);
		}
	}
	
	
	/**
	 * Saves the specified JasperReports properties in the dedicated Eclipse one.
	 * 
	 * @param key the properties keys, must be not null and have the same size of value
	 * @param value the properties values, must be not null and have the same size of key
	 */
	public static void storeJasperReportsProperty(String[] key, String[] value){
		Assert.isNotNull(key);
		Assert.isNotNull(value);
		Assert.isTrue(key.length == value.length);
		Properties jrProperties = loadJasperReportsProperties();
		if(jrProperties!=null){
			for(int i=0;i<key.length;i++){
				jrProperties.setProperty(key[i], value[i]);
			}
			getJaspersoftStudioPrefStore().setValue(
					NET_SF_JASPERREPORTS_JRPROPERTIES, FileUtils.getPropertyAsString(jrProperties));
		}
		else{
			JaspersoftStudioPlugin.getInstance().logError(
					NLS.bind(Messages.PreferencesUtils_CannotStoreJRPropertyError, key),null);
		}
	}
	
	
	/**
	 * Gets the specified JasperReports property from the Workspace Preferences.
	 * 
	 * @param key the property key
	 * @return the property value found, <code>null</code> otherwise
	 */
	public static String getJasperReportsProperty(String key){
		Properties jrProperties = loadJasperReportsProperties();
		if(jrProperties!=null){
			return jrProperties.getProperty(key);
		}
		else{
			JaspersoftStudioPlugin.getInstance().logError(
					NLS.bind(Messages.PreferencesUtils_CannotReadJRPropertyError, key),null);
			return null;
		}
	}
	
	/**
	 * @return the Preferences Store for the Jaspersoft Studio plugin
	 */
	public static IPreferenceStore getJaspersoftStudioPrefStore(){
		return JaspersoftStudioPlugin.getInstance().getPreferenceStore();
	}
	
	/**
	 * Create the properties file from a string that embed all the properties pair key\value
	 * 
	 * @param propertiesString the string that contains all the properties to load
	 * @return
	 */
	public static Properties loadJasperReportsProperties(String propertiesString){
		try {
			return
					FileUtils.load(propertiesString);
		} catch (IOException e) {
			JaspersoftStudioPlugin.getInstance().logError(Messages.PreferencesUtils_CannotLoadJRPRopertiesError, e);
		}
		return null;
	}
	
	/*
	 * Loads the JasperReports properties as a {@link Properties} set instance.
	 */
	private static Properties loadJasperReportsProperties(){
		try {
			return
					FileUtils.load(getJaspersoftStudioPrefStore().getString(NET_SF_JASPERREPORTS_JRPROPERTIES));
		} catch (IOException e) {
			JaspersoftStudioPlugin.getInstance().logError(Messages.PreferencesUtils_CannotLoadJRPRopertiesError, e);
		}
		return null;
	}
}
