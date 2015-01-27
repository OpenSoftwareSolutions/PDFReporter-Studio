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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.table.DesignCell;
import net.sf.jasperreports.components.table.StandardBaseColumn;
import net.sf.jasperreports.components.table.util.TableUtil;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesHolder;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignGroup;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRPropertyChangeSupport;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.table.TableManager;
import com.jaspersoft.studio.components.table.TableNodeIconDescriptor;
import com.jaspersoft.studio.components.table.messages.Messages;
import com.jaspersoft.studio.components.table.model.AMCollection;
import com.jaspersoft.studio.components.table.model.MTable;
import com.jaspersoft.studio.components.table.model.MTableGroupFooter;
import com.jaspersoft.studio.components.table.model.MTableGroupHeader;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroup;
import com.jaspersoft.studio.components.table.model.columngroup.MColumnGroupCell;
import com.jaspersoft.studio.components.table.util.TableColumnNumerator;
import com.jaspersoft.studio.components.table.util.TableColumnSize;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.IContainerLayout;
import com.jaspersoft.studio.model.IGraphicElement;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.IPastable;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.propexpr.JPropertyExpressionsDescriptor;
import com.jaspersoft.studio.property.descriptor.propexpr.PropertyExpressionsDTO;
import com.jaspersoft.studio.property.descriptors.PixelPropertyDescriptor;
import com.jaspersoft.studio.utils.Misc;

public class MColumn extends APropertyNode implements IPastable, IContainer,
		IContainerLayout, IGraphicElement, IContainerEditPart {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

	public static String PROPERTY_NAME = "NAME";
	
	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new TableNodeIconDescriptor("tablecell"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m field.
	 */
	public MColumn() {
		super();
	}

	private JRDesignGroup jrGroup;

	public JRDesignGroup getJrGroup() {
		return jrGroup;
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
	public MColumn(ANode parent, StandardBaseColumn column, String name,
			int index) {
		super(parent, index);
		setValue(column);
		this.name = name;
		List<ANode> n = getAMCollection();
		if (n != null && !n.isEmpty()) {
			ANode aNode =  n.get(n.size() - 1);
			type = TableColumnSize.getType(aNode.getClass());
			if (aNode instanceof MTableGroupHeader) {
				jrGroup = ((MTableGroupHeader) aNode).getJrDesignGroup();
				grName = jrGroup.getName();
			}
			if (aNode instanceof MTableGroupFooter) {
				jrGroup = ((MTableGroupFooter) aNode).getJrDesignGroup();
				grName = jrGroup.getName();
			}
		}
	}

	public MColumn getNorth() {
		ANode mparent = getParent();
		if (TableManager.isBottomOfTable(type)) {
			if (this instanceof MColumnGroup
					|| this instanceof MColumnGroupCell)
				return (MColumn) getChildren().get(0);

			MTable mtable = getMTable();
			List<ANode> amCollection = getAMCollection();
			int index = mtable.getChildren().indexOf(
					amCollection.get(amCollection.size() - 1));
			AMCollection newmc = (AMCollection) mtable.getChildren().get(
					index - 1);
			return (MColumn) newmc.getChildren().get(0);
		} else if (mparent instanceof MColumnGroup
				|| mparent instanceof MColumnGroupCell)
			return (MColumn) mparent;

		MTable mtable = getMTable();
		int index = mtable.getChildren().indexOf(mparent);
		if (index > 0) {
			AMCollection newmc = (AMCollection) mtable.getChildren().get(
					index - 1);
			return getBottomColumn(newmc.getChildren());
		} else
			return null;
	}

	private MColumn getBottomColumn(List<INode> newmc) {
		for (INode col : newmc) {
			if (col instanceof MColumnGroup || col instanceof MColumnGroupCell)
				col = getBottomColumn(col.getChildren());
			if (col instanceof MColumn)
				return (MColumn) col;
		}
		return null;
	}

	private String grName;
	private int type = TableUtil.TABLE_HEADER;

	public int getType() {
		return type;
	}

	public String getGrName() {
		return Misc.nvl(grName);
	}

	@Override
	public StandardBaseColumn getValue() {
		return (StandardBaseColumn) super.getValue();
	}

	private String name;

	@Override
	public Color getForeground() {
		return ColorConstants.lightGray;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		String oldValue = this.name;
		this.name = name;
		getPropertyChangeSupport().firePropertyChange(PROPERTY_NAME, oldValue, name); //$NON-NLS-1$
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
		String tt = "";// getValue().getUUID().toString() + "\n";
		List<ANode> nodes = getAMCollection();
		for (int i = nodes.size() - 1; i >= 0; i--)
			tt += nodes.get(i).getDisplayText() + "\n";
		tt += "\t" + getIconDescriptor().getToolTip() + ": " + getDisplayText();
		return tt;
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
		JRExpressionPropertyDescriptor printWhenExprD = new JRExpressionPropertyDescriptor(
				StandardBaseColumn.PROPERTY_PRINT_WHEN_EXPRESSION,
				Messages.MColumn_print_when_expression);
		printWhenExprD
				.setDescription(Messages.MColumn_print_when_expression_description);
		desc.add(printWhenExprD);

		PixelPropertyDescriptor wD = new PixelPropertyDescriptor(
				StandardBaseColumn.PROPERTY_WIDTH,
				Messages.MColumn_column_width);
		desc.add(wD);

		// JPropertiesPropertyDescriptor propertiesMapD = new
		// JPropertiesPropertyDescriptor(
		// MGraphicElement.PROPERTY_MAP,
		// com.jaspersoft.studio.messages.Messages.common_properties);
		// propertiesMapD
		// .setDescription(com.jaspersoft.studio.messages.Messages.common_properties);
		// desc.add(propertiesMapD);

		JPropertyExpressionsDescriptor propertiesD = new JPropertyExpressionsDescriptor(
				JRDesignElement.PROPERTY_PROPERTY_EXPRESSIONS,
				com.jaspersoft.studio.messages.Messages.MGraphicElement_property_expressions);
		propertiesD
				.setDescription(com.jaspersoft.studio.messages.Messages.MGraphicElement_property_expressions_description);
		desc.add(propertiesD);

		printWhenExprD.setCategory(Messages.MColumn_column_properties_category);
		wD.setCategory(Messages.MColumn_column_properties_category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java
	 * .lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		StandardBaseColumn jrElement = getValue();
		if (id.equals(StandardBaseColumn.PROPERTY_WIDTH))
			return jrElement.getWidth();
		if (id.equals(StandardBaseColumn.PROPERTY_PRINT_WHEN_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getPrintWhenExpression());
		if (id.equals(DesignCell.PROPERTY_HEIGHT))
			return getMTable().getTableManager().getYhcolumn(type, grName,
					jrElement).height;
		JRPropertiesMap propertiesMap = jrElement.getPropertiesMap();
		if (propertiesMap != null)
			propertiesMap = propertiesMap.cloneProperties();
		if (id.equals(JRDesignElement.PROPERTY_PROPERTY_EXPRESSIONS)) {
			JRPropertyExpression[] propertyExpressions = jrElement
					.getPropertyExpressions();
			if (propertyExpressions != null)
				propertyExpressions = propertyExpressions.clone();
			return new PropertyExpressionsDTO(propertyExpressions,
					propertiesMap, this);
		}
		if (id.equals(MGraphicElement.PROPERTY_MAP))
			return propertiesMap;
		return null;
	}

	private boolean canSet = true;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java
	 * .lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		StandardBaseColumn jrElement = getValue();

		if (id.equals(StandardBaseColumn.PROPERTY_WIDTH)) {
			if ((Integer) value >= 0 && canSet) {
				canSet = false;
				MTable table = getMTable();

				table.getTableManager().setWidth(jrElement, (Integer) value);
				table.getTableManager().update();
				// table.getTableManager().refresh();
				getPropertyChangeSupport()
						.firePropertyChange(
								new PropertyChangeEvent(this,
										StandardBaseColumn.PROPERTY_WIDTH,
										null, value));

				canSet = true;
			}
		} else if (id.equals(DesignCell.PROPERTY_HEIGHT)) {
			MTable mtable = getMTable();
			Integer height = (Integer) value;
			AMCollection section = getSection();
			if (section != null && height.intValue() >= 0) {

				@SuppressWarnings("unchecked")
				Class<AMCollection> classType = (Class<AMCollection>) section
						.getClass();
				String grName = null;
				if (section instanceof MTableGroupHeader)
					grName = ((MTableGroupHeader) section).getJrDesignGroup()
							.getName();
				if (section instanceof MTableGroupFooter)
					grName = ((MTableGroupFooter) section).getJrDesignGroup()
							.getName();

				mtable.getTableManager().setHeight(null, height, jrElement,
						TableColumnSize.getType(classType), grName);

				// cell.setHeight(height);
				mtable.getTableManager().update();

				getPropertyChangeSupport().firePropertyChange(
						new PropertyChangeEvent(this,
								DesignCell.PROPERTY_HEIGHT, null, value));
			}
		} else if (id.equals(StandardBaseColumn.PROPERTY_PRINT_WHEN_EXPRESSION))
			jrElement.setPrintWhenExpression(ExprUtil.setValues(
					jrElement.getPrintWhenExpression(), value, null));
		else if (id.equals(MGraphicElement.PROPERTY_MAP)) {
			JRPropertiesMap v = (JRPropertiesMap) value;
			String[] names = jrElement.getPropertiesMap().getPropertyNames();
			for (int i = 0; i < names.length; i++) {
				jrElement.getPropertiesMap().removeProperty(names[i]);
			}
			names = v.getPropertyNames();
			for (int i = 0; i < names.length; i++)
				jrElement.getPropertiesMap().setProperty(names[i],
						v.getProperty(names[i]));
			this.getPropertyChangeSupport().firePropertyChange(
					MGraphicElement.PROPERTY_MAP, false, true);
		} else if (id.equals(JRDesignElement.PROPERTY_PROPERTY_EXPRESSIONS)) {
			if (value instanceof PropertyExpressionsDTO) {
				PropertyExpressionsDTO dto = (PropertyExpressionsDTO) value;
				JRPropertyExpression[] v = dto.getPropExpressions();
				JRPropertyExpression[] expr = jrElement
						.getPropertyExpressions();
				if (expr != null)
					for (JRPropertyExpression ex : expr)
						jrElement.removePropertyExpression(ex);
				if (v != null)
					for (JRPropertyExpression p : v)
						jrElement.addPropertyExpression(p);
				// now change properties
				JRPropertiesMap vmap = dto.getPropMap();
				String[] names = jrElement.getPropertiesMap()
						.getPropertyNames();
				for (int i = 0; i < names.length; i++) {
					jrElement.getPropertiesMap().removeProperty(names[i]);
				}
				if (vmap != null) {
					names = vmap.getPropertyNames();
					for (int i = 0; i < names.length; i++)
						jrElement.getPropertiesMap().setProperty(names[i],
								vmap.getProperty(names[i]));
					this.getPropertyChangeSupport().firePropertyChange(
							MGraphicElement.PROPERTY_MAP, false, true);
				}
			}
		}
	}

	public JRDesignElement createJRElement(JasperDesign jasperDesign) {
		return null;
	}

	public MTable getMTable() {
		ANode node = getParent();
		while (node != null) {
			if (node instanceof MTable) {
				return (MTable) node;
			}
			node = node.getParent();
		}
		return null;
	}

	private List<ANode> list;

	public List<ANode> getAMCollection() {
		if (list == null) {
			list = new ArrayList<ANode>();
			ANode node = getParent();
			while (node != null) {
				list.add(node);
				if (node instanceof AMCollection)
					return list;
				node = node.getParent();
			}
		}
		return list;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		final AMCollection section = getSection();
		if (section != null) {
			if (evt.getPropertyName().equals(section.getCellEvent())) {
				if (evt.getSource() == this.getValue()) {

					final StandardBaseColumn bc = (StandardBaseColumn) evt
							.getSource();

					final ANode parent = (ANode) getParent();
					final MColumn child = this;
					final int newIndex = parent.getChildren().indexOf(this);

					parent.removeChild(child);

					section.createColumn(parent, bc, 122, newIndex);

					MTable mtable = (MTable) section.getParent();
					if (mtable == null){
						((JRPropertyChangeSupport)evt.getSource()).removePropertyChangeListener(child);
					} else {
						mtable.getTableManager().refresh();
						TableColumnNumerator.renumerateColumnNames(mtable);
						parent.propertyChange(evt);
					}
				}
			}
		}
		super.propertyChange(evt);
	}

	public AMCollection getSection() {
		INode n = getParent();
		while (n != null) {
			if (n instanceof AMCollection)
				return (AMCollection) n;
			n = n.getParent();
		}
		return null;
	}

	public Rectangle getBounds() {
		StandardBaseColumn c = getValue();
		MTable mc = getMTable();
		if (mc != null && c != null)
			return mc.getTableManager().getBounds(c, type, grName);
		return null;
	}

	public int getDefaultWidth() {
		return 0;
	}

	public int getDefaultHeight() {
		return 0;
	}

	@Override
	public JRPropertiesHolder[] getPropertyHolder() {
		return new JRPropertiesHolder[] { getValue(), getMTable().getValue() };
	}

}
