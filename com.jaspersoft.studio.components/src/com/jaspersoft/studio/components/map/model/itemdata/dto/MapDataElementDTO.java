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
package com.jaspersoft.studio.components.map.model.itemdata.dto;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemData;
import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.util.JRCloneUtils;

import com.jaspersoft.studio.components.map.model.itemdata.ElementDataHelper;

/**
 * This DTO allows to wrap a list of {@link Item} elements, also spread across different {@link ItemData} objects,
 * but that share the same {@link ItemProperty} 'name'.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapDataElementDTO implements JRCloneable{

	private List<MapDataElementItemDTO> dataItems;
	private ItemProperty name;

	public List<MapDataElementItemDTO> getDataItems() {
		if(dataItems==null){
			dataItems=new ArrayList<MapDataElementItemDTO>();
		}
		return dataItems;
	}

	public void setDataItems(List<MapDataElementItemDTO> dataItems) {
		this.dataItems = dataItems;
	}

	public ItemProperty getName() {
		return name;
	}

	public void setName(ItemProperty name) {
		this.name = ElementDataHelper.fixElementNameProperty(name);
		for(MapDataElementItemDTO i : getDataItems()) {
			i.setParentName(this.name);
		}
	}
	
	@Override
	public Object clone() {
		MapDataElementDTO clone = new MapDataElementDTO();
		clone.setName((ItemProperty) name.clone());
		clone.setDataItems(JRCloneUtils.cloneList(getDataItems()));
		return clone;
	}
	
}
