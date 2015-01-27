/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization;

import java.util.ResourceBundle;

import com.jaspersoft.studio.model.util.NodeIconDescriptor;

/**
 * Icon descriptor for the Custom Visualization component element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CVNodeIconDescriptor extends NodeIconDescriptor {

	private static ResourceBundle resourceBundleIcons;
	
	public CVNodeIconDescriptor(String name) {
		super(name,CustomVisualizationActivator.getDefault());
	}

	@Override
	public ResourceBundle getResourceBundleIcons() {
		return resourceBundleIcons;
	}

	@Override
	public void setResourceBundleIcons(ResourceBundle resourceBundleIcons) {
		CVNodeIconDescriptor.resourceBundleIcons = resourceBundleIcons;
	}
	
}
