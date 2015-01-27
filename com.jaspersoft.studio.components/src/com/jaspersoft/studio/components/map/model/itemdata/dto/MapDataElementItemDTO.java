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

import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.StandardItem;
import net.sf.jasperreports.engine.JRCloneable;

import com.jaspersoft.studio.components.map.model.itemdata.ElementDataHelper;

/**
 * This DTO allows to wrap an {@link Item} element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapDataElementItemDTO implements JRCloneable{

	private boolean isStatic;
	private ItemProperty parentName;
	private String datasetName;
	private Item item;

	public MapDataElementItemDTO(ItemProperty parentName) {
		setParentName(parentName);
		setStatic(true);
	}

	public boolean isStatic() {
		return isStatic;
	}

	public void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
		if(isStatic) {
			this.datasetName = null;
		}
	}

	public ItemProperty getParentName() {
		return parentName;
	}

	public void setParentName(ItemProperty parentName) {
		this.parentName = ElementDataHelper.fixElementNameProperty(parentName);
		if(this.item!=null) {
			ItemProperty pname = ElementDataHelper.getItemProperty(this.item, "name"); //$NON-NLS-1$
			if(pname!=null) {
				((StandardItem)this.item).removeItemProperty(pname);
			}
			if(this.parentName != null){
				((StandardItem)this.item).addItemProperty(this.parentName);	
			}
		}
	}

	public String getDatasetName() {
		return datasetName;
	}

	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
		this.isStatic = (datasetName == null);
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	@Override
	public Object clone() {
		MapDataElementItemDTO cloneObj = new MapDataElementItemDTO(this.parentName);
		cloneObj.setDatasetName(this.datasetName);
		cloneObj.setItem((Item)this.item.clone());
		return cloneObj;
	}
}
