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
package com.jaspersoft.studio.data;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.model.ANode;

/*
 * This class wraps a DataAdapter to display a dataAdapter inside the repository view. The dataAdapters node in the view
 * takes care to update its content
 * 
 * @author czhu, gtoffoli
 */
public class MDataAdapter extends ANode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public DataAdapterDescriptor getValue() {
		return (DataAdapterDescriptor) super.getValue();
	}

	public MDataAdapter(MDataAdapters parent, DataAdapterDescriptor dataAdapter) {
		super(parent, dataAdapter, -1);
	}

	public String getDisplayText() {
		return getValue().getTitle();
	}

	public ImageDescriptor getImagePath() {
		return getValue().getIcon16();
	}
}
