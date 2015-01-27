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

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.action.ActionFactory;
import com.jaspersoft.studio.data.sql.action.DeleteAction;
import com.jaspersoft.studio.data.sql.action.expression.ChangeOperator;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpression;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpressionGroup;
import com.jaspersoft.studio.data.sql.action.expression.CreateXExpression;
import com.jaspersoft.studio.data.sql.action.expression.EditExpression;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;

public class JoinFromTableDialog extends ATitledDialog {
	private MFromTable srcTable;
	private String fromTable;
	private String join = AMKeyword.INNER_JOIN;
	private TreeViewer treeViewer;
	private ActionFactory afactory;
	private SQLQueryDesigner designer;
	private boolean create = false;

	public JoinFromTableDialog(Shell parentShell, SQLQueryDesigner designer) {
		this(parentShell, designer, false);
	}

	public JoinFromTableDialog(Shell parentShell, SQLQueryDesigner designer, boolean create) {
		super(parentShell);
		setTitle(Messages.JoinFromTableDialog_0);
		setDescription(Messages.JoinFromTableDialog_1);
		this.designer = designer;
		this.create = create;
	}

	public void setValue(MFromTable value) {
		this.srcTable = value;
		if (value instanceof MFromTableJoin) {
			fromTable = ((MFromTable) value.getParent()).toSQLString();
			join = ((MFromTableJoin) value).getJoin();
		} else {
			List<INode> children = value.getParent().getChildren();
			int ind = children.indexOf(value) - 1;
			if (ind < 0)
				ind = children.indexOf(value) + 1;
			fromTable = ((MFromTable) children.get(ind)).toSQLString();
		}
	}

	private String[] getFromTables() {
		ANode parent = srcTable.getParent();
		if (srcTable instanceof MFromTableJoin)
			parent = parent.getParent();
		List<String> lst = new ArrayList<String>();
		String spStr = null;
		if (srcTable instanceof MFromTableJoin) {
			MFromTable sp = ((MFromTable) srcTable.getParent());
			spStr = sp.toSQLString().replace(",", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
			lst.add(sp.getDisplayText());
		}
		for (INode s : parent.getChildren()) {
			if (srcTable == s)
				continue;
			if (spStr != null && s.getDisplayText().equals(spStr))
				continue;
			if (!s.getDisplayText().equals(srcTable.toSQLString()))
				lst.add(s.getDisplayText());
		}
		return lst.toArray(new String[lst.size()]);
	}

	private int getFromTablesIndex() {
		String[] fromTables = getFromTables();
		String ftbl = fromTable.replace(",", "").trim(); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 0; i < fromTables.length; i++) {
			if (fromTables[i].equals(ftbl))
				return i;
		}
		if (fromTables != null && fromTables.length > 0)
			fromTable = fromTables[0];
		return 0;
	}

	public String getFromTable() {
		return fromTable;
	}

	public void setFromTable(String fromTable) {
		this.fromTable = fromTable;
	}

	public String getJoin() {
		return join;
	}

	public void setJoin(String join) {
		this.join = join;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);
		cmp.setLayout(new GridLayout(3, false));

		Combo ftable = new Combo(cmp, SWT.READ_ONLY);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		ftable.setLayoutData(gd);
		ftable.setItems(getFromTables());
		ftable.select(getFromTablesIndex());

		Combo keyword = new Combo(cmp, SWT.READ_ONLY);
		keyword.setItems(AMKeyword.JOIN_KEYWORDS);

		Text lbl = new Text(cmp, SWT.BORDER | SWT.READ_ONLY);
		lbl.setText(srcTable.getValue().toSQLString());
		lbl.setToolTipText(lbl.getText());
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = 200;
		lbl.setLayoutData(gd);

		DataBindingContext bindingContext = new DataBindingContext();
		bindingContext.bindValue(SWTObservables.observeSelection(keyword), PojoObservables.observeValue(this, "join")); //$NON-NLS-1$ 
		bindingContext.bindValue(SWTObservables.observeSelection(ftable), PojoObservables.observeValue(this, "fromTable")); //$NON-NLS-1$

		if (!create) {
			treeViewer = new TreeViewer(cmp, SWT.MULTI | SWT.BORDER);
			treeViewer.setContentProvider(new ReportTreeContetProvider());
			treeViewer.setLabelProvider(new ReportTreeLabelProvider());
			gd = new GridData(GridData.FILL_BOTH);
			gd.heightHint = 200;
			gd.horizontalSpan = 3;
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

			MenuManager menuMgr = new MenuManager();
			menuMgr.setRemoveAllWhenShown(true);
			afactory = new ActionFactory(designer, treeViewer);
			menuMgr.addMenuListener(new IMenuListener() {
				public void menuAboutToShow(IMenuManager mgr) {
					TreeSelection s = (TreeSelection) treeViewer.getSelection();
					Object[] selection = s != null ? s.toArray() : null;
					boolean isFromTable = false;
					if (selection != null)
						for (Object o : selection)
							if (o instanceof MFromTable) {
								isFromTable = true;
								break;
							}

					for (AAction act : afactory.getActions()) {
						if (act == null)
							mgr.add(new org.eclipse.jface.action.Separator());
						else if (act.calculateEnabled(selection)) {
							if (isFromTable && !(act instanceof CreateExpressionGroup || act instanceof CreateExpression || act instanceof CreateXExpression))
								continue;
							mgr.add(act);
						}
					}
				}

			});
			Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
			treeViewer.getControl().setMenu(menu);
			treeViewer.addDoubleClickListener(new IDoubleClickListener() {

				@Override
				public void doubleClick(DoubleClickEvent event) {
					runAction(event, afactory.getAction(ChangeOperator.class));
					runAction(event, afactory.getAction(EditExpression.class));
				}

				private void runAction(DoubleClickEvent event, AAction sd) {
					if (sd.calculateEnabled(event.getSelection()))
						sd.run();
				}
			});
			treeViewer.getControl().addKeyListener(new KeyAdapter() {
				@Override
				public void keyPressed(KeyEvent event) {
					if (event.character == SWT.DEL && event.stateMask == 0) {
						TreeSelection s = (TreeSelection) treeViewer.getSelection();
						if (s == null)
							return;
						List<Object> selection = new ArrayList<Object>();
						for (Object obj : s.toList()) {
							if (obj instanceof MFromTable)
								continue;
							selection.add(obj);
						}

						List<DeleteAction<?>> dactions = afactory.getDeleteActions(selection.toArray());
						for (DeleteAction<?> da : dactions) {
							da.run();
							break;
						}
					}
				}
			});

			treeViewer.setInput(srcTable.getParent());
			treeViewer.expandAll();
		}
		return cmp;
	}
}
