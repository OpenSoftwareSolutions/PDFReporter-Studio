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
package com.jaspersoft.studio.components.map.model.marker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.components.map.MapComponent;

/**
 * Simple enum that allows to distinguish different kind of marker coordinates.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public enum MarkerCoordinatesType {
	LATITUDE_LONGITUDE(Arrays.asList(MapComponent.PROPERTY_latitude,MapComponent.PROPERTY_longitude)),
	XY(Arrays.asList("x","y")); //$NON-NLS-1$ //$NON-NLS-2$

	private List<String> mandatoryProperties;
	
	private MarkerCoordinatesType(List<String> properties){
		this.mandatoryProperties = new ArrayList<String>(properties.size());
		this.mandatoryProperties.addAll(properties);
	}
	
	public List<String> getMandatoryProperties(){
		return this.mandatoryProperties;
	}
	
	public boolean isMandatoryProperty(String propertyName){
		return this.mandatoryProperties.contains(propertyName);
	}
}
