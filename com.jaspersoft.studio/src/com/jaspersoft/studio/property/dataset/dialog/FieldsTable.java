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
import java.util.Map;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.part.PluginTransfer;

import com.jaspersoft.studio.dnd.NodeDragListener;
import com.jaspersoft.studio.dnd.NodeTableDropAdapter;
import com.jaspersoft.studio.dnd.NodeTransfer;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.sortfield.command.wizard.WizardSortFieldPage.SHOW_TYPE;
import com.jaspersoft.studio.property.descriptor.classname.ClassTypeCellEditor;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.UIUtil;

public class FieldsTable {
	private TableViewer tviewer;
	private Table wtable;
	private Composite composite;
	private JRDesignDataset dataset;
	private Color background;

	public FieldsTable(Composite parent, JRDesignDataset dataset, Color background) {
		this.dataset = dataset;
		this.background = background;
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

		wtable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
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
		col[1].setText(Messages.common_classTypeLabel);
		col[1].pack();

		col[2] = new TableColumn(wtable, SWT.NONE);
		col[2].setText(Messages.common_descriptionLabel);
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
		addDropSupport();

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton() {
			protected void afterElementAdded(Object selement) {
				try {
					dataset.addField((JRField) selement);
				} catch (JRException e) {
					e.printStackTrace();
				}
			};
		}.createNewButtons(bGroup, tviewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				JRDesignField f = new JRDesignField();
				f.setName(getName());
				f.setValueClass(String.class);
				return f;
			}

			private String getName() {
				List<JRDesignField> list = (List<JRDesignField>) tviewer.getInput();
				String name = "Field"; //$NON-NLS-1$
				boolean match = false;
				String tmp = name;
				for (int i = 1; i < 100000; i++) {
					tmp = ModelUtils.getNameFormat(name, i);

					for (JRDesignField f : list) {
						match = f.getName().equals(tmp);
						if (match)
							break;
					}
					if (!match)
						break;
				}
				return tmp;
			}
		});
		new DeleteButton() {
			protected void afterElementDeleted(Object element) {
				if (element != null)
					dataset.removeField(((JRDesignField) element).getName());
			};
		}.createDeleteButton(bGroup, tviewer);

		new ListOrderButtons().createOrderButtons(bGroup, tviewer);

		List<JRField> fields = dataset.getFieldsList();
		if (fields == null)
			fields = new ArrayList<JRField>();
		setFields(fields);
	}

	private void addDropSupport() {
		int ops = DND.DROP_COPY | DND.DROP_MOVE;
		Transfer[] transfers = new Transfer[] { NodeTransfer.getInstance(), PluginTransfer.getInstance() };
		tviewer.addDragSupport(ops, transfers, new NodeDragListener(tviewer));

		transfers = new Transfer[] { NodeTransfer.getInstance() };
		NodeTableDropAdapter dropAdapter = new NodeTableDropAdapter(tviewer) {
			@Override
			public boolean performDrop(Object data) {
				if (data instanceof ANode[]) {
					ANode[] nodes = (ANode[]) data;
					List<JRField> fields = (List<JRField>) tviewer.getInput();
					for (ANode n : nodes) {
						JRDesignField f = (JRDesignField) n.getAdapter(JRDesignField.class);
						if (f != null) {
							// be sure that the name is ok
							f.setName(ModelUtils.getNameForField((List<JRDesignField>) tviewer.getInput(), f.getName()));
							fields.add(f);
						}
					}
					setFields(fields);
					return true;
				}
				return false;
			}
		};
		tviewer.addDropSupport(ops, transfers, dropAdapter);
	}

	public <T extends JRField> void setFields(List<T> fields) {
		List<T> newfields = new ArrayList<T>(fields);
		tviewer.setInput(newfields);
		tviewer.refresh();

		for (JRField f : dataset.getFields())
			dataset.removeField(f);
		for (JRField f : newfields)
			try {
				dataset.addField(f);
			} catch (JRException e) {
				e.printStackTrace();
			}
	}

	public List<JRDesignField> getFields() {
		return (List<JRDesignField>) tviewer.getInput();
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				if (property.equals("NAME")) //$NON-NLS-1$
					return true;
				if (property.equals("TYPE")) //$NON-NLS-1$
					return true;
				if (property.equals("DESCRIPTION")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRDesignField prop = (JRDesignField) element;
				if ("NAME".equals(property)) //$NON-NLS-1$
					return prop.getName();
				if ("TYPE".equals(property)) //$NON-NLS-1$
					return prop.getValueClassName();
				if ("DESCRIPTION".equals(property)) //$NON-NLS-1$
					return Misc.nvl(prop.getDescription(), ""); //$NON-NLS-1$

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				JRDesignField field = (JRDesignField) tableItem.getData();
				if ("NAME".equals(property)) { //$NON-NLS-1$
					List<JRDesignField> list = (List<JRDesignField>) tviewer.getInput();
					boolean exists = false;
					for (JRDesignField f : list) {
						exists = f.getName().equals(value);
						if (exists)
							break;
					}
					if (!exists) {
						String oldName = field.getName();
						dataset.getFieldsMap().remove(oldName);
						field.setName((String) value);
						dataset.getFieldsMap().put(field.getName(), field);

						Map<String, JRSortField> sortFields = dataset.getSortFieldsMap();
						JRSortField sf = sortFields.get(oldName + "|" + SortFieldTypeEnum.FIELD.getName());
						// If a field with the same name is not present or if it is present but with a different type then show it
						if (sf != null) {
							dataset.removeSortField(sf);
							((JRDesignSortField) sf).setName(field.getName());
							try {
								dataset.addSortField(sf);
							} catch (JRException e) {
								e.printStackTrace();
							}
						}
					}
				} else if ("TYPE".equals(property)) { //$NON-NLS-1$
					field.setValueClassName((String) value);
				} else if ("DESCRIPTION".equals(property)) { //$NON-NLS-1$
					field.setDescription((String) value);
				}
				tviewer.update(element, new String[] { property });
				tviewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new ClassTypeCellEditor(parent),
				new TextCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "NAME", "TYPE", "DESCRIPTION" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			JRDesignField field = (JRDesignField) element;
			switch (columnIndex) {
			case 0:
				return field.getName();
			case 1:
				return Misc.nvl(field.getValueClassName(), ""); //$NON-NLS-1$
			case 2:
				return Misc.nvl(field.getDescription(), ""); //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}
}
