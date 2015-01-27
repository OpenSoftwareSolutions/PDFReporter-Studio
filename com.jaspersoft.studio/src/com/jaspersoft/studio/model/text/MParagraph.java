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
package com.jaspersoft.studio.model.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.TabStop;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.type.LineSpacingEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.tabstops.TabStopsPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.FloatPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

public class MParagraph extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public MParagraph(ANode parent, JRBaseParagraph bParagraph) {
		super();
		setJasperConfiguration(parent.getJasperConfiguration()); 
		setValue(bParagraph);
	}

	
	/*@Override
	public HashMap<String,Object> getStylesDescriptors() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (getValue() == null)
			return result;
		JRBaseParagraph jrElement = (JRBaseParagraph) getValue();
		result.put(JRBaseParagraph.PROPERTY_SPACING_BEFORE, jrElement.getOwnSpacingBefore());
		result.put(JRBaseParagraph.PROPERTY_SPACING_AFTER, jrElement.getOwnSpacingAfter());
		result.put(JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT, jrElement.getOwnFirstLineIndent());
		result.put(JRBaseParagraph.PROPERTY_LEFT_INDENT, jrElement.getOwnLeftIndent());
		//result.put(JRBaseParagraph.PROPERTY_LINE_SPACING, jrElement.getOwnLineSpacing());
		result.put(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE, jrElement.getOwnLineSpacingSize());
		result.put(JRBaseParagraph.PROPERTY_RIGHT_INDENT, jrElement.getOwnRightIndent());
		result.put(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH, jrElement.getOwnTabStopWidth());
		return result;
	}*/


	
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		lineSpacingD = new JSSEnumPropertyDescriptor(JRBaseParagraph.PROPERTY_LINE_SPACING, Messages.common_line_spacing,
				LineSpacingEnum.class, NullEnum.INHERITED);
		lineSpacingD.setDescription(Messages.MTextElement_line_spacing_description);
		desc.add(lineSpacingD);

		FloatPropertyDescriptor lineSpacingSize = new FloatPropertyDescriptor(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE,
				Messages.MParagraph_lineSpacingSizeTitle);
		lineSpacingSize.setDescription(Messages.MParagraph_lineSpacingSizeDescription);
		desc.add(lineSpacingSize);

		PixelPropertyDescriptor firstLineIdent = new PixelPropertyDescriptor(
				JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT, Messages.MParagraph_firstIdentTitle);
		firstLineIdent.setDescription(Messages.MParagraph_firstIdentDescription);
		desc.add(firstLineIdent);

		PixelPropertyDescriptor leftIdent = new PixelPropertyDescriptor(JRBaseParagraph.PROPERTY_LEFT_INDENT,
				Messages.MParagraph_leftIdentTitle);
		leftIdent.setDescription(Messages.MParagraph_leftIdentDescription);
		desc.add(leftIdent);

		PixelPropertyDescriptor rightIdent = new PixelPropertyDescriptor(JRBaseParagraph.PROPERTY_RIGHT_INDENT,
				Messages.MParagraph_rightIdentTitle);
		rightIdent.setDescription(Messages.MParagraph_rightIdentDescription);
		desc.add(rightIdent);

		PixelPropertyDescriptor spacingBefore = new PixelPropertyDescriptor(JRBaseParagraph.PROPERTY_SPACING_BEFORE,
				Messages.MParagraph_spacingBeforeTitle);
		spacingBefore.setDescription(Messages.MParagraph_spacingBeforeDescription);
		desc.add(spacingBefore);

		PixelPropertyDescriptor spacingAfter = new PixelPropertyDescriptor(JRBaseParagraph.PROPERTY_SPACING_AFTER,
				Messages.MParagraph_spacingAfterTitle);
		spacingAfter.setDescription(Messages.MParagraph_spacingAfterDescription);
		desc.add(spacingAfter);

		PixelPropertyDescriptor tabStopWidth = new PixelPropertyDescriptor(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH,
				Messages.MParagraph_tabStopWidthTitle);
		tabStopWidth.setDescription(Messages.MParagraph_tabStopWidthDescription);
		desc.add(tabStopWidth);

		TabStopsPropertyDescriptor tabStops = new TabStopsPropertyDescriptor(JRBaseParagraph.PROPERTY_TAB_STOPS,
				Messages.MParagraph_tabStopsTitle);
		tabStops.setDescription(Messages.MParagraph_tabStopsDescription);
		desc.add(tabStops);

		tabStops.setCategory("Paragraph"); //$NON-NLS-1$
		lineSpacingD.setCategory("Paragraph"); //$NON-NLS-1$
		lineSpacingSize.setCategory("Paragraph"); //$NON-NLS-1$
		firstLineIdent.setCategory("Paragraph"); //$NON-NLS-1$
		leftIdent.setCategory("Paragraph"); //$NON-NLS-1$
		rightIdent.setCategory("Paragraph"); //$NON-NLS-1$
		spacingBefore.setCategory("Paragraph"); //$NON-NLS-1$
		spacingAfter.setCategory("Paragraph"); //$NON-NLS-1$
		tabStopWidth.setCategory("Paragraph"); //$NON-NLS-1$

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#paragraph"); //$NON-NLS-1$
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSEnumPropertyDescriptor lineSpacingD;

	@Override
	public Map<String, Object> getDefaultsMap() {
		return defaultsMap;
	}

	@Override
	public IPropertyDescriptor[] getDescriptors() {
		return descriptors;
	}

	@Override
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}
	
	
	public Object getPropertyActualValue(Object id) {
		JRBaseParagraph jrElement = (JRBaseParagraph) getValue();
		if (jrElement != null) {
			if (id.equals(JRBaseParagraph.PROPERTY_LINE_SPACING))
				return lineSpacingD.getEnumValue(jrElement.getLineSpacing());
			if (id.equals(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE))
				return jrElement.getLineSpacingSize();

			if (id.equals(JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT))
				return jrElement.getFirstLineIndent();

			if (id.equals(JRBaseParagraph.PROPERTY_LEFT_INDENT))
				return jrElement.getLeftIndent();
			if (id.equals(JRBaseParagraph.PROPERTY_RIGHT_INDENT))
				return jrElement.getRightIndent();

			if (id.equals(JRBaseParagraph.PROPERTY_SPACING_BEFORE))
				return jrElement.getSpacingBefore();
			if (id.equals(JRBaseParagraph.PROPERTY_SPACING_AFTER))
				return jrElement.getSpacingAfter();
			if (id.equals(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH))
				return jrElement.getTabStopWidth();
		}
		return super.getPropertyActualValue(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRBaseParagraph jrElement = (JRBaseParagraph) getValue();
		if (jrElement != null) {
			if (id.equals(JRBaseParagraph.PROPERTY_LINE_SPACING))
				return lineSpacingD.getEnumValue(jrElement.getOwnLineSpacing());
			if (id.equals(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE))
				return jrElement.getOwnLineSpacingSize();

			if (id.equals(JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT))
				return jrElement.getOwnFirstLineIndent();

			if (id.equals(JRBaseParagraph.PROPERTY_LEFT_INDENT))
				return jrElement.getOwnLeftIndent();
			if (id.equals(JRBaseParagraph.PROPERTY_RIGHT_INDENT))
				return jrElement.getOwnRightIndent();

			if (id.equals(JRBaseParagraph.PROPERTY_SPACING_BEFORE))
				return jrElement.getOwnSpacingBefore();
			if (id.equals(JRBaseParagraph.PROPERTY_SPACING_AFTER))
				return jrElement.getOwnSpacingAfter();
			if (id.equals(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH))
				return jrElement.getOwnTabStopWidth();
			if (id.equals(JRBaseParagraph.PROPERTY_TAB_STOPS)) {
				TabStop[] tabStops = jrElement.getTabStops();
				if (tabStops != null)
					return Arrays.asList(tabStops);
				return new ArrayList<TabStop>();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRBaseParagraph jrElement = (JRBaseParagraph) getValue();
		if (jrElement != null) {
			if (id.equals(JRBaseParagraph.PROPERTY_LINE_SPACING))
				jrElement.setLineSpacing((LineSpacingEnum) lineSpacingD.getEnumValue(value));
			if (id.equals(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE))
				jrElement.setLineSpacingSize((Float) value);
			if (id.equals(JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT))
				jrElement.setFirstLineIndent((Integer) value);
			if (id.equals(JRBaseParagraph.PROPERTY_LEFT_INDENT))
				jrElement.setLeftIndent((Integer) value);
			if (id.equals(JRBaseParagraph.PROPERTY_RIGHT_INDENT))
				jrElement.setRightIndent((Integer) value);

			if (id.equals(JRBaseParagraph.PROPERTY_SPACING_BEFORE))
				jrElement.setSpacingBefore((Integer) value);
			if (id.equals(JRBaseParagraph.PROPERTY_SPACING_AFTER))
				jrElement.setSpacingAfter((Integer) value);

			if (id.equals(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH))
				jrElement.setTabStopWidth((Integer) value);
			if (id.equals(JRBaseParagraph.PROPERTY_TAB_STOPS)) {
				jrElement.addTabStop(null);
			}
		}
	}

	public String getDisplayText() {
		return null;
	}

	public ImageDescriptor getImagePath() {
		return null;
	}

}
