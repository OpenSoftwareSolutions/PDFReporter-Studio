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
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRLineBox;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.base.JRBaseLineBox;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.design.JRDesignElement;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.box.BoxPropertyDescriptor;

/*
 * The Class MGeneric.
 */
public abstract class MGraphicElementLineBox extends MGraphicElement implements IGraphicElement, ILineBox {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String LINE_BOX = "LineBox"; //$NON-NLS-1$

	
	public MGraphicElementLineBox() {
		super();
	}

	public MGraphicElementLineBox(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	public MGraphicElementLineBox(ANode parent, JRDesignElement jrLine, int newIndex) {
		super(parent, jrLine, newIndex);
	}

	@Override
	public HashMap<String, Object> getStylesDescriptors() {
		HashMap<String, Object> result = super.getStylesDescriptors();
		if (getValue() == null)
			return result;
		MLineBox element = (MLineBox) getPropertyValue(LINE_BOX);
		// result.putAll(element.getStylesDescriptors());
		result.put(LINE_BOX, element);
		return result;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		BoxPropertyDescriptor lineBoxD = new BoxPropertyDescriptor(LINE_BOX, Messages.common_line_box);
		lineBoxD.setDescription(Messages.MGraphicElementLineBox_line_box_description);
		desc.add(lineBoxD);
		lineBoxD.setCategory(Messages.common_graphic);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#box");
	}

	private MLineBox lineBox;

	
	private MLineBox getLineBox(){
		JRBoxContainer jrGraphicElement = (JRBoxContainer) getValue();
		if (lineBox == null) {
			lineBox = new MLineBox(jrGraphicElement.getLineBox());
			setChildListener(lineBox);
		}
		return lineBox;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		// pen
		if (id.equals(LINE_BOX)) {
			return getLineBox();
		}
		return super.getPropertyValue(id);
	}

	public JRBoxContainer getBoxContainer() {
		return (JRBoxContainer) getValue();
	}
	
	/**
	 * Return the graphical properties for an MGraphicalElementLineBox
	 */
	public HashSet<String> generateGraphicalProperties(){
		HashSet<String> result = super.generateGraphicalProperties();
		result.add(JRBaseLineBox.PROPERTY_BOTTOM_PADDING);
		result.add(JRBaseLineBox.PROPERTY_LEFT_PADDING);
		result.add(JRBaseLineBox.PROPERTY_PADDING);
		result.add(JRBaseLineBox.PROPERTY_RIGHT_PADDING);
		result.add(JRBaseLineBox.PROPERTY_TOP_PADDING);
		result.add(JRBasePen.PROPERTY_LINE_COLOR);
		result.add(JRBasePen.PROPERTY_LINE_STYLE);
		result.add(JRBasePen.PROPERTY_LINE_WIDTH);
		return result;
	}
	
	private void transferLinePenProeprties(JRPen jrTarget, JRPen source){
		if (jrTarget != null && source != null){
			jrTarget.setLineColor(getColorClone(source.getOwnLineColor()));
			jrTarget.setLineStyle(source.getOwnLineStyleValue());
			Float lineWidth = source.getOwnLineWidth();
			jrTarget.setLineWidth(lineWidth != null ?  lineWidth.floatValue(): null);
		}
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRLineBox jrSourceBox = (JRLineBox) getBoxContainer().getLineBox();
		if (jrSourceBox != null && target instanceof JRBoxContainer){
			JRLineBox jrTargetBox = ((JRBoxContainer) target).getLineBox();
			
			jrTargetBox.setPadding(jrSourceBox.getOwnPadding());
			jrTargetBox.setTopPadding(jrSourceBox.getOwnTopPadding());
			jrTargetBox.setBottomPadding(jrSourceBox.getOwnBottomPadding());
			jrTargetBox.setLeftPadding(jrSourceBox.getOwnLeftPadding());
			jrTargetBox.setRightPadding(jrSourceBox.getOwnRightPadding());

			transferLinePenProeprties(jrTargetBox.getPen(), jrSourceBox.getPen());
			transferLinePenProeprties(jrTargetBox.getLeftPen(), jrSourceBox.getLeftPen());
			transferLinePenProeprties(jrTargetBox.getRightPen(), jrSourceBox.getRightPen());
			transferLinePenProeprties(jrTargetBox.getTopPen(), jrSourceBox.getTopPen());
			transferLinePenProeprties(jrTargetBox.getBottomPen(), jrSourceBox.getBottomPen());
		}
	}
	
	protected void applyDefaultValue(){
		if (DefaultManager.INSTANCE.hasDefault()){
			
		}
	}
}
