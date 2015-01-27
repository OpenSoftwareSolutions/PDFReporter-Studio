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
package com.jaspersoft.studio.editor.preview.view.control;

import java.util.List;

import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.PrintBookmark;
import net.sf.jasperreports.engine.base.BasePrintBookmark;

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider.IStyledLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.StyledCellLabelProvider;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.editor.preview.PreviewJRPrint;
import com.jaspersoft.studio.editor.preview.view.APreview;
import com.jaspersoft.studio.editor.preview.view.report.swt.SWTViewer;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class VBookmarks extends APreview {
	private Composite composite;
	private TreeViewer treeViewer;
	private PreviewJRPrint pcontainer;

	public VBookmarks(Composite parent, JasperReportsConfiguration jContext, PreviewJRPrint pcontainer) {
		super(parent, jContext);
		this.pcontainer = pcontainer;
	}

	public void setJasperPrint(JasperPrint jrPrint) {
		BasePrintBookmark root = new BasePrintBookmark("Root", 0, "");
		if (jrPrint != null && jrPrint.getBookmarks() != null)
			for (PrintBookmark pb : jrPrint.getBookmarks())
				root.addBookmark(pb);
		treeViewer.setInput(root);
		treeViewer.expandToLevel(1);
	}

	@Override
	protected Control createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout());

		treeViewer = new TreeViewer(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		treeViewer.setContentProvider(new BookmarkContentProvider());
		treeViewer.setLabelProvider(new BookmarkLabelProvider());
		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				ITreeSelection sel = (ITreeSelection) event.getSelection();
				PrintBookmark pb = (PrintBookmark) sel.getFirstElement();
				APreview view = pcontainer.getDefaultViewer();
				if (view instanceof SWTViewer)
					((SWTViewer) view).setPageNumber(pb.getPageIndex() - 1);
			}
		});
		composite.addControlListener(new ControlListener() {

			@Override
			public void controlResized(ControlEvent e) {
				int h = composite.getSize().y;
				int w = composite.getSize().x;
				composite.setSize(composite.computeSize(w, h, true));
				composite.layout();
			}

			@Override
			public void controlMoved(ControlEvent e) {

			}
		});
		return composite;
	}

	private class BookmarkLabelProvider extends StyledCellLabelProvider implements IStyledLabelProvider {

		@Override
		public StyledString getStyledText(Object element) {
			if (element instanceof PrintBookmark) {
				String label = ((PrintBookmark) element).getLabel();
				return new StyledString(Misc.nvl(label));
			}
			return new StyledString("");
		}

		@Override
		public String getToolTipText(Object element) {
			if (element instanceof PrintBookmark) {
				PrintBookmark pb = (PrintBookmark) element;
				return pb.getLabel() + "\nAddress: " + pb.getElementAddress() + "\nPage: " + pb.getPageIndex();
			}
			return "";
		}

		@Override
		public void update(ViewerCell cell) {
			try {
				Object element = cell.getElement();
				StyledString st = getStyledText(element);
				cell.setText(st.getString());
				cell.setStyleRanges(getStyledText(element).getStyleRanges());
				cell.setImage(getImage(element));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public Image getImage(Object element) {
			return JaspersoftStudioPlugin.getInstance().getImage("icons/blue-document-bookmark.png");
		}
	}

	private class BookmarkContentProvider implements ITreeContentProvider {
		private Object[] EMPTY_ARRAY = new Object[0];

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

		}

		@Override
		public void dispose() {

		}

		@Override
		public boolean hasChildren(Object element) {
			PrintBookmark pb = (PrintBookmark) element;
			if (pb.getBookmarks() != null)
				return !pb.getBookmarks().isEmpty();
			return false;
		}

		@Override
		public Object getParent(Object element) {
			return null;
		}

		@Override
		public Object[] getElements(Object element) {
			if (element instanceof List)
				return ((List<?>) element).toArray();
			return getChildren(element);
		}

		@Override
		public Object[] getChildren(Object element) {
			PrintBookmark pb = (PrintBookmark) element;
			if (pb.getBookmarks() != null)
				return pb.getBookmarks().toArray();
			return EMPTY_ARRAY;
		}
	};
}
