/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.prm.dialog;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.ATitledDialog;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.prm.ParameterSet;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.EditButton;
import com.jaspersoft.studio.swt.widgets.table.IEditElement;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.Misc;

public class ParameterSetDialog extends ATitledDialog {
	private int indx = -1;
	private ParameterSet prmSet;
	private Text text;
	private Table table;
	private EditButton<JRDesignParameter> eBtn;

	public ParameterSetDialog(Shell parentShell, Table table) {
		this(parentShell, -1, new ParameterSet(), table);
	}

	public ParameterSetDialog(Shell parentShell, int indx, ParameterSet prmSet, Table table) {
		super(parentShell);
		this.prmSet = prmSet;
		this.indx = indx;
		this.table = table;
		setTitle("Parameter Set");
		setDescription("");
		setDefaultSize(600, 600);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.common_name);

		int style = SWT.BORDER;
		if (prmSet.isBuiltIn())
			style = style | SWT.READ_ONLY;
		text = new Text(composite, style);
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		text.setText(Misc.nvl(prmSet.getName())); //$NON-NLS-1$
		UIUtils.getDisplay().asyncExec(new Runnable() {

			@Override
			public void run() {
				setName();
			}
		});

		if (!prmSet.isBuiltIn())
			text.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					setName();
				}
			});

		createTable(composite);

		applyDialogFont(composite);
		return composite;
	}

	protected void createTable(Composite parent) {
		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		cmp.setLayoutData(gd);

		Label lbl = new Label(cmp, SWT.NONE);
		lbl.setText("Parameters");
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.horizontalSpan = 2;
		lbl.setLayoutData(gd);

		final TableViewer viewer = new TableViewer(cmp, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION
				| SWT.BORDER);

		// create the columns
		// not yet implemented
		createColumns(viewer);

		// make lines and header visible
		final Table table = viewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(prmSet.getParameters());

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				eBtn.push();
			}
		});

		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 5;
		gd.grabExcessHorizontalSpace = true;
		table.setLayoutData(gd);

		cmp = new Composite(cmp, SWT.NONE);
		cmp.setLayout(new GridLayout());
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = 80;
		cmp.setLayoutData(gd);

		new NewButton().createNewButtons(cmp, viewer, new INewElement() {

			@Override
			public Object newElement(List<?> input, int pos) {
				ParameterDialog dialog = new ParameterDialog(UIUtils.getShell(), table);
				if (dialog.open() == Dialog.OK)
					return dialog.getPValue();
				return null;
			}
		});
		new ListOrderButtons().createOrderButtons(cmp, viewer);
		eBtn = new EditButton<JRDesignParameter>();
		eBtn.createEditButtons(cmp, viewer, new IEditElement<JRDesignParameter>() {

			@Override
			public void editElement(List<JRDesignParameter> input, int pos) {
				JRDesignParameter prm = input.get(pos);
				ParameterDialog dialog = new ParameterDialog(UIUtils.getShell(), pos, (JRDesignParameter) prm.clone(), table);
				if (dialog.open() == Dialog.OK)
					input.set(pos, dialog.getPValue());
			}
		});
		new DeleteButton().createDeleteButton(cmp, viewer);
	}

	private void createColumns(TableViewer viewer) {
		TableViewerColumn colFirstName = new TableViewerColumn(viewer, SWT.NONE);
		colFirstName.getColumn().setWidth(200);
		colFirstName.getColumn().setText("Name");
		colFirstName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				JRDesignParameter p = (JRDesignParameter) element;
				return p.getName();
			}
		});

		colFirstName = new TableViewerColumn(viewer, SWT.NONE);
		colFirstName.getColumn().setWidth(200);
		colFirstName.getColumn().setText("Description");
		colFirstName.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				JRDesignParameter p = (JRDesignParameter) element;
				return p.getDescription();
			}
		});
	}

	private void setValidationError(String message) {
		getButton(IDialogConstants.OK_ID).setEnabled(message == null);
		setDescription(message);
	}

	public ParameterSet getPValue() {
		return this.prmSet;
	}

	protected void setName() {
		setValidationError(null);
		String pname = text.getText();
		if (pname.isEmpty()) {
			setValidationError("Name can't be empty");
			return;
		}
		for (int i = 0; i < table.getItemCount(); i++) {
			TableItem ti = table.getItem(i);
			if (ti.getText(0).equals(pname) && i != indx) {
				setValidationError("This name already exists, please select another one");
				return;
			}
		}
		prmSet.setName(pname);
	}

}
