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
package com.jaspersoft.studio.utils.compatibility;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

/**
 * This class "replaces" the {@link org.eclipse.gef.SharedImages} one.
 * <p>
 * This is needed in order to use some {@link ImageDescriptor} constants that were introduced 
 * since Eclipse 3.7.
 * Because we still give support for 3.5 and 3.6, we need to ensure backcompatibility.
 * 
 * TODO - Replace the use of this class with the {@link org.eclipse.gef.SharedImages}
 * when support for 3.5 and 3.6 will be dropped.
 *
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public class SharedImages {
	/**
	 * A 16x16 icon representing the Selection Tool
	 */
	public static final ImageDescriptor DESC_SELECTION_TOOL_16;

	/**
	 * A 24x24 icon representing the Selection Tool
	 */
	public static final ImageDescriptor DESC_SELECTION_TOOL_24;

	/**
	 * A 16x16 icon representing the Marquee Tool (nodes and connections)
	 */
	public static final ImageDescriptor DESC_MARQUEE_TOOL_16;

	/**
	 * A 24x24 icon representing the Marquee Tool (nodes and connections)
	 */
	public static final ImageDescriptor DESC_MARQUEE_TOOL_24;

	/**
	 * A 16x16 icon representing the Marquee Tool (nodes only)
	 */
	public static final ImageDescriptor DESC_MARQUEE_TOOL_NODES_16;

	/**
	 * A 24x24 icon representing the Marquee Tool (nodes only).
	 */
	public static final ImageDescriptor DESC_MARQUEE_TOOL_NODES_24;

	/**
	 * A 16x16 icon representing the Marquee Tool (connections only)
	 */
	public static final ImageDescriptor DESC_MARQUEE_TOOL_CONNECTIONS_16;

	/**
	 * A 24x24 icon representing the Marquee Tool (connections only).
	 */
	public static final ImageDescriptor DESC_MARQUEE_TOOL_CONNECTIONS_24;

	static {
		DESC_SELECTION_TOOL_16 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/arrow16.gif"); //$NON-NLS-1$
		DESC_SELECTION_TOOL_24 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/arrow24.gif"); //$NON-NLS-1$
		DESC_MARQUEE_TOOL_16 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/marquee16.gif"); //$NON-NLS-1$
		DESC_MARQUEE_TOOL_24 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/marquee24.gif"); //$NON-NLS-1$
		DESC_MARQUEE_TOOL_NODES_16 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/marquee_nodes16.gif"); //$NON-NLS-1$
		DESC_MARQUEE_TOOL_NODES_24 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/marquee_nodes24.gif"); //$NON-NLS-1$
		DESC_MARQUEE_TOOL_CONNECTIONS_16 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/marquee_wires16.gif"); //$NON-NLS-1$
		DESC_MARQUEE_TOOL_CONNECTIONS_24 = ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID,"icons/sharedimages/marquee_wires24.gif"); //$NON-NLS-1$
	}
}
