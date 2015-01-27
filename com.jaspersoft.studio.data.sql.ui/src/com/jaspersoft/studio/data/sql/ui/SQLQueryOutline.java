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
package com.jaspersoft.studio.data.sql.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.PluginTransfer;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.AAction;
import com.jaspersoft.studio.data.sql.action.ActionFactory;
import com.jaspersoft.studio.data.sql.action.DeleteAction;
import com.jaspersoft.studio.data.sql.action.expression.ChangeOperator;
import com.jaspersoft.studio.data.sql.action.expression.CreateExpression;
import com.jaspersoft.studio.data.sql.action.expression.EditExpression;
import com.jaspersoft.studio.data.sql.action.groupby.CreateGroupByColumn;
import com.jaspersoft.studio.data.sql.action.order.CreateOrderByColumn;
import com.jaspersoft.studio.data.sql.action.order.OrderByDesc;
import com.jaspersoft.studio.data.sql.action.select.CreateColumn;
import com.jaspersoft.studio.data.sql.action.select.EditColumn;
import com.jaspersoft.studio.data.sql.action.select.SelectDistinct;
import com.jaspersoft.studio.data.sql.action.table.CreateTable;
import com.jaspersoft.studio.data.sql.action.table.EditTable;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.MHaving;
import com.jaspersoft.studio.data.sql.model.query.MUnion;
import com.jaspersoft.studio.data.sql.model.query.MWhere;
import com.jaspersoft.studio.data.sql.model.query.expression.AMExpression;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpression;
import com.jaspersoft.studio.data.sql.model.query.expression.MExpressionGroup;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTableJoin;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupBy;
import com.jaspersoft.studio.data.sql.model.query.groupby.MGroupByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.AMOrderByMember;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByColumn;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderByExpression;
import com.jaspersoft.studio.data.sql.model.query.select.MSelect;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectColumn;
import com.jaspersoft.studio.data.sql.model.query.select.MSelectExpression;
import com.jaspersoft.studio.dnd.NodeDragListener;
import com.jaspersoft.studio.dnd.NodeTransfer;
import com.jaspersoft.studio.dnd.NodeTreeDropAdapter;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;

public class SQLQueryOutline {
	private SQLQueryDesigner designer;

	public SQLQueryOutline(SQLQueryDesigner designer) {
		this.designer = designer;
	}

	private boolean isRefresh = false;

	public Control createOutline(Composite parent) {
		treeViewer = new TreeViewer(parent, SWT.BORDER | SWT.MULTI) {

			@Override
			public void refresh(boolean updateLabels) {
				if (isRefresh) {
					super.refresh(updateLabels);
					return;
				}
				isRefresh = true;
				super.refresh(updateLabels);
				designer.refreshQuery();
				isRefresh = false;
			}
		};
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());
		treeViewer.setContentProvider(new ReportTreeContetProvider());
		treeViewer.setUseHashlookup(true);
		ColumnViewerToolTipSupport.enableFor(treeViewer);

		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		afactory = new ActionFactory(designer, treeViewer);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager mgr) {
				TreeSelection s = (TreeSelection) treeViewer.getSelection();
				afactory.fillMenu(s != null ? s.toArray() : null, mgr);
			}

		});
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		treeViewer.getControl().setMenu(menu);

		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { NodeTransfer.getInstance(), PluginTransfer.getInstance() };
		treeViewer.addDragSupport(ops, transfers, new NodeDragListener(treeViewer) {
			@Override
			public void dragStart(DragSourceEvent event) {
				IStructuredSelection selection = (IStructuredSelection) viewer.getSelection();
				Object fe = selection.getFirstElement();
				event.doit = !viewer.getSelection().isEmpty() && isDragable(fe);
			}

			public boolean isDragable(Object fe) {
				if (fe instanceof MSelect || fe instanceof MFrom || fe instanceof MGroupBy || fe instanceof MHaving || fe instanceof MWhere || fe instanceof MOrderBy || fe instanceof MUnion)
					return false;
				return true;
			}
		});

		transfers = new Transfer[] { NodeTransfer.getInstance(), TemplateTransfer.getInstance(), PluginTransfer.getInstance() };
		NodeTreeDropAdapter dropAdapter = new NodeTreeDropAdapter(treeViewer) {
			@Override
			public boolean validateDrop(Object target, int op, TransferData type) {
				return super.validateDrop(target, op, type) || TemplateTransfer.getInstance().isSupportedType(type);
			}

			@Override
			public boolean performDrop(Object data) {
				if (data == null)
					return false;
				List<ANode> nodes = new ArrayList<ANode>();
				List<Object> objects = new ArrayList<Object>();
				if (data.getClass().isArray()) {
					Object[] ar = (Object[]) data;
					for (Object obj : ar)
						if (obj instanceof ANode)
							nodes.add((ANode) obj);
						else
							objects.add(obj);
				} else if (data instanceof ANode)
					nodes.add((ANode) data);
				else
					objects.add(data);
				Object target = getCurrentTarget();
				if (target instanceof ANode && ((ANode) target).getParent() == null)
					return false;
				doDropObjects((ANode) target, objects);
				return doDrop((ANode) target, Util.getAllNodes(data));
			}

			private void doDropObjects(ANode target, List<Object> objects) {
				List<JRDesignParameter> prms = new ArrayList<JRDesignParameter>();
				for (Object obj : objects)
					if (obj instanceof JRDesignParameter)
						prms.add((JRDesignParameter) obj);
				if (!prms.isEmpty()) {
					CreateExpression ce = afactory.getAction(CreateExpression.class);
					if (ce.calculateEnabled(new Object[] { target }))
						ce.run(prms);
				}
			}

			private boolean doDrop(ANode target, List<ANode> node) {
				Set<MSqlTable> tablesset = new LinkedHashSet<MSqlTable>();
				Set<MSQLColumn> colsset = new LinkedHashSet<MSQLColumn>();
				Set<ANode> others = new LinkedHashSet<ANode>();
				Util.filterTables(node, tablesset, colsset, others);

				doDropTable(target, tablesset);
				doDropColumn(target, colsset);
				if (!others.isEmpty()) {
					for (ANode n : others) {
						ANode oldNode = Util.getOldNode((ANode) target, n);
						if ((target instanceof MExpressionGroup || target instanceof MWhere || target instanceof MHaving || target instanceof MFromTableJoin)
								&& (n instanceof MExpression || n instanceof MExpressionGroup)) {
							oldNode.setParent(null, -1);
							oldNode.setParent((ANode) target, -1);
							refreshAndReveal(oldNode);
							continue;
						}
						if (target instanceof AMExpression && (n instanceof AMExpression || n instanceof MExpressionGroup)) {
							oldNode.setParent(null, -1);
							AMExpression<?> mexpr = (AMExpression<?>) target;
							ANode p = mexpr.getParent();
							oldNode.setParent(p, p.getChildren().indexOf(mexpr));

							refreshAndReveal(oldNode);
							continue;
						}
						if (n instanceof MSelectColumn || n instanceof MSelectExpression) {
							int ind = -1;
							// can drop also in a child of one of this
							if (target instanceof MWhere) {

							}
							if (target instanceof MGroupByColumn) {
								ANode p = ((MGroupByColumn) target).getParent();
								ind = p.getChildren().indexOf(target);
								target = p;
							}
							if (target instanceof MGroupBy) {
								if (n instanceof MSelectColumn)
									refreshAndReveal(new MGroupByColumn((MGroupBy) target, (MSelectColumn) n, ind));
								continue;
							}
							if (target instanceof MHaving) {

							}
							if (target instanceof AMOrderByMember) {
								ANode p = ((AMOrderByMember<?>) target).getParent();
								ind = p.getChildren().indexOf(target);
								target = p;
							}
							if (target instanceof MOrderBy) {
								if (n instanceof MSelectExpression)
									refreshAndReveal(new MOrderByExpression((MOrderBy) target, (MSelectExpression) n, ind));
								else
									refreshAndReveal(new MOrderByColumn((MOrderBy) target, (MSelectColumn) n, ind));
								continue;
							}
						}
						reorder(target, n);
					}
				}
				return false;
			}

			private void reorder(Object target, ANode n) {
				ANode parent = null;
				if (target instanceof AMKeyword)
					parent = (ANode) target;
				else if (target.getClass().isAssignableFrom(n.getClass()))
					parent = ((ANode) target).getParent();
				if (n.getParent().equals(parent)) {
					int ind = parent.getChildren().indexOf(n);
					if (ind >= 0 && ind < parent.getChildren().size()) {
						n = (ANode) parent.getChildren().get(ind);
						int pos = 0;
						if (target != parent)
							pos = parent.getChildren().indexOf(target);
						parent.removeChild(n);
						parent.addChild(n, pos);
						refreshAndReveal(n);
					}
				}
			}

			protected void doDropTable(Object target, Set<MSqlTable> tablesset) {
				if (!tablesset.isEmpty()) {
					Set<MSqlTable> tmp = new LinkedHashSet<MSqlTable>();
					for (MSqlTable t : tablesset) {
						MSqlTable mt = Util.getTable(designer.getDbMetadata().getRoot(), t);
						designer.getDbMetadata().loadTable(mt);
						tmp.add(mt);
					}
					tablesset.clear();
					tablesset.addAll(tmp);

					for (MSqlTable t : tablesset)
						designer.getDbMetadata().loadTable(t);
					if (target instanceof MSelect || target instanceof MSelectColumn || target instanceof MSelectExpression) {
						Set<MSQLColumn> cols = new HashSet<MSQLColumn>();
						for (MSqlTable t : tablesset) {
							for (INode n : t.getChildren())
								cols.add((MSQLColumn) n);
						}
						doDropColumn(target, cols);
						return;
					}
					CreateTable ct = afactory.getAction(CreateTable.class);
					if (ct.calculateEnabled(new Object[] { target }))
						ct.run(tablesset);
				}
			}

			protected void doDropColumn(Object target, Set<MSQLColumn> colsset) {
				if (!colsset.isEmpty()) {
					CreateColumn ct = afactory.getAction(CreateColumn.class);
					if (ct.calculateEnabled(new Object[] { target }))
						ct.run(colsset);
					CreateGroupByColumn cg = afactory.getAction(CreateGroupByColumn.class);
					if (cg.calculateEnabled(new Object[] { target }))
						cg.run(colsset);
					CreateOrderByColumn co = afactory.getAction(CreateOrderByColumn.class);
					if (co.calculateEnabled(new Object[] { target }))
						co.run(colsset);
					CreateExpression ce = afactory.getAction(CreateExpression.class);
					if (ce.calculateEnabled(new Object[] { target }))
						ce.run(colsset);
				}
			}

		};
		treeViewer.addDropSupport(ops, transfers, dropAdapter);
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				runAction(event, afactory.getAction(SelectDistinct.class));
				runAction(event, afactory.getAction(OrderByDesc.class));
				runAction(event, afactory.getAction(ChangeOperator.class));
				runAction(event, afactory.getAction(EditColumn.class));
				runAction(event, afactory.getAction(EditTable.class));
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

					List<DeleteAction<?>> dactions = afactory.getDeleteActions(s != null ? s.toArray() : null);
					for (DeleteAction<?> da : dactions) {
						da.run();
						break;
					}
				}
			}
		});
		refreshViewer();
		return treeViewer.getControl();
	}

	protected void refreshViewer() {
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (treeViewer.getTree().isDisposed())
					return;
				treeViewer.setInput(designer.getRoot());
				treeViewer.expandToLevel(1);
			}
		});
	}

	private TreeViewer treeViewer;
	private ActionFactory afactory;

	public ActionFactory getAfactory() {
		return afactory;
	}

	public void scheduleRefresh() {
		if (isRefresh)
			return;
		isRefresh = true;
		if (designer.getRoot() != null)
			designer.getRoot().setValue(designer.getjDataset());
		treeViewer.refresh(true);
		isRefresh = false;
	}

	public TreeViewer getTreeViewer() {
		return treeViewer;
	}

	public void dispose() {

	}

	protected void refreshAndReveal(ANode toselect) {
		treeViewer.refresh(true);
		treeViewer.reveal(toselect);
	}
}
