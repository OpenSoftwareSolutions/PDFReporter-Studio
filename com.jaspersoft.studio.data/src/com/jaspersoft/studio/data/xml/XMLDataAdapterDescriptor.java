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
package com.jaspersoft.studio.data.xml;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.xml.XmlDataAdapterImpl;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuter;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuterFactory;
import net.sf.jasperreports.engine.util.xml.JRXPathExecuterUtils;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.studio.data.AWizardDataEditorComposite;
import com.jaspersoft.studio.data.Activator;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterEditor;
import com.jaspersoft.studio.data.IWizardDataEditorProvider;
import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.XMLUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class XMLDataAdapterDescriptor extends DataAdapterDescriptor implements IFieldsProvider, IWizardDataEditorProvider {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private boolean recursiveFind;

	@Override
	public XmlDataAdapterImpl getDataAdapter() {
		if (dataAdapter == null)
			dataAdapter = new XmlDataAdapterImpl();
		return (XmlDataAdapterImpl) dataAdapter;
	}

	@Override
	public DataAdapterEditor getEditor() {
		return new XMLDataAdapterEditor();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.DataAdapterFactory#getIcon(int)
	 */
	@Override
	public Image getIcon(int size) {
		// TODO Auto-generated method stub
		if (size == 16) {
			return Activator.getDefault().getImage("icons/blue-document-code.png"); //$NON-NLS-1$
		}
		return null;
	}

	@Override
	public boolean supportsGetFieldsOperation(JasperReportsConfiguration jConfig) {
		return true;
	}

	@Override
	public List<JRDesignField> getFields(DataAdapterService con, JasperReportsConfiguration jConfig, JRDataset jDataset) throws JRException, UnsupportedOperationException {
		setRecursiveRetrieval(jConfig);
		ArrayList<JRDesignField> fields = new ArrayList<JRDesignField>();
		String fileName = getDataAdapter().getFileName();
		File in = new File(fileName);
		Document doc = JRXmlUtils.parse(in, XMLUtils.isNamespaceAware(getDataAdapter(), jConfig.getJasperDesign()));
		fields.addAll(getFieldsFromDocument(doc, jConfig, jDataset));
		return fields;
	}

	/**
	 * Returns the list of fields provided by an XML document and the related
	 * query.
	 * 
	 * @param doc
	 *          the W3C XML document
	 * @param jConfig
	 *          the JasperReports configuration instance
	 * @param jDataset
	 *          the current dataset
	 * @return the list of fields
	 * @throws JRException
	 */
	protected List<JRDesignField> getFieldsFromDocument(Document doc, JasperReportsConfiguration jConfig, JRDataset jDataset) throws JRException {
		JRXPathExecuterFactory xPathExecuterFactory = JRXPathExecuterUtils.getXPathExecuterFactory(jConfig);
		JRXPathExecuter xPathExecuter = xPathExecuterFactory.getXPathExecuter();
		NodeList nodes = xPathExecuter.selectNodeList(doc, jDataset.getQuery().getText());
		LinkedHashMap<String, JRDesignField> fieldsMap = new LinkedHashMap<String, JRDesignField>();
		for (int nIdx = 0; nIdx < nodes.getLength(); nIdx++) {
			Node currNode = nodes.item(nIdx);
			findDirectChildrenAttributes(currNode,fieldsMap,"");
			if(currNode.getNodeType() == Node.ELEMENT_NODE) {
				NodeList childNodes = currNode.getChildNodes();
				findChildFields(childNodes, fieldsMap,"");
			}
		}
		return new ArrayList<JRDesignField>(fieldsMap.values());
	}
	
	/*
	 * Finds and adds a possible list of attributes for the specified node.
	 */
	private void findDirectChildrenAttributes(Node node,LinkedHashMap<String, JRDesignField> fieldsMap,String prefix) {
		NamedNodeMap attributes = node.getAttributes();
		for(int i=0;i<attributes.getLength();i++){
			Node item = attributes.item(i);
			if(item.getNodeType()==Node.ATTRIBUTE_NODE){
				addNewField(item.getNodeName(), fieldsMap, item, prefix);
			}
		}
	}

	/*
	 * Finds and adds a possible list of children nodes for the specified node.
	 */
	private void findChildFields(NodeList nodes, LinkedHashMap<String, JRDesignField> fieldsMap,String prefix) {
		if(nodes!=null) {
			List<String> childrenNames = new ArrayList<String>(); // temp list to avoid duplicates at the same level
			for (int i = 0; i < nodes.getLength(); i++) {
				Node item = nodes.item(i);
				String nodeName = item.getNodeName();
				if((item.getNodeType() == Node.ELEMENT_NODE || item.getNodeType() == Node.ATTRIBUTE_NODE) && 
						!childrenNames.contains(nodeName)) {
					if(recursiveFind) {
						findDirectChildrenAttributes(item,fieldsMap,prefix+nodeName+"/");
					}
					addNewField(nodeName, fieldsMap, item, prefix);
					if(recursiveFind && item.hasChildNodes()){
						findChildFields(item.getChildNodes(), fieldsMap,prefix+nodeName+"/");
					}
				}
			}
		}
	}
	
	/*
	 * 
	 */
	private void setRecursiveRetrieval(JasperReportsConfiguration jconfig) {
		recursiveFind = jconfig
				.getPropertyBoolean(XMLQueryEditorPreferencePage.P_USE_RECURSIVE_RETRIEVAL, false);
	}

	/*
	 * Adds a new JRDesignField to the current map.
	 * A proper name is generated if the node one can not be used.
	 */
	private void addNewField(String nodeName,
			LinkedHashMap<String, JRDesignField> fieldsMap, Node item, String prefix) {
		JRDesignField f = new JRDesignField();
		f.setName(ModelUtils.getNameForField(
				new ArrayList<JRDesignField>(fieldsMap.values()), nodeName));
		f.setValueClass(String.class);
		if (item.getNodeType() == Node.ATTRIBUTE_NODE) {
			f.setDescription(prefix + "@" + item.getNodeName()); //$NON-NLS-1$
		} else {
			f.setDescription(prefix + item.getNodeName());
		}
		fieldsMap.put(nodeName, f);
	}

	@Override
	public AWizardDataEditorComposite createDataEditorComposite(Composite parent, WizardPage page) {
		return new XMLWizardDataEditorComposite(parent, page, this);
	}
}
