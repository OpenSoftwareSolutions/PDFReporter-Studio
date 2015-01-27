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
package com.jaspersoft.studio.editor.jrexpressions.ui.support;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

/**
 * Label provider for the navigator tree containing the categories of object items.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ObjectsNavigatorLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		if(element!=null && 
				element instanceof ObjectCategoryItem){
			return ((ObjectCategoryItem) element).getIcon();
		}
		return super.getImage(element);
	}

	@Override
	public String getText(Object element) {
		if(element!=null &&
				element instanceof ObjectCategoryItem){
			return ((ObjectCategoryItem) element).getDisplayName();
		}
		return super.getText(element);
	}

}
