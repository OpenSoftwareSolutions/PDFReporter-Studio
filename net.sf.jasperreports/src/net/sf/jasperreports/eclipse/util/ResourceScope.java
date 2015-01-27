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

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.IScopeContext;
import org.osgi.service.prefs.Preferences;

public class ResourceScope implements IScopeContext {

	/**
	 * String constant (value of <code>"resource"</code>) used for the scope name
	 * for this preference scope.
	 */
	public static final String SCOPE = "resource"; //$NON-NLS-1$

	private IResource context;

	/**
	 * Create and return a new resource scope for the given resource. The given
	 * resource must not be <code>null</code>.
	 * 
	 * @param context
	 *          the project
	 * @exception IllegalArgumentException
	 *              if the project is <code>null</code>
	 */
	public ResourceScope(IResource context) {
		super();
		if (context == null)
			throw new IllegalArgumentException();
		this.context = context;
	}

	private Preferences pref;

	/*
	 * @see org.eclipse.core.runtime.IScopeContext#getNode(java.lang.String)
	 */
	public IEclipsePreferences getNode(String qualifier) {
		if (qualifier == null)
			throw new IllegalArgumentException();
		if (pref == null) {
			Preferences pnode = Platform.getPreferencesService().getRootNode().node(SCOPE);
			String ps = context.getFullPath().makeRelative().toPortableString();
			ps = ps.replace('/', ';');
			pref = pnode.node(ps);
		}
		return (IEclipsePreferences) pref.node(qualifier);
	}

	/*
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getLocation()
	 */
	public IPath getLocation() {
		IPath location = context.getLocation();
		return location == null ? null : location;
	}

	/*
	 * @see org.eclipse.core.runtime.preferences.IScopeContext#getName()
	 */
	public String getName() {
		return SCOPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (!(obj instanceof ResourceScope))
			return false;
		ResourceScope other = (ResourceScope) obj;
		return context.equals(other.context);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return super.hashCode() + context.getFullPath().hashCode();
	}
}
