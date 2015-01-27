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
package com.jaspersoft.studio.property.descriptor.pattern.dialog;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;

public class NumericPattern extends APattern {

	private Spinner zeroes;
	
	private Spinner decimals;
	
	private Button sep;
	
	private boolean isRefreshing = false;
	
	public NumericPattern(Composite parent, String value) {
		this(parent, NumberFormat.getNumberInstance(), value);
	}

	public NumericPattern(Composite parent, Format formatter, String value) {
		super(parent, formatter, new BigDecimal("-10023.1234567654"), value); //$NON-NLS-1$
		setDescription(Messages.NumericPattern_description);
	}
	
	@Override
	public void setValue(String value) {
		super.setValue(value);
		backparsePattern();
	}
	
	private void backparsePattern(){
		if (!value.isEmpty()){
			isRefreshing = true;
			try{
				DecimalFormat backParser = new DecimalFormat(value);
				zeroes.setSelection(backParser.getMinimumIntegerDigits());
				decimals.setSelection(backParser.getMinimumFractionDigits());
				sep.setSelection(backParser.isGroupingUsed() && backParser.getGroupingSize() == 3);
				setFormatter(backParser);
				setPattern(backParser.toPattern());
				formatChanged();
			} catch (Exception ex){
				//The pattern couldn't be parsed, log the exception but not print it to the console
				JaspersoftStudioPlugin.getInstance().logError(ex);
			}
			isRefreshing = false;
		}
	}
	
	

	@Override
	public Control createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, true));
		Label lab = new Label(container, SWT.NONE | SWT.CENTER);
		lab.setText(Messages.NumericPattern_leading_zeroes);

		lab = new Label(container, SWT.NONE | SWT.CENTER);
		lab.setText(Messages.NumericPattern_decimal_places + ":");

		zeroes = new Spinner(container, SWT.BORDER);
		zeroes.setMinimum(0);
		zeroes.setMaximum(100);
		zeroes.setSelection(1);
		zeroes.setIncrement(1);
		zeroes.setPageIncrement(10);
		zeroes.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		decimals = new Spinner(container, SWT.BORDER);
		decimals.setMinimum(0);
		decimals.setMaximum(100);
		decimals.setSelection(2);
		decimals.setIncrement(1);
		decimals.setPageIncrement(10);
		decimals.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		sep = new Button(container, SWT.CHECK);
		sep.setText(Messages.NumericPattern_use_1000_sperator);
		GridData gd = new GridData();
		gd.horizontalSpan = 2;
		sep.setLayoutData(gd);

		list = new List(container, SWT.BORDER);
		gd = new GridData(GridData.FILL_BOTH | GridData.GRAB_VERTICAL);
		gd.horizontalSpan = 2;
		gd.heightHint = 200;
		gd.widthHint = 100;
		list.setLayoutData(gd);
	

		DecimalFormat f = (DecimalFormat) getFormatter();
		for (String s : getDefaults()) {
			f.applyPattern(s);
			list.add(f.format(getSample()));
		}
		
		backparsePattern();
		
		sep.addSelectionListener(new SelectionListener() {
			
			public void widgetSelected(SelectionEvent e) {
				if (!isRefreshing){
					DecimalFormat d = (DecimalFormat) getFormatter();
					d.setGroupingUsed(((Button)e.widget).getSelection());
					d.setGroupingSize(3);
					setPattern(d.toPattern());
					value = d.toPattern();
					formatChanged();
				}
			}

			public void widgetDefaultSelected(SelectionEvent e) {
			}
		});
		decimals.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!isRefreshing){
					DecimalFormat d = (DecimalFormat) getFormatter();
					d.setMinimumFractionDigits(decimals.getSelection());
					setPattern(d.toPattern());
					value = d.toPattern();
					formatChanged();
				}
			}
		});
		zeroes.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				if (!isRefreshing){
					DecimalFormat d = (DecimalFormat) getFormatter();
					d.setMinimumIntegerDigits(zeroes.getSelection());
					setPattern(d.toPattern());
					value = d.toPattern();
					formatChanged();
				}
			}
		});
		list.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				int sel = list.getSelectionIndex();
				if (sel >= 0) {
					setPattern(dList.get(sel));
				}
				formatChanged();
				DecimalFormat d = (DecimalFormat) getFormatter();
				sep.setSelection(d.isGroupingUsed());
				zeroes.setSelection(d.getMinimumIntegerDigits());
				decimals.setSelection(d.getMinimumFractionDigits());
			}

			public void widgetDefaultSelected(SelectionEvent e) {

			}
		});
		return container;
	}
	
	protected java.util.List<String> dList;
	protected List list;

	protected java.util.List<String> getDefaults() {
		if (dList == null) {
			dList = new ArrayList<String>();
			dList.add("#,##0.###;#,-##0.###"); //$NON-NLS-1$
			dList.add("#,##0.###;#,##0.###-"); //$NON-NLS-1$
			dList.add("#,##0.###;(#,##0.###)"); //$NON-NLS-1$
			dList.add("#,##0.###;(-#,##0.###)"); //$NON-NLS-1$
			dList.add("#,##0.###;(#,##0.###-)"); //$NON-NLS-1$
			setPattern(dList.get(0));
		}
		return dList;
	}

}
