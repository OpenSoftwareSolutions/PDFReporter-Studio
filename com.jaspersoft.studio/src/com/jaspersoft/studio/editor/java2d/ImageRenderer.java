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
package com.jaspersoft.studio.editor.java2d;

import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Display;
/*/*
 * The Interface ImageRenderer.
 */
public interface ImageRenderer
{
  
  /**
	 * Render.
	 * 
	 * @param paramDisplay
	 *          the param display
	 * @param paramGC
	 *          the param gc
	 * @param imageData
	 *          the image data
	 * @param paramInt1
	 *          the param int1
	 * @param paramInt2
	 *          the param int2
	 * @param paramInt3
	 *          the param int3
	 * @param paramInt4
	 *          the param int4
	 * @param paramInt5
	 *          the param int5
	 * @param paramInt6
	 *          the param int6
	 * @param paramInt7
	 *          the param int7
	 * @param paramInt8
	 *          the param int8
	 */
  public void render(Display paramDisplay, GC paramGC, int[] imageData, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, int paramInt6, int paramInt7, int paramInt8);
}
