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
package com.jaspersoft.studio.server.properties.action;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jface.action.Action;

import com.jaspersoft.studio.server.properties.ASection;

public abstract class AAction extends Action {
	protected Set<ASection> sections = new HashSet<ASection>();

	public AAction(String name) {
		super(name);
	}

	public void addSection(ASection section) {
		sections.add(section);
	}

	public void removeSection(ASection section) {
		sections.remove(section);
	}
}
