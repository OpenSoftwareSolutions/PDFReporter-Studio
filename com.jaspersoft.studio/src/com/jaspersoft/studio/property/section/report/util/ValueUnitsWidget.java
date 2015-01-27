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
package com.jaspersoft.studio.property.section.report.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ValueUnitsWidget {

	private final class SpinerSelectionListener implements SelectionListener {
		public void widgetSelected(SelectionEvent e) {
			unit.setValue(new Float(val.getSelection() / Math.pow(10, digits)).floatValue(),
					Unit.getUnits()[unitc.getSelectionIndex()]);
		}

		public void widgetDefaultSelected(SelectionEvent e) {
			widgetSelected(e);
		}
	}

	private final class SpinerModifyListener implements ModifyListener {
		@Override
		public void modifyText(ModifyEvent e) {
			unit.setValue(new Float(val.getSelection() / Math.pow(10, digits)).floatValue(),
					Unit.getUnits()[unitc.getSelectionIndex()]);
		}
	}

	public ValueUnitsWidget(JasperReportsConfiguration jConfig) {
		unit = new Unit(0, Unit.PX, jConfig);
	}

	private Unit unit;
	private int max = Integer.MAX_VALUE;
	private int digits = 0;
	private Combo unitc;
	private Spinner val;
	private SpinerSelectionListener spinerSelection;
	private SpinerModifyListener spinerModify;

	public void createComponent(Composite parent, String label, String toolTip) {
		Label lbl = new Label(parent, SWT.NONE);
		lbl.setText(label);
		val = new Spinner(parent, SWT.BORDER | SWT.RIGHT);
		val.setToolTipText(toolTip);
		GridData gd = new GridData();
		gd.widthHint = 80;
		val.setLayoutData(gd);

		unitc = new Combo(parent, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		unitc.setItems(Unit.getUnits());
		unitc.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				String u = Unit.getUnits()[unitc.getSelectionIndex()];
				if (unit.setUnit(u)) {
					setSpinerValue(u);
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		spinerSelection = new SpinerSelectionListener();
		val.addSelectionListener(spinerSelection);

		spinerModify = new SpinerModifyListener();
		val.addModifyListener(spinerModify);

		unitc.select(0);
		setSpinerValue(unit.getUnit());
	}

	public void setMax(int max) {
		removeListeners();
		this.max = max;
		setSpinnerMax(max);
		addListeners();
	}

	private void setSpinnerMax(int max) {
		val.setMaximum((int) Math.round(unit.pixel2unit(max) * Math.pow(10, digits)));
	}

	private void setSpinerValue(String u) {
		digits = u.equals(Unit.PX) ? 0 : 4;

		removeListeners();
		val.setDigits(digits);
		val.setMinimum(0);
		setSpinnerMax(Math.max(max, unit.getPxValue()));
		val.setIncrement(1);
		val.setSelection((int) Math.round(unit.getValue(u) * Math.pow(10, digits)));
		addListeners();
	}

	private void removeListeners() {
		val.removeModifyListener(spinerModify);
		for (ModifyListener ml : mlisteners)
			val.removeModifyListener(ml);
		val.removeSelectionListener(spinerSelection);
		for (SelectionListener sl : slisteners)
			val.removeSelectionListener(sl);
	}

	private void addListeners() {
		val.addSelectionListener(spinerSelection);
		for (SelectionListener sl : slisteners)
			val.addSelectionListener(sl);
		val.addModifyListener(spinerModify);
		for (ModifyListener ml : mlisteners)
			val.addModifyListener(ml);
	}

	private List<SelectionListener> slisteners = new ArrayList<SelectionListener>();
	private List<ModifyListener> mlisteners = new ArrayList<ModifyListener>();

	public void addSelectionListener(SelectionListener listener) {
		slisteners.add(listener);
		val.addSelectionListener(listener);
	}

	public void removeSelectionListener(ModifyListener listener) {
		slisteners.remove(listener);
		val.removeModifyListener(listener);
	}

	public void addModifyListener(ModifyListener listener) {
		mlisteners.add(listener);
		val.addModifyListener(listener);
	}

	public void removeModifyListener(SelectionListener listener) {
		mlisteners.remove(listener);
		val.removeSelectionListener(listener);
	}

	public void setUnit(String u) {
		if (unit.setUnit(u)) {
			unitc.select(Unit.getUnitIndex(u));
			setSpinerValue(u);
		}
	}

	public int getValue() {
		return unit.getPxValue();
	}

	public String getUnit() {
		return unit.getUnit();
	}

	public void setValue(int px) {
		unit.setValue(px, Unit.PX);
		setSpinerValue(unit.getUnit());
	}
}
