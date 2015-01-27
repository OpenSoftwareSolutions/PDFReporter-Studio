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
package com.jaspersoft.studio.property.section.widgets;

import org.eclipse.jface.fieldassist.AutoCompleteField;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.utils.inputhistory.InputHistoryCache;

public abstract class AHistorySPropertyWidget extends ASPropertyWidget {
	public static final String HIST_PREFIX = "asproperty.";
	protected AutoCompleteField autocomplete;

	public AHistorySPropertyWidget(Composite parent, AbstractSection section, IPropertyDescriptor pDescriptor) {
		super(parent, section, pDescriptor);
	}

	protected abstract Text getTextControl();

	protected Object getHistoryKey() {
		return HIST_PREFIX + pDescriptor.getId();
	}

	@Override
	protected void handleFocusLost() {
		if (autocomplete != null) {
			autocomplete.setProposals(InputHistoryCache.get(null));
			InputHistoryCache.put(getHistoryKey(), getTextControl().getText());
		}
		super.handleFocusLost();
	}

	@Override
	protected void handleFocusGained() {
		if (autocomplete != null)
			autocomplete.setProposals(InputHistoryCache.get(getHistoryKey()));
		super.handleFocusGained();
	}

}
