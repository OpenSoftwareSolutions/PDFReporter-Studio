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

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.JRPen;
import net.sf.jasperreports.engine.base.JRBasePen;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGraphicElement;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.property.descriptor.pen.PenPropertyDescriptor;

/*
 * The Class MGeneric.
 */
public abstract class MGraphicElementLinePen extends MGraphicElement implements IGraphicElement {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String LINE_PEN = "LinePen"; //$NON-NLS-1$

	public MGraphicElementLinePen() {
		super();
	}

	public MGraphicElementLinePen(ANode parent, int newIndex) {
		super(parent, newIndex);
	}

	public MGraphicElementLinePen(ANode parent, JRDesignElement jrLine, int newIndex) {
		super(parent, jrLine, newIndex);
	}

	@Override
	public HashMap<String, Object> getStylesDescriptors() {
		HashMap<String, Object> result = super.getStylesDescriptors();
		if (getValue() == null)
			return result;
		MLinePen linepen = (MLinePen) getPropertyValue(LINE_PEN);
		// result.putAll(linepen.getStylesDescriptors());
		result.put(LINE_PEN, linepen);
		return result;
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		PenPropertyDescriptor linePenD = new PenPropertyDescriptor(LINE_PEN, Messages.common_line_pen);
		linePenD.setDescription(Messages.MGraphicElementLinePen_line_pen_description);
		desc.add(linePenD);
		linePenD.setCategory(Messages.common_graphic);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#graphicElement");
	}

	private MLinePen linePen;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		// pen
		if (id.equals(LINE_PEN)) {
			JRDesignGraphicElement jrGraphicElement = (JRDesignGraphicElement) getValue();
			if (linePen == null) {
				linePen = new MLinePen(jrGraphicElement.getLinePen());
				linePen.getPropertyChangeSupport().addPropertyChangeListener(this);
			}
			return linePen;
		}
		return super.getPropertyValue(id);
	}
	
	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		JRDesignGraphicElement jrSourceElement = (JRDesignGraphicElement) getValue();
		JRPen jrSourceBox = (JRPen)jrSourceElement.getLinePen();
		
		JRDesignGraphicElement jrTargetElement = (JRDesignGraphicElement) target;
		JRPen jrTargetBox = (JRPen)jrTargetElement.getLinePen();
		if (jrSourceBox != null && jrTargetBox != null){
			jrTargetBox.setLineColor(getColorClone(jrSourceBox.getOwnLineColor()));
			jrTargetBox.setLineStyle(jrSourceBox.getOwnLineStyleValue());
			jrTargetBox.setLineWidth(jrSourceBox.getOwnLineWidth());
		}
	}

	/**
	 * Return the graphical properties for an MGraphicalElementLinePen 
	 */
	public HashSet<String> generateGraphicalProperties(){
		HashSet<String> result = super.generateGraphicalProperties();
		result.add(JRBasePen.PROPERTY_LINE_COLOR);
		result.add(JRBasePen.PROPERTY_LINE_STYLE);
		result.add(JRBasePen.PROPERTY_LINE_WIDTH);
		return result;
	}
}
