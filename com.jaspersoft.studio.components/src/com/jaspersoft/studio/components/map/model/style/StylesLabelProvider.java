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

import net.sf.jasperreports.components.map.ItemProperty;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.ElementDataHelper;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementDTO;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementItemDTO;

/**
 * Label Provider for a viewer exposing the Path Styles of the map.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class StylesLabelProvider extends LabelProvider {
	
	@Override
	public String getText(Object element) {
		if(element instanceof MapDataElementDTO) {
			ItemProperty name = ((MapDataElementDTO) element).getName();
			if(name!=null){
				return Messages.StylesLabelProvider_Style + ElementDataHelper.getItemPropertyValueAsString(name);
			}
			else {
				return Messages.StylesLabelProvider_Style + ElementDataHelper.DEFAULT_ELEMENT_NAME;
			}
		}
		else if(element instanceof MapDataElementItemDTO) {
			return Messages.StylesLabelProvider_StyleItem;
		}
		return super.getText(element);
	}
	
	@Override
	public Image getImage(Object element) {
		if(element instanceof MapDataElementDTO){
			return Activator.getDefault().getImage("/icons/pathstyle-icon-16.png"); //$NON-NLS-1$
		}
		else if(element instanceof MapDataElementItemDTO){
			return Activator.getDefault().getImage("/icons/pathstyleitem-icon-16.png"); //$NON-NLS-1$
		}
		return super.getImage(element);
	}
}
