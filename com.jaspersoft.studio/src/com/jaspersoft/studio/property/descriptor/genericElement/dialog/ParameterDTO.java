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
package com.jaspersoft.studio.property.descriptor.genericElement.dialog;

import net.sf.jasperreports.engine.JRGenericElementParameter;
import net.sf.jasperreports.engine.design.JasperDesign;
/*
 * @author Chicu Veaceslav
 * 
 */
public class ParameterDTO {
	public ParameterDTO() {
		super();
	}

	public ParameterDTO(String property, String description) {
		super();
		this.property = property;
		this.description = description;
	}

	private String property;
	private String description;
	private JRGenericElementParameter[] value;
	private JasperDesign jd;

	public void setJasperDesign(JasperDesign jd) {
		this.jd = jd;
	}

	public JasperDesign getJasperDesign() {
		return jd;
	}

	public JRGenericElementParameter[] getValue() {
		return value;
	}

	public void setValue(JRGenericElementParameter[] value) {
		this.value = value;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
