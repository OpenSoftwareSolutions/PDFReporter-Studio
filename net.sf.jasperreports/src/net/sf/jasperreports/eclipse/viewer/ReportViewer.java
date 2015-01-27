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

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.view.JRHyperlinkListener;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class ReportViewer implements IReportViewer {

	private JasperPrint document;
	private int pageIndex;
	private int style;

	private ViewerCanvas viewerComposite;

	private List<JRHyperlinkListener> hyperlinkListeners;
	private JasperReportsContext jContext;

	public ReportViewer(JasperReportsContext jContext) {
		this(SWT.NONE, jContext);
	}

	public ReportViewer(int style, JasperReportsContext jContext) {
		this.style = style;
		this.jContext = jContext;
	}

	public void exportImage(String file, int width, int height) {
		ImageLoader loader = new ImageLoader();
		Image actualImage = viewerComposite.getActualImage();
		int resizeWidth = width > 0 ? width : actualImage.getBounds().width;
		int resizeHeight = height > 0 ? height : actualImage.getBounds().height;
		loader.data = new ImageData[] { actualImage.getImageData().scaledTo(resizeWidth, resizeHeight) };
		loader.save(file, SWT.IMAGE_PNG);
	}

	public String getReportPath() {
		IFile reportFile = (IFile) jContext.getValue(FileUtils.KEY_FILE);
		if (reportFile != null) {
			String fileName = reportFile.getName();
			String path = reportFile.getLocation().toPortableString();
			path = path.substring(0, path.lastIndexOf(fileName));
			return path;
		}
		return null;
	}

	public String getReportName() {
		IFile reportFile = (IFile) jContext.getValue(FileUtils.KEY_FILE);
		if (reportFile != null) {
			String fileName = reportFile.getName();
			String extension = reportFile.getFileExtension();
			String path = reportFile.getLocation().toPortableString();
			path = path.substring(0, path.lastIndexOf(fileName));
			fileName = fileName.substring(0, fileName.lastIndexOf(extension) - 1);
			return fileName;
		}
		return null;
	}

	public void setReport(JasperPrint document) {
		try {
			if (this.document == document)
				return;
			this.document = document;
			this.pageIndex = Math.min(Math.max(0, pageIndex), getPageCount() - 1);
			if (viewerComposite != null)
				viewerComposite.setZoomInternal(viewerComposite.computeZoom());
			fireViewerModelChanged();
		} catch (OutOfMemoryError e) {
			this.document = null;
		}
	}

	public boolean hasReport() {
		return document != null;
	}

	public JasperPrint getReport() {
		return document;
	}

	public void setZoom(float zoom) {
		viewerComposite.setZoom(zoom);
	}

	public boolean canChangeZoom() {
		return viewerComposite.hasReport();
	}

	public int getZoomMode() {
		return viewerComposite.getZoomMode();
	}

	public void setZoomMode(int zoomMode) {
		viewerComposite.setZoomMode(zoomMode);
	}

	public float[] getZoomLevels() {
		return viewerComposite.getZoomLevels();
	}

	public void zoomIn() {
		viewerComposite.zoomIn();
	}

	public boolean canZoomIn() {
		return viewerComposite.canZoomIn();
	}

	public float getZoom() {
		return viewerComposite.getZoom();
	}

	public void zoomOut() {
		viewerComposite.zoomOut();
	}

	public boolean canZoomOut() {
		return viewerComposite.canZoomOut();
	}

	private int getPageCount() {
		return document == null ? 0 : document.getPages().size();
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		if (pageIndex != getPageIndex()) {
			this.pageIndex = Math.min(Math.max(0, pageIndex), getPageCount() - 1);
			fireViewerModelChanged();
		}
	}

	public boolean canGotoFirstPage() {
		return hasReport() && pageIndex > 0;
	}

	public void gotoFirstPage() {
		if (this.pageIndex != 0 && canGotoFirstPage())
			setPageIndex(0);
	}

	public boolean canGotoLastPage() {
		return hasReport() && pageIndex < getPageCount() - 1;
	}

	public void gotoLastPage() {
		if (canGotoLastPage()) {
			setPageIndex(getPageCount() - 1);
		}
	}

	public boolean canGotoNextPage() {
		return hasReport() && pageIndex < getPageCount() - 1;
	}

	public void gotoNextPage() {
		if (canGotoNextPage()) {
			setPageIndex(pageIndex + 1);
		}
	}

	public boolean canGotoPreviousPage() {
		return hasReport() && pageIndex > 0;
	}

	public void gotoPreviousPage() {
		if (canGotoPreviousPage()) {
			setPageIndex(pageIndex - 1);
		}
	}

	private Set<IReportViewerListener> listenerSet = new LinkedHashSet<IReportViewerListener>();

	public void addReportViewerListener(IReportViewerListener listener) {
		listenerSet.add(listener);
	}

	public void removeReportViewerListener(IReportViewerListener listener) {
		listenerSet.remove(listener);
	}

	public void fireViewerModelChanged() {
		ReportViewerEvent e = new ReportViewerEvent(this);
		for (IReportViewerListener l : listenerSet)
			l.viewerStateChanged(e);
	}

	public Control createControl(Composite parent) {
		if (viewerComposite == null) {
			viewerComposite = new ViewerCanvas(parent, style, jContext);
			viewerComposite.setReportViewer(this);
		}
		return viewerComposite;
	}

	public Control getControl() {
		return viewerComposite;
	}

	public void addHyperlinkListener(JRHyperlinkListener listener) {
		if (hyperlinkListeners == null) {
			hyperlinkListeners = new ArrayList<JRHyperlinkListener>();
		} else {
			hyperlinkListeners.remove(listener); // add once
		}

		hyperlinkListeners.add(listener);
	}

	public void removeHyperlinkListener(JRHyperlinkListener listener) {
		if (hyperlinkListeners != null)
			hyperlinkListeners.remove(listener);
	}

	public JRHyperlinkListener[] getHyperlinkListeners() {
		return hyperlinkListeners == null ? new JRHyperlinkListener[0] : (JRHyperlinkListener[]) hyperlinkListeners.toArray(new JRHyperlinkListener[hyperlinkListeners.size()]);
	}
}
