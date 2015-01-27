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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.swt.SWT;
import org.w3c.dom.Document;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.DataAdapterManager;

/**
 * Generic utilities to import server configurations and data adapters from iReport
 * 
 * @author Orlandin Marco
 */
public class ImportUtility {
	
	/**
	 * Character used by the operative system to separate the path folders
	 */
	public final static String FILE_SEPARATOR = System.getProperty("file.separator");;

	/**
	 * Enumeration for the supported operative systems
	 */
	public enum OperativeSystem{MacOS, Windows, Linux}
	
	/**
	 * Static path inside the ireport configuration folders structure, that brings to the configuration file
	 */
	private static final String configurationPrefix = FILE_SEPARATOR.concat("config").concat(FILE_SEPARATOR).concat("Preferences")
															.concat(FILE_SEPARATOR).concat("com").concat(FILE_SEPARATOR).concat("jaspersoft");
	
	/**
	 * A map of all the converter for the data adapter, the key is the connectionClass used in ireport to specify the 
	 * type of a data adapter, and the value is the converter that can create a JSS data adapter from that type of iReport adapter
	 */
	private static HashMap<String, IDataAdapterCreator> importManager = null;
	
	/**
	 * Initialize the importManager map
	 */
	private static void createImportManager(){
		importManager = new HashMap<String, IDataAdapterCreator>();
		
		for (DataAdapterFactory factory : DataAdapterManager.getDataAdapterFactories()){
			IDataAdapterCreator creator = factory.iReportConverter();
			if (creator != null){
				importManager.put(creator.getID(), creator);
			}
		}
	}
	
	/**
	 * Return the actual operative system
	 */
	public static OperativeSystem GetPlatform () {
		String platform = SWT.getPlatform();
		if (platform.equals("cocoa") || platform.equals("carbon")) return OperativeSystem.MacOS; 
		else if (platform.toLowerCase().startsWith("win")) return OperativeSystem.Windows;
		return OperativeSystem.Linux;
	}
	
	/**
	 * Return a list of all the iReport configuration in a precise path 
	 * 
	 * @param path the path of the iReport folder (the folder that contains the X.Y.Z folders of i report
	 * where X, Y, and Z are version number
	 * @param if the parent folder is the one of a pro version of JSS
	 * 
	 * @return a not null list of all the configurations found in the specified path
	 * 
	 */
	private static List<IReportDescriptor> getVersions(String path, boolean isPro){
		List<IReportDescriptor> result = new ArrayList<IReportDescriptor>();
		String versionExpression = "[0-9]\\.[0-9]\\.[0-9]";
		File destination = new File(path);
		if (destination.exists()){
			File[] children = destination.listFiles();
			for(File child : children){
				if (child.isDirectory() && child.getName().matches(versionExpression)){
					String confPath = child.getAbsolutePath().concat(FILE_SEPARATOR).concat(configurationPrefix);
					File confFile = new File (confPath);
					if (confFile.exists()){
						result.add(new IReportDescriptor(confFile, child.getName(), isPro));
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * Check if is possible to build a data adapter from an xml definition
	 * 
	 * @param className the value of the attribute connectionClass in the xml definition, used to search a creator
	 * for this definition
	 * @return true if there is a creator for the data adapter, false otherwise
	 */
	public static boolean hasAdapter(String className){
		if (importManager == null) createImportManager();
		return importManager.containsKey(className);
	}
	
	/**
	 * Build a data adapter from the iReport XML definition
	 * 
	 * @param xml the iReport xml definition
	 * @param className the value of the attribute connectionClass in the xml definition, used to search a creator
	 * for this definition
	 * @return a JSS data adapter, if there is a creator for the xml definition
	 */
	public static DataAdapterDescriptor getAdapter(Document xml, String className){
		if (importManager == null) createImportManager();
		IDataAdapterCreator creator = importManager.get(className);
		if (creator != null) return creator.buildFromXML(xml);
		return null;
	}
	
	/**
	 * Return an iReport configuration in a precise path 
	 * 
	 * @param path a path to the file ireport.properties, configuration file of iReport
	 * 
	 * @return a configuration for the specified installation of JSS or null if the path 
	 * is not of an ireport.properties files
	 * 
	 */
	public static IReportDescriptor GetDescriptor(String path){
		File propFile = new File(path);
		if (propFile.exists() && propFile.getName().equals("ireport.properties")){
			IReportDescriptor newDesc = new IReportDescriptor(propFile.getParentFile(), "", false);
			return newDesc;
		}
		return null;
	}
	
	/**
	 * Return a list of the available iReport configuration, searched in the default installation 
	 * directories
	 */
	public static List<IReportDescriptor> getIReportConfigurationFolder(){
		List<IReportDescriptor> result = new ArrayList<IReportDescriptor>();
		String iReportFolder = System.getProperty("user.home");
		String iReportProFolder = System.getProperty("user.home");
		OperativeSystem platform = GetPlatform();
		
		//Get the user path appropriate for the OS
		switch (platform){
		case MacOS: 
			iReportFolder = iReportFolder.concat("/Library/Application Support/ireport");
			iReportProFolder = iReportProFolder.concat("/Library/Application Support/ireportpro");
			break;
		case Windows:
			iReportFolder = iReportFolder.concat("\\.ireport");
			iReportProFolder = iReportProFolder.concat("\\.ireportpro");
			break;
		case Linux:
			iReportFolder = iReportFolder.concat("/.ireport");
			iReportProFolder = iReportProFolder.concat("/.ireportpro");
			break;
		}
		
		result.addAll(getVersions(iReportFolder,false));
		result.addAll(getVersions(iReportProFolder,true));
		
		return result;
	}
	
}
