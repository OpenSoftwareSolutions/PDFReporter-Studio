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

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.model.AMSQLObject;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlSchema;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.metadata.MTables;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;

public class UsedColumnsDialog extends ATitledDialog {
	private TreeViewer treeViewer;
	private MRoot root;
	private List<MSQLColumn> cols = new ArrayList<MSQLColumn>();
	private List<MSqlTable> tables;
	private List<AMSQLObject> columns;

	public UsedColumnsDialog(Shell parentShell) {
		super(parentShell);
		setTitle("Columns Dialog");
		setDefaultSize(650, 780);
	}

	public void setRoot(MRoot root) {
		this.root = root;
	}

	public void setSelection(ANode sel) {
		tables = Util.getTables(sel);
		if (!(sel instanceof MExpression))
			columns = Util.getUsedColumns(sel);
	}

	@Override
	public boolean close() {
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		for (Object obj : ts.toList()) {
			if (obj instanceof MSQLColumn)
				cols.add((MSQLColumn) obj);
		}

		return super.close();
	}

	public List<MSQLColumn> getColumns() {
		return cols;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);

		treeViewer = new TreeViewer(cmp, SWT.SINGLE | SWT.BORDER);
		treeViewer.setContentProvider(new ReportTreeContetProvider() {
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MRoot) {
					List<INode> n = new ArrayList<INode>();
					MRoot p = (MRoot) parentElement;
					for (INode node : p.getChildren())
						if (node instanceof MSqlSchema && getChildrens4Schema((MSqlSchema) node).length > 0)
							n.add(node);
					return n.toArray();
				} else if (parentElement instanceof MSqlSchema)
					return getChildrens4Schema((MSqlSchema) parentElement);
				else if (parentElement instanceof MTables)
					return getChildrens4Tables((MTables) parentElement);
				return super.getChildren(parentElement);
			}

			protected Object[] getChildrens4Schema(MSqlSchema p) {
				if (p.getChildren() != null && p.getChildren().size() > 0) {
					List<INode> n = new ArrayList<INode>();
					for (INode node : p.getChildren())
						if (node instanceof MTables && getChildrens4Tables((MTables) node).length > 0)
							n.add(node);
					return n.toArray();
				}
				return new Object[0];
			}

			protected Object[] getChildrens4Tables(MTables p) {
				if (p.getChildren() != null && p.getChildren().size() > 0) {
					List<INode> n = new ArrayList<INode>();
					for (INode node : p.getChildren())
						if (tables.contains(node))
							n.add(node);
					return n.toArray();
				}
				return new Object[0];
			}

		});
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());
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
				getButton(IDialogConstants.OK_ID).setEnabled(el instanceof MSQLColumn);
			}
		});
		treeViewer.setInput(root);
		treeViewer.expandAll();
		return cmp;
	}
}
