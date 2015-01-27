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

import net.sf.jasperreports.components.barcode4j.PDF417Component;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.barcode.messages.Messages;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.property.descriptors.DoublePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;

public class MPDF417 extends MBarcode4j {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MPDF417() {
		super();
	}

	public MPDF417(ANode parent, JRDesignComponentElement jrBarcode,
			int newIndex) {
		super(parent, jrBarcode, newIndex);
	}

	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement el = new JRDesignComponentElement();
		PDF417Component component = new PDF417Component();
		JRDesignExpression exp = new JRDesignExpression();
		exp.setText("\"123456789\""); //$NON-NLS-1$
		component.setCodeExpression(exp);
		el.setComponent(component);
		el.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", "jr", "PDF417")); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		
		DefaultManager.INSTANCE.applyDefault(this.getClass(), el);
		
		return el;
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

		IntegerPropertyDescriptor minColumnsD = new IntegerPropertyDescriptor(
				PDF417Component.PROPERTY_MIN_COLUMNS,
				Messages.MPDF417_min_columns);
		minColumnsD.setDescription(Messages.MPDF417_min_columns_description);
		desc.add(minColumnsD);

		IntegerPropertyDescriptor maxColumnsD = new IntegerPropertyDescriptor(
				PDF417Component.PROPERTY_MAX_COLUMNS,
				Messages.MPDF417_max_columns);
		maxColumnsD.setDescription(Messages.MPDF417_max_columns_description);
		desc.add(maxColumnsD);

		IntegerPropertyDescriptor minRowsD = new IntegerPropertyDescriptor(
				PDF417Component.PROPERTY_MIN_ROWS, Messages.MPDF417_min_rows);
		minRowsD.setDescription(Messages.MPDF417_min_rows_description);
		desc.add(minRowsD);

		IntegerPropertyDescriptor maxRowsD = new IntegerPropertyDescriptor(
				PDF417Component.PROPERTY_MAX_ROWS, Messages.MPDF417_max_rows);
		maxRowsD.setDescription(Messages.MPDF417_max_rows_description);
		desc.add(maxRowsD);

		DoublePropertyDescriptor width2HeightRatioD = new DoublePropertyDescriptor(
				PDF417Component.PROPERTY_WIDTH_TO_HEIGHT_RATIO,
				Messages.MPDF417_width_to_height_ratio);
		width2HeightRatioD
				.setDescription(Messages.MPDF417_width_to_height_ratio_description);
		desc.add(width2HeightRatioD);

		IntegerPropertyDescriptor errorCorrectionLevelD = new IntegerPropertyDescriptor(
				PDF417Component.PROPERTY_ERROR_CORRECTION_LEVEL,
				Messages.MPDF417_error_correction_level);
		errorCorrectionLevelD
				.setDescription(Messages.MPDF417_error_correction_level_description);
		desc.add(errorCorrectionLevelD);

		minColumnsD.setCategory(Messages.MPDF417_properties_category);
		maxColumnsD.setCategory(Messages.MPDF417_properties_category);
		minRowsD.setCategory(Messages.MPDF417_properties_category);
		maxRowsD.setCategory(Messages.MPDF417_properties_category);
		width2HeightRatioD.setCategory(Messages.MPDF417_properties_category);
		errorCorrectionLevelD.setCategory(Messages.MPDF417_properties_category);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		PDF417Component jrList = (PDF417Component) jrElement.getComponent();

		if (id.equals(PDF417Component.PROPERTY_MIN_COLUMNS))
			return jrList.getMinColumns();
		if (id.equals(PDF417Component.PROPERTY_MAX_COLUMNS))
			return jrList.getMaxColumns();
		if (id.equals(PDF417Component.PROPERTY_MIN_ROWS))
			return jrList.getMinRows();
		if (id.equals(PDF417Component.PROPERTY_MAX_ROWS))
			return jrList.getMaxRows();
		if (id.equals(PDF417Component.PROPERTY_WIDTH_TO_HEIGHT_RATIO))
			return jrList.getWidthToHeightRatio();
		if (id.equals(PDF417Component.PROPERTY_ERROR_CORRECTION_LEVEL))
			return jrList.getErrorCorrectionLevel();

		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		PDF417Component jrList = (PDF417Component) jrElement.getComponent();

		if (id.equals(PDF417Component.PROPERTY_MIN_ROWS))
			jrList.setMinRows((Integer) value);
		if (id.equals(PDF417Component.PROPERTY_MAX_ROWS))
			jrList.setMaxRows((Integer) value);
		if (id.equals(PDF417Component.PROPERTY_MIN_COLUMNS))
			jrList.setMinColumns((Integer) value);
		if (id.equals(PDF417Component.PROPERTY_MAX_COLUMNS))
			jrList.setMaxColumns((Integer) value);
		if (id.equals(PDF417Component.PROPERTY_WIDTH_TO_HEIGHT_RATIO))
			jrList.setWidthToHeightRatio((Double) value);
		if (id.equals(PDF417Component.PROPERTY_ERROR_CORRECTION_LEVEL))
			jrList.setErrorCorrectionLevel((Integer) value);

		super.setPropertyValue(id, value);
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRDesignComponentElement jrSourceElement = (JRDesignComponentElement) getValue();
		PDF417Component jrSourceBarcode = (PDF417Component) jrSourceElement.getComponent();
		
		JRDesignComponentElement jrTargetElement = (JRDesignComponentElement) target;
		PDF417Component jrTargetBarcode = (PDF417Component) jrTargetElement.getComponent();
		
		jrTargetBarcode.setMinColumns(jrSourceBarcode.getMinColumns());
		jrTargetBarcode.setMaxColumns(jrSourceBarcode.getMaxColumns());
		jrTargetBarcode.setMinRows(jrSourceBarcode.getMinRows());
		jrTargetBarcode.setMaxRows(jrSourceBarcode.getMaxRows());
		jrTargetBarcode.setWidthToHeightRatio(jrSourceBarcode.getWidthToHeightRatio());
		jrTargetBarcode.setErrorCorrectionLevel(jrSourceBarcode.getErrorCorrectionLevel());
	}
}
