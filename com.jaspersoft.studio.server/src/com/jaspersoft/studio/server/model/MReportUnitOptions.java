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

import java.util.ArrayList;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceProperty;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.server.ServerIconDescriptor;

public class MReportUnitOptions extends MResource implements IInputControlsContainer {
	private static final String PROP_OPTIONS_NAME = "PROP_OPTIONS_NAME";
	private static final String PROP_VALUES = "PROP_VALUES";
	public static final String PROP_RU_URI = "PROP_RU_URI";
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MReportUnitOptions(ANode parent, ResourceDescriptor rd, int index) {
		super(parent, rd, index);
	}

	private static IIconDescriptor iconDescriptor;

	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new ServerIconDescriptor("reportunitoptions"); //$NON-NLS-1$
		return iconDescriptor;
	}

	@Override
	public void setParent(ANode parent, int newIndex) {
		if (parent instanceof MReportUnit)
			parent = parent.getParent();
		super.setParent(parent, newIndex);
	}

	@Override
	public IIconDescriptor getThisIconDescriptor() {
		return getIconDescriptor();
	}

	public static ResourceDescriptor createDescriptor(MReportUnit parent) {
		ResourceDescriptor rd = MResource.createDescriptor(parent);
		ResourceProperty rp = new ResourceProperty(PROP_RU_URI, parent.getValue().getUriString());
		rd.getProperties().add(rp);

		rp = new ResourceProperty(PROP_OPTIONS_NAME, rd.getName());
		rd.getProperties().add(rp);

		rp = new ResourceProperty(PROP_VALUES);
		rp.setProperties(new ArrayList<ResourceProperty>());
		rd.getProperties().add(rp);

		rd.setParentFolder(parent.getValue().getParentFolder());
		rd.setWsType(ResourceDescriptor.TYPE_REPORT_OPTIONS);
		return rd;
	}
}
