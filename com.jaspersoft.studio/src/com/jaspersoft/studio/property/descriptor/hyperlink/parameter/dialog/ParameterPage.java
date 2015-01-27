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
package com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignHyperlinkParameter;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionCellEditor;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class ParameterPage extends WizardPage implements IExpressionContextSetter {
	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			JRHyperlinkParameter hparam = (JRHyperlinkParameter) element;
			switch (columnIndex) {
			case 0:
				return hparam.getName();
			case 1:
				if (hparam != null && hparam.getValueExpression() != null)
					return hparam.getValueExpression().getText();
			}
			return ""; //$NON-NLS-1$
		}
	}

	private ParameterDTO value;
	private Table table;
	private TableViewer tableViewer;
	private ExpressionContext expContext;

	public ParameterDTO getValue() {
		return value;
	}

	@Override
	public void dispose() {
		List<JRHyperlinkParameter> props = (List<JRHyperlinkParameter>) tableViewer.getInput();

		value = new ParameterDTO();
		value.setValue(props.toArray(new JRHyperlinkParameter[props.size()]));

		super.dispose();
	}

	public void setValue(ParameterDTO value) {
		this.value = value;
		if (value == null) {
			value = new ParameterDTO();
		}
		if (table != null)
			fillTable(table);
	}

	protected ParameterPage(String pageName) {
		super(pageName);
		setTitle(Messages.HyperlinkParameterPage_Title);
		setDescription(Messages.HyperlinkParameterPage_Description);

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
				JRDesignHyperlinkParameter param = new JRDesignHyperlinkParameter();
				int i = 0;
				String name = "NEW_PARAMETER";//$NON-NLS-1$
				while (getName(input, name, i) == null)
					i++;
				name += "_" + i;//$NON-NLS-1$
				param.setName(name);
				JRDesignExpression expression = new JRDesignExpression();
				expression.setText("");//$NON-NLS-1$
				param.setValueExpression(expression);

				return param;
			}

			private String getName(List<?> input, String name, int i) {
				name += "_" + i;//$NON-NLS-1$
				for (Object dto : input) {
					JRDesignHyperlinkParameter prm = (JRDesignHyperlinkParameter) dto;
					if (prm.getName() != null && prm.getName().trim().equals(name)) {
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
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.ParameterPage_parameter);

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
				JRHyperlinkParameter prop = (JRHyperlinkParameter) element;
				if ("VALUE".equals(property)) //$NON-NLS-1$
					if (prop.getValueExpression() != null)
						return prop.getValueExpression();
				if ("NAME".equals(property)) { //$NON-NLS-1$
					return prop.getName();
				}
				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				setErrorMessage(null);
				setMessage(getDescription());
				JRDesignHyperlinkParameter data = (JRDesignHyperlinkParameter) tableItem.getData();
				if ("VALUE".equals(property)) { //$NON-NLS-1$
					if (value instanceof JRExpression) {
						data.setValueExpression((JRExpression) value);
					} else {
						value.getClass();
					}
				} else if ("NAME".equals(property)) { //$NON-NLS-1$
					List<JRHyperlinkParameter> plist = (List<JRHyperlinkParameter>) tableViewer.getInput();
					for (JRHyperlinkParameter p : plist) {
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

		JRExpressionCellEditor jrExpressionCellEditor = new JRExpressionCellEditor(parent, expContext);
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), jrExpressionCellEditor });
		viewer.setColumnProperties(new String[] { "NAME", "VALUE" }); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void fillTable(Table table) {
		List<JRHyperlinkParameter> lst = new ArrayList<JRHyperlinkParameter>();
		if (value.getValue() != null)
			lst.addAll(Arrays.asList(value.getValue()));
		tableViewer.setInput(lst);
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}

}
