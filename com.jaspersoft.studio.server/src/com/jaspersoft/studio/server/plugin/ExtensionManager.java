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
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.wizard.IWizardPage;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.model.AMJrxmlContainer;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.restv2.ARestV2Connection;
import com.jaspersoft.studio.server.protocol.restv2.WsTypes;

public class ExtensionManager {
	private List<IResourceFactory> resources = new ArrayList<IResourceFactory>();
	private List<IConnection> protocols = new ArrayList<IConnection>();
	private List<IPublishContributor> publisher = new ArrayList<IPublishContributor>();

	public void init() {
		IConfigurationElement[] config = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio.server", "resources"); //$NON-NLS-1$ //$NON-NLS-2$
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IResourceFactory)
					resources.add((IResourceFactory) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}

		config = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio.server", "publisher"); //$NON-NLS-1$ //$NON-NLS-2$
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IPublishContributor)
					publisher.add((IPublishContributor) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}

		config = Platform.getExtensionRegistry().getConfigurationElementsFor("com.jaspersoft.studio.server", "protocols"); //$NON-NLS-1$ //$NON-NLS-2$
		for (IConfigurationElement e : config) {
			try {
				Object o = e.createExecutableExtension("ClassFactory"); //$NON-NLS-1$
				if (o instanceof IConnection)
					protocols.add((IConnection) o);
			} catch (CoreException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public void publishJrxml(AMJrxmlContainer mrunit, IProgressMonitor monitor, JasperDesign jasper, Set<String> fileset, IFile file, String version) throws Exception {
		for (IPublishContributor r : publisher)
			r.publishJrxml(mrunit, monitor, jasper, fileset, file, version);
	}

	public void publishParameters(MReportUnit mrunit, IProgressMonitor monitor, JasperDesign jasper) throws Exception {
		for (IPublishContributor r : publisher)
			r.publishParameters(mrunit, monitor, jasper);
	}

	public MResource getResource(ANode parent, ResourceDescriptor resource, int index) {
		for (IResourceFactory r : resources) {
			MResource mr = r.getResource(parent, resource, index);
			if (mr != null)
				return mr;
		}
		return null;
	}

	public IWizardPage[] getResourcePage(ANode parent, MResource resource) {
		for (IResourceFactory r : resources) {
			IWizardPage[] mr = r.getResourcePage(parent, resource);
			if (mr != null)
				return mr;
		}
		return null;
	}

	public List<IConnection> getProtocols() {
		List<IConnection> cons = new ArrayList<IConnection>();
		for (IConnection p : protocols) {
			try {
				cons.add(p.getClass().newInstance());
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return cons;
	}

	public ANode createNewResource(ANode root, ANode parent) {
		for (IResourceFactory r : resources)
			r.createNewResource(root, parent);
		return null;
	}

	public ANode createNewDatasource(ANode root, ANode parent) {
		for (IResourceFactory r : resources)
			r.createNewDatasource(root, parent);
		return null;
	}

	public void initWsTypes(WsTypes wsType) {
		for (IResourceFactory r : resources)
			r.initWsTypes(wsType);
	}

	public void initContainers(Set<Class<? extends ClientResource<?>>> containers) {
		for (IResourceFactory r : resources)
			r.initContainers(containers);
	}

	public ResourceDescriptor getRD(ARestV2Connection rc, ClientResource<?> cr, ResourceDescriptor rd) throws ParseException {
		for (IResourceFactory r : resources) {
			ResourceDescriptor nrd = r.getRD(rc, cr, rd);
			if (nrd != null)
				return nrd;
		}
		return null;
	}

	public ClientResource<?> getResource(ARestV2Connection rc, ClientResource<?> cr, ResourceDescriptor rd) throws ParseException {
		for (IResourceFactory r : resources) {
			ClientResource<?> nrd = r.getResource(rc, cr, rd);
			if (nrd != null)
				return nrd;
		}
		return null;
	}
}
