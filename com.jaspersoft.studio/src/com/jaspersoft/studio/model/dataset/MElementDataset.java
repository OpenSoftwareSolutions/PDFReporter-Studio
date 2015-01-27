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
package com.jaspersoft.studio.model.dataset;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRDatasetRun;
import net.sf.jasperreports.engine.JRElementDataset;
import net.sf.jasperreports.engine.JRGroup;
import net.sf.jasperreports.engine.design.JRDesignDatasetRun;
import net.sf.jasperreports.engine.design.JRDesignElementDataset;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.IncrementTypeEnum;
import net.sf.jasperreports.engine.type.ResetTypeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.IContainer;
import com.jaspersoft.studio.model.IContainerEditPart;
import com.jaspersoft.studio.model.dataset.descriptor.DatasetRunPropertyDescriptor;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;

public class MElementDataset extends APropertyNode implements IContainer, IContainerEditPart {
	private static IIconDescriptor iconDescriptor;
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	/**
	 * Gets the icon descriptor.
	 * 
	 * @return the icon descriptor
	 */
	public static IIconDescriptor getIconDescriptor() {
		if (iconDescriptor == null)
			iconDescriptor = new NodeIconDescriptor("dataset"); //$NON-NLS-1$
		return iconDescriptor;
	}

	public ImageDescriptor getImagePath() {
		return getIconDescriptor().getIcon16();
	}

	public String getDisplayText() {
		return getIconDescriptor().getTitle();
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

	public MElementDataset(JRElementDataset value, JasperDesign jasperDesign) {
		super();
		setValue(value);
		this.jasperDesign = jasperDesign;
	}

	public MElementDataset(ANode parent, JRElementDataset value, JasperDesign jasperDesign) {
		super(parent, -1);
		setValue(value);
		this.jasperDesign = jasperDesign;
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

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		resetTypeD = new JSSEnumPropertyDescriptor(JRDesignElementDataset.PROPERTY_RESET_TYPE, Messages.common_reset_type,
				ResetTypeEnum.class, NullEnum.NOTNULL);
		resetTypeD.setDescription(Messages.MElementDataset_reset_type_description);
		desc.add(resetTypeD);

		inctypeD = new JSSEnumPropertyDescriptor(JRDesignElementDataset.PROPERTY_INCREMENT_TYPE,
				Messages.common_increment_type, IncrementTypeEnum.class, NullEnum.NOTNULL);
		inctypeD.setDescription(Messages.MElementDataset_increment_type_description);
		desc.add(inctypeD);

		JRExpressionPropertyDescriptor incWhenExprD = new JRExpressionPropertyDescriptor(
				JRDesignElementDataset.PROPERTY_INCREMENT_WHEN_EXPRESSION, Messages.MElementDataset_increment_when_expression);
		incWhenExprD.setDescription(Messages.MElementDataset_increment_when_expression_description);
		desc.add(incWhenExprD);
		incWhenExprD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#incrementWhenExpression"));

		resetGroupD = new RComboBoxPropertyDescriptor(JRDesignElementDataset.PROPERTY_RESET_GROUP,
				Messages.common_reset_group, new String[] { "" }); //$NON-NLS-1$
		resetGroupD.setDescription(Messages.MElementDataset_reset_group_description);
		desc.add(resetGroupD);

		incGroupD = new RComboBoxPropertyDescriptor(JRDesignElementDataset.PROPERTY_INCREMENT_GROUP,
				Messages.common_increment_group, new String[] { "" }); //$NON-NLS-1$
		incGroupD.setDescription(Messages.MElementDataset_increment_group_description);
		desc.add(incGroupD);

		DatasetRunPropertyDescriptor datasetRunD = new DatasetRunPropertyDescriptor(
				JRDesignElementDataset.PROPERTY_DATASET_RUN, Messages.MElementDataset_dataset_run);
		datasetRunD.setDescription(Messages.MElementDataset_dataset_run_description);
		desc.add(datasetRunD);
		datasetRunD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#datasetRun"));

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#dataset");
	}

	public void setGroupItems(String[] items) {
		if (resetGroupD != null)
			resetGroupD.setItems(items);
		if (incGroupD != null)
			incGroupD.setItems(items);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignElementDataset jrElement = (JRDesignElementDataset) getValue();
		if (id.equals(JRDesignElementDataset.PROPERTY_RESET_TYPE))
			return resetTypeD.getEnumValue(jrElement.getResetTypeValue());
		if (id.equals(JRDesignElementDataset.PROPERTY_INCREMENT_TYPE))
			return inctypeD.getEnumValue(jrElement.getIncrementTypeValue());
		if (id.equals(JRDesignElementDataset.PROPERTY_INCREMENT_WHEN_EXPRESSION))
			return ExprUtil.getExpression(jrElement.getIncrementWhenExpression());
		if (id.equals(JRDesignElementDataset.PROPERTY_RESET_GROUP)) {
			if (jrElement.getResetGroup() != null)
				return jrElement.getResetGroup().getName();
			return ""; //$NON-NLS-1$
		}
		if (id.equals(JRDesignElementDataset.PROPERTY_INCREMENT_GROUP)) {
			if (jrElement.getIncrementGroup() != null)
				return jrElement.getIncrementGroup().getName();
			return ""; //$NON-NLS-1$
		}
		if (id.equals(JRDesignElementDataset.PROPERTY_DATASET_RUN)) {
			JRDatasetRun j = jrElement.getDatasetRun();
			if (j == null) {
				j = new JRDesignDatasetRun();
			}
			if (mDatasetRun != null)
				mDatasetRun.setValue(j);
			else
				mDatasetRun = new MDatasetRun(j, getJasperDesign());
			setChildListener(mDatasetRun);
			return mDatasetRun;
		}
		return null;
	}

	private MDatasetRun mDatasetRun;
	private RComboBoxPropertyDescriptor incGroupD;
	private RComboBoxPropertyDescriptor resetGroupD;

	private JasperDesign jasperDesign;
	private static JSSEnumPropertyDescriptor resetTypeD;
	private static JSSEnumPropertyDescriptor inctypeD;

	@Override
	public JasperDesign getJasperDesign() {
		return jasperDesign;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignElementDataset jrElement = (JRDesignElementDataset) getValue();
		if (id.equals(JRDesignElementDataset.PROPERTY_INCREMENT_TYPE))
			jrElement.setIncrementType((IncrementTypeEnum) inctypeD.getEnumValue(value));
		else if (id.equals(JRDesignElementDataset.PROPERTY_RESET_TYPE))
			jrElement.setResetType((ResetTypeEnum) resetTypeD.getEnumValue(value));
		else if (id.equals(JRDesignElementDataset.PROPERTY_INCREMENT_WHEN_EXPRESSION))
			jrElement.setIncrementWhenExpression(ExprUtil.setValues(jrElement.getIncrementWhenExpression(), value));
		else if (id.equals(JRDesignElementDataset.PROPERTY_INCREMENT_GROUP)) {
			if (value != null && !value.equals("")) { //$NON-NLS-1$
				JRGroup group = (JRGroup) getJasperDesign().getGroupsMap().get(value);
				jrElement.setIncrementGroup(group);
			}
		} else if (id.equals(JRDesignElementDataset.PROPERTY_RESET_GROUP)) {
			if (value != null && !value.equals("")) { //$NON-NLS-1$
				JRGroup group = (JRGroup) getJasperDesign().getGroupsMap().get(value);
				jrElement.setResetGroup(group);
			}
		} else if (id.equals(JRDesignElementDataset.PROPERTY_DATASET_RUN)) {
			if (value == null) {
				jrElement.setDatasetRun(null);
			} else {
				MDatasetRun mdr = (MDatasetRun) value;
				JRDesignDatasetRun dr = (JRDesignDatasetRun) mdr.getValue();
				if (dr.getDatasetName() != null)
					jrElement.setDatasetRun(dr);
				else
					jrElement.setDatasetRun(null);
			}
		}
	}

}
