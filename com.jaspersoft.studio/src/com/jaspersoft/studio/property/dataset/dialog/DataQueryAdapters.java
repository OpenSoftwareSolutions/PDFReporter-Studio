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
package com.jaspersoft.studio.property.dataset.dialog;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.query.JRJdbcQueryExecuterFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ToolBar;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.DataAdapterManager;
import com.jaspersoft.studio.data.IFieldSetter;
import com.jaspersoft.studio.data.IQueryDesigner;
import com.jaspersoft.studio.data.MDataAdapters;
import com.jaspersoft.studio.data.designer.AQueryDesignerContainer;
import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.data.widget.DataAdapterAction;
import com.jaspersoft.studio.data.widget.IDataAdapterRunnable;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public abstract class DataQueryAdapters extends AQueryDesignerContainer {

	/** Property to save a default data adapter to select */
	public static final String DEFAULT_DATAADAPTER = "com.jaspersoft.studio.data.defaultdataadapter";

	private JRDesignDataset newdataset;
	private JasperDesign jDesign;

	private Color background;
	private IFile file;

	public DataQueryAdapters(Composite parent, JasperReportsConfiguration jConfig, JRDesignDataset newdataset,
			Color background, IRunnableContext runner) {
		setRunnableContext(runner);
		if (jConfig != null) {
			this.file = (IFile) jConfig.get(FileUtils.KEY_FILE);
			this.jDesign = jConfig.getJasperDesign();
		}
		this.newdataset = newdataset;
		this.jConfig = jConfig;
		if (background != null)
			this.background = background;
		// else
		this.background = parent.getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
	}

	public DataQueryAdapters(Composite parent, JasperReportsConfiguration jConfig, JRDesignDataset newdataset,
			Color background) {
		this(parent, jConfig, newdataset, background, null);
	}

	public void dispose() {
		qdfactory.dispose();
		dmfactory.dispose();
	}

	private Composite composite;
	private DataAdapterAction dscombo;
	private Combo langCombo;
	private String[] languages;
	private Composite langComposite;
	private StackLayout langLayout;
	private QDesignerFactory qdfactory;
	private CTabFolder tabFolder;

	public Composite getControl() {
		return composite;
	}

	public Composite getQueryControl() {
		return tabFolder;
	}

	public void setFile(JasperReportsConfiguration jConfig) {
		this.file = (IFile) jConfig.get(FileUtils.KEY_FILE);
		this.jDesign = jConfig.getJasperDesign();
		dscombo.setDataAdapterStorages(DataAdapterManager.getDataAdapter(file));
		setDataset(jDesign, newdataset);
	}

	public CTabFolder createTop(Composite parent, IFieldSetter fsetter) {
		tabFolder = new CTabFolder(parent, SWT.TOP | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 150;
		tabFolder.setLayoutData(gd);

		createQuery(tabFolder);
		createMappingTools(tabFolder, fsetter);

		tabFolder.setSelection(0);
		return tabFolder;
	}

	private void createMappingTools(CTabFolder tabFolder, IFieldSetter fsetter) {
		dmfactory = new DataMappingFactory(tabFolder, fsetter, this);
	}

	private void createQuery(CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.DataQueryAdapters_querytab);

		Composite sectionClient = new Composite(tabFolder, SWT.NONE);
		sectionClient.setLayout(new GridLayout(3, false));
		sectionClient.setBackground(background);
		sectionClient.setBackgroundMode(SWT.INHERIT_FORCE);

		Label label = new Label(sectionClient, SWT.NONE);
		label.setText(Messages.DataQueryAdapters_languagetitle);

		langCombo = new Combo(sectionClient, SWT.SINGLE | SWT.BORDER);
		languages = ModelUtils.getQueryLanguages(jConfig);
		langCombo.setItems(languages);
		langCombo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				changeLanguage();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		langCombo.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				String lang = langCombo.getText();
				int index = Misc.indexOf(languages, lang);
				if (index < 0) {
					languages[0] = lang;
					langCombo.setItem(0, lang);
					langCombo.select(0);
					changeLanguage();
				}
			}
		});

		tbCompo = new Composite(sectionClient, SWT.NONE);
		tbCompo.setBackgroundMode(SWT.INHERIT_FORCE);
		tbLayout = new StackLayout();
		tbCompo.setLayout(tbLayout);
		tbCompo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		langComposite = new Composite(sectionClient, SWT.NONE);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 3;
		langComposite.setLayoutData(gd);
		langLayout = new StackLayout();
		langLayout.marginWidth = 0;
		langLayout.marginWidth = 0;
		langComposite.setLayout(langLayout);
		langComposite.setBackground(background);

		qdfactory = new QDesignerFactory(langComposite, tbCompo, this);
		// for (String lang : languages)
		// qdfactory.getDesigner(lang);

		bptab.setControl(sectionClient);
	}

	private IQueryDesigner currentDesigner = null;
	private DataMappingFactory dmfactory;

	private void changeLanguage() {
		if (!isRefresh) {
			qStatus.showInfo("");
			String lang = langCombo.getText();
			((JRDesignQuery) newdataset.getQuery()).setLanguage(lang);
			final IQueryDesigner designer = qdfactory.getDesigner(lang);
			langLayout.topControl = designer.getControl();
			tbLayout.topControl = designer.getToolbarControl();
			tbCompo.layout();
			langComposite.layout();
			currentDesigner = designer;
			currentDesigner.setJasperConfiguration(jConfig);
			UIUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					currentDesigner.setQuery(jDesign, newdataset, jConfig);
					currentDesigner.setDataAdapter(dscombo.getSelected());
				}
			});
		}
	}

	public String getContextHelpId() {
		return currentDesigner.getContextHelpId();
	}

	public Composite createToolbar(Composite parent) {
		final Composite comp = new Composite(parent, SWT.NONE);
		comp.setLayout(new GridLayout(4, false));
		comp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		comp.setBackgroundMode(SWT.INHERIT_FORCE);

		Label lbl = new Label(comp, SWT.NONE);
		lbl.setImage(JaspersoftStudioPlugin.getInstance().getImage(MDataAdapters.getIconDescriptor().getIcon16()));

		final ToolBar tb = new ToolBar(comp, SWT.FLAT | SWT.RIGHT);
		tb.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		final ToolBarManager manager = new ToolBarManager(tb);
		IDataAdapterRunnable adapterRunReport = new IDataAdapterRunnable() {

			public void runReport(DataAdapterDescriptor da) {
				if (da != null)
					newdataset.setProperty(DEFAULT_DATAADAPTER, da.getName());
				currentDesigner.setDataAdapter(da);
				qStatus.showInfo("");
			}

			public boolean isNotRunning() {
				return true;
			}
		};
		dscombo = new DataAdapterAction(adapterRunReport, DataAdapterManager.getDataAdapter(file));

		manager.add(dscombo);

		manager.update(true);
		tb.pack();

		createStatusBar(comp);

		createProgressBar(comp);

		return comp;
	}

	private IRunnableContext runner;

	public void setRunnableContext(IRunnableContext runner) {
		this.runner = runner;
	}

	protected void createProgressBar(final Composite comp) {
		if (runner == null)
			runner = new RunWithProgressBar(comp);
	}

	public void run(boolean fork, boolean cancelable, IRunnableWithProgress runnable) throws InvocationTargetException,
			InterruptedException {
		runner.run(fork, cancelable, runnable);
	}

	public void getFields(IProgressMonitor monitor) {
		doGetFields(monitor);
	}

	private boolean isRefresh = false;
	private StackLayout tbLayout;
	private Composite tbCompo;

	public void setDataset(JasperDesign jDesign, JRDesignDataset ds) {
		newdataset = ds;
		JRQuery query = newdataset.getQuery();
		if (query == null) {
			query = new JRDesignQuery();
			((JRDesignQuery) query).setLanguage(JRJdbcQueryExecuterFactory.QUERY_LANGUAGE_SQL);
			((JRDesignQuery) query).setText("");
			newdataset.setQuery((JRDesignQuery) query);
		}
		isRefresh = true;
		int langindex = Misc.indexOf(languages, query.getLanguage());
		if (langindex >= 0)
			langCombo.select(langindex);
		else {
			langCombo.setItem(0, Misc.nvl(query.getLanguage()));
			langCombo.select(0);
		}
		isRefresh = false;
		changeLanguage();

		if (jDesign != null) {
			// Try to find the default data adapter for the specified dataset
			String defaultAdapter = ds.getPropertiesMap().getProperty(DEFAULT_DATAADAPTER);
			if (defaultAdapter == null && ds.isMainDataset()) {
				// if none available get the default for the main report
				defaultAdapter = jDesign.getProperty(DEFAULT_DATAADAPTER);
			}
			dscombo.setSelected(defaultAdapter);
			currentDesigner.setDataAdapter(dscombo.getSelected());
		}
	}

	public String getLanguage() {
		int langind = langCombo.getSelectionIndex();
		if (langind < 0 || langind > languages.length)
			langind = 0;
		return languages[langind];

	}

	public String getQuery() {
		return qdfactory.getDesigner(newdataset.getQuery().getLanguage()).getQuery();
	}

	public DataAdapterDescriptor getDataAdapter() {
		return dscombo.getSelected();
	}

	@Override
	protected void doGetFields(IProgressMonitor monitor) {
		final DataAdapterDescriptor da = dscombo.getSelected();
		if (da != null && da instanceof IFieldsProvider && ((IFieldsProvider) da).supportsGetFieldsOperation(jConfig)) {
			qStatus.showInfo("");

			monitor.beginTask(Messages.DataQueryAdapters_jobname, -1);

			ClassLoader oldClassloader = Thread.currentThread().getContextClassLoader();
			Thread.currentThread().setContextClassLoader(jConfig.getClassLoader());

			DataAdapterService das = DataAdapterServiceUtil.getInstance(jConfig).getService(da.getDataAdapter());
			try {
				final List<JRDesignField> fields = ((IFieldsProvider) da).getFields(das, jConfig, newdataset);
				if (fields != null) {
					monitor.setTaskName("Setting Fields");
					Display.getDefault().syncExec(new Runnable() {

						public void run() {
							setFields(fields);
						}
					});
					monitor.setTaskName("Fields set");
				}
			} catch (Exception e) {
				if (e.getCause() != null)
					qStatus.showError(e.getCause().getMessage(), e);
				else
					qStatus.showError(e);
			} finally {
				Thread.currentThread().setContextClassLoader(oldClassloader);
				das.dispose();
				monitor.done();
			}
		}
	}

}
