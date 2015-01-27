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
package com.jaspersoft.studio.property.color.chooser;

import com.jaspersoft.studio.utils.AlfaRGB;

/**
 * Interface to define a color provider
 * 
 * @author Orlandin Marco
 *
 */
public interface IColorProvider {
	
	/**
	 * Get the color selected by the color provider
	 * 
	 * @return the alfa rgb of the color
	 */
	public AlfaRGB getSelectedColor();
	
	/**
	 * Dispose the color provider
	 */
	public void dispose();
}
