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
 * Contains the informations and resources of a translation of a single plugin
 * 
 * @author Orlandin Marco
 *
 */
public class TranslationInformation {
	
	/**
	 * The plugin name
	 */
	private String pluginName;
	
	/**
	 * A list of resources inside the plugin, that can be file or packages that 
	 * essentially contains the translated resources (typically the properties files)
	 */
	private List<ITranslationResource> resources;
	
	/**
	 * Create an instance of the class without resources
	 * 
	 * @param pluginName the name of the plugin that translated by the contained resources
	 */
	public TranslationInformation(String pluginName){
		this.pluginName = pluginName;
		this.resources = new ArrayList<ITranslationResource>();
	}
	
	/**
	 * Create an instance of the class
	 * 
	 * @param pluginName the name of the plugin that translated by the contained resources
	 * @param resources a not null list of resources
	 */
	public TranslationInformation(String pluginName, List<ITranslationResource> resources){
		this.pluginName = pluginName;
		this.resources = resources;
	}
	
	/**
	 * Add a new resource to the resources list
	 * 
	 * @param resource resource to add
	 */
	public void addResource(ITranslationResource resource){
		resources.add(resource);
	}
	
	/**
	 * Set the list of resources
	 * 
	 * @param resources a non null list of resources
	 */
	public void setResources(List<ITranslationResource> resources){
		this.resources = resources;
	}
	
	/**
	 * Return the list of the resources
	 * 
	 * @return list of the resources contained into the translation
	 */
	public List<ITranslationResource> getResources(){
		return resources;
	}
	
	/**
	 * Return the plugin name
	 * 
	 * @return the full name of the plugin
	 */
	public String getPluginName(){
		return pluginName;
	}
}
