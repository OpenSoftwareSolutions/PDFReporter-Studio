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
package com.jaspersoft.studio.editor.jrexpressions.ui.messages;

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
		return "com.jaspersoft.studio.editor.jrexpressions.ui";
	}

	protected List<AbstractResourceDefinition> initializeProperties(){
		List<AbstractResourceDefinition> result = new ArrayList<AbstractResourceDefinition>();
		result.add(new PackageResourceDefinition("en_EN", 
												 "com.jaspersoft.studio.editor.jrexpressions.ui.messages", 
												 "messages.properties", 
												 "In this file there are the strings used inside the expression editor dialog",
												 getClassLoader(),
												 "com/jaspersoft/studio/editor/jrexpressions/ui/messages/messages.properties", this));
		
		result.add(new PackageResourceDefinition("en_EN", 
												 null, 
												 "plugin.properties", 
												 "In this file there are the strings used inside the expression editor dialog",
												 getClassLoader(),
												 "plugin.properties", this));
		
		propertiesCache.put(getPluginName(), result);
		return result;
	}


}
