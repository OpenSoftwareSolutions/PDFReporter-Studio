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
package com.jaspersoft.studio.editor.gef.figures.svg;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.WeakHashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.util.XMLResourceDescriptor;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.gef.figures.ComponentFigure;
import com.jaspersoft.studio.editor.gef.figures.JRComponentFigure;

/**
 * Figure that support SVG element rendering.
 * <p>
 * 
 * NOTE: the code was taken from the GMF framework and was slightly modified in order to be used inside JSS. Right now
 * it extends {@link JRComponentFigure}.
 * 
 */
public class SVGFigure extends JRComponentFigure {

	private String uri;
	private boolean failedToLoadDocument, specifyCanvasWidth = true, specifyCanvasHeight = true;
	private SimpleImageTranscoder transcoder;

	private static WeakHashMap<String, Document> documentsMap = new WeakHashMap<String, Document>();

	public final String getURI() {
		return uri;
	}

	public final void setURI(String uri) {
		setURI(uri, true);
	}

	public void setURI(String uri, boolean loadOnDemand) {
		this.uri = uri;
		transcoder = null;
		failedToLoadDocument = false;
		if (loadOnDemand) {
			loadDocument();
		}
	}

	private void loadDocument() {
		transcoder = null;
		failedToLoadDocument = true;
		if (uri == null) {
			return;
		}
		String parser = XMLResourceDescriptor.getXMLParserClassName();
		SAXSVGDocumentFactory factory = new SAXSVGDocumentFactory(parser);
		try {
			Document document;
			if (documentsMap.containsKey(uri))
				document = documentsMap.get(uri);
			else {
				document = factory.createDocument(uri);
				documentsMap.put(uri, document);
			}
			transcoder = new SimpleImageTranscoder(document);
			failedToLoadDocument = false;
		} catch (IOException e) {
			JaspersoftStudioPlugin.getInstance().logError("Error loading SVG file", e);
		}
	}

	protected final Document getDocument() {
		if (failedToLoadDocument) {
			return null;
		}
		if (transcoder == null) {
			loadDocument();
		}
		return transcoder == null ? null : transcoder.getDocument();
	}

	/**
	 * Returns true if document was loaded without errors; tries to load document if needed.
	 */
	public final boolean checkContentAvailable() {
		return getDocument() != null;
	}

	private XPath getXPath() {
		XPath xpath = XPathFactory.newInstance().newXPath();
		xpath.setNamespaceContext(new InferringNamespaceContext(getDocument().getDocumentElement()));
		return xpath;
	}

	/**
	 * Executes XPath query over the SVG document.
	 */
	protected final NodeList getNodes(String query) {
		Document document = getDocument();
		if (document != null) {
			try {
				return (NodeList) getXPath().evaluate(query, document, XPathConstants.NODESET);
			} catch (XPathExpressionException e) {
				throw new RuntimeException(e);
			}
		}
		return null;
	}

	/**
	 * Reads color value from the document.
	 */
	protected Color getColor(Element element, String attributeName) {
		if (getDocument() == null || getDocument() != element.getOwnerDocument()) {
			return null;
		}
		Color color = null;
		// Make sure that CSSEngine is available.
		BridgeContext ctx = transcoder.initCSSEngine();
		try {
			color = SVGUtils.toSWTColor(element, attributeName);
		} finally {
			if (ctx != null) {
				ctx.dispose();
			}
		}
		return color;
	}

	@Override
	public void paint(Graphics graphics) {
		// super.paintFigure(graphics);
		Document document = getDocument();
		if (document == null) {
			return;
		}
		Rectangle r = getClientArea();
		transcoder.setCanvasSize(specifyCanvasWidth ? r.width : -1, specifyCanvasHeight ? r.height : -1);
		updateRenderingHints(graphics);
		BufferedImage awtImage = transcoder.getBufferedImage();
		if (awtImage != null) {
			int imgWidth = awtImage.getWidth(null);
			int imgHeight = awtImage.getHeight(null);
			Graphics2D g = ComponentFigure.getG2D(graphics);
			if (g != null) {
				// Delegate the painting to the AWT Graphics2D
				g.setColor(java.awt.Color.WHITE);
				g.fillRect(r.x, r.y, r.x + r.width, r.y + r.height);
				g.drawImage(awtImage, r.x, r.y, r.x + r.width, r.y + r.height, 0, 0, imgWidth, imgHeight, null);
			}
			paintBorder(graphics);
		}
	}

	private void updateRenderingHints(Graphics graphics) {
		{
			int aa = SWT.DEFAULT;
			try {
				aa = graphics.getAntialias();
			} catch (Exception e) {
				// not supported
			}
			Object aaHint;
			if (aa == SWT.ON) {
				aaHint = RenderingHints.VALUE_ANTIALIAS_ON;
			} else if (aa == SWT.OFF) {
				aaHint = RenderingHints.VALUE_ANTIALIAS_OFF;
			} else {
				aaHint = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
			}
			if (transcoder.getRenderingHints().get(RenderingHints.KEY_ANTIALIASING) != aaHint) {
				transcoder.getRenderingHints().put(RenderingHints.KEY_ANTIALIASING, aaHint);
				transcoder.contentChanged();
			}
		}
		{
			int aa = SWT.DEFAULT;
			try {
				aa = graphics.getTextAntialias();
			} catch (Exception e) {
				// not supported
			}
			Object aaHint;
			if (aa == SWT.ON) {
				aaHint = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
			} else if (aa == SWT.OFF) {
				aaHint = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
			} else {
				aaHint = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
			}
			if (transcoder.getRenderingHints().get(RenderingHints.KEY_TEXT_ANTIALIASING) != aaHint) {
				transcoder.getRenderingHints().put(RenderingHints.KEY_TEXT_ANTIALIASING, aaHint);
				transcoder.contentChanged();
			}
		}
	}

	public final Rectangle2D getAreaOfInterest() {
		getDocument();
		return transcoder == null ? null : transcoder.getCanvasAreaOfInterest();
	}

	public void setAreaOfInterest(Rectangle2D value) {
		getDocument();
		if (transcoder != null) {
			transcoder.setCanvasAreaOfInterest(value);
		}
		repaint();
	}

	public final boolean isSpecifyCanvasWidth() {
		return specifyCanvasWidth;
	}

	public void setSpecifyCanvasWidth(boolean specifyCanvasWidth) {
		this.specifyCanvasWidth = specifyCanvasWidth;
		contentChanged();
	}

	public final boolean isSpecifyCanvasHeight() {
		return specifyCanvasHeight;
	}

	public void setSpecifyCanvasHeight(boolean specifyCanvasHeight) {
		this.specifyCanvasHeight = specifyCanvasHeight;
		contentChanged();
	}

	/**
	 * Should be called when SVG document has been changed. It will be re-rendered and figure will be repainted.
	 */
	public void contentChanged() {
		getDocument();
		if (transcoder != null) {
			transcoder.contentChanged();
		}
		repaint();
	}
}
