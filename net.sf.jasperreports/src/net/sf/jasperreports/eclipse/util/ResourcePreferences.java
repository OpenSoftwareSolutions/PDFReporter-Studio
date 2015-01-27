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
package net.sf.jasperreports.eclipse.util;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import net.sf.jasperreports.eclipse.messages.Messages;

import org.eclipse.core.internal.preferences.EclipsePreferences;
import org.eclipse.core.internal.utils.Policy;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.osgi.util.NLS;
import org.osgi.service.prefs.BackingStoreException;

public class ResourcePreferences extends EclipsePreferences {

	/**
	 * Cache which nodes have been loaded from disk
	 */
	protected static Set<String> loadedNodes = Collections.synchronizedSet(new HashSet<String>());
	private IFile file;
	private IEclipsePreferences loadLevel;
	private boolean initialized = false;
	/**
	 * Flag indicating that this node is currently reading values from disk, to
	 * avoid flushing during a read.
	 */
	private boolean isReading;
	private IFile pfile;
	private String qualifier;

	/**
	 * Default constructor. Should only be called by #createExecutableExtension.
	 */
	public ResourcePreferences() {
		super(null, null);
	}

	private ResourcePreferences(EclipsePreferences parent, String name) {
		super(parent, name);
		String path = absolutePath().replaceAll("/resource", "").replace(';', //$NON-NLS-1$ //$NON-NLS-2$
				'/');
		if (name != null) {
			if (path.endsWith(name)) {
				path = path.substring(0, path.length() - name.length() - 1);
				qualifier = name;
			}
		}
		if (!path.isEmpty())
			pfile = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(path));

		initialize();
	}

	/*
	 * Figure out what the children of this node are based on the resources that
	 * are in the workspace.
	 */
	private String[] computeChildren() {
		// if (pfile == null || !pfile.exists())
		return EMPTY_STRING_ARRAY;
		// IResource[] members = null;
		// try {
		// members = folder.members();
		// } catch (CoreException e) {
		// return EMPTY_STRING_ARRAY;
		// }
		// List<String> result = new ArrayList<String>();
		// for (int i = 0; i < members.length; i++) {
		// IResource resource = members[i];
		// if (resource.getType() == IResource.FILE
		// && PREFS_FILE_EXTENSION.equals(resource.getFullPath()
		// .getFileExtension()))
		// result.add("com.jaspersoft.studio");
		// }
		// return new String[] { "com.jaspersoft.studio" };
	}

	public void flush() throws BackingStoreException {
		if (isReading)
			return;
		// call the internal method because we don't want to be
		// synchronized, we will do that ourselves later.
		super.flush();
	}

	/*
	 * Return the node at which these preferences are loaded/saved.
	 */
	protected IEclipsePreferences getLoadLevel() {
		if (loadLevel == null) {
			if (pfile == null || qualifier == null)
				return null;
			loadLevel = this;
		}
		return loadLevel;
	}

	/*
	 * Calculate and return the file system location for this preference node. Use
	 * the absolute path of the node to find out the project name so we can get
	 * its location on disk.
	 * 
	 * NOTE: we cannot cache the location since it may change over the course of
	 * the project life-cycle.
	 */
	protected IPath getLocation() {
		if (pfile == null || qualifier == null)
			return null;
		IPath path = pfile.getLocation();
		return computeLocation(path, qualifier);
	}

	protected EclipsePreferences internalCreate(EclipsePreferences nodeParent, String nodeName, Object context) {
		return new ResourcePreferences(nodeParent, nodeName);
	}

	protected void initialize() {
		// if already initialized, then skip this initialization
		if (initialized)
			return;

		// initialize the children only if project is opened
		try {
			synchronized (this) {
				String[] names = computeChildren();
				for (int i = 0; i < names.length; i++)
					addChild(names[i], null);
			}
		} finally {
			// mark as initialized so that subsequent project opening will
			// not initialize preferences again
			initialized = true;
		}
	}

	protected boolean isAlreadyLoaded(IEclipsePreferences node) {
		return loadedNodes.contains(node.absolutePath());
	}

	protected boolean isAlreadyLoaded(String path) {
		return loadedNodes.contains(path);
	}

	protected void load() throws BackingStoreException {
		if (pfile == null || !pfile.exists()) {
			if (Policy.DEBUG_PREFERENCES)
				Policy.debug(NLS.bind(Messages.ResourcePreferences_ErrPreferenceFileNotExist, absolutePath()));
			return;
		}
		// Before loading the preferences, try to initialize parent node
		// (/project/<projectName>)
		// see bug 335591
		((ResourcePreferences) parent).initialize();
		if (Policy.DEBUG_PREFERENCES)
			Policy.debug(NLS.bind(Messages.ResourcePreferences_LoadingPreferencesFile, pfile.getFullPath()));
		try {
			Properties result = FilePrefUtil.loadPreferences(pfile);
			// Map<QualifiedName, String> map = pfile.getPersistentProperties();
			// for (QualifiedName qn : map.keySet()) {
			// result.put(qn.getLocalName(), map.get(qn));
			// }

			convertFromProperties(this, result, true);
			loadedNodes.add(absolutePath());
		} catch (CoreException e) {
			String message = NLS.bind(Messages.ResourcePreferences_ErrorLoadingPreferenceFile, pfile.getFullPath());
			log(new Status(IStatus.ERROR, ResourcesPlugin.PI_RESOURCES, IStatus.ERROR, message, e));
			throw new BackingStoreException(message);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.internal.preferences.EclipsePreferences#nodeExists(java
	 * .lang.String)
	 * 
	 * If we are at the /project node and we are checking for the existence of a
	 * child, we want special behaviour. If the child is a single segment name,
	 * then we want to return true if the node exists OR if a project with that
	 * name exists in the workspace.
	 */
	public boolean nodeExists(String path) throws BackingStoreException {
		if (path.length() == 0)
			return super.nodeExists(path);
		if (path.charAt(0) == IPath.SEPARATOR)
			return super.nodeExists(path);
		if (path.indexOf(IPath.SEPARATOR) != -1)
			return super.nodeExists(path);
		// if we are checking existance of a single segment child of /project,
		// base the answer on
		// whether or not it exists in the workspace.
		return ResourcesPlugin.getWorkspace().getRoot().getProject(path).exists() || super.nodeExists(path);
	}

	protected void save() throws BackingStoreException {
		Properties table = convertToProperties(new Properties(), ""); //$NON-NLS-1$
		// nothing to save. delete existing file if one exists.

		table.put(VERSION_KEY, VERSION_VALUE);
		// print the table to a string and remove the timestamp
		// that Properties#store always adds
		try {
			Map<QualifiedName, String> map = pfile.getPersistentProperties();
			for (QualifiedName qn : map.keySet()) {
				try {
					pfile.setPersistentProperty(qn, null);
				} catch (CoreException e) {
					e.printStackTrace();
				}
			}
			FilePrefUtil.savePreferences(pfile, qualifier, table);
			// for (String key : table.stringPropertyNames()) {
			// try {
			// pfile.setPersistentProperty(new QualifiedName(qualifier,
			// key), table.getProperty(key));
			// } catch (CoreException e) {
			// e.printStackTrace();
			// }
			// }
		} catch (CoreException e1) {
			e1.printStackTrace();
		}

	}
}
