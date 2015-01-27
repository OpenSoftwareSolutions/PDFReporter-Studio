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
package com.jaspersoft.studio.property.descriptor.genericElement.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignGenericElementParameter;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableCursor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionCellEditor;
import com.jaspersoft.studio.utils.Misc;

public class ParameterPage extends WizardPage implements IExpressionContextSetter {
	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((JRGenericElementParameter) element).getName();
			case 1:
				JRGenericElementParameter value2 = (JRGenericElementParameter) element;
				if (value2 != null && value2.getValueExpression() != null)
					return value2.getValueExpression().getText();
				break;
			case 2:
				return Boolean.toString(((JRGenericElementParameter) element).isSkipWhenEmpty());
			}

			return ""; //$NON-NLS-1$
		}
	}

	private ParameterDTO value;
	private Table table;
	private TableViewer tableViewer;
	private ExpressionContext expContext;

	// private TableCursor cursor;

	public ParameterDTO getValue() {
		return value;
	}

	@Override
	public void dispose() {
		// clear all properties
		List<JRGenericElementParameter> props = (List<JRGenericElementParameter>) tableViewer.getInput();
		value = new ParameterDTO();
		value.setValue(props.toArray(new JRGenericElementParameter[props.size()]));

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
		setTitle(Messages.ParameterPage_generic_elements_parameters);
		setDescription(Messages.ParameterPage_description);

	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		setControl(composite);

		buildTable(composite);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalSpan = 2;
		gd.heightHint = 400;
		gd.widthHint = 600;
		table.setLayoutData(gd);

		Button addB = new Button(composite, SWT.PUSH | SWT.CENTER);
		addB.setText(Messages.common_add);
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		addB.setLayoutData(gridData);
		addB.addSelectionListener(new SelectionAdapter() {

			// Remove the selection and refresh the view
			@Override
			public void widgetSelected(SelectionEvent e) {
				List<JRGenericElementParameter> list = (List<JRGenericElementParameter>) tableViewer.getInput();
				String newName = "NEW PARAMETER"; //$NON-NLS-1$
				for (int i = 1; i < Integer.MAX_VALUE; i++) {
					if (checkName(newName, list))
						newName = "NEW PARAMETER " + i; //$NON-NLS-1$
					else
						break;
				}
				JRDesignGenericElementParameter p = new JRDesignGenericElementParameter();
				JRDesignExpression expression = new JRDesignExpression();
				p.setValueExpression(expression);
				p.setName(newName); //$NON-NLS-1$
				list.add(p);
				tableViewer.add(p);
				tableViewer.setSelection(new StructuredSelection(p));
				// cursor.setSelection(table.getSelectionIndex(), 0);
				tableViewer.refresh();
				table.setFocus();
			}

			private boolean checkName(String newName, List<JRGenericElementParameter> list) {
				for (JRGenericElementParameter dto : list) {
					if (dto.getName() == null || dto.getName().trim().equals(newName)) //$NON-NLS-1$
						return true;
				}
				return false;
			}
		});

		Button delB = new Button(composite, SWT.PUSH | SWT.CENTER);
		delB.setText(Messages.common_delete);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING | GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.widthHint = 80;
		delB.setLayoutData(gridData);
		delB.addSelectionListener(new SelectionAdapter() {

			// Remove the selection and refresh the view
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection iStructuredSelection = (IStructuredSelection) tableViewer.getSelection();
				JRGenericElementParameter property = (JRGenericElementParameter) iStructuredSelection.getFirstElement();
				Object input = tableViewer.getInput();
				if (input instanceof List<?>) {
					List<?> list = (List<?>) input;
					int index = list.indexOf(property);
					list.remove(property);
					tableViewer.remove(property);
					tableViewer.refresh();
					Object sp = null;
					if (index >= list.size())
						index = list.size() - 1;
					if (index >= 0)
						sp = list.get(index);

					if (sp != null) {
						tableViewer.setSelection(new StructuredSelection(sp));
						// cursor.setSelection(table.getSelectionIndex(), 0);
					} else
						setMessage(Messages.common_table_is_empty);
				}
			}
		});
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setToolTipText("");
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		// cursor = new TableCursor(table, SWT.NONE);

		tableViewer = new TableViewer(table);
		attachContentProvider(tableViewer);
		attachLabelProvider(tableViewer);
		attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		table.setLayout(tlayout);

		setColumnToolTip();

		TableColumn[] column = new TableColumn[3];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.ParameterPage_parameter);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.common_expression);

		column[2] = new TableColumn(table, SWT.NONE);
		column[2].setText(Messages.ParameterPage_skip_empty);

		fillTable(table);
		for (int i = 0, n = column.length; i < n; i++) {
			column[i].pack();
		}
		table.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TableItem) {
					setMessage(getDescription(((TableItem) e.item)));
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	/**
	 * @param tableViewer
	 * @param cursor
	 */
	static void editCell(final TableViewer tableViewer, final TableCursor cursor) {
		tableViewer.editElement(cursor.getRow().getData(), cursor.getColumn());
		// hide cursor only f there is an editor active on the cell
		cursor.setVisible(!tableViewer.isCellEditorActive());
	}

	private void attachContentProvider(TableViewer viewer) {
		viewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				return ((List<?>) inputElement).toArray();
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}
		});
	}

	private void attachLabelProvider(TableViewer viewer) {
		viewer.setLabelProvider(new TLabelProvider());
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("VALUE")) //$NON-NLS-1$
					return true;
				if (property.equals("NAME")) //$NON-NLS-1$
					return true;
				if (property.equals("SKIPONEMPTY")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRGenericElementParameter prop = (JRGenericElementParameter) element;
				if ("VALUE".equals(property)) //$NON-NLS-1$
					if (prop.getValueExpression() != null)
						return Misc.nvl(prop.getValueExpression().getText(), "");
				if ("NAME".equals(property)) //$NON-NLS-1$
					return prop.getName();
				if ("SKIPONEMPTY".equals(property)) //$NON-NLS-1$
					return new Integer(prop.isSkipWhenEmpty() ? 0 : 1);

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				setErrorMessage(null);
				setMessage(getDescription(tableItem));
				JRDesignGenericElementParameter data = (JRDesignGenericElementParameter) tableItem.getData();
				if ("VALUE".equals(property)) { //$NON-NLS-1$
					if (value instanceof JRExpression) {
						data.setValueExpression((JRExpression) value);
					}
				} else if ("NAME".equals(property)) { //$NON-NLS-1$
					List<JRDesignGenericElementParameter> plist = (List<JRDesignGenericElementParameter>) tableViewer.getInput();
					for (JRDesignGenericElementParameter p : plist) {
						if (p != data && p.getName() != null && p.getName().equals(value)) {
							setErrorMessage(Messages.common_error_message_unique_properties);
							return;
						}
					}
					data.setName((String) value);
				} else if ("SKIPONEMPTY".equals(property)) { //$NON-NLS-1$
					data.setSkipWhenEmpty(new Boolean(value != null && ((Integer) value).intValue() == 1));
				}
				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
			}
		});

		JRExpressionCellEditor jrExpressionCellEditor = new JRExpressionCellEditor(parent, expContext);
		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), jrExpressionCellEditor,
				new ComboBoxCellEditor(parent, new String[] { "false", "true" }) }); //$NON-NLS-1$ //$NON-NLS-2$
		viewer.setColumnProperties(new String[] { "NAME", "VALUE", "SKIPONEMPTY" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private void fillTable(Table table) {
		List<JRGenericElementParameter> lst = new ArrayList<JRGenericElementParameter>();
		if (value.getValue() != null)
			lst.addAll(Arrays.asList(value.getValue()));
		tableViewer.setInput(lst);
	}

	private void setColumnToolTip() {
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM"); //$NON-NLS-1$
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					// fall through
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};

		Listener tableListener = new Listener() {
			Shell tip = null;

			Label label = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null)
						break;
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TableItem item = table.getItem(new Point(event.x, event.y));
					String description = getDescription(item);
					if (item != null && !description.equals("")) { //$NON-NLS-1$

						if (tip != null && !tip.isDisposed())
							tip.dispose();
						tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.TOOL);
						tip.setLayout(new FillLayout());
						label = new Label(tip, SWT.NONE);
						label.setForeground(table.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(table.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData("_TABLEITEM", item); //$NON-NLS-1$

						label.setText(description);
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds(0);
						Point pt = table.toDisplay(rect.x, rect.y);
						tip.setBounds(pt.x, pt.y, size.x, size.y);
						tip.setVisible(true);
					}
				}
				}
			}
		};
		table.addListener(SWT.Dispose, tableListener);
		table.addListener(SWT.KeyDown, tableListener);
		table.addListener(SWT.MouseMove, tableListener);
		table.addListener(SWT.MouseHover, tableListener);
	}

	private String getDescription(TableItem item) {
		// String key = ((SubreportPropertyDTO) item.getData()).getProperty();
		// List<SubreportPropertyDTO> dp = getDefaultProperties();
		// for (SubreportPropertyDTO p : dp) {
		// if (p.getProperty().equals(key))
		// return p.getDescription();
		// }
		return ""; //$NON-NLS-1$
	}

	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}

}
