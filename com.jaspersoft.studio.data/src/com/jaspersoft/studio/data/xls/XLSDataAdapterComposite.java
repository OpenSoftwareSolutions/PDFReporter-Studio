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
package com.jaspersoft.studio.data.xls;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jasperreports.data.AbstractDataAdapterService;
import net.sf.jasperreports.data.DataAdapter;
import net.sf.jasperreports.data.DataAdapterService;
import net.sf.jasperreports.data.DataAdapterServiceUtil;
import net.sf.jasperreports.data.xls.XlsDataAdapter;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.JasperReportsContext;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;

import com.jaspersoft.studio.data.AFileDataAdapterComposite;
import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.fields.IFieldsProvider;
import com.jaspersoft.studio.data.messages.Messages;
import com.jaspersoft.studio.property.descriptor.pattern.dialog.PatternEditor;
import com.jaspersoft.studio.swt.widgets.table.ListOrderButtons;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class XLSDataAdapterComposite extends AFileDataAdapterComposite {

	private Text textDatePattern;
	private Text textNumberPattern;
	private TableViewer tableViewer;
	private Table table;
	private TableViewerColumn tableViewerColumnName;
	private TableViewerColumn tableViewerColumnIndex;
	private Button btnAdd;
	private Button btnDelete;
	private Button btnCheckUseDatePattern;
	private Button btnCreateDatePattern;
	private Button btnCheckUseNumberPattern;
	private Button btnCreateNumberPattern;
	private Button btnCheckSkipFirstLine;
	private Button btnCheckQEMode;

	// The data model
	private java.util.List<String[]> rows;
	private JasperReportsConfiguration jConfig;

	/**
	 * Create the composite.
	 * 
	 * @param parent
	 * @param style
	 */
	public XLSDataAdapterComposite(Composite parent, int style, JasperReportsContext jrContext) {

		/*
		 * UI ELEMENTS
		 */
		super(parent, style, jrContext);
		setLayout(new GridLayout(1, false));

		// data model init
		rows = new ArrayList<String[]>();

		Composite composite = new Composite(this, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.marginHeight = 0;
		composite.setLayout(gl_composite);
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		createFileNameWidgets(composite);

		btnCheckQEMode = new Button(this, SWT.CHECK);
		btnCheckQEMode.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		btnCheckQEMode.setText(Messages.XLSDataAdapterComposite_2);

		Composite composite_1 = new Composite(this, SWT.NONE);
		composite_1.setLayout(new FillLayout(SWT.VERTICAL));
		composite_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 1, 1));

		Group grpColumnNames = new Group(composite_1, SWT.NONE);
		grpColumnNames.setText(Messages.XLSDataAdapterComposite_3);
		GridLayout gl_grpColumnNames = new GridLayout(1, false);
		grpColumnNames.setLayout(gl_grpColumnNames);

		Button btnGetExcelColumnsName = new Button(grpColumnNames, SWT.NONE);
		btnGetExcelColumnsName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		btnGetExcelColumnsName.setText(Messages.XLSDataAdapterComposite_4);

		Composite composite_3 = new Composite(grpColumnNames, SWT.NONE);
		GridLayout gl_composite_3 = new GridLayout(2, false);
		gl_composite_3.marginWidth = 0;
		gl_composite_3.marginHeight = 0;
		composite_3.setLayout(gl_composite_3);
		composite_3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		tableViewer = new TableViewer(composite_3, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		tableViewer.setContentProvider(new XLSContentProvider());
		tableViewer.setInput(rows);

		table = tableViewer.getTable();
		table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		table.setLinesVisible(true);
		table.setHeaderVisible(true);

		tableViewerColumnName = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnColumnName = tableViewerColumnName.getColumn();
		tblclmnColumnName.setMoveable(true);
		tblclmnColumnName.setWidth(100);
		tblclmnColumnName.setText(Messages.XLSDataAdapterComposite_5);
		tableViewerColumnName.setLabelProvider(new ColumnNameIndexLabelProvider(0));
		tableViewerColumnName.setEditingSupport(new NameIndexEditingSupport(tableViewer, 0));

		tableViewerColumnIndex = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tblclmnColumnIndex = tableViewerColumnIndex.getColumn();
		tblclmnColumnIndex.setMoveable(true);
		tblclmnColumnIndex.setWidth(100);
		tblclmnColumnIndex.setText(Messages.XLSDataAdapterComposite_6);
		tableViewerColumnIndex.setLabelProvider(new ColumnNameIndexLabelProvider(1));
		tableViewerColumnIndex.setEditingSupport(new NameIndexEditingSupport(tableViewer, 1));

		for (int i = 0, n = table.getColumnCount(); i < n; i++) {
			table.getColumn(i).pack();
		}

		Composite composite_4 = new Composite(composite_3, SWT.NONE);
		composite_4.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		GridLayout gl_composite_4 = new GridLayout(1, false);
		gl_composite_4.marginWidth = 0;
		gl_composite_4.marginHeight = 0;
		composite_4.setLayout(gl_composite_4);

		btnAdd = new Button(composite_4, SWT.NONE);
		GridData gd_btnAdd = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnAdd.widthHint = 100;
		btnAdd.setLayoutData(gd_btnAdd);
		btnAdd.setText(Messages.XLSDataAdapterComposite_7);

		btnDelete = new Button(composite_4, SWT.NONE);
		GridData gd_btnDelete = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnDelete.widthHint = 100;
		btnDelete.setLayoutData(gd_btnDelete);
		btnDelete.setText(Messages.XLSDataAdapterComposite_8);
		btnDelete.setEnabled(false);

		new ListOrderButtons().createOrderButtons(composite_4, tableViewer);

		Composite composite_2 = new Composite(this, SWT.NONE);
		composite_2.setLayout(new FillLayout(SWT.HORIZONTAL));
		composite_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

		Group grpOther = new Group(composite_2, SWT.NONE);
		grpOther.setText(Messages.XLSDataAdapterComposite_9);
		GridLayout gl_grpOther = new GridLayout(3, false);
		grpOther.setLayout(gl_grpOther);

		btnCheckUseDatePattern = new Button(grpOther, SWT.CHECK);
		btnCheckUseDatePattern.setBounds(0, 0, 93, 16);
		btnCheckUseDatePattern.setText(Messages.XLSDataAdapterComposite_10);

		textDatePattern = new Text(grpOther, SWT.BORDER);
		textDatePattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textDatePattern.setEnabled(false);

		btnCreateDatePattern = new Button(grpOther, SWT.NONE);
		GridData gd_btnCreateDatePattern = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnCreateDatePattern.widthHint = 100;
		btnCreateDatePattern.setLayoutData(gd_btnCreateDatePattern);
		btnCreateDatePattern.setText(Messages.XLSDataAdapterComposite_11);
		btnCreateDatePattern.setEnabled(false);

		btnCheckUseNumberPattern = new Button(grpOther, SWT.CHECK);
		btnCheckUseNumberPattern.setText(Messages.XLSDataAdapterComposite_12);

		textNumberPattern = new Text(grpOther, SWT.BORDER);
		textNumberPattern.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		textNumberPattern.setEnabled(false);

		btnCreateNumberPattern = new Button(grpOther, SWT.NONE);
		GridData gd_btnCreateNumberPattern = new GridData(SWT.CENTER, SWT.CENTER, false, false, 1, 1);
		gd_btnCreateNumberPattern.widthHint = 100;
		btnCreateNumberPattern.setLayoutData(gd_btnCreateNumberPattern);
		btnCreateNumberPattern.setText(Messages.XLSDataAdapterComposite_13);
		btnCreateNumberPattern.setEnabled(false);

		btnCheckSkipFirstLine = new Button(grpOther, SWT.CHECK);
		btnCheckSkipFirstLine.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 3, 1));
		btnCheckSkipFirstLine.setText(Messages.XLSDataAdapterComposite_14);

		// get Excel file columns
		btnGetExcelColumnsName.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				try {
					getExcelColumns();
				} catch (Exception e1) {
					UIUtils.showError(e1);
				}
			}
		});

		// add an entry and set selection on it
		btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				rows.add(createDataModelEntry());

				// tableViewer.addPostSelectionChangedListener can't cover
				// the first row added, so we need to manually set delete
				// button enabled for this case.
				if (rows.size() == 1) {
					btnDelete.setEnabled(true);
				}

				tableViewer.refresh();
				setTableSelection(-1);
			}
		});

		// delete selected entries and set selection on last table item
		btnDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				removeEntries();
			}
		});

		// keys listener
		table.addKeyListener(new KeyListener() {

			public void keyReleased(KeyEvent e) {
				// nothing
			}

			public void keyPressed(KeyEvent e) {

				if (e.character == SWT.DEL) {
					removeEntries();
				}
			}
		});

		// When no table items,
		// turns disabled the delete button
		// and set unchecked the skip first line button
		tableViewer.addPostSelectionChangedListener(new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				if (rows.size() <= 0) {
					btnDelete.setEnabled(false);
					btnCheckSkipFirstLine.setSelection(false);
				} else {
					btnDelete.setEnabled(true);
				}
			}
		});

		btnCheckUseDatePattern.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean bool = ((Button) e.widget).getSelection();
				if (!bool) {
					textDatePattern.setText(new SimpleDateFormat().toPattern());
				}
				textDatePattern.setEnabled(bool);
				btnCreateDatePattern.setEnabled(bool);
			}
		});

		btnCreateDatePattern.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				PatternEditor wizard = new PatternEditor();
				wizard.setNumberPatterns(false);
				wizard.setValue(textDatePattern.getText());
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				dialog.create();

				if (dialog.open() == Dialog.OK) {
					String val = wizard.getValue();
					textDatePattern.setText(val);
				}
			}
		});

		btnCheckUseNumberPattern.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				boolean bool = ((Button) e.widget).getSelection();
				if (!bool) {
					textNumberPattern.setText(new DecimalFormat().toPattern());
				}
				textNumberPattern.setEnabled(bool);
				btnCreateNumberPattern.setEnabled(bool);
			}
		});

		btnCreateNumberPattern.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				PatternEditor wizard = new PatternEditor();
				wizard.setDatePatterns(false);
				wizard.setValue(textNumberPattern.getText());
				WizardDialog dialog = new WizardDialog(getShell(), wizard);
				dialog.create();

				if (dialog.open() == Dialog.OK) {
					String val = wizard.getValue();
					textNumberPattern.setText(val);
				}
			}
		});
	}

	@Override
	protected void bindWidgets(DataAdapter dataAdapter) {
		XlsDataAdapter xlsDataAdapter = (XlsDataAdapter) dataAdapter;

		doBindFileNameWidget(xlsDataAdapter);
		bindingContext.bindValue(SWTObservables.observeSelection(btnCheckQEMode), PojoObservables.observeValue(dataAdapter, "queryExecuterMode")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeSelection(btnCheckSkipFirstLine), PojoObservables.observeValue(dataAdapter, "useFirstRowAsHeader")); //$NON-NLS-1$

		bindingContext.bindValue(SWTObservables.observeText(textDatePattern, SWT.Modify), PojoObservables.observeValue(dataAdapter, "datePattern")); //$NON-NLS-1$
		bindingContext.bindValue(SWTObservables.observeText(textNumberPattern, SWT.Modify), PojoObservables.observeValue(dataAdapter, "numberPattern")); //$NON-NLS-1$

		List<String> listColumnNames = xlsDataAdapter.getColumnNames();
		List<Integer> listColumnIndexes = xlsDataAdapter.getColumnIndexes();
		if ((listColumnNames != null && listColumnNames.size() > 0) && (listColumnIndexes != null && listColumnIndexes.size() > 0) && (listColumnNames.size() == listColumnIndexes.size())) {

			for (int i = 0; i < listColumnNames.size(); i++) {
				rows.add(new String[] { listColumnNames.get(i), listColumnIndexes.get(i).toString() });
			}

			tableViewer.refresh();
			setTableSelection(-1);
			btnDelete.setEnabled(true);
		}

		String customDatePattern = xlsDataAdapter.getDatePattern();
		if (customDatePattern != null && customDatePattern.length() > 0) {
			btnCheckUseDatePattern.setSelection(true);
			textDatePattern.setText(customDatePattern);
			textDatePattern.setEnabled(true);
			btnCreateDatePattern.setEnabled(true);
		} else {
			textDatePattern.setText(new SimpleDateFormat().toPattern());
		}

		String customNumberPattern = xlsDataAdapter.getNumberPattern();
		if (customNumberPattern != null && customNumberPattern.length() > 0) {
			btnCheckUseNumberPattern.setSelection(true);
			textNumberPattern.setText(customNumberPattern);
			textNumberPattern.setEnabled(true);
			btnCreateNumberPattern.setEnabled(true);
		} else {
			textNumberPattern.setText(new DecimalFormat().toPattern());
		}
	}

	/**
	 * Get the XLS DataAdapter with the values from the UI elements.
	 * 
	 * @return
	 */
	public DataAdapterDescriptor getDataAdapter() {
		if (dataAdapterDesc == null)
			dataAdapterDesc = new XLSDataAdapterDescriptor();

		XlsDataAdapter xlsDataAdapter = (XlsDataAdapter) dataAdapterDesc.getDataAdapter();

		xlsDataAdapter.setFileName(textFileName.getText());
		xlsDataAdapter.setQueryExecuterMode(btnCheckQEMode.getSelection());

		List<String> listColumnNames = new ArrayList<String>();
		List<Integer> listColumnIndexes = new ArrayList<Integer>();
		for (String[] row : rows) {
			listColumnNames.add(row[0]);
			listColumnIndexes.add(Integer.valueOf(row[1]));
		}

		xlsDataAdapter.setColumnNames(listColumnNames);
		xlsDataAdapter.setColumnIndexes(listColumnIndexes);

		xlsDataAdapter.setDatePattern(textDatePattern.getText());
		xlsDataAdapter.setNumberPattern(textNumberPattern.getText());
		xlsDataAdapter.setUseFirstRowAsHeader(btnCheckSkipFirstLine.getSelection());

		return dataAdapterDesc;
	}

	/**
	 * Content provider for XLSDataAdapterComposite TableViewer
	 * 
	 * @author czhu
	 * 
	 */
	private class XLSContentProvider implements IStructuredContentProvider {

		public void dispose() {
			// nothing
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
			// nothing
		}

		public Object[] getElements(Object inputElement) {
			if (inputElement != null && inputElement instanceof List)
				return ((List<?>) inputElement).toArray();
			return new Object[0];
		}
	}

	/**
	 * Extended EditingSupport
	 * 
	 * @author czhu
	 * 
	 */
	private class NameIndexEditingSupport extends EditingSupport {

		private final TableViewer viewer;
		private int columnIndex;

		public NameIndexEditingSupport(TableViewer viewer, int columnIndex) {
			super(viewer);
			this.viewer = viewer;
			this.columnIndex = columnIndex;
		}

		@Override
		protected CellEditor getCellEditor(Object element) {
			return new TextCellEditor(viewer.getTable());
		}

		@Override
		protected boolean canEdit(Object element) {
			return true;
		}

		@Override
		protected Object getValue(Object element) {
			return ((String[]) element)[columnIndex].toString();
		}

		@Override
		protected void setValue(Object element, Object value) {
			((String[]) element)[columnIndex] = (String.valueOf(value));
			viewer.refresh();
		}
	}

	/**
	 * Extended ColumnLabelProvider
	 * 
	 * @author czhu
	 * 
	 */
	private class ColumnNameIndexLabelProvider extends ColumnLabelProvider {

		private int columnIndex;

		private ColumnNameIndexLabelProvider(int columnIndex) {
			this.columnIndex = columnIndex;
		}

		@Override
		public String getText(Object element) {
			String[] row = (String[]) element;
			if (columnIndex == 0) { // 0 => Name column
				return row[columnIndex].toString();
			} else { // 1 => Index column
				String excelCellLabel = excelCellLabelRenderer(Integer.valueOf(row[columnIndex].toString()));
				return row[columnIndex] + Messages.XLSDataAdapterComposite_22 + excelCellLabel + Messages.XLSDataAdapterComposite_23;
			}
		}
	}

	/**
	 * Return the Excel cell label for a given index. i.e: <br>
	 * index 0 => Excel case A <br>
	 * index 25 => Excel case Z <br>
	 * index 26 => Excel case AA <br>
	 * index 51 => Excel case AZ <br>
	 * index 52 => Excel case BA...
	 * 
	 * @param Integer
	 *          index
	 * @return String the Excel cell label
	 */
	private String excelCellLabelRenderer(Integer index) {

		String digits = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"; //$NON-NLS-1$

		if (index != null && index instanceof Integer) {
			int val = ((Integer) index).intValue();

			String number = "" + digits.charAt(val % 26); //$NON-NLS-1$
			while (val > 0) {
				val = val / 26;
				int i = (val % 26) - 1;

				if (val == 0)
					break;
				if (val % 26 == 0) {
					// We have to remove 26 from val and print a Z...
					val -= 26;
					i = 25;
				}
				number = digits.charAt(i) + number;
			}

			return number;
		}

		return null;
	}

	/**
	 * This creates and returns a new entry for the data model
	 * 
	 * @return String[]{Name, Value}
	 */
	private String[] createDataModelEntry() {

		int i = 0;
		String column = "COLUMN_" + i; //$NON-NLS-1$

		while (!isColumnValid(column)) {
			i++;
			column = "COLUMN_" + i; //$NON-NLS-1$
		}

		return new String[] { column, String.valueOf(i) };
	}

	/**
	 * This set selection to the table's item represented by the given index. Any
	 * index out of table's range will select the last item.
	 * 
	 * @param index
	 */
	private void setTableSelection(int index) {

		if (rows != null && rows.size() > 0) {

			if (index == 0) {
				table.setSelection(index);
			} else if ((0 < index) && (index < rows.size() - 1)) {
				table.setSelection(index - 1);
			} else {
				table.setSelection(rows.size() - 1);
			}
		}
	}

	/**
	 * This method will populate the data model with the Excel columns This also
	 * checks the button "Skip the first line " and enables the delete button
	 * 
	 * @throws Exception
	 */
	private void getExcelColumns() throws Exception {

		if (textFileName.getText().length() > 0) {
			DataAdapterDescriptor da = getDataAdapter();
			if (jConfig == null)
				jConfig = JasperReportsConfiguration.getDefaultJRConfig();
			DataAdapterService das = DataAdapterServiceUtil.getInstance(jConfig).getService(da.getDataAdapter());
			 ((AbstractDataAdapterService) das).getDataAdapter();
			jConfig.setJasperDesign(new JasperDesign());
			
			//The get fields method call once a next on the data adapter to get the first line and from that is read the
			//fields name. But is useFirstRowAsHeader flag is set to false than the next call will skip the first line
			//that is the only one read to get the fields, so it will return an empty set of column names. For this 
			//reason this flag must be force to true if the data adapter is used to get the column names
			XlsDataAdapter xlsAdapter = (XlsDataAdapter)da.getDataAdapter();
			boolean useRowHeader = xlsAdapter.isUseFirstRowAsHeader();
			xlsAdapter.setUseFirstRowAsHeader(true);
			List<JRDesignField> fields = ((IFieldsProvider) da).getFields(das, jConfig, new JRDesignDataset(jConfig, false));
			xlsAdapter.setUseFirstRowAsHeader(useRowHeader);
			
			rows.clear();
			int columnIndex = 0;
			for (JRDesignField f : fields) {
				rows.add(new String[] { f.getName(), String.valueOf(columnIndex++) });
			}
			tableViewer.setInput(rows);

			tableViewer.refresh();
			setTableSelection(-1);
			btnDelete.setEnabled(true);
		}
	}


	@Override
	public void dispose() {
		if (jConfig != null)
			jConfig.dispose();
		super.dispose();
	}

	/**
	 * Removes selected entries from the data model
	 */
	private void removeEntries() {

		int[] indices = table.getSelectionIndices();

		if (indices.length > 0) {

			Arrays.sort(indices);
			int removedItems = 0;

			for (int i : indices) {
				// To prevent an IndexOutOfBoundsException
				// we need to subtract number of removed items
				// from the removed item index.
				rows.remove(i - removedItems);
				removedItems++;
			}
			tableViewer.refresh();
			setTableSelection(indices[0]);
		}
	}

	/**
	 * Check the validity of the column name. It is valid only if it is not null,
	 * not empty and not already existed.
	 * 
	 * @param string
	 * @return true or false
	 */
	private boolean isColumnValid(String column) {

		if (column == null || "".equals(column)) //$NON-NLS-1$
			return false;

		for (String[] row : rows) {
			if (row[0].equals(column)) {
				return false;
			}
		}

		return true;
	}

	@Override
	public String getHelpContextId() {
		return PREFIX.concat("adapter_excel");
	}

	@Override
	protected String[] getFileExtensions() {
		return new String[] { "*.xls", "*.*" };
	}
}
