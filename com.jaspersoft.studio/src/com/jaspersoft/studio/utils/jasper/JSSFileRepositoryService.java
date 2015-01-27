/*******************************************************************************
 * Copyright (C) 2005 - 2014 TIBCO Software Inc. All rights reserved. http://www.jaspersoft.com.
 * 
 * Unless you have purchased a commercial license agreement from Jaspersoft, the following license terms apply:
 * 
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at http://www.eclipse.org/legal/epl-v10.html
 ******************************************************************************/
package com.jaspersoft.studio.utils.jasper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.engine.JRRuntimeException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.repo.DefaultRepositoryService;
import net.sf.jasperreports.repo.FileRepositoryService;
import net.sf.jasperreports.repo.InputStreamResource;
import net.sf.jasperreports.repo.OutputStreamResource;
import net.sf.jasperreports.repo.ReportResource;
import net.sf.jasperreports.repo.RepositoryService;
import net.sf.jasperreports.repo.Resource;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.jaspersoft.studio.messages.Messages;
import com.jaspersoft.studio.utils.SubreportsUtil;

public class JSSFileRepositoryService implements RepositoryService {
	private List<RepositoryService> list;
	private JasperReportsConfiguration jConfig;

	public JSSFileRepositoryService(JasperReportsConfiguration jConfig, List<RepositoryService> list) {
		this.list = list;
		this.jConfig = jConfig;
	}

	public List<RepositoryService> getRepositoryServices() {
		return list;
	}

	@Override
	public Resource getResource(String uri) {
		for (RepositoryService rs : list) {
			Resource r = rs.getResource(uri);
			if (r != null)
				return r;
		}
		return null;
	}

	@Override
	public void saveResource(String uri, Resource resource) {
		for (RepositoryService rs : list)
			rs.saveResource(uri, resource);
	}

	@Override
	public <K extends Resource> K getResource(String uri, Class<K> resourceType) {
		for (RepositoryService rs : new ArrayList<RepositoryService>(list)) {
			K r = doGetResource(uri, resourceType, rs);
			if (r != null)
				return r;
		}
		return null;
	}

	public <K extends Resource> K doGetResource(String uri, Class<K> resourceType, RepositoryService rs) {
		try {
			K r = rs.getResource(uri, resourceType);
			if (r != null)
				return r;
		} catch (JRRuntimeException e) {
		}
		try {
			if (ReportResource.class.equals(resourceType) && uri.endsWith(FileExtension.PointJRXML)) {
				return doGetResource(uri.replaceAll(FileExtension.PointJRXML + "$", FileExtension.PointJASPER), resourceType,
						rs);
			} else if (ReportResource.class.equals(resourceType) && uri.endsWith(FileExtension.PointJASPER)) {
				String nuri = uri.replaceAll(FileExtension.PointJASPER + "$", FileExtension.PointJRXML);
				InputStreamResource inr = rs.getResource(nuri, InputStreamResource.class);
				if (inr == null)
					return null;
				if (rs instanceof DefaultRepositoryService) {
					URI dUri = new URI(uri);
					JasperCompileManager.getInstance(jConfig).compileToFile(new URI(nuri).getRawPath(), dUri.getRawPath());
				} else {
					OutputStreamResource or = new OutputStreamResource();
					if (rs instanceof FileRepositoryService)
						or.setOutputStream(((FileRepositoryService) rs).getOutputStream(uri));
					else
						or.setOutputStream(new ByteArrayOutputStream());
					JasperCompileManager.getInstance(jConfig).compileToStream(inr.getInputStream(), or.getOutputStream());
					rs.saveResource(uri, or);
				}
				refreshFile(rs, uri);
				return rs.getResource(uri, resourceType);
			}
		} catch (Throwable e1) {
			e1.printStackTrace();
		}
		return null;
	}

	private void refreshFile(final RepositoryService rs, final String uri) {
		Job job = new Job(Messages.CompileAction_jobName) {
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					if (rs instanceof DefaultRepositoryService) {
						IFile[] fs = SubreportsUtil.root.findFilesForLocationURI(new URI(uri));
						if (fs != null && fs.length > 0)
							fs[0].refreshLocal(1, monitor);
					} else if (rs instanceof FileRepositoryService) {
						IFile[] fs = SubreportsUtil.root.findFilesForLocationURI(new File(((FileRepositoryService) rs).getRoot(),
								uri).toURI());
						if (fs != null && fs.length > 0)
							fs[0].refreshLocal(1, monitor);
					}
				} catch (URISyntaxException e) {
					return Status.CANCEL_STATUS;
				} catch (CoreException e) {
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.SHORT);
		job.schedule();

	}
}
