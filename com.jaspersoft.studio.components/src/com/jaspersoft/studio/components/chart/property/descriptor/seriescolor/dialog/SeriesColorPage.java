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
package com.jaspersoft.studio.components.chart.property.descriptor.seriescolor.dialog;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.SortedSet;

import net.sf.jasperreports.engine.base.JRBaseChartPlot.JRBaseSeriesColor;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.components.chart.messages.Messages;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorCellEditor;
import com.jaspersoft.studio.property.descriptor.color.ColorLabelProvider;
import com.jaspersoft.studio.swt.widgets.table.DeleteButton;
import com.jaspersoft.studio.swt.widgets.table.INewElement;
import com.jaspersoft.studio.swt.widgets.table.ListContentProvider;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.swt.widgets.table.NewButton;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;

public class SeriesColorPage extends WizardPage {
	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {
		ColorLabelProvider clb = new ColorLabelProvider(NullEnum.NOTNULL);

		public Image getColumnImage(Object element, int columnIndex) {
			SeriesColorDTO dto = (SeriesColorDTO) element;
			switch (columnIndex) {
			case 0:
				return clb.getImage(Colors.getSWTRGB4AWTGBColor(dto.getValue()));
			}
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			SeriesColorDTO dto = (SeriesColorDTO) element;
			switch (columnIndex) {
			case 0:
				return clb.getText(Colors.getSWTRGB4AWTGBColor(dto.getValue()));
			}
			return ""; //$NON-NLS-1$
		}
	}

	private List<SeriesColorDTO> value;
	private Table table;
	private TableViewer tableViewer;

	public Collection<JRBaseSeriesColor> getValue() {
		List<JRBaseSeriesColor> lst = new ArrayList<JRBaseSeriesColor>();
		int i = 0;
		for (SeriesColorDTO p : value)
			lst.add(new JRBaseSeriesColor(i++, p.getValue()));
		return lst;
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	public void setValue(SortedSet<JRBaseSeriesColor> val) {
		value = new ArrayList<SeriesColorDTO>();
		if (val != null) {
			for (JRBaseSeriesColor bs : val)
				value.add(new SeriesColorDTO(bs.getColor()));
		}
		if (table != null)
			fillTable(table);
	}

	protected SeriesColorPage(String pageName) {
		super(pageName);
		setTitle(Messages.common_series_colors);
		setDescription(Messages.SeriesColorPage_description);

	}

	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setBackground(parent.getBackground());
		composite.setLayout(new GridLayout(2, false));
		setControl(composite);

		buildTable(composite);

		Composite bGroup = new Composite(composite, SWT.NONE);
		bGroup.setLayout(new GridLayout(1, false));
		bGroup.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		bGroup.setBackground(composite.getBackground());

		new NewButton().createNewButtons(bGroup, tableViewer, new INewElement() {

			public Object newElement(List<?> input, int pos) {
				SeriesColorDTO jrm = new SeriesColorDTO(new Color((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
				return jrm;
			}

		});

		new DeleteButton().createDeleteButton(bGroup, tableViewer);

		new ListOrderButtons().createOrderButtons(bGroup, tableViewer);

		fillTable(table);
	}

	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 200;
		gd.widthHint = 580;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		attachContentProvider(tableViewer);
		attachLabelProvider(tableViewer);
		attachCellEditors(tableViewer, table);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(100));
		table.setLayout(tlayout);

		TableColumn[] column = new TableColumn[1];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText("Color");

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
				if (property.equals("COLOR")) //$NON-NLS-1$
					return true;
				return false;
			}

			public Object getValue(Object element, String property) {
				SeriesColorDTO mi = (SeriesColorDTO) element;
				if (property.equals("COLOR"))//$NON-NLS-1$
					return Colors.getSWTRGB4AWTGBColor(mi.getValue());
				return null;
			}

			public void modify(Object element, String property, Object value) {
				TableItem ti = (TableItem) element;
				SeriesColorDTO mi = (SeriesColorDTO) ti.getData();

				if (property.equals("COLOR")) {//$NON-NLS-1$
					mi.setValue(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
				}
				tableViewer.update(element, new String[] { property });
				tableViewer.refresh();
			}
		});

		viewer.setCellEditors(new CellEditor[] { new ColorCellEditor(parent) });
		viewer.setColumnProperties(new String[] { "COLOR" }); //$NON-NLS-1$  
	}

	private void fillTable(Table table) {
		tableViewer.setInput(value);
		if (!value.isEmpty())
			table.select(0);
	}
}
