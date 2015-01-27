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
package com.jaspersoft.studio.server.editor.input;

import java.util.Date;
import java.util.Map;

import net.sf.jasperreports.types.date.DateRange;
import net.sf.jasperreports.types.date.TimestampRange;

import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.editor.preview.input.IParameter;
import com.jaspersoft.studio.editor.preview.view.control.VParameters;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.swt.widgets.DRDateTime;
import com.jaspersoft.studio.utils.Misc;

public class DateInput extends com.jaspersoft.studio.editor.preview.input.DateInput {
	public DateInput() {
		super(true, true);
	}

	@Override
	public void createInput(Composite parent, IParameter param, Map<String, Object> params) {
		if (param instanceof PResourceDescriptor) {
			PResourceDescriptor p = (PResourceDescriptor) param;
			ServerProfile sp = WSClientHelper.getServerProfile(p.getWsClient());
			if (sp != null)
				setSupportDateRange(sp.isSupportsDateRanges());
			isNumeric = !p.getWsClient().isSupported(Feature.SEARCHREPOSITORY);
		}
		this.params = params;
		this.param = param;
		Class<?> valueClass = param.getValueClass();
		if (java.sql.Date.class.isAssignableFrom(valueClass)) {
			if (supportDateRange)
				createDateRange(parent, param, params);
			else
				createDate(parent, param, params);
		} else if (java.sql.Time.class.isAssignableFrom(valueClass)) {
			createTime(parent, param, params);
		} else if (java.sql.Timestamp.class.isAssignableFrom(valueClass) || java.util.Date.class.isAssignableFrom(valueClass)) {
			if (supportDateRange)
				createTimestampRange(parent, param, params);
			else
				createTimestamp(parent, param, params);
		} else if (TimestampRange.class.isAssignableFrom(valueClass))
			createTimestampRange(parent, param, params);
		else if (DateRange.class.isAssignableFrom(valueClass))
			createDateRange(parent, param, params);
		date.setToolTipText(VParameters.createToolTip(param));
		date.addFocusListener(focusListener);
	}

	@Override
	protected void handleDateRangeChange(Class<? extends Date> clazz) {
		if (date.getSelection() != null) {
			Date d = date.getSelection();
			if (d != null)
				if (isNumeric)
					updateModel(d.getTime());
				else
					updateModel(d);
		} else {
			String ntxt = date.getNullText();
			if (ntxt.equals(DRDateTime.NULLTEXT))
				updateModel(null);
			else
				updateModel(Misc.nvl(ntxt.replaceAll(" ", "")).toUpperCase());

		}
	}
}
