/*******************************************************************************
 * Copyright (C) 2010 - 2014 Jaspersoft Corporation. All rights reserved.
 * http://www.jaspersoft.com
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, 
 * the following license terms apply:
 * 
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Jaspersoft Studio Team - initial API and implementation
 ******************************************************************************/
package org.eclipse.babel.messages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.jaspersoft.translation.resources.AbstractResourceDefinition;
import com.jaspersoft.translation.resources.IResourcesInput;
import com.jaspersoft.translation.resources.PackageResourceDefinition;

/**
 * Publish the resource that can be translated through the translation plugin
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ResourcePublisher extends IResourcesInput {

	protected static HashMap<String,List<AbstractResourceDefinition>> propertiesCache = new HashMap<String, List<AbstractResourceDefinition>>();
	
	@Override
	public String getPluginName() {
		return "com.jaspersoft.studio.babel.editor";
	}
	
	protected ClassLoader getClassLoader(){
		return this.getClass().getClassLoader();
	}
	
	protected List<AbstractResourceDefinition> initializeProperties(){
		List<AbstractResourceDefinition> result = new ArrayList<AbstractResourceDefinition>();
		result.add(new PackageResourceDefinition("en_EN", 
																						 "org.eclipse.babel.messages", 
																						 "messages.properties", 
																						 "In this file there are the standard strings used by the Resource Bundle plugin",
																						 getClassLoader(),
																						 "org/eclipse/babel/messages/messages.properties", this));
		result.add(new PackageResourceDefinition("en_EN", 
																						 null, 
																						 "plugin.properties", 
																						 "In this file there are the standard strings used by the Resource Bundle plugin",
																						 getClassLoader(),
																						 "plugin.properties", this));
		
		propertiesCache.put(getPluginName(), result);
		return result;
	}

	@Override
	public List<AbstractResourceDefinition> getResourcesElements() {
		List<AbstractResourceDefinition> result = propertiesCache.get(getPluginName());
		if (result == null) result = initializeProperties();
		return result;
	}

	@Override
	public String getContextId() {
		return null;
	}

}
