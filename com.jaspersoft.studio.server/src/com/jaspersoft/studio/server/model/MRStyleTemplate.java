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
package com.jaspersoft.studio.server.model;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.style.MStyleTemplate;
import com.jaspersoft.studio.model.util.IIconDescriptor;

public class MRStyleTemplate extends AFileResource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MRStyleTemplate(ANode parent, ResourceDescriptor rd, int index) {
		super(parent, rd, index);
	}

	public static IIconDescriptor getIconDescriptor() {
		return MStyleTemplate.getIconDescriptor();
	}

	@Override
	public IIconDescriptor getThisIconDescriptor() {
		return getIconDescriptor();
	}

	public static ResourceDescriptor createDescriptor(ANode parent) {
		ResourceDescriptor rd = MResource.createDescriptor(parent);
		rd.setWsType(ResourceDescriptor.TYPE_STYLE_TEMPLATE);
		return rd;
	}

	@Override
	public String getDefaultFileExtension() {
		return "jrtx";
	}
}
