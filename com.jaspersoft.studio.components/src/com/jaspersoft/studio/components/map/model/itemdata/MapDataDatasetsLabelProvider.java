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
package com.jaspersoft.studio.components.map.model.itemdata;

import org.eclipse.jface.viewers.LabelProvider;

import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataDatasetDTO;

/**
 * Label Provider for a list of {@link MapDataDatasetDTO} elements.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapDataDatasetsLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		if(element instanceof MapDataDatasetDTO){
			return ((MapDataDatasetDTO) element).getName();
		}
		return super.getText(element);
	}
	
}
