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
package com.jaspersoft.studio.server.publish.imp;

import java.io.File;
import java.util.Set;

import net.sf.jasperreports.engine.JRReportTemplate;
import net.sf.jasperreports.engine.JRSimpleTemplate;
import net.sf.jasperreports.engine.JRTemplateReference;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlTemplateLoader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MRStyleTemplate;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.publish.PublishOptions;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ImpStyleTemplate extends AImpObject {
	public ImpStyleTemplate(JasperReportsConfiguration jrConfig) {
		super(jrConfig);
	}

	private IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

	public File publish(JasperDesign jd, JRReportTemplate img, MReportUnit mrunit, IProgressMonitor monitor, Set<String> fileset, IFile file) throws Exception {
		AFileResource fres = findFile(mrunit, monitor, jd, fileset, getExpression(img), file);
		if (fres != null) {
			JRSimpleTemplate jrt = (JRSimpleTemplate) JRXmlTemplateLoader.load(fres.getFile());
			for (JRTemplateReference r : jrt.getIncludedTemplatesList()) {
				IFile[] fs = root.findFilesForLocationURI(fres.getFile().toURI());
				if (fs != null && fs.length > 0) {
					File ftr = findFile(file, r.getLocation());
					if (ftr != null && ftr.exists()) {
						fileset.add(ftr.getAbsolutePath());
						addResource(monitor, mrunit, fileset, ftr, new PublishOptions());
					}
				}
			}
			return fres.getFile();
		}
		return null;
	}

	@Override
	protected ResourceDescriptor createResource(MReportUnit mrunit) {
		return MRStyleTemplate.createDescriptor(mrunit);
	}

	protected JRDesignExpression getExpression(JRReportTemplate img) {
		return (JRDesignExpression) ((JRReportTemplate) img).getSourceExpression();
	}

	@Override
	public AFileResource publish(JasperDesign jd, JRDesignElement img, MReportUnit mrunit, IProgressMonitor monitor, Set<String> fileset, IFile file) throws Exception {
		return null;
	}

	@Override
	protected JRDesignExpression getExpression(JRDesignElement img) {
		return null;
	}

}
