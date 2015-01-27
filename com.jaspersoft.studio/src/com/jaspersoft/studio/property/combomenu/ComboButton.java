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
package com.jaspersoft.studio.property.combomenu;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.core.runtime.SafeRunner;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.util.SafeRunnable;
import org.eclipse.jface.util.Util;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.HelpListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.wb.swt.ResourceManager;

import com.jaspersoft.studio.help.HelpSystem;

public class ComboButton extends Viewer {

	/**
	 * Style bit: Create control with default behaviours, i.e. showing text, showing image.
	 */
	public static final int NORMAL = 0;

	/**
	 * Style bit: Don't show text.
	 */
	public static final int NO_TEXT = 1;

	/**
	 * Style bit: Don't show image.
	 */
	public static final int NO_IMAGE = 1 << 1;

	/**
	 * Style bit: Don't show spinner arrow.
	 */
	public static final int NO_ARROWS = 1 << 2;

	private static final boolean DRAWS_FOCUS = Util.isMac();

	protected static final int MARGIN = DRAWS_FOCUS ? 4 : 1;
	protected static final int CORNER_SIZE = 5;
	protected static final int BORDER = (CORNER_SIZE + 1) / 2;
	protected static final int FOCUS_CORNER_SIZE = CORNER_SIZE - 2;
	protected static final int FOCUS_BORDER = (FOCUS_CORNER_SIZE + 1) / 2;
	protected static final int IMAGE_TEXT_SPACING = 3;
	protected static final int CONTENT_ARROW_SPACING = 4;
	protected static final int ARROW_WIDTH = 7;
	protected static final int ARROW_HEIGHT = 4;
	protected static final int ARROWS_SPACING = 2;
	protected static final String ELLIPSIS = "..."; //$NON-NLS-1$

	/**
	 * Composite where the control will be placed
	 */
	private Composite control;

	/**
	 * Style bit for the control
	 */
	private int style;

	/**
	 * Text displayed in the combo button
	 */
	private String text = null;

	/**
	 * Image displayed in the combo button
	 */
	private Image image = null;

	/**
	 * Color of the text foreground
	 */
	private Color textForeground = null;

	/**
	 * Color og the text background
	 */
	private Color textBackground = null;

	/**
	 * Flag to check if the mouse pointer is over the control
	 */
	private boolean hovered = false;

	/**
	 * Flag to check if the mouse pointer is out of the control
	 */
	private boolean pressed = false;

	/**
	 * Size of the text
	 */
	private Point textSize = null;

	/**
	 * Size of the image
	 */
	private Point imageSize = null;

	/**
	 * Listeners associated to the click event
	 */
	private List<IOpenListener> openListeners = null;
	
	private IMenuProvider menuProvider;
	
	/**
	 * Caches:
	 */
	private Point cachedTextSize = null;
	private Point cachedImageSize = null;
	private String appliedText = null;
	private Rectangle bounds = null;
	private Rectangle contentArea = null;
	private Point arrowLoc = null;
	private Rectangle imgArea = null;
	private Rectangle textArea = null;

	
	/**
	 * Canvas that appear like a button
	 * 
	 * @author Orlandin Marco
	 *
	 */
	public class GraphicButton extends Canvas {
		
		/**
		 * Last help listener set for this element
		 */
		private HelpListener lastListener = null;
		
		/**
		 * Size of the maximum text size
		 */
		private Point textSize;
		
		public GraphicButton(Composite parent, String biggerString){
			super(parent, SWT.DOUBLE_BUFFERED);
			textSize = calcTextSize(biggerString.toUpperCase());
		}
		
		@Override
		public Point computeSize(int wHint, int hHint, boolean changed) {
			checkWidget();
			if (changed)
				clearCaches();
			Point imageSize = getImageSize();
			boolean hasArrows = hasArrows();

			int width;
			if (wHint != SWT.DEFAULT) {
				width = Math.max(wHint, MARGIN * 2);
			} else {
				width = MARGIN * 2 + imageSize.x + textSize.x + BORDER * 2;
				if (hasArrows) {
					width += ARROW_WIDTH + CONTENT_ARROW_SPACING;
				}
				if (imageSize.x != 0 && textSize.x != 0) {
					width += IMAGE_TEXT_SPACING;
				}
			}

			int minHeight = MARGIN * 2 + Math.max(imageSize.y, textSize.y) + BORDER * 2;
			if (hasArrows) {
				minHeight = Math.max(minHeight, ARROW_HEIGHT * 2 + ARROWS_SPACING);
			}
			int height = minHeight;
			Rectangle trim = computeTrim(0, 0, width, height);
			return new Point(trim.width, trim.height);
		}
		
		/**
		 * Substitute the original listener with one adapted to this type of element. This help will open a 
		 * browser window when used the key F1 on the element, the link is taken from the properties of this element
		 */
		@Override
		public void addHelpListener(HelpListener listener) {
			HelpProvider contextHelp = new HelpProvider(menuProvider.getMenu());
			lastListener = contextHelp.setHelp(control.getData(HelpSystem.HELP_KEY).toString());
		}

		@Override
		public void removeHelpListener(HelpListener listener) {
			if (lastListener != null){
				super.removeHelpListener(lastListener);
				lastListener = null;
			}
		}
		
	};
	
	public int getWidth(){
		int width;
		Point imageSize = getImageSize();
		Point textSize = getTextSize();
		width = MARGIN * 2 + imageSize.x + textSize.x + BORDER * 2;
		if (hasArrows()) {
			width += ARROW_WIDTH + CONTENT_ARROW_SPACING;
		}
		if (imageSize.x != 0 && textSize.x != 0) {
			width += IMAGE_TEXT_SPACING;
		}
		return width;
	}
	
	/**
	 * Constructs a new instance of this class given its parent and a style value describing its behavior and appearance.
	 * 
	 * @param parent
	 *          a composite control which will be the parent of the new instance (cannot be null)
	 * @param style
	 *          the style of control to construct
	 * 
	 * @see #NORMAL
	 * @see #NO_TEXT
	 * @see #NO_IMAGE
	 * @see #NO_ARROWS
	 * 
	 * @param biggerString
	 *          the most big string that will be represented, used to give the element a right size
	 */
	public ComboButton(Composite parent, int style, final String biggerString, final IMenuProvider menuProvider) {
		this.style = checkStyle(style, NORMAL, NORMAL, NO_TEXT, NO_IMAGE) | checkStyle(style, SWT.NONE, NO_ARROWS);
		this.menuProvider = menuProvider;
		control = new  GraphicButton(parent, biggerString);
		hookControl(control);
	}
	

	/**
	 * Hook to the control the principal events: paint, mouse up, mouse enter and exit (for hovering the control), key
	 * down, and focus in and focus out.
	 * 
	 * @param control
	 */
	protected void hookControl(Control control) {
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Paint:
					paint(event.gc, event.display);
					break;
				case SWT.Resize:
					clearCaches();
					break;
        case SWT.MouseDown:
          if (event.button == 1)
              handleMousePress();
          break;
        case SWT.MouseUp:
          if (event.button == 1)
              handleMouseRelease();
          break;
				case SWT.MouseEnter:
					handleMouseEnter();
					break;
				case SWT.MouseExit:
					handleMouseExit();
					break;
				case SWT.KeyDown:
					handleKeyPress(event);
					break;
				case SWT.FocusIn:
					handleFocusIn();
					break;
				case SWT.HELP:
					break;
				case SWT.FocusOut:
					handleFocusOut();
					break;
				default:
				}
			}
		};
		control.addListener(SWT.Paint, listener);
		control.addListener(SWT.Resize, listener);
		control.addListener(SWT.MouseDown, listener);
		control.addListener(SWT.MouseUp, listener);
		control.addListener(SWT.MouseEnter, listener);
		control.addListener(SWT.MouseExit, listener);
		control.addListener(SWT.KeyDown, listener);
		control.addListener(SWT.FocusIn, listener);
		control.addListener(SWT.FocusOut, listener);
	}


	/**
	 * Handle for the mouse press, if the menu is closed an open action will be fired for every listener, otherwise it
	 * will be considered as a focus out
	 */
	protected void handleMousePress() {
		if (!getControl().isEnabled())
			return;
			setHovered(false);
			//setPressed(true);
			fireOpen();
	}

	/**
	 * When the mouse button is released the pressed state will be updated
	 */
	protected void handleMouseRelease() {
		if (!getControl().isEnabled())
			return;
		//setPressed(false);
		setHovered(false);
	}

	/**
	 * Color the control on mouse enter
	 */
	protected void handleMouseEnter() {
		if (!getControl().isEnabled())
			return;
		setHovered(true);
	}

	/**
	 * decolor the control on mouse enter
	 */
	protected void handleMouseExit() {
		if (!getControl().isEnabled())
			return;
		setHovered(false);
	}

	/**
	 * Refresh the control when focused
	 */
	protected void handleFocusIn() {
		if (!getControl().isEnabled())
			return;
		refreshControl();
	}

	/**
	 * Refresh the control when unfocused
	 */
	protected void handleFocusOut() {
		if (!getControl().isEnabled())
			return;
		setPressed(false);
		refreshControl();
	}

	/**
	 * Actually unused
	 * 
	 * @param e
	 */
	protected void handleKeyPress(Event e) {
		if (!getControl().isEnabled())
			return;
	}

	/**
	 * Add a new listener to run when the open action is fired
	 * 
	 * @param listener
	 */
	public void addOpenListener(IOpenListener listener) {
		if (openListeners == null)
			openListeners = new ArrayList<IOpenListener>();
		openListeners.add(listener);
	}

	/**
	 * Remove a previous added listener
	 * 
	 * @param listener
	 */
	public void removeOpenListener(IOpenListener listener) {
		if (openListeners == null)
			return;
		openListeners.remove(listener);
	}

	protected void fireOpen(final OpenEvent event) {
		if (openListeners == null)
			return;
		for (final Object l : openListeners.toArray()) {
			SafeRunner.run(new SafeRunnable() {
				public void run() throws Exception {
					((IOpenListener) l).open(event);
				}
			});
		}
	}

	/**
	 * Fire the open on all the listeners
	 */
	protected void fireOpen() {
		fireOpen(new OpenEvent(this, getSelection()));
	}

	/**
	 * Return the Canvas where the control i painted
	 */
	public Control getControl() {
		return control;
	}

	/**
	 * Return the style
	 * 
	 * @return
	 */
	protected int getStyle() {
		return style;
	}

	/**
	 * Return the text into the control
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * Return the image into the button
	 * 
	 * @return
	 */
	public Image getImage() {
		return image;
	}

	/**
	 * Check if the elements has text
	 * 
	 * @return False if the text is not null or the style bit is set to NO_TEXT, true otherwise
	 */
	public boolean hasText() {
		return text != null && (style & NO_TEXT) == 0;
	}

	/**
	 * Check if the elements has and image
	 * 
	 * @return False if the image is not null or the style bit is set to NO_IMAGE, true otherwise
	 */
	public boolean hasImage() {
		return image != null && (style & NO_IMAGE) == 0;
	}

	/**
	 * Check if the elements has the arrow painted on the button
	 * 
	 * @return False if the the style bit is set to NO_ARROR, true otherwise
	 */
	protected boolean hasArrows() {
		return (style & NO_ARROWS) == 0;
	}

	/**
	 * Return the foreground color of the text
	 * 
	 * @return
	 */
	public Color getTextForeground() {
		return textForeground;
	}

	/**
	 * Return the background color of the text
	 */
	public Color getTextBackground() {
		return textBackground;
	}

	/**
	 * Set the foreground color of the text, then refresh the control
	 * 
	 * @param c
	 */
	public void setTextForeground(Color c) {
		if (c == this.textForeground || (c != null && c.equals(this.textForeground)))
			return;
		this.textForeground = c;
		refreshControl();
	}

	/**
	 * Set the background color of the text than refresh the control
	 * 
	 * @param c
	 */
	public void setTextBackground(Color c) {
		if (c == this.textBackground || (c != null && c.equals(this.textBackground)))
			return;
		this.textBackground = c;
		refreshControl();
	}

	/**
	 * Check if the control is hovered
	 * 
	 * @return true if the mouse pointer is on the control, false otherwise
	 */
	public boolean isHovered() {
		return hovered;
	}

	/**
	 * Check if the mouse button is pressed
	 * @return true if a mouse button is pressed, false otherwise
	 */
	public boolean isPressed() {
		return pressed;
	}

	/**
	 * Set the hovered state of the control, then the control will be refreshed
	 * @param hovered
	 */
	public void setHovered(boolean hovered) {
		if (hovered == this.hovered)
			return;
		this.hovered = hovered;
		refreshControl();
	}

	/**
	 * Set the pressed state of the mouse inside the control, then the 
	 * control will be refreshed
	 * @param pressed
	 */
	public void setPressed(boolean pressed) {
		if (pressed == this.pressed)
			return;
		this.pressed = pressed;
		refreshControl();
	}



	/**
	 * Set the text inside the control, then it will be refreshed, but only 
	 * if the style bit is not NO_TEXT
	 * @param text the new text
	 */
	public void setText(String text) {
		if (text == this.text || (text != null && text.equals(this.text)))
			return;
		this.text = text;
		cachedTextSize = null;
		clearCaches();
		refreshControl();
	}

	/**
	 * Set the image inside the control, then it will be refreshed, but only 
	 * if the style bit is not NO_IMAGE
	 * @param image the new image
	 */
	public void setImage(Image image) {
		if (image == this.image)
			return;
		this.image = image;
		cachedImageSize = null;
		clearCaches();
		refreshControl();
	}

	/**
	 * Set the image inside the control, then it will be refreshed, but only 
	 * if the style bit is not NO_IMAGE
	 * @param image an image descriptor of the new image
	 */
	public void setImage(ImageDescriptor imageDesc) {
		if (imageDesc != null) {
			Image image = ResourceManager.getImage(imageDesc);
			if (image == this.image)
				return;
			this.image = image;
			cachedImageSize = null;
			clearCaches();
			refreshControl();
		}
	}

	/**
	 * Return the size of the text
	 * @return
	 */
	public Point getTextSize() {
		if (textSize != null)
			return textSize;
		if (cachedTextSize == null) {
			cachedTextSize = calcTextSize();
		}
		return cachedTextSize;
	}

	/**
	 * Return the size of the image
	 * @return
	 */
	public Point getImageSize() {
		if (imageSize != null)
			return imageSize;
		if (cachedImageSize == null) {
			cachedImageSize = calcImageSize();
		}
		return cachedImageSize;
	}

	/**
	 * Set the size of the text, only if the style bit is not NO_TEXT and then refresh the 
	 * control
	 * @param size the new size
	 */
	public void setTextSize(Point size) {
		if (size == this.textSize || (size != null && size.equals(this.textSize)))
			return;
		this.textSize = size;
		cachedTextSize = null;
		refreshControl();
	}

	/**
	 * Set the size of the image, only if the style bit is not NO_IMAGE and then refresh the 
	 * control
	 * @param size the new size
	 */
	public void setImageSize(Point size) {
		if (size == this.imageSize || (size != null && size.equals(this.imageSize)))
			return;
		this.imageSize = size;
		cachedImageSize = null;
		refreshControl();
	}

	/**
	 * Calculate the text size from the text into the text variable, if the style bit 
	 * is not NO_TEXT, but the text is null then the text is assumed to be the char "X"
	 * @return the text size
	 */
	protected Point calcTextSize() {
		String string = getText();
		if (!hasText()) {
			if ((style & NO_TEXT) != 0)
				return new Point(0, 0);
			string = "X"; //$NON-NLS-1$
		}
		Point size;
		GC gc = new GC(getControl().getDisplay());
		try {
			gc.setFont(getControl().getFont());
			size = gc.stringExtent(string);
		} finally {
			gc.dispose();
		}
		if (size.x == 0 && hasText())
			size.x = 5;
		return size;
	}

	/**
	 * Set the maximum text size
	 * @param text
	 */
	public void setMaximumTextSize(String text) {
		cachedTextSize = calcTextSize(text);
	}

	/**
	 * Calculate the text size from a passed string
	 * @param text the string
	 * @return the size of the parameter text
	 */
	protected Point calcTextSize(String text) {
		Point size;
		GC gc = new GC(UIUtils.getDisplay());
		try {
			gc.setFont(UIUtils.getDisplay().getSystemFont());
			gc.getFont().getFontData()[0].setStyle(SWT.BOLD);
			size = gc.stringExtent(text.concat("  "));
		} finally {
			gc.dispose();
		}
		if (size.x == 0 && hasText())
			size.x = 5;
		return size;
	}

	/**
	 * Return the image size
	 * @return
	 */
	protected Point calcImageSize() {
		Point size = new Point(0, 0);
		if (hasImage()) {
			Rectangle bounds = image.getBounds();
			size.x = Math.max(size.x, bounds.width);
			size.y = Math.max(size.y, bounds.height);
		}
		return size;
	}

	/**
	 * Enable or disable the control
	 * @param enabled
	 */
	public void setEnabled(boolean enabled) {
		getControl().setEnabled(enabled);
		refreshControl();
	}

	/**
	 * Check if the control is enabled
	 * @return
	 */
	public boolean isEnabled() {
		return getControl().isEnabled();
	}

	public String getAppliedText() {
		if (appliedText == null) {
			buildCaches();
		}
		return appliedText;
	}

	/**
	 * Clear the cache of the size
	 */
	protected void clearCaches() {
		appliedText = null;
		bounds = null;
		contentArea = null;
		arrowLoc = null;
		imgArea = null;
		textArea = null;
	}

	/**
	 * Build the cache of the size, so until the control will not resized the same size could be reused
	 */
	protected void buildCaches() {
		bounds = control.getClientArea();
		bounds.x += MARGIN;
		bounds.y += MARGIN;
		bounds.width -= MARGIN * 2;
		bounds.height -= MARGIN * 2;
		int x1 = bounds.x + BORDER;
		int y1 = bounds.y + BORDER;
		int w1 = bounds.width - BORDER * 2;
		int h1 = bounds.height - BORDER * 2;
		boolean hasArrows = hasArrows();

		if (hasArrows) {
			arrowLoc = new Point(x1 + w1 + BORDER / 2 - ARROW_WIDTH, y1 + (h1 - ARROW_HEIGHT * 2 - ARROWS_SPACING) / 2 - 1);
		}
		contentArea = new Rectangle(x1, y1, w1 - (hasArrows ? ARROW_WIDTH + CONTENT_ARROW_SPACING : 0), h1);

		boolean hasImage = hasImage();
		boolean hasText = hasText();
		if (hasImage) {
			if (hasText) {
				Point imgSize = getImageSize();
				imgArea = new Rectangle(x1, y1, imgSize.x, h1);
			} else {
				imgArea = contentArea;
			}
		}

		if (hasText) {
			if (hasImage) {
				int w = imgArea.width + IMAGE_TEXT_SPACING;
				textArea = new Rectangle(imgArea.x + w, y1, contentArea.width - w, h1);
			} else {
				textArea = contentArea;
			}
			int maxTextWidth = textArea.width;
			Point textSize = getTextSize();
			if (textSize.x > maxTextWidth) {
				GC gc = new GC(getControl().getDisplay());
				try {
					gc.setFont(getControl().getFont());
					appliedText = getSubString(gc, text, maxTextWidth - gc.stringExtent(ELLIPSIS).x) + ELLIPSIS;
				} finally {
					gc.dispose();
				}
			} else {
				appliedText = text;
			}
		}

	}

	protected void paint(GC gc, Display display) {
		if (bounds == null)
			buildCaches();

		gc.setAntialias(SWT.ON);
		gc.setTextAntialias(SWT.ON);
		
		int x, y, w, h;
		boolean focused = getControl().isFocusControl();
		boolean hasBackgroundAndBorder = pressed || hovered || focused;
		if (hasBackgroundAndBorder) {
			// draw control background
			gc.setBackground(getBorderBackground(display));
			gc.fillRoundRectangle(bounds.x, bounds.y, bounds.width, bounds.height, CORNER_SIZE, CORNER_SIZE);
		}

		if (focused) {
			// draw focused content background
			x = contentArea.x - FOCUS_BORDER;
			y = contentArea.y - FOCUS_BORDER;
			w = contentArea.width + FOCUS_BORDER * 2;
			h = contentArea.height + FOCUS_BORDER * 2;
			gc.setBackground(getRealTextBackground(display));
			gc.fillRoundRectangle(x, y, w, h, FOCUS_CORNER_SIZE, FOCUS_CORNER_SIZE);
		}

		boolean hasImage = hasImage();
		boolean hasText = hasText();
		if (hasImage) {
			Rectangle clipping = gc.getClipping();
			if (clipping == null || clipping.intersects(imgArea)) {
				// draw image
				Point imgSize = getImageSize();
				x = imgArea.x + (imgArea.width - imgSize.x) / 2;
				y = imgArea.y + (imgArea.height - imgSize.y) / 2;
				gc.setClipping(imgArea);
				gc.drawImage(image, x, y);
				gc.setClipping(clipping);
			}
		}
		if (hasText) {
			Rectangle clipping = gc.getClipping();
			if (clipping == null || clipping.intersects(textArea)) {
				// draw text
				String text = getAppliedText();
				gc.setFont(getControl().getFont());
				Point ext = gc.stringExtent(text);
				x = textArea.x;
				y = textArea.y + (textArea.height - ext.y) / 2;
				gc.setClipping(textArea);
				gc.setForeground(getRealTextForeground(display));
				gc.drawString(text, x, y, true);
				gc.setClipping(clipping);
			}
		}

		// draw arrow
		if (hasArrows() && arrowLoc != null) {
			gc.setBackground(org.eclipse.draw2d.ColorConstants.black);
			x = arrowLoc.x + ARROW_WIDTH / 2;
			y = arrowLoc.y + ARROW_HEIGHT * 2;
			int x1 = arrowLoc.x;
			int x2 = arrowLoc.x + ARROW_WIDTH;
			int y1 = y - ARROW_HEIGHT;
			gc.fillPolygon(new int[] { x, y, x2, y1, x1 - 1, y1 });
		}

		// draw border
		x = bounds.x;
		y = bounds.y;
		w = bounds.width;
		h = bounds.height;
		gc.setForeground(getBorderForeground(display, focused));
		gc.drawRoundRectangle(x, y, w, h, CORNER_SIZE, CORNER_SIZE);
	}

	private Color getRealTextForeground(Display display) {
		if (!getControl().isEnabled())
			return display.getSystemColor(SWT.COLOR_GRAY);
		if (textForeground != null)
			return textForeground;
		return display.getSystemColor(SWT.COLOR_LIST_FOREGROUND);
	}

	private Color getRealTextBackground(Display display) {
		if (textBackground != null)
			return textBackground;
		return display.getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	private Color getBorderBackground(Display display) {
		if (pressed)
			return display.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		return display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
	}

	private Color getBorderForeground(Display display, boolean focused) {
		if (focused)
			return display.getSystemColor(SWT.COLOR_WIDGET_BORDER);
		return display.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
	}

	protected static int checkStyle(int style, int defaultValue, int... bits) {
		for (int bit : bits) {
			int s = style & bit;
			if (s != 0)
				return s;
		}
		return defaultValue;
	}

	protected static String getSubString(GC gc, String string, int maxWidth) {
		Point ext = gc.stringExtent(string);
		if (ext.x <= maxWidth || string.length() == 0)
			return string;
		return getSubString(gc, string.substring(0, string.length() - 1), maxWidth);
	}

	public Object getInput() {
		return null;
	}

	public ISelection getSelection() {
		return new StructuredSelection(this);
	}

	public void refresh() {
	}

	public void refreshControl() {
		getControl().redraw();
	}

	public void setInput(Object input) {
	}

	public void setSelection(ISelection selection, boolean reveal) {
	}

}
