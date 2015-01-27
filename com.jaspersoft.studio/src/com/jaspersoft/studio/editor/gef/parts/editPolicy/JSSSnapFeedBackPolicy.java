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
package com.jaspersoft.studio.editor.gef.parts.editPolicy;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FigureUtilities;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.editpolicies.GraphicalEditPolicy;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;

public class JSSSnapFeedBackPolicy extends GraphicalEditPolicy {
	IFigure guide[] = new IFigure[6];
	Integer location[] = new Integer[6];

	/**
	 * @see org.eclipse.gef.EditPolicy#eraseTargetFeedback(org.eclipse.gef.Request)
	 */
	public void eraseTargetFeedback(Request request) {
		for (int i = 0; i < guide.length; i++) {
			if (guide[i] != null)
				removeFeedback(guide[i]);
			guide[i] = null;
			location[i] = null;
		}
	}

	static class FadeIn extends Figure {
		int opacity = 0;
		static final int FRAMES = 6;
		Image image;
		static int count;

		FadeIn(Color bg) {
			setForegroundColor(bg);
			super.setOpaque(true);
		}

		private Color createMixedColor() {
			return FigureUtilities.mixColors(getLocalBackgroundColor(), getParent().getBackgroundColor(), (double) opacity
					/ FRAMES);
		}

		/**
		 * @see org.eclipse.draw2d.Figure#paintFigure(org.eclipse.draw2d.Graphics)
		 */
		protected void paintFigure(Graphics graphics) {
			/*if (opacity != FRAMES) {
				if (image != null) {
					image.dispose();
					count--;
					image = null;
				}
				if (opacity != FRAMES - 1) {
					Display display = Display.getCurrent();

					PaletteData pData = new PaletteData(0xFF00, 0xFF00, 0xFF);
					System.out.println(pData.isDirect);
					Color localBackgroundColor = createMixedColor();
					int fillColor = pData.getPixel(ColorConstants.orange.getRGB());
					int whiteColor = pData.getPixel(ColorConstants.white.getRGB());
					localBackgroundColor.dispose();
					ImageData iData = new ImageData(6, 1, 24, pData);
					iData.setPixel(0, 0, whiteColor);
					iData.setPixel(1, 0, whiteColor);
					iData.setPixel(2, 0, whiteColor);
					iData.setPixel(3, 0, whiteColor);
					iData.setPixel(4, 0, whiteColor);
					iData.setPixel(5, 0, whiteColor);
					iData.setAlpha(0, 0, 255);
					iData.setAlpha(1, 0, 255);
					iData.setAlpha(2, 0, 255);
					iData.setAlpha(3, 0, 255);
					iData.setAlpha(4, 0, 255);
					image = new Image(display, iData);
					count++;
				}
				Display.getCurrent().timerExec(0, new Runnable() {
					public void run() {
						opacity = Math.min(FRAMES, opacity + 1);
						repaint();
					}
				});
			}
			Rectangle r = getBounds();
			if (image != null)
				System.out.println("ciao");
				//graphics.drawImage(image, 0, 0, 5, 1, r.x, r.y, r.width, r.height);
			else {
				//super.paintFigure(graphics);*/
				graphics.setForegroundColor(getForegroundColor());
				graphics.setLineStyle(SWT.LINE_DOT);
				graphics.drawRectangle(getBounds());
			//}
		}

		/**
		 * @see org.eclipse.draw2d.Figure#removeNotify()
		 */
		public void removeNotify() {
			if (image != null) {
				image.dispose();
				count--;
				image = null;
			}
		}
	}

	// Even offset indicates a vertical feedback line; odd, horizontal.
	void highlightGuide(Integer pos, Color color, int offset) {
		//System.out.println(pos+ " " + offset);
		if (pos == null) {
			if (guide[offset] != null) {
				removeFeedback(guide[offset]);
				guide[offset] = null;
			}
			location[offset] = pos;
			return;
		}

		// pos is an integer relative to target's client area.
		// translate pos to absolute, and then make it relative to fig.
		int position = pos.intValue();
		PrecisionPoint loc = new PrecisionPoint(position, position);
		IFigure contentPane = ((GraphicalEditPart) getHost()).getContentPane();
		contentPane.translateToParent(loc);
		contentPane.translateToAbsolute(loc);

		if (location[offset] == null || !location[offset].equals(pos)) {
			location[offset] = pos;
			if (guide[offset] != null) {
				removeFeedback(guide[offset]);
				guide[offset] = null;
			}

			IFigure fig = new FadeIn(color);
			guide[offset] = fig;
			addFeedback(fig);
		fig.translateToRelative(loc);
			position = offset % 2 == 0 ? (int) Math.round(loc.preciseX()) : (int) Math.round(loc.preciseY());
			Rectangle figBounds = getFeedbackLayer().getBounds().getCopy();
			if ((offset % 2) == 1) {
				figBounds.height = 1;
				figBounds.y = position;
			} else {
				figBounds.x = position;
				figBounds.width = 1;
			}
			fig.setBounds(figBounds);
		} else {
			// The feedback layer could have grown (if auto-scrolling), so
			// resize the fade-in
			// line.
			IFigure fig = guide[offset];
			Rectangle figBounds = fig.getBounds().getCopy();
			Rectangle feedbackBounds = getFeedbackLayer().getBounds();
			if ((offset % 2) == 1) {
				figBounds.x = feedbackBounds.x;
				figBounds.width = feedbackBounds.width;
			} else {
				figBounds.y = feedbackBounds.y;
				figBounds.height = feedbackBounds.height;
			}
			fig.setBounds(figBounds);
		}
	}

	/**
	 * @see org.eclipse.gef.EditPolicy#showTargetFeedback(org.eclipse.gef.Request)
	 */
	public void showTargetFeedback(Request req) {
		if (req.getType() == null) return;
		if (req.getType().equals(REQ_MOVE) || req.getType().equals(REQ_RESIZE) || req.getType().equals(REQ_CLONE)
				|| req.getType().equals(REQ_ADD) || req.getType().equals(REQ_CREATE)) {

			Integer value;
			//System.out.println(req.getClass().toString()+ " " + req.getType().toString());
			value = (Integer) req.getExtendedData().get(SnapToGeometry.KEY_WEST_ANCHOR);
			highlightGuide(value, ColorConstants.blue, 0);

			value = (Integer) req.getExtendedData().get(SnapToGeometry.KEY_NORTH_ANCHOR);
			highlightGuide(value, ColorConstants.blue, 1);

			value = (Integer) req.getExtendedData().get(SnapToGeometry.KEY_EAST_ANCHOR);
			highlightGuide(value, ColorConstants.blue, 2);

			value = (Integer) req.getExtendedData().get(SnapToGeometry.KEY_SOUTH_ANCHOR);
			highlightGuide(value, ColorConstants.blue, 3);

			value = (Integer) req.getExtendedData().get(SnapToGuides.KEY_VERTICAL_GUIDE);
			highlightGuide(value, ColorConstants.blue, 4);

			value = (Integer) req.getExtendedData().get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
			highlightGuide(value, ColorConstants.blue, 5);
		}
	}

}
