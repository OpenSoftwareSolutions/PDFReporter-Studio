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
package com.jaspersoft.studio.editor.preview.input.array;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.EditButton;
import com.jaspersoft.studio.swt.widgets.table.IEditElement;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class TableDialog extends Dialog {

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			if (element != null)
				if (element instanceof Date)
					return new SimpleDateFormat().format(element);
				else
					return element.toString();
			return "";
		}
	}

	private IParameter prm;
	private Table table;
	private TableViewer tableViewer;

	private List<?> value;
	private Object oldValue;
	private EditElement editElement;

	@Override
	protected void setReturnCode(int code) {
		super.setReturnCode(code);
		if (code == Dialog.OK) {
			value = (List<?>) tableViewer.getInput();
			if (oldValue instanceof Collection) {
				if (oldValue instanceof List) {
					oldValue = value;
				} else {
					((Collection) oldValue).clear();
					((Collection) oldValue).addAll(value);
				}
			} else if (oldValue.getClass().isArray())
				oldValue = value.toArray();
		}
	}

	public Object getValue() {
		return oldValue;
	}

	public TableDialog(Shell parentShell, Object value, IParameter prm) {
		super(parentShell);
		this.oldValue = value;
		if (value.getClass().isArray())
			this.value = new ArrayList(Arrays.asList(value));
		else if (value instanceof Collection)
			this.value = new ArrayList((Collection) value);
		this.prm = prm;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets.Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Parameter: " + prm.getName());
	}

	@Override
	protected Control createDialogArea(final Composite parent) {
		Composite cmp = (Composite) super.createDialogArea(parent);
		((GridLayout) cmp.getLayout()).numColumns = 2;
		((GridLayout) cmp.getLayout()).makeColumnsEqualWidth = false;

		Label lbl = new Label(cmp, SWT.WRAP);
		String prmDescription = prm.getDescription();
		lbl.setText(prmDescription != null ? prmDescription : "");
		lbl.setToolTipText(prm.getDescription());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		buildTable(cmp);

		Composite bGroup = new Composite(cmp, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				ElementDialog d = new ElementDialog(parent.getShell(), prm);
				if (input != null && !input.isEmpty()) {
					int indx = table.getSelectionIndex();
					if (indx >= 0 && indx < input.size())
						d.setType(input.get(indx));
				}
				if (d.open() == Dialog.OK)
					return d.getValue();
				return null;
			}

		});
		editElement = new EditElement();
		new EditButton<Object>().createEditButtons(bGroup, tableViewer, editElement);
		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		if (prm.getValueClass().isArray() || prm.getValueClass().isAssignableFrom(List.class))
			new ListOrderButtons().createOrderButtons(bGroup, tableViewer);
		return cmp;
	}

	private final class EditElement implements IEditElement<Object> {
		@Override
		public void editElement(List<Object> input, int pos) {
			Object v = input.get(pos);
			if (v == null)
				return;

			ElementDialog dialog = new ElementDialog(table.getShell(), prm);
			dialog.setValue(v);
			if (dialog.open() == Window.OK)
				input.set(pos, dialog.getValue());
		}
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 350;
		gd.widthHint = 300;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);

		tableViewer = new TableViewer(table);
		tableViewer.setLabelProvider(new TLabelProvider());
		tableViewer.setContentProvider(new ListContentProvider());

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100));
		table.setLayout(tlayout);

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Value");
		column.pack();

		tableViewer.setInput(value);

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				StructuredSelection s = (StructuredSelection) tableViewer.getSelection();

				List<Object> inlist = (List<Object>) tableViewer.getInput();
				if (inlist == null) {
					inlist = new ArrayList<Object>();
					tableViewer.setInput(inlist);
				}
				int index = -1;
				if (!s.isEmpty())
					index = inlist.indexOf(s.getFirstElement());
				else
					return;
				editElement.editElement(inlist, index);

				tableViewer.refresh();
				tableViewer.setSelection(s);
				tableViewer.reveal(s.getFirstElement());
			}
		});
	}

}
