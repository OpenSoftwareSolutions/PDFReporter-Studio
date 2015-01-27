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

import java.util.HashSet;

import net.sf.jasperreports.eclipse.classpath.container.ClasspathContainerManager;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import org.osgi.framework.BundleContext;

/*
 * The main plugin class to be used in the desktop.
 * @author Teodor Danciu (teodord@users.sourceforge.net)
 * @version $Id: JasperCompileManager.java 1229 2006-04-19 13:27:35 +0300 (Wed, 19 Apr 2006) teodord $
 */
public class JasperReportsPlugin extends AbstractJRUIPlugin {

	// The shared instance.
	private static JasperReportsPlugin plugin;
	// The unique plug-in identifier
	public static final String PLUGIN_ID = "net.sf.jasperreports"; //$NON-NLS-1$

	/**
	 * Map to keeping track if a key is held down
	 */
	private static HashSet<Integer> pressedKeys = new HashSet<Integer>();

	/**
	 * Listener called when a key is pressed
	 */
	private static Listener keyDownListener = null;

	/**
	 * Listener called when a key is released
	 */
	private static Listener keyUpListener = null;

	/**
	 * The constructor.
	 */
	public JasperReportsPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * Initialize the listeners, this method need to be called only once and when
	 * the workbench and the display are available. All the calls after the first
	 * one dosen't do anything
	 */
	public static void initializeKeyListener() {
		if (keyDownListener == null && keyUpListener == null) {
			keyDownListener = new Listener() {
				public void handleEvent(Event e) {
					pressedKeys.add(e.keyCode);
				}
			};

			keyUpListener = new Listener() {
				public void handleEvent(Event e) {
					pressedKeys.remove(e.keyCode);
				}
			};

			PlatformUI.getWorkbench().getDisplay().addFilter(org.eclipse.swt.SWT.KeyDown, keyDownListener);
			PlatformUI.getWorkbench().getDisplay().addFilter(org.eclipse.swt.SWT.KeyUp, keyUpListener);
		}
	}

	/**
	 * Check if a key is held down or not
	 * 
	 * @param keyCode
	 *          an SWT keycode
	 * @return true if the key is held down, otherwise false
	 */
	public static boolean isPressed(int keyCode) {
		return pressedKeys.contains(keyCode);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		if (keyDownListener != null && keyUpListener != null && !PlatformUI.getWorkbench().getDisplay().isDisposed()) {
			PlatformUI.getWorkbench().getDisplay().removeFilter(org.eclipse.swt.SWT.KeyDown, keyDownListener);
			PlatformUI.getWorkbench().getDisplay().removeFilter(org.eclipse.swt.SWT.KeyUp, keyUpListener);
		}
		// Invoke the dispose of all resources handled by the generic SWT manager
		ResourceManager.dispose();
	}

	/**
	 * Returns the shared instance.
	 */
	public static JasperReportsPlugin getDefault() {
		return plugin;
	}

	@Override
	public String getPluginID() {
		return PLUGIN_ID;
	}

	private static ClasspathContainerManager classpathContainerManager;

	public static ClasspathContainerManager getClasspathContainerManager() {
		if (classpathContainerManager == null) {
			classpathContainerManager = new ClasspathContainerManager();
			classpathContainerManager.init();
		}
		return classpathContainerManager;
	}
}
