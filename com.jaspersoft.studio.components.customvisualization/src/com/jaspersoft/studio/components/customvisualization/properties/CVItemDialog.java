/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved.
 * http://www.jaspersoft.com.
 * Licensed under commercial Jaspersoft Subscription License Agreement
 ******************************************************************************/
package com.jaspersoft.studio.components.customvisualization.properties;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
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
import org.eclipse.swt.widgets.Shell;

import com.jaspersoft.jasperreports.customvisualization.CVItem;
import com.jaspersoft.jasperreports.customvisualization.CVItemProperty;
import com.jaspersoft.jasperreports.customvisualization.design.CVDesignItem;
import com.jaspersoft.studio.components.customvisualization.messages.Messages;
import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;

/**
 * Edit dialog for a {@link CVItem} element.
 * 
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class CVItemDialog extends Dialog implements
		IExpressionContextSetter {

	private CVDesignItem item;
	private ExpressionContext expContext;
	private TableViewer propertiesTV;
	private Button btnAddProperty;
	private Button btnModifyProperty;
	private Button btnRemoveProperty;
	
	public CVItemDialog(Shell parentShell, CVItem item) {
		super(parentShell);
		this.item = (CVDesignItem) item;
		if(this.item == null) {
			this.item = new CVDesignItem();
		}
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL,SWT.FILL,true,true));
		container.setLayout(new GridLayout(2,false));
		
		propertiesTV = createPropertiesTable(container);

		btnAddProperty = new Button(container, SWT.PUSH);
		btnAddProperty.setText(Messages.CVItemDialog_Add);
		btnAddProperty.setLayoutData(new GridData(SWT.FILL,SWT.TOP,false,false));
		btnAddProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewPropertyBtnPressed();
			}
		});

		btnModifyProperty = new Button(container, SWT.PUSH);
		btnModifyProperty.setText(Messages.CVItemDialog_Edit);
		btnModifyProperty.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		btnModifyProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				modifyPropertyBtnPressed();
			}
		});
		
		btnRemoveProperty = new Button(container, SWT.PUSH);
		btnRemoveProperty.setText(Messages.CVItemDialog_Remove);
		btnRemoveProperty.setLayoutData(new GridData(SWT.FILL, SWT.TOP, false, false));
		btnRemoveProperty.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				removePropertyBtnPressed();
			}
		});
		applyDialogFont(container);
		propertiesTV.setInput(this.item.getItemProperties());
		
		return container;
	}
	
	private void addNewPropertyBtnPressed() {
		CVItemPropertyDialog d = new CVItemPropertyDialog(UIUtils.getShell(), null, null);
		d.setExpressionContext(expContext);
		if(d.open()==Window.OK) {
			this.item.getItemProperties().add(d.getItemProperty());
			propertiesTV.setInput(this.item.getItemProperties());
		}
	}
	
	private void modifyPropertyBtnPressed() {
		CVItemProperty p = getCurrentSelectedProperty();
		if(p!=null) {
			CVItemProperty clonedP = (CVItemProperty) p.clone();
			CVItemPropertyDialog d = new CVItemPropertyDialog(UIUtils.getShell(), clonedP, null);
			d.setExpressionContext(expContext);
			if(d.open()==Window.OK) {
				int idx = this.item.getItemProperties().indexOf(p);
				this.item.getItemProperties().remove(p);
				this.item.getItemProperties().add(idx,clonedP);
				propertiesTV.setInput(this.item.getItemProperties());
			}
		}
	}
	
	private void removePropertyBtnPressed() {
		CVItemProperty p = getCurrentSelectedProperty();
		if (p!=null) {
			this.item.getItemProperties().remove(p);
			propertiesTV.setInput(this.item.getItemProperties());
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
		tvcName.getColumn().setText(Messages.CVItemDialog_ColName);
		tvcName.setLabelProvider(new ItemPropertyNameLabelProvider());
		tl_itemPropertiesTableViewer.setColumnData(tvcName.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));

		TableViewerColumn tvcValue = new TableViewerColumn(tv, SWT.NONE);
		tvcValue.getColumn().setText(Messages.CVItemDialog_ColValue);
		tvcValue.setLabelProvider(new ItemPropertyValueLabelProvider());
		tl_itemPropertiesTableViewer.setColumnData(tvcValue.getColumn(), new ColumnWeightData(1, ColumnWeightData.MINIMUM_WIDTH, true));
		
		tv.setContentProvider(new ArrayContentProvider());
		
		tv.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				modifyPropertyBtnPressed();
			}
		});

		return tv;
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}
	
	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.CVItemDialog_EditItem);
		UIUtils.resizeAndCenterShell(newShell, 450, 300);
	}
	
	@Override
	protected void setShellStyle(int newShellStyle) {
		super.setShellStyle(newShellStyle | SWT.RESIZE);
	}
	

	@Override
	public void setExpressionContext(ExpressionContext expContext) {
		this.expContext = expContext;
	}
	
	public CVItem getCVItem() {
		return this.item;
	}

}
