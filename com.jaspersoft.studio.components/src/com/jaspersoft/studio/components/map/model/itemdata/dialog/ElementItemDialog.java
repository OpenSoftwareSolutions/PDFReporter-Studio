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
package com.jaspersoft.studio.components.map.model.itemdata.dialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.map.ItemProperty;
import net.sf.jasperreports.components.map.StandardItem;
import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.engine.design.JRDesignDataset;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.window.Window;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.studio.components.map.messages.Messages;
import com.jaspersoft.studio.components.map.model.itemdata.ElementDataHelper;
import com.jaspersoft.studio.components.map.model.itemdata.ElementsListWidgetConfiguration;
import com.jaspersoft.studio.components.map.model.itemdata.dto.MapDataElementItemDTO;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Dialog that allows editing the information associated to a {@link MapDataElementItemDTO} element. 
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class ElementItemDialog extends Dialog {

	private TableViewer propertiesTV;
	private Button btnAddProperty;
	private Button btnModifyProperty;
	private Button btnRemoveProperty;
	private MapDataElementItemDTO itemDTO;
	private StandardItem item;
	private ExpressionContext expContext;
	private List<String> mandatoryPropertyNames;
	private String[] allDatasetNames;
	private Combo datasetCombo;
	private Button datasetRBtn;
	private Button staticRBtn;
	private Map<String, String> elementDatasetsMap;
	private JasperReportsConfiguration jconfig;
	private ExpressionContext defaultExpressionContext;
	private ElementsListWidgetConfiguration wconfig;

	public ElementItemDialog(
			Shell parentShell, MapDataElementItemDTO itemDTO, ElementsListWidgetConfiguration wconfig, 
			Map<String,String> elementDatasetsMap, JasperReportsConfiguration jconfig) {
		super(parentShell);
		this.itemDTO = itemDTO;
		this.item = (StandardItem) this.itemDTO.getItem();
		this.wconfig = wconfig;
		this.mandatoryPropertyNames = new ArrayList<String>();
		this.elementDatasetsMap = elementDatasetsMap;
		this.allDatasetNames = this.elementDatasetsMap.keySet().toArray(new String[]{});
		this.jconfig = jconfig;
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		dialogArea.setLayout(new GridLayout(1,false));
		
		Group itemKindGrp = new Group(dialogArea,SWT.NONE);
		itemKindGrp.setText(NLS.bind(Messages.ElementItemDialog_ItemKind,wconfig.getElementTxt()));
		itemKindGrp.setLayout(new GridLayout(1,false));
		itemKindGrp.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		staticRBtn = new Button(itemKindGrp,SWT.RADIO);
		staticRBtn.setText(Messages.ElementItemDialog_Static);
		staticRBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		datasetRBtn = new Button(itemKindGrp,SWT.RADIO);
		datasetRBtn.setText(Messages.ElementItemDialog_DatasetBased);
		datasetRBtn.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		
		datasetCombo = new Combo(itemKindGrp, SWT.READ_ONLY);
		GridData datasetComboGD = new GridData(SWT.LEFT, SWT.FILL, false, false);
		datasetComboGD.widthHint = 200;
		datasetComboGD.horizontalIndent = 15;
		datasetCombo.setLayoutData(datasetComboGD);
		
		Group propertiesGrp = new Group(dialogArea, SWT.NONE);
		propertiesGrp.setText(Messages.ElementItemDialog_Properties);
		propertiesGrp.setLayout(new GridLayout(2,false));
		propertiesGrp.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		propertiesTV = createPropertiesTable(propertiesGrp);
		propertiesTV.getTable().setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true,1,3));
		
		btnAddProperty = new Button(propertiesGrp, SWT.PUSH);
		btnAddProperty.setText(Messages.ElementItemDialog_Add);
		btnAddProperty.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));

		btnModifyProperty = new Button(propertiesGrp, SWT.PUSH);
		btnModifyProperty.setText(Messages.ElementItemDialog_Modify);
		btnModifyProperty.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));

		
		btnRemoveProperty = new Button(propertiesGrp, SWT.PUSH);
		btnRemoveProperty.setText(Messages.ElementItemDialog_Remove);
		btnRemoveProperty.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		
		initWidgets();
		attachListeners();
		enableDefaultButtons();
				
		return dialogArea;
	}
	
	private void initWidgets() {
		boolean staticItem = itemDTO.isStatic();
		staticRBtn.setSelection(staticItem);
		datasetRBtn.setSelection(!staticItem);
		datasetCombo.setEnabled(!staticItem);
		datasetCombo.setItems(allDatasetNames);
		if(!staticItem){
			int index = Arrays.asList(allDatasetNames).indexOf(itemDTO.getDatasetName());
			datasetCombo.select(index);
		}
		propertiesTV.setInput(item.getProperties());
	}

	private void attachListeners() {
		btnAddProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addPropertyBtnPressed();
			}
		});
	
		btnModifyProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifyPropertyBtnPressed();				
			}
		});
		
		btnRemoveProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removePropertyBtnPressed();
			}
		});
		
		propertiesTV.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				modifyPropertyBtnPressed();
			}
		});
		propertiesTV.addSelectionChangedListener(new ISelectionChangedListener() {
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				enableDefaultButtons();
			}
		});
		staticRBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				datasetCombo.setEnabled(false);
				datasetCombo.setItems(new String[0]);
				itemDTO.setDatasetName(null);
				computeExpressionContext();
			}
		});
		datasetRBtn.addSelectionListener(new SelectionAdapter(){
			@Override
			public void widgetSelected(SelectionEvent e) {
				datasetCombo.setEnabled(true);
				datasetCombo.setItems(allDatasetNames);
			}
		});
		datasetCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				itemDTO.setDatasetName(datasetCombo.getText());
				computeExpressionContext();
			}
		});
	}
	
	private TableViewer createPropertiesTable(Composite parent) {
		Composite cmpItemPropertiesTableViewer=new Composite(parent, SWT.NONE);
		cmpItemPropertiesTableViewer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true,1,3));
		TableColumnLayout tl_itemPropertiesTableViewer = new TableColumnLayout();
		cmpItemPropertiesTableViewer.setLayout(tl_itemPropertiesTableViewer);
		
		TableViewer tv = new TableViewer(cmpItemPropertiesTableViewer,SWT.BORDER | SWT.V_SCROLL | SWT.SINGLE | SWT.FULL_SELECTION);
		tv.getTable().setHeaderVisible(true);
		tv.getTable().setLinesVisible(true);
		
		TableViewerColumn tvcName = new TableViewerColumn(tv, SWT.NONE);
		tvcName.getColumn().setText(Messages.ElementItemDialog_NameColumn);
		tvcName.setLabelProvider(getItemPropertyNameLabelProvider());
		tl_itemPropertiesTableViewer.setColumnData(tvcName.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));

		TableViewerColumn tvcValue = new TableViewerColumn(tv, SWT.NONE);
		tvcValue.getColumn().setText(Messages.ElementItemDialog_ValueColumn);
		tvcValue.setLabelProvider(getItemPropertyValueLabelProvider());
		tl_itemPropertiesTableViewer.setColumnData(tvcValue.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		tv.setContentProvider(new ArrayContentProvider());

		return tv;
	}

	private CellLabelProvider getItemPropertyValueLabelProvider() {
		return new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof ItemProperty) {
					String value = ElementDataHelper.getItemPropertyValueAsString((ItemProperty) element);
					return Misc.nvl(value);
				}
				return super.getText(element);
			}
		};
	}

	private CellLabelProvider getItemPropertyNameLabelProvider() {
		return new ColumnLabelProvider(){
			@Override
			public String getText(Object element) {
				if(element instanceof ItemProperty) {
					return ((ItemProperty) element).getName();
				}
				return super.getText(element);
			}
		};
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(NLS.bind(Messages.ElementItemDialog_DialogTitle,wconfig.getElementTxt()));
		UIUtils.resizeAndCenterShell(newShell, 500, 550);
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}
	
	@Override
	public boolean close() {
		return super.close();
	}

	public void setMandatoryPropertyNames(List<String> names) {
		this.mandatoryPropertyNames = names;
	}
	
	private void addPropertyBtnPressed() {
		ItemPropertyDialog dialog = new ItemPropertyDialog(UIUtils.getShell(), null,wconfig.getElementPropertiesResourceLocation());
		dialog.setExpressionContext(expContext);
		if(dialog.open()==Window.OK){
			((StandardItem)item).addItemProperty(dialog.getItemProperty());
			refreshTable();
		}
	}
	
	private void modifyPropertyBtnPressed() {
		ItemProperty property = getCurrentSelectedProperty();
		if(property!=null) {
			ItemProperty clonedP = (ItemProperty) property.clone();
			ItemPropertyDialog dialog = new ItemPropertyDialog(UIUtils.getShell(),clonedP,wconfig.getElementPropertiesResourceLocation());
			dialog.setExpressionContext(expContext);
			if(dialog.open() == Window.OK){
				((StandardItem)item).removeItemProperty(property);
				((StandardItem)item).addItemProperty(dialog.getItemProperty());
				refreshTable();
			}
		}
	}
	
	private void removePropertyBtnPressed() {
		ItemProperty property = getCurrentSelectedProperty();
		if(property!=null) {
			if(isMandatoryProperty(property.getName())){
				MessageDialog.openError(
						UIUtils.getShell(), Messages.ElementItemDialog_ErrorDialogTitle, 
						NLS.bind(Messages.ElementItemDialog_ErrorDialogMandatoryNameMsg,property.getName()));
			}
			else {
				((StandardItem)item).removeItemProperty(property);
				refreshTable();
			}
		}
	}
	
	private void refreshTable() {
		this.propertiesTV.setInput(item.getProperties());
		enableDefaultButtons();
	}

	private void enableDefaultButtons() {
		ItemProperty selP = getCurrentSelectedProperty();
		btnAddProperty.setEnabled(true);
		btnModifyProperty.setEnabled(selP!=null);
		btnRemoveProperty.setEnabled(selP!=null && !isMandatoryProperty(selP.getName()));
	}
	
	private boolean isMandatoryProperty(String pname) {
		for(String n : mandatoryPropertyNames) {
			if(pname.equals(n)){
				return true;
			}
		}
		return false;
	}
	
	private ItemProperty getCurrentSelectedProperty() {
		Object selEl = ((IStructuredSelection) propertiesTV.getSelection()).getFirstElement();
		if(selEl instanceof ItemProperty) {
			return (ItemProperty) selEl;
		}
		return null;
	}

	public MapDataElementItemDTO getItemDTO() {
		return this.itemDTO;
	}

	public void setDefaultExpressionContext(
			ExpressionContext defaultExpressionContext) {
		// used when the static mode is selected
		this.defaultExpressionContext = defaultExpressionContext;
	}

	private void computeExpressionContext() {
		if(staticRBtn.getSelection()) {
			expContext = defaultExpressionContext;
		}
		else {
			String dsSelected = datasetCombo.getText();
			// update the expression context
			JRDesignDataset designDS = 
					ModelUtils.getDesignDatasetByName(jconfig.getJasperDesign(), elementDatasetsMap.get(dsSelected));
			expContext = new ExpressionContext(designDS,jconfig);
		}
	}
	
	@Override
	protected void okPressed() {
		if(datasetRBtn.getSelection() && datasetCombo.getText().isEmpty()) {
			MessageDialog.openError(UIUtils.getShell(), Messages.ElementItemDialog_ErrorDialogTitle, Messages.ElementItemDialog_ErrorDialogNoDatasetMsg);
		}
		else {
			super.okPressed();	
		}
	}
}
