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
package com.jaspersoft.studio.server.protocol;

import java.io.File;
import java.text.Format;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.authority.ClientUser;
import com.jaspersoft.jasperserver.dto.permissions.RepositoryPermission;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;
import com.jaspersoft.studio.server.AFinderUI;
import com.jaspersoft.studio.server.model.datasource.filter.IDatasourceFilter;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.wizard.exp.ExportOptions;
import com.jaspersoft.studio.server.wizard.imp.ImportOptions;
import com.jaspersoft.studio.server.wizard.permission.PermissionOptions;

public interface IConnection {
	public void setParent(IConnection parent);

	public Format getDateFormat();

	public Format getTimestampFormat();

	public Format getTimeFormat();

	public Format getNumberFormat();

	public boolean connect(IProgressMonitor monitor, ServerProfile sp) throws Exception;

	public ServerProfile getServerProfile();

	public ServerInfo getServerInfo(IProgressMonitor monitor) throws Exception;

	public ServerInfo getServerInfo();

	public String getWebservicesUri();

	public String getUsername();

	public String getPassword();

	public ResourceDescriptor get(IProgressMonitor monitor, ResourceDescriptor rd, File f) throws Exception;

	public List<ResourceDescriptor> list(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception;

	public ResourceDescriptor move(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception;

	public ResourceDescriptor copy(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception;

	public ResourceDescriptor addOrModifyResource(IProgressMonitor monitor, ResourceDescriptor rd, File inputFile) throws Exception;

	public ResourceDescriptor modifyReportUnitResource(IProgressMonitor monitor, ResourceDescriptor runit, ResourceDescriptor rd, File inFile) throws Exception;

	public void delete(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception;

	public void delete(IProgressMonitor monitor, ResourceDescriptor rd, ResourceDescriptor runit) throws Exception;

	public ReportExecution runReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception;

	public void cancelReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception;

	public List<ResourceDescriptor> listDatasources(IProgressMonitor monitor, IDatasourceFilter filter) throws Exception;

	public void findResources(IProgressMonitor monitor, AFinderUI callback) throws Exception;

	public ResourceDescriptor toResourceDescriptor(ClientResource<?> rest) throws Exception;

	public boolean isSupported(Feature f);

	public void reorderInputControls(String uri, List<ResourceDescriptor> rd, IProgressMonitor monitor) throws Exception;

	public ResourceDescriptor initInputControls(String uri, IProgressMonitor monitor) throws Exception;

	public List<ResourceDescriptor> cascadeInputControls(ResourceDescriptor runit, List<ResourceDescriptor> ics, IProgressMonitor monitor) throws Exception;

	public StateDto importMetaData(ImportOptions options, IProgressMonitor monitor) throws Exception;

	public StateDto exportMetaData(ExportOptions options, IProgressMonitor monitor) throws Exception;

	public Integer getPermissionMask(ResourceDescriptor rd, IProgressMonitor monitor) throws Exception;

	public List<RepositoryPermission> getPermissions(ResourceDescriptor rd, IProgressMonitor monitor, PermissionOptions options) throws Exception;

	public List<RepositoryPermission> setPermissions(ResourceDescriptor rd, List<RepositoryPermission> perms, PermissionOptions options, IProgressMonitor monitor) throws Exception;

	public ClientUser getUser(IProgressMonitor monitor) throws Exception;
}
