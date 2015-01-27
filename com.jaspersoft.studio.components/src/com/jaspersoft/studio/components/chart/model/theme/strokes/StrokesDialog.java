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
package com.jaspersoft.studio.components.chart.model.theme.strokes;

import java.awt.BasicStroke;
import java.awt.Stroke;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.studio.components.chart.model.theme.stroke.StrokeDialog;
import com.jaspersoft.studio.components.chart.model.theme.stroke.StrokeLabelProvider;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.EditButton;
import com.jaspersoft.studio.swt.widgets.table.IEditElement;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class StrokesDialog extends Dialog {
	private Table table;
	private TableViewer tableViewer;
	private List<Stroke> value;
	private EditElement editElement;
	private EditButton<Object> editButton;

	protected StrokesDialog(Shell parentShell) {
		super(parentShell);
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText("Strokes");
	}

	public List<Stroke> getValue() {
		return value;
	}

	@Override
	protected void setReturnCode(int code) {
		super.setReturnCode(code);
		if (code == Dialog.OK)
			value = (List<Stroke>) tableViewer.getInput();
	}

	public void setValue(List<Stroke> value) {
		this.value = value;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		final Composite cmp = (Composite) super.createDialogArea(parent);
		cmp.setLayout(new GridLayout(2, false));

		buildTable(cmp);

		Composite bGroup = new Composite(cmp, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				StrokeDialog d = new StrokeDialog(cmp.getShell());
				d.setValue(new BasicStroke());
				if (d.open() == Dialog.OK)
					return d.getValue();
				return null;
			}

		});
		editElement = new EditElement();
		editButton = new EditButton<Object>();
		editButton.createEditButtons(bGroup, tableViewer, editElement);
		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);

		tableViewer.setInput(value);
		return cmp;
	}

	private void buildTable(Composite parent) {
		table = new Table(parent, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setHeaderVisible(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		gd.widthHint = 300;
		table.setLayoutData(gd);
		table.addMouseListener(new MouseListener() {
			@Override
			public void mouseUp(MouseEvent e) {
			}

			@Override
			public void mouseDown(MouseEvent e) {
			}

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				editButton.push();
			}
		});
		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new StrokeLabelProvider());

		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText("Stroke");
		column.pack();
	}

	private final class EditElement implements IEditElement<Object> {
		@Override
		public void editElement(List<Object> input, int pos) {
			Object v = input.get(pos);
			if (v == null)
				return;

			StrokeDialog dialog = new StrokeDialog(table.getShell());
			dialog.setValue((Stroke) v);
			if (dialog.open() == Window.OK)
				input.set(pos, dialog.getValue());
		}
	}
}
