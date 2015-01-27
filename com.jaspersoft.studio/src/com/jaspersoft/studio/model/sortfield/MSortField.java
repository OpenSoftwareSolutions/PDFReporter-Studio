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
package com.jaspersoft.studio.model.sortfield;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRField;
import net.sf.jasperreports.engine.JRSortField;
import net.sf.jasperreports.engine.JRVariable;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignSortField;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.SortOrderEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.JaspersoftStudioPlugin;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.IDragable;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.model.variable.MVariable;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.section.AbstractSection;
import com.jaspersoft.studio.property.section.widgets.ASPropertyWidget;
import com.jaspersoft.studio.property.section.widgets.SPToolBarEnum;
import com.jaspersoft.studio.utils.ModelUtils;

/*
 * The Class MField.
 * 
 * @author Chicu Veaceslav
 */
public class MSortField extends APropertyNode implements ICopyable, IDragable {
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
			iconDescriptor = new NodeIconDescriptor("sortfield"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m field.
	 */
	public MSortField() {
		super();
	}

	/**
	 * Instantiates a new m field.
	 * 
	 * @param parent
	 *          the parent
	 * @param jfRield
	 *          the jf rield
	 * @param newIndex
	 *          the new index
	 */
	public MSortField(ANode parent, JRDesignSortField jfRield, int newIndex) {
		super(parent, newIndex);
		setValue(jfRield);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		return ((JRDesignSortField) getValue()).getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getImagePath()
	 */
	public ImageDescriptor getImagePath() {
		if (getValue() != null) {
			JRDesignSortField sortField = (JRDesignSortField) getValue();
			if (sortField.getType().equals(SortFieldTypeEnum.FIELD))
				return MField.getIconDescriptor().getIcon16();
			if (sortField.getType().equals(SortFieldTypeEnum.VARIABLE))
				return MVariable.getIconDescriptor().getIcon16();
		}
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

	private IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private RComboBoxPropertyDescriptor nameD;

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
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		if (nameD != null) {
			JRDesignDataset jrDataset = getDataSet();
			if (jrDataset == null)
				return;
			if (getValue() != null) {
				Map<String, JRSortField> sortFields = jrDataset.getSortFieldsMap();
				JRDesignSortField sortField = (JRDesignSortField) getValue();
				List<String> items = new ArrayList<String>();
				items.add(sortField.getName());
				if (sortField.getType().equals(SortFieldTypeEnum.FIELD)) {
					for (JRField f : jrDataset.getFieldsList()) {
						JRSortField checkIfPresent = sortFields.get(f.getName() + "|" + SortFieldTypeEnum.FIELD.getName());
						//If a field with the same name is not present or if it is present but with a different type then show it
						if (checkIfPresent == null){
							items.add(f.getName());
						}
					}
				} else {
					for (JRVariable f : jrDataset.getVariablesList()) {
						JRSortField checkIfPresent = sortFields.get(f.getName() + "|" + SortFieldTypeEnum.VARIABLE.getName());
						if (checkIfPresent == null){
							items.add(f.getName());
						}
					}
				}
				nameD.setItems(items.toArray(new String[items.size()]));
			}
		}
	}

	private JRDesignDataset dataset;
	private static JSSEnumPropertyDescriptor typeD;
	private static JSSEnumPropertyDescriptor orderD;

	protected JRDesignDataset getDataSet() {
		if (dataset != null)
			return dataset;
		return ModelUtils.getDataset(this);
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		nameD = new RComboBoxPropertyDescriptor(JRDesignSortField.PROPERTY_NAME, Messages.common_name, new String[] { "" }); //$NON-NLS-1$
		nameD.setDescription(Messages.MSortField_name_description);
		desc.add(nameD);

		typeD = new JSSEnumPropertyDescriptor(JRDesignSortField.PROPERTY_TYPE, Messages.MSortField_typeTitle,
				SortFieldTypeEnum.class, NullEnum.NOTNULL) {
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				Image[] images = new Image[] {
						JaspersoftStudioPlugin.getInstance().getImage("icons/resources/fields-sort-16.png"), //$NON-NLS-1$
						JaspersoftStudioPlugin.getInstance().getImage("icons/resources/variables-sort-16.png") }; //$NON-NLS-1$
				return new SPToolBarEnum(parent, section, this, images, false);
			}
		};
		typeD.setDescription("Sort field type"); //$NON-NLS-1$
		desc.add(typeD);

		orderD = new JSSEnumPropertyDescriptor(JRDesignSortField.PROPERTY_ORDER, Messages.common_order,
				SortOrderEnum.class, NullEnum.NOTNULL) {
			public ASPropertyWidget createWidget(Composite parent, AbstractSection section) {
				Image[] images = new Image[] {
						JaspersoftStudioPlugin.getInstance().getImage("icons/resources/sort-number-column.png"), //$NON-NLS-1$
						JaspersoftStudioPlugin.getInstance().getImage("icons/resources/sort-number-descending.png") }; //$NON-NLS-1$
				return new SPToolBarEnum(parent, section, this, images, false);
			}
		};
		orderD.setDescription(Messages.MSortField_order_description);
		desc.add(orderD);

		defaultsMap.put(JRDesignSortField.PROPERTY_ORDER, typeD.getEnumValue(SortOrderEnum.ASCENDING));
		defaultsMap.put(JRDesignSortField.PROPERTY_TYPE, orderD.getEnumValue(SortFieldTypeEnum.FIELD));

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#sortField"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignSortField jrField = (JRDesignSortField) getValue();
		if (id.equals(JRDesignSortField.PROPERTY_NAME))
			return jrField.getName();
		if (id.equals(JRDesignSortField.PROPERTY_ORDER)) {
			if (orderD == null)
				getPropertyDescriptors();
			return orderD.getEnumValue(jrField.getOrderValue());
		}
		if (id.equals(JRDesignSortField.PROPERTY_TYPE)) {
			if (typeD == null)
				getPropertyDescriptors();
			return typeD.getEnumValue(jrField.getType());
		}
		return null;
	}

	/**
	 * FIXME: this function is used to generate the key from a sortfield into the sortfields map inside the jasperreports
	 * structure. This function in jasperreport is private and for this reason it was copied here. It is necessary because
	 * when the name of a sortfield is changed also the map should be updated but JR dosen't do that, so we need to do it
	 * manually, but to do it we need the function to calculate the key. Delete and reinsert the sortfield is not a
	 * solution, it's an unnecessary heavy operation and other that this it raise a series of events that cause many
	 * problems in JSS nodes model
	 * 
	 */
	@SuppressWarnings("unused")
	private String getSortFieldKey(JRSortField sortField) {
		return getSortFieldKey(sortField.getName(), sortField.getType().getName());
	}
	
	private String getSortFieldKey(String name, String type){
		return name + "|" + type;
	}

	
	/**
	 * Change the name and the type of the sortfields updating also its entry
	 * in the dataset map
	 * 
	 * @param oldName the old name
	 * @param oldType the old type
	 * @param newName the new name
	 * @param newType the new type
	 * @param field the field
	 */
	private void changeNameAndType(String oldName, String oldType, String newName, SortFieldTypeEnum newType, JRSortField field){
		JRDesignDataset d = ModelUtils.getDataset(this);
		if (d != null) {
			String oldKey = getSortFieldKey(oldName, oldType);
			d.getSortFieldsMap().remove(oldKey);
			d.getSortFieldsMap().put(getSortFieldKey(newName, newType.getName()), field);
			JRDesignSortField jrField = (JRDesignSortField) field;
			jrField.setName(newName);
			jrField.setType(newType);
		}
	}
	
	/**
	 * Change the type of the sort fields and give to it also a new available name 
	 * according the its new type (the first free name). If there aren't free names 
	 * available for that type then the type is not change either
	 * 
	 * @param newType the new type of the sort field
	 * @return true if the type and renaming option was successful, false otherwise
	 */
	private boolean selectFirstAvailableName(SortFieldTypeEnum newType){
		JRDesignDataset d = ModelUtils.getDataset(this);
		JRDesignSortField jrField = (JRDesignSortField) getValue();
		//chek if the type is the same
		if (!newType.equals(jrField.getType())){
			String oldType = jrField.getType().getName();
			if (newType.equals(SortFieldTypeEnum.FIELD)) {
				List<JRField> fields = d.getFieldsList();
				for (JRField field : fields){
					String newName = field.getName();
					String key = getSortFieldKey(newName, SortFieldTypeEnum.FIELD.getName());
					if (!d.getSortFieldsMap().containsKey(key)){
						changeNameAndType(jrField.getName(), oldType, newName, SortFieldTypeEnum.FIELD, jrField);
						return true;
					}
				}
			} else {
				List<JRVariable> variables = d.getVariablesList();
				for (JRVariable variable : variables){
					String newName = variable.getName();
					String key = getSortFieldKey(newName, SortFieldTypeEnum.VARIABLE.getName());
					if (!d.getSortFieldsMap().containsKey(key)){
						changeNameAndType(jrField.getName(), oldType, newName, SortFieldTypeEnum.VARIABLE, jrField);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignSortField jrField = (JRDesignSortField) getValue();
		if (id.equals(JRDesignSortField.PROPERTY_NAME)) {
			if (!value.equals("")) { //$NON-NLS-1$
				String oldName = jrField.getName();
				String oldType = jrField.getType().getName();
				String newName = (String) value;
				//The type is the same
				changeNameAndType(oldName, oldType, newName,  jrField.getType(), jrField);
			}
		} else if (id.equals(JRDesignSortField.PROPERTY_ORDER))
			jrField.setOrder((SortOrderEnum) orderD.getEnumValue(value));
		else if (id.equals(JRDesignSortField.PROPERTY_TYPE)) {
			SortFieldTypeEnum type = (SortFieldTypeEnum) typeD.getEnumValue(value);
			selectFirstAvailableName(type);
		}
	}

	/**
	 * Creates the jr field.
	 * 
	 * @param jrDataset
	 *          the jr dataset
	 * @return the jR design field
	 */
	public static JRDesignSortField createJRSortField(JRDesignDataset jrDataset) {
		JRDesignSortField jrDesignField = new JRDesignSortField();
		return jrDesignField;
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MSortFields)
			return true;
		return false;
	}
}
