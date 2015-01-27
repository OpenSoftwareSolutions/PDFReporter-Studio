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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.base.JRBaseLineBox;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.pen.PenPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;

public class MLineBox extends APropertyNode implements IPropertySource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MLineBox(JRLineBox lineBox) {
		super();
		setValue(lineBox);
	}

	@Override
	public HashMap<String, Object> getStylesDescriptors() {
		HashMap<String, Object> result = new HashMap<String, Object>();
		if (getValue() == null)
			return result;
		JRBaseLineBox element = (JRBaseLineBox) getValue();
		result.put(JRBaseLineBox.PROPERTY_PADDING, element.getOwnPadding());
		result.put(JRBaseLineBox.PROPERTY_TOP_PADDING, element.getOwnTopPadding());
		result.put(JRBaseLineBox.PROPERTY_BOTTOM_PADDING, element.getOwnBottomPadding());
		result.put(JRBaseLineBox.PROPERTY_LEFT_PADDING, element.getOwnLeftPadding());
		result.put(JRBaseLineBox.PROPERTY_RIGHT_PADDING, element.getOwnRightPadding());
		MLinePen linePen = (MLinePen) getPropertyValue(LINE_PEN);
		result.put(LINE_PEN, linePen);
		MLinePen linePenTop = (MLinePen) getPropertyValue(LINE_PEN_TOP);
		result.put(LINE_PEN_TOP, linePenTop);
		MLinePen linePenBottom = (MLinePen) getPropertyValue(LINE_PEN_BOTTOM);
		result.put(LINE_PEN_BOTTOM, linePenBottom);
		MLinePen linePenLeft = (MLinePen) getPropertyValue(LINE_PEN_LEFT);
		result.put(LINE_PEN_LEFT, linePenLeft);
		MLinePen linePenRight = (MLinePen) getPropertyValue(LINE_PEN_RIGHT);
		result.put(LINE_PEN_RIGHT, linePenRight);
		return result;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		IntegerPropertyDescriptor paddingD = new IntegerPropertyDescriptor(JRBaseLineBox.PROPERTY_PADDING,
				Messages.common_padding);
		paddingD.setDescription(Messages.MLineBox_padding_description);
		desc.add(paddingD);

		IntegerPropertyDescriptor paddingLeftD = new IntegerPropertyDescriptor(JRBaseLineBox.PROPERTY_LEFT_PADDING,
				Messages.MLineBox_left_padding);
		paddingLeftD.setDescription(Messages.MLineBox_left_padding_description);
		desc.add(paddingLeftD);

		IntegerPropertyDescriptor paddingRightD = new IntegerPropertyDescriptor(JRBaseLineBox.PROPERTY_RIGHT_PADDING,
				Messages.MLineBox_right_padding);
		paddingRightD.setDescription(Messages.MLineBox_right_padding_description);
		desc.add(paddingRightD);

		IntegerPropertyDescriptor paddingTopD = new IntegerPropertyDescriptor(JRBaseLineBox.PROPERTY_TOP_PADDING,
				Messages.MLineBox_top_padding);
		paddingTopD.setDescription(Messages.MLineBox_top_padding_description);
		desc.add(paddingTopD);

		IntegerPropertyDescriptor paddingBottomD = new IntegerPropertyDescriptor(JRBaseLineBox.PROPERTY_BOTTOM_PADDING,
				Messages.MLineBox_bottom_padding);
		paddingBottomD.setDescription(Messages.MLineBox_bottom_padding_description);
		desc.add(paddingBottomD);

		paddingD.setCategory(Messages.common_padding);
		paddingBottomD.setCategory(Messages.common_padding);
		paddingTopD.setCategory(Messages.common_padding);
		paddingLeftD.setCategory(Messages.common_padding);
		paddingRightD.setCategory(Messages.common_padding);
		// --------------------------------------------------------------------------------------------------------------
		// pen
		PenPropertyDescriptor linePenD = new PenPropertyDescriptor(LINE_PEN, Messages.common_line_pen);
		linePenD.setDescription(Messages.MLineBox_line_pen_description);
		desc.add(linePenD);

		PenPropertyDescriptor linePenTopD = new PenPropertyDescriptor(LINE_PEN_TOP, Messages.MLineBox_line_pen_top);
		linePenTopD.setDescription(Messages.MLineBox_line_pen_top_description);
		desc.add(linePenTopD);

		PenPropertyDescriptor linePenBottomD = new PenPropertyDescriptor(LINE_PEN_BOTTOM, Messages.MLineBox_line_pen_bottom);
		linePenBottomD.setDescription(Messages.MLineBox_line_pen_bottom_description);
		desc.add(linePenBottomD);

		PenPropertyDescriptor linePenLeftD = new PenPropertyDescriptor(LINE_PEN_LEFT, Messages.MLineBox_line_pen_left);
		linePenLeftD.setDescription(Messages.MLineBox_line_pen_left_description);
		desc.add(linePenLeftD);

		PenPropertyDescriptor linePenRightD = new PenPropertyDescriptor(LINE_PEN_RIGHT, Messages.MLineBox_line_pen_right);
		linePenRightD.setDescription(Messages.MLineBox_line_pen_right_description);
		desc.add(linePenRightD);

		defaultsMap.put(JRBaseLineBox.PROPERTY_PADDING, null);
		defaultsMap.put(JRBaseLineBox.PROPERTY_LEFT_PADDING, null);
		defaultsMap.put(JRBaseLineBox.PROPERTY_RIGHT_PADDING, null);
		defaultsMap.put(JRBaseLineBox.PROPERTY_TOP_PADDING, null);
		defaultsMap.put(JRBaseLineBox.PROPERTY_BOTTOM_PADDING, null);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#box");
	}

	public static final String LINE_PEN = "LinePen"; //$NON-NLS-1$
	public static final String LINE_PEN_TOP = "LinePen_TOP"; //$NON-NLS-1$
	public static final String LINE_PEN_BOTTOM = "LinePen_BOTTOM"; //$NON-NLS-1$
	public static final String LINE_PEN_LEFT = "LinePen_LEFT"; //$NON-NLS-1$
	public static final String LINE_PEN_RIGHT = "LinePen_RIGHT"; //$NON-NLS-1$

	private MLinePen linePen;
	private MLinePen linePenTop;
	private MLinePen linePenBottom;
	private MLinePen linePenLeft;
	private MLinePen linePenRight;

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
		JRLineBox lineBox = (JRLineBox) getValue();
		if (lineBox != null) {
			if (id.equals(JRBaseLineBox.PROPERTY_PADDING))
				return lineBox.getOwnPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_LEFT_PADDING))
				return lineBox.getOwnLeftPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_RIGHT_PADDING))
				return lineBox.getOwnRightPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_TOP_PADDING))
				return lineBox.getOwnTopPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING))
				return lineBox.getOwnBottomPadding();
			// ----------------------------------------------------
			if (id.equals(LINE_PEN)) {
					return getLinePen(lineBox);
			}
			if (id.equals(LINE_PEN_TOP)) {
					return getTopLinePen(lineBox);
			}
			if (id.equals(LINE_PEN_BOTTOM)) {
					return getBottomLinePen(lineBox);
			}
			if (id.equals(LINE_PEN_LEFT)) {
					return getLeftLinePen(lineBox);
			}
			if (id.equals(LINE_PEN_RIGHT)) {
					return getRightLinePen(lineBox);
			}
		}
		return null;
	}
	
	private MLinePen getLinePen(JRLineBox lineBox){
		if (linePen == null) {
			linePen = new MLinePen(lineBox.getPen());
			setChildListener(linePen);
			linePen.getPropertyDescriptors();
		}
		return linePen;
	}
	
	private MLinePen getTopLinePen(JRLineBox lineBox){
		if (linePenTop == null) {
			linePenTop = new MLinePen(lineBox.getTopPen());
			setChildListener(linePenTop);
			linePenTop.getPropertyDescriptors();
		}
		return linePenTop;
	}
	
	private MLinePen getBottomLinePen(JRLineBox lineBox){
		if (linePenBottom == null) {
			linePenBottom = new MLinePen(lineBox.getBottomPen());
			setChildListener(linePenBottom);
			linePenBottom.getPropertyDescriptors();
		}
		return linePenBottom;
	}
	
	private MLinePen getLeftLinePen(JRLineBox lineBox){
		if (linePenLeft == null) {
			linePenLeft = new MLinePen(lineBox.getLeftPen());
			setChildListener(linePenLeft);
			linePenLeft.getPropertyDescriptors();
		}
		return linePenLeft;
	}
	
	private MLinePen getRightLinePen(JRLineBox lineBox){
		if (linePenRight == null) {
			linePenRight = new MLinePen(lineBox.getRightPen());
			setChildListener(linePenRight);
			linePenRight.getPropertyDescriptors();
		}
		return linePenRight;
	}

	public Object getPropertyActualValue(Object id) {
		// pen
		JRLineBox lineBox = (JRLineBox) getValue();
		if (lineBox != null) {
			if (id.equals(JRBaseLineBox.PROPERTY_PADDING))
				return lineBox.getPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_LEFT_PADDING))
				return lineBox.getLeftPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_RIGHT_PADDING))
				return lineBox.getRightPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_TOP_PADDING))
				return lineBox.getTopPadding();
			if (id.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING))
				return lineBox.getBottomPadding();
			// ----------------------------------------------------
			if (id.equals(LINE_PEN)) {
				if (linePen == null) {
					linePen = new MLinePen(lineBox.getPen());
					setChildListener(linePen);
					linePen.getPropertyDescriptors();
				}
				return linePen;
			}
			if (id.equals(LINE_PEN_TOP)) {
				if (linePenTop == null) {
					linePenTop = new MLinePen(lineBox.getTopPen());
					setChildListener(linePenTop);
					linePenTop.getPropertyDescriptors();
				}
				return linePenTop;
			}
			if (id.equals(LINE_PEN_BOTTOM)) {
				if (linePenBottom == null) {
					linePenBottom = new MLinePen(lineBox.getBottomPen());
					setChildListener(linePenBottom);
					linePenBottom.getPropertyDescriptors();
				}
				return linePenBottom;
			}
			if (id.equals(LINE_PEN_LEFT)) {
				if (linePenLeft == null) {
					linePenLeft = new MLinePen(lineBox.getLeftPen());
					setChildListener(linePenLeft);
					linePenLeft.getPropertyDescriptors();
				}
				return linePenLeft;
			}
			if (id.equals(LINE_PEN_RIGHT)) {
				if (linePenRight == null) {
					linePenRight = new MLinePen(lineBox.getRightPen());
					setChildListener(linePenRight);
					linePenRight.getPropertyDescriptors();
				}
				return linePenRight;
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRLineBox lineBox = (JRLineBox) getValue();
		if (lineBox != null) {
			if (id.equals(JRBaseLineBox.PROPERTY_PADDING))
				lineBox.setPadding((Integer) value);
			else if (id.equals(JRBaseLineBox.PROPERTY_TOP_PADDING))
				lineBox.setTopPadding((Integer) value);
			else if (id.equals(JRBaseLineBox.PROPERTY_BOTTOM_PADDING))
				lineBox.setBottomPadding((Integer) value);
			else if (id.equals(JRBaseLineBox.PROPERTY_LEFT_PADDING))
				lineBox.setLeftPadding((Integer) value);
			else if (id.equals(JRBaseLineBox.PROPERTY_RIGHT_PADDING))
				lineBox.setRightPadding((Integer) value);
			// --------------------------------------------
		}
	}

	public String getDisplayText() {
		return null;
	}

	public ImageDescriptor getImagePath() {
		return null;
	}

}
