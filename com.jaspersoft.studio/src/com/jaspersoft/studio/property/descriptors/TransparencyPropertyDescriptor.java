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
package com.jaspersoft.studio.property.descriptors;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPTransparency;

/*
 * The Class FloatPropertyDescriptor.
 * 
 * @author Chicu Veaceslav
 */
public class TransparencyPropertyDescriptor extends FloatPropertyDescriptor {

	/**
	 * Instantiates a new float property descriptor.
	 * 
	 * @param id
	 *          the id
	 * @param displayName
	 *          the display name
	 */
	public TransparencyPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		SPTransparency spTransparency = new SPTransparency(parent, section, this);
		spTransparency.setBorders(new Float(0), new Float(1));
		spTransparency.setNumType(Float.class);
		return spTransparency;
	}
}
