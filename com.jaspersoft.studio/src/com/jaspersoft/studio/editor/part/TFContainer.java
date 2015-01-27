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
package com.jaspersoft.studio.editor.part;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

import com.jaspersoft.studio.editor.report.AbstractVisualEditor;

public class TFContainer extends Composite {
	private StackLayout stackLayout;
	private ToolBar toolBar;
	private ToolBar additionalToolbar;
	private ToolBarManager additionalToolbarManager;

	public TFContainer(Composite parent, int style) {
		super(parent, style);
		GridLayout layout = new GridLayout(2,false);
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		layout.verticalSpacing = 0;
		setLayout(layout);

		toolBar = new ToolBar(this, SWT.HORIZONTAL | SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		
		additionalToolbar = new ToolBar(this, SWT.HORIZONTAL | SWT.FLAT | SWT.WRAP | SWT.RIGHT);
		GridData additionalToolbarGD = new GridData(SWT.RIGHT, SWT.CENTER, true, false);
		additionalToolbar.setLayoutData(additionalToolbarGD);
		additionalToolbarManager = new ToolBarManager(additionalToolbar);
		
		content = new Composite(this, SWT.NONE);
		stackLayout = new StackLayout();
		stackLayout.marginWidth = 0;
		stackLayout.marginHeight = 0;
		content.setLayout(stackLayout);
		content.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
	}

	public Composite getContent() {
		return content;
	}

	private List<TFItem> tfitems = new ArrayList<TFItem>();
	private int selection = -1;

	public int indexOf(TFItem item) {
		return tfitems.indexOf(item);
	}

	public int getSelectionIndex() {
		return selection;
	}

	public void setSelection(int selection) {
		this.selection = selection;
		for (int i = 0; i < toolBar.getItemCount(); i++) {
			toolBar.getItem(i).setSelection(i == selection);
		}
		stackLayout.topControl = getItem(selection).getControl();
		
		Object data = toolBar.getItem(selection).getData();
		if(data instanceof TFItem){
			TFItem tfItem=(TFItem)data; 
			if(tfItem.getData() instanceof AbstractVisualEditor){
				populateAdditionalToolbar((AbstractVisualEditor) tfItem.getData());
			}
		}
		
		getParent().layout();
	}

	public void removeItem(TFItem item) {
		int index = tfitems.indexOf(item);
		toolBar.getItem(index).dispose();
		tfitems.remove(item);
		toolBar.update();
		this.pack();
		this.layout(true);
		if (index == selection)
			setSelection(--index);
	}

	public TFItem getItem(int selectedIndex) {
		return tfitems.get(selectedIndex);
	}

	public int getItemCount() {
		return tfitems.size();
	}

	private List<SelectionListener> listeners = new ArrayList<SelectionListener>();
	private Composite content;

	public void addSelectionListener(SelectionListener listener) {
		listeners.add(listener);
	}

	public void createItem(final TFItem item, int index) {
		final ToolItem ti = new ToolItem(toolBar, SWT.RADIO);
		ti.setText("Item1" + item.getText());
		ti.setData(item);
		ti.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				if (ti.getSelection() && tfitems.indexOf(item) != selection)
					for (SelectionListener sl : listeners)
						sl.widgetSelected(e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});
		tfitems.add(index, item);
		toolBar.update();
		this.pack();
		this.layout(true);
	}
	
	public void update(TFItem tfItem) {
		for (ToolItem it : toolBar.getItems()) {
			if (it.getData() == tfItem) {	
				it.setText(tfItem.getText());
				it.setImage(tfItem.getImage());
				toolBar.update();
				layout(true);
				break;
			}
		}
	}
	
	/*
	 * Enrich the toolbar manager registered for the additional toolbar on the right.
	 */
	private void populateAdditionalToolbar(AbstractVisualEditor editor){
		additionalToolbarManager.removeAll();
		editor.contributeItemsToEditorTopToolbar(additionalToolbarManager);
		additionalToolbarManager.update(true);
	}
}
