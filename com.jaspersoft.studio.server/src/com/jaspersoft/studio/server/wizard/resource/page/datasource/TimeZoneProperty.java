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
package com.jaspersoft.studio.server.wizard.resource.page.datasource;

import java.util.TimeZone;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.restv2.DiffFields;
import com.jaspersoft.studio.swt.widgets.WTimeZone;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.UIUtil;

public class TimeZoneProperty {

	public static void addTimeZone(MResource res, Composite composite) {
		final ResourceDescriptor rd = res.getValue();
		if (res.isSupported(Feature.TIMEZONE)) {
			UIUtil.createLabel(composite, "Time Zone");
			final WTimeZone tzone = new WTimeZone(composite, SWT.NONE);
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			// gd.horizontalSpan = 2;
			tzone.setLayoutData(gd);
			String stz = DiffFields.getSoapValue(rd, DiffFields.TIMEZONE);
			if (!Misc.isNullOrEmpty(stz))
				tzone.setSelection(TimeZone.getTimeZone(stz));
			tzone.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					TimeZone timeZone = tzone.getTimeZone();
					String v = timeZone != null ? timeZone.getID() : null;
					DiffFields.setSoapValue(rd, DiffFields.TIMEZONE, v);
				}
			});
			tzone.addModifyListener(new ModifyListener() {

				@Override
				public void modifyText(ModifyEvent e) {
					TimeZone timeZone = tzone.getTimeZone();
					String v = timeZone != null ? timeZone.getID() : null;
					DiffFields.setSoapValue(rd, DiffFields.TIMEZONE, v);
				}
			});
		}
	}

}
