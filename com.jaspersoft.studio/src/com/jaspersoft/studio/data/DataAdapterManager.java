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
package com.jaspersoft.studio.data;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.eclipse.wizard.project.ProjectUtil;
import net.sf.jasperreports.util.CastorUtil;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.data.storage.FileDataAdapterStorage;
import com.jaspersoft.studio.data.storage.PreferencesDataAdapterStorage;

/*
 * The main plugin class to be used in the desktop.
 * 
 * @author Giulio Toffoli (gt78@users.sourceforge.net)
 */
public class DataAdapterManager {

	private static List<DataAdapterFactory> dataAdapterFactories = new ArrayList<DataAdapterFactory>();

	/*******************************
	 ** Data Adapter Factories Part **
	 *******************************/

	/**
	 * Add a DataAdapterFactory to the list of DataAdapterFactories in JaspersoftStudio. The new type of data adapter will
	 * then be visible when a new data adapter is created.
	 * 
	 * @param factory
	 */
	public static void addDataAdapterFactory(DataAdapterFactory factory) {
		if (!dataAdapterFactories.contains(factory)
				&& findFactoryByDataAdapterClass(factory.getDataAdapterClassName()) == null) {
			dataAdapterFactories.add(factory);
		}
	}

	/**
	 * Remove the DataAdapterFactory to the list of DataAdapterFactories in JaspersoftStudio.
	 * 
	 * @param factory
	 */
	public static void removeDataAdapterFactory(DataAdapterFactory factory) {
		if (dataAdapterFactories.contains(factory)) {
			dataAdapterFactories.remove(factory);
		}
	}

	/**
	 * Return a copy of the list of DataAdapterFactories in JaspersoftStudio.
	 */
	public static synchronized List<DataAdapterFactory> getDataAdapterFactories() {

		// Let's sort the list based on the description. Please note that the description may be localized,
		// so not all the languages have the same order if assumptions are done.

		DataAdapterFactory[] factories = dataAdapterFactories.toArray(new DataAdapterFactory[dataAdapterFactories.size()]);

		Arrays.sort(factories, new Comparator<DataAdapterFactory>() {

			@Override
			public int compare(DataAdapterFactory df1, DataAdapterFactory df2) {

				String name1 = (df1 == null) ? "" : df1.getLabel();
				String name2 = (df2 == null) ? "" : df2.getLabel();
				return name1.compareTo(name2);
			}
		});

		List<DataAdapterFactory> listOfDataAdapterFactories = new ArrayList<DataAdapterFactory>();
		listOfDataAdapterFactories.addAll(Arrays.asList(factories));

		return listOfDataAdapterFactories;
	}

	/**
	 * 
	 * @param adapterClassName
	 * @return
	 */
	public static DataAdapterFactory findFactoryByDataAdapterClass(String adapterClassName) {
		if (adapterClassName == null || adapterClassName.isEmpty())
			return null;

		for (DataAdapterFactory factory : dataAdapterFactories) {
			if (adapterClassName.equals(factory.getDataAdapterClassName())) {
				return factory;
			}
		}
		return null; // No factory found for this dataAdpater..
	}

	private static Map<Object, ADataAdapterStorage> storages = new HashMap<Object, ADataAdapterStorage>();

	public static ADataAdapterStorage[] getDataAdapter(IFile file) {
		ADataAdapterStorage[] st = new ADataAdapterStorage[file == null ? 1 : 2];
		st[0] = getPreferencesStorage();
		if (file != null)
			st[1] = getProjectStorage(file.getProject());
		return st;
	}

	public static ADataAdapterStorage getProjectStorage(IProject key) {
		ADataAdapterStorage s = storages.get(key);
		if (s == null) {
			s = new FileDataAdapterStorage(key);
			s.getDataAdapterDescriptors();
			storages.put(key, s);
		}
		return s;
	}

	public static ADataAdapterStorage getPreferencesStorage() {
		ADataAdapterStorage s = storages.get("PREFERENCES");
		if (s == null) {
			s = new PreferencesDataAdapterStorage();
			storages.put("PREFERENCES", s);
			s.getDataAdapterDescriptors();
		}
		return s;
	}

	public static List<ADataAdapterStorage> getProjectStorages() {
		List<ADataAdapterStorage> das = new ArrayList<ADataAdapterStorage>();
		for (IProject prj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (ProjectUtil.isOpen(prj))
				das.add(getProjectStorage(prj));
		}
		return das;
	}

	/***********************
	 ** Data Adapters Part **
	 ***********************/

	/**
	 * Return a copy of the list of DataAdapters in JaspersoftStudio.
	 */
	// public static List<DataAdapterDescriptor> getDataAdapters() {
	// if (!loaded) {
	// new ExtensionManager().init();
	// }
	// List<DataAdapterDescriptor> listOfDataAdapters = new ArrayList<DataAdapterDescriptor>();
	// listOfDataAdapters.addAll(dataAdapters);
	// return listOfDataAdapters;
	// }

	public static String toDataAdapterFile(DataAdapterDescriptor dataAdapter) {
		return "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + dataAdapter.toXml();
	}

	/**
	 * Creates a copy of a data adapter looking for the right Factory.
	 * 
	 * A NullPointerException is raised is the dataAdapter is null or if a suitable DataAdapterFactory is not found.
	 * 
	 * @param dataAdapter
	 * @return
	 */
	public static DataAdapterDescriptor cloneDataAdapter(DataAdapterDescriptor src) {
		DataAdapter srcDataAdapter = src.getDataAdapter();
		DataAdapterFactory factory = findFactoryByDataAdapterClass(srcDataAdapter.getClass().getName());
		DataAdapterDescriptor copy = factory.createDataAdapter();
		srcDataAdapter = (DataAdapter) CastorUtil.read(new ByteArrayInputStream(src.toXml().getBytes()),
				srcDataAdapter.getClass());
		copy.setDataAdapter(srcDataAdapter);
		return copy;
	}
}
