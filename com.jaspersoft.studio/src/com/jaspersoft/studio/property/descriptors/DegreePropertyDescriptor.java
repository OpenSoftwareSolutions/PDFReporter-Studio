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
import com.jaspersoft.studio.property.section.widgets.SPDegree;

/*
 * The Class FloatPropertyDescriptor.
 * 
 * @author Chicu Veaceslav
 */
public class DegreePropertyDescriptor extends DoublePropertyDescriptor {

	/**
	 * Instantiates a new float property descriptor.
	 * 
	 * @param id
	 *          the id
	 * @param displayName
	 *          the display name
	 */
	public DegreePropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		SPDegree spDegree = new SPDegree(parent, section, this);
		spDegree.setNumType(Double.class);
		spDegree.setBorders(new Double(-360), new Double(360));
		return spDegree;
	}
}
