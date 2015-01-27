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
package com.jaspersoft.studio.editor.gef.figures;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import net.sf.jasperreports.engine.base.JRBaseReport;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

import com.jaspersoft.studio.editor.gef.parts.PageEditPart;
import com.jaspersoft.studio.editor.java2d.J2DUtils;
import com.jaspersoft.studio.model.ANode;

/*
 * The Class PageFigure.
 * 
 * @author Chicu Veaceslav
 */
public class ReportPageFigure extends APageFigure {

	/** The bands height. */
	private int bandsHeight = 0;
	protected JRBaseReport jrDesign = null;
	/** The bands vertical lines color */
	protected Color printMarginColor = new Color(170, 168, 255);

	public void setPrintMarginColor(Color printMarginColor) {
		this.printMarginColor = printMarginColor;
	}

	/**
	 * Instantiates a new page figure.
	 * 
	 * @param jd the jasper design
	 * @param viewMargins if the margins are visible
	 * @param page the parent page
	 */
	public ReportPageFigure(JRBaseReport jd, boolean viewMargins, PageEditPart page) {
		super(viewMargins, page);
		this.jrDesign = jd;
	}

	/**
	 * Sets the bands height.
	 * 
	 * @param bandsHeight
	 *          the new bands height
	 */
	public void setBandsHeight(int bandsHeight) {
		this.bandsHeight = bandsHeight;
	}

	/*
	private JrxmlEditor editor = null;
	
	protected boolean isMainEditor(){
		if (editor == null){
			IEditorPart editor = SelectionHelper.getActiveJRXMLEditor();
			
			if (editor instanceof JrxmlEditor) {
				this.editor = (JrxmlEditor)editor; 
				
				if (page.getJasperDesign() == this.editor.getReportContainer().getModel().getJasperDesign()){
					this.editor = null;
				}
			}
		}
		if (editor != null){
			return (editor.getActivePage() == JrxmlEditor.PAGE_DESIGNER && editor.getReportContainer().getActivePage() == 0);
		}
		return true;
	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Shape#paintFigure(org.eclipse.draw2d.Graphics)
	 */
	@Override
	public void paintFigure(Graphics g) {
		if (jrDesign != null && viewMargins) {
			Rectangle clientArea = getClientArea();
			clientArea.x -= dx;
			clientArea.y -= dy;

			int pageWidth = jrDesign.getPageWidth();
			int pageHeight = bandsHeight;// + jrDesign.getTopMargin() + jrDesign.getBottomMargin();

			int leftMargin = jrDesign.getLeftMargin();
			int rightMargin = jrDesign.getRightMargin();
			// int topMargin = jrDesign.getTopMargin();
			// int bottomMargin = jrDesign.getBottomMargin();

			Rectangle rectangle = new Rectangle(clientArea.x, clientArea.y, pageWidth, pageHeight);
			g.setBackgroundColor(pageBackground);
			g.fillRectangle(rectangle);

			Point topLeft = new Point(clientArea.x + leftMargin, clientArea.y);
			Point topRight = new Point(clientArea.x + pageWidth - rightMargin, clientArea.y);

			Point bottomLeft = new Point(topLeft.x, clientArea.y + pageHeight);
			Point bottomRight = new Point(topRight.x, clientArea.y + pageHeight);
			Graphics2D graphics2d = ComponentFigure.getG2D(g);
			if (graphics2d != null) {
				Stroke oldStroke = graphics2d.getStroke();

				paintGrid(g, rectangle);

				graphics2d.setColor(printMarginColor);

				graphics2d.setStroke(new BasicStroke(0.5f));
				graphics2d.setStroke(J2DUtils.getInvertedZoomedStroke(graphics2d.getStroke(), g.getAbsoluteScale()));

				// g.drawLine(clientArea.x, clientArea.y + topMargin, clientArea.x + pageWidth, clientArea.y + topMargin);
				g.drawLine(topLeft.x, topLeft.y, bottomLeft.x, bottomLeft.y);
				g.drawLine(topRight.x, topRight.y, bottomRight.x, bottomRight.y);
				graphics2d.setStroke(oldStroke);
			}
		}
		if (getBorder() != null)
			getBorder().paint(this, g, NO_INSETS);
	}
	
	/**
	 * Override the original paintChildren to avoid to pain elements 
	 * that are marked as not visible inside the model
	 */
	protected void paintChildren(Graphics graphics) {
		//if (!isMainEditor()) return;
			
		for (int i = 0; i < getChildren().size(); i++) {
			IFigure child = (IFigure) getChildren().get(i);
			boolean modelVisible = true;
			if (child instanceof FrameFigure){
				ANode model = ((FrameFigure)child).getModel();
				if (model != null) {
					modelVisible = model.isVisible();
					child.setVisible(modelVisible);
				}
			}
			if (child.isVisible() && modelVisible && isFigurevisible(child)) {
				// determine clipping areas for child
				Rectangle[] clipping = null;
				if (getClippingStrategy() != null) {
					clipping = getClippingStrategy().getClip(child);
				} else {
					// default clipping behavior is to clip at bounds
					clipping = new Rectangle[] { child.getBounds() };
				}
				// child may now paint inside the clipping areas
				for (int j = 0; j < clipping.length; j++) {
					if (clipping[j].intersects(graphics.getClip(Rectangle.SINGLETON))) {
						graphics.clipRect(clipping[j]);
						child.paint(graphics);
						graphics.restoreState();
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.handles.HandleBounds#getHandleBounds()
	 */
	public Rectangle getHandleBounds() {
		Rectangle clientArea = getClientArea();
		clientArea.x -= dx;
		clientArea.y -= dy;

		int pageWidth = jrDesign.getPageWidth();
		int pageHeight = bandsHeight;// + jrDesign.getTopMargin() + jrDesign.getBottomMargin();
		Insets insets = getInsets();
		return new Rectangle(clientArea.x - insets.right, clientArea.y - insets.top,
				pageWidth + insets.left + insets.right, pageHeight + insets.top + insets.bottom);
	}

}
