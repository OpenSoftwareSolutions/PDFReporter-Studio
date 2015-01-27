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
package com.jaspersoft.studio.utils.compatibility;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class "adds" the {@link org.eclipse.draw2d.FigureUtilities} missing methods in 3.5 and 3.6 Eclipse.
 * <p>
 * Because we still give support for 3.5 and 3.6, we need to ensure back-compatibility.
 * 
 * TODO - Replace the use of this class with the {@link org.eclipse.gef.SharedImages}
 * when support for 3.5 and 3.6 will be dropped.
 *
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 */
public class FigureUtilities {

	/**
	 * Determines whether the given figure is showing and not (completely)
	 * clipped.
	 * 
	 * @param figure
	 *            The figure to test
	 * @return <code>true</code> if the given figure is showing and not
	 *         completely clipped, <code>false</code> otherwise.
	 */
	public static boolean isNotFullyClipped(IFigure figure) {
		if (figure == null || !figure.isShowing()) {
			return false;
		}
		// check if figure is clipped
		// TODO: IClippingStrategy has to be taken into consideration as well.
		Rectangle figBounds = figure.getBounds().getCopy();
		IFigure walker = figure.getParent();
		while (!figBounds.isEmpty() && walker != null) {
			walker.translateToParent(figBounds);
			figBounds.intersect(walker.getBounds());
			walker = walker.getParent();
		}
		return !figBounds.isEmpty();
	}
}
