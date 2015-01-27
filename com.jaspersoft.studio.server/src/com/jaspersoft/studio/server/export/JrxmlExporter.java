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
package com.jaspersoft.studio.server.export;

import java.io.ByteArrayInputStream;

import net.sf.jasperreports.engine.JRExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.QualifiedName;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.model.INode;
import com.jaspersoft.studio.server.Activator;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MJar;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResource;
import com.jaspersoft.studio.server.model.server.MServerProfile;
import com.jaspersoft.studio.server.model.server.ServerProfile;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class JrxmlExporter extends AExporter {
	public static final String PROP_REPORT_ISMAIN = "ireport.jasperserver.report.ismain";
	public static final QualifiedName KEY_REPORT_ISMAIN = new QualifiedName(Activator.PLUGIN_ID, PROP_REPORT_ISMAIN);

	public JrxmlExporter(IPath path) {
		super(path);
	}

	@Override
	public IFile exportToIFile(AFileResource res, ResourceDescriptor rd, String fkeyname, IProgressMonitor monitor) throws Exception {
		IFile f = super.exportToIFile(res, rd, fkeyname, monitor);
		if (f != null) {
			JasperReportsConfiguration jrConfig = res.getJasperConfiguration();
			if (jrConfig == null) {
				jrConfig = JasperReportsConfiguration.getDefaultJRConfig(f);
				res.setJasperConfiguration(jrConfig);
			} else
				jrConfig.init(f);
			try {
				JasperDesign jd = JRXmlLoader.load(jrConfig, f.getContents());
				setPropServerURL(res, jd);
				setPropReportUnit(res, jd);
				getResources(res, jd);

				MServerProfile sp = (MServerProfile) res.getRoot();
				if (sp != null)
					f.setContents(new ByteArrayInputStream(JRXmlWriterHelper.writeReport(null, jd, sp.getValue().getJrVersion()).getBytes("UTF-8")), IFile.KEEP_HISTORY | IFile.FORCE, monitor);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (f != null)
			f.setPersistentProperty(KEY_REPORT_ISMAIN, Boolean.toString(rd.isMainReport()));
		return f;
	}

	protected void setPropServerURL(AFileResource res, JasperDesign jd) {
		INode n = res.getRoot();
		if (n != null && n instanceof MServerProfile) {
			MServerProfile server = (MServerProfile) n;
			ServerProfile v = server.getValue();
			jd.setProperty(AExporter.PROP_SERVERURL, v.getUrl());
			jd.setProperty(AExporter.PROP_USER, v.getUser() + (v.getOrganisation() != null ? "|" + v.getOrganisation() : ""));
		}
	}

	protected void setPropReportUnit(MResource res, JasperDesign jd) {
		if (!res.getValue().isMainReport())
			jd.setProperty(AExporter.PROP_REPORTRESOURCE, res.getValue().getUriString());
		MReportUnit repunit = res.getReportUnit();
		if (repunit != null) {
			ResourceDescriptor runit = repunit.getValue();
			if (runit != null)
				jd.setProperty(AExporter.PROP_REPORTUNIT, runit.getUriString());
		} else
			jd.getPropertiesMap().removeProperty(AExporter.PROP_REPORTUNIT);
	}

	private void getResources(MResource res, JasperDesign jd) throws Exception {
		if (res.getParent() instanceof MReportUnit) {
			for (INode n : res.getParent().getChildren()) {
				if (n instanceof MJar) {
					// download
				}
			}
		}
		// List<JRDesignElement> elements = ModelUtils.getAllElements(jd);
		// for (JRDesignElement ele : elements) {
		// if (ele instanceof JRDesignImage)
		// cacheResource(res, ((JRDesignImage) ele).getExpression());
		// else if (ele instanceof JRDesignSubreport) {
		// cacheResource(res, ((JRDesignSubreport) ele).getExpression());
		// // go recursively?
		// }
		// // get fonts?
		// }
	}

	protected void cacheResource(MResource res, JRExpression imgexp) throws Exception {
	}
}
