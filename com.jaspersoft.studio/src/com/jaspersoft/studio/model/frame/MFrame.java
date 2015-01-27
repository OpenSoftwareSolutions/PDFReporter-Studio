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
package com.jaspersoft.studio.model.frame;

import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignFrame;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ImageDescriptor;

import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.IGraphicElementContainer;
import com.jaspersoft.studio.model.IGraphicalPropertiesHandler;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.IPastable;
import com.jaspersoft.studio.model.IPastableGraphic;
import com.jaspersoft.studio.model.MGraphicElementLineBox;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.model.util.ReportFactory;

/*
 * The Class MFrame.
 */
public class MFrame extends MGraphicElementLineBox implements IPastable, IPastableGraphic, IContainer,
		IContainerLayout, IContainerEditPart, IGraphicElementContainer {
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
			iconDescriptor = new NodeIconDescriptor("frame"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m frame.
	 */
	public MFrame() {
		super();
	}

	/**
	 * Instantiates a new m frame.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrFrame
	 *          the jr frame
	 * @param newIndex
	 *          the new index
	 */
	public MFrame(ANode parent, JRDesignFrame jrFrame, int newIndex) {
		super(parent, newIndex);
		setValue(jrFrame);
	}

	@Override
	public int getDefaultHeight() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_HEIGHT);
		return defaultValue != null ? (Integer)defaultValue : 200;
	}

	@Override
	public int getDefaultWidth() {
		Object defaultValue = DefaultManager.INSTANCE.getDefaultPropertiesValue(this.getClass(), JRDesignElement.PROPERTY_WIDTH);
		return defaultValue != null ? (Integer)defaultValue : 200;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#createJRElement(net.sf.jasperreports.engine.design.JasperDesign)
	 */
	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		JRDesignElement jrDesignElement = new JRDesignFrame();

		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrDesignElement);

		jrDesignElement.setWidth(getDefaultWidth());
		jrDesignElement.setHeight(getDefaultHeight());
		return jrDesignElement;
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

	public int getTopPadding() {
		JRDesignFrame frame = (JRDesignFrame) getValue();
		if (frame != null)
			return frame.getLineBox().getTopPadding();
		return 0;
	}

	public int getLeftPadding() {
		JRDesignFrame frame = (JRDesignFrame) getValue();
		if (frame != null)
			return frame.getLineBox().getLeftPadding();
		return 0;
	}

	@Override
	public JRDesignFrame getValue() {
		return (JRDesignFrame) super.getValue();
	}

	@Override
	public Dimension getSize() {
		JRDesignFrame jrDesignFrame = getValue();
		int h = jrDesignFrame.getHeight();
		int w = jrDesignFrame.getWidth();
		return new Dimension(w, h);
	}

	@Override
	public JRPropertiesHolder[] getPropertyHolder() {
		return new JRPropertiesHolder[] { getValue() };
	}
	
	/**
	 * If the model has the children not in sync with the JRElement the build
	 * the correct list
	 */
	@Override
	public List<INode> initModel() {
		if (getValue().getChildren().size()>0 && (getChildren() == null || getChildren().size() == 0)){
			MFrame copy = new MFrame();
			copy.setValue(getValue());
			ReportFactory.createElementsForBand(copy,getValue().getChildren() );
			return copy.getChildren();
		} else return getChildren();
	}

	
	@Override
	public HashSet<String> getUsedStyles() {
		HashSet<String> usedStyles = super.getUsedStyles();
		for(INode node : getChildren()){
			if (node instanceof IGraphicalPropertiesHandler){
				HashSet<String> childStyles = ((IGraphicalPropertiesHandler)node).getUsedStyles();
				usedStyles.addAll(childStyles);
			}
		}
		return usedStyles;
	}
	
	public HashSet<String> generateGraphicalProperties(){
		HashSet<String> result = super.generateGraphicalProperties();
		result.add(JRDesignFrame.PROPERTY_CHILDREN);
		result.add(JRDesignElement.PROPERTY_ELEMENT_GROUP);
		return result;
	}
}
