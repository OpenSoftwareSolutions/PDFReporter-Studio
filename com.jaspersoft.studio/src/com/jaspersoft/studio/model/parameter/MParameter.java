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
package com.jaspersoft.studio.model.parameter;

import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignParameter;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.editor.expression.ExpressionContext.Visibility;
import com.jaspersoft.studio.editor.expression.IExpressionContextSetter;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.checkbox.CheckBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.properties.JPropertiesPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class MParameter.
 * 
 * @author Chicu Veaceslav
 */
public class MParameter extends MParameterSystem implements ICopyable {
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
			iconDescriptor = new NodeIconDescriptor("parameter"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m parameter.
	 */
	public MParameter() {
		super();
	}

	public boolean isMainDataset() {
		if (getParent() instanceof MParameters) {
			MParameters prms = (MParameters) getParent();
			if (prms.getValue() instanceof JRDesignDataset)
				if (getJasperDesign().getMainDataset() == prms.getValue())
					return true;
		}
		return false;
	}

	/**
	 * Instantiates a new m parameter.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrParameter
	 *          the jr parameter
	 * @param newIndex
	 *          the new index
	 */
	public MParameter(ANode parent, JRDesignParameter jrParameter, int newIndex) {
		super(parent, jrParameter, newIndex);
	}
	

	@Override
	public JRDesignParameter getValue() {
		return (JRDesignParameter) super.getValue();
	}

	@Override
	public Color getForeground() {
		return null;
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		super.createPropertyDescriptors(desc, defaultsMap);

		NTextPropertyDescriptor descriptionD = new NTextPropertyDescriptor(JRDesignParameter.PROPERTY_DESCRIPTION,
				Messages.common_description);
		descriptionD.setDescription(Messages.MParameter_description_description);
		desc.add(descriptionD);
		descriptionD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#parameterDescription"));

		CheckBoxPropertyDescriptor isForPromptingD = new CheckBoxPropertyDescriptor(
				JRDesignParameter.PROPERTY_FOR_PROMPTING, Messages.MParameter_is_for_prompting, NullEnum.NOTNULL);
		isForPromptingD.setDescription(Messages.MParameter_is_for_prompting_description);
		desc.add(isForPromptingD);

		JRExpressionPropertyDescriptor defValueExprD = new JRExpressionPropertyDescriptor(
				JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION, Messages.MParameter_default_value_expression);
		defValueExprD.setDescription(Messages.MParameter_default_value_expression_description);
		desc.add(defValueExprD);
		defValueExprD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#defaultValueExpression"));

		JPropertiesPropertyDescriptor propertiesD = new JPropertiesPropertyDescriptor(PROPERTY_MAP,
				Messages.common_properties);
		propertiesD.setDescription(Messages.MParameter_properties_description);
		desc.add(propertiesD);
		propertiesD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#property"));

		NClassTypePropertyDescriptor classD = new NClassTypePropertyDescriptor(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME,
				Messages.MParameter_nested_type_name);
		classD.setDescription(Messages.MParameter_nested_type_name_description);
		desc.add(classD);

		defaultsMap.put(JRDesignParameter.PROPERTY_FOR_PROMPTING, Boolean.TRUE);
		defaultsMap.put(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME, null);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#parameter");
	}

	private static final String PROPERTY_MAP = "PROPERTY_MAP"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.parameter.MParameterSystem#getPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		JRDesignParameter jrParameter = (JRDesignParameter) getValue();
		if (id.equals(JRDesignParameter.PROPERTY_DESCRIPTION))
			return jrParameter.getDescription();
		if (id.equals(JRDesignParameter.PROPERTY_FOR_PROMPTING))
			return new Boolean(jrParameter.isForPrompting());
		if (id.equals(JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION))
			return ExprUtil.getExpression(jrParameter.getDefaultValueExpression());
		if (id.equals(PROPERTY_MAP)) {
			return jrParameter.getPropertiesMap();
		}
		if (id.equals(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME))
			return jrParameter.getNestedTypeName();
		return super.getPropertyValue(id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.parameter.MParameterSystem#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		JRDesignParameter jrParameter = (JRDesignParameter) getValue();
		if (id.equals(JRDesignParameter.PROPERTY_DESCRIPTION))
			jrParameter.setDescription((String) value);
		else if (id.equals(JRDesignParameter.PROPERTY_FOR_PROMPTING) && isMainDataset())
			jrParameter.setForPrompting(((Boolean) value).booleanValue());
		else if (id.equals(JRDesignParameter.PROPERTY_NESTED_TYPE_NAME))
			jrParameter.setNestedTypeName((String) value);
		else if (id.equals(JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION))
			jrParameter.setDefaultValueExpression(ExprUtil.setValues(jrParameter.getDefaultValueExpression(), value));
		else if (id.equals(PROPERTY_MAP)) {
			JRPropertiesMap v = (JRPropertiesMap) value;
			String[] names = jrParameter.getPropertiesMap().getPropertyNames();
			for (int i = 0; i < names.length; i++) {
				jrParameter.getPropertiesMap().removeProperty(names[i]);
			}
			names = v.getPropertyNames();
			for (int i = 0; i < names.length; i++)
				jrParameter.getPropertiesMap().setProperty(names[i], v.getProperty(names[i]));
			this.getPropertyChangeSupport().firePropertyChange(PROPERTY_MAP, false, true);
		}
		super.setPropertyValue(id, value);
	}

	/**
	 * Creates the jr parameter.
	 * 
	 * @param jrDataset
	 *          the jr dataset
	 * @return the jR design parameter
	 */
	public static JRDesignParameter createJRParameter(JRDesignDataset jrDataset) {
		JRDesignParameter jrDesignParameter = new JRDesignParameter();
		jrDesignParameter.setSystemDefined(false);
		jrDesignParameter.setName(ModelUtils.getDefaultName(jrDataset.getParametersMap(), "Parameter")); //$NON-NLS-1$
		return jrDesignParameter;
	}
	
	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		for(IPropertyDescriptor d : descriptors) {
			if(d.getId().equals(JRDesignParameter.PROPERTY_DEFAULT_VALUE_EXPRESSION)) {
				// fix the visibilities mask: allows only PARAMETERS
				ExpressionContext expContext = getExpressionContext();
				if(expContext!=null){
					expContext.setVisibilities(EnumSet.of(Visibility.SHOW_PARAMETERS));
					((IExpressionContextSetter)d).setExpressionContext(expContext);
				}
			}
		}
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MParameters)
			return true;
		return false;
	}
	
	public ExpressionContext getExpressionContext() {
		JRDesignDataset dataSet = ModelUtils.getDataset(this);
		JasperReportsConfiguration conf = getJasperConfiguration();
		if (dataSet != null && conf != null)
			return new ExpressionContext(dataSet, conf);
		return null;
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setEditable(true);
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		if (ExpressionContext.class.equals(adapter)) {
			return getExpressionContext();
		}
		return super.getAdapter(adapter);
	}
}
