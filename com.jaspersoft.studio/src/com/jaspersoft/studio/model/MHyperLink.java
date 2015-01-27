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

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRHyperlink;
import net.sf.jasperreports.engine.JRHyperlinkParameter;
import net.sf.jasperreports.engine.design.JRDesignHyperlink;
import net.sf.jasperreports.engine.type.HyperlinkTargetEnum;
import net.sf.jasperreports.engine.type.HyperlinkTypeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.ParameterPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.hyperlink.parameter.dialog.ParameterDTO;
import com.jaspersoft.studio.utils.EnumHelper;

public class MHyperLink extends APropertyNode {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MHyperLink(JRHyperlink hyperLink) {
		super();
		setValue(hyperLink);
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		
		String prefix = "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#";
		
		JRExpressionPropertyDescriptor anchorExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION, Messages.MHyperLink_hyperlink_anchor_expression);
		anchorExpressionD.setDescription(Messages.MHyperLink_hyperlink_anchor_expression_description);
		desc.add(anchorExpressionD);
		anchorExpressionD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION));

		JRExpressionPropertyDescriptor pageExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION, Messages.MHyperLink_hyperlink_page_expression);
		pageExpressionD.setDescription(Messages.MHyperLink_hyperlink_page_expression_description);
		desc.add(pageExpressionD);
		pageExpressionD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION));

		JRExpressionPropertyDescriptor referenceExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION, Messages.MHyperLink_hyperlink_reference_expression);
		referenceExpressionD.setDescription(Messages.MHyperLink_hyperlink_reference_expression_description);
		desc.add(referenceExpressionD);
		referenceExpressionD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION));

		JRExpressionPropertyDescriptor whenExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION, Messages.MHyperLink_whenexpr);
		whenExpressionD.setDescription(Messages.MHyperLink_whenexpr_desc);
		desc.add(whenExpressionD);
		whenExpressionD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION));

		JRExpressionPropertyDescriptor toolTipExpressionD = new JRExpressionPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION, Messages.MHyperLink_hyperlink_tooltip_expression);
		toolTipExpressionD.setDescription(Messages.MHyperLink_hyperlink_tooltip_expression_description);
		desc.add(toolTipExpressionD);
		toolTipExpressionD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION));

		ParameterPropertyDescriptor propertiesD = new ParameterPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS, Messages.common_parameters);
		propertiesD.setDescription(Messages.MHyperLink_parameters_description);
		desc.add(propertiesD);
		propertiesD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS));

		RComboBoxPropertyDescriptor linkTargetD = new RComboBoxPropertyDescriptor(
				JRDesignHyperlink.PROPERTY_HYPERLINK_TARGET, Messages.MHyperLink_link_target, EnumHelper.getEnumNames(
						HyperlinkTargetEnum.values(), NullEnum.NULL));
		linkTargetD.setDescription(Messages.MHyperLink_link_target_description);
		desc.add(linkTargetD);
		linkTargetD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + "sectionHyperlink_hyperlinkTarget"));

		RComboBoxPropertyDescriptor linkTypeD = new RComboBoxPropertyDescriptor(JRDesignHyperlink.PROPERTY_LINK_TYPE,
				Messages.MHyperLink_link_type, EnumHelper.getEnumNames(HyperlinkTypeEnum.values(), NullEnum.NULL));
		linkTypeD.setDescription(Messages.MHyperLink_link_type_description);
		desc.add(linkTypeD);
		linkTypeD.setHelpRefBuilder(new HelpReferenceBuilder(prefix + "sectionHyperlink_hyperlinkType"));


		propertiesD.setCategory(Messages.MHyperLink_hyperlink_category);
		anchorExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		whenExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		pageExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		referenceExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);
		toolTipExpressionD.setCategory(Messages.MHyperLink_hyperlink_category);

		linkTargetD.setCategory(Messages.MHyperLink_hyperlink_category);
		linkTypeD.setCategory(Messages.MHyperLink_hyperlink_category);

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		// pen
		JRHyperlink jrElement = (JRHyperlink) getValue();
		if (jrElement != null) {
			if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
				return jrElement.getLinkTarget();
			if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
				return jrElement.getLinkType();
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
				return ExprUtil.getExpression(jrElement.getHyperlinkAnchorExpression());
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
				return ExprUtil.getExpression(jrElement.getHyperlinkPageExpression());
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
				return ExprUtil.getExpression(jrElement.getHyperlinkReferenceExpression());
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION))
				return ExprUtil.getExpression(jrElement.getHyperlinkWhenExpression());
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
				return ExprUtil.getExpression(jrElement.getHyperlinkTooltipExpression());
			if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
				if (propertyDTO == null) {
					propertyDTO = new ParameterDTO();
					propertyDTO.setJasperDesign(getJasperDesign());
					propertyDTO.setValue(jrElement.getHyperlinkParameters());
				}
				return propertyDTO;
			}
		}
		return null;
	}

	private ParameterDTO propertyDTO;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignHyperlink jrElement = (JRDesignHyperlink) getValue();
		if (jrElement != null) {
			if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TARGET))
				jrElement.setLinkTarget((String) value);
			else if (id.equals(JRDesignHyperlink.PROPERTY_LINK_TYPE))
				jrElement.setLinkType((String) value);
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_ANCHOR_EXPRESSION))
				jrElement.setHyperlinkAnchorExpression(ExprUtil.setValues(jrElement.getHyperlinkAnchorExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PAGE_EXPRESSION))
				jrElement.setHyperlinkPageExpression(ExprUtil.setValues(jrElement.getHyperlinkPageExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_REFERENCE_EXPRESSION))
				jrElement
						.setHyperlinkReferenceExpression(ExprUtil.setValues(jrElement.getHyperlinkReferenceExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_WHEN_EXPRESSION))
				jrElement.setHyperlinkWhenExpression(ExprUtil.setValues(jrElement.getHyperlinkWhenExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_TOOLTIP_EXPRESSION))
				jrElement.setHyperlinkTooltipExpression(ExprUtil.setValues(jrElement.getHyperlinkTooltipExpression(), value));
			else if (id.equals(JRDesignHyperlink.PROPERTY_HYPERLINK_PARAMETERS)) {
				if (value instanceof ParameterDTO) {
					ParameterDTO v = (ParameterDTO) value;

					JRHyperlinkParameter[] hyperlinkParameters = jrElement.getHyperlinkParameters();
					if (hyperlinkParameters != null)
						for (JRHyperlinkParameter prm : hyperlinkParameters)
							jrElement.removeHyperlinkParameter(prm);

					for (JRHyperlinkParameter param : v.getValue())
						jrElement.addHyperlinkParameter(param);

					propertyDTO = v;
				}
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
