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
import java.net.URL;
import java.util.Set;

import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JasperDesign;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.model.AFileResource;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.server.model.MResourceBundle;
import com.jaspersoft.studio.server.publish.PublishOptions;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ImpResourceBundle extends AImpObject {
	public ImpResourceBundle(JasperReportsConfiguration jrConfig) {
		super(jrConfig);
	}

	public File publish(JasperReportsConfiguration jConfig, JasperDesign jd, String dpath, MReportUnit mrunit, IProgressMonitor monitor, Set<String> fileset, IFile file) throws Exception {
		File f = null;
		URL url = jConfig.getClassLoader().getResource(dpath + ".properties");
		if (url != null)
			f = new File(url.toURI());
		else
			f = findFile(file, dpath + ".properties");
		if (f != null && f.exists()) {
			fileset.add(f.getAbsolutePath());
			addResource(monitor, mrunit, fileset, f, new PublishOptions());
		}
		return f;
	}

	@Override
	protected ResourceDescriptor createResource(MReportUnit mrunit) {
		return MResourceBundle.createDescriptor(mrunit);
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
