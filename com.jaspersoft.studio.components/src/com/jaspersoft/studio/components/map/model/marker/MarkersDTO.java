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
package com.jaspersoft.studio.components.map.model.marker;

import java.util.List;

import net.sf.jasperreports.components.map.Item;

import com.jaspersoft.studio.model.ANode;

public class MarkersDTO {
	private List<Item> marker;
	private ANode pnode;

	public MarkersDTO(List<Item> propExpressions, ANode pnode) {
		super();
		this.marker = propExpressions;
		this.pnode = pnode;
	}

	public List<Item> getMarkers() {
		return marker;
	}

	public void setMarkers(List<Item> propExpressions) {
		this.marker = propExpressions;
	}

	public ANode getPnode() {
		return pnode;
	}

	public void setPnode(ANode pnode) {
		this.pnode = pnode;
	}

}
