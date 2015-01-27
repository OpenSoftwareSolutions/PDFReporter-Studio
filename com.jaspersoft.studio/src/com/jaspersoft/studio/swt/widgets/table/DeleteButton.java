/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.swt.widgets.table;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;

public class DeleteButton {

	private Button delB;

	private final class DeleteListener extends SelectionAdapter {

		private TableViewer tableViewer;

		private DeleteListener(TableViewer tableViewer) {
			this.tableViewer = tableViewer;
		}

		// Remove the selection and refresh the view
		public void widgetSelected(SelectionEvent e) {
			StructuredSelection s = (StructuredSelection) tableViewer.getSelection();
			Object selement = null;
			if (!s.isEmpty()) {
				List<?> inlist = (List<?>) tableViewer.getInput();
				for (Object obj : s.toArray()) {
					if (!canRemove(obj))
						continue;
					int ind = inlist.indexOf(obj);
					inlist.remove(obj);
					afterElementDeleted(obj);
					if (ind < inlist.size()) {
						selement = inlist.get(ind);
					}
				}
				tableViewer.refresh();
				if (selement != null)
					tableViewer.setSelection(new StructuredSelection(selement));
			}
		}
	}

	protected boolean canRemove(Object obj) {
		return true;
	}

	public void createDeleteButton(Composite composite, final TableViewer tableViewer) {
		delB = new Button(composite, SWT.PUSH);
		delB.setText(Messages.common_delete);
		delB.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING));
		delB.addSelectionListener(new DeleteListener(tableViewer));
		setEnabledState(tableViewer);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				setEnabledState(tableViewer);
			}
		});
	}

	public void setEnabled(boolean enabled) {
		delB.setEnabled(enabled);
	}

	private void setEnabledState(final TableViewer tableViewer) {
		boolean enable = true;
		StructuredSelection s = (StructuredSelection) tableViewer.getSelection();
		if (!s.isEmpty()) {
			for (Object obj : s.toArray()) {
				if (!canRemove(obj)) {
					enable = false;
					break;
				}
			}
		} else
			enable = true;
		delB.setEnabled(enable);
	}

	/**
	 * Additional operations to be performed once the element has been deleted.
	 * 
	 * @param element
	 */
	protected void afterElementDeleted(Object element) {
		// empty...
	}
}
