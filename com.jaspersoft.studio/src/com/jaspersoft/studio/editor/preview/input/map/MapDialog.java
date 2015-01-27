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
package com.jaspersoft.studio.editor.preview.input.map;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class MapDialog extends Dialog {

	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			MapItem mi = (MapItem) element;
			switch (columnIndex) {
			case 0:
				return convert(mi.key);
			case 1:
				return convert(mi.value);
			}
			return "";
		}

		private String convert(Object obj) {
			if (obj != null)
				if (obj instanceof Date)
					return new SimpleDateFormat().format(obj);
				else
					return obj.toString();
			return "";
		}
	}

	private IParameter prm;
	private Table table;
	private TableViewer tableViewer;

	private Map<Object, Object> value;
	private Map<Object, Object> oldValue;
	private EditElement editElement;

	@Override
	protected void setReturnCode(int code) {
		super.setReturnCode(code);
		if (code == Dialog.OK) {
			List<MapItem> lst = (List<MapItem>) tableViewer.getInput();
			oldValue.clear();
			for (MapItem it : lst)
				oldValue.put(it.key, it.value);
		}
	}

	public Object getValue() {
		return oldValue;
	}

	public MapDialog(Shell parentShell, Map<Object, Object> value, IParameter prm) {
		super(parentShell);
		this.oldValue = value;
		if (value instanceof Map)
			this.value = new HashMap<Object, Object>((Map<Object, Object>) value);
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
				MapElementDialog d = new MapElementDialog(parent.getShell(), prm);
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
		new EditButton<MapItem>().createEditButtons(bGroup, tableViewer, editElement);
		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		return cmp;
	}

	private final class EditElement implements IEditElement<MapItem> {
		@Override
		public void editElement(List<MapItem> input, int pos) {
			MapItem v = input.get(pos);
			if (v == null)
				return;

			MapElementDialog dialog = new MapElementDialog(table.getShell(), prm);
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

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText("Key");

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText("Value");

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, true));
		tlayout.addColumnData(new ColumnWeightData(50, true));
		table.setLayout(tlayout);

		List<MapItem> lst = new ArrayList<MapItem>();
		for (Object key : value.keySet())
			lst.add(new MapItem(key, value.get(key)));
		tableViewer.setInput(lst);

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

				List<MapItem> inlist = (List<MapItem>) tableViewer.getInput();
				if (inlist == null) {
					inlist = new ArrayList<MapItem>();
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
