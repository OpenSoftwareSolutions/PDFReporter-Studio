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
package com.jaspersoft.studio.data.jdbc;

import java.util.UUID;

import net.sf.jasperreports.data.jdbc.JdbcDataAdapterImpl;
import net.sf.jasperreports.eclipse.util.SecureStorageUtils;

import org.eclipse.equinox.security.storage.StorageException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.Activator;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.adapter.IDataAdapterCreator;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.data.secret.DataAdaptersSecretsProvider;

/**
 * Creator to build a JSS JDBC data adapter from the xml definition of an iReport JDBC 
 * data adapter
 * 
 * @author Orlandin Marco
 */
public class JDBCCreator implements IDataAdapterCreator {

	@Override
	public DataAdapterDescriptor buildFromXML(Document docXML) {
		JdbcDataAdapterImpl result = new JdbcDataAdapterImpl();
		
		NamedNodeMap rootAttributes = docXML.getChildNodes().item(0).getAttributes();
		String connectionName = rootAttributes.getNamedItem("name").getTextContent(); //$NON-NLS-1$
		result.setName(connectionName);
		
		NodeList children = docXML.getChildNodes().item(0).getChildNodes();
		for(int i=0; i<children.getLength(); i++){
			Node node = children.item(i);
			if (node.getNodeName().equals("connectionParameter")){ //$NON-NLS-1$
				String paramName = node.getAttributes().getNamedItem("name").getTextContent(); //$NON-NLS-1$
				
				if (paramName.equals("ServerAddress")) result.setServerAddress(node.getTextContent()); //$NON-NLS-1$
				if (paramName.equals("SavePassword")) result.setSavePassword(node.getTextContent().equals("true")); //$NON-NLS-1$ //$NON-NLS-2$
				if (paramName.equals("Url")) result.setUrl(node.getTextContent()); //$NON-NLS-1$
				if (paramName.equals("Database")) result.setDatabase(node.getTextContent()); //$NON-NLS-1$
				if (paramName.equals("Password")) result.setPassword(getPasswordValue(node.getTextContent())); //$NON-NLS-1$
				if (paramName.equals("Username")) result.setUsername(node.getTextContent()); //$NON-NLS-1$
				if (paramName.equals("JDBCDriver")) result.setDriver(node.getTextContent()); //$NON-NLS-1$
			}
		}
		JDBCDataAdapterDescriptor desc = new JDBCDataAdapterDescriptor();
		desc.setDataAdapter(result);
		return desc;
	}

	/* 
	 * Gets the secret storage key or the plain text password value.
	 */
	private String getPasswordValue(String passwordFieldTxt) {
		return JaspersoftStudioPlugin.shouldUseSecureStorage() 
				? getSecretStorageKey(passwordFieldTxt) : passwordFieldTxt;
	}

	@Override
	public String getID() {
		return "com.jaspersoft.ireport.designer.connection.JDBCConnection"; //$NON-NLS-1$
	}
	
	/*
	 * Returns the key that will be used to retrieve the information from 
	 * the secure preferences.
	 */
	private String getSecretStorageKey(String pass) {
		try {
			UUID uuidKey = UUID.randomUUID();
			SecureStorageUtils.saveToDefaultSecurePreferences(
					DataAdaptersSecretsProvider.SECRET_NODE_ID, uuidKey.toString(), pass);
			return uuidKey.toString();
		} catch (StorageException e) {
			Activator.getDefault().logError(Messages.JDBCCreator_ErrSecurPrefStorage,e);
		};
		// in case something goes wrong return the clear-text password
		// we will rely on back-compatibility
		return pass;
	}

}
