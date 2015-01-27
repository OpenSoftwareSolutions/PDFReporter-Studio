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
package com.jaspersoft.studio.components.sort.model;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.components.sort.SortComponent;
import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRElement;
import net.sf.jasperreports.engine.component.ComponentKey;
import net.sf.jasperreports.engine.design.JRDesignComponentElement;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.design.events.JRChangeEventsSupport;
import net.sf.jasperreports.engine.type.EvaluationTimeEnum;
import net.sf.jasperreports.engine.type.HorizontalAlignEnum;
import net.sf.jasperreports.engine.type.SortFieldTypeEnum;
import net.sf.jasperreports.engine.type.VerticalAlignEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.components.sort.SortNodeIconDescriptor;
import com.jaspersoft.studio.editor.defaults.DefaultManager;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.MGraphicElement;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.color.ColorPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.utils.AlfaRGB;
import com.jaspersoft.studio.utils.Colors;
import com.jaspersoft.studio.utils.EnumHelper;

/**
 * 
 * @author veaceslav chicu
 * 
 */
public class MSort extends MGraphicElement {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public MSort() {
		super();
	}

	public MSort(ANode parent, JRDesignComponentElement jrObject, int newIndex) {
		super(parent, jrObject, newIndex);
	}

	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;
	
	private RComboBoxPropertyDescriptor evaluationGroupNameD;

	@Override
	public void setValue(Object value) {
		if (getValue() != null) {
			Object obj = getComponent();
			if (obj instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport()
						.removePropertyChangeListener(this);
		}
		if (value != null) {
			Object obj = getComponent(value);
			if (value instanceof JRChangeEventsSupport)
				((JRChangeEventsSupport) obj).getEventSupport()
						.addPropertyChangeListener(this);
		}
		super.setValue(value);
	}

	private Object getComponent() {
		return getComponent(getValue());
	}

	private Object getComponent(Object value) {
		if (value != null) {
			JRDesignComponentElement jrElement = (JRDesignComponentElement) value;
			return jrElement.getComponent();
		}
		return null;
	}

	@Override
	public JRDesignComponentElement createJRElement(JasperDesign jasperDesign) {
		JRDesignComponentElement jrcomponent = new JRDesignComponentElement();
		SortComponent component = new SortComponent();
		component.setSortFieldType(SortFieldTypeEnum.FIELD);
		jrcomponent.setComponent(component);
		jrcomponent.setComponentKey(new ComponentKey("http://jasperreports.sourceforge.net/jasperreports/components", "c", "sort")); //$NON-NLS-1$
		
		DefaultManager.INSTANCE.applyDefault(this.getClass(), jrcomponent);
		
		return jrcomponent;
	}

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new SortNodeIconDescriptor("sort"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getDisplayText()
	 */
	@Override
	public String getDisplayText() {
		return getIconDescriptor().getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getImagePath()
	 */
	@Override
	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.MGeneric#getToolTip()
	 */
	@Override
	public String getToolTip() {
		return getIconDescriptor().getToolTip();
	}

	private IPropertyDescriptor[] descriptors;
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
		super.createPropertyDescriptors(desc, defaultsMap);

		ComboBoxPropertyDescriptor evaluationTimeD = new ComboBoxPropertyDescriptor(
				SortComponent.PROPERTY_EVALUATION_TIME,
				Messages.common_evaluation_time, EnumHelper.getEnumNames(
						EvaluationTimeEnum.values(), NullEnum.NOTNULL));
		evaluationTimeD
				.setDescription(Messages.MGenericElement_evaluation_time_description);
		desc.add(evaluationTimeD);

		evaluationGroupNameD = new RComboBoxPropertyDescriptor(
				SortComponent.PROPERTY_EVALUATION_GROUP,
				Messages.MGenericElement_evaluation_group_name,
				new String[] { "" }); //$NON-NLS-2$
		evaluationGroupNameD
				.setDescription(Messages.MGenericElement_evaluation_group_name_description);
		desc.add(evaluationGroupNameD);

		ColorPropertyDescriptor color = new ColorPropertyDescriptor(
				SortComponent.PROPERTY_HANDLER_COLOR, "Handler Color",
				NullEnum.NULL);
		color.setDescription("Handler color");
		desc.add(color);

		ComboBoxPropertyDescriptor horizAlign = new ComboBoxPropertyDescriptor(
				SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN,
				"Handler Horizontal Alignement", EnumHelper.getEnumNames(
						HorizontalAlignEnum.values(), NullEnum.NOTNULL));
		horizAlign.setDescription("Handler horizontal alignement");
		desc.add(horizAlign);

		ComboBoxPropertyDescriptor vertAlign = new ComboBoxPropertyDescriptor(
				SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN,
				"Handler Vertical Alignement", EnumHelper.getEnumNames(
						VerticalAlignEnum.values(), NullEnum.NOTNULL));
		vertAlign.setDescription("Handler vertical alignement");
		desc.add(vertAlign);

		ComboBoxPropertyDescriptor sortFieldType = new ComboBoxPropertyDescriptor(
				SortComponent.PROPERTY_COLUMN_TYPE, "SortField Type",
				EnumHelper.getEnumNames(SortFieldTypeEnum.values(),
						NullEnum.NOTNULL));
		sortFieldType.setDescription("SortField type");
		desc.add(sortFieldType);

		NTextPropertyDescriptor sortFieldName = new NTextPropertyDescriptor(
				SortComponent.PROPERTY_COLUMN_NAME, "SortField Name");
		sortFieldName.setDescription("SortField name");
		desc.add(sortFieldName);

		color.setCategory("Sort Properties");
		sortFieldType.setCategory("Sort Properties");
		sortFieldName.setCategory("Sort Properties");

		horizAlign.setCategory("Sort Properties");
		vertAlign.setCategory("Sort Properties");
		evaluationTimeD.setCategory("Sort Properties");
		evaluationGroupNameD.setCategory("Sort Properties");

		defaultsMap.put(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN,
				VerticalAlignEnum.MIDDLE);
		defaultsMap.put(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN,
				HorizontalAlignEnum.LEFT);
		defaultsMap.put(SortComponent.PROPERTY_EVALUATION_TIME,
				EvaluationTimeEnum.NOW);
		defaultsMap.put(SortComponent.PROPERTY_HANDLER_COLOR, null);
		defaultsMap.put(SortComponent.PROPERTY_COLUMN_TYPE,
				SortFieldTypeEnum.FIELD);

	}
	
	@Override
	protected void setGroupItems(String[] items) {
		super.setGroupItems(items);
		if (evaluationGroupNameD != null)
			evaluationGroupNameD.setItems(items);
	}

	@Override
	public Object getPropertyValue(Object id) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		SortComponent component = (SortComponent) jrElement.getComponent();

		if (id.equals(SortComponent.PROPERTY_HANDLER_COLOR))
			return Colors.getSWTRGB4AWTGBColor(component.getHandlerColor());
		if (id.equals(SortComponent.PROPERTY_COLUMN_NAME))
			return component.getSortFieldName();
		if (id.equals(SortComponent.PROPERTY_COLUMN_TYPE))
			return EnumHelper.getValue(component.getSortFieldType(), 0, false);

		if (id.equals(SortComponent.PROPERTY_EVALUATION_TIME))
			return EnumHelper.getValue(component.getEvaluationTime(), 1, false);
		if (id.equals(SortComponent.PROPERTY_EVALUATION_GROUP))
			return component.getEvaluationGroup();
		if (id.equals(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN))
			return EnumHelper.getValue(component.getHandlerHorizontalAlign(),
					1, false);
		if (id.equals(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN))
			return EnumHelper.getValue(component.getHandlerVerticalAlign(), 1,
					false);
		return super.getPropertyValue(id);
	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignComponentElement jrElement = (JRDesignComponentElement) getValue();
		SortComponent component = (SortComponent) jrElement.getComponent();

		if (id.equals(SortComponent.PROPERTY_HANDLER_COLOR))
			component.setHandlerColor(Colors.getAWT4SWTRGBColor((AlfaRGB) value));
		else if (id.equals(SortComponent.PROPERTY_COLUMN_NAME))
			component.setSortFieldName((String) value);
		else if (id.equals(SortComponent.PROPERTY_COLUMN_TYPE))
			component.setSortFieldType((SortFieldTypeEnum) EnumHelper
					.getSetValue(SortFieldTypeEnum.values(), value, 0, false));

		else if (id.equals(SortComponent.PROPERTY_EVALUATION_TIME))
			component.setEvaluationTime((EvaluationTimeEnum) EnumHelper
					.getSetValue(EvaluationTimeEnum.values(), value, 1, false));
		else if (id.equals(SortComponent.PROPERTY_EVALUATION_GROUP)) {
			component.setEvaluationGroup((String) value);
		} else if (id.equals(SortComponent.PROPERTY_HANDLER_HORIZONTAL_ALIGN)) {
			component
					.setHandlerHorizontalAlign((HorizontalAlignEnum) EnumHelper
							.getSetValue(HorizontalAlignEnum.values(), value,
									1, false));
		} else if (id.equals(SortComponent.PROPERTY_HANDLER_VERTICAL_ALIGN)) {
			component.setHandlerVerticalAlign((VerticalAlignEnum) EnumHelper
					.getSetValue(VerticalAlignEnum.values(), value, 1, false));
		} else
			super.setPropertyValue(id, value);
	}

	@Override
	public void trasnferProperties(JRElement target){
		super.trasnferProperties(target);
		
		JRDesignComponentElement jrSourceElement = (JRDesignComponentElement) getValue();
		SortComponent jrSourceSort = (SortComponent) jrSourceElement.getComponent();
		
		JRDesignComponentElement jrTargetElement = (JRDesignComponentElement) target;
		SortComponent jrTargetSort = (SortComponent) jrTargetElement.getComponent();
		
		jrTargetSort.setHandlerColor(getColorClone(jrSourceSort.getHandlerColor()));
		jrTargetSort.setHandlerHorizontalAlign(jrSourceSort.getHandlerHorizontalAlign());
		jrTargetSort.setHandlerVerticalAlign(jrSourceSort.getHandlerVerticalAlign());
	}
}
