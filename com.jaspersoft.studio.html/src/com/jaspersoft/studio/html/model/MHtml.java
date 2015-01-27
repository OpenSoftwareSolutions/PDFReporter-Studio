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
package com.jaspersoft.studio.html.model;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.html.HtmlComponent;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.html.HtmlNodeIconDescriptor;
import com.jaspersoft.studio.html.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public class MHtml extends MGraphicElement {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static IIconDescriptor iconDescriptor;
	private RComboBoxPropertyDescriptor evaluationGroupNameD;
	private static JSSEnumPropertyDescriptor scaleTypeD;
	private static JSSEnumPropertyDescriptor hAlignD;
	private static JSSEnumPropertyDescriptor vAlignD;
	private static JSSEnumPropertyDescriptor evaluationTimeD;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new HtmlNodeIconDescriptor("html"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public MHtml() {
		super();
	}

	public MHtml(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	public MHtml(ANode parent, JRDesignComponentElement jrHtml, int newIndex) {
		this(parent, newIndex);
		setValue(jrHtml);
	}

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

	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement el = new JRDesignComponentElement();
		HtmlComponent component = new HtmlComponent();
		JRDesignExpression exp = new JRDesignExpression();
		exp.setValueClassName("java.lang.String"); //$NON-NLS-1$
		exp.setText("\"<p style='background-color:yellow;'>HTML paragraph</p>\""); //$NON-NLS-1$
		component.setHtmlContentExpression(exp);
		el.setComponent(component);
		el.setComponentKey(new ComponentKey(
				"http://jasperreports.sourceforge.net/htmlcomponent", "hc", "html")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		return el;
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

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *            the desc
	 */
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc,
			Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		JRExpressionPropertyDescriptor contentExprD = new JRExpressionPropertyDescriptor(
				HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION,
				Messages.MHtml_content_expression);
		contentExprD
				.setDescription(Messages.MHtml_content_expression_description);
		desc.add(contentExprD);

		scaleTypeD = new JSSEnumPropertyDescriptor(
				HtmlComponent.PROPERTY_SCALE_TYPE, Messages.MHtml_scaletype,
				ScaleImageEnum.class, NullEnum.NOTNULL);
		scaleTypeD.setDescription(Messages.MHtml_scaletype_description);
		desc.add(scaleTypeD);

		CheckBoxPropertyDescriptor clipOverflow = new CheckBoxPropertyDescriptor(
				HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW,
				Messages.MHtml_cliponoverflow, NullEnum.NULL);
		clipOverflow.setDescription(Messages.MHtml_cliponoverflow_desc);
		desc.add(clipOverflow);

		hAlignD = new JSSEnumPropertyDescriptor(
				HtmlComponent.PROPERTY_HORIZONTAL_ALIGN,
				Messages.MHtml_horizontalalign, HorizontalAlignEnum.class,
				NullEnum.NOTNULL, 3);
		hAlignD.setDescription(Messages.MHtml_horizontalalign_description);
		desc.add(hAlignD);

		vAlignD = new JSSEnumPropertyDescriptor(
				HtmlComponent.PROPERTY_VERTICAL_ALIGN,
				Messages.MHtml_verticalalign, VerticalAlignEnum.class,
				NullEnum.NOTNULL, 3);
		vAlignD.setDescription(Messages.MHtml_verticalalign_description);
		desc.add(vAlignD);

		evaluationTimeD = new JSSEnumPropertyDescriptor(
				HtmlComponent.PROPERTY_EVALUATION_TIME,
				Messages.MHtml_evaluation_time, EvaluationTimeEnum.class,
				NullEnum.NOTNULL);
		evaluationTimeD
				.setDescription(Messages.MHtml_evaluation_time_description);
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(
				HtmlComponent.PROPERTY_EVALUATION_GROUP,
				Messages.MHtml_evaluation_group, new String[] { "" }); //$NON-NLS-1$
		evaluationGroupNameD
				.setDescription(Messages.MHtml_evaluation_group_description);
		desc.add(evaluationGroupNameD);

		contentExprD.setCategory(Messages.common_properties_category);
		scaleTypeD.setCategory(Messages.common_properties_category);
		hAlignD.setCategory(Messages.common_properties_category);
		vAlignD.setCategory(Messages.common_properties_category);
		evaluationTimeD.setCategory(Messages.common_properties_category);
		evaluationGroupNameD.setCategory(Messages.common_properties_category);
		clipOverflow.setCategory(Messages.common_properties_category);

		defaultsMap.put(HtmlComponent.PROPERTY_EVALUATION_TIME,
				evaluationTimeD.getEnumValue(EvaluationTimeEnum.NOW));
		defaultsMap.put(HtmlComponent.PROPERTY_SCALE_TYPE,
				scaleTypeD.getEnumValue(ScaleImageEnum.RETAIN_SHAPE));
		defaultsMap.put(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN,
				hAlignD.getEnumValue(HorizontalAlignEnum.LEFT));
		defaultsMap.put(HtmlComponent.PROPERTY_VERTICAL_ALIGN,
				vAlignD.getEnumValue(VerticalAlignEnum.MIDDLE));
		defaultsMap.put(HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW, Boolean.FALSE);
	}

	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

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

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		HtmlComponent htmlComp = (HtmlComponent) jrElement.getComponent();

		if (id.equals(HtmlComponent.PROPERTY_EVALUATION_TIME))
			return evaluationTimeD.getEnumValue(htmlComp.getEvaluationTime());
		if (id.equals(HtmlComponent.PROPERTY_EVALUATION_GROUP))
			return htmlComp.getEvaluationGroup();
		if (id.equals(HtmlComponent.PROPERTY_SCALE_TYPE))
			return scaleTypeD.getEnumValue(htmlComp.getScaleType());
		if (id.equals(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN))
			return hAlignD.getEnumValue(htmlComp.getHorizontalAlign());
		if (id.equals(HtmlComponent.PROPERTY_VERTICAL_ALIGN))
			return vAlignD.getEnumValue(htmlComp.getVerticalAlign());
		if (id.equals(HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION))
			return ExprUtil.getExpression(htmlComp.getHtmlContentExpression());
		if (id.equals(HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW))
			return htmlComp.getClipOnOverflow();

		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		HtmlComponent htmlComp = (HtmlComponent) jrElement.getComponent();

		if (id.equals(HtmlComponent.PROPERTY_EVALUATION_TIME))
			htmlComp.setEvaluationTime((EvaluationTimeEnum) evaluationTimeD
					.getEnumValue(value));
		else if (id.equals(HtmlComponent.PROPERTY_EVALUATION_GROUP))
			htmlComp.setEvaluationGroup((String) value);
		else if (id.equals(HtmlComponent.PROPERTY_SCALE_TYPE))
			htmlComp.setScaleType((ScaleImageEnum) scaleTypeD
					.getEnumValue(value));
		else if (id.equals(HtmlComponent.PROPERTY_HORIZONTAL_ALIGN))
			htmlComp.setHorizontalAlign((HorizontalAlignEnum) hAlignD
					.getEnumValue(value));
		else if (id.equals(HtmlComponent.PROPERTY_VERTICAL_ALIGN))
			htmlComp.setVerticalAlign((VerticalAlignEnum) vAlignD
					.getEnumValue(value));
		else if (id.equals(HtmlComponent.PROPERTY_HTMLCONTENT_EXPRESSION))
			htmlComp.setHtmlContentExpression(ExprUtil.setValues(
					htmlComp.getHtmlContentExpression(), value));
		else if (id.equals(HtmlComponent.PROPERTY_CLIP_ON_OVERFLOW))
			htmlComp.setClipOnOverflow((Boolean) value);
		else
			super.setPropertyValue(id, value);
	}
}
