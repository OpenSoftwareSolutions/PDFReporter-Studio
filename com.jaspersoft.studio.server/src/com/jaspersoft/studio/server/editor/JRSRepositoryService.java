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
package com.jaspersoft.studio.server.editor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.repo.FileRepositoryService;
import net.sf.jasperreports.repo.RepositoryService;
import net.sf.jasperreports.repo.Resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.ResourceFactory;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.messages.Messages;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.utils.ReferenceResolver;
import com.jaspersoft.studio.utils.CacheMap;
import com.jaspersoft.studio.utils.Callback;
import com.jaspersoft.studio.utils.jasper.JSSFileRepositoryService;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JRSRepositoryService implements RepositoryService {
	private JSSFileRepositoryService parent;
	private MServerProfile msp;
	private IConnection c;
	private String rpath;
	private String serverUri;
	private JasperDesign jDesign;
	private String runitUri;
	private JasperReportsConfiguration jConfig;
	private FileRepositoryService repService;

	public JRSRepositoryService(JSSFileRepositoryService parent, JasperReportsConfiguration jConfig) {
		this.parent = parent;
		this.jConfig = jConfig;
	}

	public FileRepositoryService getFileRepositoryService() {
		return repService;
	}

	private boolean hasServerUrl() {
		if (jDesign == null)
			this.jDesign = jConfig.getJasperDesign();
		String uri = jDesign.getProperty(AExporter.PROP_SERVERURL);
		if (uri == null)
			return false;
		String serverUser = jDesign.getProperty(AExporter.PROP_USER);
		runitUri = jDesign.getProperty(AExporter.PROP_REPORTUNIT);
		if (!uri.equals(serverUri)) {
			serverUri = uri;
			c = null;
		}
		if (c == null && !isConnecting) {
			isConnecting = true;
			msp = ServerManager.getServerByUrl(serverUri, serverUser);
			setupConnection(msp.getWsClient(new Callback<IConnection>() {

				@Override
				public void completed(IConnection value) {
					setupConnection(value);
				}
			}));
		}
		return true;
	}

	private void setupConnection(IConnection conn) {
		c = conn;
		try {
			rpath = msp.getTmpDir(new NullProgressMonitor()).getRawLocation().toOSString();
			List<RepositoryService> servs = parent.getRepositoryServices();
			if (repService != null)
				servs.remove(repService);
			repService = new FileRepositoryService(jConfig, rpath, true);
			int ind = servs.indexOf(JRSRepositoryService.this);
			servs.add(Math.max(0, Math.max(ind - 2, ind - 1)), repService);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			isConnecting = false;
		}
	}

	private boolean isConnecting = false;

	@Override
	public Resource getResource(String uri) {
		return null;
	}

	@Override
	public void saveResource(String uri, Resource resource) {
	}

	private List<ResourceDescriptor> reportUnitResources = null;
	private CacheMap<String, String> negCache = new CacheMap<String, String>(1000);

	@Override
	public synchronized <K extends Resource> K getResource(String uri, Class<K> resourceType) {
		if (hasServerUrl() && c != null) {
			if (uri.startsWith("repo:")) {
				// it's possible to have a resource with id=repo:something (from
				// practice)
				K r = doGetResource("repo:" + uri, resourceType);
				if (r != null)
					return r;
			}
			return doGetResource(uri, resourceType);
		}
		return null;
	}

	protected <K extends Resource> K doGetResource(String uri, Class<K> resourceType) {
		if (negCache.containsKey(uri))
			return null;
		negCache.put(uri, null);

		String objectUri = uri;
		if (uri.startsWith("repo:")) { //$NON-NLS-1$ 
			objectUri = uri.substring(5);
			K r = getFromParent(objectUri, resourceType);
			if (r != null)
				return r;
		}
		try {
			IProgressMonitor monitor = new NullProgressMonitor();
			if (objectUri.contains("/")) { //$NON-NLS-1$
				// Locate the resource inside the repository...
				ResourceDescriptor r = new ResourceDescriptor();
				r.setUriString(objectUri);
				r = c.get(monitor, r, null);
				if (r.getIsReference())
					r = ReferenceResolver.resolveReference(c, r, null);
				String fpath = rpath;
				if (!objectUri.startsWith("/")) //$NON-NLS-1$
					fpath += "/"; //$NON-NLS-1$
				fpath += objectUri;
				File f = new File(fpath);
				if (f.createNewFile())
					c.get(monitor, r, f);
			} else if (runitUri != null) {
				// Locate the resource inside the report unit, if any...
				if (reportUnitResources == null) {
					ResourceDescriptor rd = new ResourceDescriptor();
					rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
					rd.setUriString(runitUri);
					rd = c.get(monitor, rd, null);
					reportUnitResources = c.list(monitor, rd);
					if (reportUnitResources == null)
						reportUnitResources = new ArrayList<ResourceDescriptor>();
				}

				// find the resource...
				for (ResourceDescriptor r : reportUnitResources) {
					if (r.getName() == null || !r.getName().equals(objectUri))
						continue;
					if (r.getIsReference())
						r = ReferenceResolver.resolveReference(c, r, monitor);
					if (ResourceFactory.isFileResourceType(r)) {
						IFile file = (IFile) jConfig.get(FileUtils.KEY_FILE);
						File f = new File(file.getParent().getRawLocation().toFile(), objectUri);
						if (f.createNewFile())
							c.get(monitor, r, f);
						break;
					}
				}
			}
			refresh();
			return getFromParent(uri, resourceType);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	protected <K extends Resource> K getFromParent(String uri, Class<K> resourceType) {
		for (RepositoryService rs : parent.getRepositoryServices()) {
			if (rs == this)
				continue;
			try {
				K r = parent.doGetResource(uri, resourceType, rs);
				if (r != null)
					return r;
			} catch (JRRuntimeException e) {
			}
		}
		return null;
	}

	private boolean isRefreshing = false;
	private boolean needNewRefresh = false;

	private void refresh() {
		needNewRefresh = true;
		if (isRefreshing)
			return;
		isRefreshing = true;
		Job job = new Job(Messages.JRSRepositoryService_4) {
			protected IStatus run(IProgressMonitor monitor) {
				needNewRefresh = false;
				try {
					msp.getTmpDir(monitor).refreshLocal(IResource.DEPTH_INFINITE, monitor);
				} catch (Exception e) {
					// e.printStackTrace();
				} finally {
					isRefreshing = false;
					UIUtils.getDisplay().asyncExec(new Runnable() {

						@Override
						public void run() {
							if (needNewRefresh)
								refresh();
						}
					});
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.setSystem(true);
		job.schedule(2000);
	}
}
