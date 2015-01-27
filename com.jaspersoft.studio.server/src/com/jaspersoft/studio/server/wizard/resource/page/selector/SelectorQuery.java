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
package com.jaspersoft.studio.server.wizard.resource.page.selector;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.MRQuery;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.utils.Misc;

public class SelectorQuery extends ASelector {

	@Override
	protected ResourceDescriptor createLocal(MResource res) {
		ResourceDescriptor rd = MRQuery.createDescriptor(res);
		rd.setName(Messages.SelectorQuery_0);
		rd.setLabel(rd.getName());
		return rd;
	}

	@Override
	protected boolean isResCompatible(MResource r) {
		return r instanceof MRQuery;
	}

	private static ResourceDescriptor getQuery(ResourceDescriptor ru) {
		for (Object obj : ru.getChildren()) {
			ResourceDescriptor r = (ResourceDescriptor) obj;
			String t = r.getWsType();
			if (t.equals(ResourceDescriptor.TYPE_QUERY))
				return r;
		}
		return null;
	}

	@Override
	protected ResourceDescriptor getResourceDescriptor(ResourceDescriptor ru) {
		return getQuery(ru);
	}

	@Override
	public boolean isPageComplete() {
		boolean b = super.isPageComplete();
		if (b) {
			ResourceDescriptor rd = res.getValue();
			String[] qvc = rd.getQueryVisibleColumns();
			b = qvc != null && qvc.length > 0 && !Misc.isNullOrEmpty(rd.getQueryValueColumn());
		}
		return b;
	}

	@Override
	protected String[] getIncludeTypes() {
		return new String[] { ResourceMediaType.QUERY_CLIENT_TYPE };
	}

	@Override
	protected String[] getExcludeTypes() {
		return null;
	}

}
