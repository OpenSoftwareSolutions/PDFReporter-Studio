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

import java.io.InputStream;
import java.util.Locale;

/**
 * This class define a file resource that can recovered from the translation editor
 * and easily localized by the user
 * 
 * @author Orlandin Marco
 *
 */
public class PackageResourceDefinition extends AbstractResourceDefinition{

	/**
	 * The locale of the resource file
	 */
	private String locale;
	
	/**
	 * The package where the resource file is placed
	 */
	private String packageName;
	
	/**
	 * The name of the resource file
	 */
	private String fileName;
	
	/**
	 * A description of the resource file
	 */
	private String description;
	
	/**
	 * Loader used to read the file from the filesystem
	 */
	private ClassLoader propertiesLoader;
	
	/**
	 * the path of the file
	 */
	private String filePath;
	
	/**
	 * Container of the resource file
	 */
	private IResourcesInput parent;
	
	/**
	 * Create an instance of the class representing a resource file, 
	 * the loader used to read the file is Messages.class.getClassLoader()
	 * 
	 * @param locale The locale of the resource file
	 * @param packageName The package where the resource file is placed
	 * @param fileName The name of the resource file
	 * @param description A description of the resource file
	 * @param filePath the path of the file
	 * @param parent Container of the resource file
	 */
	public PackageResourceDefinition(String locale, String packageName, String fileName, String description, String filePath, IResourcesInput parent){
		this.locale = locale;
		this.packageName = packageName;
		this.fileName = fileName;
		this.description = description;
		this.propertiesLoader = this.getClass().getClassLoader();
		this.parent = parent;
		this.filePath = filePath;
	}
	
	/**
	 * Create an instance of the class representing a resource file, 
	 * using a provided class loader
	 * 
	 * @param locale The locale of the resource file
	 * @param packageName The package where the resource file is placed
	 * @param fileName The name of the resource file
	 * @param description A description of the resource file
	 * @param loader the class loader used to read the file from the FS
	 * @param filePath the path of the file
	 * @param parent Container of the resource file
	 */
	public PackageResourceDefinition(String locale, String packageName, String fileName, String description, ClassLoader loader, String filePath, IResourcesInput parent){
		this.locale = locale;
		this.packageName = packageName;
		this.fileName = fileName;
		this.description = description;
		this.propertiesLoader = loader;
		this.parent = parent;
		this.filePath = filePath;
	}
	
	/**
	 * Return the locale of the handled properties file
	 * 
	 * @return a not null string representing the locale of the file
	 */
	@Override
	public String getLocale() {
		return locale;
	}
	
	/**
	 * Return the path of the handled properties file
	 * 
	 * @return a not null string representing the path of the file
	 */
	public String getPath(){
		return filePath;
	}

	/**
	 * Return the package where the file is placed
	 * 
	 * @return a text representing the name of the package where
	 * the properties file is placed or null if the file is not inside
	 * a package
	 */
	@Override
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Return the filename with it's extension
	 * 
	 * @return the name of the file with it's extension, must be not null
	 */
	@Override
	public String getFileName() {
		return fileName;
	}
	
	/**
	 * Return the description of the file
	 * 
	 * @return a not null textual description
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * Return an inputstram to the properties source file. The input is created 
	 * by the class loader with the provided path
	 * 
	 * @return a not null inputstream from where the pairs
	 * key\value are read
	 */
	@Override
	protected InputStream getFileInput() {
		return propertiesLoader.getResourceAsStream(filePath);
	}
	
	protected InputStream getLocalizedInput(Locale loc){
		String locName = new String(fileName);
		if (locName.endsWith(".properties")){
			locName = locName.substring(0,locName.length()-11);
			for(Locale searchedLoc : Locale.getAvailableLocales()){
				if (searchedLoc.getLanguage().equals(loc.getLanguage())){
					String newPath = filePath.substring(0,filePath.length()-fileName.length());
					InputStream stream = propertiesLoader.getResourceAsStream(newPath + locName + "_" + searchedLoc.toString() + ".properties");
					if (stream != null) return stream;
				}
			}
		} 
		return null;
	}
	
	/**
	 * Return the plugin name where the file is placed
	 * 
	 * @return a text representing the name of the plugin where
	 * the properties file is placed, must be not null
	 * 
	 */
	@Override
	public String getPluginName() {
		return parent.getPluginName();
	}
	
	/**
	 * Improve the equals method by checking also the path
	 */
	@Override
	public boolean equals(Object obj) {
		PackageResourceDefinition resource = (PackageResourceDefinition)obj;
		return safeEquals(getPath(), resource.getPath()) && super.equals(obj);
	}
	

}
