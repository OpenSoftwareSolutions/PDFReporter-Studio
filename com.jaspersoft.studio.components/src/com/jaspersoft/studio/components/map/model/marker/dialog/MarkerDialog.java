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
package com.jaspersoft.studio.components.map.model.marker.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.StandardItem;
import net.sf.jasperreports.components.map.StandardItemProperty;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.jaspersoft.studio.components.Activator;
import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.marker.MarkerCoordinatesType;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.EditButton;
import com.jaspersoft.studio.swt.widgets.table.IEditElement;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;

public class MarkerDialog extends Dialog {

	private MarkerCoordinatesType coordinatesType = MarkerCoordinatesType.LATITUDE_LONGITUDE;

	private final class EditElement implements IEditElement<ItemProperty> {
		@Override
		public void editElement(List<ItemProperty> input, int pos) {
			StandardItemProperty v = (StandardItemProperty) input.get(pos);
			if (v == null)
				return;
			v = (StandardItemProperty) v.clone();
			MarkerPropertyDialog dialog = new MarkerPropertyDialog(Display.getDefault().getActiveShell(), propsDescFile);

			dialog.setValue((StandardItemProperty) v, expContext, isPropertyMandatory(v));
			if (dialog.open() == Window.OK)
				input.set(pos, v);
		}
	}

	private boolean isPropertyMandatory(ItemProperty mprop) {
		return "id".equals(mprop.getName()) || coordinatesType.isMandatoryProperty(mprop.getName()); //$NON-NLS-1$
	}

	private StandardItem value;
	private Table table;
	private TableViewer tableViewer;
	private EditButton<ItemProperty> editButton;
	private String propsDescFile;

	public MarkerDialog(Shell parentShell) {
		this(parentShell, MarkerCoordinatesType.LATITUDE_LONGITUDE,null);
		try {
			this.propsDescFile = Activator.getDefault().getFileLocation("resources/googleMap.properties");
		} catch (Exception e) {
			// do not care
		}
	}

	public MarkerDialog(Shell parentShell, MarkerCoordinatesType coordinatesType, String propsDescFile) {
		super(parentShell);
		this.coordinatesType = coordinatesType;
		this.propsDescFile = propsDescFile;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.window.Window#configureShell(org.eclipse.swt.widgets
	 * .Shell)
	 */
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.MarkerDialog_Title);
	}

	@Override
	protected boolean isResizable() {
		return true;
	}

	@Override
	public boolean close() {
		value.getProperties().clear();
		List<ItemProperty> in = (List<ItemProperty>) tableViewer.getInput();
		value.getProperties().addAll(in);
		return super.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		composite.setLayout(new GridLayout(2, false));

		buildTable(composite);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 500;
		table.setLayoutData(gd);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				StandardItemProperty v = new StandardItemProperty("property", "value", null); //$NON-NLS-1$ //$NON-NLS-2$

				MarkerPropertyDialog dialog = new MarkerPropertyDialog(Display.getDefault().getActiveShell(),propsDescFile);
				dialog.setValue(v, expContext, isPropertyMandatory(v));
				if (dialog.open() == Window.OK)
					return v;
				return null;
			}
		});

		editButton = new EditButton<ItemProperty>();
		editButton.createEditButtons(bGroup, tableViewer, new EditElement());
		new DeleteButton() {
			protected boolean canRemove(Object obj) {
				if (obj instanceof ItemProperty) {
					return !isPropertyMandatory((ItemProperty) obj);
				}
				return super.canRemove(obj);
			};
		}.createDeleteButton(bGroup, tableViewer);
		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);
		// here a table starts
		fillValue(value);
		return composite;
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL);
		table.setHeaderVisible(true);
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
		tableViewer.setLabelProvider(new TMarkerPropertyLabelProvider());

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.MarkerDialog_Name);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.MarkerDialog_ValueOrExpression);

		for (int i = 0, n = column.length; i < n; i++)
			column[i].pack();

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(45, true));
		tlayout.addColumnData(new ColumnWeightData(55, true));
		table.setLayout(tlayout);
	}

	private ExpressionContext expContext;

	public void setValue(StandardItem value, ExpressionContext expContext) {
		this.value = value;
		this.expContext = expContext;
	}

	private void fillValue(StandardItem value) {
		List<ItemProperty> mprops = new ArrayList<ItemProperty>();
		for (ItemProperty smp : value.getProperties()) {
			mprops.add(smp);
		}
		tableViewer.setInput(mprops);
	}

}
