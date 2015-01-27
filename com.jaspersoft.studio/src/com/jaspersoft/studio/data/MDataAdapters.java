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

import java.beans.PropertyChangeEvent;

import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;

public class MDataAdapters extends ANode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (ADataAdapterStorage.PROP_DATAADAPTERS.equals(evt.getPropertyName())) {
			if (evt.getOldValue() == null && evt.getNewValue() != null) {
				boolean exists = false;
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getNewValue()) {
						exists = true;
						break;
					}
				}
				if (!exists)
					new MDataAdapter(this, (DataAdapterDescriptor) evt.getNewValue());
			}
			if (evt.getOldValue() != null)
				for (INode n : getChildren()) {
					MDataAdapter m = (MDataAdapter) n;
					if (m.getValue() == evt.getOldValue()) {
						removeChild(m);
						break;
					}
				}
		}
		super.propertyChange(evt);
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("dataAdapters"); //$NON-NLS-1$
		return iconDescriptor;
	}

	private transient ADataAdapterStorage storage;

	public MDataAdapters(ANode parent, ADataAdapterStorage storage) {
		super(parent, -1);
		this.storage = storage;
		storage.addPropertyChangeListener(this);
		storage.getDataAdapterDescriptors();
		for (DataAdapterDescriptor dad : storage.getDataAdapterDescriptors())
			new MDataAdapter(this, dad);
	}

	@Override
	public ADataAdapterStorage getValue() {
		return storage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

}
