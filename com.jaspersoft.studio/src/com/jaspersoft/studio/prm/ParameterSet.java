/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.prm;

import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.engine.design.JRDesignParameter;

public class ParameterSet {
	public static final String PARAMETER_SETS = "com.jaspersoft.studio.parametersets";
	public static final String PARAMETER_SET = "com.jaspersoft.studio.parameterset";

	private String name;
	private List<JRDesignParameter> parameters = new ArrayList<JRDesignParameter>();
	private boolean builtIn = false;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<JRDesignParameter> getParameters() {
		return parameters;
	}

	public void setParameters(List<JRDesignParameter> parameters) {
		this.parameters = parameters;
	}

	public boolean isBuiltIn() {
		return builtIn;
	}

	public void setBuiltIn(boolean builtIn) {
		this.builtIn = builtIn;
	}
}
