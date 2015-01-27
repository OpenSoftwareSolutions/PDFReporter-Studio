/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.swt.widgets.table;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.swt.events.ChangeEvent;
import com.jaspersoft.studio.swt.events.ChangeListener;

public class ListOrderButtons {
	private Button upField;
	private Button downFields;

	private Set<ChangeListener> listeners = new HashSet<ChangeListener>(1); // or can use ChangeSupport in NB 6.0

	/**
	 * Add a change listener to listen for changes on the selected fields
	 * 
	 * @param ChangeListener
	 *          a listener
	 */
	public final void addChangeListener(ChangeListener l) {
		synchronized (listeners) {
			listeners.add(l);
		}
	}

	public final void removeChangeListener(ChangeListener l) {
		synchronized (listeners) {
			listeners.remove(l);
		}
	}

	/**
	 * Method to invoke when the out fields set changes.
	 */
	protected final void fireChangeEvent() {
		Iterator<ChangeListener> it;
		synchronized (listeners) {
			it = new HashSet<ChangeListener>(listeners).iterator();
		}
		ChangeEvent ev = new ChangeEvent(this);
		while (it.hasNext()) {
			it.next().changed(ev);
		}
	}

	private final class ElementOrderChanger extends SelectionAdapter {
		private final TableViewer tableViewer;
		private boolean up;

		private ElementOrderChanger(TableViewer tableViewer, boolean up) {
			this.tableViewer = tableViewer;
			this.up = up;
		}

		@SuppressWarnings({ "rawtypes" })
		@Override
		public void widgetSelected(SelectionEvent e) {
			StructuredSelection s = (StructuredSelection) tableViewer.getSelection();
			if (!s.isEmpty()) {
				List lst = (List) tableViewer.getInput();
				moveDown(lst, s);
				tableViewer.refresh();
				tableViewer.setSelection(s);
				tableViewer.reveal(s.getFirstElement());

				fireChangeEvent();
			}
		}

		private void moveDown(List lst, StructuredSelection s) {
			Object[] selected = s.toArray();
			int[] indxs = new int[selected.length];
			for (int i = 0; i < selected.length; i++)
				indxs[i] = lst.indexOf(selected[i]);
			for (Object obj : selected)
				lst.remove(obj);

			for (int i = 0; i < indxs.length; i++) {
				int index = up ? indxs[i] - 1 : indxs[i] + 1;
				if (index < 0)
					index = 0;
				if (index >= 0 && index < lst.size())
					lst.add(index, selected[i]);
				else
					lst.add(selected[i]);
			}
		}

	}

	public void createOrderButtons(Composite composite, TableViewer tableViewer) {
		upField = new Button(composite, SWT.PUSH);
		upField.setText(Messages.common_up);
		upField.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		upField.addSelectionListener(new ElementOrderChanger(tableViewer, true));

		downFields = new Button(composite, SWT.PUSH);
		downFields.setText(Messages.common_down);
		downFields.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		downFields.addSelectionListener(new ElementOrderChanger(tableViewer, false));
	}

	public void setEnabled(boolean enabled) {
		upField.setEnabled(enabled);
		downFields.setEnabled(enabled);
	}
}
