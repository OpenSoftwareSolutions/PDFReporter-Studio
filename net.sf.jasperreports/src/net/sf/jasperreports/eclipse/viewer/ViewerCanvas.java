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
package net.sf.jasperreports.eclipse.viewer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.ImageMapRenderable;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPrintAnchorIndex;
import net.sf.jasperreports.engine.JRPrintElement;
import net.sf.jasperreports.engine.JRPrintHyperlink;
import net.sf.jasperreports.engine.JRPrintImage;
import net.sf.jasperreports.engine.JRPrintImageAreaHyperlink;
import net.sf.jasperreports.engine.JRPrintPage;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.Renderable;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleGraphics2DReportConfiguration;
import net.sf.jasperreports.view.JRHyperlinkListener;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.wb.swt.Keyboard;

public class ViewerCanvas extends Canvas {
	public static final int ZOOM_MODE_NONE = 0;
	public static final int ZOOM_MODE_ACTUAL_SIZE = 1;
	public static final int ZOOM_MODE_FIT_WIDTH = 2;
	public static final int ZOOM_MODE_FIT_HEIGHT = 3;
	public static final int ZOOM_MODE_FIT_PAGE = 4;

	public static final int MARGIN = 5;
	private static final Cursor CURSOR_SIZEALL = new Cursor(null, SWT.CURSOR_SIZEALL);
	private static final Cursor CURSOR_HAND = new Cursor(null, SWT.CURSOR_HAND);

	private JRPrintPage page;
	private List<IHyperlinkContainer> links = new ArrayList<IHyperlinkContainer>();
	private Image reportImage;
	private String errorMessage;
	private boolean dragging;
	private JRPrintHyperlink currentLink;
	private Rectangle ds;
	private IReportViewer rViewer;

	private MouseListener mListener = new MouseListener() {

		@Override
		public void mouseUp(MouseEvent e) {
			if (reportImage != null) {
				if (dragging) {
					dragging = false;
					setCursor(null);
				}

				if (e.button == 1) {
					if (currentLink != null && currentLink == getHyperlinkAt(e.x, e.y)) {
						// handle click
						setCursor(null);
						try {
							BusyIndicator.showWhile(getDisplay(), new Runnable() {
								public void run() {
									handleHyperlinkClick();
								}
							});
						} finally {
							setCursor(CURSOR_HAND);
						}
					}
				}

				// if jumped to a different page image can be missing
				if (reportImage != null)
					updateHyperlink(e.x, e.y);
				else {
					currentLink = null;
					setCursor(null);
				}
			}
		}

		@Override
		public void mouseDown(MouseEvent e) {
			if (reportImage != null) {
				if (e.button == 1) {
					if (currentLink == null) {
						dragging = true;
						ds = new Rectangle(e.x, e.y, getVerticalBar().getSelection(), getHorizontalBar().getSelection());
						setCursor(CURSOR_SIZEALL);
					}
				}
			}
		}

		@Override
		public void mouseDoubleClick(MouseEvent e) {
		}
	};
	private MouseMoveListener mMoveListener = new MouseMoveListener() {

		@Override
		public void mouseMove(MouseEvent e) {
			if (reportImage != null) {
				if (dragging) {
					setScrollBarSelection(getHorizontalBar(), ds.height - e.x + ds.x);
					setScrollBarSelection(getVerticalBar(), ds.width - e.y + ds.y);
					repaint();
				} else
					updateHyperlink(e.x, e.y);
			}
		}
	};
	private PaintListener pListener = new PaintListener() {

		@Override
		public void paintControl(PaintEvent e) {
			paint(e.gc);
		}
	};
	private KeyListener keyListener = new KeyListener() {
		private int setDecrementSelection(ScrollBar sb) {
			int s = sb.getSelection();
			setScrollBarSelection(sb, s - sb.getIncrement());
			return s;
		}

		private int setIncrementSelection(ScrollBar sb) {
			int s = sb.getSelection();
			setScrollBarSelection(sb, s + sb.getIncrement());
			return s;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			System.out.println(e.toString());
			if ((e.stateMask & Keyboard.getCtrlKey()) != 0) {
				switch (e.keyCode) {
				case '=':
				case '+':
				case SWT.KEYPAD_ADD:
					zoomIn();
					repaint();
					break;
				case '0':
				case SWT.KEYPAD_0:
					setZoom(1);
					break;
				case '-':
				case SWT.KEYPAD_SUBTRACT:
					zoomOut();
					break;
				}
			} else
				switch (e.keyCode) {
				case SWT.ARROW_UP:
				case SWT.PAGE_UP:
					ScrollBar sb = getVerticalBar();
					if (sb.getSelection() == setDecrementSelection(sb)) {
						if (rViewer.canGotoPreviousPage()) {
							rViewer.gotoPreviousPage();
							setScrollBarSelection(sb, sb.getMaximum());
							repaint();
						}
					} else
						repaint();
					break;
				case SWT.ARROW_DOWN:
				case SWT.PAGE_DOWN:
					sb = getVerticalBar();
					if (sb.getSelection() == setIncrementSelection(sb)) {
						if (rViewer.canGotoNextPage()) {
							rViewer.gotoNextPage();
							setScrollBarSelection(sb, sb.getMinimum());
							repaint();
						}
					} else
						repaint();
					break;
				case SWT.ARROW_LEFT:
					setDecrementSelection(getHorizontalBar());
					repaint();
					break;
				case SWT.ARROW_RIGHT:
					setIncrementSelection(getHorizontalBar());
					repaint();
					break;
				case SWT.HOME:
					if (rViewer.canGotoFirstPage())
						rViewer.gotoFirstPage();
					sb = getVerticalBar();
					setScrollBarSelection(sb, sb.getMinimum());
					repaint();
					break;
				case SWT.END:
					if (rViewer.canGotoLastPage())
						rViewer.gotoLastPage();
					break;
				}
		}

		@Override
		public void keyPressed(KeyEvent e) {
		}

	};
	private TraverseListener tListener = new TraverseListener() {

		@Override
		public void keyTraversed(TraverseEvent e) {
			e.doit = true;
		}
	};
	private SelectionAdapter sListener = new SelectionAdapter() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			repaint();
		}
	};
	private ControlListener cListener = new ControlListener() {

		@Override
		public void controlResized(ControlEvent e) {
			if (!(zoomMode == ZOOM_MODE_NONE || zoomMode == ZOOM_MODE_ACTUAL_SIZE)) {
				float zm = computeZoom();
				if (!hasReport())
					return;
				if (Math.abs(zm - zoom) > 0.00001) {
					setZoomInternal(zm);
					rViewer.fireViewerModelChanged();
				}
			}
			refresh();
		}

		@Override
		public void controlMoved(ControlEvent e) {
		}
	};
	private int zoomMode = ZOOM_MODE_NONE;
	private float zoom = 1.0f;
	private static final float[] zoomLevels = new float[] { 0.5f, 0.75f, 1.0f, 1.25f, 1.50f, 1.75f, 2.0f };

	public int getZoomMode() {
		return zoomMode;
	}

	public float getZoom() {
		return zoom;
	}

	public void setZoomMode(int zoomMode) {
		if (!hasReport())
			return;

		if (zoomMode != getZoomMode()) {
			this.zoomMode = zoomMode;
			setZoomInternal(computeZoom());
			rViewer.fireViewerModelChanged();
		}
	}

	private float getMinZoom() {
		return zoomLevels[0];
	}

	private float getMaxZoom() {
		return zoomLevels[zoomLevels.length - 1];
	}

	public float getNextZoom() {
		for (int i = 0; i < zoomLevels.length; i++) {
			if (zoom < zoomLevels[i])
				return zoomLevels[i];
		}

		return getMaxZoom();
	}

	public float getPreviousZoom() {
		for (int i = zoomLevels.length - 1; i >= 0; i--) {
			if (zoom > zoomLevels[i])
				return zoomLevels[i];
		}

		return getMinZoom();
	}

	public boolean canZoomIn() {
		return hasReport() && zoom < getMaxZoom();
	}

	public boolean canZoomOut() {
		return hasReport() && zoom > getMinZoom();
	}

	public void setZoomInternal(float zoom) {
		this.zoom = zoom;
	}

	public float[] getZoomLevels() {
		return zoomLevels;
	}

	public void zoomIn() {
		if (canZoomIn())
			setZoom(getNextZoom());
	}

	public void zoomOut() {
		if (canZoomOut())
			setZoom(getPreviousZoom());
	}

	public void setZoom(float zm) {
		if (!hasReport())
			return;
		zoomMode = ZOOM_MODE_NONE;
		if (Math.abs(zm - zoom) > 0.00001) {
			setZoomInternal(zm);
			rViewer.fireViewerModelChanged();
		}
	}

	public boolean hasReport() {
		return rViewer.hasReport();
	}

	public float computeZoom() {
		JasperPrint doc = rViewer.getReport();
		if (doc == null)
			return 1.0f;
		int pw = doc.getPageWidth();
		int ph = doc.getPageHeight();
		Point fitSize = getFitSize();
		switch (zoomMode) {
		case ZOOM_MODE_ACTUAL_SIZE:
			return 1.0f;
		case ZOOM_MODE_FIT_WIDTH:
			double ratio = ratio(fitSize.x, pw);
			return ratio(getFitSize((int) (pw * ratio), (int) (ph * ratio)).x, pw);
		case ZOOM_MODE_FIT_HEIGHT:
			ratio = ratio(fitSize.y, ph);
			return ratio(getFitSize((int) (pw * ratio), (int) (ph * ratio)).y, ph);
		case ZOOM_MODE_FIT_PAGE:
			return Math.min(ratio(fitSize.x, pw), ratio(fitSize.y, ph));
		}
		return zoom;
	}

	private float ratio(int a, int b) {
		return (a * 100 / b) / 100.0f;
	}

	private IReportViewerListener listener = new IReportViewerListener() {
		public void viewerStateChanged(ReportViewerEvent evt) {
			if (!isDisposed())
				refresh();
		}
	};
	private JasperReportsContext jContext;

	public ViewerCanvas(Composite parent, int style, JasperReportsContext jContext) {
		super(parent, style | SWT.H_SCROLL | SWT.V_SCROLL);
		this.jContext = jContext;
		addPaintListener(pListener);
		addMouseMoveListener(mMoveListener);
		addMouseListener(mListener);
		addKeyListener(keyListener);
		addTraverseListener(tListener);
		addControlListener(cListener);

		setupBar(getHorizontalBar());
		setupBar(getVerticalBar());
	}

	private void setupBar(ScrollBar sb) {
		sb.setIncrement(20);
		sb.addSelectionListener(sListener);
	}

	/**
	 * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
	 */
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Rectangle b = getContentBounds();
		Rectangle trim = computeTrim(0, 0, calcSize(wHint, b.width), calcSize(hHint, b.height));
		return new Point(trim.width, trim.height);
	}

	private static int calcSize(int hint, int size) {
		if (hint != SWT.DEFAULT)
			return hint;
		return size > 0 ? size : 64 + 2 * MARGIN;
	}

	private Image renderPage() throws Throwable {
		JasperPrint jr = rViewer.getReport();
		BufferedImage img = new BufferedImage((int) (jr.getPageWidth() * zoom) + 1, (int) (jr.getPageHeight() * zoom) + 1, BufferedImage.TYPE_INT_RGB);

		Graphics2D g2d = (Graphics2D) img.getGraphics();
		try {
			JRGraphics2DExporter exporter = new JRGraphics2DExporter(jContext);
			exporter.setExporterInput(new SimpleExporterInput(jr));

			SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
			output.setGraphics2D(g2d);
			exporter.setExporterOutput(output);

			SimpleGraphics2DReportConfiguration grxConfiguration = new SimpleGraphics2DReportConfiguration();
			grxConfiguration.setPageIndex(rViewer.getPageIndex());
			grxConfiguration.setZoomRatio(zoom);
			exporter.setConfiguration(grxConfiguration);

			exporter.exportReport();

			g2d.setColor(Color.black);
			g2d.setStroke(new BasicStroke(1));
			g2d.drawRect(0, 0, (int) (img.getWidth() / zoom), (int) (img.getHeight() / zoom));
		} finally {
			g2d.dispose();
		}
		return UIUtils.awt2Swt(img);
	}

	/**
	 * Attaches the report viewer to the canvas
	 * 
	 * @param viewer
	 *          the viewer
	 */
	public void setReportViewer(IReportViewer viewer) {
		if (rViewer != null)
			rViewer.removeReportViewerListener(listener);
		this.rViewer = viewer;
		if (rViewer != null)
			rViewer.addReportViewerListener(listener);
		refresh();
	}

	private void repaint() {
		if (isDisposed())
			return;
		GC gc = new GC(this);
		try {
			paint(gc);
		} finally {
			gc.dispose();
		}
	}

	private void paint(GC gc) {
		Rectangle ca = getClientArea();
		ScrollBar hBar = getHorizontalBar();
		ScrollBar vBar = getVerticalBar();
		if (reportImage != null) {
			Rectangle b = reportImage.getBounds();
			int x;
			gc.fillRectangle(0, 0, ca.width, ca.height);

			if (b.width <= ca.width)
				x = (ca.width - b.width) / 2;
			else {
				x = -getHorizontalBar().getSelection() + MARGIN;

				// if (x > 0) {
				// draw left margin
				// gc.fillRectangle(0, 0, x, ca.height);
				// }

				// if (x + b.width < ca.width) {
				// draw right margin
				// gc.fillRectangle(x + b.width, 0, ca.width - x - b.width, b.height);
				// }
			}

			int y;
			if (b.height <= ca.height)
				y = (ca.height - b.height) / 2;
			else {
				y = -getVerticalBar().getSelection() + MARGIN;

				// if (y > 0) {
				// draw top margin
				// gc.fillRectangle(0, 0, ca.width, y);
				// }

				// if (y + b.height < ca.height) {
				// draw bottom margin
				// gc.fillRectangle(0, y + b.height, ca.width, ca.height - y -
				// b.height);
				// }
			}
			gc.drawImage(reportImage, x, y);
		} else if (errorMessage != null) {
			Rectangle b = getContentBounds();
			int x = b.width > ca.width ? -hBar.getSelection() : 0;
			int y = b.height > ca.height ? -vBar.getSelection() : 0;
			gc.fillRectangle(ca);
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_RED));
			gc.drawText(errorMessage, x + MARGIN, y + MARGIN);
		}
	}

	protected void refresh() {
		if (rViewer != null && hasReport()) {
			try {
				List<JRPrintPage> pages = rViewer.getReport().getPages();
				if (pages.isEmpty())
					refresh(null, "Document is Empty", null);
				else
					refresh(renderPage(), null, pages.get(rViewer.getPageIndex()));
			} catch (Throwable e) {
				e.printStackTrace();
				refresh(null, e.getMessage(), null);
			}
		} else
			refresh(null, null, null);
		setFocus();
	}

	private void refresh(Image reportImage, String errorMessage, JRPrintPage page) {
		if (this.reportImage != null)
			this.reportImage.dispose();
		if (isDisposed())
			return;

		this.reportImage = reportImage;
		this.errorMessage = errorMessage;
		this.page = page;

		setCursor(null);
		setToolTipText(null);
		currentLink = null;
		initHypelinks();

		ScrollBar hBar = getHorizontalBar();
		ScrollBar vBar = getVerticalBar();

		hBar.setSelection(0);
		vBar.setSelection(0);
		if (!updatingScrollbars)
			try {
				updatingScrollbars = true;
				Rectangle b = getContentBounds();

				boolean hVisible = hasHScroll(b.width, false);
				boolean vVisible = hasVScroll(b.height, hVisible);
				if (!hVisible && vVisible)
					hVisible = hasHScroll(b.width, vVisible);
				Point size = getSize();
				Rectangle clientArea = getClientArea();

				setupScrollBar(hBar, hVisible, clientArea.width, b.width, size.x);
				setupScrollBar(vBar, vVisible, clientArea.height, b.height, size.y);
			} finally {
				updatingScrollbars = false;
			}
		redraw();
	}

	boolean updatingScrollbars;

	private void setupScrollBar(ScrollBar sb, boolean visible, int ca, int b, int size) {
		if (sb == null)
			return;
		sb.setVisible(visible);
		if (!visible)
			sb.setSelection(0);
		else {
			sb.setPageIncrement(ca - sb.getIncrement());
			int max = b + size - ca;
			sb.setMaximum(max);
			sb.setThumb(size > max ? max : size);
		}
	}

	private boolean hasHScroll(int w, boolean visible) {
		if (getHorizontalBar() == null)
			return false;
		Rectangle b = getBounds();
		b.width -= 2 * getBorderWidth();
		ScrollBar vBar = getVerticalBar();
		if (visible && vBar != null)
			b.width -= vBar.getSize().x;
		return w > b.width;
	}

	private boolean hasVScroll(int h, boolean visible) {
		if (getVerticalBar() == null)
			return false;
		Rectangle b = getBounds();
		b.height -= 2 * getBorderWidth();
		ScrollBar hBar = getHorizontalBar();
		if (visible && hBar != null)
			b.height -= hBar.getSize().y;
		return h > b.height;
	}

	@Override
	public void dispose() {
		if (reportImage != null)
			reportImage.dispose();
		super.dispose();
	}

	public Point getFitSize() {
		Rectangle b = getBounds();
		int borderWidth = getBorderWidth();
		return new Point(b.width - 2 * borderWidth - 2 * MARGIN, b.height - 2 * borderWidth - 2 * MARGIN);
	}

	public Point getFitSize(int width, int height) {
		Point size = getFitSize();
		boolean vbar = false;
		boolean hbar = false;

		if (width > size.x) {
			size.y -= getHorizontalBar().getSize().y;
			hbar = true;
		}
		if (height > size.y) {
			size.x -= getVerticalBar().getSize().x;
			vbar = true;
		}
		if (vbar && !hbar && width > size.x)
			size.y -= getHorizontalBar().getSize().y;
		return size;
	}

	private void updateHyperlink(int x, int y) {
		JRPrintHyperlink link = getHyperlinkAt(x, y);
		if (link != null) {
			if (currentLink == null || currentLink != link) {
				currentLink = link;
				setCursor(CURSOR_HAND);
				setToolTipText(getLinkToolTip(link));
			}
		} else if (currentLink != null) {
			currentLink = null;
			setCursor(null);
			setToolTipText(null);
		}
	}

	private void handleHyperlinkClick() {
		switch (currentLink.getHyperlinkTypeValue()) {
		case REFERENCE:
		case REMOTE_ANCHOR:
		case REMOTE_PAGE:
		case CUSTOM:
			notifyHyperlinkListeners(currentLink);
			break;
		case LOCAL_ANCHOR:
			Map<String, JRPrintAnchorIndex> anchorIndexes = rViewer.getReport().getAnchorIndexes();
			JRPrintAnchorIndex indx = anchorIndexes.get(currentLink.getHyperlinkAnchor());
			if (indx == null)
				return;
			if (indx.getPageIndex() != rViewer.getPageIndex())
				rViewer.setPageIndex(indx.getPageIndex());

			if (reportImage != null) {
				JRPrintElement p = indx.getElement();
				setScrollBarSelection(getHorizontalBar(), (int) (p.getX() * zoom));
				setScrollBarSelection(getVerticalBar(), (int) (p.getY() * zoom));
				repaint();
			}
			break;
		case LOCAL_PAGE:
			rViewer.setPageIndex(currentLink.getHyperlinkPage().intValue() - 1);
			break;
		}
	}

	private void setScrollBarSelection(ScrollBar sb, int selection) {
		sb.setSelection(Math.max(sb.getMinimum(), Math.min(selection, sb.getMaximum())));
	}

	private Rectangle getContentBounds() {
		if (reportImage != null) {
			Rectangle bounds = reportImage.getBounds();
			bounds.width += 2 * MARGIN;
			bounds.height += 2 * MARGIN;
			return bounds;
		} else if (errorMessage != null) {
			GC gc = new GC(this);
			Point extent = gc.textExtent(errorMessage);
			gc.dispose();

			return new Rectangle(0, 0, extent.x + 2 * MARGIN, extent.y + 2 * MARGIN);
		}

		return new Rectangle(0, 0, 0, 0);
	}

	private JRPrintHyperlink getHyperlinkAt(int x, int y) {
		Rectangle b = reportImage.getBounds();
		Rectangle ca = getClientArea();

		int oX = b.width <= ca.width ? (ca.width - b.width) / 2 : -getHorizontalBar().getSelection() + MARGIN;
		int oY = b.height <= ca.height ? (ca.height - b.height) / 2 : -getVerticalBar().getSelection() + MARGIN;

		Point point = new Point((int) ((x - oX - ca.x) / zoom), (int) ((y - oY - ca.y) / zoom));

		for (ListIterator<IHyperlinkContainer> it = links.listIterator(links.size()); it.hasPrevious();) {
			JRPrintHyperlink hyperlink = it.previous().getHyperlink(point);
			if (hyperlink != null)
				return hyperlink;
		}
		return null;
	}

	private void initHypelinks() {
		links.clear();
		if (page == null)
			return;
		List<JRPrintElement> elements = page.getElements();
		if (elements == null)
			return;

		for (JRPrintElement element : elements) {
			if (element instanceof JRPrintImage) {
				Renderable r = ((JRPrintImage) element).getRenderable();
				if (r instanceof ImageMapRenderable) {
					try {
						List<JRPrintImageAreaHyperlink> hyperlinks = ((ImageMapRenderable) r).getImageAreaHyperlinks(new java.awt.Rectangle(0, 0, element.getWidth(), element.getHeight()));
						if (hyperlinks != null)
							links.add(new ImageAreaHyperlink(element.getX(), element.getY(), hyperlinks));
					} catch (JRException e) {
						throw new RuntimeException(e);
					}
				}
			} else if (element instanceof JRPrintHyperlink && !((JRPrintHyperlink) element).getHyperlinkTypeValue().equals(HyperlinkTypeEnum.NONE))
				links.add(new PrintHyperlink(0, 0, element));
		}
	}

	private static interface IHyperlinkContainer {
		public JRPrintHyperlink getHyperlink(Point point);
	}

	private static class PrintHyperlink implements IHyperlinkContainer {
		private final int x;

		private final int y;

		private final JRPrintElement element;

		public PrintHyperlink(int originX, int originY, JRPrintElement element) {
			this.x = originX + element.getX();
			this.y = originY + element.getY();
			this.element = element;
		}

		public JRPrintHyperlink getHyperlink(Point point) {
			if (point.x >= x && point.x < x + element.getWidth() && point.y >= y && point.y < y + element.getHeight())
				return (JRPrintHyperlink) element;
			return null;
		}
	}

	private static class ImageAreaHyperlink implements IHyperlinkContainer {
		private int originX;
		private int originY;

		private List<JRPrintImageAreaHyperlink> imageAreaHyperlinks;

		public ImageAreaHyperlink(int originX, int originY, List<JRPrintImageAreaHyperlink> imageAreaHyperlinks) {
			this.originX = originX;
			this.originY = originY;
			this.imageAreaHyperlinks = imageAreaHyperlinks;
		}

		public JRPrintHyperlink getHyperlink(Point point) {
			int x = point.x - originX;
			int y = point.y - originY;

			for (JRPrintImageAreaHyperlink areaHyperlink : imageAreaHyperlinks)
				if (areaHyperlink.getArea().containsPoint(x, y))
					return areaHyperlink.getHyperlink();
			return null;
		}
	}

	private String getLinkToolTip(JRPrintHyperlink link) {
		String tTip = link.getHyperlinkTooltip();
		if (tTip == null) {
			HyperlinkTypeEnum hType = link.getHyperlinkTypeValue();
			if (hType.equals(HyperlinkTypeEnum.REFERENCE))
				tTip = link.getHyperlinkReference();
			else if (hType.equals(HyperlinkTypeEnum.LOCAL_ANCHOR)) {
				if (link.getHyperlinkAnchor() != null)
					tTip = "#" + link.getHyperlinkAnchor(); //$NON-NLS-1$
			} else if (hType.equals(HyperlinkTypeEnum.LOCAL_PAGE)) {
				if (link.getHyperlinkPage() != null)
					tTip = "#page " + link.getHyperlinkPage(); //$NON-NLS-1$
			} else if (hType.equals(HyperlinkTypeEnum.REMOTE_ANCHOR)) {
				if (link.getHyperlinkReference() != null)
					tTip = link.getHyperlinkReference();
				if (currentLink.getHyperlinkAnchor() != null)
					tTip = "#" + currentLink.getHyperlinkAnchor(); //$NON-NLS-1$ 
			} else if (hType.equals(HyperlinkTypeEnum.REMOTE_PAGE)) {
				if (link.getHyperlinkReference() != null)
					tTip = link.getHyperlinkReference();
				if (link.getHyperlinkPage() != null)
					tTip = "#page " + link.getHyperlinkPage(); //$NON-NLS-1$ 
			}
		}
		return tTip;
	}

	private void notifyHyperlinkListeners(JRPrintHyperlink link) {
		for (JRHyperlinkListener l : rViewer.getHyperlinkListeners()) {
			try {
				l.gotoHyperlink(link);
			} catch (JRException e) {
				throw new RuntimeException(e);
			}
		}
	}

	public Image getActualImage() {
		return reportImage;
	}

}
