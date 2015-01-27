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
package com.jaspersoft.studio.model.genericElement;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.JRGenericElementType;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGenericElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.genericElement.ParameterPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.genericElement.dialog.ParameterDTO;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public class MGenericElement extends MGraphicElement {
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
			iconDescriptor = new NodeIconDescriptor("generic"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m cross tab.
	 */
	public MGenericElement() {
		super();
	}

	/**
	 * Instantiates a new m cross tab.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrCrosstab
	 *          the jr crosstab
	 * @param newIndex
	 *          the new index
	 */
	public MGenericElement(ANode parent, JRDesignGenericElement jrCrosstab, int newIndex) {
		super(parent, newIndex);
		setValue(jrCrosstab);
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

	private IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private RComboBoxPropertyDescriptor evaluationGroupNameD;

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

		evaluationTimeD = new JSSEnumPropertyDescriptor(JRDesignGenericElement.PROPERTY_EVALUATION_TIME,
				Messages.common_evaluation_time, EvaluationTimeEnum.class, NullEnum.NOTNULL);
		evaluationTimeD.setDescription(Messages.MGenericElement_evaluation_time_description);
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(JRDesignGenericElement.PROPERTY_EVALUATION_GROUP_NAME,
				Messages.MGenericElement_evaluation_group_name, new String[] { "" }); //$NON-NLS-1$
		evaluationGroupNameD.setDescription(Messages.MGenericElement_evaluation_group_name_description);
		desc.add(evaluationGroupNameD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#genericElement");

		NTextPropertyDescriptor nameD = new NTextPropertyDescriptor(PROPERTY_NAME,
				Messages.MGenericElement_generic_type_name);
		nameD.setDescription(Messages.MGenericElement_generic_type_name_description);
		desc.add(nameD);

		NTextPropertyDescriptor nameSpaceD = new NTextPropertyDescriptor(PROPERTY_NAMESPACE,
				Messages.MGenericElement_generic_type_namespace);
		nameSpaceD.setDescription(Messages.MGenericElement_generic_type_namespace_description);
		desc.add(nameSpaceD);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#genericElementType");

		ParameterPropertyDescriptor parametersD = new ParameterPropertyDescriptor(
				JRDesignGenericElement.PROPERTY_PARAMETERS, Messages.common_parameters);
		parametersD.setDescription(Messages.MGenericElement_parameters_description);
		desc.add(parametersD);
		parametersD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#genericElementParameter"));

		parametersD.setCategory(Messages.MGenericElement_generic_element_properties_category);
		nameD.setCategory(Messages.MGenericElement_generic_element_properties_category);
		nameSpaceD.setCategory(Messages.MGenericElement_generic_element_properties_category);
		evaluationTimeD.setCategory(Messages.MGenericElement_generic_element_properties_category);
		evaluationGroupNameD.setCategory(Messages.MGenericElement_generic_element_properties_category);

		defaultsMap.put(JRDesignGenericElement.PROPERTY_EVALUATION_TIME, EvaluationTimeEnum.NOW);
	}

	public static final String PROPERTY_NAME = "name"; //$NON-NLS-1$
	public static final String PROPERTY_NAMESPACE = "namespace"; //$NON-NLS-1$
	private ParameterDTO propertyDTO;
	private static JSSEnumPropertyDescriptor evaluationTimeD;

	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignGenericElement jrElement = (JRDesignGenericElement) getValue();
		if (id.equals(JRDesignGenericElement.PROPERTY_EVALUATION_TIME))
			return evaluationTimeD.getEnumValue(jrElement.getEvaluationTimeValue());
		if (id.equals(JRDesignGenericElement.PROPERTY_EVALUATION_GROUP_NAME))
			return jrElement.getEvaluationGroupName();
		JRGenericElementType genericType = jrElement.getGenericType();
		if (genericType != null) {
			if (id.equals(PROPERTY_NAME))
				return genericType.getName();
			if (id.equals(PROPERTY_NAMESPACE))
				return genericType.getNamespace();
		}
		if (id.equals(JRDesignGenericElement.PROPERTY_PARAMETERS)) {
			if (propertyDTO == null) {
				propertyDTO = new ParameterDTO();
				propertyDTO.setJasperDesign(getJasperDesign());
				propertyDTO.setValue(jrElement.getParameters());
			}
			return propertyDTO;
		}
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignGenericElement jrElement = (JRDesignGenericElement) getValue();
		JRGenericElementType genericType = jrElement.getGenericType();
		if (id.equals(JRDesignGenericElement.PROPERTY_EVALUATION_TIME))
			jrElement.setEvaluationTime((EvaluationTimeEnum) evaluationTimeD.getEnumValue(value));
		else if (id.equals(JRDesignGenericElement.PROPERTY_EVALUATION_GROUP_NAME))
			jrElement.setEvaluationGroupName((String) value);
		else if (id.equals(JRDesignGenericElement.PROPERTY_PARAMETERS)) {
			if (value instanceof ParameterDTO) {
				ParameterDTO v = (ParameterDTO) value;

				if (jrElement.getParameters() != null)
					for (JRGenericElementParameter prm : jrElement.getParameters())
						jrElement.removeParameter(prm);

				for (JRGenericElementParameter param : v.getValue())
					jrElement.addParameter(param);

				propertyDTO = v;
			}

			// FIXME: in JR rewrite hashCode to work with null values for namespace and name
		} else if (id.equals(PROPERTY_NAME)) {
			String namespace = genericType != null ? genericType.getNamespace() : "";
			jrElement.setGenericType(new JRGenericElementType(namespace, (String) value));
		} else if (id.equals(PROPERTY_NAMESPACE)) {
			String name = genericType != null ? genericType.getName() : "";
			jrElement.setGenericType(new JRGenericElementType((String) value, name));
		}

		super.setPropertyValue(id, value);
	}

	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignGenericElement el = new JRDesignGenericElement(jasperDesign);
		el.setGenericType(new JRGenericElementType("namespace", "name"));

		DefaultManager.INSTANCE.applyDefault(this.getClass(), el);
		
		return el;
	}
}
