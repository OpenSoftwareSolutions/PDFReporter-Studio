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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.map.ItemData;
import net.sf.jasperreports.engine.JRCloneable;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.util.JRCloneUtils;

/**
 * This DTO allows to wrap a list of {@link ItemData} elements.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapDataElementsConfiguration implements JRCloneable {

	private List<MapDataDatasetDTO> datasets;
	private List<MapDataElementDTO> elements;
	private String elementLabel;

	public MapDataElementsConfiguration(String elementLabel) {
		this.elementLabel = elementLabel;
	}

	public List<MapDataDatasetDTO> getDatasets() {
		if(datasets==null){
			datasets=new ArrayList<MapDataDatasetDTO>();
		}
		return datasets;
	}

	public void setDatasets(List<MapDataDatasetDTO> datasets) {
		this.datasets = datasets;
	}

	public List<MapDataElementDTO> getElements() {
		if(elements==null){
			elements=new ArrayList<MapDataElementDTO>();
		}
		return elements;
	}

	public void setElements(List<MapDataElementDTO> elements) {
		this.elements = elements;
	}

	public List<String> getAllDatasetNames() {
		List<String> names = new ArrayList<String>();
		for(MapDataDatasetDTO d : datasets) {
			names.add(d.getName());
		}
		return names;
	}
	
	public Map<String,String> getElementDatasetsMap() {
		Map<String,String> map = new HashMap<String, String>();
		for(MapDataDatasetDTO d : datasets) {
			JRDatasetRun dsRun = d.getDataset().getDatasetRun();
			String designDSname = ""; //$NON-NLS-1$
			if(dsRun!=null){
				designDSname = dsRun.getDatasetName();
			}
			map.put(d.getName(), designDSname);
		}
		return map;
	}

	@Override
	public Object clone(){
		MapDataElementsConfiguration clone = new MapDataElementsConfiguration(getElementLabel());
		clone.setDatasets(JRCloneUtils.cloneList(getDatasets()));
		clone.setElements(JRCloneUtils.cloneList(getElements()));
		return clone;
	}

	public String getElementLabel() {
		return this.elementLabel;
	}
}
