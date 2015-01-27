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
package com.jaspersoft.studio.rcp.handlers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.eclipse.ui.AbstractSourceProvider;

import com.jaspersoft.studio.ConfigurationPathProvider;

/**
 * This class provide and environment variable with the locale language
 * that can be used directly from the plugin.xml. Even if it actually
 * not used in the future could be implemented a method to make the 
 * actual selected language not visible (its already possible, but has
 * the problem that the language list is not update when an element is
 * selected and JSS is not restarted)
 * 
 * @author Orlandin Marco
 *
 */
public class LocaleSourceProvider extends AbstractSourceProvider {

	/**
	 * Key of the variable
	 */
	public static final String ACTUAL_LOCALE = "actual_locale";
	
	/**
	 * Cached value of the variable
	 */
	private static String actualLocale = null;

	@Override
	public void dispose() {
	}
	
	/**
	 * Read the locale value from the configuration file of the product
	 * if it is found, otherwise take it directly from the environment
	 * 
	 * @return string with the locale code of the language
	 */
	private static String getActualLocale() {
		URL location = null;
		String path = ConfigurationPathProvider.getPath();
		String locale = null;
		try {
			location = new URL(path);
		} catch (MalformedURLException e) {}
		try {
			String fileName = location.getFile();
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			try {
				String line = in.readLine();
				while(line !=null && locale == null){
					if (line.equals("-nl")) {
						locale = in.readLine();
					} else line = in.readLine();
				}
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			locale = Locale.getDefault().toString();
		}
		if (locale == null) locale = Locale.getDefault().toString();
		return locale;
	}
	
	/**
	 * Return the last read locale value 
	 * 
	 * @return string with the locale code of the language
	 */
	public static String getLocale(){
		if (actualLocale == null) actualLocale = getActualLocale();
		return actualLocale;
	}
	
	/**
	 * Force the update of the cached locale value
	 */
	public void forceRefreshLocale(){
		 actualLocale = getActualLocale();
	}

	@Override
	public Map<String, String> getCurrentState() {
		Map<String,String> currentState = new HashMap<String,String>(1);
		String locale =  getLocale();
		currentState.put(ACTUAL_LOCALE, locale);
		return currentState;
	}

	@Override
	public String[] getProvidedSourceNames() {
		return new String[] {ACTUAL_LOCALE};
	}

}
