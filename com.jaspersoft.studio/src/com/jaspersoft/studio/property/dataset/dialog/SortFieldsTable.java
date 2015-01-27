/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.property.dataset.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.sortfield.MSortField;
import com.jaspersoft.studio.model.sortfield.command.wizard.SortFieldWizard;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.UIUtil;

public class SortFieldsTable {
	private TableViewer tviewer;
	private Table wtable;
	private Composite composite;
	private JRDesignDataset dataset;
	private Color background;

	public SortFieldsTable(Composite parent, JRDesignDataset dataset, Color background) {
		this.background = background;
		this.dataset = dataset;
		createControl(parent);
	}

	public Composite getControl() {
		return composite;
	}

	private void createControl(Composite parent) {
		composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		composite.setBackground(background);
		composite.setBackgroundMode(SWT.INHERIT_FORCE);

		wtable = new Table(composite, SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 100;
		wtable.setLayoutData(gd);
		wtable.setHeaderVisible(true);
		wtable.setLinesVisible(true);

		TableColumn[] col = new TableColumn[3];
		col[0] = new TableColumn(wtable, SWT.NONE);
		col[0].setText(Messages.common_fieldNameLabel);
		col[0].pack();

		col[1] = new TableColumn(wtable, SWT.NONE);
		col[1].setText(Messages.common_type);
		col[1].pack();

		col[2] = new TableColumn(wtable, SWT.NONE);
		col[2].setText(Messages.common_order);
		col[2].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100, false));
		tlayout.addColumnData(new ColumnWeightData(100, false));
		tlayout.addColumnData(new ColumnWeightData(100, false));
		wtable.setLayout(tlayout);

		tviewer = new TableViewer(wtable);
		tviewer.setContentProvider(new ListContentProvider());
		tviewer.setLabelProvider(new TLabelProvider());
		attachCellEditors(tviewer, wtable);
		UIUtil.setViewerCellEditingOnDblClick(tviewer);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		bGroup.setBackground(background);

		new NewButton() {
			protected void afterElementAdded(Object selement) {
				try {
					dataset.removeSortField((JRSortField) selement);
					dataset.addSortField((JRSortField) selement);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}.createNewButtons(bGroup, tviewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				List<JRDesignSortField> fields = new ArrayList<JRDesignSortField>(getFields());
				for (JRSortField f : dataset.getSortFields())
					dataset.removeSortField(f);
				for (JRDesignSortField f : fields)
					try {
						dataset.addSortField(f);
					} catch (JRException e) {
						e.printStackTrace();
					}

				JRDesignSortField jrField = MSortField.createJRSortField(dataset);
				SortFieldWizard wizard = new SortFieldWizard();
				wizard.init(dataset, jrField);
				WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
				dialog.create();
				if (dialog.open() != Dialog.OK)
					return null;
				return jrField;
			}

		});
		new DeleteButton() {
			protected void afterElementDeleted(Object element) {
				if (element != null)
					dataset.removeSortField((JRSortField) element);
			}
		}.createDeleteButton(bGroup, tviewer);

		new ListOrderButtons().createOrderButtons(bGroup, tviewer);

		List<JRSortField> fields = dataset.getSortFieldsList();
		if (fields == null)
			fields = new ArrayList<JRSortField>();
		tviewer.setInput(fields);
	}

	public List<JRDesignSortField> getFields() {
		return (List<JRDesignSortField>) tviewer.getInput();
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("NAME")) //$NON-NLS-1$
					return false;
				if (property.equals("TYPE")) //$NON-NLS-1$
					return false;
				if (property.equals("ORDER")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRDesignSortField prop = (JRDesignSortField) element;
				if ("NAME".equals(property)) //$NON-NLS-1$
					return prop.getName();
				if ("TYPE".equals(property)) //$NON-NLS-1$
					return EnumHelper.getValue(prop.getType(), 0, false);
				if ("ORDER".equals(property)) //$NON-NLS-1$
					return EnumHelper.getValue(prop.getOrderValue(), 1, false);

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				JRDesignSortField field = (JRDesignSortField) tableItem.getData();
				if ("NAME".equals(property)) { //$NON-NLS-1$
					field.setName((String) value);
				} else if ("TYPE".equals(property)) { //$NON-NLS-1$
					field.setType((SortFieldTypeEnum) EnumHelper.getSetValue(SortFieldTypeEnum.values(), value, 0, false));
				} else if ("ORDER".equals(property)) { //$NON-NLS-1$
					field.setOrder((SortOrderEnum) EnumHelper.getSetValue(SortOrderEnum.values(), value, 1, false));
				}
				tviewer.update(element, new String[] { property });
				tviewer.refresh();
			}
		});

		viewer
				.setCellEditors(new CellEditor[] {
						new TextCellEditor(parent),
						new ComboBoxCellEditor(parent, EnumHelper.getEnumNames(SortFieldTypeEnum.values(), NullEnum.NOTNULL)),
						new ComboBoxCellEditor(parent, EnumHelper.getEnumNames(SortOrderEnum.values(), NullEnum.NOTNULL),
								SWT.READ_ONLY) });
		viewer.setColumnProperties(new String[] { "NAME", "TYPE", "ORDER" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			JRDesignSortField field = (JRDesignSortField) element;
			switch (columnIndex) {
			case 0:
				return field.getName();
			case 1:
				return field.getType().getName();
			case 2:
				if (field.getOrderValue() != null)
					return field.getOrderValue().getName();
			}
			return ""; //$NON-NLS-1$
		}

		public Image getColumnImage(Object element, int columnIndex) {
			JRDesignSortField field = (JRDesignSortField) element;
			switch (columnIndex) {
			case 0:
				if (field.getType().equals(SortFieldTypeEnum.FIELD))
					return JaspersoftStudioPlugin.getInstance().getImage(MField.getIconDescriptor().getIcon16());
				else
					return JaspersoftStudioPlugin.getInstance().getImage(MVariable.getIconDescriptor().getIcon16());
			}
			return null; //$NON-NLS-1$
		}
	}

	public void refresh() {
		tviewer.refresh();
	}
}
