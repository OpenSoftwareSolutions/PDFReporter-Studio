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
package com.jaspersoft.studio.data.excel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.sf.jasperreports.data.excel.ExcelDataAdapterImpl;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;

/**
 * Creator to build a JSS XLS data adapter from the xml definition of an iReport XLS 
 * data adapter
 * 
 * @author Orlandin Marco
 */
public class ExcelCreator implements IDataAdapterCreator {

	private class Column implements Comparable<Column>{
		
		private Object value;
		
		private int index;
		
		public Column(int index, Object value){
			this.index = index;
			this.value = value;
		}

		@Override
		public int compareTo(Column o) {
			return index - o.getIndex();
		}
		
		public int getIndex(){
			return index;
		}
		
		public Object getValue(){
			return value;
		}
	}
	
	
	@Override
	public DataAdapterDescriptor buildFromXML(Document docXML) {
		ExcelDataAdapterImpl result = new ExcelDataAdapterImpl();
		
		NamedNodeMap rootAttributes = docXML.getChildNodes().item(0).getAttributes();
		String connectionName = rootAttributes.getNamedItem("name").getTextContent();
		result.setName(connectionName);
		
		NodeList children = docXML.getChildNodes().item(0).getChildNodes();
		List<Column> colIndexes = new ArrayList<Column>();
		List<Column> colNames = new ArrayList<Column>();
		for(int i=0; i<children.getLength(); i++){
			Node node = children.item(i);
			if (node.getNodeName().equals("connectionParameter")){
				String paramName = node.getAttributes().getNamedItem("name").getTextContent();
				
				if (paramName.startsWith("INDEX_")){
					int index = Integer.parseInt(paramName.substring(paramName.lastIndexOf("_")+1));
					colIndexes.add(new Column(index, node.getTextContent()));
				}		
				
				if (paramName.startsWith("COLUMN_")){
					int index = Integer.parseInt(paramName.substring(paramName.lastIndexOf("_")+1));
					colNames.add(new Column(index, node.getTextContent()));
				}		

				if (paramName.equals("useFirstRowAsHeader")) result.setUseFirstRowAsHeader(node.getTextContent().equals("true"));	
				if (paramName.equals("customDateFormat")) result.setDatePattern(node.getTextContent());
				if (paramName.equals("customNumberFormat")) result.setNumberPattern(node.getTextContent());
				if (paramName.equals("Filename")) result.setFileName(node.getTextContent());
			}
		}
		
		Collections.sort(colNames);
		List<String> names = new ArrayList<String>();
		for(Column col : colNames)
			names.add(col.getValue().toString());
		result.setColumnNames(names);
		
		Collections.sort(colIndexes);
		List<Integer> indexes = new ArrayList<Integer>();
		for(Column col : colIndexes)
			indexes.add(Integer.parseInt(col.getValue().toString()));
		result.setColumnIndexes(indexes);
		
		ExcelDataAdapterDescriptor desc = new ExcelDataAdapterDescriptor();
		desc.setDataAdapter(result);
		return desc;
	}

	@Override
	public String getID() {
		return "com.jaspersoft.ireport.designer.connection.JRExcelDataSourceConnection";
	}
}
