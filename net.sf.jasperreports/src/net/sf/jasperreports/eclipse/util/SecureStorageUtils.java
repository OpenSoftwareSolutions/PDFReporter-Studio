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
package net.sf.jasperreports.eclipse.util;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.SecurePreferencesFactory;
import org.eclipse.equinox.security.storage.StorageException;

/**
 * Utility class containing the API to manage the secure preferences in
 * Eclipse/Equinox.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class SecureStorageUtils {

	private SecureStorageUtils(){
		// Do nothing - prevent instantiation
	}
	
	/**
	 * Returns default secure preferences.
	 * 
	 * @return the default secure preferences
	 * @see SecurePreferencesFactory#getDefault()
	 */
	public static ISecurePreferences getDefaultSecurePreferences() {
		return SecurePreferencesFactory.getDefault();
	}

	/**
	 * Returns node corresponding to the path specified inside the default
	 * secure preferences. If such node does not exist, no new node is created.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @return node corresponding to the path if it exists, <code>null</code>
	 *         otherwise
	 */
	public static ISecurePreferences getDefaultSecurePreferencesNode(String pathName) {
		return getSecurePreferencesNode(pathName, getDefaultSecurePreferences(), false);
	}
	
	/**
	 * Returns node corresponding to the path specified inside the default
	 * secure preferences. If such node does not exist, a new node is created
	 * depending on the specified <code>createIfMissing</code> parameter.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @param createIfMissing
	 *            flag to decide whether to create the new node or not
	 * @return node corresponding to the path
	 */
	public static ISecurePreferences getDefaultSecurePreferencesNode(String pathName, boolean createIfMissing) {
		return getSecurePreferencesNode(pathName, getDefaultSecurePreferences(), createIfMissing);
	}
	
	/**
	 * Returns node corresponding to the path specified inside the specified
	 * secure preferences. If such node does not exist, no new node is created.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @param preferences
	 *            the secure preferences to look into
	 * @return node corresponding to the path if it exists, <code>null</code>
	 *         otherwise
	 */
	public static ISecurePreferences getSecurePreferencesNode(String pathName, ISecurePreferences preferences) {
		return getSecurePreferencesNode(pathName, preferences, false);
	}
	
	/**
	 * Returns node corresponding to the path specified inside the specified
	 * secure preferences. If such node does not exist, a new node is created
	 * depending on the specified <code>createIfMissing</code> parameter.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @param preferences
	 *            the secure preferences to look into
	 * @param createIfMissing
	 *            flag to decide whether to create the new node or not
	 * @return node corresponding to the path
	 */
	public static ISecurePreferences getSecurePreferencesNode(String pathName, ISecurePreferences preferences, boolean createIfMissing) {
		Assert.isNotNull(pathName);
		Assert.isNotNull(preferences);
		if(preferences.nodeExists(pathName) || createIfMissing){
			return preferences.node(pathName);
		}
		return null;
	}
	
	/**
	 * Stores the specified value into the default secure preferences node
	 * specified.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @param key
	 *            key with which the value is going to be associated
	 * @param value
	 *            value to store
	 * @throws StorageException
	 *             if exception occurred during encryption
	 */
	public static void saveToDefaultSecurePreferences(String pathName, String key, String value) throws StorageException {
		ISecurePreferences prefNode = getDefaultSecurePreferencesNode(pathName, true);
		prefNode.put(key, value, true);
	}
	
	/**
	 * Retrieves a value associated with the key in this default secure
	 * preferences node.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @param key
	 *            key with this the value is associated
	 * @param defaultValue
	 *            default value to return if the key is not associated with any
	 *            value
	 * @return decrypted value associated to the key
	 * @throws StorageException
	 *             if exception occurred during decryption
	 */
	public static String readFromDefaultSecurePreferences(String pathName, String key, String defaultValue) throws StorageException {
		ISecurePreferences prefNode = getDefaultSecurePreferencesNode(pathName);
		if(prefNode!=null) {
			return prefNode.get(key, defaultValue);			
		}
		return null;
	}
	
	/**
	 * Retrieves a value associated with the key in this default secure
	 * preferences node.
	 * If nothing is found then <code>null</code> value is returned.
	 * 
	 * @param pathName
	 *            absolute or relative path to the node
	 * @param key
	 *            key with this the value is associated
	 * @return decrypted value associated to the key, <code>null</code> otherwise
	 * @throws StorageException
	 *             if exception occurred during decryption
	 */
	public static String readFromDefaultSecurePreferences(String pathName, String key) throws StorageException {
		return readFromDefaultSecurePreferences(pathName, key, null);
	}
	
}
