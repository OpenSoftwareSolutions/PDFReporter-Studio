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
package com.jaspersoft.translation.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

/**
 * This class define a file resource that can recovered from the translation editor
 * and easily localized by the user
 * 
 * @author Orlandin Marco
 *
 */
public abstract class AbstractResourceDefinition {
	
	/**
	 * Cache for the strings read from the resource file. the key is the unique identifier of 
	 * the file, the value is the properties map of that file
	 */
	private static HashMap<String, Hashtable<Object, Object>> stringsMap = new HashMap<String, Hashtable<Object,Object>>();
	
	/**
	 * Return an array of strings that are the keys inside the 
	 * Resource file
	 * 
	 * @return not null array containing in each position a key
	 * of the resource file
	 */
	public String[] getKeys() {
		Hashtable<Object, Object> map = getMap();
		Set<Object> keys = map.keySet();
		String[] result = new String[keys.size()];
		int i=0;
		for(Object key : keys){
			result[i] = key.toString();
			i++;
		}
		return result;
	}

	/**
	 * Return an array of strings that are the values inside the 
	 * Resource file
	 * 
	 * @return not null array containing in each position a values
	 * of the resource file
	 */
	public String[] getValues() {
		Hashtable<Object, Object> map = getMap();
		String[] result = new String[map.values().size()];
		String[] keys = getKeys();
		int i=0;
		for(String key : keys){
			result[i] = getValue(key);
			i++;
		}
		return result;
	}
	
	/**
	 * Return the map that contains all the pairs key\value inside 
	 * the properties file. When this method is called the first time
	 * the map is created and initialized from the file then cached.
	 * The following times the cached one is returned
	 * 
	 * @return a not null map  that contains all the pairs key\value inside 
	 * the properties file.
	 */
	protected Hashtable<Object,Object> getMap(){
		String uniqueId = getUniqueId();
		Hashtable<Object,Object> result = stringsMap.get(uniqueId);
		if (result == null)  {
			//not cached need to load it
			result = new Properties();		
			try {
				InputStream is = getFileInput();
				if (is != null) {
					((Properties)result).load(is);
					stringsMap.put(uniqueId, result);
				} else System.out.println("Resource "+uniqueId+" not found");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * Return the map that contains all the pairs key\value inside 
	 * the properties file. When this method is called the first time
	 * the map is created and initialized from the file then cached.
	 * The following times the cached one is returned
	 * 
	 * @param loc the requested language
	 * @return a not null map  that contains all the pairs key\value inside 
	 * the properties file. The map is void if the specified language can not
	 * be found
	 */
	public Hashtable<Object,Object> getLocalizedProerties(Locale loc){
		InputStream is = getLocalizedInput(loc);
		Hashtable<Object,Object> result = new Properties();		
		if (is == null) return result;
		else {
			try {
					((Properties)result).load(is);
			} catch (IOException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	/**
	 * Return the value of a specific key inside the properties file
	 * 
	 * @param key the key
	 * @return the value, can be null if the key is not found
	 */
	public String getValue(String key) {
		Hashtable<Object, Object> map = getMap();
		Object value = map.get(key);
		return value != null ? value.toString() : null;
	}
	
	/**
	 * Return the file name without the extension .properties if present
	 * 
	 * @return the filename without the .properties extension
	 */
	public String getFileNameWithoutExtension(){
		String fileName = getFileName();
		if (fileName.endsWith(".properties")) return fileName.substring(0, fileName.length()-11);
		return fileName;
	}
	
	/**
	 * Return an unique id for the represented property file, used for caching it
	 * 
	 * @return an unique id of the file
	 */
	public String getUniqueId(){
		return getPluginName()+getPackageName()+getFileName();
	}
	
	/**
	 * Return an inputstram to the properties source file
	 * 
	 * @return a not null inputstream from where the pairs
	 * key\value are read
	 */
	protected abstract InputStream getFileInput();
	
	/**
	 * Return an inputstram to the properties source file of a specific language
	 * 
	 * @return a not null inputstream from where the pairs
	 * key\value are read if it can be found in the specific language
	 * null otherwise
	 */
	protected abstract InputStream getLocalizedInput(Locale loc);
	
	/**
	 * Return the description of the file
	 * 
	 * @return a not null textual description
	 */
	public abstract String getDescription();
	
	/**
	 * Return the package where the file is placed
	 * 
	 * @return a text representing the name of the package where
	 * the properties file is placed or null if the file is not inside
	 * a package
	 */
	public abstract String getPackageName();
	
	/**
	 * Return the filename with it's extension
	 * 
	 * @return the name of the file with it's extension, must be not null
	 */
	public abstract String getFileName();
	
	/**
	 * Return the locale of the handled properties file
	 * 
	 * @return a not null string representing the locale of the file
	 */
	public abstract String getLocale();
	
	/**
	 * Return the plugin name where the file is placed
	 * 
	 * @return a text representing the name of the plugin where
	 * the properties file is placed, must be not null
	 * 
	 */
	public abstract String getPluginName();
	
	/**
	 * Determines whether two objects are equal, including <code>null</code> values.
	 * 
	 * @param o1
	 * @param o2
	 * @return whether the two objects are equal
	 */
	public static boolean safeEquals(Object o1, Object o2)
	{
		return (o1 == null) ? (o2 == null) : (o2 != null && o1.equals(o2));
	}
	
	/**
	 * Check if the ResourceDefinition are pointing at the same resource (if they have the same package
	 * and filename).
	 */
	@Override
	public boolean equals(Object obj) {
		AbstractResourceDefinition resource = (AbstractResourceDefinition)obj;
		
		return safeEquals(getPackageName(), resource.getPackageName()) && 
			   safeEquals(getPluginName(), resource.getPluginName());
	}
	
}
