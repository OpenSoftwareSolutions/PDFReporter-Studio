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
package com.jaspersoft.studio.property.descriptor.propexpr.dialog;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.design.JRDesignPropertyExpression;

import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.help.TableHelpListener;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.properties.dialog.PropertiesList;
import com.jaspersoft.studio.property.descriptor.properties.dialog.PropertyDTO;
import com.jaspersoft.studio.property.descriptor.properties.dialog.TPropertyLabelProvider;
import com.jaspersoft.studio.property.descriptor.propexpr.PropertyExpressionsDTO;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.EditButton;
import com.jaspersoft.studio.swt.widgets.table.IEditElement;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.wizards.ContextHelpIDs;
import com.jaspersoft.studio.wizards.JSSHelpWizardPage;

public class JRPropertyExpressionPage extends JSSHelpWizardPage {
	private final class EditElement implements IEditElement<PropertyDTO> {
		@Override
		public void editElement(List<PropertyDTO> input, int pos) {
			PropertyDTO v = (PropertyDTO) input.get(pos);
			if (v == null)
				return;
			try {
				v = (PropertyDTO) v.clone();
				JRPropertyExpressionDialog dialog = new JRPropertyExpressionDialog(Display.getDefault().getActiveShell());

				dialog.setValue(v);
				if (dialog.open() == Window.OK)
					input.set(pos, v);
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}
	}

	private PropertyExpressionsDTO value;
	private Table table;
	private TableViewer tableViewer;
	private List<PropertyDTO> defaultProperties;
	private EditButton<PropertyDTO> editButton;

	public PropertyExpressionsDTO getValue() {
		return new PropertyExpressionsDTO(value.getPropExpressions(), value.getPropMap(), value.getPnode());
	}

	@Override
	public void dispose() {
		// clear all properties
		List<PropertyDTO> props = (List<PropertyDTO>) tableViewer.getInput();
		List<JRPropertyExpression> pexpr = new ArrayList<JRPropertyExpression>();
		for (String str : value.getPropMap().getPropertyNames())
			value.getPropMap().removeProperty(str);
		value.setPropExpressions(null);
		for (PropertyDTO p : props) {
			if (p.getValue() instanceof JRExpression) {
				JRDesignPropertyExpression jrpexp = new JRDesignPropertyExpression();
				jrpexp.setName(p.getProperty());
				jrpexp.setValueExpression((JRExpression) p.getValue());
				pexpr.add(jrpexp);
			} else if (p.getValue() instanceof String) {
				value.getPropMap().setProperty(p.getProperty(), (String) p.getValue());
			}
		}
		if (pexpr != null && !pexpr.isEmpty())
			value.setPropExpressions(pexpr.toArray(new JRPropertyExpression[pexpr.size()]));
		super.dispose();
	}

	public void setValue(PropertyExpressionsDTO value) {
		this.value = value;
		if (table != null)
			fillTable(table);
	}

	protected JRPropertyExpressionPage(String pageName) {
		super(pageName);
		setTitle(Messages.common_properties);
		setDescription(Messages.JRPropertyPage_description);
	}
	
	/**
	 * Return the context name for the help of this page
	 */
	@Override
	protected String getContextName() {
		return ContextHelpIDs.WIZARD_PROPERTIES;
	}

	public void createControl(final Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		setControl(composite);

		buildTable(composite);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 400;
		table.setLayoutData(gd);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				int i = 1;
				String name = "newproperty"; //$NON-NLS-1$
				while (getName(input, name, i) == null)
					i++;
				name += "_" + i; //$NON-NLS-1$
				PropertyDTO v = new PropertyDTO(name, "NEW_VALUE");
				v.setPnode(value.getPnode());
				JRPropertyExpressionDialog dialog = new JRPropertyExpressionDialog(Display.getDefault().getActiveShell());
				dialog.setValue(v);
				if (dialog.open() == Window.OK)
					return v;
				return null;
			}

			private String getName(List<?> input, String name, int i) {
				name += "_" + i;
				for (Object dto : input) {
					PropertyDTO prm = (PropertyDTO) dto;
					if (prm.getProperty() != null && prm.getProperty().trim().equals(name)) {
						return null;
					}
				}
				return name;
			}
		});

		editButton = new EditButton<PropertyDTO>();
		editButton.createEditButtons(bGroup, tableViewer, new EditElement());
		new DeleteButton().createDeleteButton(bGroup, tableViewer);
		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);
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
		//set the help for the elements inside the table
		TableHelpListener.setTableHelp(table);
		
		tableViewer = new TableViewer(table);
		tableViewer.setContentProvider(new ListContentProvider());
		tableViewer.setLabelProvider(new TPropertyLabelProvider());
		// attachCellEditors(tableViewer, table);

		setColumnToolTip();

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.common_name);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.JRPropertyPage_value);

		fillTable(table);
		for (int i = 0, n = column.length; i < n; i++) {
			column[i].pack();
		}

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, true));
		tlayout.addColumnData(new ColumnWeightData(50, true));
		table.setLayout(tlayout);

		table.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TableItem)
					setMessage(getDescription(((TableItem) e.item)));
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}

	private void fillTable(Table table) {
		List<PropertyDTO> props = new ArrayList<PropertyDTO>();
		if (value.getPropExpressions() != null)
			for (JRPropertyExpression pe : value.getPropExpressions())
				if (pe != null) {
					PropertyDTO dto = new PropertyDTO(pe.getName(), pe.getValueExpression());
					dto.setPnode(value.getPnode());
					props.add(dto);
				}
		JRPropertiesMap pmap = value.getPropMap();
		if (pmap != null)
			for (String pe : pmap.getPropertyNames()) {
				PropertyDTO dto = new PropertyDTO(pe, pmap.getProperty(pe));
				dto.setPnode(value.getPnode());
				props.add(dto);
			}
		tableViewer.setInput(props);
	}

	private void setColumnToolTip() {
		final Listener labelListener = new Listener() {
			public void handleEvent(Event event) {
				Label label = (Label) event.widget;
				Shell shell = label.getShell();
				switch (event.type) {
				case SWT.MouseDown:
					Event e = new Event();
					e.item = (TableItem) label.getData("_TABLEITEM"); //$NON-NLS-1$
					// Assuming table is single select, set the selection as if
					// the mouse down event went through to the table
					table.setSelection(new TableItem[] { (TableItem) e.item });
					table.notifyListeners(SWT.Selection, e);
					// fall through
				case SWT.MouseExit:
					shell.dispose();
					break;
				}
			}
		};

		Listener tableListener = new Listener() {
			Shell tip = null;

			Label label = null;

			public void handleEvent(Event event) {
				switch (event.type) {
				case SWT.Dispose:
				case SWT.KeyDown:
				case SWT.MouseMove: {
					if (tip == null)
						break;
					tip.dispose();
					tip = null;
					label = null;
					break;
				}
				case SWT.MouseHover: {
					TableItem item = table.getItem(new Point(event.x, event.y));
					String description = getDescription(item);
					if (item != null && !description.equals("")) { //$NON-NLS-1$

						if (tip != null && !tip.isDisposed())
							tip.dispose();
						tip = new Shell(table.getShell(), SWT.ON_TOP | SWT.TOOL);
						tip.setLayout(new FillLayout());
						label = new Label(tip, SWT.NONE);
						label.setForeground(table.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_FOREGROUND));
						label.setBackground(table.getShell().getDisplay().getSystemColor(SWT.COLOR_INFO_BACKGROUND));
						label.setData("_TABLEITEM", item); //$NON-NLS-1$

						label.setText(description);
						label.addListener(SWT.MouseExit, labelListener);
						label.addListener(SWT.MouseDown, labelListener);
						Point size = tip.computeSize(SWT.DEFAULT, SWT.DEFAULT);
						Rectangle rect = item.getBounds(0);
						Point pt = table.toDisplay(rect.x, rect.y);
						tip.setBounds(pt.x, pt.y, size.x, size.y);
						tip.setVisible(true);
					}
				}
				}
			}
		};
		table.addListener(SWT.Dispose, tableListener);
		table.addListener(SWT.KeyDown, tableListener);
		table.addListener(SWT.MouseMove, tableListener);
		table.addListener(SWT.MouseHover, tableListener);
	}

	private String[] getDefaultPropertyItems() {
		defaultProperties = getDefaultProperties();
		String[] strnames = new String[defaultProperties.size()];
		for (int i = 0; i < strnames.length; i++)
			strnames[i] = defaultProperties.get(i).getProperty();
		return strnames;
	}

	private List<PropertyDTO> getDefaultProperties() {
		if (defaultProperties == null) {
			defaultProperties = PropertiesList.getJRProperties();
		}
		return defaultProperties;
	}

	private String getDescription(TableItem item) {
		if (item != null && item.getData() != null) {
			String key = ((PropertyDTO) item.getData()).getProperty();
			List<PropertyDTO> dp = getDefaultProperties();
			for (PropertyDTO p : dp) {
				if (p.getProperty().equals(key))
					return p.getDescription();
			}
		}
		return getDescription(); //$NON-NLS-1$
	}
}
