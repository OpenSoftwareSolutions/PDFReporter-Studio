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

import net.sf.jasperreports.components.map.ItemData;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRElementDataset;

/**
 * This DTO allows to wrap the dataset that can be used by an {@link ItemData} element.
 * 
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapDataDatasetDTO implements JRCloneable{

	private JRElementDataset dataset;
	private String name;

	public JRElementDataset getDataset() {
		return dataset;
	}

	public void setDataset(JRElementDataset dataset) {
		this.dataset = dataset;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Object clone() {
		MapDataDatasetDTO clone = new MapDataDatasetDTO();
		clone.setName(getName());
		clone.setDataset((JRElementDataset) getDataset().clone());
		return clone;
	}
}
