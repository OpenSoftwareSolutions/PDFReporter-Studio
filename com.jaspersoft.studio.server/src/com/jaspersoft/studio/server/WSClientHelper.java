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
package com.jaspersoft.studio.server;

import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import net.sf.jasperreports.eclipse.util.FileUtils;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.Argument;
import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.jasperserver.dto.resources.ClientResource;
import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.model.util.ModelUtil;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.IInputControlsContainer;
import com.jaspersoft.studio.server.model.MFolder;
import com.jaspersoft.studio.server.model.MRDataAdapter;
import com.jaspersoft.studio.server.model.MRDataAdapterFile;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.datasource.filter.IDatasourceFilter;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.server.protocol.Feature;
import com.jaspersoft.studio.server.protocol.IConnection;
import com.jaspersoft.studio.server.protocol.ProxyConnection;
import com.jaspersoft.studio.server.protocol.ReportExecution;
import com.jaspersoft.studio.server.wizard.resource.page.selector.SelectorDatasource;

public class WSClientHelper {
	private static Map<IConnection, ServerProfile> clients = new HashMap<IConnection, ServerProfile>();

	public static ServerProfile getServerProfile(IConnection cli) {
		return clients.get(cli);
	}

	public static IConnection connect(MServerProfile msp, IProgressMonitor monitor) throws Exception {
		if (monitor != null)
			monitor.subTask("Connecting to " + msp.getDisplayText());
		IConnection c = new ProxyConnection();
		boolean cres = c.connect(monitor, msp.getValue());
		if (cres) {
			if (monitor != null)
				monitor.subTask("Connected");
			msp.setWsClient(c);
			clients.put(c, msp.getValue());
			return c;
		} else if (monitor != null)
			monitor.subTask("Not Connected");
		return null;
	}

	public static boolean checkConnection(MServerProfile msp, IProgressMonitor monitor) throws Exception {
		monitor.subTask("Connecting to " + msp.getDisplayText());
		IConnection c = new ProxyConnection();
		boolean cres = c.connect(monitor, msp.getValue());
		if (cres) {
			monitor.subTask("Connected");
			msp.setWsClient(c);
			monitor.subTask("Trying to read folder");
			ResourceDescriptor rd = new ResourceDescriptor();
			rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
			rd.setUriString("/");
			c.get(monitor, rd, null);
		} else
			monitor.subTask("Not Connected");
		return cres;
	}

	public static void connectGetData(MServerProfile msp, IProgressMonitor monitor) throws Exception {
		msp.removeChildren();
		WSClientHelper.listFolder(msp, connect(msp, monitor), "/", monitor, 0);
	}

	/**
	 * This function shows how to create a folder in the root directory.
	 * Subfolders can be created just specifying a proper Uri string i.e.
	 * rd.setUriString("/this/is/my/new/folder");
	 * 
	 * @param client
	 * @param folderLabel
	 * @param folderName
	 * @throws IOException
	 */
	public static List<ResourceDescriptor> listFolder(ANode parent, IConnection client, String folderUri, IProgressMonitor monitor, int depth) throws Exception {
		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
		rd.setUriString(folderUri);
		if (depth < 1) {
			parent.removeChildren();
			return listFolder(parent, -1, client, monitor, rd, depth);
		}
		return null;
	}

	private static List<ResourceDescriptor> listFolder(ANode parent, int index, IConnection client, IProgressMonitor monitor, ResourceDescriptor rd, int depth) throws Exception {
		monitor.subTask("Listing " + rd.getUriString());
		depth++;

		List<ResourceDescriptor> children = client.list(monitor, rd);
		if (parent instanceof MServerProfile || (parent instanceof MResource && ((MResource) parent).getValue().getWsType().equals(ResourceDescriptor.TYPE_FOLDER))) {
			Collections.sort(children, new Comparator<ResourceDescriptor>() {

				@Override
				public int compare(ResourceDescriptor arg0, ResourceDescriptor arg1) {
					if (arg0.getLabel() == arg1.getLabel())
						return 0;
					if (arg0.getLabel() == null)
						return -1;
					if (arg1.getLabel() == null)
						return 1;
					String wsType0 = arg0.getWsType();
					String wsType1 = arg1.getWsType();
					if (wsType0.equals(wsType1))
						return arg0.getLabel().compareTo(arg1.getLabel());
					if (wsType0.equals(ResourceDescriptor.TYPE_FOLDER))
						return -1;
					if (wsType1.equals(ResourceDescriptor.TYPE_FOLDER))
						return 1;
					if (wsType0.equals(ResourceDescriptor.TYPE_REPORTUNIT))
						return -1;
					if (wsType1.equals(ResourceDescriptor.TYPE_REPORTUNIT))
						return 1;
					if (wsType0.equals(ResourceDescriptor.TYPE_DOMAIN_TOPICS))
						return -1;
					if (wsType1.equals(ResourceDescriptor.TYPE_DOMAIN_TOPICS))
						return 1;
					return wsType0.compareTo(wsType1);
				}
			});
		}

		Set<String> set = new HashSet<String>();
		for (ResourceDescriptor r : children) {
			if (set.contains(r.getUriString()))
				continue;
			set.add(r.getUriString());
			if (rd.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT) || rd.getWsType().equals(ResourceDescriptor.TYPE_DOMAIN_TOPICS)) {
				if (SelectorDatasource.isDatasource(r))
					continue;
			}
			MResource node = ResourceFactory.getResource(parent, r, index);
			if (depth <= 0) {
				if (r.getWsType().equals(ResourceDescriptor.TYPE_FOLDER)) {
					listFolder(node, client, r.getUriString(), monitor, depth);
				} else if (r.getWsType().equals(ResourceDescriptor.TYPE_REPORTUNIT) || rd.getWsType().equals(ResourceDescriptor.TYPE_DOMAIN_TOPICS)) {
					r = client.get(monitor, r, null);
					Set<String> setRU = new HashSet<String>();
					List<ResourceDescriptor> children2 = r.getChildren();
					for (ResourceDescriptor res : children2) {
						if (setRU.contains(res.getUriString()))
							continue;
						setRU.add(res.getUriString());
						if (SelectorDatasource.isDatasource(res))
							continue;
						if (res.getWsType().equals(ResourceDescriptor.TYPE_FOLDER))
							listFolder(node, client, res.getUriString(), monitor, depth);
						else
							ResourceFactory.getResource(node, res, index);
						if (monitor.isCanceled())
							return children;
					}
				}
			}
			if (monitor.isCanceled())
				return children;
		}
		return children;
	}

	public static ResourceDescriptor getResource(IProgressMonitor monitor, ANode res, ResourceDescriptor rd) throws Exception {
		if (res instanceof AFileResource)
			return getResource(monitor, res, rd, ((AFileResource) res).getFile());
		return getResource(monitor, res, rd, (String) null);
	}

	public static ResourceDescriptor getResource(IProgressMonitor monitor, ANode res, ResourceDescriptor rd, String file) throws Exception {
		File f = null;
		if (file != null)
			f = new File(file);
		return getResource(monitor, res, rd, f);
	}

	public static ResourceDescriptor getResource(IProgressMonitor monitor, ANode res, ResourceDescriptor rd, File f) throws Exception {
		MServerProfile sp = (MServerProfile) res.getRoot();
		return sp.getWsClient(monitor).get(monitor, rd, f);
	}

	public static ResourceDescriptor getResource(IProgressMonitor monitor, IConnection cl, ResourceDescriptor rd, File f) throws Exception {
		return cl.get(monitor, rd, f);
	}

	public static ResourceDescriptor getReference(IProgressMonitor monitor, ANode root, ResourceDescriptor rd) throws Exception {
		MServerProfile sp = (MServerProfile) root.getRoot();
		if (rd.getReferenceUri() != null) {
			String ref = rd.getReferenceUri();
			int ldel = ref.lastIndexOf("/");
			String pfolder = ref.substring(0, ldel - 1);
			String file = ref.substring(ldel + 1, ref.length());

			ResourceDescriptor r = new ResourceDescriptor();
			r.setParentFolder(pfolder);
			r.setName(file);
			r.setUriString(rd.getReferenceUri());
			r.setWsType(ResourceDescriptor.TYPE_CONTENT_RESOURCE);
			return sp.getWsClient(monitor).get(monitor, r, null);
		}
		return null;
	}

	public static void saveResource(MResource res, IProgressMonitor monitor) throws Exception {
		saveResource(res, monitor, true);
	}

	public static ResourceDescriptor saveResource(MResource res, IProgressMonitor monitor, boolean refresh) throws Exception {
		INode n = res.getRoot();
		ResourceDescriptor rd = null;
		if (n != null && n instanceof MServerProfile) {
			MServerProfile sp = (MServerProfile) n;
			rd = res.getValue();
			if (rd.getIsNew()) {
				rd.setUriString(getParentFolder(rd) + rd.getName());
			}
			File file = null;
			if (res instanceof AFileResource) {
				file = ((AFileResource) res).getFile();
				if (file != null) {
					rd.setData(Base64.encodeBase64(FileUtils.getBytes(file)));
					rd.setHasData(true);
				} else
					rd.setHasData(false);
			} else
				rd.setHasData(false);

			MReportUnit mru = res.getReportUnit();
			IConnection cli = sp.getWsClient(monitor);
			if (cli == null)
				cli = connect(sp, monitor);
			System.out.println("saving: " + rd.getUriString() + " parent:" + rd.getParentFolder());
			if (mru != null && res != mru) {
				String wsType = rd.getWsType();
				if (wsType.equals(ResourceDescriptor.TYPE_INPUT_CONTROL) && !rd.getIsNew())
					rd = cli.addOrModifyResource(monitor, rd, file);
				else if (res instanceof MRDataAdapterFile || res instanceof MRDataAdapter)
					rd = cli.addOrModifyResource(monitor, rd, file);
				else {
					if (wsType.equals(ResourceDescriptor.TYPE_JRXML) && !rd.getIsNew() && rd.getName().equals("main_jrxml"))
						rd.setMainReport(true);
					// String turi = rd.getUriString();
					ResourceDescriptor trd = cli.modifyReportUnitResource(monitor, mru.getValue(), rd, file);
					// if (!trd.getUriString().equals(turi))
					// rd = getResource(cli, rd, null);
					rd = trd;
				}
			} else
				rd = cli.addOrModifyResource(monitor, rd, file);
			if (refresh && res.getParent() instanceof MResource) {
				// standard resource creation inside an existing MResource
				refreshContainer((MResource) res.getParent(), monitor);
			} else if (res.getParent() instanceof MServerProfile) {
				// resource created inside the root folder
				connectGetData((MServerProfile) res.getParent(), monitor);
				fireResourceChanged(res);
			}
		}
		return rd;
	}

	public static ResourceDescriptor save(IProgressMonitor monitor, MResource f) throws Exception {
		try {
			return WSClientHelper.saveResource(f, monitor, false);
		} catch (Exception e) {
			if (f.getValue().getIsNew()) {
				ResourceDescriptor rd = f.getValue();
				MServerProfile sp = (MServerProfile) f.getRoot();
				MReportUnit n = f.getReportUnit();
				IConnection wsClient = sp.getWsClient(monitor);
				if (f.getParent() instanceof MFolder && !(f instanceof MReportUnit)) {
					ResourceDescriptor prd = ((MResource) f.getParent()).getValue();
					ResourceDescriptor v = f.getValue();
					v.setParentFolder(getParentFolder(prd) + prd.getName());//$NON-NLS-1$    
					v.setUriString(getParentFolder(v) + f.getValue().getName());//$NON-NLS-1$
				} else if (!(f instanceof MReportUnit || f instanceof MRDataAdapter || f instanceof MRDataAdapterFile)) {
					ResourceDescriptor prd = ((MResource) f.getParent()).getValue();
					ResourceDescriptor v = f.getValue();
					v.setParentFolder(prd.getParentFolder() + "/" + prd.getName() + "_files");//$NON-NLS-1$    
					v.setUriString(v.getParentFolder() + "/" + f.getValue().getName());//$NON-NLS-1$
				}
				try {
					if (n != null && !(f instanceof MReportUnit))
						wsClient.delete(monitor, rd, ((MReportUnit) n).getValue());
					else
						wsClient.delete(monitor, rd);
				} catch (Exception e1) {
					// e.printStackTrace();
				}
				try {
					return WSClientHelper.saveResource(f, monitor, false);
				} catch (Exception e1) {
					f.getValue().setIsNew(false);
					return WSClientHelper.saveResource(f, monitor, false);
				}
			}
			throw e;
		}
	}

	private static String getParentFolder(ResourceDescriptor rd) {
		String pfd = rd.getParentFolder();
		if (!pfd.equals("/"))
			pfd += "/";
		return pfd;
	}

	public static void deleteResource(IProgressMonitor monitor, MResource res) throws Exception {
		ResourceDescriptor rd = res.getValue();
		MServerProfile sp = (MServerProfile) res.getRoot();
		if (!rd.getIsNew()) {
			MReportUnit n = res.getReportUnit();
			IConnection wsClient = sp.getWsClient(monitor);
			if (n != null && !(res instanceof MReportUnit))
				wsClient.delete(monitor, rd, ((MReportUnit) n).getValue());
			else
				wsClient.delete(monitor, rd);
		}
		((ANode) res.getParent()).removeChild(res);
	}

	public static void refreshResource(final MResource res, IProgressMonitor monitor) throws Exception {
		ResourceDescriptor rd = res.getValue();
		INode n = res.getRoot();
		if (n != null && n instanceof MServerProfile) {
			ResourceDescriptor newrd = res.getWsClient().get(monitor, rd, null);
			if (newrd != null) {
				res.setValue(newrd);
				if (res instanceof MFolder || res instanceof MReportUnit || (res.isSupported(Feature.INPUTCONTROLS_ORDERING) && (res instanceof IInputControlsContainer))) {
					res.removeChildren();

					listFolder(res, -1, res.getWsClient(), monitor, newrd, 0);
				}
			} else
				connectGetData((MServerProfile) res.getRoot(), monitor);
			fireResourceChanged(res);
		} else {
			// posible problem?
		}
	}

	public static void refreshContainer(MResource res, IProgressMonitor monitor) throws Exception {
		if (res instanceof MFolder || res instanceof MReportUnit || (res.isSupported(Feature.INPUTCONTROLS_ORDERING) && (res instanceof IInputControlsContainer))) {
			res.removeChildren();

			listFolder(res, -1, res.getWsClient(), monitor, res.getValue(), 0);
			fireResourceChanged(res);
		}
	}

	public static void fireResourceChanged(final MResource res) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				ServerManager.getPropertyChangeSupport().firePropertyChange(new PropertyChangeEvent(res, "MODEL", null, res));
			}
		});
	}

	//
	// public static ReportExecution runReportUnit(IProgressMonitor monitor,
	// MReportUnit res) throws Exception {
	// ResourceDescriptor rd = res.getValue();
	// MServerProfile sp = (MServerProfile) res.getRoot();
	//
	// ReportExecution repExec = new ReportExecution();
	//
	// Map<String, Object> parameters = new HashMap<String, Object>();
	//
	// List<Argument> args = new ArrayList<Argument>();
	// args.add(new Argument(Argument.RUN_OUTPUT_FORMAT,
	// Argument.RUN_OUTPUT_FORMAT_JRPRINT));
	//
	// return sp.getWsClient(monitor).runReport(monitor, rd, parameters, args);
	// }

	public static ReportExecution runReportUnit(IProgressMonitor monitor, ReportExecution repExec, Map<String, Object> parameters) throws Exception {
		if (repExec.getResourceDescriptor() == null) {
			ResourceDescriptor rd = new ResourceDescriptor();
			rd.setUriString(repExec.getReportURI());
			rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);
			repExec.setResourceDescriptor(rd);
		}
		repExec.setPrm(parameters);
		if (repExec.getArgs() == null) {
			List<Argument> args = new ArrayList<Argument>();
			args.add(new Argument(Argument.RUN_OUTPUT_FORMAT, Argument.RUN_OUTPUT_FORMAT_JRPRINT));
			repExec.setArgs(args);
		}
		return getClient(monitor, repExec.getReportURIFull()).runReport(monitor, repExec);
	}

	public static void cancelReportUnit(IProgressMonitor monitor, ReportExecution repExec) throws Exception {
		getClient(monitor, repExec.getReportURIFull()).cancelReport(monitor, repExec);
	}

	public static ResourceDescriptor getReportUnit(IProgressMonitor monitor, String uri) throws Exception {
		ResourceDescriptor rd = new ResourceDescriptor();
		rd.setUriString(getReportUnitUri(uri));
		rd.setWsType(ResourceDescriptor.TYPE_REPORTUNIT);

		return getClient(monitor, uri).get(monitor, rd, null);
	}

	public static String getReportUnitUri(String uri) {
		StringTokenizer st = new StringTokenizer(uri, ":");
		String name = st.nextToken();
		return st.nextToken();

		// return uri.substring(uri.indexOf(":") + 1);
	}

	public static IConnection getClient(IProgressMonitor monitor, String uri) throws Exception {
		MServerProfile sp = ServerManager.getServerProfile(uri);
		return sp.getWsClient(monitor);
	}

	public static MResource findSelected(IProgressMonitor monitor, ResourceDescriptor rd, MServerProfile msp) throws Exception {
		IConnection c = msp.getWsClient(monitor);
		if (ModelUtil.isEmpty(msp))
			listFolder(msp, c, "/", monitor, 0);
		return findSelected(msp.getChildren(), monitor, rd.getUriString(), c);
	}

	public static MResource findSelected(List<INode> list, IProgressMonitor monitor, String prunit, IConnection cli) throws Exception {
		if (monitor.isCanceled())
			return null;
		int maxl = 0;
		int pos = -1;
		for (int i = 0; i < list.size(); i++) {
			if (!(list.get(i) instanceof MResource))
				continue;
			MResource mr = (MResource) list.get(i);
			String uri = mr.getValue().getUriString();
			if (prunit.equals(uri)) {
				ANode p = mr.getParent();
				int ind = p.getChildren().indexOf(mr);
				p.removeChild(mr);
				return ResourceFactory.getResource(p, cli.get(monitor, mr.getValue(), null), ind);
			}
			if (prunit.startsWith(uri) && prunit.length() >= uri.length()) {
				if (maxl < uri.length()) {
					maxl = uri.length();
					pos = i;
				}
			}
		}
		if (pos >= 0) {
			MResource mr = (MResource) list.get(pos);
			String uri = mr.getValue().getUriString();
			listFolder(mr, cli, uri, monitor, 0);
			return findSelected(mr.getChildren(), monitor, prunit, cli);
		}
		return null;
	}

	public static List<ResourceDescriptor> getDatasourceList(IProgressMonitor monitor, IConnection c, IDatasourceFilter f) throws Exception {
		return c.listDatasources(monitor, f);
	}

	public static MServerProfile getDatasourceListTree(IProgressMonitor monitor, MServerProfile sp, IDatasourceFilter f) throws Exception {
		List<ResourceDescriptor> list = getDatasourceList(monitor, sp.getWsClient(monitor), f);
		sp.removeChildren();
		for (ResourceDescriptor r : list)
			addDataSource(sp, r);
		return sp;
	}

	private static void addDataSource(MServerProfile sp, ResourceDescriptor r) {
		String url = r.getUriString();
		StringTokenizer st = new StringTokenizer(url, "/");
		String turl = "/";
		ANode parent = sp;
		while (st.hasMoreTokens()) {
			String token = st.nextToken();
			String sf = turl + token;
			if (sf.equals(url)) {
				ResourceFactory.getResource(parent, r, -1);
				break;
			}
			MResource child = null;
			for (INode node : parent.getChildren()) {
				if (node instanceof MResource) {
					MResource mr = (MResource) node;
					if (mr.getValue().getUriString().equals(sf)) {
						child = mr;
						break;
					}
				}
			}
			if (child == null) {
				ResourceDescriptor rd = new ResourceDescriptor();
				rd.setName(token);
				rd.setLabel(token);
				rd.setUriString(sf);
				rd.setWsType(ResourceDescriptor.TYPE_FOLDER);
				child = ResourceFactory.getResource(parent, rd, -1);
				child.removeChildren();
			}
			parent = child;
			turl = sf + "/";
		}

	}

	/**
	 * Returns a valid {@link IConnection} associated to the specified
	 * {@link MResource} instance.
	 * <p>
	 * 
	 * Usually the resource root element is an {@link MServerProfile} instance
	 * that holds a valid connection.
	 * 
	 * @param resource
	 *          the remote resource
	 * @return a valid {@link IConnection} if any associated, <code>null</code>
	 *         otherwise
	 * @throws Exception
	 */
	public static IConnection getClient(IProgressMonitor monitor, MResource resource) throws Exception {
		INode root = resource.getRoot();
		if (root instanceof MServerProfile)
			return ((MServerProfile) root).getWsClient(monitor);
		return null;
	}

	public static final String _FILES = "_files/";

	public static void findResources(IProgressMonitor monitor, AFinderUI callback) throws Exception {
		try {
			IConnection c = callback.getServerProfile().getWsClient(monitor);
			c.findResources(monitor, callback);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ResourceDescriptor toResourceDescriptor(MServerProfile sp, ClientResource<?> rest) throws Exception {
		return sp.getWsClient((IProgressMonitor) null).toResourceDescriptor(rest);
	}
}
