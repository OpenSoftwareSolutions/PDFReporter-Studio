/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.properties;

import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;
import com.jaspersoft.jasperreports.customvisualization.design.CVDesignComponent;
import com.jaspersoft.studio.components.customvisualization.messages.Messages;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.utils.ModelUtils;

/**
 * Widget to modify the {@link CVDesignComponent#PROPERTY_ITEM_PROPERTIES}
 * property in the dedicated Property section.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 * 
 */
public class SPCVItemPropertiesList extends ASPropertyWidget {

	private TableViewer propertiesTV;
	private Button btnAddProperty;
	private Button btnModifyProperty;
	private Button btnRemoveProperty;
	private Group propertiesGrp;
	private List<CVItemProperty> itemProps;

	public SPCVItemPropertiesList(Composite parent,
			AbstractSection section,
			IPropertyDescriptor pdescriptor) {
		super(parent,section,pdescriptor);
	}

	@Override
	protected void createComponent(Composite parent) {
		propertiesGrp = new Group(parent, SWT.NONE);
		propertiesGrp.setLayout(new GridLayout(2,false));
		propertiesGrp.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		
		propertiesTV = createPropertiesTable(propertiesGrp);
		propertiesTV.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				modifyPropertyBtnPressed();
			}
		});
		
		btnAddProperty = new Button(propertiesGrp, SWT.PUSH);
		btnAddProperty.setText(Messages.SPCVItemPropertiesList_Add);
		btnAddProperty.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		btnAddProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewPropertyBtnPressed();
			}
		});

		btnModifyProperty = new Button(propertiesGrp, SWT.PUSH);
		btnModifyProperty.setText(Messages.SPCVItemPropertiesList_Edit);
		btnModifyProperty.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		btnModifyProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifyPropertyBtnPressed();
			}
		});
		
		btnRemoveProperty = new Button(propertiesGrp, SWT.PUSH);
		btnRemoveProperty.setText(Messages.SPCVItemPropertiesList_Remove);
		btnRemoveProperty.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		btnRemoveProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removePropertyBtnPressed();
			}
		});
	}
	
	private void addNewPropertyBtnPressed() {
		CVItemPropertyDialog d = new CVItemPropertyDialog(UIUtils.getShell(), null, null);
		d.setExpressionContext(getExpressionContext());
		if(d.open()==Window.OK) {
			itemProps.add(d.getItemProperty());
			section.changeProperty(CVDesignComponent.PROPERTY_ITEM_PROPERTIES, itemProps);
		}
	}
	
	private void modifyPropertyBtnPressed() {
		CVItemProperty p = getCurrentSelectedProperty();
		if(p!=null) {
			CVItemProperty clonedP = (CVItemProperty) p.clone();
			CVItemPropertyDialog d = new CVItemPropertyDialog(UIUtils.getShell(), clonedP, null);
			d.setExpressionContext(getExpressionContext());
			if(d.open()==Window.OK) {
				int idx = itemProps.indexOf(p);
				itemProps.remove(p);
				itemProps.add(idx,clonedP);
				section.changeProperty(CVDesignComponent.PROPERTY_ITEM_PROPERTIES, itemProps);
			}
		}
	}
	
	private void removePropertyBtnPressed() {
		CVItemProperty p = getCurrentSelectedProperty();
		if (p!=null) {
			itemProps.remove(p);
			section.changeProperty(CVDesignComponent.PROPERTY_ITEM_PROPERTIES, itemProps);
		}
	}

	private CVItemProperty getCurrentSelectedProperty() {
		Object selEl = ((IStructuredSelection) propertiesTV.getSelection()).getFirstElement();
		if(selEl instanceof CVItemProperty) {
			return (CVItemProperty) selEl;
		}
		return null;
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
		tvcName.getColumn().setText(Messages.SPCVItemPropertiesList_ColName);
		tvcName.setLabelProvider(new ItemPropertyNameLabelProvider());
		tl_itemPropertiesTableViewer.setColumnData(tvcName.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));

		TableViewerColumn tvcValue = new TableViewerColumn(tv, SWT.NONE);
		tvcValue.getColumn().setText(Messages.SPCVItemPropertiesList_ColValue);
		tvcValue.setLabelProvider(new ItemPropertyValueLabelProvider());
		tl_itemPropertiesTableViewer.setColumnData(tvcValue.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		tv.setContentProvider(new ArrayContentProvider());

		return tv;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void setData(APropertyNode pnode, Object value) {
		itemProps = (List<CVItemProperty>) value;
		propertiesTV.setInput(itemProps);
	}

	@Override
	public Control getControl() {
		return propertiesGrp;
	}
	
	private ExpressionContext getExpressionContext() {
		return ModelUtils.getElementExpressionContext(null, section.getElement());
	}

}
