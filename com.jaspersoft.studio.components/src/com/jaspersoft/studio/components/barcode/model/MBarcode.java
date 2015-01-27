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
package com.jaspersoft.studio.components.barcode.model;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public abstract class MBarcode extends MGraphicElement {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MBarcode() {
		super();
	}

	public MBarcode(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	private IPropertyDescriptor[] descriptors;
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1,
			Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *            the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc,
			Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		JRExpressionPropertyDescriptor codeExprD = new JRExpressionPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_CODE_EXPRESSION,
				Messages.MBarcode_code_expression);
		codeExprD.setDescription(Messages.MBarcode_code_expression_description);
		desc.add(codeExprD);

		evaluationTimeD = new JSSEnumPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_EVALUATION_TIME,
				Messages.MBarcode_evaluation_time, EvaluationTimeEnum.class,
				NullEnum.NOTNULL);
		evaluationTimeD
				.setDescription(Messages.MBarcode_evaluation_time_description);
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_EVALUATION_GROUP,
				Messages.MBarcode_evaluation_group, new String[] { "" }); //$NON-NLS-1$
		evaluationGroupNameD
				.setDescription(Messages.MBarcode_evaluation_group_description);
		desc.add(evaluationGroupNameD);

		evaluationTimeD.setCategory(Messages.common_properties_category);
		evaluationGroupNameD.setCategory(Messages.common_properties_category);
		codeExprD.setCategory(Messages.common_properties_category);

		defaultsMap.put(StandardBarbecueComponent.PROPERTY_EVALUATION_TIME,
				evaluationTimeD.getEnumValue(EvaluationTimeEnum.NOW));
	}

	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

	private RComboBoxPropertyDescriptor evaluationGroupNameD;
	protected static JSSEnumPropertyDescriptor evaluationTimeD;

	@Override
	public void setValue(Object value) {
		if (getValue() != null) {
			Object obj = getComponent();
			if (obj instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport()
						.removePropertyChangeListener(this);
		}
		if (value != null) {
			Object obj = getComponent(value);
			if (value instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport()
						.addPropertyChangeListener(this);
		}
		super.setValue(value);
	}

	private Object getComponent() {
		return getComponent(getValue());
	}

	private Object getComponent(Object value) {
		if (value != null) {
			JRDesignComponentElement jrElement = (JRDesignComponentElement) value;
			return jrElement.getComponent();
		}
		return null;
	}
}
