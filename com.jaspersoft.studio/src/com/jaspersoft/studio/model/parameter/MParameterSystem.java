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
package com.jaspersoft.studio.model.parameter;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IDragable;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSValidatedTextPropertyDescriptor;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * The Class MParameterSystem.
 * 
 * @author Chicu Veaceslav
 */
public class MParameterSystem extends APropertyNode implements IDragable {
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
			iconDescriptor = new NodeIconDescriptor("parameter-report"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m parameter system.
	 */
	public MParameterSystem() {
		super();
	}

	/**
	 * Instantiates a new m parameter system.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrParameter
	 *          the jr parameter
	 * @param newIndex
	 *          the new index
	 */
	public MParameterSystem(ANode parent, JRDesignParameter jrParameter, int newIndex) {
		super(parent, newIndex);
		setValue(jrParameter);
	}

	@Override
	public JRDesignParameter getValue() {
		return (JRDesignParameter) super.getValue();
	}

	@Override
	public Color getForeground() {
		return ColorConstants.lightGray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return ((JRDesignParameter) getValue()).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static ParameterNameValidator validator;

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
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		//Set into the validator the actual reference
		validator.setTargetNode(this);
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		validator = new ParameterNameValidator();
		validator.setTargetNode(this);
		JSSValidatedTextPropertyDescriptor nameD = new JSSValidatedTextPropertyDescriptor(JRDesignParameter.PROPERTY_NAME, Messages.common_name, validator);
		nameD.setDescription(Messages.MParameterSystem_name_description);
		desc.add(nameD);

		NClassTypePropertyDescriptor classD = new NClassTypePropertyDescriptor(JRDesignParameter.PROPERTY_VALUE_CLASS_NAME,
				Messages.common_class);
		classD.setDescription(Messages.MParameterSystem_class_description);
		desc.add(classD);
		classD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#parameter_class"));

		defaultsMap.put(JRDesignParameter.PROPERTY_VALUE_CLASS_NAME, "java.lang.String"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignParameter jrParameter = (JRDesignParameter) getValue();
		if (id.equals(JRDesignParameter.PROPERTY_NAME))
			return jrParameter.getName();
		if (id.equals(JRDesignParameter.PROPERTY_VALUE_CLASS_NAME))
			return jrParameter.getValueClassName();
		return null;
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (JRDesignParameter.PROPERTY_NAME.equals(evt.getPropertyName())){
			JRDesignDataset d = ModelUtils.getDataset(this);
			JRDesignParameter jrParameter = (JRDesignParameter) getValue();
			if (d != null) {
				d.getParametersMap().remove(evt.getOldValue());
				d.getParametersMap().put(jrParameter.getName(), jrParameter);
			}
		}
		super.propertyChange(evt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		if (!isEditable())
			return;
		JRDesignParameter jrParameter = (JRDesignParameter) getValue();
		if (id.equals(JRDesignParameter.PROPERTY_NAME)){
			if (!value.equals("")) {
				jrParameter.setName((String) value);
			} 
		} else if (id.equals(JRDesignParameter.PROPERTY_VALUE_CLASS_NAME)){
				jrParameter.setValueClassName((String) value);
		}
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setEditable(false);
	}

	/**
	 * Creates the jr parameter.
	 * 
	 * @param jrDataset
	 *          the jr dataset
	 * @return the jR design parameter
	 */
	public static JRDesignParameter createJRParameter(JRDesignDataset jrDataset) {
		JRDesignParameter jrDesignParameter = new JRDesignParameter();
		jrDesignParameter.setSystemDefined(true);
		jrDesignParameter.setName(ModelUtils.getDefaultName(jrDataset.getParametersMap(), "Parameter")); //$NON-NLS-1$
		return jrDesignParameter;
	}
}
