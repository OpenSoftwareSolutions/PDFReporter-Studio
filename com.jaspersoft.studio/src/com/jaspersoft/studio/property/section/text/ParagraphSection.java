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
package com.jaspersoft.studio.property.section.text;

import net.sf.jasperreports.engine.base.JRBaseParagraph;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.ExpandableComposite;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.text.MParagraph;
import com.jaspersoft.studio.model.text.MTextElement;
import com.jaspersoft.studio.properties.view.TabbedPropertySheetPage;
import com.jaspersoft.studio.property.section.AbstractRealValueSection;

/*
 * The location section on the location tab.
 * 
 * @author Chicu Veaceslav
 */
public class ParagraphSection extends AbstractRealValueSection {
	
	private ExpandableComposite section;

	/**
	 * @see org.eclipse.ui.views.properties.tabbed.ITabbedPropertySection#createControls(org.eclipse.swt.widgets.Composite,
	 *      org.eclipse.ui.views.properties.tabbed.TabbedPropertySheetPage)
	 */
	public void createControls(Composite parent, TabbedPropertySheetPage tabbedPropertySheetPage) {
		super.createControls(parent, tabbedPropertySheetPage);

		parent = getWidgetFactory().createSection(parent, "Paragraph", true, 2);
		section = (ExpandableComposite)parent.getParent();
		
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_LINE_SPACING);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_LEFT_INDENT);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_RIGHT_INDENT);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_SPACING_BEFORE);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_SPACING_AFTER);
		createWidget4Property(parent, JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH);
	}
	
	@Override
	protected void initializeProvidedProperties() {
		super.initializeProvidedProperties();
		addProvidedProperties(JRBaseParagraph.PROPERTY_LINE_SPACING, Messages.common_line_spacing);
		addProvidedProperties(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE, Messages.MParagraph_lineSpacingSizeTitle);
		addProvidedProperties(JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT, Messages.MParagraph_firstIdentTitle);
		addProvidedProperties(JRBaseParagraph.PROPERTY_LEFT_INDENT, Messages.MParagraph_leftIdentTitle);
		addProvidedProperties(JRBaseParagraph.PROPERTY_SPACING_BEFORE, Messages.MParagraph_spacingBeforeTitle);
		addProvidedProperties(JRBaseParagraph.PROPERTY_SPACING_AFTER, Messages.MParagraph_spacingAfterTitle);
		addProvidedProperties(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH, Messages.MParagraph_tabStopWidthTitle);
		addProvidedProperties(JRBaseParagraph.PROPERTY_RIGHT_INDENT, Messages.MParagraph_rightIdentTitle);
	}

	@Override
	public void expandForProperty(Object propertyId) {
		if (section != null && !section.isExpanded()) section.setExpanded(true);
	}
	
	@Override
	protected APropertyNode getModelFromEditPart(Object item) {
		APropertyNode md = super.getModelFromEditPart(item);
		if (md instanceof MTextElement) {
			MParagraph paragraph = (MParagraph) md.getPropertyValue("paragraph");
			return paragraph;
		}
		return md;
	}

}
