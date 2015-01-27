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
package com.jaspersoft.studio.components.table.model.column;

import java.beans.PropertyChangeEvent;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.Cell;
import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.StandardColumn;
import net.sf.jasperreports.engine.JRBoxContainer;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElementGroup;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRStyle;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignElementGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.CollectionElementAddedEvent;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.MTableGroupFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupHeader;
import com.jaspersoft.studio.components.table.util.TableColumnSize;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.IGraphicElementContainer;
import com.jaspersoft.studio.model.IGraphicalPropertiesHandler;
import com.jaspersoft.studio.model.IGroupElement;
import com.jaspersoft.studio.model.ILineBox;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.IPastable;
import com.jaspersoft.studio.model.IPastableGraphic;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.MLineBox;
import com.jaspersoft.studio.model.MRoot;
import com.jaspersoft.studio.model.util.ReportFactory;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.box.BoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.IntegerPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;

public class MCell extends MColumn implements IGraphicElement,
		IPastableGraphic, ILineBox, IGraphicElementContainer, IPastable,
		IGroupElement, IGraphicalPropertiesHandler {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Instantiates a new m field.
	 */
	public MCell() {
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
	public MCell(ANode parent, StandardBaseColumn column, Cell cell,
			String name, int index) {
		super(parent, column, name, index);
		this.cell = (DesignCell) cell;
		this.cell.getEventSupport().addPropertyChangeListener(this);
	}

	private DesignCell cell;

	public DesignCell getCell() {
		return cell;
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

	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		JasperDesign jasperDesign = getJasperDesign();
		if (jasperDesign != null) {
			if (styleD != null && cell != null) {
				JRStyle[] styles = jasperDesign.getStyles();
				String[] items = new String[styles.length + 1];
				items[0] = cell.getStyleNameReference() != null ? cell
						.getStyleNameReference() : ""; //$NON-NLS-1$
				for (int j = 0; j < styles.length; j++) {
					items[j + 1] = styles[j].getName();
				}
				styleD.setItems(items);
			}
		}
	}

	private RWComboBoxPropertyDescriptor styleD;

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

		styleD = new RWComboBoxPropertyDescriptor(DesignCell.PROPERTY_STYLE,
				Messages.MCell_parent_style, new String[] { "" }, //$NON-NLS-1$
				NullEnum.NULL);
		styleD.setDescription(Messages.MCell_parent_style_description);
		desc.add(styleD);

		PixelPropertyDescriptor hD = new PixelPropertyDescriptor(
				DesignCell.PROPERTY_HEIGHT, Messages.MCell_height);
		desc.add(hD);

		IntegerPropertyDescriptor hrowspan = new IntegerPropertyDescriptor(
				DesignCell.PROPERTY_ROW_SPAN, Messages.MCell_rowspan);
		desc.add(hrowspan);

		BoxPropertyDescriptor lineBoxD = new BoxPropertyDescriptor(LINE_BOX,
				Messages.MCell_line_box);
		lineBoxD.setDescription(Messages.MCell_line_box_description);
		desc.add(lineBoxD);

		styleD.setCategory(Messages.MCell_cell_properties_category);
		hD.setCategory(Messages.MCell_cell_properties_category);
		lineBoxD.setCategory(Messages.MCell_cell_properties_category);

		defaultsMap.put(DesignCell.PROPERTY_STYLE, null);
	}

	public static final String LINE_BOX = "LineBox"; //$NON-NLS-1$
	private MLineBox lineBox;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		if (cell != null) {
			if (id.equals(DesignCell.PROPERTY_STYLE)) {
				if (cell.getStyleNameReference() != null)
					return cell.getStyleNameReference();
				if (cell.getStyle() != null)
					return cell.getStyle().getName();
				return ""; //$NON-NLS-1$
			}

			if (id.equals(DesignCell.PROPERTY_HEIGHT))
				return cell.getHeight();
			if (id.equals(DesignCell.PROPERTY_ROW_SPAN))
				return cell.getRowSpan();
			if (id.equals(LINE_BOX)) {
				JRBoxContainer jrGraphicElement = (JRBoxContainer) cell;
				if (lineBox == null) {
					lineBox = new MLineBox(jrGraphicElement.getLineBox());
					setChildListener(lineBox);
				}
				return lineBox;
			}
			if (id.equals(MGraphicElement.PROPERTY_MAP)) {
				// to avoid duplication I remove it first
				JRPropertiesMap pmap = cell.getPropertiesMap();
				return pmap;
			}
		}
		return super.getPropertyValue(id);
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
		if (cell != null) {
			if (id.equals(DesignCell.PROPERTY_STYLE)) {
				if (!value.equals("") && value != null) { //$NON-NLS-1$
					JRStyle style = (JRStyle) getJasperDesign().getStylesMap()
							.get(value);
					if (style != null) {
						cell.setStyle(style);
						cell.setStyleNameReference(null);
					} else {
						cell.setStyleNameReference((String) value);
						cell.setStyle(null);
					}
				} else {
					cell.setStyle(null);
					cell.setStyleNameReference(null);
				}
			} else if (id.equals(DesignCell.PROPERTY_ROW_SPAN)) {
				cell.setRowSpan((Integer) value);
			} else if (id.equals(DesignCell.PROPERTY_HEIGHT)) {
				MTable mtable = getMTable();
				Integer height = (Integer) value;
				AMCollection section = getSection();
				if (height != null && section != null && height.intValue() >= 0) {

					@SuppressWarnings("unchecked")
					Class<AMCollection> classType = (Class<AMCollection>) section
							.getClass();
					String grName = null;
					if (section instanceof MTableGroupHeader)
						grName = ((MTableGroupHeader) section)
								.getJrDesignGroup().getName();
					if (section instanceof MTableGroupFooter)
						grName = ((MTableGroupFooter) section)
								.getJrDesignGroup().getName();

					mtable.getTableManager().setHeight(cell, height,
							(StandardBaseColumn) getValue(),
							TableColumnSize.getType(classType), grName);

					// cell.setHeight(height);
					mtable.getTableManager().update();

					getPropertyChangeSupport().firePropertyChange(
							new PropertyChangeEvent(this,
									DesignCell.PROPERTY_HEIGHT, null, value));
				}
				return;
			} else if (id.equals(MGraphicElement.PROPERTY_MAP)) {
				JRPropertiesMap v = (JRPropertiesMap) value;
				String[] names = cell.getPropertiesMap().getPropertyNames();
				for (int i = 0; i < names.length; i++) {
					cell.getPropertiesMap().removeProperty(names[i]);
				}
				names = v.getPropertyNames();
				for (int i = 0; i < names.length; i++)
					cell.getPropertiesMap().setProperty(names[i],
							v.getProperty(names[i]));
				this.getPropertyChangeSupport().firePropertyChange(
						MGraphicElement.PROPERTY_MAP, false, true);
				return; // Attention! MColumn has his own property map, here we work with cell
			}
		}
		super.setPropertyValue(id, value);
	}

	public int getDefaultWidth() {
		return 20;
	}

	public int getDefaultHeight() {
		return 20;
	}

	@Override
	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		return null;
	}

	@Override
	public MTable getMTable() {
		INode node = getParent();
		while (node != null) {
			if (node instanceof MTable) {
				return (MTable) node;
			}
			node = node.getParent();
		}
		return null;
	}

	@Override
	public Color getForeground() {
		return ColorConstants.black;
	}

	@Override
	public void setValue(Object value) {
		if (value == null && cell != null)
			cell.getEventSupport().removePropertyChangeListener(this);

		super.setValue(value);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName()
				.equals(JRDesignElementGroup.PROPERTY_CHILDREN)) {
			if (evt.getSource() == cell) {
				if (evt.getOldValue() == null && evt.getNewValue() != null) {
					int newIndex = -1;
					if (evt instanceof CollectionElementAddedEvent) {
						newIndex = ((CollectionElementAddedEvent) evt)
								.getAddedIndex();
					}
					// add the node to this parent
					ANode n = ReportFactory.createNode(this, evt.getNewValue(),
							newIndex);
					if (evt.getNewValue() instanceof JRElementGroup) {
						JRElementGroup jrFrame = (JRElementGroup) evt
								.getNewValue();
						ReportFactory.createElementsForBand(n,
								jrFrame.getChildren());
					}
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
			}
		}
		HashSet<String> graphicalProperties = getGraphicalProperties();
		if (graphicalProperties.contains(evt.getPropertyName())){
			setChangedProperty(true);
		}
		super.propertyChange(evt);
	}

	public JRBoxContainer getBoxContainer() {
		return cell;
	}

	public int getTopPadding() {
		return cell.getLineBox().getTopPadding();
	}

	public int getLeftPadding() {
		return cell.getLineBox().getLeftPadding();
	}

	public MTable getTable() {
		INode node = this;
		while (node != null && node.getParent() != null
				&& !(node instanceof MTable) && !(node instanceof MRoot)) {
			node = node.getParent();
		}
		if (node instanceof MTable)
			return (MTable) node;
		return null;
	}

	@Override
	public Dimension getSize() {
		return new Dimension(getValue().getWidth(), getCell().getHeight());
	}

	@Override
	public JRElementGroup getJRElementGroup() {
		return getCell();
	}

	@Override
	public JRPropertiesHolder[] getPropertyHolder() {
		return new JRPropertiesHolder[] { cell, getValue(),
				getMTable().getValue() };
	}
	
	/**
	 * Flag changed when some property that has graphical impact on the element is changed.
	 * This is used to redraw the elemnt only when something graphical is changed isndie it,
	 * all the other times can just be copied
	 */
	private boolean visualPropertyChanged = true;

	/**
	 * True if some graphical property is changed for the element, false otherwise
	 */
	@Override
	public boolean hasChangedProperty(){
		synchronized (this) {
			return visualPropertyChanged;
		}
	}
	
	/**
	 * Set the actual state of the property change flag
	 */
	@Override
	public void setChangedProperty(boolean value){
		synchronized (this) {
			if (value){
				ANode parent = getParent();
				while(parent != null){
					if (parent instanceof IGraphicalPropertiesHandler){
						IGraphicalPropertiesHandler handler = (IGraphicalPropertiesHandler)parent;
						handler.setChangedProperty(true);
						//We can exit the cycle since the setChangedProperty on the parent will propagate the
						//refresh on the upper levels
						break;
					} else {
						parent = parent.getParent();
					}
				}
			}
			visualPropertyChanged = value;
		}
	}

	private static HashSet<String> cachedGraphicalProperties = null;
	
	/**
	 * Return the graphical properties for an MGraphicalElement
	 */
	@Override
	public HashSet<String> getGraphicalProperties() {
		if (cachedGraphicalProperties == null){
			cachedGraphicalProperties = new HashSet<String>();
			cachedGraphicalProperties.add(DesignCell.PROPERTY_STYLE);
			cachedGraphicalProperties.add(DesignCell.PROPERTY_ROW_SPAN);
			cachedGraphicalProperties.add(DesignCell.PROPERTY_HEIGHT);
		}
		return cachedGraphicalProperties;
	}
	
	private void addStyle(HashSet<String> stylesMap, JRStyle style){
		if (style != null) stylesMap.add(style.getName());
	}
	
	/**
	 * Return all the styles of the column
	 */
	@Override
	public HashSet<String> getUsedStyles() {
		StandardBaseColumn standardCol = getValue();
		HashSet<String> result = new HashSet<String>();
		if (standardCol.getColumnFooter() != null) addStyle(result, standardCol.getColumnFooter().getStyle());
		if (standardCol.getColumnHeader() != null) addStyle(result, standardCol.getColumnFooter().getStyle());
		if (standardCol.getTableHeader() != null) addStyle(result, standardCol.getTableHeader().getStyle());
		if (standardCol.getTableFooter() != null) addStyle(result, standardCol.getTableFooter().getStyle());
		if (standardCol instanceof StandardColumn){
			DesignCell detCell = (DesignCell) ((StandardColumn)standardCol).getDetailCell();
			if (detCell != null)  addStyle(result, detCell.getStyle());
		}
		return result;
	}

	@Override
	public List<INode> initModel() {
		return getChildren();
	}
}
