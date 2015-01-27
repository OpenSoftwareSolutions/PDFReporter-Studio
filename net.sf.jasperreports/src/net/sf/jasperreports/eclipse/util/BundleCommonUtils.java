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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import net.sf.jasperreports.eclipse.messages.Messages;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;
import org.osgi.framework.Bundle;

/**
 * Utility class that provides methods to work with plug-ins/bundles.
 * <p>
 * 
 * Among the facilities there are methods to deal with logging and file location
 * resolving.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * @see ResourceManager
 * @see SWTResourceManager
 * 
 */
public final class BundleCommonUtils {
	
	private BundleCommonUtils(){
		// Do nothing... prevent instantiation
	}

	/**
	 * Get the {@link Bundle} instance referring to the specified bundle ID.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @return the bundle instance if a valid one exists, <code>null</code>
	 *         otherwise
	 */
	public static Bundle getBundle(String bundleID) {
		Bundle bundle = Platform.getBundle(bundleID);
		if (bundle == null) {
			return null;
		}
		return bundle;
	}

	// ------------------------------------------- //
	// Files, Images and Locations utility methods
	// ------------------------------------------- //
	
	/**
	 * Get the full path name for a resource located inside the specified
	 * bundle.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param path
	 *            the path of the internal resource
	 * @return the string corresponding to the full path
	 * @throws IOException
	 *             if a problem occurs during conversion
	 */
	public static String getFileLocation(String bundleID, String path) throws IOException{
		Assert.isNotNull(bundleID);
		Assert.isNotNull(path);
		Bundle bundle = getBundle(bundleID);
		if(bundle!=null){
			return FileLocator.toFileURL(bundle.getEntry(path)).getPath();
		}
		else {
			return null;
		}
	}
	
	/**
	 * Returns an image descriptor for the image file at the given bundle
	 * relative path.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param path
	 *            the bundle path to look for
	 * @return the image descriptor if any, <code>null</code> otherwise
	 */
	public static ImageDescriptor getImageDescriptor(String bundleID, String path) {
		Assert.isNotNull(bundleID);
		Assert.isNotNull(path);
		return AbstractUIPlugin.imageDescriptorFromPlugin(bundleID, path);
	}
	
	/**
	 * Returns an SWT image related to the given bundle relative path.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param path
	 *            the bundle path of the image
	 * @return the SWT image instance if any, <code>null</code> otherwise
	 */
	public static Image getImage(String bundleID, String path){
		Assert.isNotNull(bundleID);
		Assert.isNotNull(path);
		return ResourceManager.getPluginImage(bundleID, path);
	}
	
	/**
	 * Returns the SWT image related to the specified image descriptor.
	 * 
	 * @param descriptor
	 *            the image descriptor
	 * @return the SWT image
	 */
	public static Image getImage(ImageDescriptor descriptor){
		return ResourceManager.getImage(descriptor);
	}
	
	/**
	 * Returns an URL representing the given bundle's installation directory.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * 
	 * @return the given bundle's installation directory
	 */
	public static URL getInstallUrl(String bundleID) {
		Bundle bundle = getBundle(bundleID);
		if (bundle == null) {
			return null;
		}
		return bundle.getEntry("/"); //$NON-NLS-1$
	}
	
	// ----------------------- //
	// Logging utility methods
	// ----------------------- //

	/**
	 * Logs an error message and an optional exception for the specified bundle.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param message
	 *            a human-readable message
	 * @param exception
	 *            a low-level exception, or <code>null</code> if not applicable
	 */
	public static void logError(String bundleID, String message, Throwable exception) {
		Bundle bundle = getBundle(bundleID);
		if (bundle == null){
			System.err.println(
					NLS.bind(Messages.BundleCommonUtils_LoggingToStdErr, bundleID));
			System.err.println(Messages.BundleCommonUtils_MessagePrefix + message);
			exception.printStackTrace();
			return;
		}
		Platform.getLog(bundle).log(
				new Status(IStatus.ERROR, bundleID, message, exception));
	}
		
	/**
	 * Logs a warning message and an optional exception for the specified
	 * bundle.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param message
	 *            a human-readable message
	 * @param exception
	 *            a low-level exception, or <code>null</code> if not applicable
	 */
	public static void logWarning(String bundleID, String message, Throwable exception) {
		Bundle bundle = getBundle(bundleID);
		if (bundle == null){
			System.err.println(
					NLS.bind(Messages.BundleCommonUtils_LoggingToStdErr, bundleID));
			System.err.println(Messages.BundleCommonUtils_MessagePrefix + message);
			exception.printStackTrace();
			return;
		}
		Platform.getLog(bundle).log(
				new Status(IStatus.WARNING, bundleID, message, exception));
	}

	/**
	 * Logs an information message for the specified bundle.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param message
	 *            a human-readable message
	 */
	public static void logInfo(String bundleID, String message) {
		Bundle bundle = getBundle(bundleID);
		if (bundle == null){
			System.out.println(
					NLS.bind(Messages.BundleCommonUtils_LoggingToStdOut, bundleID));
			System.out.println(Messages.BundleCommonUtils_MessagePrefix + message);
			return;
		}
		Platform.getLog(bundle)
				.log(new Status(IStatus.INFO, bundleID, message));
	}

	/**
	 * Logs a generic status for the specified bundle.
	 * 
	 * @param bundleID
	 *            the bundle ID
	 * @param status
	 *            the status object to be logged
	 */
	public static void logStatus(String bundleID, IStatus status) {
		Bundle bundle = getBundle(bundleID);
		if (bundle == null){
			return;
		}
		Platform.getLog(bundle).log(status);
	}

	/**
	 * @return the absolute path of the workspace location
	 */
	public static String getWorkspaceLocation(){
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		File wsDirLocation = ws.getRoot().getLocation().toFile();
		return wsDirLocation.getAbsolutePath();
	}
	
}
