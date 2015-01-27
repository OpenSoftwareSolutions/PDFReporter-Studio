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
package com.jaspersoft.studio.data.querydesigner.json;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;

import org.apache.commons.io.IOUtils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.jaspersoft.studio.data.designer.tree.ISelectableNodes;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.datasource.json.JsonSupportNode;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * This class works with the specified Json data information.
 * Usually this will be read from an input file or an existing string.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class JsonDataManager implements ISelectableNodes<JsonSupportNode> {
	private ObjectMapper mapper;
	private JsonNode jsonRoot;
	private MRoot jsonSupportModel;
	private Map<JsonSupportNode, JsonNode> jsonNodesMap;

	/**
	 * Tries to load a Json tree structure using the 
	 * specified stream as input source.
	 * 
	 * @param filename the name of the json file
	 * @throws IOException
	 */
	public void loadJsonDataFile(String filename) throws IOException{
		getJsonNodesMap().clear();
		InputStream ins = null;
		try {
			File f = new File(filename);
			if(f.exists()) {
				ins = new FileInputStream(f);
			}
			else {
				ins=new URL(filename).openStream();	
			}
			jsonRoot=getJsonMapper().readTree(ins);
			buildJsonSupportTree();
		} catch (IOException e) {
			throw e;
		} finally {
			IOUtils.closeQuietly(ins);
		}
	}
	
	/**
	 * Tries to load a Json tree structure using the 
	 * specified string content as input source.
	 * 
	 * @param jsonData the string containing json data
	 * @throws IOException 
	 * @throws JsonProcessingException 
	 */
	public void loadJsonDataString(String jsonData) throws JsonProcessingException, IOException{
		jsonRoot=getJsonMapper().readTree(jsonData);
		buildJsonSupportTree();
	}
	
	/*
	 * Returns the Json object mapper.
	 */
	private ObjectMapper getJsonMapper() {
		if(mapper==null){
			mapper = new ObjectMapper();
			mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
			mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
			mapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
		}
		return mapper;
	}
	
	/**
	 * @return the current json root node
	 */
	public JsonNode getJsonTreeRoot(){
		return jsonRoot;
	}
	
	/**
	 * @return the current support model root
	 */
	public MRoot getJsonSupportModel(){
		return jsonSupportModel;
	}
	
	/*
	 * Creates the support tree that uses ANodes.
	 */
	private void buildJsonSupportTree(){
		jsonSupportModel=new MRoot(null, null);
		List<JsonSupportNode> children=getChildrenJsonNodes(getJsonTreeRoot());
		for(JsonSupportNode c : children){
			c.setParent(jsonSupportModel, -1);
		}
	}

	/*
	 * Extract the children ANodes for a specified Json node.
	 */
	private List<JsonSupportNode> getChildrenJsonNodes(JsonNode jsonNode) {
		List<JsonSupportNode> children=new ArrayList<JsonSupportNode>();
		if(jsonNode.isArray() && jsonNode.equals(jsonRoot)){
			// Assumption: consider the first element as a template
			jsonNode=jsonNode.get(0);
		}
		Iterator<String> fieldNames = jsonNode.fieldNames();
		while(fieldNames.hasNext()){
			String name = fieldNames.next();
			JsonNode tmpNode = jsonNode.get(name);
			if(tmpNode.isObject()){
				JsonSupportNode child=new JsonSupportNode();
				child.setNodeText(name);
				List<JsonSupportNode> innerChildren=getChildrenJsonNodes(tmpNode);
				for(JsonSupportNode innerChild : innerChildren){
					innerChild.setParent(child, -1);
				}
				getJsonNodesMap().put(child, tmpNode);
				children.add(child);
			}
			else if(tmpNode.isArray()){
				Iterator<JsonNode> elements = tmpNode.elements();
				while(elements.hasNext()){
					JsonNode el=elements.next();
					JsonSupportNode child=new JsonSupportNode();
					child.setNodeText(name);
					List<JsonSupportNode> innerChildren=getChildrenJsonNodes(el);
					for(JsonSupportNode innerChild : innerChildren){
						innerChild.setParent(child, -1);
					}
					getJsonNodesMap().put(child, el);
					children.add(child);
				}
			}
			else if(tmpNode.isValueNode()){
				JsonSupportNode child=new JsonSupportNode();
				child.setNodeText(name);
				getJsonNodesMap().put(child, tmpNode);
				children.add(child);			
			}
		}
		return children;
	}

	/*
	 * (non-Javadoc)
	 * @see com.jaspersoft.studio.data.querydesigner.ISelectableNodes#getSelectableNodes(java.lang.String)
	 */
	public List<JsonSupportNode> getSelectableNodes(String query) {
		List<JsonSupportNode> selectedList=new ArrayList<JsonSupportNode>();
		JsonQueryHelper jsonQueryHelper = new JsonQueryHelper(mapper);
		try {
			JsonNode jsonData = jsonQueryHelper.getJsonData(jsonRoot, query);
			if(jsonData!=null){
				List<JsonNode> elementsList=new ArrayList<JsonNode>();
				if(jsonData.isArray()){
					Iterator<JsonNode> elements = jsonData.elements();
					while(elements.hasNext()){
						elementsList.add(elements.next());
					}	
				}
				else if(jsonData.isObject()){
					elementsList.add(jsonData);
				}
				
				for(JsonSupportNode sn : getJsonNodesMap().keySet()){
					if(elementsList.contains(getJsonNodesMap().get(sn))){
						selectedList.add(sn);
					}
				}
			}
		} catch (JRException e) {
			// Do not care about error in node selection
		}
		
		return selectedList;
	}
	
	/**
	 * Given a JSON selection query, extracts from the result set 
	 * the list of possible fields that can be used in a report.
	 * 
	 * @param query the JSON query text
	 * @return a list of fields
	 */
	public List<JRDesignField> extractFields(String query){
		JsonQueryHelper jsonQueryHelper=new JsonQueryHelper(mapper);
		try{
			JsonNode jsonData = jsonQueryHelper.getJsonData(jsonRoot, query);
			if(jsonData!=null){
				if(jsonData.isArray()){
					return getFieldsFromArrayNode((ArrayNode)jsonData);
				}
				else if(jsonData.isObject()){
					return getFieldsFromObjectNode((ObjectNode)jsonData);
				}
			}
		}catch (JRException e){
			// Do not care about error in node selection
		}
		return new ArrayList<JRDesignField>();
	}
	
	/*
	 * Gets the fields from a JSON node of type object.
	 */
	private List<JRDesignField> getFieldsFromObjectNode(ObjectNode node){
		List<JRDesignField> fields = new ArrayList<JRDesignField>();
		Iterator<String> fieldNames = node.fieldNames();
		while(fieldNames.hasNext()){
			String name = fieldNames.next();
			JRDesignField f=new JRDesignField();
			f.setName(ModelUtils.getNameForField(fields, name));
			f.setDescription(name);
			f.setValueClass(String.class);
			fields.add(f);		
		}
		return fields;
	}
	
	/*
	 * Gets the fields from a JSON node of type array.
	 */
	private List<JRDesignField> getFieldsFromArrayNode(ArrayNode node){
		// Assumption: consider the first element as template 
		JsonNode firstEl = node.get(0);
		if(firstEl instanceof ObjectNode){
			return getFieldsFromObjectNode((ObjectNode)firstEl);
		}
		else if (firstEl instanceof ArrayNode){
			return getFieldsFromArrayNode((ArrayNode)firstEl);
		}
		return new ArrayList<JRDesignField>();
	}

	public Map<JsonSupportNode, JsonNode> getJsonNodesMap() {
		if(this.jsonNodesMap==null){
			this.jsonNodesMap=new HashMap<JsonSupportNode, JsonNode>();
		}
		return jsonNodesMap;
	}
	
	public String getQueryExpression(String existingQuery, JsonSupportNode selectedNode){
		String absoluteQuery=getAbsoluteQueryExpression(selectedNode);
		if(existingQuery!=null && absoluteQuery.startsWith(existingQuery)
				&& absoluteQuery.length()>existingQuery.length()){
			// consider also an additional . selector
			int qLength = existingQuery.length();
			return absoluteQuery.substring(qLength+Math.min(qLength, 1));
		}
		return absoluteQuery;
	}
	
	private String getAbsoluteQueryExpression(JsonSupportNode selectedNode){
		StringBuffer queryBuff=new StringBuffer();
		queryBuff.insert(0, selectedNode.getNodeText());
		JsonSupportNode tmpNode=selectedNode;
		while(tmpNode.getParent()!=null && !(tmpNode.getParent() instanceof MRoot)){
			tmpNode=(JsonSupportNode) tmpNode.getParent();
			queryBuff.insert(0, tmpNode.getNodeText()+".");
		}
		return queryBuff.toString();
	}
}
