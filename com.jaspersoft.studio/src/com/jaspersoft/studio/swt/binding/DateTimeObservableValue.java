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
package com.jaspersoft.studio.swt.binding;

import java.util.Calendar;
import java.util.Date;

import org.eclipse.core.databinding.observable.Diffs;
import org.eclipse.core.databinding.observable.value.AbstractObservableValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public class DateTimeObservableValue extends AbstractObservableValue {

	private final DateTime dateTime;

	protected Date oldValue;

	Listener listener = new Listener() {

		public void handleEvent(final Event event) {
			Date newValue = dateTimeToDate();

			if (!newValue.equals(DateTimeObservableValue.this.oldValue)) {
				fireValueChange(Diffs.createValueDiff(
						DateTimeObservableValue.this.oldValue, newValue));
				DateTimeObservableValue.this.oldValue = newValue;

			}
		}

	};

	public DateTimeObservableValue(final DateTime dateTime) {
		this.dateTime = dateTime;
		this.dateTime.addListener(SWT.Selection, this.listener);
	}

	@Override
	protected Object doGetValue() {
		return dateTimeToDate();
	}

	@Override
	protected void doSetValue(final Object value) {
		if (value instanceof Date) {
			Date date = (Date) value;
			dateToDateTime(date);
		}
	}

	public Object getValueType() {
		return Date.class;
	}

	private void dateToDateTime(final Date date) {
		if (!this.dateTime.isDisposed()) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			this.dateTime.setYear(cal.get(Calendar.YEAR));
			this.dateTime.setMonth(cal.get(Calendar.MONTH));
			this.dateTime.setDay(cal.get(Calendar.DAY_OF_MONTH));
			this.dateTime.setHours(cal.get(Calendar.HOUR_OF_DAY));
			this.dateTime.setMinutes(cal.get(Calendar.MINUTE));
			this.dateTime.setSeconds(cal.get(Calendar.SECOND));
		}
	}

	private Date dateTimeToDate() {
		Date result = null;
		if (!this.dateTime.isDisposed()) {
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, this.dateTime.getYear());
			cal.set(Calendar.MONTH, this.dateTime.getMonth());
			cal.set(Calendar.DAY_OF_MONTH, this.dateTime.getDay());
			cal.set(Calendar.HOUR_OF_DAY, this.dateTime.getHours());
			cal.set(Calendar.MINUTE, this.dateTime.getMinutes());
			cal.set(Calendar.SECOND, this.dateTime.getSeconds());
			result = cal.getTime();
		}
		return result;
	}

	@Override
	public synchronized void dispose() {
		this.dateTime.removeListener(SWT.Selection, this.listener);
		super.dispose();
	}

}
