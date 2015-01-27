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
package com.jaspersoft.studio.editor.gef.figures.borders;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.gef.handles.HandleBounds;

import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.figures.ReportPageFigure;
import com.jaspersoft.studio.editor.gef.figures.util.RoundGradientPaint;

/*
 * The Class ShadowBorder.
 */
public class ShadowBorder extends AbstractBorder {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#getInsets(org.eclipse.draw2d.IFigure)
	 */
	public Insets getInsets(IFigure figure) {
		return new Insets(ReportPageFigure.PAGE_BORDER.top);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Border#paint(org.eclipse.draw2d.IFigure, org.eclipse.draw2d.Graphics,
	 * org.eclipse.draw2d.geometry.Insets)
	 */
	public void paint(IFigure figure, Graphics graphics, Insets insets) {

		Graphics2D g = ComponentFigure.getG2D(graphics);
		if (g != null) {
			org.eclipse.draw2d.geometry.Rectangle bounds = figure.getBounds();
			if (figure instanceof HandleBounds)
				bounds = ((HandleBounds) figure).getHandleBounds();

			paintShadowBorder(g, bounds.x - insets.left, bounds.y - insets.top, bounds.width + insets.right + insets.left,
					bounds.height + insets.top + insets.bottom);
		}

	}

	/**
	 * Paint shadow border.
	 * 
	 * @param g
	 *          the g
	 * @param x
	 *          the x
	 * @param y
	 *          the y
	 * @param width
	 *          the width
	 * @param height
	 *          the height
	 */
	private void paintShadowBorder(Graphics2D g, int x, int y, int width, int height) {

		// TOP ______________________________________________
		Rectangle2D r = new Rectangle2D.Double(x + 10, y, width - 20, 10);
		GradientPaint gp = new GradientPaint(0f, (float) (y + 2), new Color(0, 0, 0, 0), 0f, (float) (y + 9.5), new Color(
				0, 0, 0, 60)); //

		g.setPaint(gp);
		g.fill(r);

		// BOTTOM ______________________________________________
		r = new Rectangle2D.Double(x + 10, y + height - 10, width - 20, 10);
		gp = new GradientPaint(0f, (float) (r.getY()), new Color(0, 0, 0, 60), 0f, (float) (r.getY() + 7.5), new Color(0,
				0, 0, 0)); //
		g.setPaint(gp);
		g.fill(r);

		// LEFT ______________________________________________
		r = new Rectangle2D.Double(x, y + 10, 10, height - 20);
		gp = new GradientPaint((float) (r.getX() + 2), 0f, new Color(0, 0, 0, 0), (float) (r.getX() + 9.5), 0f, new Color(
				0, 0, 0, 60)); //
		g.setPaint(gp);
		g.fill(r);

		// RIGHT ______________________________________________
		r = new Rectangle2D.Double(x + width - 10, y + 10, 10, height - 20);
		gp = new GradientPaint((float) (r.getX()), 0f, new Color(0, 0, 0, 60), (float) (r.getX() + 7.5), 0f, new Color(0,
				0, 0, 0)); //
		g.setPaint(gp);
		g.fill(r);

		// TOP LEFT ______________________________________________
		r = new Rectangle2D.Double(x, y, 10, 10);
		// float[] dist = { 0f, 0.95f };
		// Color[] colors = { new Color(0, 0, 0, 60), new Color(0, 0, 0, 0) };
		// RadialGradientPaint radgp = new RadialGradientPaint(new Point2D.Float(x + 10, x + 10), 10f, dist, colors);
		RoundGradientPaint rgp = new RoundGradientPaint(x + 9.5, y + 9.5f, new Color(0, 0, 0, 60), new Point2D.Float(0,
				6.5f), new Color(0, 0, 0, 0));

		g.setPaint(rgp);
		g.fill(r);

		// TOP RIGHT ______________________________________________
		r = new Rectangle2D.Double(x + width - 10, y, 10, 10);
		rgp = new RoundGradientPaint(r.getX() + 0.5, r.getY() + 9.5f, new Color(0, 0, 0, 60), new Point2D.Float(0, 6.5f),
				new Color(0, 0, 0, 0));

		g.setPaint(rgp);
		g.fill(r);

		// BOTTOM RIGHT ______________________________________________
		r = new Rectangle2D.Double(x + width - 10, y + height - 10, 10, 10);
		rgp = new RoundGradientPaint(r.getX() + 0.5, r.getY() + 0.5f, new Color(0, 0, 0, 60), new Point2D.Float(0, 6.5f),
				new Color(0, 0, 0, 0));

		g.setPaint(rgp);
		g.fill(r);

		r = new Rectangle2D.Double(x, y + height - 10, 10, 10);
		rgp = new RoundGradientPaint(r.getX() + 9.5, r.getY() + 0.5f, new Color(0, 0, 0, 60), new Point2D.Float(0, 6.5f),
				new Color(0, 0, 0, 0));

		g.setPaint(rgp);
		g.fill(r);

		// ((Graphics2D)g).setPaint(Color.RED);
		// ((Graphics2D)g).draw(r);
	}

}
