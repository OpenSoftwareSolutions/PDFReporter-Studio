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
package com.jaspersoft.studio.data.adapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Properties;

/**
 * Class that represent the configuration of an iReport installation, and provide the methods
 * to read its configuration files
 * 
 * @author Orlandin Marco
 */
public class IReportDescriptor {

	/**
	 * File to the folder that contains the file ireport.properties
	 */
	protected File destination;
	
	/**
	 * Version of iReport
	 */
	protected String version;
	
	/**
	 * True if this configuration is of an iReport pro
	 */
	private boolean isPro;
	
	/**
	 * String append to the name of this configuration when it behold to a pro version 
	 * of iReport
	 */
	private static final String PRO_STRING = "Pro";
	
	protected Properties configurationProperties = null;
	
	protected Properties serverProperties = null;
	
	public IReportDescriptor(File destination, String version, boolean isPro){
		this.destination = destination;
		this.version = version;
		this.isPro = isPro;
	}
	
	/**
	 * Return the file to the folder that contains the configuration
	 */
	public File getFile(){
		return destination;
	}
	
	/**
	 * Return the name of this configuration
	 */
	public String getName(){
		if (isPro) return "iReport ".concat(version).concat(" ").concat(PRO_STRING);
		else return "iReport ".concat(version);
	}
	
	/**
	 * Return a properties file containing the iReport configuration, or null if this 
	 * file is not found
	 */
	public Properties getConfiguration(){
		
		if (configurationProperties == null) {
			configurationProperties = loadConfiguration(); 
		}
		return configurationProperties;
	
	}
	
	/**
	 * Read the properties file of iReport and return a subset of the properties with olny the ones compatible
	 * with JSS
	 * 
	 * @return a subset of the iReport properties file with the JSS compatible properties
	 */
	protected Properties loadConfiguration(){
		Properties prop = new Properties();
		String path = destination.getAbsolutePath().concat(ImportUtility.FILE_SEPARATOR).concat("ireport.properties");
		File newFile = new File(path);
		try {
			if (newFile.exists()){
				FileInputStream is = new FileInputStream(newFile);
				prop.load(is);
				is.close();
				
				Properties result = new Properties();
				for(Entry<Object, Object> entry : prop.entrySet()){
					String key = entry.getKey().toString();
					//Remove this prefix since it is for a jasper report property
					if (key.startsWith("ireport.jrproperty.")  && key.length() > 19 ){
						result.put(key.substring(19), entry.getValue());
					} else {
						result.put(key, entry.getValue());
					}
				}
				return result;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	protected Properties loadServerConfiguration(){
		Properties prop = new Properties();
		String path = destination.getAbsolutePath().concat(ImportUtility.FILE_SEPARATOR).concat("ireport")
																	.concat(ImportUtility.FILE_SEPARATOR).concat("jasperserver.properties");
		File newFile = new File(path);
		try {
			if (newFile.exists()){
				FileInputStream is = new FileInputStream(newFile);
				prop.load(is);
				is.close();
				return prop;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Return a properties file containing the JasperReport server connection configured in iReport
	 * or null if this file is not found
	 */
	public Properties getServerConnection(){
		
		if (serverProperties == null) {
			serverProperties = loadServerConfiguration(); 
		}
		return serverProperties;
	}
	
}
