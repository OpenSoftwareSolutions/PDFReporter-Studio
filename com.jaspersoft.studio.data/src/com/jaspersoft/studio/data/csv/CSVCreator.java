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
package com.jaspersoft.studio.data.csv;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.data.csv.CsvDataAdapterImpl;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;

/**
 * Creator to build a JSS CSV data adapter from the xml definition of an iReport CSV 
 * data adapter
 * 
 * @author Orlandin Marco
 */
public class CSVCreator implements IDataAdapterCreator {

	private class ColumnName implements Comparable<ColumnName>{
		
		private String name;
		
		private int index;
		
		public ColumnName(int index, String name){
			this.index = index;
			this.name = name;
		}

		@Override
		public int compareTo(ColumnName o) {
			return index - o.getIndex();
		}
		
		public int getIndex(){
			return index;
		}
		
		public String getName(){
			return name;
		}
	}
	
	@Override
	public DataAdapterDescriptor buildFromXML(Document docXML) {
		CsvDataAdapterImpl result = new CsvDataAdapterImpl();
		
		NamedNodeMap rootAttributes = docXML.getChildNodes().item(0).getAttributes();
		String connectionName = rootAttributes.getNamedItem("name").getTextContent();
		result.setName(connectionName);
		
		NodeList children = docXML.getChildNodes().item(0).getChildNodes();
		List<ColumnName> columnNames = new ArrayList<ColumnName>();

		for(int i=0; i<children.getLength(); i++){
			Node node = children.item(i);
			if (node.getNodeName().equals("connectionParameter")){
				String paramName = node.getAttributes().getNamedItem("name").getTextContent();
	
				if (paramName.startsWith("COLUMN_")){
					int index = Integer.parseInt(paramName.substring(paramName.lastIndexOf("_")+1));
					columnNames.add(new ColumnName(index, node.getTextContent()));
				}			
				if (paramName.equals("fieldDelimiter")) result.setFieldDelimiter(node.getTextContent());
				if (paramName.equals("queryExecuterMode")) result.setQueryExecuterMode(node.getTextContent().equals("true"));
				if (paramName.equals("useFirstRowAsHeader")) result.setUseFirstRowAsHeader(node.getTextContent().equals("true"));
				if (paramName.equals("customDateFormat")) result.setDatePattern(node.getTextContent());
				if (paramName.equals("Filename")) result.setFileName(node.getTextContent());
				if (paramName.equals("recordDelimiter")) result.setRecordDelimiter(node.getTextContent());
				
			}
		}
		
		Collections.sort(columnNames);
		List<String> names = new ArrayList<String>();
		for(ColumnName col : columnNames)
			names.add(col.getName());
		result.setColumnNames(names);
		CSVDataAdapterDescriptor desc = new CSVDataAdapterDescriptor();
		desc.setDataAdapter(result);
		return desc;
	}

	@Override
	public String getID() {
		return "com.jaspersoft.ireport.designer.connection.JRCSVDataSourceConnection";
	}

}
