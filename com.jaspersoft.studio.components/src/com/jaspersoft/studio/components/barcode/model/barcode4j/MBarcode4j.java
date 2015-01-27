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
package com.jaspersoft.studio.components.barcode.model.barcode4j;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.barbecue.StandardBarbecueComponent;
import net.sf.jasperreports.components.barcode4j.BarcodeComponent;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.barcode.BarcodeNodeIconDescriptor;
import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.components.barcode.model.MBarcode;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.DoublePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSComboPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

/*
 * The Class MBarcode.
 */
public class MBarcode4j extends MBarcode {
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
			iconDescriptor = new BarcodeNodeIconDescriptor("barcode"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m barcode.
	 */
	public MBarcode4j() {
		super();
	}

	/**
	 * Instantiates a new m barcode.
	 * 
	 * @param parent
	 *            the parent
	 * @param jrBarcode
	 *            the jr barcode
	 * @param newIndex
	 *            the new index
	 */
	public MBarcode4j(ANode parent, JRDesignComponentElement jrBarcode,
			int newIndex) {
		super(parent, newIndex);
		setValue(jrBarcode);
	}

	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement el = new JRDesignComponentElement();
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

		JSSComboPropertyDescriptor orientationD = new JSSComboPropertyDescriptor(
				BarcodeComponent.PROPERTY_ORIENTATION,
				Messages.MBarcode4j_orientation, Orientation.getItems());
		orientationD
				.setDescription(Messages.MBarcode4j_orientation_description);
		desc.add(orientationD);

		JRExpressionPropertyDescriptor patternExprD = new JRExpressionPropertyDescriptor(
				BarcodeComponent.PROPERTY_PATTERN_EXPRESSION,
				Messages.MBarcode4j_pattern_expression);
		patternExprD
				.setDescription(Messages.MBarcode4j_pattern_expression_description);
		desc.add(patternExprD);

		JSSComboPropertyDescriptor textPositionD = new JSSComboPropertyDescriptor(
				BarcodeComponent.PROPERTY_TEXT_POSITION,
				Messages.MBarcode4j_text_position, TextPosition.getItems());
		textPositionD
				.setDescription(Messages.MBarcode4j_text_position_description);
		desc.add(textPositionD);

		DoublePropertyDescriptor quiteZoneD = new DoublePropertyDescriptor(
				BarcodeComponent.PROPERTY_QUIET_ZONE,
				Messages.MBarcode4j_quiet_zone);
		quiteZoneD.setDescription(Messages.MBarcode4j_quiet_zone_description);
		desc.add(quiteZoneD);

		PixelPropertyDescriptor moduleWidthD = new PixelPropertyDescriptor(
				BarcodeComponent.PROPERTY_MODULE_WIDTH,
				Messages.MBarcode4j_module_width);
		moduleWidthD
				.setDescription(Messages.MBarcode4j_module_width_description);
		desc.add(moduleWidthD);

		DoublePropertyDescriptor vertQuietZoneD = new DoublePropertyDescriptor(
				BarcodeComponent.PROPERTY_VERTICAL_QUIET_ZONE,
				Messages.MBarcode4j_vertical_quiet_zone);
		vertQuietZoneD
				.setDescription(Messages.MBarcode4j_vertical_quiet_zone_description);
		desc.add(vertQuietZoneD);

		vertQuietZoneD.setCategory(Messages.common_properties_category);
		moduleWidthD.setCategory(Messages.common_properties_category);
		quiteZoneD.setCategory(Messages.common_properties_category);
		orientationD.setCategory(Messages.common_properties_category);
		patternExprD.setCategory(Messages.common_properties_category);
		textPositionD.setCategory(Messages.common_properties_category);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		BarcodeComponent jrList = (BarcodeComponent) jrElement.getComponent();

		if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_TIME))
			return evaluationTimeD
					.getEnumValue(jrList.getEvaluationTimeValue());
		if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_GROUP))
			return jrList.getEvaluationGroup();

		if (id.equals(BarcodeComponent.PROPERTY_MODULE_WIDTH))
			return jrList.getModuleWidth() != null ? jrList.getModuleWidth().intValue() : null;
		if (id.equals(BarcodeComponent.PROPERTY_QUIET_ZONE))
			return jrList.getQuietZone();
		if (id.equals(BarcodeComponent.PROPERTY_VERTICAL_QUIET_ZONE))
			return jrList.getVerticalQuietZone();
		if (id.equals(BarcodeComponent.PROPERTY_ORIENTATION))
			return Orientation.getPos4Orientation(jrList.getOrientation());
		if (id.equals(BarcodeComponent.PROPERTY_TEXT_POSITION))
			return TextPosition.getPos4TextPosition(jrList.getTextPosition());

		if (id.equals(StandardBarbecueComponent.PROPERTY_CODE_EXPRESSION))
			return ExprUtil.getExpression(jrList.getCodeExpression());
		if (id.equals(BarcodeComponent.PROPERTY_PATTERN_EXPRESSION))
			return ExprUtil.getExpression(jrList.getPatternExpression());
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		BarcodeComponent jrList = (BarcodeComponent) jrElement.getComponent();

		if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_TIME))
			jrList.setEvaluationTimeValue((EvaluationTimeEnum) evaluationTimeD
					.getEnumValue(value));
		else if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_GROUP))
			jrList.setEvaluationGroup((String) value);

		else if (id.equals(BarcodeComponent.PROPERTY_MODULE_WIDTH))
			if (value instanceof Integer) jrList.setModuleWidth(((Integer)value).doubleValue());
			else jrList.setModuleWidth((Double) value);
		else if (id.equals(BarcodeComponent.PROPERTY_QUIET_ZONE))
			jrList.setQuietZone((Double) value);
		else if (id.equals(BarcodeComponent.PROPERTY_VERTICAL_QUIET_ZONE))
			jrList.setVerticalQuietZone((Double) value);
		else if (id.equals(BarcodeComponent.PROPERTY_ORIENTATION))
			jrList.setOrientation(Orientation
					.getOrientation4Pos((Integer) value));
		else if (id.equals(BarcodeComponent.PROPERTY_TEXT_POSITION))
			jrList.setTextPosition(TextPosition
					.getTextPosition4Pos((Integer) value));

		else if (id.equals(BarcodeComponent.PROPERTY_PATTERN_EXPRESSION)) {
			jrList.setPatternExpression(ExprUtil.setValues(
					jrList.getPatternExpression(), value, null));
		} else if (id
				.equals(StandardBarbecueComponent.PROPERTY_CODE_EXPRESSION)) {
			jrList.setCodeExpression(ExprUtil.setValues(
					jrList.getCodeExpression(), value, null));
		} else
			super.setPropertyValue(id, value);
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRDesignComponentElement jrSourceElement = (JRDesignComponentElement) getValue();
		BarcodeComponent jrSourceBarcode = (BarcodeComponent) jrSourceElement.getComponent();
		
		JRDesignComponentElement jrTargetElement = (JRDesignComponentElement) target;
		BarcodeComponent jrTargetBarcode = (BarcodeComponent) jrTargetElement.getComponent();
		
		jrTargetBarcode.setModuleWidth(jrSourceBarcode.getModuleWidth());
		jrTargetBarcode.setQuietZone(jrSourceBarcode.getQuietZone());
		jrTargetBarcode.setVerticalQuietZone(jrSourceBarcode.getVerticalQuietZone());
		jrTargetBarcode.setOrientation(jrSourceBarcode.getOrientation());
		jrTargetBarcode.setTextPosition(jrSourceBarcode.getTextPosition());
	}
}
