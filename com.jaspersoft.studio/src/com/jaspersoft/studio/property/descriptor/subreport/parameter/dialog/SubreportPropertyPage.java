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
package com.jaspersoft.studio.property.descriptor.subreport.parameter.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSubreportParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreportParameter;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.subreport.command.wizard.NewSubreportPage;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionCellEditor;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSWizard;
import com.jaspersoft.studio.wizards.JSSWizardPage;

public class SubreportPropertyPage extends JSSWizardPage {

	/**
	 * This variable stores the last set of parameters specified by using loadSettings. Settings will be reloaded if the
	 * new array is different from this one...
	 */
	private JRParameter[] lastParameters = null;

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			JRSubreportParameter sp = (JRSubreportParameter) element;
			switch (columnIndex) {
			case 0:
				return sp.getName();
			case 1:
				if (sp != null && sp.getExpression() != null)
					return sp.getExpression().getText();
			}
			return ""; //$NON-NLS-1$
		}
	}

	private JRSubreportParameter[] value;
	private Table table;
	private TableViewer tableViewer;

	public JRSubreportParameter[] getValue() {
		if (!tableViewer.getControl().isDisposed()) {
			List<JRSubreportParameter> lst = (List<JRSubreportParameter>) tableViewer.getInput();
			if (lst != null)
				value = lst.toArray(new JRSubreportParameter[lst.size()]);
		}
		return value;
	}

	@Override
	public void dispose() {
		getValue();
		super.dispose();
	}

	public void setValue(JRSubreportParameter[] value) {
		this.value = value;
		if (table != null)
			fillTable(table);
	}

	/**
	 * @wbp.parser.constructor
	 */
	public SubreportPropertyPage() {
		this("subreportpage"); //$NON-NLS-1$
	}

	public SubreportPropertyPage(String pageName) {
		super(pageName);
		setTitle(Messages.common_subreport_parameters);
		setDescription(Messages.SubreportPropertyPage_description);
	}

	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_SUBREPORT_PROPERTIES;
	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		setControl(composite);

		buildTable(composite);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		table.setLayoutData(gd);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				JRDesignSubreportParameter param = new JRDesignSubreportParameter();
				int i = 0;
				String name = "NEW_PARAMETER";//$NON-NLS-1$
				while (getName(input, name, i) == null)
					i++;
				name += "_" + i; //$NON-NLS-1$
				param.setName(name);
				JRDesignExpression expression = new JRDesignExpression();
				expression.setText("");
				param.setExpression(expression);

				return param;
			}

			private String getName(List<?> input, String name, int i) {
				name += "_" + i; //$NON-NLS-1$
				for (Object dto : input) {
					JRSubreportParameter prm = (JRSubreportParameter) dto;
					if (prm.getName() != null && prm.getName().trim().equals(name)) {
						return null;
					}
				}
				return name;
			}

		});

		new DeleteButton().createDeleteButton(bGroup, tableViewer);

		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);

		Button bMaster = new Button(bGroup, SWT.PUSH);
		bMaster.setText("Copy From Master");
		bMaster.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JasperReportsConfiguration jrConfig = (JasperReportsConfiguration) getSettings().get(
						JSSWizard.JASPERREPORTS_CONFIGURATION);
				if (jrConfig == null)
					return;
				JasperDesign jd = jrConfig.getJasperDesign();
				if (jd == null)
					return;
				List<JRSubreportParameter> lst = (List<JRSubreportParameter>) tableViewer.getInput();
				if (lst == null)
					lst = new ArrayList<JRSubreportParameter>();
				for (JRParameter prm : jd.getParametersList()) {
					if (prm.isSystemDefined())
						continue;
					String name = prm.getName();
					boolean exists = false;
					for (JRSubreportParameter sp : lst) {
						if (sp.getName().equals(name)) {
							exists = true;
							break;
						}
					}
					if (exists)
						return;

					JRDesignSubreportParameter param = new JRDesignSubreportParameter();
					param.setName(name);
					param.setExpression(ExprUtil.createExpression("$P{" + name + "}"));
					lst.add(param);
				}
				tableViewer.setInput(lst);
				tableViewer.refresh(true);
			}
		});
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setHeaderVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new TLabelProvider());
		attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.common_name);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.common_expression);

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

		fillTable(table);
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("VALUE")) //$NON-NLS-1$
					return true;
				if (property.equals("NAME")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRSubreportParameter prop = (JRSubreportParameter) element;
				if ("VALUE".equals(property)) //$NON-NLS-1$
					if (prop.getExpression() != null)
						return prop.getExpression();// Misc.nvl(prop.getExpression(), "");
				if ("NAME".equals(property)) { //$NON-NLS-1$
					return prop.getName();
				}
				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				setErrorMessage(null);
				setMessage(getDescription());
				JRDesignSubreportParameter data = (JRDesignSubreportParameter) tableItem.getData();
				if ("VALUE".equals(property)) { //$NON-NLS-1$
					if (value instanceof JRExpression) {
						data.setExpression((JRExpression) value);
					}
				} else if ("NAME".equals(property)) { //$NON-NLS-1$
					List<JRDesignSubreportParameter> plist = (List<JRDesignSubreportParameter>) tableViewer.getInput();
					for (JRDesignSubreportParameter p : plist) {
						if (p != data && p.getName() != null && p.getName().equals(value)) {
							setErrorMessage(Messages.common_error_message_unique_properties);
							return;
						}
					}
					if (value == null || ((String) value).trim().equals("")) { //$NON-NLS-1$
						setErrorMessage(Messages.common_error_message_non_empty_properties_string_name);
						return;
					}
					data.setName((String) value);
				}
				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
			}
		});
		// FIXME we should have an expressionContext here, look also in SubreportWizard there are some comments there
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new JRExpressionCellEditor(parent, null) });
		viewer.setColumnProperties(new String[] { "NAME", "VALUE" }); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void fillTable(Table table) {
		if (value != null) {
			List<JRSubreportParameter> plist = new ArrayList<JRSubreportParameter>(Arrays.asList(value));

			tableViewer.setInput(plist);
		}
	}

	@Override
	public void setVisible(boolean visible) {
		loadSettings();
		super.setVisible(visible);
	}

	protected void loadSettings() {
		JRParameter[] parameters = null;
		// load settings, if available..
		if (getSettings() != null && getSettings().containsKey(NewSubreportPage.SUBREPORT_PARAMETERS)) {
			parameters = (JRParameter[]) getSettings().get(NewSubreportPage.SUBREPORT_PARAMETERS);
		}

		if (lastParameters != parameters) {
			lastParameters = parameters;
			List<JRDesignSubreportParameter> sParameters = new ArrayList<JRDesignSubreportParameter>();

			if (lastParameters != null && lastParameters.length > 0) {
				// Create an array of subreport parameters to be used in in the table model...
				for (JRParameter p : lastParameters) {
					if (!p.isSystemDefined()) {
						JRDesignSubreportParameter sp = new JRDesignSubreportParameter();
						sp.setName(p.getName());
						sp.setExpression(new JRDesignExpression());
						sParameters.add(sp);
					}
				}
			}
			setValue(sParameters.toArray(new JRDesignSubreportParameter[sParameters.size()]));
		}
	}

}
