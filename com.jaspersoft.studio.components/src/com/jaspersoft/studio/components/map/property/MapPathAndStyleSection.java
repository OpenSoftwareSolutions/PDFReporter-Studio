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
package com.jaspersoft.studio.components.map.property;

import net.sf.jasperreports.components.map.StandardMapComponent;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractSection;

/**
 * This section should be used to customize the list of map paths and related styles.
 *  
 * @author Massimo Rabbi (mrabbi@users.sourceforge.net)
 *
 */
public class MapPathAndStyleSection extends AbstractSection {
	
	@Override
	public void createControls(Composite parent,
			TabbedPropertySheetPage aTabbedPropertySheetPage) {
		super.createControls(parent, aTabbedPropertySheetPage);
		parent.setLayout(new GridLayout(1,false));
		createWidget4Property(parent, StandardMapComponent.PROPERTY_PATH_DATA_LIST,false);
		createWidget4Property(parent, StandardMapComponent.PROPERTY_PATH_STYLE_LIST,false);
	}

}
