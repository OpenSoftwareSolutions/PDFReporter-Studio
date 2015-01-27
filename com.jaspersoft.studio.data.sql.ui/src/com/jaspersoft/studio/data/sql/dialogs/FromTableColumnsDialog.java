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
package com.jaspersoft.studio.data.sql.dialogs;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;

public class FromTableColumnsDialog extends ATitledDialog {
	private final class MetaDataListener implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent evt) {

			treeViewer.refresh(true);
		}
	}

	private TreeViewer treeViewer;
	private Map<MSQLColumn, MFromTable> cols = new HashMap<MSQLColumn, MFromTable>();
	private MFrom root;
	private int style = SWT.MULTI;
	private MetaDataListener metaDataListener;

	public FromTableColumnsDialog(Shell parentShell) {
		super(parentShell);
		setTitle(Messages.FromTableColumnsDialog_0);
		setDefaultSize(650, 780);
	}

	public FromTableColumnsDialog(Shell parentShell, int style) {
		this(parentShell);
		this.style = style;
	}

	public void setSelection(ANode sel) {
		INode mr = Util.getQueryRoot(sel);
		if (mr == null)
			mr = sel.getRoot();
		if (mr != null)
			for (INode n : mr.getChildren())
				if (n instanceof MFrom) {
					root = (MFrom) n;
					break;
				}
	}

	@Override
	public boolean close() {
		if (root != null && root.getRoot() != null)
			root.getRoot().getPropertyChangeSupport()
					.removePropertyChangeListener(metaDataListener);
		if (getReturnCode() == OK) {
			TreeSelection ts = (TreeSelection) treeViewer.getSelection();
			for (TreePath tp : ts.getPaths())
				if (tp.getSegmentCount() == 2)
					cols.put((MSQLColumn) tp.getLastSegment(),
							(MFromTable) tp.getFirstSegment());
		}
		return super.close();
	}

	public Map<MSQLColumn, MFromTable> getColumns() {
		return cols;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);

		treeViewer = new TreeViewer(cmp, style | SWT.BORDER);
		treeViewer.setContentProvider(new ReportTreeContetProvider() {
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MFrom) {
					List<MFromTable> tables = Util
							.getFromTables((MFrom) parentElement);
					return tables.toArray();
				} else if (parentElement instanceof MFromTable) {
					MFromTable mftable = (MFromTable) parentElement;
					return mftable.getValue().getChildren().toArray();
				}
				return super.getChildren(parentElement);
			}

		});
		treeViewer.setLabelProvider(new ReportTreeLabelProvider() {

			@Override
			public Image getImage(Object element) {
				if (element instanceof MFromTable) {
					ImageDescriptor imagePath = ((MFromTable) element)
							.getValue().getImagePath();
					if (imagePath != null)
						return JaspersoftStudioPlugin.getInstance().getImage(
								imagePath);
				}
				return super.getImage(element);
			}

			@Override
			public StyledString getStyledText(Object element) {
				if (element instanceof MFromTableJoin) {
					MFromTableJoin mft = (MFromTableJoin) element;
					StyledString ss = new StyledString(mft.getValue()
							.toSQLString());
					mft.addAlias(ss);
					return ss;
				}
				return super.getStyledText(element);
			}
		});
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 400;
		gd.minimumWidth = 400;
		treeViewer.getControl().setLayoutData(gd);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection ts = (TreeSelection) treeViewer.getSelection();
				Object el = ts.getFirstElement();
				if (el instanceof MSQLColumn)
					okPressed();
				else {
					if (treeViewer.getExpandedState(el))
						treeViewer.collapseToLevel(el, 1);
					else
						treeViewer.expandToLevel(el, 1);
				}
			}
		});
		ColumnViewerToolTipSupport.enableFor(treeViewer);
		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				TreeSelection ts = (TreeSelection) treeViewer.getSelection();
				Object el = ts.getFirstElement();
				getButton(IDialogConstants.OK_ID).setEnabled(
						el instanceof MSQLColumn);
			}
		});
		treeViewer.setInput(root);
		metaDataListener = new MetaDataListener();
		if (root != null && root.getRoot() != null)
			root.getRoot().getPropertyChangeSupport()
					.addPropertyChangeListener(metaDataListener);

		treeViewer.expandAll();
		return cmp;
	}
}
