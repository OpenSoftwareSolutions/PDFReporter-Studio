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

import net.sf.jasperreports.eclipse.ui.util.UIUtils;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.wizard.WizardDialog;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ResourceMediaType;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.MROlapMondrianConnection;
import com.jaspersoft.studio.server.wizard.resource.AddResourceWizard;

public class SelectorMondrianConnection extends ASelector {

	@Override
	protected MResource getLocalResource(MResource res, ResourceDescriptor runit, ANode pnode) {
		AddResourceWizard wizard = new AddResourceWizard(res, true);
		wizard.setMondrianOnly(true);
		WizardDialog dialog = new WizardDialog(UIUtils.getShell(), wizard);
		dialog.create();
		if (dialog.open() != Dialog.OK)
			return null;
		MResource r = wizard.getResource();
		ResourceDescriptor ref = r.getValue();
		ref.setIsNew(true);
		ref.setIsReference(false);
		ref.setParentFolder(runit.getParentFolder() + "/" + runit.getName() + "_files"); //$NON-NLS-1$
		ref.setDirty(true);
		return r;
	}

	@Override
	protected ResourceDescriptor createLocal(MResource res) {
		return null;
	}

	@Override
	protected boolean isResCompatible(MResource r) {
		return r instanceof MROlapMondrianConnection;
	}

	protected ResourceDescriptor getResourceDescriptor(ResourceDescriptor ru) {
		for (Object obj : ru.getChildren()) {
			ResourceDescriptor r = (ResourceDescriptor) obj;
			if (r.getWsType().equals(ResourceDescriptor.TYPE_SECURE_MONDRIAN_CONNECTION) || r.getWsType().equals(ResourceDescriptor.TYPE_OLAP_MONDRIAN_CONNECTION))
				return r;
		}
		return null;
	}

	@Override
	protected String[] getIncludeTypes() {
		return new String[] { ResourceMediaType.MONDRIAN_CONNECTION_CLIENT_TYPE, ResourceMediaType.SECURE_MONDRIAN_CONNECTION_CLIENT_TYPE };
	}

	@Override
	protected String[] getExcludeTypes() {
		return null;
	}

}
