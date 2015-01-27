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
package com.jaspersoft.studio.jface.dialogs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.JRDataset;
import net.sf.jasperreports.engine.JRDatasetParameter;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignDatasetParameter;
import net.sf.jasperreports.engine.design.JRDesignExpression;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Table;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.IEditableDatasetRun;
import com.jaspersoft.studio.property.dataset.DatasetRunSelectionListener;
import com.jaspersoft.studio.swt.widgets.WTextExpression;
import com.jaspersoft.studio.utils.ModelUtils;

/** 
 * This generic composite can be reused in dialogs/wizards when there is the need to edit the dataset run information of
 * a report element.<br>
 * 
 * @author mrabbi
 * 
 * @see IEditableDatasetRun
 * 
 */
public class DatasetRunBaseComposite extends Composite implements IExpressionContextSetter{
	
	private IEditableDatasetRun datasetRunInstance;
	private Combo comboSubDataset;
	private TabFolder tabFolderDataSetRun;
	private Combo comboConnDS;
	private WTextExpression connDSExpression;
	private WTextExpression paramsMapExpression;
	private TableViewer tableViewerDatasetRunParams;
	private List<DatasetRunSelectionListener> dsRunSelectionListeners;
	private ExpressionContext expContext;

	public DatasetRunBaseComposite(IEditableDatasetRun datasetRun, Composite parent, int style) {
		super(parent, style);
		this.dsRunSelectionListeners=new ArrayList<DatasetRunSelectionListener>();
		this.setLayout(new GridLayout(2,false));
		this.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2));
		this.datasetRunInstance=datasetRun;
		
		Label lblSubDataset = new Label(this, SWT.NONE);
		lblSubDataset.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		lblSubDataset.setText(Messages.DatasetRunBaseComposite_SubDatasetLbl);
		
		comboSubDataset = new Combo(this, SWT.NONE | SWT.READ_ONLY);
		GridData gd_combo = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
		gd_combo.horizontalIndent = 15;
		comboSubDataset.setLayoutData(gd_combo);
		comboSubDataset.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				int selIndex=((Combo)e.widget).getSelectionIndex();
				updateSubDatasetInformation(selIndex);
				// On dataset selection change we force the initial 
				// selection of the No connection / datasource expression
				comboConnDS.select(0);
				connDSExpression.setEnabled(false);
				connDSExpression.setVisible(false);
				connDSExpression.setExpression(null);
				notifyDatasetRunSelectionChanged();
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		tabFolderDataSetRun = new TabFolder(this, SWT.NONE);
		GridData gd_tabFolderDataSetRun = new GridData(SWT.FILL, SWT.FILL, true, true, 2, 2);
		tabFolderDataSetRun.setLayoutData(gd_tabFolderDataSetRun);
		tabFolderDataSetRun.setEnabled(false);
		tabFolderDataSetRun.setVisible(false);

		
		TabItem tbtmConnectionDatasourceExpression = new TabItem(tabFolderDataSetRun, SWT.NONE);
		tbtmConnectionDatasourceExpression.setText(Messages.DatasetRunBaseComposite_ConnDSExprTab);
		
		Composite compositeConnDSExpContent = new Composite(tabFolderDataSetRun, SWT.NONE);
		tbtmConnectionDatasourceExpression.setControl(compositeConnDSExpContent);
		compositeConnDSExpContent.setLayout(new GridLayout(1, false));
		
		Label lblConnDSExprSelection = new Label(compositeConnDSExpContent, SWT.NONE);
		lblConnDSExprSelection.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		lblConnDSExprSelection.setText(Messages.DatasetRunBaseComposite_ConnDSExprLbl);
		
		comboConnDS = new Combo(compositeConnDSExpContent, SWT.NONE | SWT.READ_ONLY);
		comboConnDS.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		comboConnDS.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				int connectionType=((Combo)e.widget).getSelectionIndex();
				
				switch (connectionType) {
				case 0:
					connDSExpression.setEnabled(false);
					connDSExpression.setVisible(false);
					connDSExpression.setExpression(null);
					break;
				case 1:
					connDSExpression.setEnabled(true);
					connDSExpression.setVisible(true);
					connDSExpression.setExpression(new JRDesignExpression("$P{REPORT_CONNECTION}"));			 //$NON-NLS-1$
					break;
				case 2:
					connDSExpression.setEnabled(true);
					connDSExpression.setVisible(true);
					connDSExpression.setExpression(new JRDesignExpression("new net.sf.jasperreports.engine.JREmptyDataSource(1)")); //$NON-NLS-1$
					break;
				}
			}
			
			public void widgetDefaultSelected(SelectionEvent e) {
				
			}
		});
		
		connDSExpression = new WTextExpression(compositeConnDSExpContent, SWT.NONE, Messages.DatasetRunBaseComposite_ConnDSExprWidgetLbl, WTextExpression.LABEL_ON_LEFT){
			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				int connectionType=comboConnDS.getSelectionIndex();		
				switch (connectionType) {
					case 0:
						datasetRunInstance.setConnectionExpression(null);
						datasetRunInstance.setDataSourceExpression(null);
						break;
					case 1:
			            datasetRunInstance.setDataSourceExpression(null);
			            datasetRunInstance.setConnectionExpression(exp);			
						break;
					case 2:
						datasetRunInstance.setConnectionExpression(null);
						datasetRunInstance.setDataSourceExpression(exp);
						break;
				}
			}
		};
		connDSExpression.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,1));
		
		TabItem tbtmParameters = new TabItem(tabFolderDataSetRun, SWT.NONE);
		tbtmParameters.setText(Messages.DatasetRunBaseComposite_ParametersTab);

		Composite compositeParametersContent = new Composite(tabFolderDataSetRun, SWT.NONE);
		tbtmParameters.setControl(compositeParametersContent);
		compositeParametersContent.setLayout(new GridLayout(2, false));
		
		Composite compositeTableViewerDSRunParams=new Composite(compositeParametersContent, SWT.NONE);
		compositeTableViewerDSRunParams.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
		TableColumnLayout layoutForTableViewerDSRunParams = new TableColumnLayout();
		compositeTableViewerDSRunParams.setLayout(layoutForTableViewerDSRunParams);
		
		tableViewerDatasetRunParams = new TableViewer(compositeTableViewerDSRunParams, SWT.BORDER | SWT.FULL_SELECTION);
		Table tableDatasetRunParameters = tableViewerDatasetRunParams.getTable();
		tableDatasetRunParameters.setHeaderVisible(true);
		tableDatasetRunParameters.setLinesVisible(true);
		
		TableViewerColumn tblclmnDatasetRunParam = new TableViewerColumn(tableViewerDatasetRunParams, SWT.NONE);
		tblclmnDatasetRunParam.getColumn().setWidth(156);
		tblclmnDatasetRunParam.getColumn().setText(Messages.DatasetRunBaseComposite_ParametersCol1);
		tblclmnDatasetRunParam.setLabelProvider(new DSParameterNameLabelProvider());
		layoutForTableViewerDSRunParams.setColumnData(tblclmnDatasetRunParam.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		TableViewerColumn tblclmnDatasetRunParamExp = new TableViewerColumn(tableViewerDatasetRunParams, SWT.NONE);
		tblclmnDatasetRunParamExp.getColumn().setWidth(187);
		tblclmnDatasetRunParamExp.getColumn().setText(Messages.DatasetRunBaseComposite_ParametersCol2);
		tblclmnDatasetRunParamExp.setLabelProvider(new DSParameterExpressionLabelProvider());
		layoutForTableViewerDSRunParams.setColumnData(tblclmnDatasetRunParamExp.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		tableViewerDatasetRunParams.setContentProvider(new ArrayContentProvider());
		tableViewerDatasetRunParams.addDoubleClickListener(new IDoubleClickListener() {
			
			public void doubleClick(DoubleClickEvent event) {
				Object selElement = ((IStructuredSelection) event.getSelection()).getFirstElement();
				if (selElement!=null){
					JRDesignDataset designDS = 
							(JRDesignDataset)datasetRunInstance.getEditableDataset().getJasperDesign().getDatasetMap().get(
									datasetRunInstance.getJRDatasetRun().getDatasetName());
					JRDesignDatasetParameter originalParameter=(JRDesignDatasetParameter)selElement;
					DatasetRunPameterDialog dialog=new DatasetRunPameterDialog((JRDesignDatasetParameter)originalParameter.clone(), designDS, getShell());
					dialog.setExpressionContext(expContext);
					if(dialog.open()==Window.OK){
						JRDesignDatasetParameter modifiedParameter = dialog.getModifiedDatasetParameter();
						datasetRunInstance.removeParameter(originalParameter);
						datasetRunInstance.addParameter(modifiedParameter);
						tableViewerDatasetRunParams.setInput(datasetRunInstance.getJRDatasetRun().getParameters());
					}
				}				
			}
		});
		
		Button btnAddParamDatasetRun = new Button(compositeParametersContent, SWT.NONE);
		btnAddParamDatasetRun.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnAddParamDatasetRun.setText(Messages.DatasetRunBaseComposite_AddParamBtn);
		btnAddParamDatasetRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				JRDesignDataset designDS = 
						(JRDesignDataset)datasetRunInstance.getEditableDataset().getJasperDesign().getDatasetMap().get(
								datasetRunInstance.getJRDatasetRun().getDatasetName());
				DatasetRunPameterDialog dialog=new DatasetRunPameterDialog(null, designDS, getShell());
				dialog.setExpressionContext(expContext);
				if(dialog.open()==Window.OK){
					JRDesignDatasetParameter newParameter = dialog.getModifiedDatasetParameter();
					datasetRunInstance.addParameter(newParameter);
					tableViewerDatasetRunParams.setInput(datasetRunInstance.getJRDatasetRun().getParameters());
				}
			}
		});
		
		Button btnModifyParameterDatasetRun = new Button(compositeParametersContent, SWT.NONE);
		btnModifyParameterDatasetRun.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnModifyParameterDatasetRun.setText(Messages.DatasetRunBaseComposite_ModifyParamBtn);
		btnModifyParameterDatasetRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selElement = ((IStructuredSelection) tableViewerDatasetRunParams.getSelection()).getFirstElement();
				if (selElement!=null){
					JRDesignDataset designDS = 
							(JRDesignDataset)datasetRunInstance.getEditableDataset().getJasperDesign().getDatasetMap().get(
									datasetRunInstance.getJRDatasetRun().getDatasetName());
					JRDesignDatasetParameter originalParameter=(JRDesignDatasetParameter)selElement;
					DatasetRunPameterDialog dialog=new DatasetRunPameterDialog((JRDesignDatasetParameter)originalParameter.clone(), designDS, getShell());
					dialog.setExpressionContext(expContext);
					if(dialog.open()==Window.OK){
						JRDesignDatasetParameter modifiedParameter = dialog.getModifiedDatasetParameter();
						datasetRunInstance.removeParameter(originalParameter);
						datasetRunInstance.addParameter(modifiedParameter);
						tableViewerDatasetRunParams.setInput(datasetRunInstance.getJRDatasetRun().getParameters());
					}
				}
			}
		});
		
		Button btnRemoveParameterDatasetRun = new Button(compositeParametersContent, SWT.NONE);
		btnRemoveParameterDatasetRun.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false, 1, 1));
		btnRemoveParameterDatasetRun.setText(Messages.DatasetRunBaseComposite_RemoveParamBtn);
		btnRemoveParameterDatasetRun.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Object selElement = ((IStructuredSelection) tableViewerDatasetRunParams.getSelection()).getFirstElement();
				if (selElement!=null){
					datasetRunInstance.removeParameter((JRDesignDatasetParameter)selElement);
					tableViewerDatasetRunParams.setInput(datasetRunInstance.getJRDatasetRun().getParameters());
				}
			}
		});
		
		TabItem tbtmParametersMapExp = new TabItem(tabFolderDataSetRun, SWT.NONE);
		tbtmParametersMapExp.setText(Messages.DatasetRunBaseComposite_ParametersMapExprTab);
		
		Composite compositeParamsExpMapBox = new Composite(tabFolderDataSetRun, SWT.NONE);
		tbtmParametersMapExp.setControl(compositeParamsExpMapBox);
		GridLayout gl_compositeParamsExpMapBox = new GridLayout(3, false);
		compositeParamsExpMapBox.setLayout(gl_compositeParamsExpMapBox);
		paramsMapExpression = new WTextExpression(compositeParamsExpMapBox, SWT.NONE, Messages.DatasetRunBaseComposite_ParametersMapExprWidgetLbl, WTextExpression.LABEL_ON_TOP){

			@Override
			public void setExpression(JRDesignExpression exp) {
				super.setExpression(exp);
				datasetRunInstance.setParametersMapExpression(exp);
			}
			
		};
		paramsMapExpression.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
				
		initWidgets();
		
	}

	/*
	 * Inits all the components inside the composite mask. 
	 */
	private void initWidgets() {
		// Sub dataset information
		fillSubDatasetComboBox();
		if (this.datasetRunInstance.getJRDatasetRun()==null){
			// Force selection for the report main dataset
			comboSubDataset.select(0);
			tabFolderDataSetRun.setEnabled(false);
			tabFolderDataSetRun.setVisible(false);
		}
		else {
			// Select the correct dataset name
			String datasetName = this.datasetRunInstance.getJRDatasetRun().getDatasetName();
			for (int i=0;i<comboSubDataset.getItemCount();i++){
				if (comboSubDataset.getItem(i).equals(datasetName)){
					comboSubDataset.select(i);
					break;
				}
			}
			if (comboSubDataset.getSelectionIndex()>=1){
				tabFolderDataSetRun.setEnabled(true);
				tabFolderDataSetRun.setVisible(true);
			}
			else{
				// Handle dirty dataset name for dataset run information
				MessageDialog.openError(getShell(), Messages.DatasetRunBaseComposite_SubDatasetErrorTitle, 
						MessageFormat.format(Messages.DatasetRunBaseComposite_SubDatasetErrorMsg, new Object[]{datasetName}));
				tabFolderDataSetRun.setEnabled(false);
				tabFolderDataSetRun.setVisible(false);
				comboSubDataset.select(0);
			}
		}
		
		// Connection/Datasource Expression
		comboConnDS.setItems(new String[]{
				"Don't use connection or datasource", //$NON-NLS-1$
				"Use a Connection expression", //$NON-NLS-1$
				"Use a DataSource expression"}); //$NON-NLS-1$
		JRDesignExpression exp1=null;
		if (datasetRunInstance.getJRDatasetRun()==null) {
			comboConnDS.select(0);
			this.connDSExpression.setEnabled(false);
			this.connDSExpression.setVisible(false);
		}
		else {
			if (datasetRunInstance.getJRDatasetRun().getConnectionExpression()!=null){
				comboConnDS.select(1);
				this.connDSExpression.setEnabled(true);
				this.connDSExpression.setVisible(true);
				exp1=(JRDesignExpression)datasetRunInstance.getJRDatasetRun().getConnectionExpression();
			}
			else if (datasetRunInstance.getJRDatasetRun().getDataSourceExpression()!=null){
				comboConnDS.select(2);
				this.connDSExpression.setEnabled(true);
				this.connDSExpression.setVisible(true);
				exp1=(JRDesignExpression)datasetRunInstance.getJRDatasetRun().getDataSourceExpression();
			}
			else {
				comboConnDS.select(0);
				this.connDSExpression.setEnabled(false);
				this.connDSExpression.setVisible(false);
			}
		}
		this.connDSExpression.setExpression(exp1);

		// Parameters
		if (datasetRunInstance.getJRDatasetRun()!=null){
			tableViewerDatasetRunParams.setInput(datasetRunInstance.getJRDatasetRun().getParameters());
		}
		
		// Parameters Map Expression
		JRDesignExpression exp2=null;
		if (datasetRunInstance.getJRDatasetRun()!=null){
			exp2=(JRDesignExpression) datasetRunInstance.getJRDatasetRun().getParametersMapExpression();
		}
		paramsMapExpression.setExpression(exp2);
		
	}
	
	/*
	 * Fills the dataset list combo box. 
	 */
	private void fillSubDatasetComboBox() {
		List<JRDataset> datasetsList = this.datasetRunInstance.getEditableDataset().getJasperDesign().getDatasetsList();
		comboSubDataset.removeAll();
		List<String> datasetNames=new ArrayList<String>();
		// Always add the report main dataset
		datasetNames.add("[Report main dataset]"); //$NON-NLS-1$
		if (datasetsList!=null && !datasetsList.isEmpty()){
			// Add all other datasets
			for (int i=0;i<datasetsList.size();i++){
				JRDataset currDS = datasetsList.get(i);
				datasetNames.add(currDS.getName());
			}
		}
		comboSubDataset.setItems(datasetNames.toArray(new String[]{}));
	}
	
	/*
	 * Updates the sub dataset information with the correct selected dataset name.
	 */
	private void updateSubDatasetInformation(int selIndex) {
		if (selIndex==0){
			// Must force the use of the report main dataset
			this.datasetRunInstance.resetDatasetRun(true);
			tabFolderDataSetRun.setEnabled(false);
			tabFolderDataSetRun.setVisible(false);
		}
		else {
			this.datasetRunInstance.resetDatasetRun(false);
			String selectDatasetName=comboSubDataset.getItem(selIndex);
			List<JRDataset> datasetsList = this.datasetRunInstance.getEditableDataset().getJasperDesign().getDatasetsList();
			for (JRDataset ds : datasetsList){
				if (ds.getName().equals(selectDatasetName)){
					this.datasetRunInstance.setDatasetName(selectDatasetName);
					break;
				}
			}
			tabFolderDataSetRun.setEnabled(true);
			tabFolderDataSetRun.setVisible(true);
		}
	}

	
	/* 
	 * Label provider for the first column of the viewer (dataset parameter name).
	 */
	private class DSParameterNameLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element!=null){
				return ((JRDatasetParameter)element).getName();
			}
			return null;
		}
	}
	
	/*
	 * Label provider for the second column of the viewer (dataset parameter expression).
	 */
	private class DSParameterExpressionLabelProvider extends ColumnLabelProvider {
		@Override
		public String getText(Object element) {
			if (element!=null){
				JRExpression expression = ((JRDatasetParameter)element).getExpression();
				if (expression!=null){
					return expression.getText();
				}
			}
			return null;
		}
	}

	public void addDatasetRunSelectionListener(DatasetRunSelectionListener listener){
		dsRunSelectionListeners.add(listener);
	}
	
	public void removeDatasetRunSelectionListener(DatasetRunSelectionListener listener){
		dsRunSelectionListeners.remove(listener);
	}
	
	private void notifyDatasetRunSelectionChanged(){
		for(DatasetRunSelectionListener l : dsRunSelectionListeners){
			l.selectionChanged();
		}
		fixDSParametersList();
	}

	/* 
	 * Fixes the dataset run parameters in the list.
	 * We can keep the ones with the same name, discard others.
	 */
	private void fixDSParametersList() {
		JRDatasetParameter[] currParams = (JRDatasetParameter[]) tableViewerDatasetRunParams.getInput();
		String datasetName = "";
		JRDatasetRun jrDatasetRun = datasetRunInstance.getJRDatasetRun();
		if(jrDatasetRun!=null) {
			datasetName = jrDatasetRun.getDatasetName();
			List<JRParameter> parameters4Datasource = ModelUtils.getParameters4Datasource(datasetRunInstance.getEditableDataset().getJasperDesign(), datasetName);
			if(currParams!=null) {
				for(JRDatasetParameter p1 : currParams) {
					for(JRParameter p2 : parameters4Datasource) {
						if(p2.getName().equals(p1.getName())){
							datasetRunInstance.addParameter((JRDatasetParameter) p1.clone());
							break;
						}
					}
				}
			}
			tableViewerDatasetRunParams.setInput(datasetRunInstance.getJRDatasetRun().getParameters());
		}
		else {
			// switching to main one
			tableViewerDatasetRunParams.getTable().clearAll();
		}
		
	}

	/*
	 * (non-Javadoc)
	 * @see com.jaspersoft.studio.editor.expression.IExpressionContextSetter#setExpressionContext(com.jaspersoft.studio.editor.expression.ExpressionContext)
	 */
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext=expContext;
		this.connDSExpression.setExpressionContext(expContext);
		this.paramsMapExpression.setExpressionContext(expContext);
	}
}
