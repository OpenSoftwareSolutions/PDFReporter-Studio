/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.property.dataset.dialog;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignParameter;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.FormDialog;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.IDataPreviewInfoProvider;
import com.jaspersoft.studio.data.IFieldSetter;
import com.jaspersoft.studio.data.designer.AQueryDesignerContainer;
import com.jaspersoft.studio.data.designer.QueryStatus;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.field.command.CreateFieldCommand;
import com.jaspersoft.studio.model.field.command.DeleteFieldCommand;
import com.jaspersoft.studio.model.parameter.MParameter;
import com.jaspersoft.studio.model.parameter.MParameterSystem;
import com.jaspersoft.studio.model.parameter.MParameters;
import com.jaspersoft.studio.model.parameter.command.CreateParameterCommand;
import com.jaspersoft.studio.model.parameter.command.DeleteParameterCommand;
import com.jaspersoft.studio.model.sortfield.command.CreateSortFieldCommand;
import com.jaspersoft.studio.model.sortfield.command.DeleteSortFieldCommand;
import com.jaspersoft.studio.property.SetValueCommand;
import com.jaspersoft.studio.swt.events.ExpressionModifiedEvent;
import com.jaspersoft.studio.swt.events.ExpressionModifiedListener;
import com.jaspersoft.studio.swt.widgets.CSashForm;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;

public class DatasetDialog extends FormDialog implements IFieldSetter, IDataPreviewInfoProvider {
	private MDataset mdataset;
	// private MReport mreport;
	private JasperReportsConfiguration jConfig;
	private Map<JRField, JRField> mapfields;
	private Map<JRParameter, JRParameter> mapparam;
	private CommandStack cmdStack;

	public DatasetDialog(Shell shell, MDataset mdataset, JasperReportsConfiguration jConfig, CommandStack cmdStack) {
		super(shell);
		this.cmdStack = cmdStack;
		this.mdataset = mdataset;
		this.jConfig = jConfig;
		newdataset = (JRDesignDataset) ((JRDesignDataset) mdataset.getValue()).clone();

		mapfields = new HashMap<JRField, JRField>();
		List<JRField> newFieldsList = newdataset.getFieldsList();
		List<JRField> oldFieldsList = mdataset.getValue().getFieldsList();
		for (int i = 0; i < oldFieldsList.size(); i++)
			mapfields.put(oldFieldsList.get(i), newFieldsList.get(i));

		mapparam = new HashMap<JRParameter, JRParameter>();
		List<JRParameter> newParamList = newdataset.getParametersList();
		List<JRParameter> oldParamList = mdataset.getValue().getParametersList();
		for (int i = 0; i < oldParamList.size(); i++)
			mapparam.put(oldParamList.get(i), newParamList.get(i));
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.DatasetDialog_title);
		UIUtils.resizeAndCenterShell(newShell, 1024, 768);
		// setShellStyle(getShellStyle() | SWT.MIN | SWT.MAX | SWT.RESIZE);
	}

	@Override
	public boolean close() {
		if (getReturnCode() == OK) {
			createCommand();
			if (cmdStack != null)
				cmdStack.execute(command);
		}
		dataquery.dispose();
		ptable.getPropertyChangeSupport().removePropertyChangeListener(prmListener);
		return super.close();
	}

	/**
	 * Set the root control of the wizard, and also add a listener to do the perform help action and set the context of
	 * the top control.
	 */
	protected void setHelpControl(Control newControl) {
		newControl.addListener(SWT.Help, new Listener() {
			@Override
			public void handleEvent(Event event) {
				performHelp();
			}
		});
	};

	/**
	 * Set and show the help data if a context, that bind this wizard with the data, is provided
	 */
	public void performHelp() {
		String child = dataquery.getContextHelpId();
		if (child == null)
			child = ContextHelpIDs.WIZARD_QUERY_DIALOG;

		PlatformUI.getWorkbench().getHelpSystem().setHelp(body, child);
		PlatformUI.getWorkbench().getHelpSystem().displayHelp(child);
	};

	@Override
	protected void createFormContent(final IManagedForm mform) {
		FormToolkit toolkit = mform.getToolkit();
		body = mform.getForm().getBody();

		setHelpControl(body);
		body.setLayout(new GridLayout(1, true));
		background = body.getBackground();
		body.setBackground(body.getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));

		dataquery = new DataQueryAdapters(mform.getForm().getBody(), jConfig, newdataset, background) {
			@Override
			protected void createStatusBar(Composite comp) {
				qStatus = new QueryStatus(comp);
			}

			@Override
			public void setFields(List<JRDesignField> fields) {
				DatasetDialog.this.setFields(fields);
			}

			@Override
			public List<JRDesignField> getCurrentFields() {
				return DatasetDialog.this.getCurrentFields();
			}

			@Override
			public void setParameters(List<JRDesignParameter> params) {
				DatasetDialog.this.setParameters(params);
			}

			@Override
			public int getContainerType() {
				return AQueryDesignerContainer.CONTAINER_WITH_INFO_TABLES;
			}

		};

		dataquery.createToolbar(body);

		SashForm sf = new CSashForm(body, SWT.VERTICAL);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 600;
		gd.minimumHeight = 400;
		gd.widthHint = 800;
		sf.setLayoutData(gd);
		sf.setLayout(new GridLayout());

		dataquery.createTop(sf, this);

		// int tabHeight = c.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		// tabHeight = Math.max(tabHeight, ctf.getTabHeight());
		// ctf.setTabHeight(tabHeight);
		//
		// ctf.setTopRight(c);

		createBottom(sf, toolkit);
		sf.setWeights(new int[] { 450, 250 });

		JasperDesign jd = mdataset.getJasperDesign();
		if (jd == null) {
			jd = mdataset.getMreport().getJasperDesign();
		}
		setDataset(jd, newdataset);
	}

	public void setFields(List<JRDesignField> fields) {
		ftable.setFields(fields);
	}

	public void addFields(List<JRDesignField> fields) {
		List<JRDesignField> allFields = ftable.getFields();
		for (JRDesignField f : fields) {
			// Take care of having "valid" name for field
			String newName = ModelUtils.getNameForField(allFields, f.getName());
			f.setName(newName);
			allFields.add(f);
		}
		ftable.setFields(allFields);
	}

	public void clearFields() {
		ftable.setFields(new ArrayList<JRDesignField>(0));
	}

	public void setParameters(List<JRDesignParameter> fields) {
		ptable.setFields(fields);
	}

	public List<JRDesignField> getCurrentFields() {
		return ftable.getFields();
	}

	private void createBottom(Composite parent, FormToolkit toolkit) {
		CTabFolder tabFolder = new CTabFolder(parent, SWT.BOTTOM | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.heightHint = 250;
		tabFolder.setLayoutData(gd);

		createFields(toolkit, tabFolder);
		createParameters(toolkit, tabFolder);
		createSortFields(toolkit, tabFolder);
		createFilterExpression(toolkit, tabFolder);
		createDataPreview(toolkit, tabFolder);

		tabFolder.setSelection(0);
		tabFolder.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				sftable.refresh();
			}

		});
	}

	private void createDataPreview(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem dataPreviewtab = new CTabItem(tabFolder, SWT.NONE);
		dataPreviewtab.setText(Messages.DatasetDialog_DataPreviewTab);

		dataPreviewTable = new DataPreviewTable(tabFolder, this, background);

		dataPreviewtab.setControl(dataPreviewTable.getControl());
	}

	private void createParameters(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.DatasetDialog_ParametersTab);

		ptable = new ParametersTable(tabFolder, newdataset, background, mdataset.isMainDataset());

		ptable.getPropertyChangeSupport().addPropertyChangeListener(prmListener);
		bptab.setControl(ptable.getControl());
	}

	private PropertyChangeListener prmListener = new PropertyChangeListener() {

		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			MRoot mroot = new MRoot(null, null);
			mroot.setJasperConfiguration(jConfig);
			MReport mrep = new MReport(mroot, jConfig);
			MDataset mdts = new MDataset(mrep, newdataset, -1);
			MParameters<?> mprms = new MParameters<JRDesignDataset>(mdts, newdataset, JRDesignDataset.PROPERTY_PARAMETERS);
			MParameter mprm = new MParameter(mprms, (JRDesignParameter) arg0.getSource(), -1);
			List<Command> cmds = JaspersoftStudioPlugin.getPostSetValueManager().postSetValue(mprm,
					JRDesignParameter.PROPERTY_NAME, arg0.getNewValue(), arg0.getOldValue());
			for (Command c : cmds)
				c.execute();
			dataquery.setDataset(jConfig.getJasperDesign(), newdataset);
		}
	};

	private void createFields(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.DatasetDialog_fieldstab);

		ftable = new FieldsTable(tabFolder, newdataset, background);

		bptab.setControl(ftable.getControl());
	}

	private void createSortFields(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.DatasetDialog_sortingtab);

		sftable = new SortFieldsTable(tabFolder, newdataset, background);

		bptab.setControl(sftable.getControl());
	}

	private void createFilterExpression(FormToolkit toolkit, CTabFolder tabFolder) {
		CTabItem bptab = new CTabItem(tabFolder, SWT.NONE);
		bptab.setText(Messages.DatasetDialog_filterexpression);

		Composite sectionClient = toolkit.createComposite(tabFolder);
		FillLayout fLayout = new FillLayout();
		fLayout.marginHeight = 5;
		fLayout.marginWidth = 5;
		sectionClient.setLayout(fLayout);

		filterExpression = new WTextExpression(sectionClient, SWT.NONE);
		filterExpression.setBackground(sectionClient.getBackground());
		filterExpression.setExpressionContext(new ExpressionContext(newdataset, mdataset.getJasperConfiguration()));
		filterExpression.setExpression((JRDesignExpression) newdataset.getFilterExpression());
		filterExpression.addModifyListener(new ExpressionModifiedListener() {

			@Override
			public void expressionModified(ExpressionModifiedEvent event) {
				newdataset.setFilterExpression(event.modifiedExpression);
			}
		});
		bptab.setControl(sectionClient);
	}

	private JSSCompoundCommand command;
	private Color background;

	public JSSCompoundCommand getCommand() {
		return command;
	}

	private JRDesignDataset newdataset;
	private FieldsTable ftable;
	private ParametersTable ptable;
	private SortFieldsTable sftable;
	private DataQueryAdapters dataquery;
	private WTextExpression filterExpression;
	private DataPreviewTable dataPreviewTable;
	private Composite body;

	public void setDataset(JasperDesign jDesign, JRDesignDataset ds) {
		dataquery.setDataset(jDesign, ds);
	}

	public void createCommand() {
		JRDesignDataset ds = (JRDesignDataset) (mdataset.getParent() == null ? mdataset.getJasperConfiguration()
				.getJasperDesign().getMainDesignDataset() : mdataset.getValue());
		command = new JSSCompoundCommand(mdataset);

		String lang = newdataset.getQuery().getLanguage();
		((JRDesignQuery) newdataset.getQuery()).setText(dataquery.getQuery());

		String qtext = newdataset.getQuery().getText();
		if (ds.getQuery() == null) {
			JRDesignQuery jrQuery = new JRDesignQuery();
			jrQuery.setLanguage(lang);
			jrQuery.setText(qtext);
			command.add(setValueCommand(JRDesignDataset.PROPERTY_QUERY, new MQuery(jrQuery, mdataset), mdataset));
		} else {
			IPropertySource mquery = (IPropertySource) mdataset.getPropertyValue(JRDesignDataset.PROPERTY_QUERY);
			if (ds.getQuery().getLanguage() == null || !ds.getQuery().getLanguage().equals(lang))
				command.add(setValueCommand(JRDesignQuery.PROPERTY_LANGUAGE, lang, mquery));
			if (!ds.getQuery().getText().equals(qtext))
				command.add(setValueCommand(JRDesignQuery.PROPERTY_TEXT, qtext, mquery));
		}
		String fexprtext = filterExpression.getText();
		if (fexprtext.trim().equals("")) //$NON-NLS-1$
			fexprtext = null;
		command.add(setValueCommand(JRDesignDataset.PROPERTY_FILTER_EXPRESSION, fexprtext, mdataset));
		command.add(setValueCommand(MDataset.PROPERTY_MAP, newdataset.getPropertiesMap(), mdataset));

		List<JRField> oldfields = ds.getFieldsList();
		List<JRDesignField> newfields = ftable.getFields();
		for (JRField f : oldfields) {
			Boolean canceled = null;
			for (JRDesignField newf : newfields)
				if (newf.getName().equals(f.getName()) || mapfields.get(f) == newf) {
					canceled = Boolean.TRUE;
					break;
				}
			if (canceled == null)
				command.add(new DeleteFieldCommand(jConfig, ds, (JRDesignField) f, canceled));
		}
		for (JRDesignField newf : newfields) {
			boolean notexists = true;
			for (JRField f : oldfields) {
				if (newf.getName().equals(f.getName())) {
					MField mfield = mdataset.getField(newf.getName());
					if (mfield != null) {
						addSetValueCommand(command, JRDesignField.PROPERTY_VALUE_CLASS_NAME, newf.getValueClassName(), mfield);
						addSetValueCommand(command, JRDesignField.PROPERTY_DESCRIPTION, newf.getDescription(), mfield);
					}
					notexists = false;
				} else if (mapfields.containsValue(newf)) {
					MField mfield = null;
					for (JRField p : mapfields.keySet()) {
						if (mapfields.get(p) == newf) {
							mfield = mdataset.getField(p.getName());
							break;
						}
					}
					if (mfield != null) {
						command.add(setValueCommand(JRDesignField.PROPERTY_NAME, newf.getName(), mfield));
						addSetValueCommand(command, JRDesignField.PROPERTY_VALUE_CLASS_NAME, newf.getValueClassName(), mfield);
						addSetValueCommand(command, JRDesignField.PROPERTY_DESCRIPTION, newf.getDescription(), mfield);
					}
					notexists = false;
				}
				if (!notexists)
					break;
			}
			if (notexists)
				command.add(new CreateFieldCommand(ds, newf, -1));
		}

		List<JRSortField> dssfields = ds.getSortFieldsList();
		List<JRDesignSortField> sfields = sftable.getFields();
		for (JRSortField f : dssfields)
			command.add(new DeleteSortFieldCommand(ds, f));
		for (JRDesignSortField newf : sfields)
			command.add(new CreateSortFieldCommand(ds, newf, -1));

		List<JRParameter> oldparams = ds.getParametersList();
		List<JRParameter> newparams = newdataset.getParametersList();
		for (JRParameter f : oldparams) {
			if (f.isSystemDefined())
				continue;
			Boolean canceled = null;
			for (JRParameter newf : newparams)
				if (newf.getName().equals(f.getName()) || mapparam.get(f) == newf) {
					canceled = Boolean.TRUE;
					break;
				}
			if (canceled == null)
				command.add(new DeleteParameterCommand(jConfig, ds, f, canceled));
		}
		for (JRParameter newf : newparams) {
			if (newf.isSystemDefined())
				continue;
			boolean notexists = true;
			for (JRParameter f : oldparams) {
				if (newf.getName().equals(f.getName())) {
					MParameterSystem mparam = mdataset.getParamater(newf.getName());
					if (mparam != null) {
						addSetValueCommand(command, JRDesignParameter.PROPERTY_VALUE_CLASS_NAME, newf.getValueClassName(), mparam);
						addSetValueCommand(command, JRDesignParameter.PROPERTY_DESCRIPTION, newf.getDescription(), mparam);
						addSetValueCommand(command, JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION,
								newf.getDefaultValueExpression(), mparam);
						addSetValueCommand(command, JRDesignParameter.PROPERTY_FOR_PROMPTING, newf.isForPrompting(), mparam);
					}
					notexists = false;
				} else if (mapparam.containsValue(newf)) {
					MParameterSystem mparam = null;
					for (JRParameter p : mapparam.keySet()) {
						if (mapparam.get(p) == newf) {
							mparam = mdataset.getParamater(p.getName());
							break;
						}
					}
					if (mparam != null) {
						command.add(setValueCommand(JRDesignParameter.PROPERTY_NAME, newf.getName(), mparam));
						addSetValueCommand(command, JRDesignParameter.PROPERTY_VALUE_CLASS_NAME, newf.getValueClassName(), mparam);
						addSetValueCommand(command, JRDesignParameter.PROPERTY_DESCRIPTION, newf.getDescription(), mparam);
						addSetValueCommand(command, JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION,
								newf.getDefaultValueExpression(), mparam);
						addSetValueCommand(command, JRDesignParameter.PROPERTY_FOR_PROMPTING, newf.isForPrompting(), mparam);
					}
					notexists = false;
				}
				if (!notexists)
					break;
			}
			if (notexists)
				command.add(new CreateParameterCommand(ds, newf, -1));
		}
	}

	private void addSetValueCommand(JSSCompoundCommand cc, String property, Object value, IPropertySource target) {
		if (value != null && !value.equals(target.getPropertyValue(property))) {
			SetValueCommand cmd = new SetValueCommand();
			cmd.setTarget(target);
			cmd.setPropertyId(property);
			cmd.setPropertyValue(value);
			cc.add(cmd);
		}
	}

	private Command setValueCommand(String property, Object value, IPropertySource target) {
		SetValueCommand cmd = new SetValueCommand();
		cmd.setTarget(target);
		cmd.setPropertyId(property);
		cmd.setPropertyValue(value);
		return cmd;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.IDataPreviewInfoProvider#getJasperReportsConfig()
	 */
	public JasperReportsConfiguration getJasperReportsConfig() {
		return this.jConfig;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.IDataPreviewInfoProvider#getDataAdapterDescriptor()
	 */
	public DataAdapterDescriptor getDataAdapterDescriptor() {
		return this.dataquery.getDataAdapter();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.IDataPreviewInfoProvider#getDesignDataset()
	 */
	public JRDesignDataset getDesignDataset() {
		return this.newdataset;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.data.IDataPreviewInfoProvider#getFieldsForPreview()
	 */
	public List<JRDesignField> getFieldsForPreview() {
		return this.getCurrentFields();
	}

}
