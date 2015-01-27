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
package org.eclipse.nebula.widgets.gallery;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.wb.swt.ResourceManager;

/**
 * Custom renderer for a gallery item.
 * It display a rounded selection border on the image when the specified
 * item is selected.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class RoundedGalleryItemRenderer extends DefaultGalleryItemRenderer {

	public RoundedGalleryItemRenderer() {
		// Set defaults
		foregroundColor = Display.getDefault().getSystemColor(
				SWT.COLOR_BLACK);
		backgroundColor = Display.getDefault().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND);

		selectionForegroundColor = Display.getDefault().getSystemColor(
				SWT.COLOR_BLACK);
		selectionBackgroundColor = Display.getDefault().getSystemColor(
				SWT.COLOR_LIST_BACKGROUND);

		// Create drop shadows
		createColors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.nebula.widgets.gallery.AbstractGalleryItemRenderer#draw(org
	 * .eclipse.swt.graphics.GC, org.eclipse.nebula.widgets.gallery.GalleryItem,
	 * int, int, int, int, int)
	 */
	public void draw(GC gc, GalleryItem item, int index, int x, int y,
			int width, int height) {
		_drawImage = item.getImage();
		_drawForegroundColor = getForeground(item);

		// Set up the GC
		gc.setFont(getFont(item));

		// Create some room for the label.
		int useableHeight = height;
		int fontHeight = 0;
		if (item.getText() != null && !EMPTY_STRING.equals(item.getText())
				&& this.showLabels) {
			fontHeight = gc.getFontMetrics().getHeight();
			useableHeight -= fontHeight + 2;
		}

		int imageWidth = 0;
		int imageHeight = 0;
		int xShift = 0;
		int yShift = 0;
		Point size = null;

		if (_drawImage != null) {
			Rectangle itemImageBounds = _drawImage.getBounds();
			imageWidth = itemImageBounds.width;
			imageHeight = itemImageBounds.height;

			size = RendererHelper.getBestSize(imageWidth, imageHeight, width
					- 8 - 2 * this.dropShadowsSize, useableHeight - 8 - 2
					* this.dropShadowsSize);

			xShift = RendererHelper.getShift(width, size.x);
			yShift = Math.max(useableHeight - size.y,0);

			if (dropShadows) {
				Color c = null;
				for (int i = this.dropShadowsSize - 1; i >= 0; i--) {
					c =  dropShadowsColors.get(i);
					gc.setForeground(c);

					gc.drawLine(x + width + i - xShift - 1, y + dropShadowsSize
							+ yShift, x + width + i - xShift - 1, y
							+ useableHeight + i - yShift);
					gc.drawLine(x + xShift + dropShadowsSize, y + useableHeight
							+ i - yShift - 1, x + width + i - xShift, y - 1
							+ useableHeight + i - yShift);
				}
			}
		}

		// Draw background (rounded rectangles)

		// Checks if background has to be drawn
		_drawBackground = selected;
		_drawBackgroundColor = null;
		if (!_drawBackground && item.getBackground(true) != null) {
			_drawBackgroundColor = getBackground(item);

			if (!RendererHelper.isColorsEquals(_drawBackgroundColor,
					galleryBackgroundColor)) {
				_drawBackground = true;
			}
		}

		if (_drawBackground) {
			// Set colors
			if (selected) {
				gc.setBackground(selectionBackgroundColor);
				gc.setForeground(selectionBackgroundColor);
			} else if (_drawBackgroundColor != null) {
				gc.setBackground(_drawBackgroundColor);
			}

			// Draw
			if (showRoundedSelectionCorners) {
				gc.fillRoundRectangle(x, y, width, useableHeight,
						selectionRadius, selectionRadius);
			} else {
				gc.fillRectangle(x, y, width, height);
			}

			if (item.getText() != null && !EMPTY_STRING.equals(item.getText())
					&& showLabels) {
				gc.fillRoundRectangle(x, y + height - fontHeight, width,
						fontHeight, selectionRadius, selectionRadius);
			}
		}

		// Draw image
		if(selected){
			_drawImage=item.getSelectedImage();
		}
		if (_drawImage != null && size != null) {
			if (size.x > 0 && size.y > 0) {
				gc.drawImage(_drawImage, 0, 0, imageWidth, imageHeight, x
						+ xShift, y + yShift, size.x, size.y);
				drawAllOverlays(gc, item, x, y, size, xShift, yShift);
				if(selected){
					gc.setForeground(ResourceManager.getColor(SWT.COLOR_LIST_SELECTION));
					gc.setLineWidth(5);
					gc.drawRoundRectangle(x + xShift, y + yShift, size.x, size.y, 10, 10);
				}
			}

		}

		// Draw label
		if (item.getText() != null && !EMPTY_STRING.equals(item.getText())
				&& showLabels) {
			// Set colors
			if (selected) {
				// Selected : use selection colors.
				gc.setForeground(selectionForegroundColor);
				gc.setBackground(selectionBackgroundColor);
			} else {
				// Not selected, use item values or defaults.

				// Background
				if (_drawBackgroundColor != null) {
					gc.setBackground(_drawBackgroundColor);
				} else {
					gc.setBackground(backgroundColor);
				}

				// Foreground
				if (_drawForegroundColor != null) {
					gc.setForeground(_drawForegroundColor);
				} else {
					gc.setForeground(foregroundColor);
				}
			}

			// Create label
			String text = RendererHelper.createLabel(item.getText(), gc,
					width - 10);

			// Center text
			int textWidth = gc.textExtent(text).x;
			int textxShift = RendererHelper.getShift(width, textWidth);

			// Draw
			gc.drawText(text, x + textxShift, y + height - fontHeight, true);
		}
	}

	@Override
	public Font getFont(GalleryItem item) {
		Font baseFont = super.getFont(item);
		FontData fontData = baseFont.getFontData()[0];
		Font itemFont = ResourceManager.getFont(fontData.getName(), fontData.getHeight(), fontData.getStyle());
		
		// If we are rendering the selected item, we return a bold version of the font...
		return selected ? ResourceManager.getBoldFont(itemFont) : itemFont;
	}
	
}
