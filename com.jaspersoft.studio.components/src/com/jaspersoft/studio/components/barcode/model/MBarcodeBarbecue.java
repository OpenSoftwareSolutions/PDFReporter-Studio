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
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.barcode.BarcodeNodeIconDescriptor;
import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IRotatable;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

/*
 * The Class MBarcode.
 */
public class MBarcodeBarbecue extends MBarcode implements IRotatable {
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
	public MBarcodeBarbecue() {
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
	public MBarcodeBarbecue(ANode parent, JRDesignComponentElement jrBarcode,
			int newIndex) {
		super(parent, newIndex);
		setValue(jrBarcode);
	}

	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement el = new JRDesignComponentElement();
		StandardBarbecueComponent component = new StandardBarbecueComponent();
		JRDesignExpression exp = new JRDesignExpression();
		exp.setText("\"1234\""); //$NON-NLS-1$
		component.setCodeExpression(exp);
		el.setComponent(component);
		el.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", "jr", "barbecue")); //$NON-NLS-1$
		
		DefaultManager.INSTANCE.applyDefault(this.getClass(), el);
		
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
	private static JSSEnumPropertyDescriptor rotationD;

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

		PixelPropertyDescriptor widthD = new PixelPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_BAR_WIDTH,
				Messages.MBarcodeBarbecue_bar_width);
		widthD.setDescription(Messages.MBarcodeBarbecue_bar_width_description);
		desc.add(widthD);

		PixelPropertyDescriptor heightD = new PixelPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_BAR_HEIGTH,
				Messages.MBarcodeBarbecue_bar_height);
		heightD.setDescription(Messages.MBarcodeBarbecue_bar_height_description);
		desc.add(heightD);

		JRExpressionPropertyDescriptor appIDexprD = new JRExpressionPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_APPLICATION_IDENTIFIER_EXPRESSION,
				Messages.MBarcodeBarbecue_application_identifier_expression);
		appIDexprD
				.setDescription(Messages.MBarcodeBarbecue_application_identifier_expression_description);
		desc.add(appIDexprD);

		RComboBoxPropertyDescriptor typeD = new RComboBoxPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_TYPE,
				Messages.MBarcodeBarbecue_type,
				new String[] {
						"2of7", "3of9", "Bookland", "Codabar", "Code128", "Code128A", "Code128B", "Code128C", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
						"Code39", "Code39 (Extended)", "EAN128", "EAN13", "GlobalTradeItemNumber", "Int2of5", "Monarch", "NW7", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
						"PDF417", "PostNet", "RandomWeightUPCA", "SCC14ShippingCode", "ShipmentIdentificationNumber", "SSCC18", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
						"Std2of5", "UCC128", "UPCA", "USD3", "USD4", "USPS" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		typeD.setDescription(Messages.MBarcodeBarbecue_type_description);
		desc.add(typeD);

		CheckBoxPropertyDescriptor checksumRequiredD = new CheckBoxPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_CHECKSUM_REQUIRED,
				Messages.MBarcodeBarbecue_checksum_required);
		checksumRequiredD
				.setDescription(Messages.MBarcodeBarbecue_checksum_required_description);
		desc.add(checksumRequiredD);

		CheckBoxPropertyDescriptor drawTextD = new CheckBoxPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_DRAW_TEXT,
				Messages.MBarcodeBarbecue_draw_text);
		drawTextD
				.setDescription(Messages.MBarcodeBarbecue_draw_text_description);
		desc.add(drawTextD);

		rotationD = new JSSEnumPropertyDescriptor(
				StandardBarbecueComponent.PROPERTY_ROTATION,
				Messages.MBarcodeBarbecue_rotation, RotationEnum.class,
				NullEnum.INHERITED);
		rotationD
				.setDescription(Messages.MBarcodeBarbecue_rotation_description);
		desc.add(rotationD);

		rotationD.setCategory(Messages.common_properties_category);
		widthD.setCategory(Messages.common_properties_category);
		typeD.setCategory(Messages.common_properties_category);
		drawTextD.setCategory(Messages.common_properties_category);
		checksumRequiredD.setCategory(Messages.common_properties_category);
		heightD.setCategory(Messages.common_properties_category);
		appIDexprD.setCategory(Messages.common_properties_category);

		defaultsMap.put(StandardBarbecueComponent.PROPERTY_EVALUATION_TIME,
				evaluationTimeD.getEnumValue(EvaluationTimeEnum.NOW));
		defaultsMap.put(StandardBarbecueComponent.PROPERTY_ROTATION, null);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		StandardBarbecueComponent jrList = (StandardBarbecueComponent) jrElement
				.getComponent();

		if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_TIME))
			return evaluationTimeD
					.getEnumValue(jrList.getEvaluationTimeValue());
		if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_GROUP))
			return jrList.getEvaluationGroup();
		if (id.equals(StandardBarbecueComponent.PROPERTY_CHECKSUM_REQUIRED))
			return new Boolean(jrList.isChecksumRequired());
		if (id.equals(StandardBarbecueComponent.PROPERTY_DRAW_TEXT))
			return new Boolean(jrList.isDrawText());

		if (id.equals(StandardBarbecueComponent.PROPERTY_TYPE))
			return jrList.getType();

		if (id.equals(StandardBarbecueComponent.PROPERTY_BAR_HEIGTH))
			return jrList.getBarHeight();
		if (id.equals(StandardBarbecueComponent.PROPERTY_BAR_WIDTH))
			return jrList.getBarWidth();
		if (id.equals(StandardBarbecueComponent.PROPERTY_CODE_EXPRESSION))
			return ExprUtil.getExpression(jrList.getCodeExpression());
		if (id.equals(StandardBarbecueComponent.PROPERTY_ROTATION))
			return rotationD.getEnumValue(jrList.getOwnRotation());
		if (id.equals(StandardBarbecueComponent.PROPERTY_APPLICATION_IDENTIFIER_EXPRESSION))
			return ExprUtil.getExpression(jrList
					.getApplicationIdentifierExpression());
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		StandardBarbecueComponent jrList = (StandardBarbecueComponent) jrElement
				.getComponent();

		if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_TIME))
			jrList.setEvaluationTimeValue((EvaluationTimeEnum) evaluationTimeD
					.getEnumValue(value));
		else if (id.equals(StandardBarbecueComponent.PROPERTY_EVALUATION_GROUP))
			jrList.setEvaluationGroup((String) value);
		else if (id
				.equals(StandardBarbecueComponent.PROPERTY_CHECKSUM_REQUIRED))
			jrList.setChecksumRequired(((Boolean) value).booleanValue());
		else if (id.equals(StandardBarbecueComponent.PROPERTY_DRAW_TEXT))
			jrList.setDrawText(((Boolean) value).booleanValue());
		else if (id.equals(StandardBarbecueComponent.PROPERTY_TYPE))
			jrList.setType((String) value);
		else if (id.equals(StandardBarbecueComponent.PROPERTY_ROTATION))
			jrList.setRotation((RotationEnum) rotationD.getEnumValue(value));

		else if (id.equals(StandardBarbecueComponent.PROPERTY_BAR_HEIGTH))
			jrList.setBarHeight((Integer) value);
		else if (id.equals(StandardBarbecueComponent.PROPERTY_BAR_WIDTH))
			jrList.setBarWidth((Integer) value);
		else if (id.equals(StandardBarbecueComponent.PROPERTY_CODE_EXPRESSION)) {
			jrList.setCodeExpression(ExprUtil.setValues(
					jrList.getCodeExpression(), value, null));
		} else if (id
				.equals(StandardBarbecueComponent.PROPERTY_APPLICATION_IDENTIFIER_EXPRESSION)) {
			jrList.setApplicationIdentifierExpression(ExprUtil.setValues(
					jrList.getApplicationIdentifierExpression(), value, null));
		} else
			super.setPropertyValue(id, value);
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRDesignComponentElement jrSourceElement = (JRDesignComponentElement) getValue();
		StandardBarbecueComponent jrSourceCode = (StandardBarbecueComponent) jrSourceElement.getComponent();
		
		JRDesignComponentElement jrTargetElement = (JRDesignComponentElement) target;
		StandardBarbecueComponent jrTargetCode = (StandardBarbecueComponent) jrTargetElement.getComponent();
		
		jrTargetCode.setChecksumRequired(jrSourceCode.isChecksumRequired());
		jrTargetCode.setDrawText(jrSourceCode.isDrawText());
		jrTargetCode.setType(getStringClone(jrSourceCode.getType()));
		jrTargetCode.setBarHeight(jrSourceCode.getBarHeight());
		jrTargetCode.setBarWidth(jrSourceCode.getBarWidth());
		jrTargetCode.setRotation(jrSourceCode.getOwnRotation());
	}
}
