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
package com.jaspersoft.studio.components.map.model.style;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.MapDataElementsConfigurationLabelProvider;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

/**
 * Descriptor for the <code>StandardMapComponent.PROPERTY_PATH_STYLE_LIST</code> property.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapStylesDescriptor extends NTextPropertyDescriptor {

	public MapStylesDescriptor(Object id, String displayName) {
		super(id, displayName);
	}

	@Override
	public CellEditor createPropertyEditor(Composite parent) {
//		return new MapStylesCellEditor(parent);
		return null;
	}
	
	@Override
	public ILabelProvider getLabelProvider() {
		return new MapDataElementsConfigurationLabelProvider(Messages.MapStylesDescriptor_Styles);
	}
	
	@Override
	public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
		return new SPMapStylesList(parent, section, this);
	}
}
