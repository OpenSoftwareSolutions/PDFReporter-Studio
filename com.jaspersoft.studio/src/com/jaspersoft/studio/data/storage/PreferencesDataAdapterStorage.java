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
package com.jaspersoft.studio.data.storage;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.Enumeration;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.util.JRXmlUtils;
import net.sf.jasperreports.util.CastorUtil;

import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterFactory;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.preferences.util.PropertiesHelper;

public class PreferencesDataAdapterStorage extends ADataAdapterStorage {
	private static final String PREF_KEYS_DATA_ADAPTERS = "dataAdapters";//$NON-NLS-1$
	private Preferences prefs;
	private int ID = 0;

	public PreferencesDataAdapterStorage() {
		prefs = PropertiesHelper.INSTANCE_SCOPE.getNode(JaspersoftStudioPlugin.getUniqueIdentifier());
	}

	@Override
	public void addDataAdapter(String url, DataAdapterDescriptor adapter) {
		super.addDataAdapter(url.isEmpty() ? getNewID() : url, adapter);
		save(url, adapter);
	}

	private String getNewID() {
		return "" + ID++;
	}

	@Override
	public void findAll() {
		String xml = prefs.get(PREF_KEYS_DATA_ADAPTERS, null);
		if (xml != null) {
			try {
				Document document = JRXmlUtils.parse(new InputSource(new StringReader(xml)));

				NodeList adapterNodes = document.getDocumentElement().getChildNodes();// .getElementsByTagName("dataAdapter");

				for (int i = 0; i < adapterNodes.getLength(); ++i) {
					Node adapterNode = adapterNodes.item(i);

					if (adapterNode.getNodeType() == Node.ELEMENT_NODE) {
						// 1. Find out the class of this data adapter...
						String adapterClassName = adapterNode.getAttributes().getNamedItem("class").getNodeValue(); //$NON-NLS-1$

						DataAdapterFactory factory = DataAdapterManager.findFactoryByDataAdapterClass(adapterClassName);

						if (factory == null) {
							// we should at least log a warning here....
							JaspersoftStudioPlugin
									.getInstance()
									.getLog()
									.log(
											new Status(Status.WARNING, JaspersoftStudioPlugin.getUniqueIdentifier(), Status.OK,
													Messages.DataAdapterManager_nodataadapterfound + adapterClassName, null));
							continue;
						}

						DataAdapterDescriptor dataAdapterDescriptor = factory.createDataAdapter();

						DataAdapter dataAdapter = dataAdapterDescriptor.getDataAdapter();

						dataAdapter = (DataAdapter) CastorUtil.read(adapterNode, dataAdapter.getClass());

						dataAdapterDescriptor.setDataAdapter(dataAdapter);

						addDataAdapter(getNewID(), dataAdapterDescriptor);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// Add a list of default data adapters only if none is found.
		if (getDataAdapterDescriptors().size() == 0) {
			Bundle bundle = JaspersoftStudioPlugin.getInstance().getBundle();
			Enumeration<URL> urls = bundle.findEntries("defaults/dataadapter/prefs/", "*.xml", true);
			while (urls.hasMoreElements()) {
				InputStream in = null;
				try {
					in = urls.nextElement().openStream();
					DataAdapterDescriptor dad = FileDataAdapterStorage.readDataADapter(in, null);
					if (dad == null)
						continue;
					DataAdapterDescriptor prefdad = findDataAdapter(dad.getName());
					if (prefdad == null)
						addDataAdapter(getNewID(), dad);
					else
						addDataAdapter(getUrl(prefdad), dad);
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					FileUtils.closeStream(in);
				}
			}
		}
		for (DataAdapterDescriptor dad : JaspersoftStudioPlugin.getDefaultDAManager().getDefaultDAs()) {
			DataAdapterDescriptor prefdad = findDataAdapter(dad.getName());
			if (prefdad == null)
				addDataAdapter(getNewID(), dad);
			else
				addDataAdapter(getUrl(prefdad), dad);
		}
	}

	@Override
	public void save(String url, DataAdapterDescriptor adapter) {
		try {
			StringBuffer xml = new StringBuffer();
			xml.append("<dataAdapters>\n"); //$NON-NLS-1$
			for (DataAdapterDescriptor desc : getDataAdapterDescriptors()) {
				try {
					xml.append(desc.toXml());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			xml.append("</dataAdapters>"); //$NON-NLS-1$

			prefs.put("dataAdapters", xml.toString()); //$NON-NLS-1$ 
			prefs.flush();
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(String url) {
		save(url, null);
	}

}
