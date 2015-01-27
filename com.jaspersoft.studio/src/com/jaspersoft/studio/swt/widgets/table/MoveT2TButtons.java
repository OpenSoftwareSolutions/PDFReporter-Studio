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
package com.jaspersoft.studio.swt.widgets.table;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.swt.events.ChangeEvent;
import com.jaspersoft.studio.swt.events.ChangeListener;
import com.jaspersoft.studio.utils.GridDataUtil;
import com.jaspersoft.studio.utils.UIUtil;

public class MoveT2TButtons {

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

	private final class MoveListener implements SelectionListener, IDoubleClickListener {

		private final ColumnViewer leftTView;
		private final ColumnViewer rightTView;

		private MoveListener(ColumnViewer leftTView, ColumnViewer rightTView) {
			this.leftTView = leftTView;
			this.rightTView = rightTView;
		}

		public void widgetSelected(SelectionEvent e) {
			handleMove(leftTView, rightTView);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			handleMove(leftTView, rightTView);
		}

		public void doubleClick(DoubleClickEvent event) {
			handleMove(leftTView, rightTView);
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void handleMove(ColumnViewer leftTView, ColumnViewer rightTView) {
		StructuredSelection s = (StructuredSelection) leftTView.getSelection();
		if (!s.isEmpty()) {
			List left = (List) leftTView.getInput();
			List right = (List) rightTView.getInput();
			if (left == null)
				leftTView.setInput(new ArrayList());
			if (right == null)
				rightTView.setInput(new ArrayList());
			for (Object obj : s.toArray()) {
				left.remove(obj);
				right.add(obj);

			}
			leftTView.refresh();
			rightTView.refresh();
			fireChangeEvent();
		}
	}

	private final class MoveAllListener implements SelectionListener {
		private final ColumnViewer leftTView;
		private final ColumnViewer rightTView;

		private MoveAllListener(ColumnViewer leftTView, ColumnViewer rightTView) {
			this.leftTView = leftTView;
			this.rightTView = rightTView;
		}

		public void widgetSelected(SelectionEvent e) {
			handleMoveAll(leftTView, rightTView);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void handleMoveAll(ColumnViewer leftTView, ColumnViewer rightTView) {
		List left = (List) leftTView.getInput();
		List right = (List) rightTView.getInput();
		for (Object obj : left)
			right.add(obj);

		left.clear();

		leftTView.refresh();
		rightTView.refresh();
		fireChangeEvent();
	}

	public void createButtons(Composite composite, ColumnViewer leftTView, TableViewer rightTView) {
		Button addField = new Button(composite, SWT.PUSH);
		int chw = UIUtil.getCharWidth(addField) * 7;
		addField.setText(" > "); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = chw;
		addField.setLayoutData(gd);
		addField.addSelectionListener(new MoveListener(leftTView, rightTView));

		Button addFields = new Button(composite, SWT.PUSH);
		addFields.setText(">>"); //$NON-NLS-1$
		addFields.setLayoutData(GridDataUtil.clone(gd));
		addFields.addSelectionListener(new MoveAllListener(leftTView, rightTView));

		Button delField = new Button(composite, SWT.PUSH);
		delField.setText(" < "); //$NON-NLS-1$
		delField.setLayoutData(GridDataUtil.clone(gd));
		delField.addSelectionListener(new MoveListener(rightTView, leftTView));

		Button delFields = new Button(composite, SWT.PUSH);
		delFields.setText("<<"); //$NON-NLS-1$
		delFields.setLayoutData(GridDataUtil.clone(gd));
		delFields.addSelectionListener(new MoveAllListener(rightTView, leftTView));

		// Add the doubleclick selection to the table viewers
		leftTView.addDoubleClickListener(new MoveListener(leftTView, rightTView));
		rightTView.addDoubleClickListener(new MoveListener(rightTView, leftTView));
	}

	public void createButtonsShort(Composite composite, ColumnViewer leftTView, TableViewer rightTView,
			boolean doubleClick) {
		Button addField = new Button(composite, SWT.PUSH);
		int chw = UIUtil.getCharWidth(addField) * 7;
		addField.setText(">"); //$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint = chw;
		addField.setLayoutData(gd);
		addField.addSelectionListener(new MoveListener(leftTView, rightTView));

		Button delField = new Button(composite, SWT.PUSH);
		delField.setText("<"); //$NON-NLS-1$
		delField.setLayoutData(GridDataUtil.clone(gd));
		delField.addSelectionListener(new MoveListener(rightTView, leftTView));
		if (doubleClick) {
			// Add the doubleclick selection to the table viewers
			leftTView.addDoubleClickListener(new MoveListener(leftTView, rightTView));
			rightTView.addDoubleClickListener(new MoveListener(rightTView, leftTView));
		}
	}

}
