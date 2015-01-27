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
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.HttpResponseException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.authority.ClientUser;
import com.jaspersoft.jasperserver.dto.permissions.RepositoryPermission;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.jasperserver.dto.serverinfo.ServerInfo;
import com.jaspersoft.jasperserver.jaxrs.client.dto.importexport.StateDto;
import com.jaspersoft.studio.server.AFinderUI;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.model.datasource.filter.IDatasourceFilter;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.protocol.restv2.RestV2ConnectionJersey;
import com.jaspersoft.studio.server.wizard.exp.ExportOptions;
import com.jaspersoft.studio.server.wizard.imp.ImportOptions;
import com.jaspersoft.studio.server.wizard.permission.PermissionOptions;

public class ProxyConnection implements IConnection {
	public Format getDateFormat() {
		return c.getDateFormat();
	}

	public Format getTimestampFormat() {
		return c.getTimestampFormat();
	}

	@Override
	public Format getTimeFormat() {
		return c.getTimeFormat();
	}

	public Format getNumberFormat() {
		return c.getNumberFormat();
	}

	private IConnection[] cons = getConnections();

	private IConnection[] getConnections() {
		List<IConnection> c = new ArrayList<IConnection>();
		// c.add(new RestV2Connection());
		c.add(new RestV2ConnectionJersey());

		c.addAll(Activator.getExtManager().getProtocols());
		for (IConnection con : c)
			con.setParent(this);
		return c.toArray(new IConnection[c.size()]);
	}

	public IConnection[] getActiveConnections() {
		return cons;
	}

	private IConnection c;
	private IConnection soap;

	@Override
	public boolean connect(IProgressMonitor monitor, ServerProfile sp) throws Exception {
		Exception exc = null;
		for (IConnection co : cons) {
			String connName = co.getClass().getName().toUpperCase();
			if (sp.isUseOnlySOAP() && !connName.contains("SOAP"))
				continue;
			try {
				if (c == null && co.connect(monitor, sp))
					c = co;
				if (soap == null && connName.contains("SOAP")) {
					if (c == co)
						soap = co;
					else if (co.connect(monitor, sp))
						soap = co;
				}
				serverInfo = co.getServerInfo();
			} catch (Exception e) {
				e.printStackTrace();
				exc = e;
			}
			if (monitor.isCanceled())
				break;
		}
		if (c == null && exc != null)
			throw exc;
		return c != null;
	}

	private ServerInfo serverInfo;

	@Override
	public ServerInfo getServerInfo() {
		if (serverInfo == null)
			try {
				serverInfo = getServerInfo(new NullProgressMonitor());
			} catch (Exception e) {
				e.printStackTrace();
			}
		return serverInfo;
	}

	@Override
	public ServerInfo getServerInfo(IProgressMonitor monitor) throws Exception {
		return c != null ? c.getServerInfo(monitor) : null;
	}

	private boolean useSoap(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		String v = c.getServerInfo(monitor).getVersion();
		if (c != soap && v.compareTo("5.5") > 0 && v.compareTo("5.6") < 0 && rd.getWsType().equals(ResourceDescriptor.TYPE_REFERENCE))
			return true;
		return false;
	}

	@Override
	public ResourceDescriptor get(IProgressMonitor monitor, ResourceDescriptor rd, File f) throws Exception {
		if (useSoap(monitor, rd))
			rd = soap.get(monitor, rd, f);
		else
			try {
				rd = c.get(monitor, rd, f);
			} catch (Exception e) {
				if (e instanceof HttpResponseException) {
					HttpResponseException he = (HttpResponseException) e;
					if (he.getStatusCode() == 500 && he.getMessage().contains("Unexpected error")) {
						rd = soap.get(monitor, rd, f);
						rd.setChildrenDirty(false);
						return rd;
					} else if (he.getStatusCode() == 403) {
						rd = soap.get(monitor, rd, f);
						rd.setChildrenDirty(false);
						return rd;
					} else if (he.getStatusCode() == 401) {
						if (!error401) {
							c.connect(monitor, getServerProfile());
							error401 = true;
							return get(monitor, rd, f);
						}
					}
				}
				error401 = false;
				throw e;
			}
		if (rd != null)
			rd.setChildrenDirty(false);
		error401 = false;
		return rd;
	}

	private boolean error401 = false;

	@Override
	public List<ResourceDescriptor> list(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		List<ResourceDescriptor> list = null;
		// String v = c.getServerInfo(monitor).getVersion();
		// if (c != soap && v.compareTo("5.5") > 0 && v.compareTo("6") < 0)
		// list = soap.list(monitor, rd);
		// else
		try {
			list = c.list(monitor, rd);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 500) {// &&
																				// he.getMessage().contains("Unexpected error"))
																				// {
					list = soap.list(monitor, rd);
					for (ResourceDescriptor r : list)
						r.setChildrenDirty(false);
					return list;
				} else if (he.getStatusCode() == 401) {
					if (!error401) {
						c.connect(monitor, getServerProfile());
						error401 = true;
						return list(monitor, rd);
					}
				}
			}
			error401 = false;
			throw e;
		}
		for (ResourceDescriptor r : list)
			r.setChildrenDirty(false);
		error401 = false;
		return list;
	}

	@Override
	public List<ResourceDescriptor> listDatasources(IProgressMonitor monitor, IDatasourceFilter f) throws Exception {
		List<ResourceDescriptor> list = null;
		try {
			list = c.listDatasources(monitor, f);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 500) {// &&
																				// he.getMessage().contains("Unexpected error"))
																				// {
					list = soap.listDatasources(monitor, f);
					for (ResourceDescriptor r : list)
						r.setChildrenDirty(false);
					return list;
				} else if (he.getStatusCode() == 401) {
					if (!error401) {
						c.connect(monitor, getServerProfile());
						error401 = true;
						return listDatasources(monitor, f);
					}
				}
			}
			error401 = false;
			throw e;
		}
		for (ResourceDescriptor r : list)
			r.setChildrenDirty(false);
		error401 = false;
		return list;
	}

	@Override
	public ResourceDescriptor move(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		try {
			rd = c.move(monitor, rd, destFolderURI);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					if (!error401) {
						c.connect(monitor, getServerProfile());
						error401 = true;
						return move(monitor, rd, destFolderURI);
					}
				}
			}
			error401 = false;
			throw e;
		}
		rd.setChildrenDirty(false);
		return rd;
	}

	@Override
	public ResourceDescriptor copy(IProgressMonitor monitor, ResourceDescriptor rd, String destFolderURI) throws Exception {
		try {
			rd = c.copy(monitor, rd, destFolderURI);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					if (!error401) {
						c.connect(monitor, getServerProfile());
						error401 = true;
						return c.copy(monitor, rd, destFolderURI);
					}
				}
			}
			error401 = false;
			throw e;
		}
		rd.setChildrenDirty(false);
		return rd;
	}

	@Override
	public ResourceDescriptor addOrModifyResource(IProgressMonitor monitor, ResourceDescriptor rd, File inputFile) throws Exception {
		try {
			rd = c.addOrModifyResource(monitor, rd, inputFile);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.addOrModifyResource(monitor, rd, inputFile);
				}
			}
			error401 = false;
			throw e;
		}
		rd.setChildrenDirty(false);
		return rd;
	}

	@Override
	public ResourceDescriptor modifyReportUnitResource(IProgressMonitor monitor, ResourceDescriptor runit, ResourceDescriptor rd, File inFile) throws Exception {
		try {
			rd = c.modifyReportUnitResource(monitor, runit, rd, inFile);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.modifyReportUnitResource(monitor, runit, rd, inFile);
				}
			}
			error401 = false;
			throw e;
		}
		rd.setChildrenDirty(false);
		return rd;
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd) throws Exception {
		try {
			c.delete(monitor, rd);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					c.delete(monitor, rd);
					return;
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public void delete(IProgressMonitor monitor, ResourceDescriptor rd, ResourceDescriptor runit) throws Exception {
		try {
			c.delete(monitor, rd, runit);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					c.delete(monitor, rd, runit);
					return;
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public ReportExecution runReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
		try {
			return c.runReport(monitor, repExec);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.runReport(monitor, repExec);
				}
				repExec.setStatus("failed");
				return repExec;
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public void cancelReport(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
		try {
			c.cancelReport(monitor, repExec);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					c.cancelReport(monitor, repExec);
					return;
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public String getWebservicesUri() {
		return c.getWebservicesUri();
	}

	@Override
	public String getUsername() {
		return c.getUsername();
	}

	@Override
	public String getPassword() {
		return c.getPassword();
	}

	@Override
	public void findResources(IProgressMonitor monitor, AFinderUI callback) throws Exception {
		try {
			c.findResources(monitor, callback);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					c.findResources(monitor, callback);
					return;
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public ResourceDescriptor toResourceDescriptor(ClientResource<?> rest) throws Exception {
		return c.toResourceDescriptor(rest);
	}

	@Override
	public boolean isSupported(Feature f) {
		return c.isSupported(f);
	}

	@Override
	public void reorderInputControls(String uri, List<ResourceDescriptor> rd, IProgressMonitor monitor) throws Exception {
		try {
			c.reorderInputControls(uri, rd, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					c.reorderInputControls(uri, rd, monitor);
					return;
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public ServerProfile getServerProfile() {
		return c.getServerProfile();
	}

	@Override
	public ResourceDescriptor initInputControls(String uri, IProgressMonitor monitor) throws Exception {
		try {
			return c.initInputControls(uri, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.initInputControls(uri, monitor);
				}
			}
			error401 = false;
			throw e;
		}
		// ResourceDescriptor rd = new ResourceDescriptor();
		// rd.setUriString(WSClientHelper.getReportUnitUri(uri));
		// rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
		// return rd;
	}

	@Override
	public List<ResourceDescriptor> cascadeInputControls(ResourceDescriptor runit, List<ResourceDescriptor> ics, IProgressMonitor monitor) throws Exception {
		try {
			return c.cascadeInputControls(runit, ics, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.cascadeInputControls(runit, ics, monitor);
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public void setParent(IConnection parent) {
	}

	@Override
	public StateDto importMetaData(ImportOptions options, IProgressMonitor monitor) throws Exception {
		try {
			return c.importMetaData(options, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.importMetaData(options, monitor);
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public StateDto exportMetaData(ExportOptions options, IProgressMonitor monitor) throws Exception {
		try {
			return c.exportMetaData(options, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.exportMetaData(options, monitor);
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public Integer getPermissionMask(ResourceDescriptor rd, IProgressMonitor monitor) throws Exception {
		try {
			return c.getPermissionMask(rd, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.getPermissionMask(rd, monitor);
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public List<RepositoryPermission> getPermissions(ResourceDescriptor rd, IProgressMonitor monitor, PermissionOptions options) throws Exception {
		try {
			return c.getPermissions(rd, monitor, options);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.getPermissions(rd, monitor, options);
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public ClientUser getUser(IProgressMonitor monitor) throws Exception {
		try {
			return c.getUser(monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.getUser(monitor);
				}
			}
			error401 = false;
			throw e;
		}
	}

	@Override
	public List<RepositoryPermission> setPermissions(ResourceDescriptor rd, List<RepositoryPermission> perms, PermissionOptions options, IProgressMonitor monitor) throws Exception {
		try {
			return c.setPermissions(rd, perms, options, monitor);
		} catch (Exception e) {
			if (e instanceof HttpResponseException) {
				HttpResponseException he = (HttpResponseException) e;
				if (he.getStatusCode() == 401) {
					c.connect(monitor, getServerProfile());
					error401 = true;
					return c.setPermissions(rd, perms, options, monitor);
				}
			}
			error401 = false;
			throw e;
		}
	}

}
