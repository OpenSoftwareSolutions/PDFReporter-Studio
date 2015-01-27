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
package com.jaspersoft.studio.components.crosstab.model.rowgroup;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.JRCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstab;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabGroup;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabRowGroup;
import net.sf.jasperreports.crosstabs.type.CrosstabRowPositionEnum;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.JSSCompoundCommand;
import com.jaspersoft.studio.components.crosstab.CrosstabComponentFactory;
import com.jaspersoft.studio.components.crosstab.CrosstabNodeIconDescriptor;
import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.MCrosstab;
import com.jaspersoft.studio.components.crosstab.model.MCrosstabGroup;
import com.jaspersoft.studio.components.crosstab.model.cell.MGroupCell;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

public class MRowGroup extends MCrosstabGroup implements ICopyable {
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
			iconDescriptor = new CrosstabNodeIconDescriptor("rowgroup"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m field.
	 */
	public MRowGroup() {
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
	public MRowGroup(ANode parent, JRCrosstabRowGroup jfRield, int newIndex) {
		super(parent, jfRield, newIndex);
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
	private static JSSEnumPropertyDescriptor columnPositionD;

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
		super.createPropertyDescriptors(desc, defaultsMap);

		columnPositionD = new JSSEnumPropertyDescriptor(
				JRDesignCrosstabRowGroup.PROPERTY_POSITION,
				Messages.MRowGroup_row_position, CrosstabRowPositionEnum.class,
				NullEnum.NOTNULL);
		columnPositionD
				.setDescription(Messages.MRowGroup_row_position_description);
		desc.add(columnPositionD);

		PixelPropertyDescriptor widthD = new PixelPropertyDescriptor(
				JRDesignCrosstabRowGroup.PROPERTY_WIDTH, Messages.common_width);
		widthD.setDescription(Messages.MRowGroup_width_description);
		desc.add(widthD);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		JRDesignCrosstabRowGroup jrField = (JRDesignCrosstabRowGroup) getValue();
		if (id.equals(JRDesignCrosstabRowGroup.PROPERTY_POSITION))
			return columnPositionD.getEnumValue(jrField.getPositionValue());
		if (id.equals(JRDesignCrosstabRowGroup.PROPERTY_WIDTH))
			return jrField.getWidth();
		return super.getPropertyValue(id);
	}

	/**
	 * Called when the name of a group change
	 * Search in the crosstab the group cells that are using a reference
	 * to the group and update also their references.
	 * It update the group map in the JRCrosstabElement to keep it 
	 * in sync with the current group name.
	 * Finally since the cell uses the group name for the model display
	 * name it run a fake event to update the cells in the editor
	 */
	@Override
	protected void updateGroups(String oldName, String newName){
		ANode crosstab = getParent().getParent();
		List<MGroupCell> cellsToRefresh = new ArrayList<MGroupCell>();
		for(INode child : crosstab.getChildren()){
			if (child instanceof MGroupCell){
				MGroupCell cell = (MGroupCell)child;
				String rowGroup = cell.getCell().getRowTotalGroup();
				if (rowGroup != null && rowGroup.equals(oldName)){
					cell.getCell().setRowTotalGroup(newName);
					cellsToRefresh.add(cell);
				}
			}
		}
		JRDesignCrosstab jrCrosstab = (JRDesignCrosstab)crosstab.getValue();
		Map<String, Integer> groupMap = jrCrosstab.getRowGroupIndicesMap();
		if (groupMap.containsKey(oldName)){
			Integer value = groupMap.remove(oldName);
			groupMap.put(newName, value);
		}
		//The refresh must be done after the update of the map
		for(MGroupCell cell : cellsToRefresh){
			JSSCompoundCommand.forceRefreshVisuals(cell);
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignCrosstabRowGroup jrField = (JRDesignCrosstabRowGroup) getValue();
		if (id.equals(JRDesignCrosstabRowGroup.PROPERTY_POSITION))
			jrField.setPosition((CrosstabRowPositionEnum) columnPositionD
					.getEnumValue(value));
		else if (id.equals(JRDesignCrosstabRowGroup.PROPERTY_WIDTH)) {
			jrField.setWidth((Integer) value);
			MCrosstab cross = getMCrosstab();
			cross.getCrosstabManager().refresh();
			getPropertyChangeSupport().firePropertyChange(
					new PropertyChangeEvent(this,
							JRDesignCrosstabRowGroup.PROPERTY_WIDTH, null,
							value));
		} else
			super.setPropertyValue(id, value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(
				JRDesignCrosstabGroup.PROPERTY_TOTAL_POSITION)) {
			this.removeChildren();
			CrosstabComponentFactory.createRowGroupCells(this,
					(JRCrosstabRowGroup) getValue());
			MCrosstab mCrosstab = getMCrosstab();
			CrosstabComponentFactory.deleteCellNodes(mCrosstab);
			CrosstabComponentFactory.createCellNodes(
					(JRDesignCrosstab) mCrosstab.getValue(), mCrosstab);
		}
		super.propertyChange(evt);
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MRowGroups)
			return true;
		return false;
	}

}
