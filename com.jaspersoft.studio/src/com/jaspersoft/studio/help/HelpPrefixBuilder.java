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
package com.jaspersoft.studio.help;

import org.eclipse.ui.views.properties.IPropertyDescriptor;

public class HelpPrefixBuilder implements IHelpRefBuilder {

	private String helpref;

	public HelpPrefixBuilder(String prefix, IPropertyDescriptor descriptor) {
		helpref = prefix + "_" + descriptor.getId();
	}

	@Override
	public String getHelpReference() {
		return helpref;
	}

}
