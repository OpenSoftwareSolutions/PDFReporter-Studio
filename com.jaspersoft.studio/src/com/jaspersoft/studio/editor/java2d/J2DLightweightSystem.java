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

import java.awt.Graphics2D;

import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;

/*
 * /* The Class J2DLightweightSystem.
 */
public final class J2DLightweightSystem extends LightweightSystem {

	private J2DGraphicsSource gsSource;

	public J2DLightweightSystem(Canvas c) {
		super(c);
		setUpdateManager(new J2DUpdateManager());
	}

	/**
	 * Instantiates a new j2 d lightweight system.
	 */
	public J2DLightweightSystem() {
		setUpdateManager(new J2DUpdateManager());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LightweightSystem#setControl(org.eclipse.swt.widgets.Canvas)
	 */
	public final void setControl(Canvas canvas) {
		super.setControl(canvas);
		if (canvas != null) {
			gsSource = new J2DGraphicsSource(canvas);
			getUpdateManager().setGraphicsSource(gsSource);
		}
	}

	public Graphics2D getGraphics2D() {
		if (gsSource != null)
			return gsSource.getGraphics2d();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.LightweightSystem#paint(org.eclipse.swt.graphics.GC)
	 */
	public final void paint(GC gc) {

		((J2DUpdateManager) getUpdateManager()).paintAll(gc);
	}
}
