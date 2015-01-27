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
package com.jaspersoft.studio.components.messages;

import java.util.ArrayList;
import java.util.List;

import com.jaspersoft.translation.resources.AbstractResourceDefinition;
import com.jaspersoft.translation.resources.PackageResourceDefinition;

/**
 * Publish the resource that can be translated through the translation plugin
 * 
 * @author Orlandin Marco
 *
 */
public class ResourcePublisher extends com.jaspersoft.studio.messages.ResourcePublisher{

	@Override
	public String getPluginName() {
		return "com.jaspersoft.studio.components";
	}

	protected List<AbstractResourceDefinition> initializeProperties(){
		List<AbstractResourceDefinition> result = new ArrayList<AbstractResourceDefinition>();
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.barcode.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the barcode elements",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/barcode/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.chart.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the chart elements included in the comunity version (JFreeChar)",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/chart/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.commonstyles.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the syles element (ie chart styles)",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/commonstyles/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.crosstab.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the crosstab elements",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/crosstab/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.list.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the list elements",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/list/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.map.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the map elements included in the comunity version",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/map/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.components.table.messages", 
												 "messages.properties", 
												 "In this file there are the strings used by the table elements",
												 getClassLoader(),
												 "com/jaspersoft/studio/components/table/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 null, 
												 "plugin.properties", 
												 "In this file there are few strings regarding all the extra components added to Jaspersoft Studio by this plugin",
												 getClassLoader(),
												 "plugin.properties", this));
		
		propertiesCache.put(getPluginName(), result);
		return result;
	}


}
