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
package com.jaspersoft.studio.model.datasource.xml;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.JaspersoftStudioPlugin;

/**
 * ANode representing an XML document attribute node.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class XMLAttributeNode extends XMLNode{

	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public ImageDescriptor getImagePath() {
		return ResourceManager.getPluginImageDescriptor(
				JaspersoftStudioPlugin.PLUGIN_ID, "icons/resources/attribute_obj.gif");
	}

	@Override
	public String getDisplayText() {
		return super.getDisplayText();
	}
	
}
