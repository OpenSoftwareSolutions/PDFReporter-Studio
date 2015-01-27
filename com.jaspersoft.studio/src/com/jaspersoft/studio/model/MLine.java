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
import net.sf.jasperreports.engine.JRLine;
import net.sf.jasperreports.engine.base.JRBaseLine;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignLine;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.LineDirectionEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.EnumHelper;

/*
 * The Class MLine.
 */
public class MLine extends MGraphicElementLinePen {
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
			iconDescriptor = new NodeIconDescriptor("line"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m line.
	 */
	public MLine() {
		super();
	}

	/**
	 * Instantiates a new m line.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrLine
	 *          the jr line
	 * @param newImage
	 *          the new image
	 */
	public MLine(ANode parent, JRDesignLine jrLine, int newImage) {
		super(parent, newImage);
		setValue(jrLine);
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSEnumPropertyDescriptor directionD;
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

		directionD = new JSSEnumPropertyDescriptor(JRBaseLine.PROPERTY_DIRECTION, Messages.MLine_direction,
				LineDirectionEnum.class, NullEnum.NULL);
		directionD.setDescription(Messages.MLine_direction_description);
		directionD.setCategory(Messages.MLine_line_category);
		desc.add(directionD);

		/*
		 * fillD = new JSSEnumPropertyDescriptor(JRBaseStyle.PROPERTY_FILL, Messages.common_fill, FillEnum.class,
		 * NullEnum.INHERITED); fillD.setDescription(Messages.MLine_fill_description); desc.add(fillD);
		 */

		defaultsMap.put(JRBaseLine.PROPERTY_DIRECTION, EnumHelper.getValue(LineDirectionEnum.TOP_DOWN, 1, true));
		defaultsMap.put(JRBaseStyle.PROPERTY_FILL, null);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#line");
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignLine jrElement = (JRDesignLine) getValue();
		if (id.equals(JRBaseLine.PROPERTY_DIRECTION))
			return directionD.getEnumValue(jrElement.getDirectionValue());
		if (id.equals(JRBaseStyle.PROPERTY_FILL))
			return fillD.getEnumValue(jrElement.getOwnFillValue());
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignLine jrElement = (JRDesignLine) getValue();
		if (id.equals(JRBaseLine.PROPERTY_DIRECTION))
			jrElement.setDirection((LineDirectionEnum) directionD.getEnumValue(value));
		if (id.equals(JRBaseStyle.PROPERTY_FILL))
			jrElement.setFill((FillEnum) fillD.getEnumValue(value));
		else
			super.setPropertyValue(id, value);
	}

	@Override
	public int getDefaultHeight() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_HEIGHT);
		return defaultValue != null ? (Integer)defaultValue : 30;
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
		JRDesignLine jrDesignLine = new JRDesignLine(jasperDesign);

		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrDesignLine);

		jrDesignLine.setWidth(getDefaultWidth());
		jrDesignLine.setHeight(getDefaultHeight());
		return jrDesignLine;
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
		result.add(JRBaseLine.PROPERTY_DIRECTION);
		result.add(JRBaseStyle.PROPERTY_FILL);
		return result;
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRLine jrSource = (JRLine) getValue();
		if (jrSource != null){
			JRLine jrTarget = (JRLine)target;
			jrTarget.setFill(jrSource.getOwnFillValue());
			jrTarget.setDirection(jrSource.getDirectionValue());
		}
	}
	
}
