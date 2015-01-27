/*******************************************************************************
 * Copyright (c) 2012 Stefan Reiterer.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Stefan Reiterer - initial API and implementation
 ******************************************************************************/

package org.eclipse.babel.core.util;

import java.util.Collections;
import java.util.List;

import org.eclipse.core.resources.IProject;

/**
 * This class provides common functionality to access eclipse plug-in
 * properties.
 * 
 * NOTE: Eclipse PDE core dependency is optional. Without PDE installed, the
 * methods of this class return null or empty data structures. (It is not usual
 * to manage eclipse plug-in projects, without PDE installed.
 * 
 * NOTE: The real implementation of this class is in the fragment
 * org.eclipse.babel.core.pdeutils.
 * 
 * @author Stefan Reiterer
 * 
 */
public class PDEUtils {

	/**
	 * Get the project's plug-in Id if the given project is an eclipse plug-in.
	 * 
	 * @param project
	 *            the workspace project.
	 * @return the project's plug-in Id. Null if the project is no plug-in
	 *         project.
	 */
	public static String getPluginId(IProject project) {
		return null;
	}

	/**
	 * Check if the given plug-in project is a fragment.
	 * 
	 * @param pluginProject
	 *            the plug-in project in the workspace.
	 * @return true if it is a fragment, otherwise false.
	 */
	public static boolean isFragment(IProject pluginProject) {
		return false;
	}

	/**
	 * Get all fragments for the given host project.
	 * 
	 * @param hostProject
	 *            the host plug-in project in the workspace.
	 * @return a list of all fragment projects for the given host project which
	 *         are in the same workspace as the host project.
	 */
	public static List<IProject> getFragments(IProject hostProject) {
		return Collections.emptyList();
	}

	/**
	 * Returns the fragment-id of the project if it is a fragment project with
	 * the specified host plugin id as host. Else null is returned.
	 * 
	 * @param project
	 *            the project
	 * @param hostPluginId
	 *            the host plugin id
	 * @return the plugin-id or null
	 */
	public static String getFragmentId(IProject project, String hostPluginId) {
		return null;
	}

	/**
	 * Returns the host plugin project of the specified project if it contains a
	 * fragment.
	 * 
	 * @param fragment
	 *            the fragment project
	 * @return the host plugin project or null
	 */
	public static IProject getFragmentHost(IProject fragment) {
		return null;
	}

	public static IProject[] lookupFragment(IProject fragment) {
		return null;
	}

}
