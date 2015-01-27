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
package com.jaspersoft.studio.components.crosstab.model;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstabBucket;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabBucket;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public class MBucket extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Instantiates a new m field.
	 * 
	 * @param parent
	 *            the parent
	 * @param jfRield
	 *            the jf rield
	 * @param newIndex
	 *            the new index
	 */
	public MBucket(JRCrosstabBucket jfRield) {
		super();
		setValue(jfRield);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return Messages.common_bucket;
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
	 * @see com.jaspersoft.studio.model.INode#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSEnumPropertyDescriptor orderD;

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
		orderD = new JSSEnumPropertyDescriptor(
				JRDesignCrosstabBucket.PROPERTY_ORDER, Messages.common_order,
				SortOrderEnum.class, NullEnum.NOTNULL);
		orderD.setDescription(Messages.MBucket_order_description);
		desc.add(orderD);

		JRExpressionPropertyDescriptor orderByExprD = new JRExpressionPropertyDescriptor(
				JRDesignCrosstabBucket.PROPERTY_ORDER_BY_EXPRESSION,
				Messages.MBucket_order_by_expression);
		orderByExprD
				.setDescription(Messages.MBucket_order_by_expression_description);
		desc.add(orderByExprD);

		JRExpressionPropertyDescriptor compExprD = new JRExpressionPropertyDescriptor(
				JRDesignCrosstabBucket.PROPERTY_COMPARATOR_EXPRESSION,
				Messages.MBucket_comparator_expression);
		compExprD
				.setDescription(Messages.MBucket_comparator_expression_description);
		desc.add(compExprD);

		JRExpressionPropertyDescriptor exprD = new JRExpressionPropertyDescriptor(
				JRDesignCrosstabBucket.PROPERTY_EXPRESSION,
				Messages.MBucket_expression);
		exprD.setDescription(Messages.MBucket_expression_description);
		desc.add(exprD);

		NClassTypePropertyDescriptor classD = new NClassTypePropertyDescriptor(
				JRDesignCrosstabBucket.PROPERTY_VALUE_CLASS, Messages.MBucket_valueClassTitle);
		classD.setDescription(Messages.MBucket_valueClassDescription);
		desc.add(classD);

		defaultsMap.put(JRDesignCrosstabBucket.PROPERTY_VALUE_CLASS, null);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignCrosstabBucket jrField = (JRDesignCrosstabBucket) getValue();
		if (id.equals(JRDesignCrosstabBucket.PROPERTY_ORDER))
			return orderD.getEnumValue(jrField.getOrderValue());
		if (id.equals(JRDesignCrosstabBucket.PROPERTY_COMPARATOR_EXPRESSION))
			return ExprUtil.getExpression(jrField.getComparatorExpression());
		if (id.equals(JRDesignCrosstabBucket.PROPERTY_ORDER_BY_EXPRESSION))
			return ExprUtil.getExpression(jrField.getOrderByExpression());
		if (id.equals(JRDesignCrosstabBucket.PROPERTY_EXPRESSION))
			return ExprUtil.getExpression(jrField.getExpression());
		if (id.equals(JRDesignCrosstabBucket.PROPERTY_VALUE_CLASS))
			return jrField.getValueClassName();
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignCrosstabBucket jrField = (JRDesignCrosstabBucket) getValue();

		if (id.equals(JRDesignCrosstabBucket.PROPERTY_ORDER))
			jrField.setOrder((SortOrderEnum) orderD.getEnumValue(value));
		else if (id
				.equals(JRDesignCrosstabBucket.PROPERTY_COMPARATOR_EXPRESSION))
			jrField.setComparatorExpression(ExprUtil.setValues(
					jrField.getComparatorExpression(), value));
		else if (id.equals(JRDesignCrosstabBucket.PROPERTY_ORDER_BY_EXPRESSION))
			jrField.setOrderByExpression(ExprUtil.setValues(
					jrField.getOrderByExpression(), value));
		else if (id.equals(JRDesignCrosstabBucket.PROPERTY_EXPRESSION))
			jrField.setExpression((JRDesignExpression) ExprUtil.setValues(
					jrField.getExpression(), value));
		else if (id.equals(JRDesignCrosstabBucket.PROPERTY_VALUE_CLASS))
			jrField.setValueClassName((String) value);
	}
}
