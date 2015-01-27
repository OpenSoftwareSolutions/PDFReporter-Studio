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

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRStaticText;
import net.sf.jasperreports.engine.base.JRBaseStaticText;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStaticText;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;

/*
 * The Class MStaticText.
 */
public class MStaticText extends MTextElement {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("statictext"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m static text.
	 */
	public MStaticText() {
		super();
	}

	@Override
	public HashMap<String, Object> getStylesDescriptors() {
		HashMap<String, Object> result = super.getStylesDescriptors();
		if (getValue() == null)
			return result;
		JRStaticText element = (JRStaticText) getValue();
		result.put(JRDesignStyle.PROPERTY_FONT_NAME, element.getOwnFontName());
		result.put(JRDesignStyle.PROPERTY_HORIZONTAL_ALIGNMENT, element.getOwnHorizontalAlignmentValue());
		result.put(JRDesignStyle.PROPERTY_MARKUP, element.getOwnMarkup());
		result.put(JRDesignStyle.PROPERTY_VERTICAL_ALIGNMENT, element.getOwnVerticalAlignmentValue());
		return result;
	}

	/**
	 * Instantiates a new m static text.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrStaticText
	 *          the jr static text
	 * @param newIndex
	 *          the new index
	 */
	public MStaticText(ANode parent, JRStaticText jrStaticText, int newIndex) {
		super(parent, newIndex);
		setValue(jrStaticText);
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

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

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		NTextPropertyDescriptor textD = new NTextPropertyDescriptor(JRBaseStaticText.PROPERTY_TEXT, Messages.common_text);
		desc.add(textD);
		textD.setCategory(Messages.MStaticText_text_description);

		textD
				.setHelpRefBuilder(new HelpReferenceBuilder("net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#text"));
	}

	@Override
	public Object getPropertyActualValue(Object id) {
		return super.getPropertyActualValue(id);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignStaticText jrElement = (JRDesignStaticText) getValue();
		if (id.equals(JRBaseStaticText.PROPERTY_TEXT))
			return jrElement.getText();
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignStaticText jrElement = (JRDesignStaticText) getValue();

		if (id.equals(JRBaseStaticText.PROPERTY_TEXT)){
			jrElement.setText((String) value);
		}	else
			super.setPropertyValue(id, value);
	}



	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#createJRElement(net.sf.jasperreports.engine.design.JasperDesign)
	 */
	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignStaticText jrDesignStaticText = new JRDesignStaticText();
		jrDesignStaticText.setText(Messages.common_static_text);

		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrDesignStaticText);

		return jrDesignStaticText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		if (getValue() != null) {
			return ((JRStaticText) getValue()).getText();
		}
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}
	
	/**
	 * Return the graphical properties for an MStaticText
	 */
	public HashSet<String> generateGraphicalProperties(){
		HashSet<String> result = super.generateGraphicalProperties();
		result.add(JRBaseStaticText.PROPERTY_TEXT);
		return result;
	}
}
