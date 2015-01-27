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
package com.jaspersoft.studio.data.sql.ui.metadata;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnViewerToolTipSupport;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.part.PluginTransfer;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.sql.SQLQueryDesigner;
import com.jaspersoft.studio.data.sql.Util;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.MSQLRoot;
import com.jaspersoft.studio.data.sql.model.metadata.INotInMetadata;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlSchema;
import com.jaspersoft.studio.data.sql.model.metadata.MSqlTable;
import com.jaspersoft.studio.dnd.NodeDragListener;
import com.jaspersoft.studio.dnd.NodeTransfer;
import com.jaspersoft.studio.model.IDragable;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MDummy;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.outline.ReportTreeContetProvider;
import com.jaspersoft.studio.outline.ReportTreeLabelProvider;

public class DBMetadata {
	private TreeViewer treeViewer;
	private MSQLRoot root;
	private SQLQueryDesigner designer;

	public DBMetadata(SQLQueryDesigner designer) {
		this.designer = designer;
	}

	public Control createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		stackLayout = new StackLayout();
		composite.setLayout(stackLayout);

		mcmp = new Composite(composite, SWT.BORDER);
		mcmp.setLayout(new GridLayout());

		msg = new Label(mcmp, SWT.WRAP | SWT.CENTER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = SWT.CENTER;
		gd.grabExcessVerticalSpace = true;
		gd.horizontalAlignment = SWT.CENTER;
		gd.horizontalIndent = 20;
		msg.setLayoutData(gd);
		msg.setText(Messages.DBMetadata_0);
		msg.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				doRefreshMetadata();
			}
		});

		treeViewer = new TreeViewer(composite, SWT.MULTI | SWT.BORDER);
		treeViewer.setContentProvider(new ReportTreeContetProvider() {
			@Override
			public Object[] getChildren(Object parentElement) {
				if (parentElement instanceof INode) {
					INode node = (INode) parentElement;
					List<INode> children = node.getChildren();
					List<INode> newchildren = new ArrayList<INode>();
					for (INode n : children) {
						if (n instanceof INotInMetadata && ((INotInMetadata) n).isNotInMetadata())
							continue;
						if (n.getValue() instanceof String && ((String) n.getValue()).isEmpty())
							continue;
						newchildren.add(n);
					}
					if (!newchildren.isEmpty())
						return newchildren.toArray();
				}
				return EMPTY_ARRAY;
			}
		});
		treeViewer.setLabelProvider(new ReportTreeLabelProvider());

		ColumnViewerToolTipSupport.enableFor(treeViewer);

		treeViewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE, new Transfer[] { NodeTransfer.getInstance(), PluginTransfer.getInstance() }, new NodeDragListener(treeViewer) {
			@Override
			public void dragStart(DragSourceEvent event) {
				TreeSelection s = (TreeSelection) treeViewer.getSelection();
				for (TreePath tp : s.getPaths()) {
					if (!(tp.getLastSegment() instanceof IDragable)) {
						event.doit = false;
						return;
					}
				}
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				treeViewer.refresh(true);
				if (!event.doit)
					return;
			}
		});
		treeViewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeSelection ts = (TreeSelection) treeViewer.getSelection();
				Object el = ts.getFirstElement();

				if (treeViewer.getExpandedState(el))
					treeViewer.collapseToLevel(el, 1);
				else {
					treeViewer.expandToLevel(el, 1);
					if (el instanceof MSqlSchema)
						loadSchema((MSqlSchema) el);
					else if (el instanceof MSqlTable)
						loadTable((MSqlTable) el);
				}
			}
		});
		treeViewer.addTreeListener(new ITreeViewerListener() {

			@Override
			public void treeCollapsed(TreeExpansionEvent event) {
			}

			@Override
			public void treeExpanded(TreeExpansionEvent event) {
				final Object element = event.getElement();
				if (element instanceof MSqlSchema)
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							loadSchema((MSqlSchema) element);
						}
					});
				else if (element instanceof MSqlTable)
					Display.getDefault().asyncExec(new Runnable() {

						@Override
						public void run() {
							loadTable((MSqlTable) element);
						}
					});
			}

		});
		MenuManager menuMgr = new MenuManager();
		Menu menu = menuMgr.createContextMenu(treeViewer.getControl());
		menuMgr.add(new Action(Messages.DBMetadata_1) {
			@Override
			public void run() {
				doRefreshMetadata();
			}
		});
		treeViewer.getControl().setMenu(menu);

		stackLayout.topControl = mcmp;

		root = designer.createRoot(root);
		updateUI(root);

		return composite;
	}

	private boolean running = false;
	private Label msg;
	private StackLayout stackLayout;
	private Composite mcmp;
	private Composite composite;
	private DataAdapterService das;

	public void closeConnection() {
		SchemaUtil.close(connection);
	}

	public void updateMetadata(final DataAdapterDescriptor da, DataAdapterService das, final IProgressMonitor monitor) {
		if (running)
			return;
		this.das = das;
		monitors.add(monitor);
		running = true;
		UIUtils.getDisplay().syncExec(new Runnable() {

			@Override
			public void run() {
				if (msg.isDisposed())
					return;
				msg.setText(Messages.DBMetadata_2 + da.getName() + Messages.DBMetadata_3);
				stackLayout.topControl = mcmp;
				mcmp.layout(true);
				composite.layout(true);
			}
		});
		root.removeChildren();
		if (tblMap != null)
			tblMap.clear();
		connection = getConnection(das, true);
		if (connection != null)
			try {
				DatabaseMetaData meta = connection.getMetaData();
				tableTypes = DBMetadata.readTableTypes(meta);
				List<MSqlSchema> mcurrent = MetaDataUtil.readSchemas(monitor, root, meta, schema);
				updateUI(root);
				for (MSqlSchema mcs : mcurrent) {
					if (meta.getConnection().isClosed()) {
						connection = getConnection(das, true);
						meta = connection.getMetaData();
					}
					readSchema(meta, mcs, monitor, true);
				}
			} catch (Throwable e) {
				updateUI(root);
				designer.showError(e);
			}
		updateItermediateUI();
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				Util.refreshTables(root, designer.getRoot(), designer);
				updateItermediateUI();
			}
		});
		closeConnection();
		monitors.remove(monitor);
		running = false;
	}

	public void loadTable(final MSqlTable mtable) {
		if (das != null && (mtable.getChildren().isEmpty() || mtable.getChildren().get(0) instanceof MDummy)) {
			try {
				designer.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(Messages.DBMetadata_4, IProgressMonitor.UNKNOWN);
						try {
							monitors.add(monitor);
							DatabaseMetaData meta = getConnection(das, false).getMetaData();
							MetaDataUtil.readTableColumns(meta, mtable, monitor);
							updateItermediateUI();
							if (monitor.isCanceled())
								return;
							MetaDataUtil.readTableKeys(meta, mtable, monitor);
							updateItermediateUI();
						} catch (Throwable e) {
							designer.showError(e);
						} finally {
							closeConnection();
							monitors.remove(monitor);
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void loadSchema(final MSqlSchema mschema) {
		if (das != null && (mschema.getChildren().isEmpty() || mschema.getChildren().get(0) instanceof MDummy)) {
			try {
				designer.run(true, true, new IRunnableWithProgress() {
					@Override
					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						monitor.beginTask(Messages.DBMetadata_5, IProgressMonitor.UNKNOWN);
						try {
							monitors.add(monitor);
							readSchema(getConnection(das, false).getMetaData(), mschema, monitor, false);
						} catch (Throwable e) {
							designer.showError(e);
						} finally {
							closeConnection();
							monitors.remove(monitor);
							monitor.done();
						}
					}
				});
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	protected void readSchema(DatabaseMetaData meta, MSqlSchema schema, IProgressMonitor monitor, boolean firstSelection) {
		try {
			MetaDataUtil.readSchema(meta, schema, monitor, tableTypes);
			updateItermediateUI(false);
			if (monitor.isCanceled())
				return;
			if (schema.isNotInMetadata())
				return;
			MetaDataUtil.readSchemaTables(meta, schema, getTables(), monitor);
			updateItermediateUI();
			if (monitor.isCanceled())
				return;
			if (firstSelection)
				setFirstSelection();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		if (monitor.isCanceled())
			return;
		if (schema.isNotInMetadata()) {
			MetaDataUtil.readProcedures(meta, schema, monitor);
			updateItermediateUI();
		}
	}

	public MRoot getRoot() {
		return root;
	}

	public Connection getConnection(final DataAdapterService das, boolean readCurrentSchema) {
		schema = null;
		SchemaUtil.close(connection);
		Map<String, Object> parameters = new HashMap<String, Object>();
		try {
			if (das != null)
				das.contributeParameters(parameters);
		} catch (JRException e1) {
			updateUI(root);
			designer.showError(e1);
		}

		connection = (Connection) parameters.get(JRParameter.REPORT_CONNECTION);
		if (connection == null)
			return connection;
		// TODO implement some compatibility, getSchema() available since 1.7
		if (readCurrentSchema)
			schema = SchemaUtil.getSchemaPath(connection);
		try {
			identifierQuote = connection.getMetaData().getIdentifierQuoteString();
			designer.doRefreshRoots(false);
			System.out.println("JDBC Quotes: " + connection.getMetaData().getIdentifierQuoteString());
			System.out.println("getExtraNameCharacters: " + connection.getMetaData().getExtraNameCharacters());
			System.out.println("storesLowerCaseIdentifiers: " + connection.getMetaData().storesLowerCaseIdentifiers());
			System.out.println("storesLowerCaseQuotedIdentifiers: " + connection.getMetaData().storesLowerCaseQuotedIdentifiers());
			System.out.println("storesMixedCaseIdentifiers: " + connection.getMetaData().storesMixedCaseIdentifiers());
			System.out.println("storesMixedCaseQuotedIdentifiers: " + connection.getMetaData().storesMixedCaseQuotedIdentifiers());
			System.out.println("storesUpperCaseIdentifiers: " + connection.getMetaData().storesUpperCaseIdentifiers());
			System.out.println("storesUpperCaseQuotedIdentifiers: " + connection.getMetaData().storesUpperCaseQuotedIdentifiers());
			System.out.println("supportsMixedCaseIdentifiers: " + connection.getMetaData().supportsMixedCaseIdentifiers());
			System.out.println("supportsMixedCaseQuotedIdentifiers: " + connection.getMetaData().supportsMixedCaseQuotedIdentifiers());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}

	private String identifierQuote = "";

	public String getIdentifierQuote() {
		return identifierQuote;
	}

	private String[] schema;

	public String[] getCurrentSchema() {
		return schema;
	}

	protected void updateUI(final MSQLRoot root) {
		UIUtils.getDisplay().syncExec(new Runnable() {
			public void run() {
				if (treeViewer.getControl().isDisposed())
					return;
				DBMetadata.this.root = root;
				if (DBMetadata.this.root == null)
					DBMetadata.this.root = designer.createRoot(root);
				treeViewer.setInput(DBMetadata.this.root);
				designer.refreshQueryModel();
				setFirstSelection();
				if (isEmptySchema(root)) {
					msg.setText(Messages.DBMetadata_6);
					stackLayout.topControl = mcmp;
				} else
					stackLayout.topControl = treeViewer.getControl();
				composite.layout(true);
			}
		});
	}

	public static boolean isEmptySchema(MRoot root) {
		if (root.getChildren().isEmpty())
			return true;
		for (INode n : root.getChildren())
			if (n instanceof MSqlSchema && !((MSqlSchema) n).isNotInMetadata())
				return false;
		return true;
	}

	protected void updateItermediateUI() {
		updateItermediateUI(true);
	}

	protected void updateItermediateUI(final boolean refreshMetadata) {
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				if (!treeViewer.getControl().isDisposed()) {
					treeViewer.refresh(true);
					if (refreshMetadata)
						designer.refreshedMetadata();
				}
			}
		});
	}

	protected void setFirstSelection() {
		if (schema == null)
			return;
		Display.getDefault().asyncExec(new Runnable() {
			public void run() {
				for (INode n : DBMetadata.this.root.getChildren()) {
					if (n instanceof MSqlSchema && n.getValue().equals(schema)) {
						((MSqlSchema) n).setCurrent(true);
						treeViewer.expandToLevel((MSqlSchema) n, 1);
						break;
					}
				}
			}
		});
	}

	private LinkedHashMap<String, MSqlTable> tblMap;
	private List<IProgressMonitor> monitors = new ArrayList<IProgressMonitor>();
	private List<String> tableTypes;
	private Connection connection;

	public LinkedHashMap<String, MSqlTable> getTables() {
		if (tblMap == null)
			tblMap = new LinkedHashMap<String, MSqlTable>();
		return tblMap;
	}

	public void dispose() {
		closeConnection();
		if (monitors != null)
			for (IProgressMonitor m : monitors)
				m.setCanceled(true);
	}

	protected void doRefreshMetadata() {
		if (!running) {
			designer.showInfo(""); //$NON-NLS-1$
			designer.updateMetadata();
		}
	}

	public static List<String> readTableTypes(DatabaseMetaData meta) throws SQLException {
		List<String> tableTypes = new ArrayList<String>();
		ResultSet rs = meta.getTableTypes();
		while (rs.next())
			tableTypes.add(rs.getString("TABLE_TYPE")); //$NON-NLS-1$
		rs.close();
		return tableTypes;
	}

}
