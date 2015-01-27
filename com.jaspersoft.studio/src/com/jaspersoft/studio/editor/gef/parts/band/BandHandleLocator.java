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
package com.jaspersoft.studio.editor.gef.parts.band;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Locator;
import org.eclipse.draw2d.geometry.Rectangle;
/*
 * The Class BandHandleLocator.
 */
public class BandHandleLocator implements Locator {
	private static final int bandHandleOffset = 3;
	/** The reference. */
	private IFigure reference;

	/**
	 * Creates a new MoveHandleLocator and sets its reference figure to <code>ref</code>. The reference figure should be
	 * the handle's owner figure.
	 * 
	 * @param ref
	 *          the ref
	 */
	public BandHandleLocator(IFigure ref) {
		setReference(ref);
	}

	/**
	 * Returns the reference figure for this locator.
	 * 
	 * @return the reference
	 */
	protected IFigure getReference() {
		return reference;
	}

	/**
	 * Sets the handle's bounds to that of its owner figure's bounds, expanded by the handle's Insets.
	 * 
	 * @param target
	 *          the target
	 */
	public void relocate(IFigure target) {
		Rectangle bounds = getReference().getBounds().getCopy();
		bounds.y = bounds.y + bounds.height - bandHandleOffset - 1;

		bounds.height = bandHandleOffset;
		getReference().translateToAbsolute(bounds);
		target.translateToRelative(bounds);
		target.setBounds(bounds);
	}

	/**
	 * Sets the reference figure.
	 * 
	 * @param follow
	 *          the new reference
	 */
	public void setReference(IFigure follow) {
		this.reference = follow;
	}

}
