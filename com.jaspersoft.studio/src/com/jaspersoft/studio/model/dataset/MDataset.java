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

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.design.JRDesignDataset;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.type.WhenResourceMissingTypeEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import com.jaspersoft.studio.help.HelpReferenceBuilder;
import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.APropertyNode;
import com.jaspersoft.studio.model.ICopyable;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.MQuery;
import com.jaspersoft.studio.model.MReport;
import com.jaspersoft.studio.model.field.MField;
import com.jaspersoft.studio.model.field.MFields;
import com.jaspersoft.studio.model.parameter.MParameterSystem;
import com.jaspersoft.studio.model.parameter.MParameters;
import com.jaspersoft.studio.model.util.IIconDescriptor;
import com.jaspersoft.studio.model.util.NodeIconDescriptor;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.classname.NClassTypePropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.expression.ExprUtil;
import com.jaspersoft.studio.property.descriptor.expression.JRExpressionPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.jrQuery.JRQueryButtonPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.properties.JPropertiesPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.resource.ResourceBundlePropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSEnumPropertyDescriptor;
import com.jaspersoft.studio.property.descriptors.JSSValidatedTextPropertyDescriptor;
import com.jaspersoft.studio.utils.EnumHelper;
import com.jaspersoft.studio.utils.ModelUtils;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/*
 * The Class MDataset.
 * 
 * @author Chicu Veaceslav
 */
public class MDataset extends APropertyNode implements ICopyable {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;
	public static final String PROPERTY_MAP = "PROPERTY_MAP"; //$NON-NLS-1$
	/** The icon descriptor. */
	private static IIconDescriptor iconDescriptor;

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

	/**
	 * Instantiates a new m dataset.
	 */
	public MDataset() {
		super();
	}

	private MReport mreport;

	public MDataset(MReport mreport, JRDesignDataset jrDataset) {
		super();
		this.mreport = mreport;
		setValue(jrDataset);
	}

	public boolean isMainDataset() {
		if (getJasperDesign() != null && getJasperDesign().getMainDataset() == getValue())
			return true;
		return false;
	}

	/**
	 * Instantiates a new m dataset.
	 * 
	 * @param parent
	 *          the parent
	 * @param jrDataset
	 *          the jr dataset
	 * @param newIndex
	 *          the new index
	 */
	public MDataset(ANode parent, JRDesignDataset jrDataset, int newIndex) {
		super(parent, newIndex);
		setValue(jrDataset);
		INode root = getRoot();
		if (root != null && root instanceof MReport)
			mreport = (MReport) root;
	}

	public MReport getMreport() {
		return mreport;
	}

	@Override
	public JasperDesign getJasperDesign() {
		JasperDesign jd = super.getJasperDesign();
		if (jd == null) {
			MReport mrep = getMreport();
			if (mrep != null)
				mrep.getJasperDesign();
		}
		return jd;
	}

	@Override
	public JRDesignDataset getValue() {
		return (JRDesignDataset) super.getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jaspersoft.studio.model.INode#getDisplayText()
	 */
	public String getDisplayText() {
		if (getValue() != null)
			return ((JRDesignDataset) getValue()).getName();
		return ""; //$NON-NLS-1$
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
		return getIconDescriptor().getToolTip();
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;
	private static DatasetNameValidator validator;

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
		// Set into the validator the actual reference
		validator.setTargetNode(this);
	}

	/**
	 * Creates the property descriptors.
	 * 
	 * @param desc
	 *          the desc
	 */
	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		validator = new DatasetNameValidator();
		validator.setTargetNode(this);
		JSSValidatedTextPropertyDescriptor nameD = new JSSValidatedTextPropertyDescriptor(JRDesignDataset.PROPERTY_NAME,
				Messages.common_name, validator);
		nameD.setDescription(Messages.MDataset_name_description);
		desc.add(nameD);

		JPropertiesPropertyDescriptor propertiesD = new JPropertiesPropertyDescriptor(PROPERTY_MAP,
				Messages.common_properties);
		propertiesD.setDescription(Messages.MDataset_properties_description);
		desc.add(propertiesD);
		propertiesD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#property"));

		NClassTypePropertyDescriptor classD = new NClassTypePropertyDescriptor(JRDesignDataset.PROPERTY_SCRIPTLET_CLASS,
				Messages.MDataset_scriplet_class);
		classD.setDescription(Messages.MDataset_class_description);
		desc.add(classD);
		classD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#scriptlet"));

		ResourceBundlePropertyDescriptor resBundleD = new ResourceBundlePropertyDescriptor(
				JRDesignDataset.PROPERTY_RESOURCE_BUNDLE, Messages.MDataset_resource_bundle);
		resBundleD.setDescription(Messages.MDataset_resource_bundle_description);
		desc.add(resBundleD);

		JRQueryButtonPropertyDescriptor queryD = new JRQueryButtonPropertyDescriptor(JRDesignDataset.PROPERTY_QUERY,
				Messages.common_query, NullEnum.NULL, Messages.MDataset_Edit_Query_Button_Text);
		queryD.setDescription(Messages.MDataset_query_description);
		desc.add(queryD);
		queryD.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#queryString"));

		whenResMissTypeD = new JSSEnumPropertyDescriptor(JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE,
				Messages.MDataset_when_resource_missing_type, WhenResourceMissingTypeEnum.class, NullEnum.NOTNULL);
		whenResMissTypeD.setDescription(Messages.MDataset_when_resource_missing_type_description);
		desc.add(whenResMissTypeD);

		JRExpressionPropertyDescriptor filterExpression = new JRExpressionPropertyDescriptor(
				JRDesignDataset.PROPERTY_FILTER_EXPRESSION, Messages.MDataset_filter_expression);
		filterExpression.setDescription(Messages.MDataset_filter_expression_description);
		desc.add(filterExpression);
		filterExpression.setHelpRefBuilder(new HelpReferenceBuilder(
				"net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#filterExpression"));

		defaultsMap.put(JRDesignDataset.PROPERTY_RESOURCE_BUNDLE, null);
		defaultsMap.put(JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE,
				EnumHelper.getValue(WhenResourceMissingTypeEnum.NULL, 1, false));
		defaultsMap.put(JRDesignDataset.PROPERTY_FILTER_EXPRESSION, null);

		setHelpPrefix(desc, "net.sf.jasperreports.doc/docs/schema.reference.html?cp=0_1#subDataset");
	}

	private MQuery mQuery;
	private static JSSEnumPropertyDescriptor whenResMissTypeD;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRDesignDataset jrDataset = (JRDesignDataset) getValue();
		if (jrDataset == null)
			return null;
		if (id.equals(JRDesignDataset.PROPERTY_NAME))
			return jrDataset.getName();

		if (id.equals(JRDesignDataset.PROPERTY_QUERY)) {
			if (mQuery == null) {
				JRQuery jdq = jrDataset.getQuery();
				mQuery = new MQuery(jdq, this);
				mQuery.setJasperConfiguration(getJasperConfiguration());
				setChildListener(mQuery);
			}
			return mQuery;
		}
		if (id.equals(JRDesignDataset.PROPERTY_FILTER_EXPRESSION)) {
			return ExprUtil.getExpression(jrDataset.getFilterExpression());
		}
		if (id.equals(JRDesignDataset.PROPERTY_SCRIPTLET_CLASS))
			return jrDataset.getScriptletClass();
		if (id.equals(PROPERTY_MAP)) {
			// to avoid duplication I remove it first
			JRPropertiesMap pmap = jrDataset.getPropertiesMap();
			return pmap;
		}
		if (id.equals(JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE))
			return whenResMissTypeD.getEnumValue(jrDataset.getWhenResourceMissingTypeValue());
		if (id.equals(JRDesignDataset.PROPERTY_RESOURCE_BUNDLE))
			return jrDataset.getResourceBundle();

		return null;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (JRDesignDataset.PROPERTY_NAME.equals(evt.getPropertyName())) {
			// When the name is changed, the one inside the jasperdesign is updated also
			JasperDesign design = getJasperDesign();
			JRDesignDataset jrDataset = (JRDesignDataset) getValue();
			String oldName = (String) evt.getOldValue();
			if (design != null) {
				design.getDatasetMap().remove(oldName);
				design.getDatasetMap().put(jrDataset.getName(), jrDataset);
			}
		}
		super.propertyChange(evt);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignDataset jrDataset = (JRDesignDataset) getValue();
		if (id.equals(JRDesignDataset.PROPERTY_NAME)) {
			jrDataset.setName((String) value);
		} else if (id.equals(JRDesignDataset.PROPERTY_RESOURCE_BUNDLE)) {
			String v = (String) value;
			if (v != null && v.trim().isEmpty())
				v = null;
			jrDataset.setResourceBundle(v);
		} else if (id.equals(JRDesignDataset.PROPERTY_SCRIPTLET_CLASS)) {
			String v = (String) value;
			if (v != null && v.trim().isEmpty()) {
				v = null;
			}
			jrDataset.setScriptletClass(v);
		} else if (id.equals(JRDesignDataset.PROPERTY_FILTER_EXPRESSION))
			jrDataset.setFilterExpression(ExprUtil.setValues(jrDataset.getFilterExpression(), value));
		else if (id.equals(PROPERTY_MAP)) {
			JRPropertiesMap v = (JRPropertiesMap) value;
			String[] names = jrDataset.getPropertiesMap().getPropertyNames();
			for (int i = 0; i < names.length; i++) {
				jrDataset.getPropertiesMap().removeProperty(names[i]);
			}
			names = v.getPropertyNames();
			for (int i = 0; i < names.length; i++)
				jrDataset.setProperty(names[i], v.getProperty(names[i]));
			this.getPropertyChangeSupport().firePropertyChange(PROPERTY_MAP, false, true);
		} else if (id.equals(JRDesignDataset.PROPERTY_WHEN_RESOURCE_MISSING_TYPE))
			jrDataset.setWhenResourceMissingType((WhenResourceMissingTypeEnum) whenResMissTypeD.getEnumValue(value));
		else if (id.equals(JRDesignDataset.PROPERTY_QUERY)) {
			if (value instanceof MQuery) {
				unsetChildListener(mQuery);
				mQuery = (MQuery) value;
				setChildListener(mQuery);
				JRDesignQuery jrQuery = (JRDesignQuery) mQuery.getValue();
				jrDataset.setQuery(jrQuery);
			}
		}
	}

	@Override
	public String toString() {
		return getDisplayText();
	}

	/**
	 * Creates the jr dataset.
	 * 
	 * @param jrDesign
	 *          the jr design
	 * @return the jR design dataset
	 */
	public static JRDesignDataset createJRDataset(JasperReportsConfiguration jConfig, JasperDesign jrDesign) {
		JRDesignDataset jrDesignDataset = new JRDesignDataset(jConfig, false);
		jrDesignDataset.setName(ModelUtils.getDefaultName(jrDesign.getDatasetMap(), "Dataset")); //$NON-NLS-1$
		JRDesignQuery jrDesignQuery = new JRDesignQuery();
		jrDesignQuery.setLanguage("sql"); //$NON-NLS-1$
		jrDesignQuery.setText(""); //$NON-NLS-1$
		jrDesignDataset.setQuery(jrDesignQuery);
		return jrDesignDataset;
	}

	public boolean isCopyable2(Object parent) {
		if (parent instanceof MReport)
			return true;
		return false;
	}

	public MParameterSystem getParamater(String name) {
		List<INode> children = getChildren();
		if (children == null || children.isEmpty())
			children = mreport.getChildren();
		for (INode n : children) {
			if (n instanceof MParameters) {
				for (INode nf : n.getChildren()) {
					MParameterSystem mfield = (MParameterSystem) nf;
					if (mfield.getValue().getName().equals(name))
						return mfield;
				}
				break;
			}
		}
		return null;
	}

	public MField getField(String name) {
		List<INode> children = getChildren();
		if (children == null || children.isEmpty())
			children = mreport.getChildren();
		for (INode n : children) {
			if (n instanceof MFields) {
				for (INode nf : n.getChildren()) {
					MField mfield = (MField) nf;
					if (mfield.getValue().getName().equals(name))
						return mfield;
				}
				break;
			}
		}
		return null;
	}
}
