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
package com.jaspersoft.studio.server.publish;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import com.jaspersoft.studio.model.ANode;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.ServerManager;
import com.jaspersoft.studio.server.WSClientHelper;
import com.jaspersoft.studio.server.export.AExporter;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.AMJrxmlContainer;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.utils.Misc;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class FindResources {

	public static boolean find(IProgressMonitor monitor, AMJrxmlContainer mres, JasperDesign jd) throws Exception {
		List<?> r = findResources(monitor, mres, jd);
		return !Misc.isNullOrEmpty(r);
	}

	public static List<?> findResources(IProgressMonitor monitor, AMJrxmlContainer mres, JasperDesign jd) throws Exception {
		JasperReportsConfiguration jrConfig = mres.getJasperConfiguration();
		jrConfig.put(PublishUtil.KEY_PUBLISH2JSS_DATA, new ArrayList<AFileResource>());

		String version = ServerManager.getVersion(mres);
		HashSet<String> fileset = new HashSet<String>();
		IFile file = (IFile) jrConfig.get(FileUtils.KEY_FILE);

		mres.removeChildren();

		new JrxmlPublishContributor().publishJrxml(mres, monitor, jd, fileset, file, version);

		Object r = jrConfig.get(PublishUtil.KEY_PUBLISH2JSS_DATA);
		if (r != null && r instanceof List)
			return (List<?>) r;
		return null;
	}

	public static ANode findReportUnit(MServerProfile mserv, IProgressMonitor monitor, JasperDesign jd, IFile file) {
		try {
			if (mserv != null) {
				String prunit = jd.getProperty(AExporter.PROP_REPORTUNIT);
				if (prunit == null)
					prunit = jd.getProperty(AExporter.PROP_REPORTRESOURCE);
				if (prunit == null)
					prunit = file.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, AExporter.PROP_REPORTRESOURCE));

				String srvURL = jd.getProperty(AExporter.PROP_SERVERURL);
				if (srvURL == null)
					srvURL = file.getPersistentProperty(new QualifiedName(Activator.PLUGIN_ID, AExporter.PROP_SERVERURL));
				// String srvUSER = jd.getProperty(AExporter.PROP_USER);
				// if (srvUSER == null)
				// srvUSER = file.getPersistentProperty(new
				// QualifiedName(Activator.PLUGIN_ID, AExporter.PROP_USER));

				if (prunit != null && srvURL != null && mserv.getValue().getUrl().equals(srvURL)) {
					WSClientHelper.connect(mserv, monitor);
					WSClientHelper.connectGetData(mserv, monitor);
					// We can try to locate a previous existing Report Unit.
					// If not possible we will popup the selection tree as
					// usual.
					MResource selectedRepoUnit = WSClientHelper.findSelected(mserv.getChildren(), monitor, prunit, mserv.getWsClient(monitor));
					if (selectedRepoUnit != null)
						return selectedRepoUnit;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mserv;
	}

}
