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
package com.jaspersoft.studio.server.wizard.resource.page;

import org.eclipse.core.databinding.DataBindingContext;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.wizard.resource.page.selector.ATextFileResourcePageContent;

public class XmlPageContent extends ATextFileResourcePageContent {

	public XmlPageContent(ANode parent, MResource resource, DataBindingContext bindingContext) {
		super(parent, resource, bindingContext);
	}

	private String title;

	public XmlPageContent(ANode parent, MResource resource) {
		super(parent, resource);
	}

	public XmlPageContent(ANode parent, MResource resource, String title) {
		this(parent, resource);
		this.title = title;
	}

	@Override
	public String getPageName() {
		return "com.jaspersoft.studio.server.page.xml";
	}

	@Override
	public String getName() {
		if (title != null)
			return title;
		return Messages.RDXmlFile_title;
	}

	@Override
	protected String[] getFilter() {
		return new String[] { "*.*", "*.xml" }; //$NON-NLS-1$
	}

}
