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
package com.jaspersoft.studio.data.sql.ui.gef;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.dnd.AbstractTransferDropTargetListener;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.gef.requests.GroupRequest;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.wb.swt.Keyboard;
import org.w3c.tools.codec.Base64Decoder;
import org.w3c.tools.codec.Base64FormatException;

import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.action.LayoutAction;
import com.jaspersoft.studio.data.sql.action.select.CreateColumn;
import com.jaspersoft.studio.data.sql.action.table.CreateTable;
import com.jaspersoft.studio.data.sql.action.table.DeleteTable;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.metadata.MSQLColumn;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.data.sql.model.query.from.MFrom;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.from.TableJoin;
import com.jaspersoft.studio.data.sql.ui.gef.parts.ColumnEditPart;
import com.jaspersoft.studio.data.sql.ui.gef.parts.FromEditPart;
import com.jaspersoft.studio.data.sql.ui.gef.parts.QueryEditPart;
import com.jaspersoft.studio.data.sql.ui.gef.parts.SQLDesignerEditPartFactory;
import com.jaspersoft.studio.data.sql.ui.gef.parts.TableEditPart;
import com.jaspersoft.studio.dnd.NodeTransfer;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.DialogEnabledCommand;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.KeyValue;
import com.jaspersoft.studio.model.util.ModelVisitor;

public class SQLQueryDiagram {
	private SQLQueryDesigner designer;
	private ScrollingGraphicalViewer viewer;
	public static final String SQLQUERYDIAGRAM = "SQLQUERYDIAGRAM"; //$NON-NLS-1$

	public SQLQueryDiagram(SQLQueryDesigner designer) {
		this.designer = designer;
	}

	public Control createDiagram(Composite parent) {
		viewer = new ScrollingGraphicalViewer();
		viewer.createControl(parent);
		viewer.setEditDomain(new DefaultEditDomain(null));
		viewer.setRootEditPart(new SQLDesignerRootEditPart());
		viewer.setEditPartFactory(new SQLDesignerEditPartFactory());

		final ZoomManager zoomManager = (ZoomManager) viewer.getProperty(ZoomManager.class.toString());
		viewer.setKeyHandler(new GraphicalViewerKeyHandler(viewer) {
			@Override
			public boolean keyPressed(KeyEvent event) {
				if (event.keyCode == SWT.DEL)
					doDeleteTable();
				else if ((event.stateMask & Keyboard.getCtrlKey()) != 0)
					switch (event.keyCode) {
					case SWT.KEYPAD_ADD:
					case '+':
					case '=':
						if (zoomManager.canZoomOut())
							zoomManager.zoomOut();
						break;
					case '-':
					case SWT.KEYPAD_SUBTRACT:
						if (zoomManager.canZoomIn())
							zoomManager.zoomIn();
						break;
					case '0':
						zoomManager.setZoom(1);
						break;
					case 'z':
						if (viewer.getEditDomain().getCommandStack().canUndo())
							viewer.getEditDomain().getCommandStack().undo();
						break;
					case 'y':
						if (viewer.getEditDomain().getCommandStack().canRedo())
							viewer.getEditDomain().getCommandStack().redo();
						break;
					}
				else if (((event.stateMask & SWT.ALT) != 0 && event.keyCode == 'z')) {
					if (viewer.getEditDomain().getCommandStack().canRedo())
						viewer.getEditDomain().getCommandStack().redo();
				}
				return super.keyPressed(event);
			}
		});
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.MOD1), MouseWheelZoomHandler.SINGLETON);
		viewer.setProperty(SQLQUERYDIAGRAM, designer);
		viewer.setContextMenu(new ContextMenuProvider(viewer) {

			@Override
			public void buildContextMenu(IMenuManager menu) {
				Object[] selection = null;
				IStructuredSelection s = (IStructuredSelection) viewer.getSelection();
				if (s != null) {
					List<Object> models = new ArrayList<Object>();
					for (Object obj : s.toList()) {
						if (obj instanceof EditPart) {
							if (obj instanceof ColumnEditPart) {
								models.add(obj);
							} else
								obj = ((EditPart) obj).getModel();
						}
						if (obj instanceof ANode)
							models.add((ANode) obj);
						if (obj instanceof TableJoin)
							models.add(((TableJoin) obj).getJoinTable());
					}
					if (!models.isEmpty())
						selection = models.toArray();
				}
				designer.getOutline().getAfactory().fillMenu(selection, menu);
				for (IContributionItem c : menu.getItems()) {
					if (c instanceof ActionContributionItem && ((ActionContributionItem) c).getAction() instanceof DeleteTable) {
						menu.insertAfter(c.getId(), new Action(Messages.SQLQueryDiagram_1) {
							public void run() {
								doDeleteTable();
							}
						});
						menu.remove(c);
					}
				}
				menu.add(new LayoutAction(designer));
				menu.add(new org.eclipse.jface.action.Separator());
				menu.add(new ZoomInAction(zoomManager));
				menu.add(new ZoomOutAction(zoomManager));
			}
		});
		viewer.addDropTargetListener(new QueryDesignerDropTargetListener(viewer, NodeTransfer.getInstance()));

		refreshViewer();

		return viewer.getControl();
	}

	public ScrollingGraphicalViewer getViewer() {
		return viewer;
	}

	public static final String SQL_EDITOR_TABLES = "com.jaspersoft.studio.data.sql.tables"; //$NON-NLS-1$

	protected void refreshViewer() {
		if (designer.getjDataset() != null) {
			String tbls = designer.getjDataset().getPropertiesMap().getProperty(SQL_EDITOR_TABLES);
			if (tbls != null) {
				final List<KeyValue<String, Point>> map = new ArrayList<KeyValue<String, Point>>();
				try {
					StringTokenizer st = new StringTokenizer(new Base64Decoder(tbls).processString(), ";"); //$NON-NLS-1$
					while (st.hasMoreTokens()) {
						StringTokenizer tblSt = new StringTokenizer(st.nextToken(), ","); //$NON-NLS-1$
						String tbl = tblSt.hasMoreTokens() ? tblSt.nextToken() : null;
						String xs = tblSt.hasMoreTokens() ? tblSt.nextToken() : null;
						String ys = tblSt.hasMoreTokens() ? tblSt.nextToken() : null;

						if (tbl != null && xs != null && ys != null)
							map.add(new KeyValue<String, Point>(tbl, new Point(Integer.parseInt(xs), Integer.parseInt(ys))));
					}
					new ModelVisitor<Object>(designer.getRoot()) {

						@Override
						public boolean visit(INode n) {
							if (n instanceof MFromTable) {
								MFromTable ft = (MFromTable) n;
								String t = ft.getValue().toSQLString() + ft.getAliasKeyString();
								KeyValue<String, Point> key = null;
								for (KeyValue<String, Point> kv : map) {
									if (kv.key.equals(t)) {
										ft.setNoEvents(true);
										ft.setPropertyValue(MFromTable.PROP_X, kv.value.x);
										ft.setPropertyValue(MFromTable.PROP_Y, kv.value.y);
										ft.setNoEvents(false);
										key = kv;
									}
								}
								if (key != null)
									map.remove(key);
							}
							return true;
						}
					};
				} catch (Base64FormatException e) {
					e.printStackTrace();
				}
			}
		}

		viewer.setContents(designer.getRoot());

		TreeSelection s = (TreeSelection) designer.getOutline().getTreeViewer().getSelection();
		List<MFromTable> tables = new ArrayList<MFromTable>();
		for (Object obj : s.toList()) {
			if (obj instanceof MFromTable)
				tables.add((MFromTable) obj);
		}
		if (!tables.isEmpty()) {
			List<TableEditPart> parts = new ArrayList<TableEditPart>();
			EditPart ep = viewer.getRootEditPart();
			for (Object obj : ep.getChildren())
				doAddParts(obj, parts, tables);
			viewer.setSelection(new StructuredSelection(parts));
			if (!parts.isEmpty())
				viewer.reveal(parts.get(0));
		}
		designer.refreshQueryText();
	}

	private void doAddParts(Object obj, List<TableEditPart> parts, List<MFromTable> tables) {
		if (obj instanceof QueryEditPart) {
			for (Object o : ((QueryEditPart) obj).getChildren())
				doAddParts(o, parts, tables);
		} else if (obj instanceof FromEditPart) {
			for (Object tep : ((FromEditPart) obj).getChildren()) {
				if (tep instanceof TableEditPart && tables.contains(((TableEditPart) tep).getModel()))
					parts.add((TableEditPart) tep);
			}
		}
	}

	public void scheduleRefresh() {
		refreshViewer();
	}

	public void dispose() {

	}

	protected void doDeleteTable() {
		List<EditPart> parts = viewer.getSelectedEditParts();
		CompoundCommand cc = new CompoundCommand() {
			private boolean firstRun = false;
			private boolean run = true;

			@Override
			public void execute() {
				if (!firstRun)
					run = UIUtils.showConfirmation(Messages.SQLQueryDiagram_5, Messages.SQLQueryDiagram_6);
				firstRun = true;
				if (run)
					super.execute();
			}

			@Override
			public void undo() {
				if (run)
					super.undo();
			}
		};
		for (EditPart p : parts) {
			GroupRequest deleteReq = new GroupRequest(RequestConstants.REQ_DELETE);
			Map<String, String> extendedData = new HashMap<String, String>();
			extendedData.put(Messages.SQLQueryDiagram_7, Messages.SQLQueryDiagram_8);
			deleteReq.setExtendedData(extendedData);
			deleteReq.setEditParts(p);
			cc.add(p.getCommand(deleteReq));
		}
		viewer.getEditDomain().getCommandStack().execute(cc);
		designer.refreshQueryText();
		refreshViewer();
	}

	public class QueryDesignerDropTargetListener extends AbstractTransferDropTargetListener {

		public QueryDesignerDropTargetListener(EditPartViewer viewer, Transfer xfer) {
			super(viewer, xfer);
		}

		@Override
		protected void updateTargetRequest() {
			((CreateRequest) getTargetRequest()).setLocation(getDropLocation());
		}

		@Override
		protected void handleDrop() {
			updateTargetRequest();
			updateTargetEditPart();

			doDrop(Util.getAllNodes(getCurrentEvent().data));
		}

		private void doDrop(List<ANode> node) {
			final Set<MSqlTable> tablesset = new LinkedHashSet<MSqlTable>();
			Set<MSQLColumn> colsset = new LinkedHashSet<MSQLColumn>();
			Set<ANode> others = new LinkedHashSet<ANode>();
			Util.filterTables(node, tablesset, colsset, others);

			MFrom mfrom = null;
			EditPart ep = getTargetEditPart();
			if (ep instanceof ColumnEditPart)
				ep = ep.getParent();
			if (ep instanceof TableEditPart)
				ep = ep.getParent();

			if (ep instanceof FromEditPart)
				mfrom = (MFrom) ep.getModel();
			else
				mfrom = Util.getKeyword(designer.getRoot(), MFrom.class);

			if (!tablesset.isEmpty()) {
				// TODO for Slavic - Bugzilla #34318: TEMPORARY FIX THAT YOU SHOULD
				// REVIEW
				// Forcing the loading of the tables information so the user can use
				// smoothly
				// the graphical editor (Diagram Tab) without NPE.
				Set<MSqlTable> tmp = new LinkedHashSet<MSqlTable>();
				for (MSqlTable t : tablesset) {
					MSqlTable mt = Util.getTable(designer.getDbMetadata().getRoot(), t);
					designer.getDbMetadata().loadTable(mt);
					tmp.add(mt);
				}
				tablesset.clear();
				tablesset.addAll(tmp);

				CreateTable ct = designer.getOutline().getAfactory().getAction(CreateTable.class);
				if (ct.calculateEnabled(new Object[] { mfrom })) {
					// ct.run(tablesset);
					CreateRequest tgReq = (CreateRequest) getTargetRequest();
					tgReq.setFactory(new CreationFactory() {

						@Override
						public Object getObjectType() {
							return MSqlTable.class;
						}

						@Override
						public Object getNewObject() {
							return tablesset;
						}
					});
					Command command = getTargetEditPart().getCommand(tgReq);

					if (command instanceof DialogEnabledCommand && command.canExecute()) {
						// If we have a special command that supports dialog (i.e: image
						// creation)
						// we'll show the popup dialog and continue with creation only if
						// the user has confirmed.
						if (((DialogEnabledCommand) command).openDialog() == Dialog.CANCEL) {
							getCurrentEvent().detail = DND.DROP_NONE;
							return;
						}
					}
					if (command != null && command.canExecute())
						getViewer().getEditDomain().getCommandStack().execute(command);
					else
						getCurrentEvent().detail = DND.DROP_NONE;
					refreshViewer();
				}
			}
			if (!colsset.isEmpty()) {
				CreateColumn ct = designer.getOutline().getAfactory().getAction(CreateColumn.class);
				if (ct.calculateEnabled(new Object[] { mfrom })) {
					ct.run(colsset);
					refreshViewer();
				}
			}
		}

		@Override
		protected Request createTargetRequest() {
			CreateRequest request = new CreateRequest();
			return request;
		}

	}
}
