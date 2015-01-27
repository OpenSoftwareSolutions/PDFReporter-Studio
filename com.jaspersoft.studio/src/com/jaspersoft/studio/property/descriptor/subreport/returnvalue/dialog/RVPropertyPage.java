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
package com.jaspersoft.studio.property.descriptor.subreport.returnvalue.dialog;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSubreport;
import net.sf.jasperreports.engine.JRSubreportReturnValue;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.base.JRBaseReport;
import net.sf.jasperreports.engine.design.JRDesignSubreportReturnValue;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.repo.RepositoryUtil;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.xml.sax.InputSource;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.JReportsDTO;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.classname.ClassTypeCellEditor;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class RVPropertyPage extends JSSHelpWizardPage {
	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			JRDesignSubreportReturnValue val = (JRDesignSubreportReturnValue) element;
			switch (columnIndex) {
			case 0:
				return val.getSubreportVariable();
			case 1:
				return val.getToVariable();
			case 2:
				return val.getCalculationValue().getName();
			case 3:
				return Misc.nvl(val.getIncrementerFactoryClassName());
			}
			return ""; //$NON-NLS-1$
		}
	}

	private JReportsDTO dto;

	public JReportsDTO getDto() {
		dto.setValue(getValue());
		return dto;
	}

	public void setDto(JReportsDTO dto) {
		this.dto = dto;
		value = dto.getValue();
		toVariables = null;
	}

	private List<JRSubreportReturnValue> value;
	private Table table;
	private TableViewer tableViewer;
	private String[] toVariables;

	// private TableCursor cursor;

	public List<JRSubreportReturnValue> getValue() {
		return value;
	}

	@Override
	public void dispose() {
		value = (List<JRSubreportReturnValue>) tableViewer.getInput();
		super.dispose();
	}

	public void setValue(List<JRSubreportReturnValue> value) {
		this.value = value;
		if (value == null) {
			value = new ArrayList<JRSubreportReturnValue>();
		}
		if (table != null)
			fillTable(table);
		getSubreport();
	}

	protected RVPropertyPage(String pageName) {
		super(pageName);
		setTitle(Messages.RVPropertyPage_subreport_return_values);
		setDescription(Messages.RVPropertyPage_description);
	}

	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_RETURN_VALUE;
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
				List<JRDesignSubreportReturnValue> list = (List<JRDesignSubreportReturnValue>) tableViewer.getInput();

				JRDesignSubreportReturnValue p = new JRDesignSubreportReturnValue();
				setSubreportVariableName(input, list, p);
				// get toVariable from list
				String[] toV = getToVariables();

				for (int j = 0; j < toV.length; j++) {
					boolean vExists = false;
					for (JRDesignSubreportReturnValue v : list)
						if (toV[j].equals(v.getToVariable())) {
							vExists = true;
							break;
						}
					if (!vExists) {
						p.setToVariable(toV[j]);
						p.setCalculation(CalculationEnum.NOTHING);
						return p;
					}
				}
				setErrorMessage(Messages.RVPropertyPage_error_message_report_variables_all_used);

				return null;
			}

			private void setSubreportVariableName(List<?> input, List<JRDesignSubreportReturnValue> list,
					JRDesignSubreportReturnValue p) {
				for (String spn : srcParamNames) {
					boolean vExists = false;
					for (JRDesignSubreportReturnValue v : list)
						if (spn.equals(v.getToVariable())) {
							vExists = true;
							break;
						}
					if (!vExists) {
						p.setSubreportVariable(spn);
						vExists = true;
						break;
					}
				}
				if (p.getSubreportVariable() == null) {
					int i = 0;
					String name = "NEW_VARIABLE";//$NON-NLS-1$
					while (getName(input, name, i) == null)
						i++;
					name += "_" + i; //$NON-NLS-1$
					p.setSubreportVariable(name); //$NON-NLS-1$
				}
			}

			private String getName(List<?> input, String name, int i) {
				name += "_" + i; //$NON-NLS-1$
				for (Object dto : input) {
					JRDesignSubreportReturnValue prm = (JRDesignSubreportReturnValue) dto;
					if (prm.getSubreportVariable() != null && prm.getSubreportVariable().trim().equals(name)) {
						return null;
					}
				}
				return name;
			}

		});

		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);
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
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		tlayout.addColumnData(new ColumnWeightData(50, 100, true));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[4];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.RVPropertyPage_subreport_variable);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.RVPropertyPage_to_variable);

		column[2] = new TableColumn(table, SWT.NONE);
		column[2].setText(Messages.RVPropertyPage_calculation_type);

		column[3] = new TableColumn(table, SWT.NONE);
		column[3].setText(Messages.RVPropertyPage_incrementer_factory_class);

		fillTable(table);
		for (int i = 0, n = column.length; i < n; i++) {
			column[i].pack();
		}
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("SUBREPORTVARIABLE")) //$NON-NLS-1$
					return true;
				if (property.equals("TOVARIABLE")) //$NON-NLS-1$
					return true;
				if (property.equals("CALCULATIONTYPE")) //$NON-NLS-1$
					return true;
				if (property.equals("INCREMENTERFACTORYCLASS")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRDesignSubreportReturnValue prop = (JRDesignSubreportReturnValue) element;
				if ("SUBREPORTVARIABLE".equals(property)) //$NON-NLS-1$
					return prop.getSubreportVariable();
				if ("TOVARIABLE".equals(property)) { //$NON-NLS-1$
					String[] toV = getToVariables();
					for (int i = 0; i < toV.length; i++) {
						if (toV[i].equals(prop.getToVariable()))
							return i;
					}
				}
				if ("CALCULATIONTYPE".equals(property)) //$NON-NLS-1$
					return EnumHelper.getValue(prop.getCalculationValue(), 0, false);
				if ("INCREMENTERFACTORYCLASS".equals(property)) //$NON-NLS-1$
					return Misc.nvl(prop.getIncrementerFactoryClassName());
				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				setErrorMessage(null);
				setMessage(getDescription());
				JRDesignSubreportReturnValue data = (JRDesignSubreportReturnValue) tableItem.getData();
				if ("SUBREPORTVARIABLE".equals(property)) { //$NON-NLS-1$
					data.setSubreportVariable((String) value);
				} else if ("TOVARIABLE".equals(property)) { //$NON-NLS-1$
					String[] tv = getToVariables();
					int val = (Integer) value;
					if (val >= 0 && val < tv.length)
						data.setToVariable(tv[val]);
				} else if ("CALCULATIONTYPE".equals(property)) { //$NON-NLS-1$
					data.setCalculation((CalculationEnum) EnumHelper.getSetValue(CalculationEnum.values(), value, 0, false));
				} else if ("INCREMENTERFACTORYCLASS".equals(property)) { //$NON-NLS-1$
					data.setIncrementerFactoryClassName((String) value);
				}
				validate();
				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent),
				new ComboBoxCellEditor(parent, getToVariables()),
				new ComboBoxCellEditor(parent, EnumHelper.getEnumNames(CalculationEnum.values(), NullEnum.NOTNULL)),
				new ClassTypeCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "SUBREPORTVARIABLE", "TOVARIABLE", "CALCULATIONTYPE", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				"INCREMENTERFACTORYCLASS" }); //$NON-NLS-1$
	}

	public boolean validate() {
		// validate toVariable is unique
		List<String> lto = new ArrayList<String>();
		List<JRDesignSubreportReturnValue> input = (List<JRDesignSubreportReturnValue>) tableViewer.getInput();
		for (JRDesignSubreportReturnValue d : input)
			lto.add(d.getToVariable());
		int size = lto.size();
		int setSize = new HashSet<String>(lto).size();
		if (size != setSize) {
			setErrorMessage(Messages.RVPropertyPage_error_message_return_variables_contain_duplicate_tovariable_values);
			setPageComplete(false);
			return false;
		} else {
			setErrorMessage(null);
			setPageComplete(true);
		}
		return true;
	}

	public String[] getToVariables() {
		if (toVariables == null) {
			List<String> res = new ArrayList<String>();
			List<JRVariable> vlist = dto.getjConfig().getJasperDesign().getVariablesList();
			for (Object o : vlist) {
				JRDesignVariable jdVar = (JRDesignVariable) o;
				if (!jdVar.isSystemDefined())
					res.add(jdVar.getName());
			}
			toVariables = res.toArray(new String[res.size()]);
		}
		return toVariables;
	}

	private List<String> srcParamNames = new ArrayList<String>();

	private void getSubreport() {
		JRSubreport sr = (JRSubreport) dto.getProp1();
		if (sr.getExpression() != null) {
			String path = sr.getExpression().getText();
			path = path.replace("\"", ""); //$NON-NLS-1$ //$NON-NLS-2$

			JRBaseReport jd = getFromJRXML(path.replaceAll(".jasper", ".jrxml"));
			if (jd == null)
				jd = getFromJasper(path);

			if (jd != null) {
				JRParameter[] prms = jd.getParameters();
				for (JRParameter p : prms)
					srcParamNames.add(p.getName());
			}
		}
	}

	private JRBaseReport getFromJasper(String path) {
		InputStream in = null;
		JRBaseReport jd = null;
		try {
			in = RepositoryUtil.getInstance(dto.getjConfig()).getInputStreamFromLocation(path);
			if (in != null) {
				Object obj = JRLoader.loadObject(in);
				if (obj instanceof JasperReport)
					jd = (JasperReport) obj;
			}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			FileUtils.closeStream(in);
		}
		return jd;
	}

	private JRBaseReport getFromJRXML(String path) {
		InputStream in = null;
		JRBaseReport jd = null;
		try {
			in = RepositoryUtil.getInstance(dto.getjConfig()).getInputStreamFromLocation(path);
			if (in != null) {
				InputSource is = new InputSource(new InputStreamReader(in, "UTF-8"));
				jd = new JRXmlLoader(dto.getjConfig(), JasperReportsConfiguration.getJRXMLDigester()).loadXML(is);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			FileUtils.closeStream(in);
		}
		return jd;
	}

	private void fillTable(Table table) {
		List<JRSubreportReturnValue> props = new ArrayList<JRSubreportReturnValue>();
		for (JRSubreportReturnValue v : value)
			props.add(v);
		tableViewer.setInput(props);
		getSubreport();
	}

}
