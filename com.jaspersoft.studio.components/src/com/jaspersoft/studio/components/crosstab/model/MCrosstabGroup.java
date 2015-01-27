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
package com.jaspersoft.studio.components.crosstab.model;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstabGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabTotalPositionEnum;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.property.descriptor.JRPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public abstract class MCrosstabGroup extends APropertyNode implements
		IPropertySource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Instantiates a new m field.
	 */
	public MCrosstabGroup() {
		super();
	}

	/**
	 * Instantiates a new m field.
	 * 
	 * @param parent
	 *            the parent
	 * @param jfRield
	 *            the jf rield
	 * @param newIndex
	 *            the new index
	 */
	public MCrosstabGroup(ANode parent, JRCrosstabGroup jfRield, int newIndex) {
		super(parent, newIndex);
		setValue(jfRield);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return ((JRCrosstabGroup) getValue()).getName();
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1,
			Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *            the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc,
			Map<String, Object> defaultsMap) {
		totalPositionD = new JSSEnumPropertyDescriptor(
				JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION,
				Messages.common_total_position,
				CrosstabTotalPositionEnum.class, NullEnum.NOTNULL);
		totalPositionD
				.setDescription(Messages.MCrosstabGroup_total_position_description);
		desc.add(totalPositionD);

		NTextPropertyDescriptor nameD = new NTextPropertyDescriptor(
				JRDesignCrosstabGroup.PROPERTY_NAME, Messages.common_name);
		nameD.setDescription(Messages.MCrosstabGroup_name_description);
		desc.add(nameD);

		JRPropertyDescriptor bucketD = new JRPropertyDescriptor(
				JRDesignCrosstabGroup.PROPERTY_BUCKET, Messages.common_bucket);
		bucketD.setDescription(Messages.MCrosstabGroup_bucket_description);
		desc.add(bucketD);

	}

	private MBucket mBucket;
	private static JSSEnumPropertyDescriptor totalPositionD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignCrosstabGroup jrField = (JRDesignCrosstabGroup) getValue();
		if (id.equals(JRDesignCrosstabGroup.PROPERTY_NAME))
			return jrField.getName();
		if (id.equals(JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION))
			return totalPositionD.getEnumValue(jrField.getTotalPositionValue());
		if (id.equals(JRDesignCrosstabGroup.PROPERTY_BUCKET)) {
			if (mBucket == null) {
				mBucket = new MBucket(jrField.getBucket());
				setChildListener(mBucket);
			}
			mBucket.setValue(jrField.getBucket());
			return mBucket;
		}
		return null;
	}
	
	/**
	 * Called when the group name changes, it must search the cells
	 * using that group and update their reference as well
	 * 
	 * @param oldName the old name of the group
	 * @param newName the new name of the group
	 */
	protected abstract void updateGroups(String oldName, String newName);
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignCrosstabGroup jrField = (JRDesignCrosstabGroup) getValue();
		if (id.equals(JRDesignCrosstabGroup.PROPERTY_NAME)){
			String oldName = jrField.getName();
			jrField.setName((String) value);
			//Request the update of the name of the cells associated with this group
			updateGroups(oldName, jrField.getName());
		} else if (id.equals(JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION)) {
			jrField.setTotalPosition((CrosstabTotalPositionEnum) totalPositionD
					.getEnumValue(value));
			MCrosstab cross = getMCrosstab();
			cross.getCrosstabManager().refresh();
			getPropertyChangeSupport().firePropertyChange(
					new PropertyChangeEvent(this,
							JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION,
							null, value));
		}
	}

	public MCrosstab getMCrosstab() {
		INode node = getParent();
		while (node != null) {
			if (node instanceof MCrosstab) {
				return (MCrosstab) node;
			}
			node = node.getParent();
		}
		return null;
	}

}
