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
package com.jaspersoft.studio.swt.widgets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
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

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class PropertiesComponent {
	class Property {
		public String key;
		public String value;

		public Property(String key, String value) {
			this.key = key;
			this.value = value;
		}
	}

	class ArrayLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			if (element instanceof Property) {
				switch (columnIndex) {
				case 0:
					return ((Property) element).key;
				case 1:
					return ((Property) element).value;
				}
			}
			return ""; //$NON-NLS-1$
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	private Map<String, String> properties;

	public Map<String, String> getProperties() {
		List<Property> list = (List<Property>) tviewer.getInput();
		if (list == null)
			list = new ArrayList<Property>();
		for (Property prop : list)
			properties.put(prop.key, prop.value);
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		if (properties == null)
			properties = new HashMap<String, String>();
		this.properties = properties;
		List<Property> list = new ArrayList<Property>();
		for (Entry<String, String> entry : properties.entrySet())
			list.add(new Property(entry.getKey(), entry.getValue()));

		tviewer.setInput(list);
	}

	public PropertiesComponent(Composite parent) {
		createComponent(parent);
	}

	private Control control;
	private TableViewer tviewer;
	private Table wtable;

	public Control getControl() {
		return control;
	}

	public void createComponent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		wtable = new Table(composite, SWT.V_SCROLL | SWT.MULTI | SWT.FULL_SELECTION | SWT.BORDER);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 100;
		gd.heightHint = 250;
		wtable.setLayoutData(gd);
		wtable.setHeaderVisible(true);

		TableColumn[] col = new TableColumn[2];
		col[0] = new TableColumn(wtable, SWT.NONE);
		col[0].setText(Messages.PropertiesComponent_0);
		col[0].pack();

		col[1] = new TableColumn(wtable, SWT.NONE);
		col[1].setText(Messages.PropertiesComponent_1);
		col[1].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(60, false));
		tlayout.addColumnData(new ColumnWeightData(40, false));
		wtable.setLayout(tlayout);

		tviewer = new TableViewer(wtable);
		tviewer.setContentProvider(new ListContentProvider());
		tviewer.setLabelProvider(new ArrayLabelProvider());
		attachCellEditors(tviewer, wtable);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		bGroup.setBackground(parent.getBackground());

		new NewButton().createNewButtons(bGroup, tviewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				return new Property("property", "value"); //$NON-NLS-1$ //$NON-NLS-2$
			}

		});
		new DeleteButton().createDeleteButton(bGroup, tviewer);

		this.control = composite;
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals("KEY")) //$NON-NLS-1$
					return true;
				if (property.equals("VALUE")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				Property prop = (Property) element;
				if ("KEY".equals(property)) //$NON-NLS-1$
					return prop.key;
				if ("VALUE".equals(property)) //$NON-NLS-1$
					return prop.value;

				return ""; //$NON-NLS-1$
			}

			public void modify(Object element, String property, Object value) {
				TableItem tableItem = (TableItem) element;
				Property field = (Property) tableItem.getData();
				if ("KEY".equals(property)) { //$NON-NLS-1$
					field.key = (String) value;
				} else if ("VALUE".equals(property)) { //$NON-NLS-1$
					field.value = (String) value;
				}
				tviewer.update(element, new String[] { property });
				tviewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new TextCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "KEY", "VALUE" }); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
