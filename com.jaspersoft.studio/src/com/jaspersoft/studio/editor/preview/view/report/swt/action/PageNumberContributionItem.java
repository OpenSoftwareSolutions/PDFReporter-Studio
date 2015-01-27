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
package com.jaspersoft.studio.editor.preview.view.report.swt.action;

import java.text.MessageFormat;

import net.sf.jasperreports.eclipse.viewer.IReportViewer;
import net.sf.jasperreports.eclipse.viewer.IReportViewerListener;
import net.sf.jasperreports.eclipse.viewer.ReportViewerEvent;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.messages.Messages;

public class PageNumberContributionItem extends ContributionItem {

	private IReportViewer viewer;
	private Text text;
	private ToolItem toolitem;
	private IReportViewerListener viewListener = new IReportViewerListener() {

		@Override
		public void viewerStateChanged(ReportViewerEvent evt) {
			refresh();
		}
	};
	private SelectionListener selListener = new SelectionListener() {
		public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
			if (isRefresh)
				return;
			if (viewer.hasReport())
				setPageNumber(text.getText());

		};

		public void widgetDefaultSelected(org.eclipse.swt.events.SelectionEvent e) {
			widgetSelected(e);
		};
	};

	/**
	 * Show page number.
	 * 
	 * @param viewer
	 *          the report viewer
	 */
	public PageNumberContributionItem(IReportViewer viewer) {
		Assert.isNotNull(viewer);
		this.viewer = viewer;
		viewer.addReportViewerListener(viewListener);
	}

	void refresh() {
		if (text == null || text.isDisposed())
			return;
		boolean hasDoc = viewer.hasReport();
		text.setEnabled(hasDoc);
		setText(hasDoc ? getPageMofNText() : MessageFormat.format(Messages.PageNumberContributionItem_page, "....", "...."));
	}

	private Control createControl(Composite parent) {
		text = new Text(parent, SWT.BORDER | SWT.CENTER);
		text.addSelectionListener(selListener);
		text.addVerifyListener(new VerifyListener() {
			@Override
			public void verifyText(VerifyEvent e) {
				if (isRefresh)
					return;
				if (e.start < start) {
					e.doit = false;
					text.setSelection(start);
				}
				String t = text.getText().substring(start);
				int end = t.indexOf(" ") + start;
				if (e.end > end + 1) {
					e.doit = false;
					text.setSelection(end - 1);
				}
				if (!Character.isDigit(e.character)
						&& !(e.character == SWT.BS || e.character == SWT.DEL || e.character == SWT.CR)) {
					e.doit = false;
					return;
				}
				if (e.character == SWT.DEL && !Character.isDigit(text.getText().charAt(e.start)))
					e.doit = false;
			}
		});

		refresh();
		text.pack();
		return text;
	}

	private static int start = Messages.PageNumberContributionItem_page.indexOf("{0}");

	private boolean isRefresh = false;

	private void setText(String txt) {
		isRefresh = true;
		Point oldSel = text.getSelection();
		text.setText(txt);
		text.setSelection(oldSel);
		isRefresh = false;
	}

	private void setPageNumber(String pageText) {
		pageText = pageText.substring(start);
		pageText = pageText.substring(0, pageText.indexOf(" ")).trim();

		try {
			final int pageIndex = Integer.parseInt(pageText);
			BusyIndicator.showWhile(null, new Runnable() {
				public void run() {
					viewer.setPageIndex(pageIndex - 1);
				}
			});
		} catch (NumberFormatException e) {
		}
		setText(getPageMofNText());
	}

	private String getPageMofNText() {
		return MessageFormat.format(Messages.PageNumberContributionItem_page, new Integer(viewer.getPageIndex() + 1),
				new Integer(viewer.getReport().getPages().size()));
	}

	public void dispose() {
		viewer.removeReportViewerListener(viewListener);
		text = null;
		viewer = null;
	}

	public final void fill(Composite parent) {
		createControl(parent);
	}

	public final void fill(Menu parent, int index) {
	}

	public void fill(ToolBar parent, int index) {
		toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
		Control control = createControl(parent);
		toolitem.setWidth(control.getSize().x);
		toolitem.setControl(control);
	}

}
