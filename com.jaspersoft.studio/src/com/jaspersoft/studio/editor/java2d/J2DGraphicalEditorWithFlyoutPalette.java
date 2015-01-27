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

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.swt.widgets.Composite;
/*
 * A J2DGraphicalEditorWithFlyoutPalette is a GraphicalEditorWithFlyoutPalette but a
 * J2DScrollingGraphicalViewer is created instead of a ScrollingGraphicalViewer.
 * <p>
 * Note that this code is the exact duplicate of the modifications made inside
 * J2DGraphicalEditor. The alternative was to derive this class from J2DGraphicalEditor
 * and duplicate all the GraphicalEditorWithPalette specific code. Duplicating only one
 * method makes us less dependent on the ancestors implementation.
 * </p>
 * 
 * @author Christophe Avare
 * @version $Revision: 1.1.2.1 $
 */
public abstract class J2DGraphicalEditorWithFlyoutPalette extends GraphicalEditorWithFlyoutPalette {
	
	/**
	 * Instantiates a new j2 d graphical editor with flyout palette.
	 */
	public J2DGraphicalEditorWithFlyoutPalette() {
		super();
	}

	/**
	 * Creates the GraphicalViewer on the specified <code>Composite</code>. A
	 * J2DScrollingGraphicalViewer is internally created.
	 * 
	 * @param parent
	 *            The parent composite
	 */
	protected void createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new J2DScrollingGraphicalViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
	}

}
