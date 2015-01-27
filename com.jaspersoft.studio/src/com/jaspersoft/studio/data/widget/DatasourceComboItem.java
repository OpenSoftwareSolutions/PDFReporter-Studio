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
package com.jaspersoft.studio.data.widget;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.sf.jasperreports.eclipse.viewer.IReportViewerListener;
import net.sf.jasperreports.eclipse.viewer.ReportViewerEvent;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.IPartListener;

import com.jaspersoft.studio.data.DataAdapterDescriptor;
import com.jaspersoft.studio.data.storage.ADataAdapterStorage;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.widgets.TooltipCCombo;

public class DatasourceComboItem extends ContributionItem implements PropertyChangeListener, IReportViewerListener,
		Listener {

	private TooltipCCombo combo;
	private ToolItem toolitem;
	private IPartListener partListener;
	private IDataAdapterRunnable editor;

	private ADataAdapterStorage[] dastorages;

	/**
	 * Constructor for ComboToolItem.
	 * 
	 * @param partService
	 *          used to add a PartListener
	 * @param initStrings
	 *          the initial string displayed in the combo
	 */
	public DatasourceComboItem(IDataAdapterRunnable editor, ADataAdapterStorage[] dastorages) {
		super("datasourceadaptercomboitem");
		this.editor = editor;
		setDataAdapterStorages(dastorages);
	}

	public void setDataAdapterStorages(ADataAdapterStorage[] dastorages) {
		if (this.dastorages != null) {
			for (ADataAdapterStorage das : dastorages)
				das.removePropertyChangeListener(this);
		}

		this.dastorages = dastorages;
		if (dastorages != null) {
			for (ADataAdapterStorage das : dastorages)
				das.addPropertyChangeListener(this);
		}
		refresh(true);
	}

	/**
	 * This method refresh the list of data adapters in the combo box. It does preserve the selection (if any).
	 * 
	 * If combo is disposed or not yet available, it does nothing.
	 * 
	 */
	public void updateDataAdapters() {
		if (combo == null || combo.isDisposed())
			return;
		// Remove the current listener
		combo.removeListener(SWT.Selection, this);
		combo.removeListener(SWT.DefaultSelection, this);

		DataAdapterDescriptor selectedAdapter = getSelected();
		combo.removeAll();

		combo.add("-- " + Messages.DatasourceComboItem_select_a_datasource + " --");
		if (dastorages != null)
			for (int i = 0; i < dastorages.length; i++) {
				ADataAdapterStorage s = dastorages[i];
				for (DataAdapterDescriptor d : s.getDataAdapterDescriptors())
					combo.add(d.getName(), s.getUrl(d));
				if (!s.getDataAdapterDescriptors().isEmpty() && i < dastorages.length - 1
						&& !dastorages[i + 1].getDataAdapterDescriptors().isEmpty())
					combo.add("----------------------");
			}

		// restore the selection...if any
		setSelected(selectedAdapter);

		combo.pack();
		Point size = combo.getSize();
		Rectangle bounds = combo.getBounds();
		bounds.width = Math.max(300, size.x);
		combo.setBounds(bounds);
		combo.setSize(size.x, bounds.height);
		toolitem.setWidth(size.x + 20);

		// Restore listener
		combo.addListener(SWT.Selection, this);
		combo.addListener(SWT.DefaultSelection, this);
	}

	public void setSelected(DataAdapterDescriptor selectedAdapter) {
		if (selectedAdapter != null) {
			int j = 1;
			int newSelectionIndex = 0;
			if (dastorages != null)
				for (int i = 0; i < dastorages.length; i++) {
					ADataAdapterStorage s = dastorages[i];
					for (DataAdapterDescriptor d : s.getDataAdapterDescriptors()) {
						if (selectedAdapter == d) {
							newSelectionIndex = j;
							break;
						}
						j++;
					}
					if (newSelectionIndex > 0)
						break;
					if (!s.getDataAdapterDescriptors().isEmpty() && i < dastorages.length - 1)
						j++;
				}
			combo.select(newSelectionIndex);
		}

		// Set a default selection
		if (combo.getSelectionIndex() < 0 && combo.getItemCount() > 0) {
			combo.select(0);
		}
		handleWidgetDefaultSelected(null);
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled() && editor.isNotRunning();// .canChangeZoom();
	}

	public void refresh(boolean repopulateCombo) {
		if (combo == null || combo.isDisposed())
			return;
		// $TODO GTK workaround
		try {
			if (!isEnabled()) {
				combo.setEnabled(false);
				//combo.setText(""); //$NON-NLS-1$
			} else {

				/*
				 * combo.removeListener(SWT.Selection, this); combo.removeListener(SWT.DefaultSelection, this);
				 * 
				 * if (repopulateCombo) {
				 * 
				 * 
				 * AMDatasource d = null; if (combo.getSelectionIndex() > 0) d = items.get(combo.getSelectionIndex() - 1); else
				 * if (selecteditem > 0 && items.size() > selecteditem) d = items.get(selecteditem - 1);
				 * 
				 * items = RepositoryManager.getDatasources();
				 * 
				 * combo.setItems(getStringItems()); selectCombo(0); if (d != null) for (int i = 0; i < items.size(); i++) if
				 * (items.get(i) == d) { selectCombo(i + 1); break; } } combo.setEnabled(true); combo.addListener(SWT.Selection,
				 * this); combo.addListener(SWT.DefaultSelection, this);
				 */
				if (repopulateCombo) {
					updateDataAdapters();
				}
				combo.setEnabled(true);
			}
		} catch (SWTException exception) {
			if (!SWT.getPlatform().equals("gtk")) //$NON-NLS-1$
				throw exception;
		}
	}

	private void selectCombo(int i) {
		combo.select(i);
	}

	/**
	 * Computes the width required by control
	 * 
	 * @param control
	 *          The control to compute width
	 * @return int The width required
	 */
	protected int computeWidth(Control control) {
		return control.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
	}

	/**
	 * Creates and returns the control for this contribution item under the given parent composite.
	 * 
	 * @param parent
	 *          the parent composite
	 * @return the new control
	 */
	protected Control createControl(Composite parent) {

		combo = new TooltipCCombo(parent, SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);

		combo.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				handleWidgetSelected(e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				handleWidgetDefaultSelected(e);
			}
		});
		combo.addFocusListener(new FocusListener() {
			public void focusGained(FocusEvent e) {
				// do nothing
			}

			public void focusLost(FocusEvent e) {
				refresh(false);
			}
		});

		refresh(true);
		toolitem.setWidth(computeWidth(combo));
		return combo;
	}

	/**
	 * @see org.eclipse.jface.action.ContributionItem#dispose()
	 */
	@Override
	public void dispose() {
		if (partListener == null)
			return;

		for (ADataAdapterStorage das : dastorages)
			das.removePropertyChangeListener(this);
		combo = null;
		partListener = null;
	}

	/**
	 * The control item implementation of this <code>IContributionItem</code> method calls the <code>createControl</code>
	 * framework method. Subclasses must implement <code>createControl</code> rather than overriding this method.
	 * 
	 * @param parent
	 *          The parent of the control to fill
	 */
	@Override
	public final void fill(Composite parent) {
		createControl(parent);
	}

	/**
	 * The control item implementation of this <code>IContributionItem</code> method throws an exception since controls
	 * cannot be added to menus.
	 * 
	 * @param parent
	 *          The menu
	 * @param index
	 *          Menu index
	 */
	@Override
	public final void fill(Menu parent, int index) {
		Assert.isTrue(false, "Can't add a control to a menu");//$NON-NLS-1$
	}

	/**
	 * The control item implementation of this <code>IContributionItem</code> method calls the <code>createControl</code>
	 * framework method to create a control under the given parent, and then creates a new tool item to hold it.
	 * Subclasses must implement <code>createControl</code> rather than overriding this method.
	 * 
	 * @param parent
	 *          The ToolBar to add the new control to
	 * @param index
	 *          Index
	 */
	@Override
	public void fill(ToolBar parent, int index) {
		toolitem = new ToolItem(parent, SWT.SEPARATOR, index);
		Control control = createControl(parent);
		toolitem.setControl(control);
	}

	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetDefaultSelected(SelectionEvent)
	 */
	private void handleWidgetDefaultSelected(SelectionEvent event) {
		if (combo.getSelectionIndex() > 0) {

			final DataAdapterDescriptor da = getSelected();
			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					selectedDA = da;
					editor.runReport(da);

				}
			});
		}

		refresh(false);
	}

	private DataAdapterDescriptor selectedDA;

	public DataAdapterDescriptor getSelected() {
		if (!combo.isDisposed()) {
			int index = combo.getSelectionIndex();
			if (index <= 0)
				return null;
			if (dastorages != null) {
				int j = 1;
				for (int i = 0; i < dastorages.length; i++) {
					ADataAdapterStorage s = dastorages[i];
					for (DataAdapterDescriptor d : s.getDataAdapterDescriptors()) {
						if (j == index) {
							selectedDA = d;
							selectCombo(index);
							return d;
						}
						j++;
					}
					if (!s.getDataAdapterDescriptors().isEmpty() && i < dastorages.length - 1)
						j++;
				}
			}
		}
		return selectedDA;
	}

	/**
	 * @see org.eclipse.swt.events.SelectionListener#widgetSelected(SelectionEvent)
	 */
	private void handleWidgetSelected(SelectionEvent event) {
		// forceSetText = true;
		handleWidgetDefaultSelected(event);
		// forceSetText = false;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		refresh(true);
	}

	/**
	 * @see org.eclipse.swt.widgets.Listener#handleEvent(org.eclipse.swt.widgets.Event)
	 */
	public void handleEvent(Event event) {
		switch (event.type) {
		case SWT.FocusIn:
			refresh(false);
			break;
		case SWT.Selection:
		case SWT.DefaultSelection:
			// onSelection();
			break;
		}
	}

	public void viewerStateChanged(ReportViewerEvent evt) {
		refresh(false);
	}

	public void setEnabled(boolean enabled) {
		combo.setEnabled(enabled);
	}

}
