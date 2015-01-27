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
package com.jaspersoft.studio.model.style;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.base.JRBaseFont;
import net.sf.jasperreports.engine.base.JRBaseParagraph;
import net.sf.jasperreports.engine.base.JRBaseStyle;
import net.sf.jasperreports.engine.design.JRDesignStyle;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;
import net.sf.jasperreports.engine.type.FillEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.ModeEnum;
import net.sf.jasperreports.engine.type.RotationEnum;
import net.sf.jasperreports.engine.type.ScaleImageEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.ExternalStylesManager;
import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.jface.IntegerCellEditorValidator;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.IDragable;
import com.jaspersoft.studio.model.IGraphicalPropertiesHandler;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.IPastable;
import com.jaspersoft.studio.model.MLineBox;
import com.jaspersoft.studio.model.MLinePen;
import com.jaspersoft.studio.model.text.MFont;
import com.jaspersoft.studio.model.text.MParagraph;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.box.BoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.ButtonPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.FontNamePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWFloatComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWStyleComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.pattern.PatternPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.pen.PenPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.HAlignPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSValidatedTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.OpaqueModePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.RotationPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.VAlignPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPBooleanToggle;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * The Class MStyle.
 * 
 * @author Chicu Veaceslav
 */
public class MStyle extends APropertyNode implements ICopyable, IPastable, IContainerEditPart, IDragable,
		MNotConditionalMarker {
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
			iconDescriptor = new NodeIconDescriptor("style"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m style.
	 */
	public MStyle() {
		super();
	}

	public MStyle(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	/**
	 * Instantiates a new m style.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrstyle
	 *          the jrstyle
	 * @param newIndex
	 *          the new index
	 */
	public MStyle(ANode parent, JRDesignStyle jrstyle, int newIndex) {
		super(parent, newIndex);
		setValue(jrstyle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return ((JRStyle) getValue()).getName();
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
	private static StyleNameValidator validator;

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

	private static final String LINE_PEN = "LinePen"; //$NON-NLS-1$
	private static final String LINE_BOX = "LineBox"; //$NON-NLS-1$
	private static final String PARAGRAPH = "paragraph"; //$NON-NLS-1$
	
	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		//Set into the validator the actual reference
		validator.setTargetNode(this);
	}
	
	public HashMap<String, Object> getStylesDescriptors() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (getValue() == null)
			return result;
		JRStyle element = (JRStyle) getValue();
		result.put(JRDesignStyle.PROPERTY_BACKCOLOR, element.getOwnBackcolor());
		result.put(JRDesignStyle.PROPERTY_FORECOLOR, element.getOwnForecolor());
		result.put(JRDesignStyle.PROPERTY_FILL, element.getOwnFillValue());
		result.put(JRDesignStyle.PROPERTY_FONT_NAME, element.getOwnFontName());
		result.put(JRDesignStyle.PROPERTY_FONT_SIZE, element.getOwnFontsize());
		result.put(JRDesignStyle.PROPERTY_BOLD, element.isOwnBold());
		result.put(JRDesignStyle.PROPERTY_ITALIC, element.isOwnItalic());
		result.put(JRDesignStyle.PROPERTY_UNDERLINE, element.isOwnUnderline());
		result.put(JRDesignStyle.PROPERTY_STRIKE_THROUGH, element.isOwnStrikeThrough());
		result.put(JRDesignStyle.PROPERTY_HORIZONTAL_ALIGNMENT, element.getOwnHorizontalAlignmentValue());
		result.put(JRDesignStyle.PROPERTY_MARKUP, element.getOwnMarkup());
		result.put(JRDesignStyle.PROPERTY_MODE, element.getOwnModeValue());
		result.put(JRDesignStyle.PROPERTY_PATTERN, element.getOwnPattern());
		result.put(JRDesignStyle.PROPERTY_RADIUS, element.getOwnRadius());
		result.put(JRDesignStyle.PROPERTY_ROTATION, element.getOwnRotationValue());
		result.put(JRDesignStyle.PROPERTY_SCALE_IMAGE, element.getOwnScaleImageValue());
		result.put(JRDesignStyle.PROPERTY_VERTICAL_ALIGNMENT, element.getOwnVerticalAlignmentValue());
		result.put(JRDesignStyle.PROPERTY_BLANK_WHEN_NULL, element.isOwnBlankWhenNull());
		result.put(PARAGRAPH, getPropertyValue(PARAGRAPH));
		MLinePen linePen = (MLinePen) getPropertyValue(LINE_PEN);
		result.put(LINE_PEN, linePen);
		MLineBox lineBox = (MLineBox) getPropertyValue(LINE_BOX);
		result.put(LINE_BOX, lineBox);
		return result;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {

		styleD = new RWStyleComboBoxPropertyDescriptor(JRDesignStyle.PROPERTY_PARENT_STYLE, Messages.common_parent_style, new String[] { "" }, 	NullEnum.NULL);
		styleD.setDescription(Messages.MStyle_parent_style_description);
		styleD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#reportElement_style"));
		desc.add(styleD);

		validator = new StyleNameValidator();
		validator.setTargetNode(this);
		JSSValidatedTextPropertyDescriptor nameD = new JSSValidatedTextPropertyDescriptor(JRDesignStyle.PROPERTY_NAME, Messages.common_name, validator);
		nameD.setDescription(Messages.MStyle_name_description);
		desc.add(nameD);

		PenPropertyDescriptor linePenD = new PenPropertyDescriptor(LINE_PEN, Messages.common_line_pen);
		linePenD.setDescription(Messages.MStyle_line_pen_description);
		desc.add(linePenD);

		BoxPropertyDescriptor lineBoxD = new BoxPropertyDescriptor(LINE_BOX, Messages.common_line_box);
		lineBoxD.setDescription(Messages.MStyle_line_box_description);
		desc.add(lineBoxD);
		lineBoxD.setCategory(Messages.common_graphic);

		ColorPropertyDescriptor forecolorD = new ColorPropertyDescriptor(JRDesignStyle.PROPERTY_FORECOLOR,
				Messages.common_forecolor, NullEnum.INHERITED);
		forecolorD.setDescription(Messages.MStyle_forecolor_description);
		desc.add(forecolorD);

		ColorPropertyDescriptor backcolorD = new ColorPropertyDescriptor(JRDesignStyle.PROPERTY_BACKCOLOR,
				Messages.common_backcolor, NullEnum.INHERITED);
		backcolorD.setDescription(Messages.MStyle_backcolor_description);
		desc.add(backcolorD);

		IntegerPropertyDescriptor radiusD = new IntegerPropertyDescriptor(JRBaseStyle.PROPERTY_RADIUS,
				Messages.common_radius);
		radiusD.setDescription(Messages.MStyle_radius_description);
		desc.add(radiusD);

		fillD = new JSSEnumPropertyDescriptor(JRBaseStyle.PROPERTY_FILL, Messages.common_fill, FillEnum.class,
				NullEnum.UNDEFINED);
		fillD.setDescription(Messages.MStyle_fill_description);
		desc.add(fillD);

		scaleD = new JSSEnumPropertyDescriptor(JRBaseStyle.PROPERTY_SCALE_IMAGE, Messages.MStyle_scale,
				ScaleImageEnum.class, NullEnum.UNDEFINED);
		scaleD.setDescription(Messages.MStyle_scale_description);
		desc.add(scaleD);

		halignD = new HAlignPropertyDescriptor(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT,
				Messages.common_horizontal_alignment, HorizontalAlignEnum.class, NullEnum.INHERITED);
		halignD.setDescription(Messages.MStyle_horizontal_alignment_description);
		desc.add(halignD);

		valignD = new VAlignPropertyDescriptor(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, Messages.common_vertical_alignment,
				VerticalAlignEnum.class, NullEnum.INHERITED);
		valignD.setDescription(Messages.MStyle_vertical_alignment_description);
		desc.add(valignD);

		rotationD = new RotationPropertyDescriptor(JRBaseStyle.PROPERTY_ROTATION, Messages.common_rotation,
				RotationEnum.class, NullEnum.INHERITED);
		rotationD.setDescription(Messages.MStyle_rotation_description);
		desc.add(rotationD);

		modeD = new OpaqueModePropertyDescriptor(JRBaseStyle.PROPERTY_MODE, Messages.MStyle_mode, ModeEnum.class,
				NullEnum.INHERITED);
		CheckBoxPropertyDescriptor opaqueDBool = new CheckBoxPropertyDescriptor(JRBaseStyle.PROPERTY_MODE,
				Messages.common_opaque);
		opaqueDBool.setDescription(Messages.MGraphicElement_opaque_description);
		desc.add(opaqueDBool);

		CheckBoxPropertyDescriptor blankWhenNullD = new CheckBoxPropertyDescriptor(JRBaseStyle.PROPERTY_BLANK_WHEN_NULL,
				Messages.common_blank_when_null, NullEnum.INHERITED);
		blankWhenNullD.setDescription(Messages.MStyle_blank_when_null_description);
		desc.add(blankWhenNullD);

		CheckBoxPropertyDescriptor boldD = new CheckBoxPropertyDescriptor(JRBaseFont.PROPERTY_BOLD, Messages.common_bold,
				NullEnum.INHERITED) {
			@Override
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				return new SPBooleanToggle(parent, section, this, JaspersoftStudioPlugin.getInstance().getImage(
						"icons/resources/bold.png"));
			}
		};
		boldD.setDescription(Messages.MFont_bold_description);
		desc.add(boldD);

		CheckBoxPropertyDescriptor italicD = new CheckBoxPropertyDescriptor(JRBaseFont.PROPERTY_ITALIC,
				Messages.common_italic, NullEnum.INHERITED) {
			@Override
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				return new SPBooleanToggle(parent, section, this, JaspersoftStudioPlugin.getInstance().getImage(
						"icons/resources/italic.png"));
			}
		};
		italicD.setDescription(Messages.MFont_italic_description);
		desc.add(italicD);

		CheckBoxPropertyDescriptor underlineD = new CheckBoxPropertyDescriptor(JRBaseFont.PROPERTY_UNDERLINE,
				Messages.common_underline, NullEnum.INHERITED) {
			@Override
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				return new SPBooleanToggle(parent, section, this, JaspersoftStudioPlugin.getInstance().getImage(
						"icons/resources/underline.png"));
			}
		};
		underlineD.setDescription(Messages.MFont_underline_description);
		desc.add(underlineD);

		CheckBoxPropertyDescriptor strikeThroughD = new CheckBoxPropertyDescriptor(JRBaseFont.PROPERTY_STRIKE_THROUGH,
				Messages.common_strike_trough, NullEnum.INHERITED) {
			@Override
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				return new SPBooleanToggle(parent, section, this, JaspersoftStudioPlugin.getInstance().getImage(
						"icons/resources/strikethrought.png"));
			}
		};
		strikeThroughD.setDescription(Messages.MFont_strike_trough_description);
		desc.add(strikeThroughD);

		CheckBoxPropertyDescriptor defaultD = new CheckBoxPropertyDescriptor(JRDesignStyle.PROPERTY_DEFAULT,
				Messages.MStyle_default_style, NullEnum.NOTNULL);
		defaultD.setDescription(Messages.MStyle_default_style_description);
		desc.add(defaultD);

		RWComboBoxPropertyDescriptor markupD = new RWComboBoxPropertyDescriptor(JRBaseStyle.PROPERTY_MARKUP,
				Messages.MStyle_markup, ModelUtils.getMarkups(getJasperConfiguration()), NullEnum.INHERITED);
		markupD.setDescription(Messages.MStyle_markup_description);
		desc.add(markupD);

		FontNamePropertyDescriptor fontNameD = new FontNamePropertyDescriptor(JRBaseStyle.PROPERTY_FONT_NAME,
				Messages.common_font_name, getJasperConfiguration().getFontList(), NullEnum.INHERITED);
		fontNameD.setDescription(Messages.MStyle_font_name_description);
		desc.add(fontNameD);

		RWFloatComboBoxPropertyDescriptor fontSizeD = new RWFloatComboBoxPropertyDescriptor(JRBaseStyle.PROPERTY_FONT_SIZE,
				Messages.common_font_size, ModelUtils.FONT_SIZES, NullEnum.INHERITED, false);
		fontSizeD.setDescription(Messages.MStyle_font_size_description);
		fontSizeD.setValidator(new IntegerCellEditorValidator());
		desc.add(fontSizeD);

		ButtonPropertyDescriptor fontIncrement = new ButtonPropertyDescriptor(MFont.FONT_INCREMENT, this);
		desc.add(fontIncrement);

		PatternPropertyDescriptor patternD = new PatternPropertyDescriptor(JRBaseStyle.PROPERTY_PATTERN,
				Messages.common_pattern);
		patternD.setDescription(Messages.MStyle_pattern_description);
		desc.add(patternD);

		JRPropertyDescriptor paragraph = new JRPropertyDescriptor(PARAGRAPH, "Paragraph");
		desc.add(paragraph);

		paragraph.setCategory(Messages.common_text);
		styleD.setCategory(Messages.MStyle_Style_properties);
		nameD.setCategory(Messages.MStyle_Style_properties);
		defaultD.setCategory(Messages.MStyle_Style_properties);

		forecolorD.setCategory(Messages.MStyle_common_category);
		backcolorD.setCategory(Messages.MStyle_common_category);
		modeD.setCategory(Messages.MStyle_common_category);

		linePenD.setCategory(Messages.common_graphic);

		radiusD.setCategory(Messages.common_graphic);
		scaleD.setCategory(Messages.common_graphic);
		fillD.setCategory(Messages.common_graphic);

		patternD.setCategory(Messages.common_text);
		blankWhenNullD.setCategory(Messages.common_text);
		rotationD.setCategory(Messages.common_text);
		markupD.setCategory(Messages.common_text);
		halignD.setCategory(Messages.common_text);
		valignD.setCategory(Messages.common_text);

		fontNameD.setCategory(Messages.MStyle_text_font_category);
		fontSizeD.setCategory(Messages.MStyle_text_font_category);
		boldD.setCategory(Messages.MStyle_text_font_category);
		italicD.setCategory(Messages.MStyle_text_font_category);
		underlineD.setCategory(Messages.MStyle_text_font_category);
		strikeThroughD.setCategory(Messages.MStyle_text_font_category);

		defaultsMap.put(JRBaseStyle.PROPERTY_FORECOLOR, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_BACKCOLOR, null);

		defaultsMap.put(JRBaseStyle.PROPERTY_FILL, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_SCALE_IMAGE, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_ROTATION, null);
		defaultsMap.put(JRBaseStyle.PROPERTY_MODE, modeD.getEnumValue(ModeEnum.OPAQUE));

		defaultsMap.put(JRDesignStyle.PROPERTY_BLANK_WHEN_NULL, Boolean.FALSE);
		defaultsMap.put(JRBaseStyle.PROPERTY_STRIKE_THROUGH, Boolean.FALSE);
		defaultsMap.put(JRBaseStyle.PROPERTY_UNDERLINE, Boolean.FALSE);
		defaultsMap.put(JRBaseStyle.PROPERTY_ITALIC, Boolean.FALSE);
		defaultsMap.put(JRBaseStyle.PROPERTY_BOLD, Boolean.FALSE);
		defaultsMap.put(JRBaseStyle.PROPERTY_FONT_NAME, "SansSerif"); //$NON-NLS-1$
		defaultsMap.put(JRBaseStyle.PROPERTY_FONT_SIZE, "10"); //$NON-NLS-1$

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#style");
	}

	private MLinePen linePen;
	private MLineBox lineBox;
	private MParagraph mParagraph;
	private static RWComboBoxPropertyDescriptor styleD;
	private static JSSEnumPropertyDescriptor fillD;
	private static JSSEnumPropertyDescriptor scaleD;
	private static JSSEnumPropertyDescriptor halignD;
	private static JSSEnumPropertyDescriptor valignD;
	private static JSSEnumPropertyDescriptor rotationD;
	private static JSSEnumPropertyDescriptor modeD;

	/**
	 * Return the internal style used. If the internal style is a reference to a removed style
	 * then it is also removed from the element
	 */
	public JRStyle getActualStyle(){
		JRDesignStyle jrElement = (JRDesignStyle) getValue();
		//Check if the used style is valid otherwise set it to null
		if (jrElement.getStyle() != null && !getJasperDesign().getStylesMap().containsKey(jrElement.getStyle().getName())){
			setPropertyValue(JRDesignStyle.PROPERTY_PARENT_STYLE, null);
		}
		if (jrElement.getStyle() != null){
			return jrElement.getStyle();
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		if (getValue() == null)
			return null;
		if (getValue() instanceof JRDesignStyle) {
			JRDesignStyle jrstyle = (JRDesignStyle) getValue();
			if (id.equals(JRDesignStyle.PROPERTY_NAME))
				return jrstyle.getName();
			if (id.equals(JRDesignStyle.PROPERTY_DEFAULT))
				return new Boolean(jrstyle.isDefault());
			if (id.equals(JRDesignStyle.PROPERTY_PARENT_STYLE)) {
				if (jrstyle.getStyleNameReference() != null)
					return jrstyle.getStyleNameReference();
				JRStyle actualStyle = getActualStyle();
				return actualStyle != null ? actualStyle.getName() : ""; //$NON-NLS-1$
			}
			if (id.equals(PARAGRAPH)) {
				if (mParagraph == null) {
					mParagraph = new MParagraph(this, (JRBaseParagraph) jrstyle.getParagraph());
					setChildListener(mParagraph);
				}
				return mParagraph;
			}
		}

		JRBaseStyle jrstyle = (JRBaseStyle) getValue();
		if (id.equals(LINE_PEN)) {
			if (linePen == null) {
				linePen = new MLinePen(jrstyle.getLinePen());
				setChildListener(linePen);
			}
			return linePen;
		}
		if (id.equals(LINE_BOX)) {
			if (lineBox == null) {
				lineBox = new MLineBox(jrstyle.getLineBox());
				setChildListener(lineBox);
			}
			return lineBox;
		}
		if (id.equals(JRBaseStyle.PROPERTY_PATTERN))
			return jrstyle.getOwnPattern();
		if (id.equals(JRBaseStyle.PROPERTY_RADIUS))
			return jrstyle.getOwnRadius();
		if (id.equals(JRBaseStyle.PROPERTY_MARKUP))
			return jrstyle.getOwnMarkup();
		if (id.equals(JRDesignStyle.PROPERTY_FORECOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrstyle.getOwnForecolor());
		else if (id.equals(JRDesignStyle.PROPERTY_BACKCOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrstyle.getOwnBackcolor());

		if (id.equals(JRBaseStyle.PROPERTY_FILL))
			return fillD.getEnumValue(jrstyle.getOwnFillValue());
		if (id.equals(JRBaseStyle.PROPERTY_SCALE_IMAGE))
			return scaleD.getEnumValue(jrstyle.getOwnScaleImageValue());
		if (id.equals(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT))
			return halignD.getEnumValue(jrstyle.getOwnHorizontalAlignmentValue());
		if (id.equals(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT))
			return valignD.getEnumValue(jrstyle.getOwnVerticalAlignmentValue());
		if (id.equals(JRBaseStyle.PROPERTY_ROTATION))
			return rotationD.getEnumValue(jrstyle.getOwnRotationValue());
		if (id.equals(JRBaseStyle.PROPERTY_MODE)) {
			if (modeD == null)
				modeD = new OpaqueModePropertyDescriptor(JRBaseStyle.PROPERTY_MODE, Messages.MStyle_mode, ModeEnum.class,
						NullEnum.INHERITED);
			ModeEnum modeValue = jrstyle.getOwnModeValue();
			return modeValue != null ? modeValue.equals(ModeEnum.TRANSPARENT) : null;
		}
		if (id.equals(JRBaseStyle.PROPERTY_BLANK_WHEN_NULL))
			return jrstyle.isOwnBlankWhenNull();
		if (id.equals(JRBaseStyle.PROPERTY_STRIKE_THROUGH))
			return jrstyle.isOwnStrikeThrough();
		if (id.equals(JRBaseStyle.PROPERTY_UNDERLINE))
			return jrstyle.isOwnUnderline();
		if (id.equals(JRBaseStyle.PROPERTY_ITALIC))
			return jrstyle.isOwnItalic();
		if (id.equals(JRBaseStyle.PROPERTY_BOLD))
			return jrstyle.isOwnBold();
		if (id.equals(JRBaseStyle.PROPERTY_FONT_NAME))
			return jrstyle.getOwnFontName();
		if (id.equals(JRBaseStyle.PROPERTY_FONT_SIZE))
			return jrstyle.getOwnFontsize() != null ? jrstyle.getOwnFontsize().toString() : ""; //$NON-NLS-1$
		if (lineBox != null) {
			Object val = lineBox.getPropertyValue(id);
			if (val != null)
				return val;
		}
		if (linePen != null) {
			Object val = linePen.getPropertyValue(id);
			if (val != null)
				return val;
		}
		return null;
	}

	public Object getPropertyActualValue(Object id) {
		if (getValue() == null)
			return null;
		JRBaseStyle jrstyle = (JRBaseStyle) getValue();
		if (id.equals(JRBaseStyle.PROPERTY_PATTERN))
			return jrstyle.getPattern();
		if (id.equals(JRBaseStyle.PROPERTY_RADIUS))
			return jrstyle.getRadius();
		if (id.equals(JRBaseStyle.PROPERTY_MARKUP))
			return jrstyle.getMarkup();
		if (id.equals(JRDesignStyle.PROPERTY_FORECOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrstyle.getForecolor());
		else if (id.equals(JRDesignStyle.PROPERTY_BACKCOLOR))
			return Colors.getSWTRGB4AWTGBColor(jrstyle.getBackcolor());

		if (id.equals(JRBaseStyle.PROPERTY_FILL))
			return fillD.getEnumValue(jrstyle.getFillValue());
		if (id.equals(JRBaseStyle.PROPERTY_SCALE_IMAGE))
			return scaleD.getEnumValue(jrstyle.getScaleImageValue());
		if (id.equals(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT))
			return halignD.getEnumValue(jrstyle.getHorizontalAlignmentValue());
		if (id.equals(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT))
			return valignD.getEnumValue(jrstyle.getVerticalAlignmentValue());
		if (id.equals(JRBaseStyle.PROPERTY_ROTATION))
			return rotationD.getEnumValue(jrstyle.getRotationValue());
		if (id.equals(JRBaseStyle.PROPERTY_MODE)) {
			if (modeD == null){
				modeD = new OpaqueModePropertyDescriptor(JRBaseStyle.PROPERTY_MODE, Messages.MStyle_mode, ModeEnum.class,
						NullEnum.INHERITED);
			}
			if (jrstyle.getModeValue() == null) return true; //By default the style is transparent
			else return modeD.getEnumValue(jrstyle.getModeValue()).equals(modeD.getEnumValue(ModeEnum.TRANSPARENT));
		}
		if (id.equals(JRBaseStyle.PROPERTY_BLANK_WHEN_NULL))
			return jrstyle.isBlankWhenNull();
		if (id.equals(JRBaseStyle.PROPERTY_STRIKE_THROUGH))
			return jrstyle.isStrikeThrough();
		if (id.equals(JRBaseStyle.PROPERTY_UNDERLINE))
			return jrstyle.isUnderline();
		if (id.equals(JRBaseStyle.PROPERTY_ITALIC))
			return jrstyle.isItalic();
		if (id.equals(JRBaseStyle.PROPERTY_BOLD))
			return jrstyle.isBold();
		if (id.equals(JRBaseStyle.PROPERTY_FONT_NAME))
			return jrstyle.getFontName();
		if (id.equals(JRBaseStyle.PROPERTY_FONT_SIZE))
			return jrstyle.getFontsize() != null ? jrstyle.getFontsize().toString() : ""; //$NON-NLS-1$
		if (lineBox != null) {
			Object val = lineBox.getPropertyActualValue(id);
			if (val != null)
				return val;
		}
		if (linePen != null) {
			Object val = linePen.getPropertyActualValue(id);
			if (val != null)
				return val;
		}
		return super.getPropertyActualValue(id);
	}

	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		if (!isEditable())
			return;
		if (getValue() instanceof JRDesignStyle) {
			JRDesignStyle jrstyle = (JRDesignStyle) getValue();
			if (id.equals(JRDesignStyle.PROPERTY_NAME)){
				jrstyle.setName((String) value);
			} else if (id.equals(JRDesignStyle.PROPERTY_DEFAULT)) {
				jrstyle.setDefault(((Boolean) value).booleanValue());
			} else if (id.equals(JRDesignStyle.PROPERTY_PARENT_STYLE)) {
				if (!Misc.isNullOrEmpty((String) value)) { //$NON-NLS-1$
					JRStyle style = (JRStyle) getJasperDesign().getStylesMap().get(value);
					if (style != null) {
						//FIXME: It is important to set a null first the external style, because it is returned first on the getPropertyValue and this raise a lot of events
						jrstyle.setParentStyleNameReference(null);
						jrstyle.setParentStyle(style);
					} else {
						jrstyle.setParentStyleNameReference((String) value);
						//Set the external style as parent style if existing, to resolve JR resolving problem at design time
						fixExternalStyleReference((String) value);
					}
				} else {
					//remove the style
					jrstyle.setParentStyleNameReference(null);
					jrstyle.setParentStyle(null);
				}
			}
		}
		JRBaseStyle jrstyle = (JRBaseStyle) getValue();
		if (id.equals(JRDesignStyle.PROPERTY_PATTERN))
			jrstyle.setPattern((String) value);
		else if (id.equals(JRDesignStyle.PROPERTY_RADIUS))
			jrstyle.setRadius((Integer) value);
		else if (id.equals(JRBaseStyle.PROPERTY_MARKUP))
			jrstyle.setMarkup((String) value);
		else if (id.equals(JRBaseStyle.PROPERTY_FORECOLOR))
			jrstyle.setForecolor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(JRBaseStyle.PROPERTY_BACKCOLOR))
			jrstyle.setBackcolor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));

		else if (id.equals(JRBaseStyle.PROPERTY_FILL))
			jrstyle.setFill((FillEnum) fillD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_SCALE_IMAGE))
			jrstyle.setScaleImage((ScaleImageEnum) scaleD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_HORIZONTAL_ALIGNMENT))
			jrstyle.setHorizontalAlignment((HorizontalAlignEnum) halignD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_VERTICAL_ALIGNMENT))
			jrstyle.setVerticalAlignment((VerticalAlignEnum) valignD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_ROTATION))
			jrstyle.setRotation((RotationEnum) rotationD.getEnumValue(value));
		else if (id.equals(JRBaseStyle.PROPERTY_MODE))
			if (value == null)
				jrstyle.setMode(null);
			else if ((Boolean) value)
				jrstyle.setMode(ModeEnum.TRANSPARENT);
			else
				jrstyle.setMode(ModeEnum.OPAQUE);
		else if (id.equals(JRBaseStyle.PROPERTY_BLANK_WHEN_NULL))
			jrstyle.setBlankWhenNull((Boolean) value);
		else if (id.equals(JRBaseStyle.PROPERTY_STRIKE_THROUGH))
			jrstyle.setStrikeThrough((Boolean) value);
		else if (id.equals(JRBaseStyle.PROPERTY_UNDERLINE))
			jrstyle.setUnderline((Boolean) value);
		else if (id.equals(JRBaseStyle.PROPERTY_ITALIC))
			jrstyle.setItalic((Boolean) value);
		else if (id.equals(JRBaseStyle.PROPERTY_BOLD))
			jrstyle.setBold((Boolean) value);
		else if (id.equals(JRBaseStyle.PROPERTY_FONT_NAME))
			jrstyle.setFontName((String) value);
		else if (id.equals(JRBaseStyle.PROPERTY_FONT_SIZE))
			if ((value instanceof String && value.toString().length() == 0) || value == null)
				jrstyle.setFontSize((Float)null);
			else
				jrstyle.setFontSize(new Float((String) value));
		else if (lineBox != null) {
			lineBox.setPropertyValue(id, value);
		}
		if (linePen != null) {
			linePen.setPropertyValue(id, value);
		}
	}
	
	/**
	 * When the external style change we need also to set the reference to the JRStyle 
	 * as parent style. This normally should be done by jasper reports when the reports
	 * is executed, but since we need to show it at design time we need this trick
	 * to have the external styles resolved correctly when an element is painted. Anyway
	 * if the selected styles can't be found this method set the style to null
	 * 
	 * @param externalStyleName name of the external style
	 */
	private void fixExternalStyleReference(String externalStyleName){
		if (externalStyleName != null){
			JRDesignStyle jrstyle = (JRDesignStyle) getValue();
			jrstyle.setParentStyle(ExternalStylesManager.getExternalStyle((String) externalStyleName, getJasperConfiguration()));
		}
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		//Set the external style as parent style if existing, to resolve JR resolving problem at design time
		fixExternalStyleReference(((JRBaseStyle)value).getStyleNameReference());
	}

	/**
	 * Creates the jr style.
	 * 
	 * @param jrDesign
	 *          the jr design
	 * @return the jR design style
	 */
	public static JRDesignStyle createJRStyle(JasperDesign jrDesign) {
		JRDesignStyle jrDesignStyle = new JRDesignStyle();
		jrDesignStyle.setName(ModelUtils.getDefaultName(jrDesign.getStylesMap(), "Style")); //$NON-NLS-1$
		return jrDesignStyle;
	}
	
	/**
	 * Search all the nodes that are using this styles and set the flag to tell the graphic manager
	 * to repaint them
	 * 
	 * @param childerns the children of the actual level
	 */
	private void setStyleRefresh(List<INode> childerns){
		for(INode child : childerns){
			if (child instanceof IGraphicalPropertiesHandler){
				IGraphicalPropertiesHandler graphicalElement = (IGraphicalPropertiesHandler)child;
				if (getValue().isDefault() || graphicalElement.getUsedStyles().contains(getValue().getName())){
					graphicalElement.setChangedProperty(true);
				}
			}
			setStyleRefresh(new ArrayList<INode>(child.getChildren()));
		}
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.ANode#propertyChange(java.beans.PropertyChangeEvent)
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (JRDesignStyle.PROPERTY_NAME.equals(evt.getPropertyName())){
			JasperDesign design = getJasperDesign();
			JRBaseStyle jrstyle = (JRBaseStyle) getValue();
			if (design != null){
				design.getStylesMap().remove(evt.getOldValue());
				design.getStylesMap().put(jrstyle.getName(), jrstyle);
			}
		}else if (evt.getPropertyName().equals(JRDesignStyle.PROPERTY_CONDITIONAL_STYLES) && evt.getSource() == getValue()) {
			if (evt.getOldValue() == null && evt.getNewValue() != null) {
				int newIndex = -1;
				if (evt instanceof CollectionElementAddedEvent) {
					newIndex = ((CollectionElementAddedEvent) evt).getAddedIndex();
				}
				// add the node to this parent
				ReportFactory.createNode(this, evt.getNewValue(), newIndex);
			} else if (evt.getOldValue() != null && evt.getNewValue() == null) {
				// delete
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue()) {
						removeChild((ANode) n);
						break;
					}
				}
			} else {
			// changed
				for (INode n : getChildren()) {
					if (n.getValue() == evt.getOldValue())
						n.setValue(evt.getNewValue());
				}
			}
		}
		fireUpdateForElements();
		evt = new PropertyChangeEvent(getValue(), evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		super.propertyChange(evt);
	}
	
	/**
	 * Search in background the elements that are using this styles and mark them for the refresh
	 */
	private void fireUpdateForElements(){
		Runnable notifier = new Runnable() {
	    public void run() {
	  		//Avoid the refresh if the style is not in the hierarchy
	    	INode root = getRoot();
	    	if (root != null) {
		  		setStyleRefresh(new ArrayList<INode>(root.getChildren()));
	  		}
	    }
		};
		new Thread(notifier).start();
	}
	
	/**
	 * Return the style element
	 */
	public JRStyle getValue(){
		return (JRStyle) super.getValue();
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MStyles)
			return true;
		return false;
	}

}
