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

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.TexturePaint;
import java.awt.image.BufferedImage;
import java.text.MessageFormat;

import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.wb.swt.SWTResourceManager;

import com.jaspersoft.studio.editor.gef.decorator.pdf.PDFDecorator;
import com.jaspersoft.studio.editor.gef.texture.EmptyTexture;
import com.jaspersoft.studio.editor.gef.util.FigureTextWriter;
import com.jaspersoft.studio.editor.java2d.J2DUtils;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.band.MBand;

/*
 * The Class BandFigure.
 */
public class BandFigure extends RectangleFigure {
	
	public Color marginsColor = SWTResourceManager.getColor(170, 168, 255);
	
	/**
	 * Model of the band 
	 */
	private MBand bandModel;
	private int columnNumber = 1;
	private int columnSpacing = 0;
	private int columnWidth = 0;

	private int marginLeft = 0;
	private int marginRight = 0;

	private boolean drawColumn = false;
	
	/**
	 * Textual name of the band when the content is visible
	 */
	private String bandText;
	
	/**
	 * Name of the band when the content is hidden
	 */
	private String hiddenText;
	
	/**
	 * Paint used to color the restricted area
	 */
	private static TexturePaint restrictedAreaTexture = null; 

	private FigureTextWriter twriter = new FigureTextWriter();

	public Color getMarginsColor() {
		return marginsColor;
	}

	public void setMarginsColor(Color marginsColor) {
		this.marginsColor = marginsColor;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}

	public int getColumnSpacing() {
		return columnSpacing;
	}

	public void setColumnSpacing(int columnSpacing) {
		this.columnSpacing = columnSpacing;
	}

	public int getColumnWidth() {
		return columnWidth;
	}

	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
	}

	public int getMarginLeft() {
		return marginLeft;
	}

	public void setMarginLeft(int marginLeft) {
		this.marginLeft = marginLeft;
	}

	public int getMarginRight() {
		return marginRight;
	}

	public void setMarginRight(int marginRight) {
		this.marginRight = marginRight;
	}

	/**
	 * Instantiates a new band figure.
	 */
	public BandFigure(boolean drawColumn, MBand model) {
		super();
		setLayoutManager(new FreeformLayout());
		setOpaque(false);
		this.drawColumn = drawColumn;
		this.bandModel = model;
		createTexture();
	}
	
	/**
	 * Method used to get the paint for the restricted area (all columns after the first). The paint is 
	 * cached in a static variable. If the variable is null the the paint has to be loaded from an image 
	 * file 
	 * @return Paint used to color a restricted area (all columns after the first)
	 */
  private  static TexturePaint getRestrictedAreaTexture()
  {
      if ( restrictedAreaTexture == null )
      {
              Image img2 = (new javax.swing.ImageIcon(PDFDecorator.class.getResource("/icons/resources/restricted_area.png"))).getImage(); //$NON-NLS-1$
              BufferedImage img = new BufferedImage(14, 14, BufferedImage.TYPE_INT_ARGB);
              img.getGraphics().drawImage(img2, 0, 0, null);
              restrictedAreaTexture = new TexturePaint( img, new java.awt.Rectangle(0,0, 14, 14) );
      }
      return restrictedAreaTexture;
  }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Figure#paint(org.eclipse.draw2d.Graphics)
	 */
	public void paint(Graphics graphics) {
		Rectangle b = getBounds();
		graphics.setForegroundColor(marginsColor);
		graphics.setBackgroundColor(marginsColor);

		graphics.setAlpha(128);
		Graphics2D g = ComponentFigure.getG2D(graphics);
		if (g != null) {
			Stroke oldStroke = g.getStroke();
			g.setStroke(J2DUtils.getInvertedZoomedStroke(oldStroke, graphics.getAbsoluteScale()));

			g.drawLine(b.x, b.y, b.x + b.width, b.y);
			g.drawLine(b.x, b.y + b.height - 1, b.x + b.width, b.y + b.height - 1);

			//Change the text if the band is visible or not
			if (!bandModel.isVisible()){
				g.drawLine(b.x, b.y, b.x + b.width, b.y+b.height);
				g.drawLine(b.x + b.width , b.y , b.x, b.y+b.height);
				twriter.setText(hiddenText);
			} else {
				twriter.setText(bandText);
			}
			
			twriter.painText(g, this);

			if (drawColumn) {
				int x = marginLeft + ReportPageFigure.PAGE_BORDER.left;
				for (int i = 0; i < columnNumber; i++) {
					if (i > 0){
						g.drawLine(x, b.y, x, b.y + b.height + 1);
						//Color the restricted area
						Paint oldPaint = g.getPaint();
						g.setPaint(getRestrictedAreaTexture());
						g.fillRect(x, b.y, columnWidth, b.y + b.height + 1);
						g.setPaint(oldPaint);
					}

					x += columnWidth;
					if (i < columnNumber - 1) {
						Paint p = g.getPaint();
						g.setPaint(tp);
						g.fillRect(x, b.y, columnSpacing, b.y + b.height + 1);
						g.setPaint(p);

						g.drawLine(x, b.y, x, b.y + b.height + 1);
					}
					x += columnSpacing;
				}
			}

			g.setStroke(oldStroke);
		} else
			graphics.drawLine(b.x, b.y + b.height - 1, b.x + b.width, b.y + b.height - 1);

	}

	/**
	 * Enables/disables the showing of the band name in background.
	 * 
	 * @param showBandName
	 *          flag for band name showing.
	 */
	public void setShowBandName(boolean showBandName) {
		twriter.setShowName(showBandName);
	}

	/**
	 * Sets a human-readable text that will be painted in the band background. Usually it is the band name.
	 * <p>
	 * 
	 * <b>NOTE</b>: the text will be drawn only if the related property <i>"Show Band names"</i> from the preference page
	 * <i>Jaspersoft Studio-&gt;Report Designer</i> is enabled.
	 * 
	 * @param bandText
	 *          the band text
	 */
	public void setBandText(String bandText) {
		this.bandText = bandText;
		this.hiddenText = MessageFormat.format(Messages.BandFigure_hiddenFiguretext, new Object[]{bandText});
		twriter.setText(bandText);
	}

	private TexturePaint tp;

	public TexturePaint createTexture() {
		if (tp == null)
			tp = EmptyTexture.createTexture(null, null);
		return tp;
	}
}
