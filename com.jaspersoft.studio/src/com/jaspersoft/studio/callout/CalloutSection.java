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
package com.jaspersoft.studio.callout;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;

public class CalloutSection extends AbstractSection {
	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(4, false));

		createWidget4Property(parent, MCallout.PROP_FOREGROUND);
		createWidget4Property(parent, MCallout.PROP_BACKGROUND);
		ASPropertyWidget w = createWidget4Property(parent, MCallout.PROP_TEXT);
		GridData gd = new GridData();
		gd.horizontalSpan = 4;
		w.getLabel().setLayoutData(gd);

		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 4;
		gd.heightHint = 100;
		w.getControl().setLayoutData(gd);
	}

	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(MCallout.PROP_FOREGROUND, Messages.common_forecolor);
		addProvidedProperties(MCallout.PROP_BACKGROUND, Messages.common_backcolor);
		addProvidedProperties(MCallout.PROP_TEXT, "Callout Text");
	}
}
