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
package com.jaspersoft.studio.editor.preview.input;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.types.date.DateRange;
import net.sf.jasperreports.types.date.DateRangeBuilder;
import net.sf.jasperreports.types.date.DateRangeExpression;
import net.sf.jasperreports.types.date.InvalidDateRangeExpressionException;
import net.sf.jasperreports.types.date.TimestampRange;

import org.eclipse.nebula.widgets.cdatetime.CDT;
import org.eclipse.nebula.widgets.cdatetime.CDateTime;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.view.control.VParameters;
import com.jaspersoft.studio.swt.widgets.DRDateTime;
import com.jaspersoft.studio.utils.Misc;

public class DateInput extends ADataInput {
	protected boolean supportDateRange;

	public DateInput() {
		this(false, true);
	}

	public DateInput(boolean isNumeric, boolean supportDateRange) {
		this.isNumeric = isNumeric;
		this.supportDateRange = supportDateRange;
	}

	public boolean isForType(Class<?> valueClass) {
		return Date.class.isAssignableFrom(valueClass) || DateRange.class.isAssignableFrom(valueClass);
	}

	@Override
	public void createInput(Composite parent, final IParameter param, final Map<String, Object> params) {
		super.createInput(parent, param, params);
		Class<?> valueClass = param.getValueClass();
		if (java.sql.Date.class.isAssignableFrom(valueClass)) {
			createDate(parent, param, params);
		} else if (java.sql.Time.class.isAssignableFrom(valueClass)) {
			createTime(parent, param, params);
		} else if (java.sql.Timestamp.class.isAssignableFrom(valueClass)
				|| java.util.Date.class.isAssignableFrom(valueClass)) {
			createTimestamp(parent, param, params);
		} else if (TimestampRange.class.isAssignableFrom(valueClass))
			createTimestampRange(parent, param, params);
		else if (DateRange.class.isAssignableFrom(valueClass))
			createDateRange(parent, param, params);
		date.setToolTipText(VParameters.createToolTip(param));
		date.addFocusListener(focusListener);
	}

	protected void createTimestampRange(Composite parent, final IParameter param, final Map<String, Object> params) {
		date = new DRDateTime(parent, CDT.BORDER | CDT.DATE_SHORT | CDT.TIME_MEDIUM | CDT.DROP_DOWN);
		((DRDateTime) date).setSupportDateRange(supportDateRange);

		GridData gd = new GridData();
		gd.horizontalIndent = 8;
		gd.widthHint = 25 * getCharWidth(date);
		date.setLayoutData(gd);

		setMandatory(param, date);
		ModifyListener listener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				handleDateRangeChange(Timestamp.class);
			}
		};
		((DRDateTime) date).addModifyListener(listener);
		updateInput();
		listener.modifyText(null);
	}

	protected void handleDateRangeChange(Class<? extends Date> clazz) {
		try {
			DateRangeBuilder drb = null;
			if (date.getSelection() != null)
				drb = new DateRangeBuilder(date.getSelection());
			else
				drb = new DateRangeBuilder(Misc.nvl(date.getText().replaceAll(" ", "")).toUpperCase());
			updateModel(drb.set(clazz).toDateRange());
		} catch (InvalidDateRangeExpressionException dre) {
			// Date now = new Date();
			// if (Timestamp.class.isAssignableFrom(clazz))
			// now = new Timestamp(now.getTime());
			updateModel(null);
		}
	}

	protected void createDateRange(Composite parent, final IParameter param, final Map<String, Object> params) {
		date = new DRDateTime(parent, CDT.BORDER | CDT.DATE_SHORT | CDT.DROP_DOWN);
		((DRDateTime) date).setSupportDateRange(supportDateRange);

		GridData gd = new GridData();
		gd.horizontalIndent = 8;
		gd.widthHint = 25 * getCharWidth(date);
		date.setLayoutData(gd);

		setMandatory(param, date);

		ModifyListener listener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				handleDateRangeChange(Date.class);
			}
		};
		((DRDateTime) date).addModifyListener(listener);
		updateInput();
		listener.modifyText(null);
	}

	protected void createTimestamp(Composite parent, final IParameter param, final Map<String, Object> params) {
		date = new CDateTime(parent, CDT.BORDER | CDT.DATE_SHORT | CDT.TIME_MEDIUM | CDT.DROP_DOWN);

		GridData gd = new GridData();
		gd.horizontalIndent = 8;
		gd.widthHint = 25 * getCharWidth(date);
		date.setLayoutData(gd);

		setMandatory(param, date);
		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Date sdate = date.getSelection();
				Timestamp d = sdate != null ? new java.sql.Timestamp(sdate.getTime()) : null;
				updateModel(isNumeric ? d.getTime() : d);
			}
		};
		date.addSelectionListener(listener);
		updateInput();
		listener.widgetSelected(null);
	}

	protected void createTime(Composite parent, final IParameter param, final Map<String, Object> params) {
		date = new CDateTime(parent, CDT.BORDER | CDT.TIME_MEDIUM | CDT.DROP_DOWN);
		GridData gd = new GridData();
		gd.horizontalIndent = 8;
		gd.widthHint = 25 * getCharWidth(date);
		date.setLayoutData(gd);

		setMandatory(param, date);

		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Date sdate = date.getSelection();
				Time d = sdate != null ? new java.sql.Time(sdate.getTime()) : null;
				updateModel(isNumeric ? d.getTime() : d);
			}
		};
		date.addSelectionListener(listener);
		updateInput();
		listener.widgetSelected(null);
	}

	protected void createDate(Composite parent, final IParameter param, final Map<String, Object> params) {
		date = new CDateTime(parent, CDT.BORDER | CDT.DATE_SHORT | CDT.DROP_DOWN);
		GridData gd = new GridData();
		gd.horizontalIndent = 8;
		gd.widthHint = 25 * getCharWidth(date);
		date.setLayoutData(gd);

		setMandatory(param, date);

		updateInput();
		SelectionAdapter listener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				Date sdate = date.getSelection();
				Date d = sdate != null ? new java.sql.Date(sdate.getTime()) : null;
				updateModel(isNumeric && d != null ? d.getTime() : d);
			}
		};
		date.addSelectionListener(listener);
		listener.widgetSelected(null);
	}

	public void updateInput() {
		Object d = params.get(param.getName());
		if (d != null) {
			if (d instanceof String) {
				try {
					SimpleDateFormat sdf = new SimpleDateFormat(date.getPattern());
					sdf.parse((String) d);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
			if (d instanceof Date) {
				date.setSelection((Date) d);
			} else if (d instanceof Long) {
				date.setSelection(new Date((Long) d));
				isNumeric = true;
			} else if (d instanceof DateRange) {
				DateRange dr = (DateRange) d;
				if (dr instanceof DateRangeExpression) {
					String expr = ((DateRangeExpression) dr).getExpression();
					if (expr != null) {
						((DRDateTime) date).setText(expr);
						return;
					}
				}
				date.setSelection(dr.getStart());
			}
		} else {
			date.setSelection(null);
		}
	}

	protected boolean isNumeric = false;
	protected CDateTime date;

	public boolean isSupportDateRange() {
		return supportDateRange;
	}

	public void setSupportDateRange(boolean supportDateRange) {
		this.supportDateRange = supportDateRange;
	}

}
