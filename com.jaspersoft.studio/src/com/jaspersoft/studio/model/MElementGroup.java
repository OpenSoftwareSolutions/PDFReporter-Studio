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

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JRDesignField;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.band.MBand;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;

/*
 * The Class MElementGroup.
 * 
 * @author Chicu Veaceslav
 */
public class MElementGroup extends ANode implements IContainerEditPart, IContainer, IGraphicElementContainer {
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
			iconDescriptor = new NodeIconDescriptor("elementGroup"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/** The descriptors. */
	protected static IPropertyDescriptor[] descriptors;

	/**
	 * Instantiates a new m element group.
	 * 
	 * @param parent
	 *          the parent
	 * @param jfRield
	 *          the jf rield
	 * @param newIndex
	 *          the new index
	 */
	public MElementGroup(ANode parent, JRElementGroup jfRield, int newIndex) {
		super(parent, newIndex);
		setValue(jfRield);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/**
	 * Creates the jr field.
	 * 
	 * @return the jR design field
	 */
	public JRDesignField createJRField() {
		return new JRDesignField();
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

	/**
	 * Creates the jr element group.
	 * 
	 * @return the jR design element group
	 */
	public JRDesignElementGroup createJRElementGroup() {
		return new JRDesignElementGroup();
	}

	@Override
	public int getTopPadding() {
		ANode parent = getParent();
		if (parent instanceof IGraphicElementContainer) {
			return ((IGraphicElementContainer) parent).getTopPadding();
		}
		return 0;
	}

	@Override
	public int getLeftPadding() {
		ANode parent = getParent();
		if (parent instanceof IGraphicElementContainer) {
			return ((IGraphicElementContainer) parent).getLeftPadding();
		}
		return 0;
	}

	@Override
	public Dimension getSize() {
		ANode parent = getParent();
		if (parent instanceof MBand) {
			// height of band, width of Report - margins
			int h = ((JRDesignBand) ((MBand) parent).getValue()).getHeight();
			JasperDesign jasperDesign = getJasperDesign();
			int w = jasperDesign.getPageWidth() - jasperDesign.getLeftMargin() - jasperDesign.getRightMargin();
			return new Dimension(w, h);
		} else if (parent instanceof IGraphicElementContainer) {
			return ((IGraphicElementContainer) parent).getSize();
		}
		return new Dimension(0,0);
	}
	
	
}
