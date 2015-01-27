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

import net.sf.jasperreports.engine.JRBand;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRSection;
import net.sf.jasperreports.engine.design.JRDesignBand;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.type.BandTypeEnum;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;

/*
 * The Class MBandGroupHeader.
 * 
 * @author Chicu Veaceslav
 */
public class MBandGroupHeader extends MBandGroup {
	
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;
	
	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	protected IIconDescriptor getLocalIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("groupheader"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m band group header.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrGroup
	 *          the jr group
	 * @param jrband
	 *          the jrband
	 * @param index
	 *          the index
	 */
	public MBandGroupHeader(ANode parent, JRDesignGroup jrGroup, JRBand jrband, int index) {
		super(parent, jrGroup, jrband, BandTypeEnum.GROUP_HEADER, index);
	}
	
	@Override
	public JRSection getSection(){
		return getJrGroup().getGroupHeaderSection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.band.MBand#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		if (getJrGroup() == null) return super.getDisplayText();
		JRDesignBand value = (JRDesignBand) getValue();
		String index = "";
		if (bandIndex != -1) index = " " + String.valueOf(bandIndex);
		if (value != null){
			return jrGroup.getName() + " " + Messages.MBandGroupHeader_group_header + index + " [" + value.getHeight() + "px] ";// + value.hashCode(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		}
		return jrGroup.getName() + " " + Messages.MBandGroupHeader_group_header + index;//$NON-NLS-1$
	}

	@Override
	public String getSimpleDisplayName() {
		if (getJrGroup() == null) return super.getSimpleDisplayName();
		String index = "";
		if (bandIndex != -1) index = " " + String.valueOf(bandIndex);
		return jrGroup.getName() + " " + Messages.MBandGroupHeader_group_header + index; //$NON-NLS-1$
	}
}
