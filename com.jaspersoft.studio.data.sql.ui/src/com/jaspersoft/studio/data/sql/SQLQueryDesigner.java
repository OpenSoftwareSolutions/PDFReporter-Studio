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
package com.jaspersoft.studio.data.sql;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.w3c.tools.codec.Base64Encoder;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.jdbc.JDBCDataAdapterDescriptor;
import com.jaspersoft.studio.data.querydesigner.sql.SimpleSQLQueryDesigner;
import com.jaspersoft.studio.data.sql.messages.Messages;
import com.jaspersoft.studio.data.sql.model.MSQLRoot;
import com.jaspersoft.studio.data.sql.model.query.from.MFromTable;
import com.jaspersoft.studio.data.sql.model.query.orderby.MOrderBy;
import com.jaspersoft.studio.data.sql.prefs.SQLEditorPreferencesPage;
import com.jaspersoft.studio.data.sql.text2model.Text2Model;
import com.jaspersoft.studio.data.sql.ui.SQLQueryOutline;
import com.jaspersoft.studio.data.sql.ui.SQLQuerySource;
import com.jaspersoft.studio.data.sql.ui.gef.SQLQueryDiagram;
import com.jaspersoft.studio.data.sql.ui.metadata.DBMetadata;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelVisitor;
import com.jaspersoft.studio.swt.widgets.CSashForm;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class SQLQueryDesigner extends SimpleSQLQueryDesigner {
	public static final String SQLQUERYDESIGNER = "SQLQUERYDESIGNER"; //$NON-NLS-1$
	private SashForm sf;
	private DBMetadata dbMetadata;
	private SQLQuerySource source;
	private SQLQueryOutline outline;
	private SQLQueryDiagram diagram;
	private MSQLRoot root;

	public SQLQueryDesigner() {
		super();
		refreshViewer();
		JaspersoftStudioPlugin.getInstance().addPreferenceListener(preferenceListener);
	}

	@Override
	public Control getControl() {
		return sf;
	}

	@Override
	public Control createControl(Composite parent) {
		sf = new CSashForm(parent, SWT.HORIZONTAL);
		sf.setLayoutData(new GridData(GridData.FILL_BOTH));
		sf.setLayout(new GridLayout());

		dbMetadata = new DBMetadata(this);
		Control c = dbMetadata.createControl(sf);
		c.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		tabFolder = new CTabFolder(sf, SWT.FLAT | SWT.BOTTOM | SWT.BORDER);
		tabFolder.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		createSource(tabFolder);
		createOutline(tabFolder);
		createDiagram(tabFolder);

		tabFolder.setSelection(0);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (source.isDirty())
					Text2Model.text2model(SQLQueryDesigner.this, source.getXTextDocument(), true);
				switch (tabFolder.getSelectionIndex()) {
				case 1:
					outline.scheduleRefresh();
					break;
				case 2:
					diagram.scheduleRefresh();
					break;
				}
				source.setDirty(false);
			}
		});

		sf.setWeights(new int[] { 250, 750 });
		return sf;
	}

	// private boolean warned = false;

	// private void showWarning() {
	// if (!warned && isQueryModelEmpty()) {
	// UIUtils.showWarning("Attention, SQL Query Builder will overwrite the existing query!");
	// warned = true;
	// }
	// }

	protected void createDiagram(CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.SQLQueryDesigner_diagram);

		diagram = new SQLQueryDiagram(this);
		bptab.setControl(diagram.createDiagram(tabFolder));
	}

	protected void createOutline(CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.SQLQueryDesigner_outline);

		outline = new SQLQueryOutline(this);
		bptab.setControl(outline.createOutline(tabFolder));
	}

	private void createSource(CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.SQLQueryDesigner_text);

		source = new SQLQuerySource(this);
		bptab.setControl(source.createSource(tabFolder));
	}

	@Override
	public void doSourceTextChanged() {
		super.doSourceTextChanged();
	}

	@Override
	protected String getQueryFromWidget() {
		return source.getQuery();
	}

	@Override
	protected void createLineStyler() {
	}

	@Override
	protected void setupSourceEditorFont() {
		source.setupFont(jConfig);
	}

	private boolean isModelRefresh = false;

	@Override
	protected void updateQueryText(String txt) {
		if (refreshMetadata)
			return;
		if (source != null)
			source.setQuery(txt);
		if (!isModelRefresh)
			refreshQueryModel();
	}

	public void refreshQueryModel() {
		if (source != null)
			Text2Model.text2model(this, source.getXTextDocument());
	}

	private boolean refreshMetadata = false;

	public void setRefreshMetadata(boolean refreshMetadata) {
		this.refreshMetadata = refreshMetadata;
	}

	public void refreshedMetadata() {
		if (tabFolder.getSelectionIndex() == 0)
			source.setDirty(true);
		// if (tabFolder.getSelectionIndex() != 0) {
		// refreshMetadata = true;
		// Util.refreshTables(dbMetadata.getRoot(), getRoot(), this);
		// refreshMetadata = false;
		// }
		// refreshQueryModel();
		if (tabFolder.getSelectionIndex() == 1)
			outline.scheduleRefresh();
		if (tabFolder.getSelectionIndex() == 2)
			diagram.scheduleRefresh();
	}

	public void refreshQueryText() {
		if (refreshMetadata)
			return;
		if (root != null) {
			isModelRefresh = true;
			updateQueryText(QueryWriter.writeQuery(root));
			isModelRefresh = false;
		}
	}

	public void refreshQuery() {
		if (refreshMetadata)
			return;
		if (root != null) {
			isModelRefresh = true;
			if (!isQueryModelEmpty())
				updateQueryText(QueryWriter.writeQuery(root));
			if (tabFolder.getSelectionIndex() == 2)
				diagram.scheduleRefresh();
			isModelRefresh = false;
		}
	}

	protected boolean isQueryModelEmpty() {
		boolean update = true;
		for (INode c : root.getChildren())
			if (!c.getChildren().isEmpty()) {
				update = false;
				break;
			}
		return update;
	}

	public Object getActiveEditor() {
		switch (tabFolder.getSelectionIndex()) {
		case 0:
			return source;
		case 1:
			return outline;
		case 2:
			return diagram;
		}
		return null;
	}

	private DataAdapterDescriptor da;

	@Override
	public void setDataAdapter(final DataAdapterDescriptor da) {
		if (this.da == da)
			return;
		this.da = da;
		super.setDataAdapter(da);
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				if (runningmonitor != null)
					runningmonitor.setCanceled(true);
				updateMetadata();
			}
		});
	}

	private Thread runningthread;
	private IProgressMonitor runningmonitor;
	private CTabFolder tabFolder;

	public void updateMetadata() {
		if (da instanceof JDBCDataAdapterDescriptor)
			try {
				getRoot().setValue(getjDataset());
				container.run(true, true, new IRunnableWithProgress() {

					public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
						if (runningthread != null) {
							runningmonitor.setCanceled(true);
							if (runningthread != null)
								runningthread.join();
						}
						runningmonitor = monitor;
						runningthread = Thread.currentThread();
						try {
							monitor.beginTask(Messages.SQLQueryDesigner_readmetadata, IProgressMonitor.UNKNOWN);
							dbMetadata.closeConnection();
							DataAdapterService das = DataAdapterServiceUtil.getInstance(jConfig).getService(da.getDataAdapter());
							dbMetadata.updateMetadata(da, das, monitor);
						} finally {
							monitor.done();
							runningthread = null;
							runningmonitor = null;
						}
					}
				});
			} catch (InvocationTargetException ex) {
				container.getQueryStatus().showError(ex.getTargetException());
				runningthread = null;
				runningmonitor = null;
			} catch (InterruptedException ex) {
				container.getQueryStatus().showError(ex);
				runningthread = null;
				runningmonitor = null;
			}
	}

	public SQLQueryOutline getOutline() {
		return outline;
	}

	public SQLQueryDiagram getDiagram() {
		return diagram;
	}

	@Override
	public void dispose() {
		if (dbMetadata != null)
			dbMetadata.dispose();
		if (outline != null)
			outline.dispose();
		if (diagram != null)
			diagram.dispose();
		JaspersoftStudioPlugin.getInstance().removePreferenceListener(preferenceListener);
		super.dispose();
	}

	public DBMetadata getDbMetadata() {
		return dbMetadata;
	}

	public MSQLRoot getRoot() {
		return root;
	}

	@Override
	public void setQuery(JasperDesign jDesign, JRDataset jDataset, JasperReportsConfiguration jConfig) {
		super.setQuery(jDesign, jDataset, jConfig);
		doRefreshRoots(false);
	}

	private Set<MSQLRoot> roots = new HashSet<MSQLRoot>();

	public MSQLRoot createRoot(MSQLRoot oldRoot) {
		if (oldRoot != null) {
			oldRoot.getPropertyChangeSupport().removePropertyChangeListener(tblListener);
			roots.remove(oldRoot);
		}
		MSQLRoot rt = new MSQLRoot(null, getjDataset());
		if (jConfig != null)
			rt.setIdentifierQuote(jConfig.getProperty(SQLEditorPreferencesPage.P_IDENTIFIER_QUOTE, "")); //$NON-NLS-1$
		roots.add(rt);
		rt.getPropertyChangeSupport().addPropertyChangeListener(tblListener);
		return rt;
	}

	private PropertyChangeListener tblListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			if (getjDataset() == null)
				return;
			final Set<String> tables = new HashSet<String>();
			new ModelVisitor<String>((INode) arg0.getSource()) {

				@Override
				public boolean visit(INode n) {
					if (n instanceof MFromTable) {
						MFromTable ft = (MFromTable) n;
						Object x = ((MFromTable) n).getPropertyActualValue(MFromTable.PROP_X);
						Object y = ((MFromTable) n).getPropertyActualValue(MFromTable.PROP_Y);
						if (x != null && y != null)
							tables.add(ft.getValue().toSQLString() + ft.getAliasKeyString() + "," + x + "," + y + ";");
					}
					return true;
				}
			};
			if (!tables.isEmpty()) {
				String input = "";
				for (String t : tables)
					input += t;
				getjDataset().setProperty(SQLQueryDiagram.SQL_EDITOR_TABLES, new Base64Encoder(input).processString());
			}
		}
	};
	private PreferenceListener preferenceListener = new PreferenceListener();

	private final class PreferenceListener implements IPropertyChangeListener {

		public void propertyChange(org.eclipse.jface.util.PropertyChangeEvent event) {
			String p = event.getProperty();
			if (p.equals(SQLEditorPreferencesPage.P_IDENTIFIER_QUOTE)) {
				doRefreshRoots(true);
			}
		}

	}

	public void refreshViewer() {
		if (root != null)
			root.removeChildren();
		else {
			root = createRoot(root);
		}
		Util.createSelect(root);
		new MOrderBy(root);
	}

	public void doRefreshRoots(boolean updateText) {
		String iq = jConfig.getProperty(SQLEditorPreferencesPage.P_IDENTIFIER_QUOTE, ""); //$NON-NLS-1$
		boolean useJDBCQuote = jConfig.getPropertyBoolean(SQLEditorPreferencesPage.P_USE_JDBC_QUOTE, true); //$NON-NLS-1$
		if (useJDBCQuote)
			iq = dbMetadata.getIdentifierQuote();
		boolean quoteExceptions = jConfig.getPropertyBoolean(SQLEditorPreferencesPage.P_IDENTIFIER_QUOTEONLYEXCEPTIONS, true); //$NON-NLS-1$
		for (MSQLRoot r : roots) {
			r.setIdentifierQuote(iq);
			r.setQuoteExceptions(quoteExceptions);
		}
		if (updateText)
			refreshQueryText();
	}
}
