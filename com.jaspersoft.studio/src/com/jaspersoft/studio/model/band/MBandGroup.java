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
package com.jaspersoft.studio.model.band;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.type.BandTypeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.group.MGroup;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * Abstract class with the common methods for the MBandGroupFooter and MBandGroupHeader
 * 
 * @author Orlandin Marco
 *
 */
public abstract class MBandGroup extends MBand {

	private static final long serialVersionUID = 2542088264599647492L;

	/** 
	 * The jr group. 
	 */
	protected JRDesignGroup jrGroup;
	
	/**
	 * The model of the JRGroup
	 */
	protected MGroup mGroup;
	
	protected MGroupBand mGroupBand;

	/**
	 * Instantiates a new m band group header.
	 */
	public MBandGroup() {
		super();
	}


	public MBandGroup(ANode parent, JRDesignGroup jrGroup, JRBand jrband, BandTypeEnum bandtype, int newIndex){
		super(parent, jrband, bandtype, newIndex);
		this.jrGroup = jrGroup;
		mGroup = new MGroup(null, jrGroup, -1);
		setChildListener(mGroup);
		mGroupBand = new MGroupBand(jrGroup);
		// Fix missing jasper configuration
		if (parent != null) {
			JasperReportsConfiguration jconfig = parent.getJasperConfiguration();
			if (jconfig != null) {
				mGroup.setJasperConfiguration(jconfig);
				mGroupBand.setJasperConfiguration(jconfig);
			}
		}
		bandIndex = -1;
		refreshIndex();
	}

	/**
	 * Return the icon descriptor for the element 
	 * 
	 * @return a not null icon descriptor
	 */
	protected abstract IIconDescriptor getLocalIconDescriptor();
	
	/**
	 * Gets the jr group.
	 * 
	 * @return the jr group
	 */
	public JRDesignGroup getJrGroup() {
		return jrGroup;
	}

	/**
	 * Refresh the index of the band with the current number returned by getDesignIndex
	 */
	public void refreshIndex() {
		if (jrGroup == null) return;
		INode n = getRoot();
		if (n instanceof MReport) {
			MReport mrep = (MReport) n;
			Integer index = mrep.getBandIndex(getValue());
			if (index != null) {
				bandIndex = index;
				return;
			}
		}
		setDetailIndex(getFreeIndex());
	}
	
	/**
	 * Return the JRSection of the actual band
	 * 
	 * @return a JRSection
	 */
	public abstract JRSection getSection();

	public MGroup getMGroup() {
		return mGroup;
	}
	
	/**
	 * Update the name validator for the group when a new band connected
	 * to the group is selected
	 */
	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		if (mGroup != null){
			mGroup.updateValidator();
		}
		super.postDescriptors(descriptors);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.band.MBand#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getLocalIconDescriptor().getIcon16();
	}


	@Override
	public Object getPropertyValue(Object id) {
		Object obj = mGroupBand.getPropertyValue(id);
		if (obj != null)
			return obj;
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		mGroupBand.setPropertyValue(id, value);
		super.setPropertyValue(id, value);
	}

	@Override
	public Object getPropertyDefaultValue(String id) throws Exception {
		Object obj = mGroupBand.getPropertyDefaultValue(id);
		if (obj != null)
			return obj;
		return super.getPropertyDefaultValue(id);
	}

	@Override
	public boolean isSameBandType(MBand band) {
		return super.isSameBandType(band) && jrGroup != null && jrGroup == ((MBandGroup) band).getJrGroup();
	}
	
	//Descriptor section
	

	/**
	 * Creates the property descriptors, they are identical for group header and footer
	 * 
	 * @param desc the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		new MGroupBand(getJrGroup()).createPropertyDescriptors(desc, defaultsMap);
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

}
