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
package com.jaspersoft.studio.property.descriptor.propexpr;

import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertyExpression;

import com.jaspersoft.studio.model.ANode;

public class PropertyExpressionsDTO {
	private JRPropertyExpression[] propExpressions;
	private JRPropertiesMap propMap;
	private ANode pnode;

	public PropertyExpressionsDTO(JRPropertyExpression[] propExpressions, JRPropertiesMap propMap, ANode pnode) {
		super();
		this.propExpressions = propExpressions;
		this.propMap = propMap;
		this.pnode = pnode;
	}

	public JRPropertyExpression[] getPropExpressions() {
		return propExpressions;
	}

	public void setPropExpressions(JRPropertyExpression[] propExpressions) {
		this.propExpressions = propExpressions;
	}

	public JRPropertiesMap getPropMap() {
		return propMap;
	}

	public void setPropMap(JRPropertiesMap propMap) {
		this.propMap = propMap;
	}

	public ANode getPnode() {
		return pnode;
	}

	public void setPnode(ANode pnode) {
		this.pnode = pnode;
	}

}
