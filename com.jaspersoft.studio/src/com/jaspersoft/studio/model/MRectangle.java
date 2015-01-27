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
package com.jaspersoft.studio.model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRRectangle;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignRectangle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.FillEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

/*
 * The Class MRectangle.
 */
public class MRectangle extends MGraphicElementLinePen {
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
			iconDescriptor = new NodeIconDescriptor("rectangle"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m rectangle.
	 */
	public MRectangle() {
		super();
	}

	/**
	 * Instantiates a new m rectangle.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrRectangle
	 *          the jr rectangle
	 * @param newImage
	 *          the new image
	 */
	public MRectangle(ANode parent, JRDesignRectangle jrRectangle, int newImage) {
		super(parent, newImage);
		setValue(jrRectangle);
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSEnumPropertyDescriptor fillD;

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

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		fillD = new JSSEnumPropertyDescriptor(JRBaseStyle.PROPERTY_FILL, Messages.common_fill, FillEnum.class,
				NullEnum.INHERITED);
		fillD.setDescription(Messages.MRectangle_fill_description);
		desc.add(fillD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#graphicElement");

		IntegerPropertyDescriptor rD = new IntegerPropertyDescriptor(JRBaseStyle.PROPERTY_RADIUS, Messages.common_radius);
		rD.setCategory(Messages.MRectangle_rectangle_properties_category);
		rD.setDescription(Messages.MRectangle_radius_description);
		desc.add(rD);

		defaultsMap.put(JRBaseStyle.PROPERTY_FILL, null);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#rectangle");
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignRectangle jrElement = (JRDesignRectangle) getValue();
		if (id.equals(JRBaseStyle.PROPERTY_RADIUS))
			return jrElement.getOwnRadius();
		if (id.equals(JRBaseStyle.PROPERTY_FILL))
			return fillD.getEnumValue(jrElement.getFillValue());
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignRectangle jrElement = (JRDesignRectangle) getValue();
		if (id.equals(JRBaseStyle.PROPERTY_FILL))
			jrElement.setFill((FillEnum) fillD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_RADIUS)) {
			jrElement.setRadius(value != null ? ((Integer) value).intValue() : 0);

		} else
			super.setPropertyValue(id, value);
	}

	@Override
	public int getDefaultHeight() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_HEIGHT);
		return defaultValue != null ? (Integer)defaultValue : 50;
	}

	@Override
	public int getDefaultWidth() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_WIDTH);
		return defaultValue != null ? (Integer)defaultValue : 100;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#createJRElement(net.sf.jasperreports.engine.design.JasperDesign)
	 */
	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignRectangle jrDesignRectangle = new JRDesignRectangle(jasperDesign);
		
		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrDesignRectangle);

		jrDesignRectangle.setWidth(getDefaultWidth());
		jrDesignRectangle.setHeight(getDefaultHeight());
		return jrDesignRectangle;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
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

	public HashSet<String> generateGraphicalProperties(){
		HashSet<String> result = super.generateGraphicalProperties();
		result.add(JRBaseStyle.PROPERTY_FILL);
		result.add(JRBaseStyle.PROPERTY_RADIUS);
		return result;
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRRectangle jrSource = (JRRectangle) getValue();
		if (jrSource != null){
			JRRectangle jrTarget = (JRRectangle)target;
			jrTarget.setFill(jrSource.getOwnFillValue());
			jrTarget.setRadius(jrSource.getOwnRadius());
		}
	}
}
