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

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionRectangle;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.SelectionEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;

import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * The Class BandMoveEditPolicy.
 * 
 * @author Chicu Veaceslav
 */
public class BandMoveEditPolicy extends SelectionEditPolicy {

	/** The feedback. */
	private IFigure feedback;

	/** The handle. */
	private IFigure handle;

	/** The original y location. */
//	private int originalYLocation;

	/**
	 * This figure represents the new bottom margin of the band It should turn red when the margin is at the maximum of
	 * its possibilities or when it raches the bottom of the previous band (which is when the new band height would become
	 * 0. The figure is just a line at the X coordinates. The figure width is the width of the BandPart we are going to
	 * move.
	 * 
	 * @author gtoffoli
	 * 
	 */
	private static class BandMoveFeedbackFigure extends Figure {

		/** The x label pos. */
		private int xLabelPos = 0;

		/** The band height. */
		private int bandHeight = 0;

		/**
		 * This is the X position for the label.
		 * 
		 * @param xpos
		 *          the new label position
		 */
		public void setLabelPosition(int xpos) {
			this.xLabelPos = xpos;
		}

		/**
		 * The is the new value temporary value of the band height. It is defined in pixel, so it may be converted to a
		 * different unit It is used to print a label in this feedback figure.
		 * 
		 * @param bandHeight
		 *          the new band height
		 */
		public void setBandHeight(int bandHeight) {
			this.bandHeight = bandHeight;
		}

		/**
		 * Instantiates a new band move feedback figure.
		 */
		public BandMoveFeedbackFigure() {
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.draw2d.IFigure#paint(Graphics)
		 */
		public void paint(Graphics g) {

			/*
			 * // draw the line g.setForegroundColor(this.getForegroundColor()); g.drawLine(currentBounds.x, currentBounds.y,
			 * currentBounds.x + currentBounds.width, currentBounds.y);
			 * 
			 * // Draw the label...
			 * 
			 * 
			 * g.setAlpha(128); String text = bandHeight + " px"; //$NON-NLS-1$ Label label = new Label(text);
			 * label.setFont(g.getFont()); Rectangle textBounds = label.getTextBounds();
			 * g.setBackgroundColor(ColorConstants.gray); g.fillRoundRectangle(new Rectangle(xLabelPos, currentBounds.y,
			 * textBounds.width + 20, textBounds.height + 8), 10, 10); g.setForegroundColor(ColorConstants.white);
			 * g.drawText(text, xLabelPos + 10, currentBounds.y);
			 */
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#activate()
	 */
	public void activate() {
		super.activate();
		setHandle(new BandResizeHandle((GraphicalEditPart) getHost()));
		getLayer(LayerConstants.HANDLE_LAYER).add(getHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#deactivate()
	 */
	public void deactivate() {
		if (getHandle() != null) {
			getLayer(LayerConstants.HANDLE_LAYER).remove(getHandle());
			setHandle(null);
		}
		if (feedback != null) {
			removeFeedback(feedback);
			feedback = null;
		}
		super.deactivate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#understandsRequest(Request)
	 */
	public boolean understandsRequest(Request request) {
		if (REQ_RESIZE.equals(request.getType())) {
			ChangeBoundsRequest r = (ChangeBoundsRequest) request;
			if (r.getResizeDirection() == PositionConstants.SOUTH)
				return true;
		}
		return false;
	}

	/**
	 * Gets the drag source feedback figure.
	 * 
	 * @param mouseLocation
	 *          the mouse location
	 * @return the drag source feedback figure
	 */
	protected IFigure getDragSourceFeedbackFigure(Point mouseLocation) {
		if (feedback == null) {

			feedback = new BandMoveFeedbackFigure();
			feedback.setOpaque(false);
			IFigure hfigure = getHostFigure();
			Rectangle bounds = hfigure.getBounds().getCopy();

			hfigure.translateToAbsolute(bounds);
			bounds.height = 20;

			// We need to translate the bounds of the band figure to the bounds of
			// the feedback layer.
			getFeedbackLayer().translateToRelative(bounds);
			getFeedbackLayer().translateToRelative(mouseLocation);
			feedback.setBounds(bounds);
			((BandMoveFeedbackFigure) feedback).setLabelPosition(mouseLocation.x + 10);
			Rectangle bounds2 = bounds.getCopy();
			feedback.translateToRelative(bounds2);
//			originalYLocation = feedback.getBounds().y;
			addFeedback(feedback);
		}
		return feedback;
	}

	/**
	 * Show change bounds feedback.
	 * 
	 * @param request
	 *          the request
	 */
	protected void showChangeBoundsFeedback(ChangeBoundsRequest request) {
		if (REQ_RESIZE.equals(request.getType())) {
			IFigure feedbackFigure = getDragSourceFeedbackFigure(request.getLocation());

			Point moveDelta = request.getLocation().getCopy();

			// The delta here is the mouse delta, but the viewport may have been
			// scrolled
			// so let's calculate the REAL delta...

			getFeedbackLayer().translateToRelative(moveDelta);

			// moveDelta.y = moveDelta.y - originalYLocation;
			moveDelta.x = 0;

			// The request delta is in absolute coordinates. We need to translate the
			// mouse width in
			// model coordinates...
			PrecisionRectangle rect = new PrecisionRectangle(new Rectangle(0, 0, moveDelta.x, moveDelta.y));
			getHostFigure().translateToRelative(rect);
			moveDelta.x = rect.width;
			moveDelta.y = rect.height;

			JRBand b = ((BandEditPart) getHost()).getBand();
			JasperDesign jd = ((BandEditPart) getHost()).getJasperDesign();

			/*
			 * if (!reversOrder && b.getHeight() == 0) { // Look for the right band... List<JRBand> bands =
			 * ModelUtils.getBands(jd); JRBand rightBand = bands.get(0); for (JRBand tmpBand : bands) { if (tmpBand == b)
			 * break; if (tmpBand.getHeight() == 0) continue; rightBand = tmpBand; } b = rightBand; }
			 */

			// y must be between the bottom of the previous band and max design height
			// +
			// band height + current band height

			// int bLocation = ModelUtils.getBandLocation(b, jd);
			int maxDelta = ModelUtils.getMaxBandHeight((JRDesignBand) b, jd) - b.getHeight();

			feedbackFigure.setForegroundColor(ColorConstants.black);

			if (b.getHeight() + moveDelta.y <= 0) {
				System.out.println(moveDelta.y + " set to" + (-b.getHeight())); //$NON-NLS-1$

				moveDelta.y = -b.getHeight();
				feedbackFigure.setForegroundColor(ColorConstants.darkGray);
			} else if (moveDelta.y > maxDelta) {
				// moveDelta.x = 0;
				moveDelta.y = maxDelta;
				feedbackFigure.setForegroundColor(ColorConstants.red);
			}

			((BandMoveFeedbackFigure) feedbackFigure).setBandHeight(b.getHeight() + moveDelta.y);
			// Convert the delta size to scene size...
			// rect = new PrecisionRectangle(new Rectangle(0, 0, moveDelta.x, moveDelta.y));
			// getHostFigure().translateToAbsolute(rect);
			// moveDelta.x = rect.width;
			// moveDelta.y = rect.height;

			// request.setMoveDelta(moveDelta);

			// ensureVisibility(request.getLocation());

			// request.setSizeDelta(new Dimension(0, 0));

			// feedbackFigure.setLocation(new Point(feedbackFigure.getBounds().x,
			// request.getMoveDelta().y + originalYLocation));
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#showSourceFeedback(Request)
	 */
	public void showSourceFeedback(Request request) {
		if (REQ_MOVE.equals(request.getType()))
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
		if (REQ_RESIZE.equals(request.getType()))
			showChangeBoundsFeedback((ChangeBoundsRequest) request);
	}

	/**
	 * Erase change bounds feedback.
	 * 
	 * @param request
	 *          the request
	 */
	protected void eraseChangeBoundsFeedback(ChangeBoundsRequest request) {
		if (feedback != null) {
			removeFeedback(feedback);
		}
		feedback = null;
//		originalYLocation = 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#eraseSourceFeedback(Request)
	 */
	public void eraseSourceFeedback(Request request) {
		if (REQ_MOVE.equals(request.getType()))
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
		if (REQ_RESIZE.equals(request.getType()))
			eraseChangeBoundsFeedback((ChangeBoundsRequest) request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.EditPolicy#getCommand(Request)
	 */
	public Command getCommand(Request request) {
		Command command = null;
		if (REQ_MOVE.equals(request.getType())) {
			// command = getResizeCommand((ChangeBoundsRequest) request);
		} else if (REQ_RESIZE.equals(request.getType()))
			command = getResizeCommand((ChangeBoundsRequest) request);
		return command;
	}

	/**
	 * Gets the resize command.
	 * 
	 * @param request
	 *          the request
	 * @return the resize command
	 */
	protected Command getResizeCommand(ChangeBoundsRequest request) {
		if (request.getSizeDelta().height != 0
				&& (request.getResizeDirection() == PositionConstants.SOUTH || request.getResizeDirection() == PositionConstants.NORTH)) {
			MBand mBand = (MBand) getHost().getModel();
			if (request.getResizeDirection() == PositionConstants.NORTH) {
				// change band upper
				int pos = mBand.getParent().getChildren().indexOf(mBand);
				for (int i = pos - 1; i > 0; i--) {
					INode pBand = mBand.getParent().getChildren().get(i);
					if (pBand instanceof MBand && pBand.getValue() != null) {
						mBand = (MBand) pBand;
						break;
					}
				}
				request.getSizeDelta().height = -request.getSizeDelta().height;
			}
			JRDesignBand jrdesign = (JRDesignBand) mBand.getValue();

			PrecisionRectangle deltaRect = new PrecisionRectangle(new Rectangle(0, 0, request.getSizeDelta().width,
					request.getSizeDelta().height));
			getHostFigure().translateToRelative(deltaRect);
			int delta = deltaRect.height;

			int height = jrdesign.getHeight() + delta;
			if (height < 0)
				height = 0;

			SetValueCommand setCommand = new SetValueCommand();
			setCommand.setTarget(mBand);
			setCommand.setPropertyId(JRDesignBand.PROPERTY_HEIGHT);
			setCommand.setPropertyValue(height);
			return setCommand;
		}
		return null;
	}

	/**
	 * Sets the handle.
	 * 
	 * @param handle
	 *          the new handle
	 */
	private void setHandle(IFigure handle) {
		this.handle = handle;
	}

	/**
	 * Gets the handle.
	 * 
	 * @return the handle
	 */
	private IFigure getHandle() {
		return handle;
	}

	@Override
	protected void hideSelection() {

	}

	@Override
	protected void showSelection() {

	}

}
