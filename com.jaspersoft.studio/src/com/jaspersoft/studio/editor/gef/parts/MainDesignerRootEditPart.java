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
package com.jaspersoft.studio.editor.gef.parts;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.ScalableFreeformLayeredPane;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.editparts.GridLayer;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;

import com.jaspersoft.studio.editor.gef.parts.band.NotMovablePartDragTracker;
import com.jaspersoft.studio.editor.java2d.J2DScalableFreeformLayeredPane;

/*
 * The Class MainDesignerRootEditPart.
 * 
 * @author Chicu Veaceslav
 */
public class MainDesignerRootEditPart extends ScalableFreeformRootEditPart {

	/** The Constant REPORT_LAYER. */
	public static final String REPORT_LAYER = "REPORT_LAYER"; //$NON-NLS-1$

	/** The Constant SECTIONS_LAYER. */
	public static final String SECTIONS_LAYER = "SECTIONS_LAYER"; //$NON-NLS-1$

	/** The Constant ELEMENTS_LAYER. */
	public static final String ELEMENTS_LAYER = "ELEMENTS_LAYER"; //$NON-NLS-1$

	/**
	 * Instantiates a new main designer root edit part.
	 */
	public MainDesignerRootEditPart() {
		super();
		// set zoom manager
		ZoomManager zoomManager = getZoomManager();
		zoomManager.setZoomAnimationStyle(ZoomManager.ANIMATE_ZOOM_IN_OUT);
		zoomManager.setZoomLevels(new double[] { 0.25, 0.35, 0.45, 0.5, 0.6, 0.7, 0.75, 0.8, 0.85, 0.95, 1.0, 1.1, 1.2,
				1.25, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0 });
		List<String> zoomLevels = new ArrayList<String>(3);
		zoomLevels.add(ZoomManager.FIT_ALL);
		zoomLevels.add(ZoomManager.FIT_WIDTH);
		zoomLevels.add(ZoomManager.FIT_HEIGHT);
		zoomManager.setZoomLevelContributions(zoomLevels);
	}
	
	/**
	 * Return the edited drag tracker, so the selection can be cancelled clicking out of the working area
	 */
	@Override
	public org.eclipse.gef.DragTracker getDragTracker(org.eclipse.gef.Request req) {
		return new NotMovablePartDragTracker(this);
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.ScalableFreeformRootEditPart#createScaledLayers()
	 */
	@Override
	protected ScalableFreeformLayeredPane createScaledLayers() {
		ScalableFreeformLayeredPane layers = new J2DScalableFreeformLayeredPane();
		// layers.add(createGridLayer(), GRID_LAYER);

		layers.add(getPrintableLayers(), PRINTABLE_LAYERS);
		layers.add(new FeedbackLayer(), SCALED_FEEDBACK_LAYER);
		layers.add(new FreeformLayer(), REPORT_LAYER);
		layers.add(new FreeformLayer(), SECTIONS_LAYER);
		layers.add(new FreeformLayer(), ELEMENTS_LAYER);

		return layers;
	}

	@Override
	protected void addChildVisual(EditPart childEditPart, int index) {
		if (childEditPart instanceof PageEditPart) {
			IFigure layer = getLayer(MainDesignerRootEditPart.REPORT_LAYER);
			if (layer != null) {
				IFigure pageFigure = ((PageEditPart) childEditPart).getFigure();
				layer.add(pageFigure);
			}
		}
		super.addChildVisual(childEditPart, index);
	}

	@Override
	protected GridLayer createGridLayer() {
		return new com.jaspersoft.studio.editor.gef.figures.layers.GridLayer();
	}

	class FeedbackLayer extends FreeformLayer {
		FeedbackLayer() {
			setEnabled(false);
		}
	}
}
