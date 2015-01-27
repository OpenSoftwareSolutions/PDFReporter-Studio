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
package com.jaspersoft.studio.server.wizard.resource.page;

import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
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

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ListItem;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.APageContent;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.Misc;

public class LovPageContent extends APageContent {

	private static final String VALUE = "VALUE";
	private static final String KEY = "KEY";

	public LovPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	public LovPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.lov"; //$NON-NLS-1$
	}

	@Override
	public String getName() {
		return Messages.LovPageContent_lov;
	}

	@Override
	public void dispose() {
		res.getValue().setListOfValues((List) tableViewer.getInput());
		super.dispose();
	}

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public String getColumnText(Object element, int columnIndex) {
			ListItem dto = (ListItem) element;
			if (dto != null)
				switch (columnIndex) {
				case 0:
					return dto.getLabel();
				case 1:
					if (dto.getValue() != null)
						return dto.getValue().toString();
				}
			return ""; //$NON-NLS-1$
		}

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}

	private Table table;
	private TableViewer tableViewer;

	public Control createContent(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));

		buildTable(composite);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		bGroup.setBackground(parent.getBackground());

		NewButton bnew = new NewButton();
		bnew.createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				ListItem li = new ListItem();
				li.setLabel(getName());
				li.setValue("value");

				return li;
			}

			private String getName() {
				String name = "name";
				if (exists(name))
					for (int i = 0; i < 1000; i++) {
						if (!exists(name + "_" + i))
							return name + "_" + i;
					}
				return name;
			}

		});

		DeleteButton bdel = new DeleteButton();
		bdel.createDeleteButton(bGroup, tableViewer);

		ListOrderButtons border = new ListOrderButtons();
		border.createOrderButtons(bGroup, tableViewer);

		tableViewer.setInput(res.getValue().getListOfValues());
		rebind();
		return composite;
	}

	@Override
	protected void rebind() {

	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.minimumHeight = 200;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		attachContentProvider(tableViewer);
		attachLabelProvider(tableViewer);
		attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50));
		tlayout.addColumnData(new ColumnWeightData(50));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText("Name");

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText("Value");

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

	}

	private void attachLabelProvider(TableViewer viewer) {
		viewer.setLabelProvider(new TLabelProvider());
	}

	private void attachContentProvider(TableViewer viewer) {
		viewer.setContentProvider(new ListContentProvider());
	}

	private void attachCellEditors(final TableViewer viewer, Composite parent) {
		viewer.setCellModifier(new ICellModifier() {
			public boolean canModify(Object element, String property) {
				if (property.equals(KEY))
					return true;
				if (property.equals(VALUE))
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				ListItem mi = (ListItem) element;
				if (property.equals(KEY))
					return Misc.nvl(mi.getLabel());
				if (property.equals(VALUE))
					return Misc.nvl(mi.getValue(), "");
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem ti = (TableItem) element;
				ListItem mi = (ListItem) ti.getData();

				if (property.equals(KEY)) {
					if (exists((String) value))
						return;
					mi.setLabel((String) value);
				}
				if (property.equals(VALUE))
					mi.setValue((String) value);

				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new TextCellEditor(parent), new TextCellEditor(parent) });
		viewer.setColumnProperties(new String[] { KEY, VALUE });
	}

	private boolean exists(String value) {
		List<ListItem> v = (List<ListItem>) tableViewer.getInput();
		for (ListItem li : v) {
			if (li.getLabel().equals(value))
				return true;
		}
		return false;
	}

	@Override
	public String getHelpContext() {
		return "com.jaspersoft.studio.doc.editListOfValue";
	}
}
