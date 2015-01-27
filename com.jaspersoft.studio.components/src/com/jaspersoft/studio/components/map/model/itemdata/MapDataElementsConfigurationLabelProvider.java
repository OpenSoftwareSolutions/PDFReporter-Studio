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
package com.jaspersoft.studio.components.map.model.itemdata;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.osgi.util.NLS;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementsConfiguration;

/**
 * Label Provider for the {@link MapDataElementsConfiguration} element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapDataElementsConfigurationLabelProvider extends LabelProvider {

	private String itemsKind;
	
	public MapDataElementsConfigurationLabelProvider(String itemsKind) {
		super();
		this.itemsKind = itemsKind;
	}

	@Override
	public String getText(Object element) {
		if(element == null) {
			return NLS.bind(Messages.MapDataElementsConfigurationLabelProvider_NoElementsDefined,itemsKind); 
		}
		else if (element instanceof MapDataElementsConfiguration){
			int size = ((MapDataElementsConfiguration) element).getElements().size();
			return NLS.bind(Messages.MapDataElementsConfigurationLabelProvider_ElementsNum, itemsKind, size);
		}
		else {
			return super.getText(element);			
		}
	}
}
