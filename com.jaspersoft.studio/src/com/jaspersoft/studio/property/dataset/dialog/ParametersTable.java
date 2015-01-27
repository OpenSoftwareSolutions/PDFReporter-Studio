/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.property.dataset.dialog;

import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxCellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
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
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxLabelProvider;
import com.jaspersoft.studio.property.descriptor.classname.ClassTypeCellEditor;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.UIUtil;

public class ParametersTable {
	private TableViewer tviewer;
	private Table wtable;
	private Composite composite;
	private JRDesignDataset dataset;
	private Color background;
	private boolean isMainDataset;

	public ParametersTable(Composite parent, JRDesignDataset dataset, Color background, boolean isMainDataset) {
		this.background = background;
		this.isMainDataset = isMainDataset;
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

		wtable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 100;
		wtable.setLayoutData(gd);
		wtable.setHeaderVisible(true);
		wtable.setLinesVisible(true);

		TableColumn[] col = new TableColumn[4];
		col[0] = new TableColumn(wtable, SWT.NONE);
		col[0].setText(Messages.ParametersTable_name);

		col[1] = new TableColumn(wtable, SWT.NONE);
		col[1].setText(Messages.ParametersTable_isForPrompt);

		col[2] = new TableColumn(wtable, SWT.NONE);
		col[2].setText(Messages.ParametersTable_class);

		col[3] = new TableColumn(wtable, SWT.NONE);
		col[3].setText(Messages.ParametersTable_description);

		for (TableColumn tc : col)
			tc.pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(25, false));
		tlayout.addColumnData(new ColumnWeightData(25, false));
		tlayout.addColumnData(new ColumnWeightData(25, false));
		tlayout.addColumnData(new ColumnWeightData(25, false));
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
			@Override
			protected void afterElementAdded(Object selement) {
				try {
					dataset.removeParameter((JRParameter) selement);
					dataset.addParameter((JRDesignParameter) selement);
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}.createNewButtons(bGroup, tviewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				JRDesignParameter f = new JRDesignParameter();
				f.setName(getName());
				f.setValueClass(String.class);
				return f;
			}

			private String getName() {
				List<JRDesignParameter> list = (List<JRDesignParameter>) tviewer.getInput();
				String name = "Parameter"; //$NON-NLS-1$
				boolean match = false;
				String tmp = name;
				for (int i = 1; i < 100000; i++) {
					tmp = ModelUtils.getNameFormat(name, i);

					for (JRDesignParameter f : list) {
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
		final DeleteButton delb = new DeleteButton() {
			@Override
			protected void afterElementDeleted(Object element) {
				super.afterElementDeleted(element);
				dataset.removeParameter((JRDesignParameter) element);
			}
		};
		delb.createDeleteButton(bGroup, tviewer);

		List<JRParameter> fields = dataset.getParametersList();
		if (fields == null)
			fields = new ArrayList<JRParameter>();
		setFields(fields);

		tviewer.addSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection sel = (StructuredSelection) event.getSelection();
				if (!sel.isEmpty()) {
					JRDesignParameter prm = (JRDesignParameter) sel.getFirstElement();
					delb.setEnabled(!prm.isSystemDefined());
				}
			}
		});

		tviewer.addDragSupport(DND.DROP_COPY | DND.DROP_MOVE, new Transfer[] { TemplateTransfer.getInstance(),
				PluginTransfer.getInstance() }, new NodeDragListener(tviewer));
	}

	public <T extends JRParameter> void setFields(List<T> fields) {
		tviewer.setInput(new ArrayList(fields));
		tviewer.refresh();
	}

	public List<JRDesignParameter> getFields() {
		return (List<JRDesignParameter>) tviewer.getInput();
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				JRDesignParameter field = (JRDesignParameter) element;
				if (field.isSystemDefined())
					return false;
				if (property.equals("NAME")) //$NON-NLS-1$
					return true;
				if (property.equals("ISFORPROMPT")) //$NON-NLS-1$
					return true;
				if (property.equals("TYPE")) //$NON-NLS-1$
					return true;
				if (property.equals("DESCRIPTION")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				JRDesignParameter prop = (JRDesignParameter) element;
				if ("NAME".equals(property)) //$NON-NLS-1$
					return prop.getName();
				if ("ISFORPROMPT".equals(property)) //$NON-NLS-1$
					return prop.isForPrompting();
				if ("TYPE".equals(property)) //$NON-NLS-1$
					return prop.getValueClassName();
				if ("DESCRIPTION".equals(property)) //$NON-NLS-1$
					return Misc.nvl(prop.getDescription(), ""); //$NON-NLS-1$

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				JRDesignParameter field = (JRDesignParameter) tableItem.getData();
				if ("NAME".equals(property)) { //$NON-NLS-1$
					String old = field.getName();
					if (dataset.getParametersMap().get(old) != null) {
						dataset.getParametersMap().remove(old);
						field.setName((String) value);
						dataset.getParametersMap().put(field.getName(), field);
						propertyChangeSupport.firePropertyChange(new java.beans.PropertyChangeEvent(field,
								JRDesignParameter.PROPERTY_NAME, old, field.getName()));
					}
				} else if ("ISFORPROMPT".equals(property) && isMainDataset) { //$NON-NLS-1$
					field.setForPrompting((Boolean) value);
				} else if ("TYPE".equals(property)) { //$NON-NLS-1$
					field.setValueClassName((String) value);
				} else if ("DESCRIPTION".equals(property)) { //$NON-NLS-1$
					field.setDescription((String) value);
				}
				tviewer.update(element, new String[] { property });
				tviewer.refresh();

			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new CheckboxCellEditor(parent),
				new ClassTypeCellEditor(parent), new TextCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "NAME", "ISFORPROMPT", "TYPE", "DESCRIPTION" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	private PropertyChangeSupport propertyChangeSupport;

	public PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChangeSupport == null)
			propertyChangeSupport = new PropertyChangeSupport(this);
		return propertyChangeSupport;
	}

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {
		private CheckBoxLabelProvider cblp = new CheckBoxLabelProvider(NullEnum.NOTNULL);

		public Image getColumnImage(Object element, int columnIndex) {
			JRDesignParameter field = (JRDesignParameter) element;
			switch (columnIndex) {
			case 1:
				if (!field.isSystemDefined())
					return cblp.getCellEditorImage(field.isForPrompting());
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			JRDesignParameter field = (JRDesignParameter) element;
			switch (columnIndex) {
			case 0:
				return field.getName();
			case 1:
				if (field.isSystemDefined())
					return ""; //$NON-NLS-1$
				else
					return Boolean.toString(field.isForPrompting());
			case 2:
				return Misc.nvl(field.getValueClassName(), ""); //$NON-NLS-1$
			case 3:
				return Misc.nvl(field.getDescription(), ""); //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
		}

	}
}
