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

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.editpolicies.ResizableEditPolicy;
import org.eclipse.gef.handles.AbstractHandle;
import org.eclipse.gef.handles.MoveHandle;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.jaspersoft.studio.editor.java2d.J2DGraphics;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IGraphicElement;

/*
 * The Class BandResizableEditPolicy.
 */
public class BandResizableEditPolicy extends ResizableEditPolicy {

	private String feedbackText = "";

	/**
	 * Color of the border painted to show the selection of a band
	 */
	private static Color marginColor = null;

	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		if (getHost().getModel() instanceof IGraphicElement) {
			// if (getHost() instanceof BandEditPart
			// && ((BandEditPart) getHost()).getModelNode().getValue() instanceof JRDesignBand) {
			APropertyNode n = (APropertyNode) getHost().getModel();
			int bandHeight = (Integer) n.getPropertyValue(JRDesignElement.PROPERTY_HEIGHT);
			Integer bWidth = (Integer) n.getPropertyValue(JRDesignElement.PROPERTY_WIDTH);

			Rectangle oldBounds = new Rectangle(0, 0, bWidth != null ? bWidth : 0, bandHeight);

			PrecisionRectangle rect2 = new PrecisionRectangle(new Rectangle(0, 0, request.getSizeDelta().width,
					request.getSizeDelta().height));
			getHostFigure().translateToRelative(rect2);

			oldBounds.resize(rect2.width, rect2.height);
			setFeedbackText(oldBounds.height + (bWidth != null ? "," + oldBounds.width : "") + " px");
		}
		super.showChangeBoundsFeedback(request);

	}

	/**
	 * Instantiates a new band resizable edit policy.
	 */
	public BandResizableEditPolicy() {
		super();
		setDragAllowed(false);
	}

	/**
	 * Class that paint a lateral border
	 * 
	 * @author Orlandin Marco
	 * 
	 */
	private class MarginBorder extends LineBorder {

		/**
		 * Paint the border reading it from a static variable and setting it if it's null
		 */
		@Override
		public void paint(IFigure figure, Graphics graphics, Insets insets) {
			if (figure.getChildren().isEmpty()) {
				figure.add(new Label());
			}
			Rectangle bounds = figure.getBounds();
			Graphics2D g = ((J2DGraphics) graphics).getGraphics2D();
			if (marginColor == null) {
				marginColor = new Color(128, 0, 0);
			}
			g.setColor(marginColor);
			g.fillRect(bounds.x - 3, bounds.y + 5, 5, bounds.height - 10);
		}

		public MarginBorder(int width) {
			super(width);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.ResizableEditPolicy#createSelectionHandles()
	 */
	@Override
	protected List<AbstractHandle> createSelectionHandles() {
		List<AbstractHandle> list = new ArrayList<AbstractHandle>();

		MoveHandle handle = new MoveHandle((GraphicalEditPart) getHost());
		handle.setBorder(new MarginBorder(5));
		list.add(handle);

		// BandButtonPadHandle buttonPadHandle=new BandButtonPadHandle((GraphicalEditPart)getHost());
		// buttonPadHandle.setBorder(null);
		// list.add(buttonPadHandle);
		// NonResizableHandleKit.addMoveHandle((GraphicalEditPart) getHost(), list);
		// list.add(new CellResizeHandle2((GraphicalEditPart) getHost(), PositionConstants.SOUTH));
		// // if (hasNorth)
		// list.add(new CellResizeHandle2((GraphicalEditPart) getHost(), PositionConstants.NORTH));
		return list;
	}

	protected IFigure createDragSourceFeedbackFigure() {
		// Use an invisible rectangle
		RectangleFigure r = new RectangleFigure() {

			@Override
			public void paintClientArea(Graphics g) {

				// g.setForegroundColor(ColorConstants.green);
				// Rectangle currentBounds = getBounds();
				String text = getFeedbackText();

				if (g == null)
					return;

				Rectangle clientArea = getClientArea();
				Graphics2D gr = ((J2DGraphics) g).getGraphics2D();

				// Stroke oldStroke = graphics2d.getStroke();
				// gr.setStroke(J2DUtils.getInvertedZoomedStroke(oldStroke, g.getAbsoluteScale()));

				// draw the line
				gr.setColor(Color.gray);
				// Draw the label...

				gr.fillOval(clientArea.x + (clientArea.width) / 2 - 3, clientArea.y - 3, 7, 7);
				gr.fillOval(clientArea.x + (clientArea.width) / 2 - 3, clientArea.y + clientArea.height - 4, 7, 7);

				if (clientArea.width < 20 || clientArea.height < 20) {

					gr.drawLine(clientArea.x + (clientArea.width) / 2, // Half X
							clientArea.y, // Half Y
							clientArea.x + (clientArea.width) / 2, // Half X
							clientArea.y + clientArea.height); // Up to the top of the label...

					return;
				}

				FontMetrics fm = gr.getFontMetrics();
				Rectangle2D textBounds = fm.getStringBounds(text, gr);

				java.awt.Rectangle textBgBounds = new java.awt.Rectangle(clientArea.x - 30 + (clientArea.width + 60) / 2
						- (int) textBounds.getWidth() / 2 - 10, clientArea.y - 30 + (clientArea.height + 60) / 2
						- (int) textBounds.getHeight() / 2 - 2, (int) textBounds.getWidth() + 20, (int) textBounds.getHeight() + 4);

				gr.setColor(new Color(30, 30, 30, 128));
				gr.fillRoundRect(textBgBounds.x, textBgBounds.y, textBgBounds.width, textBgBounds.height, 20, 20);

				/*
				 * gr.drawLine(clientArea.x-30, // X clientArea.y-30 + (clientArea.height+60)/2, // Half Y clientArea.x-30 +
				 * (clientArea.width+60 -textBgBounds.width)/ 2, // Up to the right side of the label clientArea.y-30 +
				 * (clientArea.height+60)/2); // Same Y...
				 * 
				 * gr.drawLine(clientArea.x-30 + (clientArea.width+60 + textBgBounds.width)/ 2, // From the left side of the
				 * label clientArea.y-30 + (clientArea.height+60)/2, // Half Y clientArea.x-30 + clientArea.width+60, // Up to
				 * the full width clientArea.y-30 + (clientArea.height+60)/2); // Same Y...
				 */

				gr.drawLine(clientArea.x - 30 + (clientArea.width + 60) / 2, // Half X
						clientArea.y - 30, // Half Y
						clientArea.x - 30 + (clientArea.width + 60) / 2, // Half X
						clientArea.y - 30 + (clientArea.height + 60 - textBgBounds.height) / 2); // Up to the top of the label...

				gr.drawLine(clientArea.x - 30 + (clientArea.width + 60) / 2, // Half X
						clientArea.y - 30 + (clientArea.height + 60 + textBgBounds.height) / 2, // // Up to the bottom of the
																																										// label...
						clientArea.x - 30 + (clientArea.width + 60) / 2, // Half X
						clientArea.y - 30 + clientArea.height + 60); // Up to the bounds height...

				gr.setColor(Color.white);

				gr.drawString(text, textBgBounds.x + 10, textBgBounds.y + fm.getAscent());
			}
		};
		r.setOpaque(false);
		r.setFill(false);
		r.setBounds(getInitialFeedbackBounds());
		addFeedback(r);
		return r;
	}

	private void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	private String getFeedbackText() {
		return feedbackText;
	}

}
