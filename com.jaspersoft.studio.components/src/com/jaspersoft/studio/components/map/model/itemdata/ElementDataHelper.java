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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.map.Item;
import net.sf.jasperreports.components.map.ItemData;
import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.StandardItemData;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRExpression;

import org.eclipse.osgi.util.NLS;

import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataDatasetDTO;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementDTO;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementItemDTO;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementsConfiguration;

/**
 * Utility class that allows to work with a list of {@link ItemData} elements, and related classes, DTOs included.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ElementDataHelper {
	
	public static final String ELEMENT_DATASET_PREFIX = "{0} Dataset "; //$NON-NLS-1$
	public static final String DEFAULT_ELEMENT_NAME = "<default>"; //$NON-NLS-1$

	private ElementDataHelper(){
		// prevent instantiation...
	}
	
	public static MapDataElementsConfiguration convertFromElementDataInformation(List<ItemData> elementDataElements, String elementLabel){
		MapDataElementsConfiguration config = new MapDataElementsConfiguration(elementLabel);
		List<MapDataDatasetDTO> datasetDTOs=new ArrayList<MapDataDatasetDTO>();
		List<MapDataElementDTO> elementsDTOs = new ArrayList<MapDataElementDTO>();
		
		int datasetsCounter = 1;
		for (ItemData elementData : elementDataElements){
			// Configure Dataset
			JRElementDataset dataset = elementData.getDataset();
			String dsNameForDTO = null;
			if(dataset!=null) {
				Object dsClone = dataset.clone();
				MapDataDatasetDTO dsDTO = new MapDataDatasetDTO();
				dsDTO.setDataset((JRElementDataset) dsClone);
				dsNameForDTO = NLS.bind(ELEMENT_DATASET_PREFIX,elementLabel) + datasetsCounter;
				dsDTO.setName(dsNameForDTO);
				datasetDTOs.add(dsDTO);
				datasetsCounter++;
			}
			
			// Extract items DTO
			for(Item elementItem : elementData.getItems()) {
				ItemProperty elementName = getElementNameForItem(elementItem);
				MapDataElementItemDTO itemDTO = new MapDataElementItemDTO(elementName);
				itemDTO.setDatasetName(dsNameForDTO);
				itemDTO.setItem((Item) elementItem.clone());
				itemDTO.setStatic(dataset == null);
				createOrUpdateElementsList(elementsDTOs, elementName, itemDTO);
			}
		}
		config.setDatasets(datasetDTOs);
		config.setElements(elementsDTOs);
		return config;
	}
	
	private static void createOrUpdateElementsList(List<MapDataElementDTO> elementsDTOs,
			ItemProperty elementName, MapDataElementItemDTO itemDTO) {
		MapDataElementDTO elementFound = null;
		for (MapDataElementDTO element : elementsDTOs) {
			if(areNamesEqual(element.getName(),elementName)){
				elementFound = element;
				break;
			}
		}
		if(elementFound==null){
			elementFound = new MapDataElementDTO();
			elementFound.setName(elementName);
			elementsDTOs.add(elementFound);
		}
		elementFound.getDataItems().add(itemDTO);
	}

	private static ItemProperty getElementNameForItem(Item item) {
		ItemProperty nameProperty = getItemProperty(item, "name"); //$NON-NLS-1$
		return nameProperty!=null ? (ItemProperty) nameProperty.clone() : null;
	}
	
	// TODO - could be refactored to a generic utility class
	public static String getItemPropertyValueAsString(ItemProperty property) {
		return getItemPropertyValueAsString(property, false);
	}
	
	// TODO - could be refactored to a generic utility class
	public static String getItemPropertyValueAsString(ItemProperty property, boolean addDoubleQuotes) {
		if(property!=null) {
			String propertyValue=property.getValue();
			if(propertyValue == null && property.getValueExpression()!=null) {
				propertyValue = property.getValueExpression().getText();
			}
			else {
				if(addDoubleQuotes){
					propertyValue = "\""+propertyValue+"\""; //$NON-NLS-1$ //$NON-NLS-2$
				}
			}
			return propertyValue;
		}
		return null;
	}

	// TODO - could be refactored to a generic utility class
	public static String getItemPropertyValueAsString(Item item, String propertyName){
		return getItemPropertyValueAsString(item, propertyName, false);
	}
		
	// TODO - could be refactored to a generic utility class
	public static String getItemPropertyValueAsString(Item item, String propertyName, boolean addDoubleQuotes){
		return getItemPropertyValueAsString(getItemProperty(item, propertyName),addDoubleQuotes);
	}
	
	// TODO - could be refactored to a generic utility class
	public static ItemProperty getItemProperty(Item item, String propertyName){
		for(ItemProperty p : item.getProperties()) {
			if(propertyName.equals(p.getName())){
				return p;
			}
		}
		return null;
	}
	
	public static List<ItemData> convertToElementDataInformation(MapDataElementsConfiguration config) {
		List<ItemData> elementDatas = new ArrayList<ItemData>();
		List<MapDataElementItemDTO> allItems = new ArrayList<MapDataElementItemDTO>();
		for(MapDataElementDTO el : config.getElements()) {
			allItems.addAll(el.getDataItems());			
		}

		// We will create an ItemData for each Dataset 
		// There could also be an ItemData in case no dataset 
		// is used by one of the items. In order to do so
		// we will go through the list of MapDataElementItemDTO
		List<MapDataDatasetDTO> remainingDS = new ArrayList<MapDataDatasetDTO>();
		remainingDS.addAll(config.getDatasets());
		for(MapDataElementItemDTO item : allItems) {
			MapDataDatasetDTO usedDSDTO = createOrUpdateElementDataList(item, elementDatas, config.getDatasets());
			if(usedDSDTO!=null){
				remainingDS.remove(usedDSDTO);
			}
		}
		// Add "empty" ItemData as much as remaining datasets
		for(MapDataDatasetDTO dsDTO : remainingDS){
			StandardItemData newElementData = new StandardItemData();
			newElementData.setDataset(dsDTO.getDataset());
			elementDatas.add(newElementData);
		}
		
		return elementDatas;
	}

	private static MapDataDatasetDTO createOrUpdateElementDataList(
			MapDataElementItemDTO item,	List<ItemData> itemDatas, List<MapDataDatasetDTO> datasetDTOs) {
		String datasetName = item.getDatasetName();
		JRElementDataset ds = null;
		MapDataDatasetDTO usedDSDTO = null;
		if(datasetName != null) {
			for(MapDataDatasetDTO d : datasetDTOs) {
				if(d.getName().equals(datasetName))
				ds = d.getDataset();
				usedDSDTO = d;
				break;
			}
		}
		
		// find the correct itemdata
		ItemData foundItemData = null;
		for(ItemData itemData : itemDatas) {
			JRElementDataset currDS = itemData.getDataset();
			if((ds!=null && ds.equals(currDS)) ||
					(ds == null && currDS == null)) {
				foundItemData = itemData;
			}
		}
		if(foundItemData!=null) {
			((StandardItemData)foundItemData).addItem(item.getItem());
		}
		else {
			// create
			StandardItemData newItemData = new StandardItemData();
			newItemData.setDataset(ds);
			newItemData.addItem(item.getItem());
			itemDatas.add(newItemData);
		}
		return usedDSDTO;
	}
	
	public static void fixDatasetNames(MapDataElementsConfiguration config){
		int counter=1;
		for(MapDataDatasetDTO ds : config.getDatasets()) {
			ds.setName(NLS.bind(ELEMENT_DATASET_PREFIX,config.getElementLabel()) + counter);
			counter++;
		}
	}

	public static void fixElementNameForChildren(MapDataElementDTO element) {
		for(MapDataElementItemDTO item : element.getDataItems()) {
			item.setParentName((ItemProperty) element.getName().clone());
		}
	}
	
	public static void removeElementDataItem(
			MapDataElementsConfiguration mapElementsConfig, ItemProperty parentName,
			MapDataElementItemDTO item) {
		for(MapDataElementDTO el : mapElementsConfig.getElements()){
			if(areNamesEqual(parentName,el.getName())){
				el.getDataItems().remove(item);
				return;
			}
		}
	}

	public static void moveDownDataItem(
			MapDataElementsConfiguration mapElementsConfig, ItemProperty parentName,
			MapDataElementItemDTO item) {
		MapDataElementDTO foundEl = null;
		for(MapDataElementDTO el : mapElementsConfig.getElements()){
			if(areNamesEqual(parentName,el.getName())){
				foundEl=el;
				break;
			}
		}
		if(foundEl!=null){
			int currItemIndex = foundEl.getDataItems().indexOf(item);
			int numItems = foundEl.getDataItems().size();
			if(currItemIndex!=-1 && currItemIndex<numItems-1) {
				foundEl.getDataItems().remove(item);
				foundEl.getDataItems().add(currItemIndex+1, item);
			}
		}
	}
	
	public static void moveUpDataItem(
			MapDataElementsConfiguration mapElementsConfig, ItemProperty parentName,
			MapDataElementItemDTO item) {
		MapDataElementDTO foundEl = null;
		for(MapDataElementDTO el : mapElementsConfig.getElements()){
			if(areNamesEqual(parentName,el.getName())){
				foundEl=el;
				break;
			}
		}
		if(foundEl!=null){
			int currItemIndex = foundEl.getDataItems().indexOf(item);
			if(currItemIndex!=-1 && currItemIndex>0) {
				foundEl.getDataItems().remove(item);
				foundEl.getDataItems().add(currItemIndex-1, item);
			}
		}
	}

	public static void updateElementDataItem(
			MapDataElementsConfiguration mapElementsConfig,
			ItemProperty pname, MapDataElementItemDTO oldDTO, MapDataElementItemDTO newDTO) {
		for(MapDataElementDTO el : mapElementsConfig.getElements()){
			if(areNamesEqual(pname,el.getName())){
				int indexOf = el.getDataItems().indexOf(oldDTO);
				el.getDataItems().remove(indexOf);
				el.getDataItems().add(indexOf, newDTO);				
				return;
			}
		}
	}
	
	public static boolean areNamesEqual(ItemProperty name1, ItemProperty name2) {
		ItemProperty name1fixed = fixElementNameProperty(name1);
		ItemProperty name2fixed = fixElementNameProperty(name2);
		String name1Txt = ""; //$NON-NLS-1$
		if(name1fixed!=null){
			name1Txt=getItemPropertyValueAsString(name1fixed, true);
		}
		String name2Txt = ""; //$NON-NLS-1$
		if(name2fixed!=null){
			name2Txt=getItemPropertyValueAsString(name2fixed, true);
		}
		return name1Txt.equals(name2Txt);
	}

	public static ItemProperty fixElementNameProperty(ItemProperty pname) {
		if (pname == null) {
			return null;
		} else {
			String value = pname.getValue();
			JRExpression valueExpression = pname.getValueExpression();
			if ((value == null || value.isEmpty())
					&& (valueExpression == null
							|| valueExpression.getText() == null || valueExpression
							.getText().isEmpty())) {
				return null;
			} else {
				return pname;
			}
		}
	}
}
