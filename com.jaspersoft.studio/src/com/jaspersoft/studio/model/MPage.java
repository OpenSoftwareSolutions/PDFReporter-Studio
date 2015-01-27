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
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.gef.figures.APageFigure;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;

/*
 * The Class MReport.
 * 
 * @author Chicu Veaceslav
 */
public class MPage extends MLockableRefresh implements IGraphicElement, IContainerEditPart {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	private Map<Object, ANode> obj2Node = new HashMap<Object, ANode>();
	private ANode realParent;
	private MDataset getDataset(JasperDesign jrDesign) {
		MDataset mDataset = new MDataset(null, (JRDesignDataset) jrDesign.getMainDataset());
		mDataset.setJasperConfiguration(getJasperConfiguration());
		return mDataset;
	}
	
	public void register(ANode n) {
		if (n.getValue() != null)
			obj2Node.put(n.getValue(), n);
	}

	public void unregister(ANode n) {
		if (n.getValue() != null)
			obj2Node.remove(n.getValue());
	}

	public ANode getNode(Object obj) {
		return obj2Node.get(obj);
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("report"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m report.
	 * 
	 * @param parent
	 *          the parent
	 * @param jd
	 *          the jd
	 */
	public MPage(ANode parent, JasperDesign jd) {
		super(parent, -1);
		setValue(jd);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getEditableValue()
	 */
	@Override
	public Object getEditableValue() {
		return this;
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java .lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JasperDesign jrDesign = (JasperDesign) getValue();
		if (id.equals(MGraphicElement.PROPERTY_MAP)) {
			// to avoid duplication I remove it first
			return (JRPropertiesMap) jrDesign.getPropertiesMap().cloneProperties();
		} else if (id.equals(JasperDesign.PROPERTY_MAIN_DATASET)){
			return getDataset(jrDesign);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JasperDesign jrDesign = (JasperDesign) getValue();
		if (id.equals(MGraphicElement.PROPERTY_MAP)) {
			JRPropertiesMap v = (JRPropertiesMap) value;
			String[] names = jrDesign.getPropertiesMap().getPropertyNames();
			for (int i = 0; i < names.length; i++)
				jrDesign.getPropertiesMap().removeProperty(names[i]);
			names = v.getPropertyNames();

			for (String str : v.getPropertyNames())
				jrDesign.setProperty(str, v.getProperty(str));
			this.getPropertyChangeSupport().firePropertyChange(MGraphicElement.PROPERTY_MAP, false, true);
		}
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
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return ((JasperDesign) getValue()).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getDefaultHeight()
	 */
	public int getDefaultHeight() {
		return 800;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getDefaultWidth()
	 */
	public int getDefaultWidth() {
		return 800;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#createJRElement(net.sf.jasperreports.engine.design.JasperDesign)
	 */
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.IGraphicElement#getBounds()
	 */
	public Rectangle getBounds() {
		return new Rectangle(APageFigure.PAGE_BORDER.left, APageFigure.PAGE_BORDER.top, 300, 400);
	}

	/**
	 * Set the real parent of the child of the page, this is used when the page is created to 
	 * open a subeditor. In that case the classic chain of parents is not valid anymore
	 * since a separate editor has a new root separated from the rest of the report. 
	 * So this field is provided to keep a reference to the real parent of the 
	 * element opened in the editor
	 * 
	 * @param realParent real parent of the element opened in the editor
	 */
	public void setRealParent(ANode realParent){
		this.realParent = realParent;
	}
	
	/**
	 * Get the real parent of the child of the page, this is used when the page is created to 
	 * open a subeditor. In that case the classic chain of parents is not valid anymore
	 * since a separate editor has a new root separated from the rest of the report. 
	 * So this field is provided to keep a reference to the real parent of the 
	 * element opened in the editor
	 * 
	 * @param realParent real parent of the element opened in the editor
	 */
	public ANode getRealParent(){
		return realParent;
	}
}
