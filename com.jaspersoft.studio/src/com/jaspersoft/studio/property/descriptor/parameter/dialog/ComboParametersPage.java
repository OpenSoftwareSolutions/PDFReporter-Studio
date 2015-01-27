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
package com.jaspersoft.studio.property.descriptor.parameter.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.MDatasetRun;

/**
 * Page used to add a parameter to a dataset run. The parameter can only be choosed between
 * the parameters already defined for dataset referenced by the dataset run. This avoid to have
 * a dataset run parameter valorization of a not existing parameter 
 * 
 * @author Orlandin Marco
 *
 */
public class ComboParametersPage extends WizardPage implements IExpressionContextSetter {
	private final class TLabelProvider extends LabelProvider implements ITableLabelProvider {

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			switch (columnIndex) {
			case 0:
				return ((JRDatasetParameter) element).getName();
			case 1:
				JRDatasetParameter value2 = (JRDatasetParameter) element;
				if (value2 != null && value2.getExpression() != null)
					return value2.getExpression().getText();
			}
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * Parameters of the current dataset run
	 */
	private ParameterDTO value;
	
	/**
	 * Table where the user can select the parameter of the dataset run and their expression
	 */
	private Table table;
	
	/**
	 * Viewer of the table
	 */
	private TableViewer tableViewer;
	
	/**
	 * Actual expression context
	 */
	private ExpressionContext expContext;
	
	/**
	 * Button used to edit a parameter inside the dataset run
	 */
	private Button editButton;
	
	/**
	 * Button used to delete a parameter inside the dataset run
	 */
	private Button deleteButton;
	
	/**
	 * Button used to create a new parameter inside the dataset run
	 */
	private Button addButton;
	
	/**
	 * Parameters inside the original dataset referenced by the dataset run
	 */
	private JRParameter[] datasetParameters;

	/**
	 * Input of the table
	 */
	private List<JRDatasetParameter> input;
	
	/**
	 * Create an instance of the pace
	 * @param pageName
	 */
	protected ComboParametersPage(String pageName) {
		super(pageName);
		setTitle(Messages.ParameterPage_dataset_parameters);
		setDescription(Messages.ParameterPage_description);
	}
	
	/**
	 * Return all the parameters that should be inside the dataset run
	 * 
	 * @return a parametersDTO containing ALL the parameters that should be inside 
	 * the dataset run
	 */
	public ParameterDTO getValue() {
		return value;
	}

	/**
	 * When the dialog is disposed the return value is update
	 */
	@Override
	public void dispose() {
		value = new ParameterDTO();
		value.setJasperDesign(value.getJasperDesign());
		List<JRDatasetParameter> returnValues = new ArrayList<JRDatasetParameter>();
		for(JRDatasetParameter param : input){
			if (param.getName() != null && !param.getName().isEmpty()) returnValues.add(param);
		}
		value.setValue(returnValues.toArray(new JRDatasetParameter[returnValues.size()]));
		super.dispose();
	}

	/**
	 * Set the valued edited by this dialog
	 * 
	 * @param value a ParametersDTO (not null) of the parameters list that the user manipulate  
	 * @param datasetRun the (not null) dataset run from where the value is extracted
	 */
	public void setValue(ParameterDTO value, MDatasetRun datasetRun) {
		this.value = value;
		if (value == null) {
			value = new ParameterDTO();
		}
		//get the dataset referenced by the dataset run
		JRDataset dataset = datasetRun.getJasperDesign().getDatasetMap().get(datasetRun.getPropertyValue(JRDesignDatasetRun.PROPERTY_DATASET_NAME));
		if (dataset != null) {
			List<JRParameter> userParameters = new ArrayList<JRParameter>();
			//flag to add or not the system parameters
			boolean addSystemParameters = true;
			for(JRParameter param : dataset.getParameters()){
				if (!param.isSystemDefined() || addSystemParameters) userParameters.add(param);
			}
			//the original dataset parameters are cached
			datasetParameters = userParameters.toArray(new JRParameter[userParameters.size()]);
		}
		else datasetParameters = new JRDesignParameter[0];
		if (table != null)
			fillTable(table);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);
		setControl(composite);

		buildTable(composite);

		//Create the buttons
		buildButtons(composite);
		
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalAlignment = GridData.FILL;
		gd.grabExcessHorizontalSpace = true;
		gd.verticalAlignment = GridData.FILL;
		gd.grabExcessVerticalSpace = true;
		gd.verticalSpan = 2;
		gd.heightHint = 400;
		gd.widthHint = 600;
		table.setLayoutData(gd);
	}
	
	/**
	 * Create the buttons to add, delete or edit a parameter 
	 * 
	 * @param composite parent where the button will be placed
	 */
	private void buildButtons(Composite composite){
		Composite buttonComposite = new Composite(composite, SWT.NONE);
		buttonComposite.setLayout(new GridLayout(1,false));
		buttonComposite.setLayoutData(new GridData(GridData.FILL_VERTICAL));
		addButton = new Button(buttonComposite, SWT.NONE);
		addButton.setText(Messages.common_add);
		addButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		addButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				CreateParameterDialog paramteterDialog = new CreateParameterDialog(getShell(), createNameComboInput());
				paramteterDialog.setExpressionContext(expContext);
				if (paramteterDialog.open() == Dialog.OK){
					JRDesignDatasetParameter newParam = new JRDesignDatasetParameter();
					newParam.setExpression(paramteterDialog.getSelectedExpression());
					newParam.setName(paramteterDialog.getSelectedParamName());
					input.add(newParam);
					tableViewer.refresh();
					checkButtonState();
				}
			}
		});
		deleteButton = new Button(buttonComposite, SWT.NONE);
		deleteButton.setEnabled(false);
		deleteButton.setText(Messages.common_delete);
		deleteButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		deleteButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if (index != -1){
					List<?> list = (List<?>) tableViewer.getInput();
					list.remove(index);
					tableViewer.refresh();
					checkButtonState();
				}
			}
		});
		
		editButton = new Button(buttonComposite, SWT.NONE);
		editButton.setEnabled(false);
		editButton.setText(Messages.common_edit);
		editButton.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		editButton.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = table.getSelectionIndex();
				if (index != -1){
					List<?> list = (List<?>) tableViewer.getInput();
					JRDesignDatasetParameter selectedItem = (JRDesignDatasetParameter)list.get(index);
					CreateParameterDialog paramteterDialog = new CreateParameterDialog(getShell(), createNameComboInput(), selectedItem);
					paramteterDialog.setExpressionContext(expContext);
					if (paramteterDialog.open() == Dialog.OK){
						selectedItem.setExpression(paramteterDialog.getSelectedExpression());
						selectedItem.setName(paramteterDialog.getSelectedParamName());
						tableViewer.refresh();
						checkButtonState();
					}
				}
			}
		});
	}
	
	/**
	 * Called when a parameter is deleted, added or edited. Disable the 
	 * edit and delete button if nothing inside is selected. This is done
	 * to avoid to do an edit or a delete operation without parameters
	 */
	private void checkButtonState(){
		int index = table.getSelectionIndex();
		if (index != -1){
			editButton.setEnabled(true);
			deleteButton.setEnabled(true);
		} else {
			editButton.setEnabled(false);
			deleteButton.setEnabled(false);
		}
	}
	

	/**
	 * Create the table control and add to it its viewer
	 * @param composite container of the table
	 */
	private void buildTable(Composite composite) {
		table = new Table(composite, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
		table.setToolTipText(""); //$NON-NLS-1$
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		tableViewer = new TableViewer(table);
		attachContentProvider(tableViewer);
		attachLabelProvider(tableViewer);

		TableLayout tlayout = new TableLayout();
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		tlayout.addColumnData(new ColumnWeightData(50, 75, true));
		table.setLayout(tlayout);

		setColumnToolTip();

		TableColumn[] column = new TableColumn[2];
		column[0] = new TableColumn(table, SWT.NONE);
		column[0].setText(Messages.ParameterPage_parameter);

		column[1] = new TableColumn(table, SWT.NONE);
		column[1].setText(Messages.common_expression);

		fillTable(table);
		for (int i = 0, n = column.length; i < n; i++) {
			column[i].pack();
		}
		table.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (e.item instanceof TableItem) {
					setMessage(getDescription(((TableItem) e.item)));
					editButton.setEnabled(true);
					deleteButton.setEnabled(true);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
	}


	/**
	 * Add to the table a content provider that simply convert a list of elements to an array
	 * 
	 * @param viewer the table viewer
	 */
	private void attachContentProvider(TableViewer viewer) {
		viewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				return ((List<?>) inputElement).toArray();
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

			}
		});
	}
	
	/**
	 * Add to the table a label provider, that assuming the table input is a JRDatasetParameter, 
	 * since the table has two columns return for the first one the name of the parameter and 
	 * for the second one its expression
	 * 
	 * @param viewer
	 */
	private void attachLabelProvider(TableViewer viewer) {
		viewer.setLabelProvider(new TLabelProvider());
	}

	
	/**
	 * Return the input of the combo, a list of the parameter name of the original dataset
	 * not already used by other parameters of the dataset run, plus a void element on the top
	 * of the list to remove a parameter from a dataset run
	 * 
	 * @return the list of string displayed in the combo
	 */
	private String[] createNameComboInput(){
		List<String> result = new ArrayList<String>();
		HashSet<String> usedParams = new HashSet<String>();
		if (input != null){
			for(JRDatasetParameter param : input)
				usedParams.add(param.getName());
		}
		for (JRParameter param : datasetParameters){
			if (!usedParams.contains(param.getName())){
					result.add(param.getName());
			}
		}
		Collections.sort(result);
		return result.toArray(new String[result.size()]);
	}

	/**
	 * Set the input  of the table and of the combo viewer. On the table will be available a number of 
	 * rows equals to the number of parameters that the user can select
	 * 
	 * @param table the table 
	 */
	private void fillTable(Table table) {
		List<JRDatasetParameter> lst = new ArrayList<JRDatasetParameter>(Arrays.asList(value.getValue()));
		input = new ArrayList<JRDatasetParameter>();
		for(JRDatasetParameter param : lst){
			JRDesignDatasetParameter newParam = new JRDesignDatasetParameter();
			newParam.setExpression(param.getExpression() != null ? (JRExpression)param.getExpression().clone() : null);
			newParam.setName(param.getName());
			input.add(newParam);
		}

		Collections.sort(input, new Comparator<JRDatasetParameter>() {

			@Override
			public int compare(JRDatasetParameter o1, JRDatasetParameter o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		tableViewer.setInput(input);
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

	private String getDescription(TableItem item) {
		return ""; //$NON-NLS-1$
	}
	
	/**
	 * Set the expression context
	 */
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}
}
