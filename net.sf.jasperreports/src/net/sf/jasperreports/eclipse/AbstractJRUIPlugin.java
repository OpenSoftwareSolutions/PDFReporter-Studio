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
package net.sf.jasperreports.eclipse;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.sf.jasperreports.eclipse.messages.Messages;
import net.sf.jasperreports.eclipse.util.BundleCommonUtils;
import net.sf.jasperreports.eclipse.util.ResourceScope;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.preferences.ScopedPreferenceStore;

/**
 * Abstract plug-in superclass that provides methods for logging and other
 * common features.
 * <p>
 * 
 * Most of the methods of this class are wrappers for the utility methods in
 * {@link BundleCommonUtils}.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see BundleCommonUtils
 * 
 */
public abstract class AbstractJRUIPlugin extends AbstractUIPlugin {

	/**
	 * @return the identifier for the current plug-in
	 */
	public abstract String getPluginID();

	/**
	 * Get the full path name for a resource located inside the plug-in.
	 * 
	 * @param path
	 *          the path of the internal resource
	 * @return the string corresponding to the full path
	 * @throws IOException
	 *           if a problem occurs during conversion
	 */
	public String getFileLocation(String path) throws IOException {
		return BundleCommonUtils.getFileLocation(getPluginID(), path);
	}

	private Map<String, ImageDescriptor> map = new HashMap<String, ImageDescriptor>();

	/**
	 * Returns an image descriptor for the image file at the given bundle relative
	 * path.
	 * 
	 * @param path
	 *          the bundle path to look for
	 * @return the image descriptor if any, <code>null</code> otherwise
	 */
	public ImageDescriptor getImageDescriptor(String path) {
		ImageDescriptor id = map.get(path);
		if (id == null)
			id = BundleCommonUtils.getImageDescriptor(getPluginID(), path);
		return id;
	}

	/**
	 * Returns an SWT image related to the given bundle relative path.
	 * 
	 * @param path
	 *          the bundle path of the image
	 * @return the SWT image instance if any, <code>null</code> otherwise
	 */
	public Image getImage(String path) {
		return BundleCommonUtils.getImage(getImageDescriptor(path));
	}

	/**
	 * Returns the SWT image related to the specified image descriptor.
	 * 
	 * @param descriptor
	 *          the image descriptor
	 * @return the SWT image
	 */
	public Image getImage(ImageDescriptor descriptor) {
		return BundleCommonUtils.getImage(descriptor);
	}

	/**
	 * Logs an error message and an optional exception.
	 * 
	 * @param message
	 *          a human-readable message
	 * @param exception
	 *          a low-level exception, or <code>null</code> if not applicable
	 */
	public void logError(String message, Throwable exception) {
		BundleCommonUtils.logError(getPluginID(), message, exception);
	}
	
	/**
	 * Logs an exception with a generic error message.
	 * 
	 * @param exception a low-level exception, can not be <code>null</code>
	 * 
	 */
	public void logError(Throwable exception) {
		Assert.isNotNull(exception);
		logError(Messages.AbstractJRUIPlugin_GenericErrorMsg, exception);
	}

	/**
	 * Logs a warning message and an optional exception.
	 * 
	 * @param message
	 *          a human-readable message
	 * @param exception
	 *          a low-level exception, or <code>null</code> if not applicable
	 */
	public void logWarning(String message, Throwable exception) {
		BundleCommonUtils.logWarning(getPluginID(), message, exception);
	}
	
	/**
	 * Logs a warning message.
	 * 
	 * @param message
	 *          a human-readable message
	 */	
	public void logWarning(String message) {
		logWarning(message, null);
	}

	/**
	 * Logs an informational message.
	 * 
	 * @param message
	 *          a human-readable message
	 */
	public void logInfo(String message) {
		BundleCommonUtils.logInfo(getPluginID(), message);
	}

	/**
	 * Logs a generic status object.
	 * 
	 * @param status
	 *          the status object to be logged
	 */
	public void log(IStatus status) {
		BundleCommonUtils.logStatus(getPluginID(), status);
	}

	@Override
	public IPreferenceStore getPreferenceStore() {
		return getPreferenceStore(null, getPluginID());
	}

	private Map<IResource, Map<String, MScopedPreferenceStore>> prefStores = new HashMap<IResource, Map<String, MScopedPreferenceStore>>();

	public ScopedPreferenceStore getPreferenceStore(IResource project, String pageId) {
		MScopedPreferenceStore pstore = null;
		if (project != null) {
			Map<String, MScopedPreferenceStore> pagemap = prefStores.get(project);
			if (pagemap != null) {
				pstore = pagemap.get(pageId);
			} else {
				pagemap = new HashMap<String, MScopedPreferenceStore>();
				prefStores.put(project, pagemap);
			}
			if (pstore == null && project != null) {
				if (project instanceof IProject) {
					pstore = new MScopedPreferenceStore(new ProjectScope((IProject) project), pageId);
					pstore.setSearchContexts(new IScopeContext[] { new ProjectScope((IProject) project), new InstanceScope() });
				} else {
					pstore = new MScopedPreferenceStore(new ResourceScope(project), pageId);
					pstore.setSearchContexts(new IScopeContext[] { new ResourceScope(project), new ProjectScope(project.getProject()), new InstanceScope() });
				}
				for (IPropertyChangeListener pl : listeners)
					pstore.addPropertyChangeListener(pl);
				pstores.add(pstore);
				pagemap.put(pageId, pstore);
			}
		} else {
			if (instStore == null) {
				instStore = new MScopedPreferenceStore(new InstanceScope(), pageId);
				instStore.setSearchContexts(new IScopeContext[] { new InstanceScope() });
				pstores.add(instStore);
				for (IPropertyChangeListener pl : listeners)
					instStore.addPropertyChangeListener(pl);
			}
			pstore = instStore;
		}
		return pstore;
	}

	private MScopedPreferenceStore instStore;
	private Set<ScopedPreferenceStore> pstores = new HashSet<ScopedPreferenceStore>();
	private Set<IPropertyChangeListener> listeners = new HashSet<IPropertyChangeListener>();

	public void addPreferenceListener(IPropertyChangeListener plistener) {
		listeners.add(plistener);
		getPreferenceStore().addPropertyChangeListener(plistener);
		for (ScopedPreferenceStore p : pstores)
			p.addPropertyChangeListener(plistener);
	}

	public void removePreferenceListener(IPropertyChangeListener plistener) {
		getPreferenceStore().removePropertyChangeListener(plistener);
		for (ScopedPreferenceStore p : pstores)
			p.removePropertyChangeListener(plistener);
		listeners.remove(plistener);
	}
}
