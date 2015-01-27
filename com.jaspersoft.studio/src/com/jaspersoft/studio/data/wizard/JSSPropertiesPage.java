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
package com.jaspersoft.studio.data.wizard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.JRPropertiesUtil.PropertySuffix;

import com.jaspersoft.studio.data.adapter.IReportDescriptor;
import com.jaspersoft.studio.preferences.util.PreferencesUtils;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;

/**
 * Page of the wizard used to import the properties from a Jaspersoft Studio Workspace into
 * the actual one
 * 
 * @author Orlandin Marco
 *
 */
public class JSSPropertiesPage extends ShowPropertiesPage {
	
	protected JSSPropertiesPage() {
		super();
	}
	
	/**
	 * Return the value of a property
	 * 
	 * @param key key of the property
	 * @return value of the property
	 */
	public String getProperyValue(String key){
		return prop.getProperty(key);
	}
	
	/**
	 *  Return the keys of all the properties available for the import.
	 * The properties are deserialized from the string inside the configuration file. At this properties 
	 * are also added the default one if they are not specified
	 */
	@Override
	protected Collection<Object> getInFields(){
		List<Object> readKeys = new ArrayList<Object>();
		IReportDescriptor selectedConfig = ((ImportJSSAdapterWizard)getWizard()).getSelectedConfiguration();
		String propertyString = selectedConfig.getConfiguration().getProperty(PreferencesUtils.NET_SF_JASPERREPORTS_JRPROPERTIES);
		prop = PreferencesUtils.loadJasperReportsProperties(propertyString);
		Set<String> storedKeys = prop.stringPropertyNames();
		for(String key :storedKeys){
				readKeys.add(key);
		}
		//Add the default one
		List<PropertySuffix> lst = PropertiesHelper.DPROP.getProperties("");
		for (PropertySuffix ps : lst) {
			if (prop.getProperty(ps.getKey()) == null) {
				readKeys.add(ps.getKey());
				prop.setProperty(ps.getKey(), ps.getValue());
			}
		}
		return readKeys;
	}
}
