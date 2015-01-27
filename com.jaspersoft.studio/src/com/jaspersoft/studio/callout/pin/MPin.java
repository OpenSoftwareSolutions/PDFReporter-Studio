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
package com.jaspersoft.studio.callout.pin;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.callout.MCallout;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;

public class MPin extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MPin(ANode parent, Point p) {
		super(parent, -1);
		setValue(p);
	}

	private MPinConnection mPinConnection;

	public void setmPinConnection(MPinConnection mPinConnection) {
		this.mPinConnection = mPinConnection;
	}

	public MPinConnection getSourceConnections() {
		return mPinConnection;
	}

	@Override
	public Object getPropertyValue(Object id) {
		Point p = getValue();
		if (id.equals(JRDesignElement.PROPERTY_X))
			return p.x;
		if (id.equals(JRDesignElement.PROPERTY_Y))
			return p.y;
		return getValue();
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		Point p = getValue();
		if (id.equals(JRDesignElement.PROPERTY_X))
			p.x = (Integer) value + 8;
		else if (id.equals(JRDesignElement.PROPERTY_Y))
			p.y = (Integer) value + 8;

		((MCallout) getParent()).setPropertyValue("", "");
	}

	@Override
	public Point getValue() {
		return (Point) super.getValue();
	}

	@Override
	public ImageDescriptor getImagePath() {
		return null;
	}

	@Override
	public String getDisplayText() {
		return null;
	}

	@Override
	public Map<String, Object> getDefaultsMap() {
		return null;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {

	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return null;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {

	}

}
