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
package com.jaspersoft.studio.model.variable;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignVariable;
import net.sf.jasperreports.engine.type.CalculationEnum;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.swt.graphics.Color;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.editor.expression.ExpressionContext;
import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class MVariable.
 * 
 * @author Chicu Veaceslav
 */
public class MVariable extends MVariableSystem implements ICopyable {
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
			iconDescriptor = new NodeIconDescriptor("variable"); //$NON-NLS-1$
		return iconDescriptor;
	}

	/**
	 * Instantiates a new m variable.
	 */
	public MVariable() {
		super();
	}

	@Override
	public Color getForeground() {
		return null;
	}

	/**
	 * Instantiates a new m variable.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrVariable
	 *          the jr variable
	 * @param newIndex
	 *          the new index
	 */
	public MVariable(ANode parent, JRDesignVariable jrVariable, int newIndex) {
		super(parent, jrVariable, newIndex);
	}

	@Override
	public JRDesignVariable getValue() {
		return (JRDesignVariable) super.getValue();
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
	public void setDescriptors(IPropertyDescriptor[] descriptors1, Map<String, Object> defaultsMap1) {
		descriptors = descriptors1;
		defaultsMap = defaultsMap1;
	}
	
	/**
	 * When the descriptor are read the group information are updated
	 */
	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		String[] items = getGroupList();
		if (items != null) {
			resetGroupD.setItems(items);
			incrementGroupD.setItems(items);
		}
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

		defaultsMap.put(JRDesignVariable.PROPERTY_VALUE_CLASS_NAME, "java.lang.String"); //$NON-NLS-1$

		resetGroupD = new RWComboBoxPropertyDescriptor(JRDesignVariable.PROPERTY_RESET_GROUP, Messages.common_reset_group,
				new String[] { "" }, NullEnum.NULL); //$NON-NLS-1$
		resetGroupD.setDescription(Messages.MVariable_reset_group_description);
		desc.add(resetGroupD);

		incrementGroupD = new RWComboBoxPropertyDescriptor(JRDesignVariable.PROPERTY_INCREMENT_GROUP,
				Messages.common_increment_group, new String[] { "" }, NullEnum.NULL); //$NON-NLS-1$
		incrementGroupD.setDescription(Messages.MVariable_increment_group_description);
		desc.add(incrementGroupD);

		calculationD = new JSSEnumPropertyDescriptor(JRDesignVariable.PROPERTY_CALCULATION, Messages.MVariable_calculation,
				CalculationEnum.class, NullEnum.NOTNULL);
		calculationD.setDescription(Messages.MVariable_calculation_description);
		desc.add(calculationD);

		resetTypeD = new JSSEnumPropertyDescriptor(JRDesignVariable.PROPERTY_RESET_TYPE, Messages.common_reset_type,
				ResetTypeEnum.class, NullEnum.NOTNULL);
		resetTypeD.setDescription(Messages.MVariable_reset_type_description);
		desc.add(resetTypeD);

		incrementTypeD = new JSSEnumPropertyDescriptor(JRDesignVariable.PROPERTY_INCREMENT_TYPE,
				Messages.common_increment_type, IncrementTypeEnum.class, NullEnum.NOTNULL);
		incrementTypeD.setDescription(Messages.MVariable_increment_type_description);
		desc.add(incrementTypeD);

		JRExpressionPropertyDescriptor expressionD = new JRExpressionPropertyDescriptor(
				JRDesignVariable.PROPERTY_EXPRESSION, Messages.common_expression);
		expressionD.setDescription(Messages.MVariable_expression_description);
		desc.add(expressionD);
		expressionD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#variableExpression"));

		JRExpressionPropertyDescriptor iniValExprD = new JRExpressionPropertyDescriptor(
				JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION, Messages.MVariable_initial_value_expression);
		iniValExprD.setDescription(Messages.MVariable_initial_value_expression_description);
		desc.add(iniValExprD);
		iniValExprD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#initialValueExpression"));

		NClassTypePropertyDescriptor factoryClassName = new NClassTypePropertyDescriptor(
				JRDesignVariable.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME, Messages.MVariable_incrementer_factory_class_name);
		factoryClassName.setDescription(Messages.MVariable_incrementer_factory_class_name_description);
		desc.add(factoryClassName);

		defaultsMap.put(JRDesignVariable.PROPERTY_CALCULATION, EnumHelper.getValue(CalculationEnum.NOTHING, 0, false));
		defaultsMap.put(JRDesignVariable.PROPERTY_RESET_TYPE, EnumHelper.getValue(ResetTypeEnum.REPORT, 1, false));
		defaultsMap.put(JRDesignVariable.PROPERTY_INCREMENT_TYPE, EnumHelper.getValue(IncrementTypeEnum.NONE, 1, false));

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#variable");
	}

	public ExpressionContext getExpressionContext() {
		JRDesignDataset dataSet = getDataSet();
		JasperReportsConfiguration conf = getJasperConfiguration();
		if (dataSet != null && conf != null)
			return new ExpressionContext(dataSet, conf);
		return null;
	}

	private RWComboBoxPropertyDescriptor resetGroupD;
	private RWComboBoxPropertyDescriptor incrementGroupD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.variable.MVariableSystem#getPropertyValue(java.lang.Object)
	 */
	@Override
	public Object getPropertyValue(Object id) {
		JRDesignVariable jrVariable = (JRDesignVariable) getValue();
		Object s = super.getPropertyValue(id);
		if (s != null)
			return s;
		if (id.equals(JRDesignVariable.PROPERTY_RESET_GROUP)) {
			if (jrVariable.getResetTypeValue().equals(ResetTypeEnum.GROUP) && resetGroupD != null) {
					if (jrVariable.getResetGroup() != null){
						return jrVariable.getResetGroup().getName();
					}
			}
			return ""; //$NON-NLS-1$
		}
		if (id.equals(JRDesignVariable.PROPERTY_INCREMENT_GROUP)) {
			if (jrVariable.getIncrementTypeValue().equals(IncrementTypeEnum.GROUP) && incrementGroupD != null) {
					if (jrVariable.getIncrementGroup() != null){
						return jrVariable.getIncrementGroup().getName();
					}
			}
			return ""; //$NON-NLS-1$
		}
		if (id.equals(JRDesignVariable.PROPERTY_CALCULATION))
			return calculationD.getEnumValue(jrVariable.getCalculationValue());
		if (id.equals(JRDesignVariable.PROPERTY_RESET_TYPE))
			return resetTypeD.getEnumValue(jrVariable.getResetTypeValue());
		if (id.equals(JRDesignVariable.PROPERTY_INCREMENT_TYPE))
			return incrementTypeD.getEnumValue(jrVariable.getIncrementTypeValue());
		if (id.equals(JRDesignVariable.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME))
			return jrVariable.getIncrementerFactoryClassName();
		if (id.equals(JRDesignVariable.PROPERTY_EXPRESSION)) {
			return ExprUtil.getExpression(jrVariable.getExpression());
		}
		if (id.equals(JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION)) {
			return ExprUtil.getExpression(jrVariable.getInitialValueExpression());
		}
		return null;
	}
	
	/**
	 * Return the list of group for the dataset parent of the variable
	 * or null if there isn't any group
	 * 
	 * @return An array of string with the name of every group inside 
	 * the variable dataset, or null if there aren't groups
	 */
	private String[] getGroupList(){
		JRDesignDataset jrDataset = getDataSet();
		JRGroup[] groups = jrDataset.getGroups();
		String[] items = null;
		if (groups != null) {
			items = new String[groups.length + 1];
			items[0] = ""; //$NON-NLS-1$
			for (int j = 0; j < groups.length; j++) {
				items[j + 1] = groups[j].getName();
			}
		}
		return items;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.variable.MVariableSystem#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	@Override
	public void setPropertyValue(Object id, Object value) {
		super.setPropertyValue(id, value);
		JRDesignVariable jrVariable = (JRDesignVariable) getValue();
		if (id.equals(JRDesignVariable.PROPERTY_RESET_GROUP)) {
			if (value != null && !value.equals("")) { //$NON-NLS-1$
				JRDesignDataset jrDataset = getDataSet();
				JRGroup group = (JRGroup) jrDataset.getGroupsMap().get(value);
				jrVariable.setResetGroup(group);
			} else jrVariable.setResetGroup(null);
		} else if (id.equals(JRDesignVariable.PROPERTY_EXPRESSION))
			jrVariable.setExpression(ExprUtil.setValues(jrVariable.getExpression(), value));
		else if (id.equals(JRDesignVariable.PROPERTY_INITIAL_VALUE_EXPRESSION))
			jrVariable.setInitialValueExpression(ExprUtil.setValues(jrVariable.getInitialValueExpression(), value));
		else if (id.equals(JRDesignVariable.PROPERTY_INCREMENT_GROUP)) {
			if (value != null && !value.equals("")) { //$NON-NLS-1$
				JRDesignDataset jrDataset = getDataSet();
				JRGroup group = (JRGroup) jrDataset.getGroupsMap().get(value);
				jrVariable.setIncrementGroup(group);
			}  else jrVariable.setIncrementGroup(null);
		} else if (id.equals(JRDesignVariable.PROPERTY_CALCULATION))
			jrVariable.setCalculation((CalculationEnum) calculationD.getEnumValue(value));
		else if (id.equals(JRDesignVariable.PROPERTY_RESET_TYPE)) {
			jrVariable.setResetType((ResetTypeEnum) resetTypeD.getEnumValue(value));
			if (!jrVariable.getResetTypeValue().equals(ResetTypeEnum.GROUP))
				jrVariable.setResetGroup(null);
		} else if (id.equals(JRDesignVariable.PROPERTY_INCREMENT_TYPE)) {
			jrVariable.setIncrementType((IncrementTypeEnum) incrementTypeD.getEnumValue(value));
			if (!jrVariable.getIncrementTypeValue().equals(IncrementTypeEnum.GROUP))
				jrVariable.setIncrementGroup(null);
		} else if (id.equals(JRDesignVariable.PROPERTY_INCREMENTER_FACTORY_CLASS_NAME)) {
			if (value == null || ((String) value).isEmpty()) {
				jrVariable.setIncrementerFactoryClassName(null);
			} else {
				jrVariable.setIncrementerFactoryClassName((String) value);
			}
		}
	}

	private static JSSEnumPropertyDescriptor calculationD;
	private static JSSEnumPropertyDescriptor resetTypeD;
	private static JSSEnumPropertyDescriptor incrementTypeD;

	protected JRDesignDataset getDataSet() {
		return ModelUtils.getDataset(this);
	}

	/**
	 * Creates the jr variable.
	 * 
	 * @param jrDataset
	 *          the jr dataset
	 * @return the jR design variable
	 */
	public static JRDesignVariable createJRVariable(JRDesignDataset jrDataset) {
		JRDesignVariable jrDesignVariable = new JRDesignVariable();
		jrDesignVariable.setSystemDefined(false);
		jrDesignVariable.setName(ModelUtils.getDefaultName(jrDataset.getVariablesMap(), "Variable_")); //$NON-NLS-1$
		return jrDesignVariable;
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MVariables)
			return true;
		return false;
	}

	@Override
	public Object getAdapter(Class adapter) {
		if (ExpressionContext.class.equals(adapter)) {
			return getExpressionContext();
		}
		return super.getAdapter(adapter);
	}
	
	@Override
	public void setValue(Object value) {
		super.setValue(value);
		setEditable(true);
	}
}
