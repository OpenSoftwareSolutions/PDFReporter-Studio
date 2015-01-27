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
package com.jaspersoft.studio.components.commonstyles;

import java.util.List;

import org.eclipse.swt.graphics.Image;

import com.jaspersoft.studio.editor.style.TemplateStyle;

/**
 * Interface that need to be implemented for the import\export of a type of styles 
 * 
 * @author Orlandin Marco
 *
 */
public interface ViewProviderInterface {
	/**
	 * Build a preview image of a TempalteStyle
	 * 
	 * @param style the style
	 * @return a preview SWT image of the style
	 */
	public abstract Image generatePreviewFigure(final TemplateStyle style);
	
	/**
	 * Return the list of the styles provided by this provider
	 * 
	 * @return a List of template style
	 */
	public abstract  List<TemplateStyle> getStylesList();
	
	/**
	 * Return a list of styles that this provider can handle, selecting them from a list
	 * of styles passed a as parameter
	 * 
	 * @param mixedList a list of mixed template style
	 * @return a list of styles taken from the mixedList, extracting the ones handled by 
	 * this provider
	 */
	public abstract  List<TemplateStyle> getStylesList(List<TemplateStyle> mixedList);
}
