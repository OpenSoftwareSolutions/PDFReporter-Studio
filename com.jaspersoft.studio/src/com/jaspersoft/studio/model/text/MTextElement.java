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
package com.jaspersoft.studio.model.text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JRDesignTextElement;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IRotatable;
import com.jaspersoft.studio.model.MGraphicElementLineBox;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.HAlignPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.RotationPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.VAlignPropertyDescriptor;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.ModelUtils;

public abstract class MTextElement extends MGraphicElementLineBox implements IRotatable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public MTextElement() {
		super();
	}

	public MTextElement(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	public MTextElement(ANode parent, JRDesignElement jrLine, int newIndex) {
		super(parent, jrLine, newIndex);
	}

	@Override
	public HashMap<String, Object> getStylesDescriptors() {
		HashMap<String, Object> result = super.getStylesDescriptors();
		if (getValue() == null)
			return result;
		result.put(PARAGRAPH, getPropertyValue(PARAGRAPH));
		result.putAll(tFont.getStylesDescriptors());
		return result;
	}

	private static final String PARAGRAPH = "paragraph"; //$NON-NLS-1$

	
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		RWComboBoxPropertyDescriptor markupD = new RWComboBoxPropertyDescriptor(JRBaseStyle.PROPERTY_MARKUP,
				Messages.MTextElement_markup, ModelUtils.getMarkups(getJasperConfiguration()), NullEnum.INHERITED);
		markupD.setDescription(Messages.MTextElement_markup_description);
		desc.add(markupD);

		hAlignD = new HAlignPropertyDescriptor(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT,
				Messages.common_horizontal_alignment, HorizontalAlignEnum.class, NullEnum.INHERITED);
		hAlignD.setDescription(Messages.MTextElement_horizontal_alignment_description);
		desc.add(hAlignD);

		vAlignD = new VAlignPropertyDescriptor(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, Messages.common_vertical_alignment,
				VerticalAlignEnum.class, NullEnum.INHERITED);
		vAlignD.setDescription(Messages.MTextElement_vertical_alignment_description);
		desc.add(vAlignD);

		rotationD = new RotationPropertyDescriptor(JRBaseStyle.PROPERTY_ROTATION, Messages.common_rotation,
				RotationEnum.class, NullEnum.INHERITED);
		rotationD.setDescription(Messages.MTextElement_rotation_description);
		desc.add(rotationD);

		JRPropertyDescriptor paragraph = new JRPropertyDescriptor(PARAGRAPH, "Paragraph");
		desc.add(paragraph);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#textElement");

		tFont = getMFont();
		tFont.createPropertyDescriptors(desc, defaultsMap);

		paragraph.setCategory(Messages.MTextElement_text_properties_category);
		markupD.setCategory(Messages.MTextElement_text_properties_category);
		hAlignD.setCategory(Messages.MTextElement_text_properties_category);
		vAlignD.setCategory(Messages.MTextElement_text_properties_category);
		rotationD.setCategory(Messages.MTextElement_text_properties_category);

		defaultsMap.put(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_ROTATION, null);

	}

	private MFont tFont;
	private MParagraph mParagraph;
	private static JSSEnumPropertyDescriptor hAlignD;
	private static JSSEnumPropertyDescriptor vAlignD;
	private static JSSEnumPropertyDescriptor rotationD;

	private MFont getMFont() {
		if (tFont == null) {
			tFont = new MFont((JRFont) getValue());
			tFont.setJasperConfiguration(getJasperConfiguration());
			setChildListener(tFont);
		}
		return tFont;
	}

	@Override
	public Object getPropertyActualValue(Object id) {
		JRDesignTextElement jrElement = (JRDesignTextElement) getValue();

		if (id.equals(JRDesignStyle.PROPERTY_MARKUP))
			return jrElement.getMarkup();

		if (id.equals(PARAGRAPH)) {
			if (mParagraph == null) {
				mParagraph = new MParagraph(this, (JRBaseParagraph) jrElement.getParagraph());
				setChildListener(mParagraph);
			}
			return mParagraph;
		}

		if (id.equals(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT))
			return hAlignD.getEnumValue(jrElement.getHorizontalAlignmentValue());
		if (id.equals(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT))
			return vAlignD.getEnumValue(jrElement.getVerticalAlignmentValue());
		if (id.equals(JRBaseStyle.PROPERTY_ROTATION))
			return rotationD.getEnumValue(jrElement.getRotationValue());

		if (getMFont() != null) {
			Object val = tFont.getPropertyActualValue(id);
			if (val != null)
				return val;
		}

		return super.getPropertyActualValue(id);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignTextElement jrElement = (JRDesignTextElement) getValue();

		if (id.equals(JRDesignStyle.PROPERTY_MARKUP))
			return jrElement.getOwnMarkup();

		if (id.equals(PARAGRAPH)) {
			if (mParagraph == null) {
				mParagraph = new MParagraph(this, (JRBaseParagraph) jrElement.getParagraph());
				setChildListener(mParagraph);
			}
			return mParagraph;
		}

		if (id.equals(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT)){
			if (hAlignD == null) getPropertyDescriptors();
			return hAlignD.getEnumValue(jrElement.getOwnHorizontalAlignmentValue());
		}
		if (id.equals(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT)){
			if (vAlignD == null) getPropertyDescriptors();
			return vAlignD.getEnumValue(jrElement.getOwnVerticalAlignmentValue());
		}
		if (id.equals(JRBaseStyle.PROPERTY_ROTATION)){
			if (rotationD == null) getPropertyDescriptors();
			return rotationD.getEnumValue(jrElement.getOwnRotationValue());
		}

		if (getMFont() != null) {
			Object val = tFont.getPropertyValue(id);
			if (val != null)
				return val;
		}

		return super.getPropertyValue(id);
	}
	

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignTextElement jrElement = (JRDesignTextElement) getValue();
		if (id.equals(JRBaseStyle.PROPERTY_MARKUP))
			jrElement.setMarkup((String) value);

		else if (id.equals(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT))
			jrElement.setHorizontalAlignment((HorizontalAlignEnum) hAlignD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT)) {
			VerticalAlignEnum va = (VerticalAlignEnum) EnumHelper.getSetValue(VerticalAlignEnum.values(), value, 1, true);
			if (va != null && va.equals(VerticalAlignEnum.JUSTIFIED))
				va = VerticalAlignEnum.MIDDLE;
			jrElement.setVerticalAlignment(va);
		} else if (id.equals(JRBaseStyle.PROPERTY_ROTATION))
			jrElement.setRotation((RotationEnum) rotationD.getEnumValue(value));

		getMFont().setPropertyValue(id, value);

		super.setPropertyValue(id, value);
	}
	
	/**
	 * Return the graphical properties for an MTextElement
	 */
	public HashSet<String> generateGraphicalProperties(){
		HashSet<String> result = super.generateGraphicalProperties();
		result.add(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT);
		result.add(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT);
		result.add(JRBaseStyle.PROPERTY_ROTATION);
		
		result.add(JRBaseParagraph.PROPERTY_LINE_SPACING);
		result.add(JRBaseParagraph.PROPERTY_LINE_SPACING_SIZE);
		result.add(JRBaseParagraph.PROPERTY_FIRST_LINE_INDENT);
		result.add(JRBaseParagraph.PROPERTY_LEFT_INDENT);
		result.add(JRBaseParagraph.PROPERTY_RIGHT_INDENT);
		result.add(JRBaseParagraph.PROPERTY_SPACING_BEFORE);
		result.add(JRBaseParagraph.PROPERTY_SPACING_AFTER);
		result.add(JRBaseParagraph.PROPERTY_TAB_STOP_WIDTH);
		
		result.add(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT);
		result.add(JRBaseFont.PROPERTY_BOLD);
		result.add(JRBaseFont.PROPERTY_UNDERLINE);
		result.add(JRBaseFont.PROPERTY_STRIKE_THROUGH);
		result.add(JRBaseFont.PROPERTY_ITALIC);
		result.add(JRBaseFont.PROPERTY_FONT_SIZE);
		result.add(JRBaseFont.PROPERTY_FONT_NAME);
		return result;
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		JRDesignTextElement jrSource = (JRDesignTextElement) getValue();
		JRDesignTextElement jrTarget = (JRDesignTextElement)target;
		
		jrTarget.setHorizontalAlignment(jrSource.getOwnHorizontalAlignmentValue());
		jrTarget.setVerticalAlignment(jrSource.getOwnVerticalAlignmentValue());
		jrTarget.setMarkup(getStringClone(jrSource.getOwnMarkup()));
		jrTarget.setRotation(jrSource.getOwnRotationValue());
		
		jrTarget.setBold(jrSource.isOwnBold());
		jrTarget.setItalic(jrSource.isOwnItalic());
		jrTarget.setUnderline(jrSource.isOwnUnderline());
		jrTarget.setStrikeThrough(jrSource.isOwnStrikeThrough());
		jrTarget.setPdfEmbedded(jrSource.isOwnPdfEmbedded());
		jrTarget.setFontName(getStringClone(jrSource.getOwnFontName()));
		jrTarget.setFontSize(jrSource.getOwnFontsize());
		jrTarget.setPdfFontName(getStringClone(jrSource.getOwnPdfFontName()));
		jrTarget.setPdfEncoding(getStringClone(jrSource.getOwnPdfEncoding()));
		
		JRBaseParagraph jrTargetParagraph = (JRBaseParagraph)jrTarget.getParagraph();
		JRBaseParagraph jrSourceParagraph = (JRBaseParagraph) jrSource.getParagraph();
		if (jrTargetParagraph != null && jrSourceParagraph != null){
			jrTargetParagraph.setLineSpacing(jrSourceParagraph.getOwnLineSpacing());
			jrTargetParagraph.setLineSpacingSize(jrSourceParagraph.getOwnLineSpacingSize());
			jrTargetParagraph.setFirstLineIndent(jrSourceParagraph.getOwnFirstLineIndent());
			jrTargetParagraph.setLeftIndent(jrSourceParagraph.getOwnLeftIndent());
			jrTargetParagraph.setRightIndent(jrSourceParagraph.getOwnRightIndent());
			jrTargetParagraph.setSpacingAfter(jrSourceParagraph.getOwnSpacingAfter());
			jrTargetParagraph.setSpacingBefore(jrTargetParagraph.getOwnSpacingBefore());
			jrTargetParagraph.setTabStopWidth(jrSourceParagraph.getOwnTabStopWidth());
		}
	}

}
