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
package com.jaspersoft.studio.components.table.model;

import java.beans.PropertyChangeEvent;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MCollection;

public abstract class AMCollection extends MCollection {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	
	public static final String REFRESH_COLUM_NAMES = "refreshColumnNamesRequest";

	public AMCollection(ANode parent, JRDesignComponentElement jrDataset,
			String property) {
		super(parent, jrDataset, property);
	}

	@Override
	public void setValue(Object value) {
		JRDesignComponentElement oldObject = (JRDesignComponentElement) getValue();
		JRDesignComponentElement newObject = (JRDesignComponentElement) value;

		if (oldObject != null) {
			StandardTable table = ((StandardTable) oldObject.getComponent());
			if (table != null)
				table.getEventSupport().removePropertyChangeListener(this);
		}
		if (newObject != null) {
			StandardTable table = ((StandardTable) newObject.getComponent());
			if (table != null)
				table.getEventSupport().addPropertyChangeListener(this);
		}
		super.setValue(value);
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(StandardTable.PROPERTY_COLUMNS)) {
			if (evt.getSource() instanceof StandardTable) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					int newIndex = -1;
					if (evt instanceof CollectionElementAddedEvent) {
						newIndex = ((CollectionElementAddedEvent) evt)
								.getAddedIndex();
					}
					StandardBaseColumn bc = (StandardBaseColumn) evt
							.getNewValue();

					createColumn(this, bc, 122, newIndex);

				} else if (evt.getOldValue() != null
						&& evt.getNewValue() == null) {
					// delete
					for (INode n : getChildren()) {
						if (n.getValue() == evt.getOldValue()) {
							removeChild((ANode) n);
							break;
						}
					}
				} else {
					// changed
					for (INode n : getChildren()) {
						if (n.getValue() == evt.getOldValue())
							n.setValue(evt.getNewValue());
					}
				}

				MTable mTable = (MTable) getParent();
				if (mTable == null) {
					((JRChangeEventsSupport)evt.getSource()).getEventSupport().removePropertyChangeListener(this);
				} else {
					mTable.getTableManager().refresh();
				}
			}
		}
		super.propertyChange(evt);
	}
	
	public abstract String getCellEvent();

	public abstract void createColumn(ANode mth, BaseColumn bc, int i, int index);

}
