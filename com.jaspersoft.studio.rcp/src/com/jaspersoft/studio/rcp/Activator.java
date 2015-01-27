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
package com.jaspersoft.studio.rcp;

import net.sf.jasperreports.eclipse.AbstractJRUIPlugin;

import org.eclipse.equinox.p2.ui.Policy;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import com.jaspersoft.studio.rcp.p2.JSSP2Policy;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractJRUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "com.jaspersoft.studio.rcp"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	// Stuff for JSS P2 Policy
	private ServiceRegistration<?> p2PolicyRegistration;
	private JSSP2Policy policy;
	private IPropertyChangeListener preferenceListener;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		plugin = this;
		// Register the p2 UI policy
		registerP2Policy(context);
		getPreferenceStore().addPropertyChangeListener(getPreferenceListener());
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		// Unregister the UI policy
		p2PolicyRegistration.unregister();
		p2PolicyRegistration = null;
		getPreferenceStore().removePropertyChangeListener(preferenceListener);
		preferenceListener = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	public String getPluginID() {
		return PLUGIN_ID;
	}
	
	/*
	 * Registers the P2 policy.
	 */
	private void registerP2Policy(BundleContext context) {
		policy = new JSSP2Policy();
		policy.updateForPreferences();
		p2PolicyRegistration = context.registerService(Policy.class.getName(), policy, null);
	}
	
	private IPropertyChangeListener getPreferenceListener() {
		if (preferenceListener == null) {
			preferenceListener = new IPropertyChangeListener() {
				public void propertyChange(PropertyChangeEvent event) {
					policy.updateForPreferences();
				}
			};
		}
		return preferenceListener;
	}

}
