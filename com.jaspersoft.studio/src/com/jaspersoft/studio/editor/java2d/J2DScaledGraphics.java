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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.ScaledGraphics;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.FontMetrics;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.wb.swt.ColorManager;

/*
 * A J2DGraphics is a draw2d Graphics that delegates all its calls to a Java2D Graphics2D object. All the draw2d
 * behavior can be reproduced except for the following methods where a 'best effort' policy is only available: <ul>
 * <li><b>xxxText() </b>: not implemented, call is directed to the corresponding xxxString()</li> <li><b>drawImage()
 * </b>: SWT Image are converted into BufferedImages prior to display. The method used is not optimized as a pixel by
 * pixel conversion is performed. The only speedup available is that once converted an image is stored in a hash map
 * indexed by the original Image: this behavior is consistent with the fact that many images are used multiple times and
 * of small size.</li> <li><b>Fonts </b>: SWT and Java2D fonts are not handled the same way, and some platform
 * diffeences may occur. The Java2D font is derived from the name/style/size of the SWT font. The original SWT font is
 * retained so that the correct font metrics are computed.</li> <li><b>Line style </b>: exact dash pattern is not known
 * and not specified by SWT: Visually equivalent patterns have been chosen but may not match each platform/window system
 * combination.</li> </ul> <li><b>Colors </b>: SWT Colors are converted exactly into opaque AWT ones.</li> <li><b>XOR
 * mode </b>: SWT operates on a binary representation of pixel, while AWT performs a logical permutation of colors. To
 * achieve a decent SWT XOR effect a light grey with 50% transparency is used as the alternate AWT color.</li>
 * <li><b>Line width </b>: While Java2D can use any real value as a line width, the SWT behavior is kept: line width is
 * always rounded to an integer value and if a scale factor is defined the line width / scale value is actually used
 * (draw2d does not scale line width).</li> <li><b>Gradients </b>: A GradientPaint is used, with the correct
 * orientation.</li> </ul> The Java2D rendering hints are initialized by calling J2DRegistry.initGraphics(), so you can
 * put your own hints that are globally available there too. If the -Dj2d.debug system property is set, a warning is
 * printed if the state stack is not empty when dispose() is called. To benefits from all the Java2D capabilities, the
 * createGraphics2D() method returns a new Graphics2D initialized with the current Graphics state.
 * 
 * @author Christophe Avare
 * 
 * @version $Revision: 1.3.4.3.2.2 $
 */
public class J2DScaledGraphics extends ScaledGraphics {

	/** The Constant hints. */
	private static final Map<Key, Object> hints = new HashMap<Key, Object>();

	static {
		// Activate AA by default
		hints.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		hints.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
		hints.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	}

	/** The Constant KEY_USE_JAVA2D. */
	public static final J2DKey KEY_USE_JAVA2D = new J2DKey(1);

	/** The Constant KEY_FIXED_LINEWIDTH. */
	public static final J2DKey KEY_FIXED_LINEWIDTH = new J2DKey(2);

	/**
	 * The Class J2DKey.
	 */
	private static class J2DKey extends RenderingHints.Key {

		/**
		 * Instantiates a new j2 d key.
		 * 
		 * @param privatekey
		 *          the privatekey
		 */
		public J2DKey(int privatekey) {
			super(privatekey);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.awt.RenderingHints.Key#isCompatibleValue(java.lang.Object)
		 */
		public boolean isCompatibleValue(Object val) {
			return (val == Boolean.TRUE) || (val == Boolean.FALSE);
		}
	}

	/** A cache for the converted SWT Images. */
	private static final Map<Image, BufferedImage> IMAGE_CACHE = new HashMap<Image, BufferedImage>(10);

	/** An approxmation dash pattern for the DOT_ARRAY style. */
	private static final float[] DOT_ARRAY = { 2, 2 };

	/** An approxmation dash pattern for the DASH_ARRAY style. */
	private static final float[] DASH_ARRAY = { 3, 3 };

	/** An approxmation dash pattern for the DASHDOT_ARRAY style. */
	private static final float[] DASHDOT_ARRAY = { 4, 2, 1, 2 };

	/** An approxmation dash pattern for the DASHDOTDOT_ARRAY style. */
	private static final float[] DASHDOTDOT_ARRAY = { 4, 2, 1, 2, 1, 2 };

	/** The color used to simulate the SWT XOR paint mode. */
	private static final java.awt.Color TRANSPARENT = new java.awt.Color(0.3f, 0.3f, 0.3f, 0.5f);

	/** The value of the screen DPI, used to compute the actual font size. */
	private double _dpi = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setAlpha(int)
	 */
	@Override
	public void setAlpha(int alpha) {
		getGraphics2D().setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, (alpha / 255.0f)));
	}

	/** Current XOR mode. */
	private boolean _xor = false;

	/** Current line style. */
	private int _lineStyle = LINE_SOLID;

	/** Current line width. */
	private int _lineWidth = 1;

	/** Current SWT background color. */
	private Color _bg;

	/** Current SWT foreground color. */
	private Color _fg;

	/** AWT cached background color. */
	private java.awt.Color _awtBg;

	/** AWT cached foreground color. */
	private java.awt.Color _awtFg;

	/** Current font object. */
	private Font _font;

	/** The Graphics2D object against which the drawing primitives are performed. */
	private Graphics2D _g2d;

	/** The GC where the graphics will ultimetely be displayed. */
	private GC gc;

	/** The pop/push/restoreState commands operate on this Ste stack. */
	private Stack<State> _stack = new Stack<State>();

	/**
	 * An internal class to store the Graphics context. Because some information is lost when a Graphics2D copy is made
	 * (like the draw2d line style), some information is stored along with a copy of the Graphics2D object to avoid
	 * complex and unecessary conversions between SWt and ava2D.
	 * 
	 * @author Christophe Avare
	 */
	private class State {

		/** The xor. */
		boolean xor;

		/** The line style. */
		int lineStyle;

		/** The line width. */
		int lineWidth;

		/** The bg. */
		Color bg;

		/** The fg. */
		Color fg;
		// Graphics2D g;
		/** The f. */
		Font f;

		/** The clip. */
		Shape clip;

		/** The t. */
		AffineTransform t;

		/**
		 * Instantiates a new state.
		 */
		public State() {
			// this.g = J2DGraphics.this._g2d;
			this.xor = J2DScaledGraphics.this._xor;
			this.lineStyle = J2DScaledGraphics.this._lineStyle;
			this.lineWidth = J2DScaledGraphics.this._lineWidth;
			this.bg = J2DScaledGraphics.this._bg;
			this.fg = J2DScaledGraphics.this._fg;
			this.f = J2DScaledGraphics.this._font;
			this.clip = _g2d.getClip();
			this.t = _g2d.getTransform();
		}

		/**
		 * Restore.
		 * 
		 * @return the state
		 */
		public State restore() {
			// Graphics2D old = J2DGraphics.this._g2d;
			// J2DGraphics.this._g2d = this.g; //(Graphics2D) this.g.create();
			// old.dispose();
			J2DScaledGraphics.this._xor = this.xor;
			J2DScaledGraphics.this._lineStyle = this.lineStyle;
			J2DScaledGraphics.this._lineWidth = this.lineWidth;
			J2DScaledGraphics.this._bg = this.bg;
			J2DScaledGraphics.this._fg = this.fg;
			J2DScaledGraphics.this.updateStroke();
			J2DScaledGraphics.this._awtFg = null;
			J2DScaledGraphics.this._awtBg = null;
			J2DScaledGraphics.this.updateColors();
			J2DScaledGraphics.this._font = this.f;
			J2DScaledGraphics.this.updateFont();
			J2DScaledGraphics.this._g2d.setTransform(this.t);
			J2DScaledGraphics.this._g2d.setClip(this.clip);
			return this;
		}

		/**
		 * The old Graphics2D object is disposed when the State object is removed from the stack.
		 */
		public void dispose() {
			// this.g.dispose();
		}
	}

	/**
	 * A J2DGraphics is built from a Control and a Graphics2DFactory. The Control object is cached so that it can be used
	 * to create a GC that will give us access to the FontMetrics. The Graphics2DFactory is not cached because only an
	 * initial Graphics2D object is needed.
	 * 
	 * @param gc
	 *          the gc
	 * @param g2d
	 *          the g2d
	 */
	public J2DScaledGraphics(Graphics graphics, GC gc, Graphics2D g2d) {
		super(graphics);
		if (gc != null) {
			_dpi = gc.getDevice().getDPI().x / 72.0;
			this.gc = gc;
			_fg = gc.getForeground();
			_bg = gc.getBackground();
			_font = gc.getFont();
		} else {
			_fg = toSWTColor(g2d.getColor());
			_bg = toSWTColor(g2d.getBackground());
		}
		_g2d = g2d;

		g2d.setRenderingHints(hints);

		// J2DRegistry.initGraphics(_g2d);
		updateStroke();
		updateColors();
		updateFont();
	}

	/**
	 * Returns a new Graphics2D initialized with the current state. Caller must call the dispose() method of the returned
	 * object, once the work on it is done.
	 * 
	 * @return A copy of the current underlying Graphics2D object
	 */
	public Graphics2D createGraphics2D() {
		return (Graphics2D) _g2d.create();
	}

	/**
	 * Original copy of the Graphics2D.
	 * 
	 * @return the current underlying Graphics2D object
	 */
	public Graphics2D getGraphics2D() {
		return _g2d;
	}

	/**
	 * Computes a Java2D font equivalent to the current SWT font. Because Java2D does not properly scale fonts if the
	 * screen DPI is not 72 (which means the font size in points is not correct), we actually create a font of size
	 * SWT-font-size * screen-DPI. The SWT font name is used as-is.
	 */
	protected void updateFont() {
		if (_font == null)
			return;
		FontData fd = _font.getFontData()[0];
		int style = fd.getStyle();
		int awtStyle = java.awt.Font.PLAIN;
		if ((style & SWT.BOLD) == SWT.BOLD) {
			awtStyle = java.awt.Font.BOLD;
		}
		if ((style & SWT.ITALIC) == SWT.ITALIC) {
			awtStyle |= java.awt.Font.ITALIC;
		}
		// System.err.println("SWT Font = " + fd);
		java.awt.Font awtFont = new java.awt.Font(fd.getName(), awtStyle, (int) Math.round(fd.getHeight() * _dpi));
		_g2d.setFont(awtFont);
		// System.err.println("AWT Font = " + awtFont);
	}

	/**
	 * Computes the AWT colors given the SWT ones. Depending on the XOR mode flag, the setXORMode() or setPaintMode()
	 * method is called.
	 */
	protected void updateColors() {
		if (_awtBg == null) {
			_awtBg = toAWTColor(_bg);
		}
		_g2d.setBackground(_awtBg);
		if (_awtFg == null) {
			_awtFg = toAWTColor(_fg);
		}
		_g2d.setColor(_awtFg);
		if (_xor) {
			_g2d.setXORMode(TRANSPARENT);
		} else {
			_g2d.setPaintMode();
		}
	}

	/**
	 * Compute a Java2D BasicStroke object given the SWT line width and style. The Java2D linecap is set to CAP_SQUARE,
	 * and the join miter angle to 10 (their defaults). If you need to use different values, first create a new Graphics2D
	 * and set your own Stroke object on it.
	 */
	protected void updateStroke() {
		BasicStroke stroke = null;
		float lw = _lineWidth;
		if (_g2d.getRenderingHint(KEY_FIXED_LINEWIDTH) == Boolean.TRUE) {
			lw /= _g2d.getTransform().getScaleX();
		}
		switch (_lineStyle) {
		case LINE_DOT:
			stroke = new BasicStroke(lw, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, DOT_ARRAY, 1);
			break;
		case LINE_DASH:
			stroke = new BasicStroke(lw, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, DASH_ARRAY, 0);
			break;
		case LINE_DASHDOT:
			stroke = new BasicStroke(lw, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, DASHDOT_ARRAY, 2);
			break;
		case LINE_DASHDOTDOT:
			stroke = new BasicStroke(lw, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, DASHDOTDOT_ARRAY, 2);
			break;
		default:
			stroke = new BasicStroke(lw, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 10, null, 0);
		}
		_g2d.setStroke(stroke);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#clipRect(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void clipRect(Rectangle r) {
		// System.err.println("clipRect ("+r+")");
		if (r == null) {
			_g2d.setClip(null);
		} else {
			_g2d.setClip(r.x, r.y, r.width, r.height);
		}
	}

	/**
	 * EMits a warning if the state stack is not empty when this method is called (and the j2d.debug property is set).
	 * 
	 * @see org.eclipse.draw2d.Graphics#dispose()
	 */
	public void dispose() {
		colorManager.dispose();
		_g2d.dispose();
		_g2d = null;
		// if (!_stack.isEmpty() && J2DRegistry.DEBUG) {
		// J2DRegistry.printMessage("Unbalanced pushState() / popState() detected.",
		// "J2DGraphics.dispose() called with " + _stack.size()
		// + " remaining pushed states!");
		// }
		_stack.removeAllElements();
		_stack = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawArc(int, int, int, int, int, int)
	 */
	public void drawArc(int x, int y, int w, int h, int offset, int length) {
		_g2d.drawArc(x, y, w, h, offset, length);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawFocus(int, int, int, int)
	 */
	public void drawFocus(int x, int y, int w, int h) {
		// System.err.println("drawFocus("+x+","+y+","+w+","+h+")");
		int ls = _lineStyle;
		setLineStyle(LINE_DOT);
		_g2d.drawRect(x, y, w, h);
		setLineStyle(ls);
	}

	/**
	 * Converts an SWT Image into a BufferedImage. Conversion is performed on a pixel by pixel basis. Transparency
	 * information is currently not used. The converted image is cached to accelerate multiple calls.
	 * 
	 * @param src
	 *          An SWT Image
	 * @return An equivalent BufferedImage
	 */
	protected BufferedImage convertSWTImage(Image src) {
		BufferedImage dst = IMAGE_CACHE.get(src);
		if (dst != null) {
			return dst;
		}
		ImageData data = src.getImageData();
		dst = new BufferedImage(data.width, data.height, BufferedImage.TYPE_INT_ARGB);
		ImageData mask = data.getTransparencyMask();
		RGB[] rgbs = data.getRGBs();
		for (int yy = 0; yy < data.height; yy++) {
			// FIX by Daniel Mazurek
			// for (int xx = 0; xx < data.height; xx++) {
			for (int xx = 0; xx < data.width; xx++) {
				int a = mask.getPixel(xx, yy);
				int rgb = data.getPixel(xx, yy);
				if (a == 1) {
					a = 255;
				}
				if (rgbs != null) {
					RGB p = rgbs[rgb];
					dst.setRGB(xx, yy, (a << 24) | (p.red << 16) | (p.green << 8) | p.blue);
				} else {
					dst.setRGB(xx, yy, (a << 24) | rgb);
				}
			}
		}
		IMAGE_CACHE.put(src, dst);
		return dst;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawImage(org.eclipse.swt.graphics.Image, int, int, int, int, int, int, int, int)
	 */
	public void drawImage(Image srcImage, int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2) {
		// System.err.println("drawImage");
		BufferedImage dst = convertSWTImage(srcImage);
		_g2d.drawImage(dst, x1, y1, x1 + w1, y1 + h1, x2, y2, x2 + w2, y2 + h2, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawImage(org.eclipse.swt.graphics.Image, int, int)
	 */
	public void drawImage(Image srcImage, int x, int y) {
		// System.err.println("drawImage(" + x + "," + y + ")");
		BufferedImage dst = convertSWTImage(srcImage);
		_g2d.drawImage(dst, null, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawLine(int, int, int, int)
	 */
	public void drawLine(int x1, int y1, int x2, int y2) {
		// System.err.println("drawLine("+x1+","+y1+","+x2+","+y2+")");
		_g2d.drawLine(x1, y1, x2, y2);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawOval(int, int, int, int)
	 */
	public void drawOval(int x, int y, int w, int h) {
		// System.err.println("drawOval("+x+","+y+","+w+","+h+")");
		_g2d.drawOval(x, y, w, h);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawPolygon(org.eclipse.draw2d.geometry.PointList)
	 */
	public void drawPolygon(PointList points) {
		Point p = new Point();
		int n = points.size();
		int[] xc = new int[n];
		int[] yc = new int[n];
		for (int i = 0; i < n; i++) {
			points.getPoint(p, i);
			xc[i] = p.x;
			yc[i] = p.y;
		}
		_g2d.drawPolygon(xc, yc, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawPolyline(org.eclipse.draw2d.geometry.PointList)
	 */
	public void drawPolyline(PointList points) {
		Point p = new Point();
		int n = points.size();
		int[] xc = new int[n];
		int[] yc = new int[n];
		for (int i = 0; i < n; i++) {
			points.getPoint(p, i);
			xc[i] = p.x;
			yc[i] = p.y;
		}
		_g2d.drawPolyline(xc, yc, n);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawRectangle(int, int, int, int)
	 */
	public void drawRectangle(int x, int y, int width, int height) {
		// System.err.println("drawRectangle ("+x+","+y+","+width+","+height+")");
		_g2d.drawRect(x, y, width, height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawRoundRectangle(org.eclipse.draw2d.geometry.Rectangle, int, int)
	 */
	public void drawRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
		_g2d.drawRoundRect(r.x, r.y, r.width, r.height, arcWidth, arcHeight);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawString(java.lang.String, int, int)
	 */
	public void drawString(String s, int x, int y) {
		// System.err.println("drawString(" + s + "," + x + "," + y + ")");
		java.awt.FontMetrics fm = _g2d.getFontMetrics();
		int dy = fm.getAscent();
		// Shape clip = g2d.getClip();
		// g2d.setClip(null);
		_g2d.drawString(s, x, y + dy);
		// g2d.setClip(clip);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#drawText(java.lang.String, int, int)
	 */
	public void drawText(String s, int x, int y) {
		// System.err.println("drawText(" + s + "," + x + "," + y + ") ?");
		drawString(s, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillArc(int, int, int, int, int, int)
	 */
	public void fillArc(int x, int y, int w, int h, int offset, int length) {
		_g2d.setColor(_awtBg);
		_g2d.fillArc(x, y, w, h, offset, length);
		_g2d.setColor(_awtFg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillGradient(int, int, int, int, boolean)
	 */
	public void fillGradient(int x, int y, int w, int h, boolean vertical) {
		// System.err.println("fillGradient ?");
		GradientPaint p = null;
		if (vertical) {
			p = new GradientPaint(x, y, _awtFg, x, y + h, _awtBg);
		} else {
			p = new GradientPaint(x, y, _awtFg, x + w, y, _awtBg);
		}
		_g2d.setPaint(p);
		_g2d.fillRect(x, y, w, h);
		_g2d.setPaint(_awtFg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillOval(int, int, int, int)
	 */
	public void fillOval(int x, int y, int w, int h) {
		_g2d.setColor(_awtBg);
		_g2d.fillOval(x, y, w, h);
		_g2d.setColor(_awtFg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillPolygon(org.eclipse.draw2d.geometry.PointList)
	 */
	public void fillPolygon(PointList points) {
		// System.err.println("fillPolygon("+points.size()+")");
		Point p = new Point();
		int n = points.size();
		int[] xc = new int[n];
		int[] yc = new int[n];
		for (int i = 0; i < n; i++) {
			points.getPoint(p, i);
			xc[i] = p.x;
			yc[i] = p.y;
		}
		_g2d.setColor(_awtBg);
		_g2d.fillPolygon(xc, yc, n);
		_g2d.setColor(_awtFg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillRectangle(int, int, int, int)
	 */
	public void fillRectangle(int x, int y, int width, int height) {
		// System.err.println("fillRectangle ("+x+","+y+","+width+","+height+")");
		_g2d.setColor(_awtBg);
		_g2d.fillRect(x, y, width, height);
		_g2d.setColor(_awtFg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillRoundRectangle(org.eclipse.draw2d.geometry.Rectangle, int, int)
	 */
	public void fillRoundRectangle(Rectangle r, int arcWidth, int arcHeight) {
		// System.err.println("fillRoundRectangle ("+r+","+arcWidth+","+arcHeight+")");
		_g2d.setColor(_awtBg);
		_g2d.fillRoundRect(r.x, r.y, r.width, r.height, arcWidth, arcHeight);
		_g2d.setColor(_awtFg);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillString(java.lang.String, int, int)
	 */
	public void fillString(String s, int x, int y) {
		// System.err.println("fillString ?");
		_g2d.drawString(s, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#fillText(java.lang.String, int, int)
	 */
	public void fillText(String s, int x, int y) {
		// System.err.println("fillText(" + s + "," + x + "," + y + ") ?");
		_g2d.drawString(s, x, y);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getAbsoluteScale()
	 */
	public double getAbsoluteScale() {
		return _g2d.getTransform().getScaleX();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getBackgroundColor()
	 */
	public Color getBackgroundColor() {
		return _bg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getClip(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public Rectangle getClip(Rectangle rect) {
		return toDraw2D(_g2d.getClipBounds(toJava2D(rect)));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getFont()
	 */
	public Font getFont() {
		return _font;
	}

	/**
	 * The fontMetrics is obtained from a GC build with the target Control so that the FontMetrics object returned is the
	 * same as in a regular draw2d call. Text layout algorithms should benefits from this.
	 * 
	 * @return the font metrics
	 * @see org.eclipse.draw2d.Graphics#getFontMetrics()
	 */
	public FontMetrics getFontMetrics() {
		// System.err.println("getFontMetrics");
		// gc.setFont(_font);
		FontMetrics fm = gc.getFontMetrics();

		return fm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getForegroundColor()
	 */
	public Color getForegroundColor() {
		return _fg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getLineStyle()
	 */
	public int getLineStyle() {
		return _lineStyle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getLineWidth()
	 */
	public int getLineWidth() {
		return _lineWidth;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getXORMode()
	 */
	public boolean getXORMode() {
		return _xor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#popState()
	 */
	public void popState() {
		// System.err.println("--> popState: " + _stack.size());
		_stack.pop().restore().dispose();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#pushState()
	 */
	public void pushState() {
		_stack.push(new State());
		// System.err.println("--> pushState: " + _stack.size());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#restoreState()
	 */
	public void restoreState() {
		// System.err.println("--> restoreState");
		(_stack.peek()).restore();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#scale(double)
	 */
	public void scale(double amount) {
		// System.err.println("scale(" + amount + ")");
		_g2d.scale(amount, amount);
		updateStroke();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color rgb) {
		// System.err.println("setBackgroundColor("+rgb+")");
		_bg = rgb;
		_awtBg = null;
		updateColors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setClip(org.eclipse.draw2d.geometry.Rectangle)
	 */
	public void setClip(Rectangle r) {
		// System.err.println("setClip ("+r+")");
		_g2d.setClip(r.x, r.y, r.width, r.height);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setFont(org.eclipse.swt.graphics.Font)
	 */
	public void setFont(Font f) {
		_font = f;
		updateFont();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setForegroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setForegroundColor(Color rgb) {
		// System.err.println("setForegroundColor("+rgb+")");
		_fg = rgb;
		_awtFg = null;
		updateColors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setLineStyle(int)
	 */
	public void setLineStyle(int style) {
		// System.err.println("setLineStyle("+style+")");
		_lineStyle = style;
		updateStroke();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setLineWidth(int)
	 */
	public void setLineWidth(int width) {
		// System.err.println("setLineWidth("+width+")");
		_lineWidth = width;
		updateStroke();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setXORMode(boolean)
	 */
	public void setXORMode(boolean b) {
		_xor = b;
		updateColors();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#translate(int, int)
	 */
	public void translate(int dx, int dy) {
		// System.err.println("translate(" + dx + "," + dy + ")");
		_g2d.translate(dx, dy);
	}

	/**
	 * Flush the image cache by removing all the entries whose SWT image is disposed. Normally only called by the
	 * J2DGraphicsSource.flushGraphics method but does not harm if called by another client.
	 */
	synchronized public static void flushImageCache() {
		// System.err.println("flushImageCache()");
		Iterator<Image> keys = IMAGE_CACHE.keySet().iterator();
		while (keys.hasNext()) {
			Image img = keys.next();
			if (img.isDisposed()) {
				keys.remove();
			}
		}
	}

	/**
	 * Converts an AWT Rectangle into a draw2d one. Do we really need all those Rectangle definitions? Hopefully,
	 * coordinate systems are the same.
	 * 
	 * @param r
	 *          The AWT Rectangle to convert
	 * @return A draw2d equivalent
	 */
	public static Rectangle toDraw2D(java.awt.Rectangle r) {
		return new Rectangle(r.x, r.y, r.width, r.height);
	}

	/**
	 * Converts a draw2d Rectangle into an AWT one. Do we really need all those Rectangle definitions? Hopefully,
	 * coordinate systems are the same.
	 * 
	 * @param r
	 *          The draw2d Rectangle to convert
	 * @return An AWT equivalent
	 */
	public static java.awt.Rectangle toJava2D(Rectangle r) {
		return new java.awt.Rectangle(r.x, r.y, r.width, r.height);
	}

	/**
	 * A utility method to convert an SWT Color to an AWT one.
	 * 
	 * @param c
	 *          The SWT Color
	 * @return An equivalent AWT Color
	 */
	public static java.awt.Color toAWTColor(Color c) {
		return new java.awt.Color(c.getRed(), c.getGreen(), c.getBlue());
	}

	private ColorManager colorManager = new ColorManager();

	/**
	 * A utility method to convert an AWT Color to an SWT one. The default display is used. Resource disposal should be
	 * performed by the caller.
	 * 
	 * @param c
	 *          The AWT Color
	 * @return An equivalent SWT Color
	 */
	public Color toSWTColor(java.awt.Color c) {
		return colorManager.getColor(new RGB(c.getRed(), c.getGreen(), c.getBlue()));
	}

	// 3.5 compatibility methods -------------------------------

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setLineAttributes(org.eclipse.swt.graphics.LineAttributes)
	 */
	public void setLineAttributes(LineAttributes paramLineAttributes) {
		setLineWidthFloat(paramLineAttributes.width);
		setLineStyle(paramLineAttributes.style);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getLineAttributes()
	 */
	public LineAttributes getLineAttributes() {
		LineAttributes localLineAttributes;
		(localLineAttributes = new LineAttributes(getLineWidthFloat())).style = getLineStyle();
		return localLineAttributes;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getLineWidthFloat()
	 */
	public float getLineWidthFloat() {
		return getLineWidth();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setLineMiterLimit(float)
	 */
	public void setLineMiterLimit(float paramFloat) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setLineWidthFloat(float)
	 */
	public void setLineWidthFloat(float paramFloat) {
		setLineWidth((int) paramFloat);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#setAdvanced(boolean)
	 */
	public void setAdvanced(boolean paramBoolean) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.draw2d.Graphics#getAdvanced()
	 */
	public boolean getAdvanced() {
		return false;
	}

	// End 3.5 compatibility methods -------------------------------
}
