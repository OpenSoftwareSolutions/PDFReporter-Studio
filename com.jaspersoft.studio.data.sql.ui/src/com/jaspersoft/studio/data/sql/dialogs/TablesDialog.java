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

import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.data.sql.model.metadata.MSqlSchema;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.metadata.MTables;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;

public class TablesDialog extends ATitledDialog {
	private TreeViewer treeViewer;
	private MRoot root;
	private List<MSqlTable> table = new ArrayList<MSqlTable>();

	public TablesDialog(Shell parentShell) {
		super(parentShell);
		setTitle("Tables Dialog");
		setDefaultSize(650, 780);
	}

	public void setRoot(MRoot root) {
		this.root = root;
	}

	@Override
	public boolean close() {
		TreeSelection ts = (TreeSelection) treeViewer.getSelection();
		for (Object obj : ts.toList()) {
			if (obj instanceof MSqlTable)
				table.add((MSqlTable) obj);
		}

		return super.close();
	}

	public List<MSqlTable> getTable() {
		return table;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);

		treeViewer = new TreeViewer(cmp, SWT.MULTI | SWT.BORDER);
		treeViewer.setContentProvider(new ReportTreeContetProvider() {
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof MSqlSchema) {
					MSqlSchema p = (MSqlSchema) parentElement;
					if (p.getChildren() != null && p.getChildren().size() > 0) {
						List<INode> n = new ArrayList<INode>();
						for (INode node : p.getChildren()) {
							if (node instanceof MTables)
								n.add(node);
						}
						return n.toArray();
					}
				}
				return super.getChildren(parentElement);
			}

			@Override
			public boolean hasChildren(Object element) {
				if (element instanceof MSqlTable)
					return false;
				return super.hasChildren(element);
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
				if (el instanceof MSqlTable)
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

		treeViewer.setInput(root);
		return cmp;
	}
}
