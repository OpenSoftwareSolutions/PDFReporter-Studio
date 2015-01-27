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
package com.jaspersoft.studio.server.plugin;

import java.text.ParseException;
import java.util.Set;

import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.restv2.ARestV2Connection;
import com.jaspersoft.studio.server.protocol.restv2.WsTypes;

public interface IResourceFactory {
	public MResource getResource(ANode parent, ResourceDescriptor resource, int index);

	public IWizardPage[] getResourcePage(ANode parent, MResource resource);

	public ANode createNewResource(ANode root, ANode parent);

	public ANode createNewDatasource(ANode root, ANode parent);

	public void initWsTypes(WsTypes wsType);

	public ResourceDescriptor getRD(ARestV2Connection rc, ClientResource<?> cr, ResourceDescriptor rd) throws ParseException;

	public ClientResource<?> getResource(ARestV2Connection rc, ClientResource<?> cr, ResourceDescriptor rd) throws ParseException;

	public void initContainers(Set<Class<? extends ClientResource<?>>> containers);
}
