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
/*
 * Copyright (c) 2006-2007 Nicolas Richeton.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors :
 *    Nicolas Richeton (nicolas.richeton@gmail.com) - initial API and implementation
 *******************************************************************************/
package org.eclipse.nebula.widgets.gallery;

import org.eclipse.swt.dnd.DragSourceEffect;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.graphics.Image;
/*
 * <p>
 * Visual effect for drag and drop operators on GalleryItem. This effect has to
 * be set in
 * {@link org.eclipse.swt.dnd.DragSource#setDragSourceEffect(DragSourceEffect)}
 * </p>
 * 
 * <p>
 * NOTE: THIS WIDGET AND ITS API ARE STILL UNDER DEVELOPMENT.
 * </p>
 * 
 * @see org.eclipse.swt.dnd.DragSource#setDragSourceEffect(DragSourceEffect)
 * 
 * @author Nicolas Richeton (nicolas.richeton@gmail.com)
 */
public class GalleryDragSourceEffect extends DragSourceEffect {
	Gallery g = null;

	/**
	 * Creates the drag source effect.
	 * 
	 * @param gallery
	 */
	public GalleryDragSourceEffect(Gallery gallery) {
		super(gallery);
		g = gallery;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.swt.dnd.DragSourceAdapter#dragStart(org.eclipse.swt.dnd.
	 * DragSourceEvent)
	 */
	public void dragStart(DragSourceEvent event) {
		GalleryItem[] selection = g.getSelection();
		if (selection != null) {
			Image img = selection[0].getImage();
			if (img != null) {
				event.image = img;
			}
		}
	}
}
