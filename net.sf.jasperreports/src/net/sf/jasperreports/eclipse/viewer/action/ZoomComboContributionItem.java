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
package net.sf.jasperreports.eclipse.viewer.action;

import java.text.DecimalFormat;

import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.eclipse.viewer.IReportViewerListener;
import net.sf.jasperreports.eclipse.viewer.ReportViewerEvent;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class ZoomComboContributionItem extends ContributionItem {
	private SelectionListener selListener = new SelectionListener() {

		@Override
		public void widgetSelected(SelectionEvent e) {
			if (isRefresh)
				return;
			onSelection();
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	};
	private FocusListener fListener = new FocusListener() {

		@Override
		public void focusLost(FocusEvent e) {
		}

		@Override
		public void focusGained(FocusEvent e) {
			if (isRefresh)
				return;
			// refresh();
		}
	};
	private IReportViewerListener viewListener = new IReportViewerListener() {

		@Override
		public void viewerStateChanged(ReportViewerEvent evt) {
			refresh();
		}
	};

	private static DecimalFormat ZOOM_FORMAT = new DecimalFormat("####%"); //$NON-NLS-1$

	private IReportViewer rViewer;
	private Combo zCombo;
	private ToolItem tItem;
	private float[] zoomLevels;
	private boolean isRefresh = false;

	/**
	 * report zoom contribution item.
	 * 
	 * @param viewer
	 *          the report viewer
	 */
	public ZoomComboContributionItem(IReportViewer viewer) {
		Assert.isNotNull(viewer);
		this.rViewer = viewer;
		this.rViewer.addReportViewerListener(viewListener);
	}

	private void refresh() {
		if (zCombo == null || zCombo.isDisposed())
			return;
		try {
			zCombo.setEnabled(rViewer.canChangeZoom());
			if (!rViewer.canChangeZoom())
				zCombo.setText(""); //$NON-NLS-1$
			else
				setZoom();
		} catch (SWTException exception) {
			if (!SWT.getPlatform().equals("gtk")) //$NON-NLS-1$
				throw exception;
		}
	}

	private String[] getZoomLevelsAsText() {
		zoomLevels = rViewer.getZoomLevels();
		if (zoomLevels == null)
			return new String[] { "100%" }; //$NON-NLS-1$
		else {
			String[] textZoomLevels = new String[zoomLevels.length];
			for (int i = 0; i < textZoomLevels.length; i++)
				textZoomLevels[i] = ZOOM_FORMAT.format(zoomLevels[i]);
			return textZoomLevels;
		}
	}

	private Control createControl(Composite parent) {
		zCombo = new Combo(parent, SWT.DROP_DOWN);
		zCombo.addSelectionListener(selListener);
		zCombo.addFocusListener(fListener);
		zCombo.setItems(getZoomLevelsAsText()); //$NON-NLS-1$
		zCombo.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (isRefresh)
					return;
				String t = zCombo.getText();
				int end = t.indexOf("%");
				if (e.end > end) {
					e.doit = false;
					zCombo.setSelection(new Point(end - 1, end - 1));
				}
				if (!Character.isDigit(e.character) && !(e.character == SWT.BS || e.character == SWT.DEL || e.character == SWT.CR)) {
					e.doit = false;
					return;
				}
				if (e.character == SWT.DEL && !Character.isDigit(zCombo.getText().charAt(e.start)))
					e.doit = false;
			}
		});

		refresh();
		zCombo.pack();
		return zCombo;
	}

	/**
	 * @see org.eclipse.jface.action.ContributionItem#dispose()
	 */
	public void dispose() {
		zCombo = null;
		rViewer.removeReportViewerListener(viewListener);
		rViewer = null;
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Composite)
	 */
	public final void fill(Composite parent) {
		createControl(parent);
	}

	/**
	 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.ToolBar,
	 *      int)
	 */
	public void fill(ToolBar parent, int index) {
		tItem = new ToolItem(parent, SWT.SEPARATOR, index);
		Control ctrl = createControl(parent);
		tItem.setWidth(zCombo.getSize().x);
		tItem.setControl(ctrl);
	}

	private void onSelection() {
		if (isRefresh)
			return;
		if (rViewer.hasReport()) {
			int ind = zCombo.getSelectionIndex();
			setZoomAsText(ind >= 0 ? zCombo.getItem(ind) : zCombo.getText());
		}
	}

	private void setZoomAsText(String value) {
		int ind = value.indexOf('%');
		if (ind != -1)
			value = value.substring(0, ind);
		try {
			final float zoom = Float.parseFloat(value) / 100;
			if (zoom != rViewer.getZoom())
				BusyIndicator.showWhile(null, new Runnable() {
					public void run() {
						rViewer.setZoom(zoom);
					}
				});
		} catch (NumberFormatException e) {
		}
	}

	protected void setZoom() {
		isRefresh = true;
		String zoom = ZOOM_FORMAT.format(rViewer.getZoom());
		zCombo.setText(zoom);
		isRefresh = false;
	}

}
