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
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.components.table.BaseColumn;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumnGroup;
import net.sf.jasperreports.components.table.StandardTable;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.table.TableComponentFactory;
import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.TableNodeIconDescriptor;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.IIconDescriptor;

public class MTableHeader extends AMCollection {
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
			iconDescriptor = new TableNodeIconDescriptor("tableheader"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/** The descriptors. */
	protected static IPropertyDescriptor[] descriptors;

	public MTableHeader(ANode parent, JRDesignComponentElement jrDataset,
			String property) {
		super(parent, jrDataset, property);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	@Override
	public String getCellEvent() {
		return StandardBaseColumn.PROPERTY_TABLE_HEADER;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		// because MTableDetail do not contains ColumnGroups, TableHeader will
		// listen for events and manage nodes
		if (evt.getPropertyName().equals(StandardTable.PROPERTY_COLUMNS)) {
			if (evt.getSource() instanceof StandardColumnGroup) {
				MTable mTable = (MTable) getParent();
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					int newIndex = -1;
					if (evt instanceof CollectionElementAddedEvent) {
						newIndex = ((CollectionElementAddedEvent) evt)
								.getAddedIndex();
					}
					StandardBaseColumn bc = (StandardBaseColumn) evt
							.getNewValue();
					for (INode n : getParent().getChildren()) {
						if (n instanceof MTableDetail) {
							boolean columnexists = false;
							for (INode cn : n.getChildren()) {
								if (cn.getValue() == bc) {
									columnexists = true;
									break;
								}
							}
							if (!columnexists) {
								newIndex = TableManager.getAllColumns(mTable)
										.indexOf(bc);
								newIndex = TableComponentFactory
										.createCellDetail((ANode) n, bc, 11,
												newIndex);
							}
							break;
						}
					}
				} else if (evt.getOldValue() != null
						&& evt.getNewValue() == null) {
					for (INode n : getParent().getChildren()) {
						if (n instanceof MTableDetail) {
							List<INode> delnodes = new ArrayList<INode>();
							List<BaseColumn> columns = TableManager
									.getAllColumns(mTable);
							for (INode node : n.getChildren())
								if (!columns.contains((node.getValue())))
									delnodes.add(node);
							// ((MTableDetail) n).removeChildren(delnodes);
							for (INode node : delnodes) {
								int index = ((ANode) n).getChildren().indexOf(
										node);
								((ANode) n).removeChild((ANode) node);
								n.getPropertyChangeSupport()
										.fireIndexedPropertyChange(
												JRDesignElementGroup.PROPERTY_CHILDREN,
												index, node, null);
							}
							break;
						}
					}
				} else {
					for (INode n : getParent().getChildren()) {
						if (n instanceof MTableDetail) {
							for (INode node : n.getChildren())
								if (node.getValue() == evt.getOldValue()) {
									((ANode) n).setValue(evt.getNewValue());
									break;
								}
							break;
						}
					}
				}

				mTable.getTableManager().refresh();
				//TableColumnNumerator.renumerateColumnNames(mTable);
			}
		}
		super.propertyChange(evt);
	}

	@Override
	public void createColumn(ANode mth, BaseColumn bc, int i, int index) {
		TableComponentFactory.createCellTableHeader(mth, bc, i, index);
	}

}
