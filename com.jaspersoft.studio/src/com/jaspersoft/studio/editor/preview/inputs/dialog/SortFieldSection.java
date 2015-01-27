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
package com.jaspersoft.studio.editor.preview.inputs.dialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

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
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.MoveT2TButtons;
import com.jaspersoft.studio.utils.EnumHelper;

public class SortFieldSection {
	private List<JRSortField> inFields;
	private List<JRSortField> outFields;

	private List<JRParameter> prompts;
	private Table rightTable;
	private Table leftTable;
	private TableViewer rightTView;
	private TableViewer leftTView;

	public SortFieldSection() {
		super();
	}

	public Control createSortField(Composite tabFolder) {
		if (prompts != null)
			for (JRParameter p : prompts)
				if (p.getName().equals("SORT_FIELDS")) {//$NON-NLS-1$

					Composite composite = new Composite(tabFolder, SWT.NONE);
					composite.setLayout(new GridLayout(4, false));
					composite.setBackground(tabFolder.getBackground());
					composite.setLayoutData(new GridData(GridData.FILL_BOTH));

					leftTable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
					leftTable.setBackground(tabFolder.getBackground());
					GridData gd = new GridData(GridData.FILL_VERTICAL);
					gd.widthHint = 150;
					leftTable.setLayoutData(gd);
					leftTable.setHeaderVisible(true);

					TableColumn[] col = new TableColumn[1];
					col[0] = new TableColumn(leftTable, SWT.NONE);
					col[0].setText(Messages.common_report_objects);
					col[0].pack();

					TableLayout tlayout = new TableLayout();
					tlayout.addColumnData(new ColumnWeightData(100, false));
					leftTable.setLayout(tlayout);

					leftTView = new TableViewer(leftTable);
					leftTView.setContentProvider(new ListContentProvider());
					leftTView.setLabelProvider(new TLabelProvider());

					Composite bGroup = new Composite(composite, SWT.NONE);
					bGroup.setBackground(tabFolder.getBackground());
					bGroup.setLayout(new GridLayout(1, false));
					bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

					// -----------------------------------
					rightTable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
					rightTable.setBackground(tabFolder.getBackground());
					rightTable.setLayoutData(new GridData(GridData.FILL_BOTH));
					rightTable.setHeaderVisible(true);

					col = new TableColumn[2];
					col[0] = new TableColumn(rightTable, SWT.NONE);
					col[0].setText(Messages.SortFieldSection_sort_field);
					col[0].pack();

					col[0] = new TableColumn(rightTable, SWT.NONE);
					col[0].setText(Messages.SortFieldSection_sort_order);
					col[0].pack();

					tlayout = new TableLayout();
					tlayout.addColumnData(new ColumnWeightData(65, true));
					tlayout.addColumnData(new ColumnWeightData(35, true));
					rightTable.setLayout(tlayout);

					rightTView = new TableViewer(rightTable);
					rightTView.setContentProvider(new ListContentProvider());
					rightTView.setLabelProvider(new TLabelProvider());

					attachCellEditors(rightTView, rightTable);

					createOrderButtons(tabFolder, composite);

					new MoveT2TButtons().createButtons(bGroup, leftTView, rightTView);

					return composite;
				}
		return null;
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("NAME")) //$NON-NLS-1$
					return false;
				if (property.equals("ORDER")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRDesignSortField prop = (JRDesignSortField) element;
				if ("NAME".equals(property)) //$NON-NLS-1$
					return prop.getName();
				if ("ORDER".equals(property)) //$NON-NLS-1$
					return EnumHelper.getValue(prop.getOrderValue(), 1, false);
				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				JRDesignSortField field = (JRDesignSortField) tableItem.getData();
				if ("NAME".equals(property)) { //$NON-NLS-1$
					field.setName((String) value);
				} else if ("ORDER".equals(property)) { //$NON-NLS-1$
					field.setOrder((SortOrderEnum) EnumHelper.getSetValue(SortOrderEnum.values(), value, 1, false));
				}
				viewer.update(element, new String[] { property });
				viewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent),
				new ComboBoxCellEditor(parent, EnumHelper.getEnumNames(SortOrderEnum.values(), NullEnum.NOTNULL)) });
		viewer.setColumnProperties(new String[] { "NAME", "ORDER" }); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				if (((JRSortField) element).getType().equals(SortFieldTypeEnum.FIELD))
					return JaspersoftStudioPlugin.getInstance().getImage(MField.getIconDescriptor().getIcon16());
				return JaspersoftStudioPlugin.getInstance().getImage(MVariable.getIconDescriptor().getIcon16());
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((JRSortField) element).getName();
			case 1:
				return ((JRSortField) element).getOrderValue().getName();
			}
			return ""; //$NON-NLS-1$
		}
	}

	private void createOrderButtons(Composite tabFolder, Composite composite) {
		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setBackground(tabFolder.getBackground());
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new ListOrderButtons().createOrderButtons(bGroup, rightTView);
	}

	@SuppressWarnings("unchecked")
	public void fillTable(final Composite tabFolder, final JasperDesign jDesign, List<JRParameter> prompts,
			final Map<String, Object> params) {
		this.prompts = prompts;
		if (prompts != null)
			UIUtils.getDisplay().asyncExec(new Runnable() {

				@Override
				public void run() {
					createSortField(tabFolder);
					inFields = new ArrayList<JRSortField>();
					List<JRField> flist = jDesign.getFieldsList();
					for (JRField f : flist) {
						inFields.add(new JRDesignSortField(f.getName(), SortFieldTypeEnum.FIELD, SortOrderEnum.ASCENDING));
					}
					List<JRVariable> vlist = jDesign.getVariablesList();
					for (JRVariable f : vlist) {
						inFields.add(new JRDesignSortField(f.getName(), SortFieldTypeEnum.VARIABLE, SortOrderEnum.ASCENDING));
					}
					leftTView.setInput(inFields);

					Object obj = params.get("SORT_FIELDS");//$NON-NLS-1$
					if (obj == null || !(obj instanceof List)) {
						outFields = new ArrayList<JRSortField>();

						params.put("SORT_FIELDS", outFields);//$NON-NLS-1$
					} else
						outFields = (List<JRSortField>) obj;

					// check if fields exists in the report
					List<JRSortField> dlist = new ArrayList<JRSortField>();
					for (JRSortField f : outFields) {
						if (f.getType().equals(SortFieldTypeEnum.FIELD) && jDesign.getFieldsMap().get(f.getName()) == null)
							dlist.add(f);
						if (f.getType().equals(SortFieldTypeEnum.VARIABLE) && jDesign.getVariablesMap().get(f.getName()) == null)
							dlist.add(f);
					}
					outFields.removeAll(dlist);

					rightTView.setInput(outFields);
				}
			});
	}
}
