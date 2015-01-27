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
package com.jaspersoft.studio.model;

import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.JRConstants;
import net.sf.jasperreports.engine.JRQuery;
import net.sf.jasperreports.engine.design.JRDesignQuery;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.model.dataset.MDataset;
import com.jaspersoft.studio.property.descriptor.NullEnum;
import com.jaspersoft.studio.property.descriptor.combo.RWComboBoxPropertyDescriptor;
import com.jaspersoft.studio.property.descriptor.text.NTextPropertyDescriptor;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.ModelUtils;

public class MQuery extends APropertyNode implements IPropertySource {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	private MDataset mdataset;

	public MQuery(JRQuery jrQuery, MDataset mdataset) {
		super();
		this.mdataset = mdataset;
		setValue(jrQuery);
	}

	public MDataset getMdataset() {
		return mdataset;
	}

	@Override
	public JRDesignQuery getValue() {
		return (JRDesignQuery) super.getValue();
	}

	@Override
	protected void postDescriptors(IPropertyDescriptor[] descriptors) {
		super.postDescriptors(descriptors);
		if (languageD != null)
			languageD.setItems(ModelUtils.getQueryLanguages(getJasperConfiguration()));
	}

	@Override
	public void createPropertyDescriptors(List<IPropertyDescriptor> desc, Map<String, Object> defaultsMap) {
		// pen
		NTextPropertyDescriptor textD = new NTextPropertyDescriptor(JRDesignQuery.PROPERTY_TEXT, Messages.common_text);
		textD.setDescription(Messages.MQuery_text_description);
		desc.add(textD);

		languageD = new RWComboBoxPropertyDescriptor(JRDesignQuery.PROPERTY_LANGUAGE, Messages.common_language,
				ModelUtils.getQueryLanguages(getJasperConfiguration()), NullEnum.NOTNULL);
		languageD.setDescription(Messages.MQuery_language_description);
		languageD.setCategory(Messages.common_report);
		desc.add(languageD);

		defaultsMap.put(JRDesignQuery.PROPERTY_LANGUAGE, "SQL"); //$NON-NLS-1$
	}

	private static IPropertyDescriptor[] descriptors;
	private static Map<String, Object> defaultsMap;

	private RWComboBoxPropertyDescriptor languageD;

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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#getPropertyValue(java.lang.Object)
	 */
	public Object getPropertyValue(Object id) {
		JRQuery jrQuery = (JRQuery) getValue();
		if (jrQuery != null) {
			if (id.equals(JRDesignQuery.PROPERTY_TEXT))
				return jrQuery.getText();
			if (id.equals(JRDesignQuery.PROPERTY_LANGUAGE))
				return jrQuery.getLanguage();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.views.properties.IPropertySource#setPropertyValue(java.lang.Object, java.lang.Object)
	 */
	public void setPropertyValue(Object id, Object value) {
		JRDesignQuery jrQuery = (JRDesignQuery) getValue();
		if (jrQuery != null) {
			if (id.equals(JRDesignQuery.PROPERTY_TEXT)) {
				jrQuery.setText((String) value);
			} else if (id.equals(JRDesignQuery.PROPERTY_LANGUAGE)) {
				String lang = Misc.nullValue((String) value);
				jrQuery.setLanguage(ModelUtils.getLanguage(lang));
			}
		}
	}

	public String getDisplayText() {
		return null;
	}

	public ImageDescriptor getImagePath() {
		return null;
	}

}
