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
package com.jaspersoft.studio.editor.toolitems;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IConfigurationElement;


public class ToolItemsSet {
	private String id;
	private String name;
	private String description;
	private boolean visibility;
	private List<IConfigurationElement> controlsConfigurationElements = new ArrayList<IConfigurationElement>();
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public boolean isVisible() {
		return visibility;
	}

	public void setVisible(boolean visibility) {
		this.visibility = visibility;
	}
	
	public List<IConfigurationElement> getControlsConfiguration(){
		return controlsConfigurationElements;
	}
	
	public void addControlConfiguration(IConfigurationElement controlDefinition){
		controlsConfigurationElements.add(controlDefinition);
	}
}
