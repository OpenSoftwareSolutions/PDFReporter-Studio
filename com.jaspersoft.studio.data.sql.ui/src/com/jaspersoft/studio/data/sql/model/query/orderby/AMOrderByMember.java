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
package com.jaspersoft.studio.data.sql.model.query.orderby;

import net.sf.jasperreports.engine.JRConstants;

import com.jaspersoft.studio.data.sql.model.query.AMKeyword;
import com.jaspersoft.studio.data.sql.model.query.AMQueryObject;
import com.jaspersoft.studio.model.ANode;

public class AMOrderByMember<T> extends AMQueryObject<T> {
	public static final long serialVersionUID = JRConstants.SERIAL_VERSION_UID;

	public AMOrderByMember(ANode parent, T value, String image, int index) {
		super(parent, value, image, index);
	}

	public AMOrderByMember(ANode parent, T value, String image) {
		super(parent, value, image);
	}

	public String addDirection() {
		return isDesc ? AMKeyword.DESCENDING_KEYWORD : AMKeyword.ASCENDING_KEYWORD;
	}

	private boolean isDesc = true;

	public boolean isDesc() {
		return isDesc;
	}

	public void setDesc(boolean isDesc) {
		this.isDesc = isDesc;
	}

}
