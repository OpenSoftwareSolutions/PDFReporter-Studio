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
package com.jaspersoft.studio.server.wizard.resource.page;

import java.io.File;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MResource;

public class ImagePageContent extends AFileResourcePageContent {

	public ImagePageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public ImagePageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.image";
	}

	@Override
	public String getName() {
		return Messages.RDImagePage_title;
	}

	@Override
	protected void createFileTab(Composite composite) {
		final Point origin = new Point(0, 0);
		canvas = new Canvas(composite, SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		canvas.setLayoutData(gd);

		canvas.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				GC gc = e.gc;
				if (img != null) {
					Rectangle client = canvas.getClientArea();
					gc.fillRectangle(0, 0, client.width, client.height);
					gc.drawImage(img, origin.x, origin.y);
					canvas.getHorizontalBar().setVisible(true);
					canvas.getVerticalBar().setVisible(true);
				} else {
					Rectangle client = canvas.getClientArea();
					gc.fillRectangle(0, 0, client.width, client.height);
					e.gc.drawText(Messages.RDImagePage_noimage, 0, 0);
					canvas.getHorizontalBar().setVisible(false);
					canvas.getVerticalBar().setVisible(false);
				}
			}
		});
		final ScrollBar hBar = canvas.getHorizontalBar();
		hBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (img == null)
					return;
				int hSelection = hBar.getSelection();
				int destX = -hSelection - origin.x;
				Rectangle rect = img.getBounds();
				canvas.scroll(destX, 0, 0, 0, rect.width, rect.height, false);
				origin.x = -hSelection;
			}
		});
		final ScrollBar vBar = canvas.getVerticalBar();
		vBar.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (img == null)
					return;
				int vSelection = vBar.getSelection();
				int destY = -vSelection - origin.y;
				Rectangle rect = img.getBounds();
				canvas.scroll(0, destY, 0, 0, rect.width, rect.height, false);
				origin.y = -vSelection;
			}
		});
		resizeListener = new Listener() {
			public void handleEvent(Event e) {
				if (img != null) {
					Rectangle rect = img.getBounds();
					Rectangle client = canvas.getClientArea();
					hBar.setMaximum(rect.width);
					vBar.setMaximum(rect.height);
					hBar.setThumb(Math.min(rect.width, client.width));
					vBar.setThumb(Math.min(rect.height, client.height));
					int hPage = rect.width - client.width;
					int vPage = rect.height - client.height;
					int hSelection = hBar.getSelection();
					int vSelection = vBar.getSelection();
					if (hSelection >= hPage) {
						if (hPage <= 0)
							hSelection = 0;
						origin.x = -hSelection;
					}
					if (vSelection >= vPage) {
						if (vPage <= 0)
							vSelection = 0;
						origin.y = -vSelection;
					}
					canvas.redraw();
				}
			}
		};
		canvas.addListener(SWT.Resize, resizeListener);
	}

	private Image img;
	private Canvas canvas;
	private Listener resizeListener;

	@Override
	protected void handleFileChange() {
		super.handleFileChange();
		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				try {
					File f = ((AFileResource) res).getFile();
					if (f == null && !res.getValue().getIsNew()) {
						f = File.createTempFile("jrsimgfile", ".png"); //$NON-NLS-1$ //$NON-NLS-2$
						f.deleteOnExit();
						f.createNewFile();
						WSClientHelper.getResource(new NullProgressMonitor(), res, res.getValue(), f);
					}
					if (f != null && f.exists()) {
						if (img != null)
							img.dispose();
						img = new Image(Display.getDefault(), f.getAbsolutePath());
						resizeListener.handleEvent(null);

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void dispose() {
		super.dispose();
		if (img != null)
			img.dispose();
	}

	@Override
	protected String[] getFilter() {
		return new String[] { "*.*", "*.png", "*.jpg", "*.jpeg", "*.gif" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

}
