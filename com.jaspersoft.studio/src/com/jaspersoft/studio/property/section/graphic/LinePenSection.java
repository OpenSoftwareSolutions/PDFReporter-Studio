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
package com.jaspersoft.studio.property.section.graphic;

import net.sf.jasperreports.engine.base.JRBasePen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.MGraphicElementLinePen;
import com.jaspersoft.studio.model.style.MStyle;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class LinePenSection extends AbstractRealValueSection {
	
	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode model = super.getModelFromEditPart(item);
		if (model != null && model instanceof MGraphicElementLinePen || model instanceof MStyle){
			boolean editable = model.isEditable();
			model = (APropertyNode) model.getPropertyValue(MGraphicElementLinePen.LINE_PEN);
			model.setEditable(editable);
		}
		return model;
	}

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent.setLayout(new GridLayout(2, false));

		Group panel = new Group(parent, SWT.NONE);
		panel.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));
		panel.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false));
		panel.setLayout(new GridLayout(2,false));
		panel.setText(Messages.LinePenSection_lineSectionName);
		
		createWidget4Property(panel, JRBasePen.PROPERTY_LINE_COLOR);
		createWidget4Property(panel, JRBasePen.PROPERTY_LINE_STYLE);
		createWidget4Property(panel, JRBasePen.PROPERTY_LINE_WIDTH);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBasePen.PROPERTY_LINE_COLOR, Messages.common_line_color);
		addProvidedProperties(JRBasePen.PROPERTY_LINE_STYLE, Messages.common_line_style);
		addProvidedProperties(JRBasePen.PROPERTY_LINE_WIDTH, Messages.MLinePen_line_width);
	}

}
