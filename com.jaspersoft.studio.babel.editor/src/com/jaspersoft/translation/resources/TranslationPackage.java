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

import java.util.ArrayList;
import java.util.List;

/**
 * Translation resource that represent a package like folder. 
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationPackage implements ITranslationResource {

	/**
	 * Name of the package
	 */
	private String packageName;
	
	/**
	 * Path to the package
	 */
	private String packagePathFolder;
	
	/**
	 * List of children of the package
	 */
	private List<ITranslationResource> packageContent;
	
	/**
	 * Create an instance of the class
	 * 
	 * @param packagePath path to the package folder 
	 * @param packageName name of the package
	 */
	public TranslationPackage(String packagePath, String packageName){
		this.packageName = packageName;
		this.packagePathFolder = packagePath;
		packageContent = new ArrayList<ITranslationResource>();
	}
	
	/**
	 * Add a resource file to the package
	 * 
	 * @param newFile the new file resource
	 */
	public void addFile(TranslationFile newFile){
		packageContent.add(newFile);
	}
	
	/**
	 * Return the name of the package
	 */
	@Override
	public String getResourceName() {
		return packageName;
	}

	/**
	 * Return the path of the package resource
	 */
	@Override
	public String getResourcePath() {
		return packagePathFolder;
	}

	/**
	 * Return the content file of the package resource
	 */
	@Override
	public List<ITranslationResource> getChildren() {
		return packageContent;
	}

	/**
	 * Return always false since a package is not a folder
	 */
	@Override
	public boolean isFile() {
		return false;
	}

	
}
