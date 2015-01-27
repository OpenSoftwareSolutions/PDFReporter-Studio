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
package com.jaspersoft.studio.components.map.model.path;

import net.sf.jasperreports.components.map.ItemProperty;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.ElementDataHelper;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementDTO;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementItemDTO;

/**
 * Label Provider for a viewer exposing the Path Datas of the map.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class PathsLabelProvider extends LabelProvider {
	private static final String LATITUDE_ATTRIBUTE = "latitude"; //$NON-NLS-1$
	private static final String LONGITUDE_ATTRIBUTE = "longitude"; //$NON-NLS-1$
	
	@Override
	public String getText(Object element) {
		if(element instanceof MapDataElementDTO) {
			ItemProperty name = ((MapDataElementDTO) element).getName();
			if(name!=null){
				return Messages.PathsLabelProvider_PathPrefix + ElementDataHelper.getItemPropertyValueAsString(name);
			}
			else {
				return Messages.PathsLabelProvider_PathPrefix + ElementDataHelper.DEFAULT_ELEMENT_NAME;
			}
		}
		else if(element instanceof MapDataElementItemDTO) {
			// Extract coordinates: latitude and longitude are mandatory attributes
			String lat=ElementDataHelper.getItemPropertyValueAsString(((MapDataElementItemDTO) element).getItem(), LATITUDE_ATTRIBUTE);
			String lon=ElementDataHelper.getItemPropertyValueAsString(((MapDataElementItemDTO) element).getItem(), LONGITUDE_ATTRIBUTE);
			return NLS.bind(Messages.PathsLabelProvider_ItemAt,lat,lon);
		}
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if(element instanceof MapDataElementDTO){
			return Activator.getDefault().getImage("/icons/path-icon-16.png"); //$NON-NLS-1$
		}
		else if(element instanceof MapDataElementItemDTO){
			return Activator.getDefault().getImage("/icons/pathitem-icon-16.png"); //$NON-NLS-1$
		}
		return super.getImage(element);
	}
}
