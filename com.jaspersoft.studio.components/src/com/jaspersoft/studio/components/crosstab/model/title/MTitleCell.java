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
package com.jaspersoft.studio.components.crosstab.model.title;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.crosstabs.CrosstabColumnCell;
import net.sf.jasperreports.crosstabs.design.DesignCrosstabColumnCell;
import net.sf.jasperreports.crosstabs.design.JRDesignCrosstabCell;
import net.sf.jasperreports.crosstabs.type.CrosstabColumnPositionEnum;
import net.sf.jasperreports.engine.JRConstants;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.crosstab.messages.Messages;
import com.jaspersoft.studio.components.crosstab.model.cell.MCell;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

public class MTitleCell extends MCell {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MTitleCell() {
		super();
	}

	private DesignCrosstabColumnCell colCell;

	public MTitleCell(ANode parent, CrosstabColumnCell jfRield, int index) {
		super(parent, jfRield.getCellContents(), Messages.MTitleCell_titlecell, index);
		this.colCell = (DesignCrosstabColumnCell) jfRield;
	}

	public DesignCrosstabColumnCell getCrosstabColumnCell() {
		return colCell;
	}

	@Override
	public Color getForeground() {
		if (getValue() == null)
			return ColorConstants.lightGray;
		return ColorConstants.black;
	}

	@Override
	public Object getPropertyValue(Object id) {
		if (id.equals(DesignCrosstabColumnCell.PROPERTY_CONTENTS_POSITION))
			return positionD.getEnumValue(colCell.getContentsPosition());
		if (id.equals(DesignCrosstabColumnCell.PROPERTY_HEIGHT))
			return colCell.getHeight();
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(DesignCrosstabColumnCell.PROPERTY_CONTENTS_POSITION))
			colCell.setContentsPosition((CrosstabColumnPositionEnum) positionD.getEnumValue(value));
		if (id.equals(DesignCrosstabColumnCell.PROPERTY_HEIGHT)) {
			colCell.setHeight((Integer) value);

			getCrosstab().getCrosstabManager().refresh();
			getPropertyChangeSupport().firePropertyChange(new PropertyChangeEvent(this, JRDesignCrosstabCell.PROPERTY_HEIGHT, null, value));
		}
		super.setPropertyValue(id, value);
	}

	@Override
	public int getDefaultHeight() {
		return 30;
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static JSSEnumPropertyDescriptor positionD;

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

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		positionD = new JSSEnumPropertyDescriptor(DesignCrosstabColumnCell.PROPERTY_CONTENTS_POSITION, Messages.MTitleCell_contentPosition, CrosstabColumnPositionEnum.class, NullEnum.NOTNULL);
		positionD.setDescription(Messages.MTitleCell_contentPosition);
		desc.add(positionD);

		super.createPropertyDescriptors(desc, defaultsMap);

		for (IPropertyDescriptor pd : desc) {
			if (pd.getId().equals(JRDesignCrosstabCell.PROPERTY_WIDTH))
				((PixelPropertyDescriptor) pd).setReadOnly(true);
		}
	}

}
