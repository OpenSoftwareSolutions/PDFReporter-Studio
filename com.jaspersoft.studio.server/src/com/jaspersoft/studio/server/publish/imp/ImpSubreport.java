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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.eclipse.util.FileUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignElement;
import net.sf.jasperreports.engine.design.JRDesignExpression;
import net.sf.jasperreports.engine.design.JRDesignSubreport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlWriter;

import org.eclipse.core.resources.IFile;

import com.jaspersoft.jasperserver.api.metadata.xml.domain.impl.ResourceDescriptor;
import com.jaspersoft.studio.server.model.MJrxml;
import com.jaspersoft.studio.server.model.MReportUnit;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

public class ImpSubreport extends AImpObject {

	public ImpSubreport(JasperReportsConfiguration jrConfig) {
		super(jrConfig);
	}

	@Override
	protected File findFile(IFile file, String str) {
		File f = super.findFile(file, str.replaceAll(FileExtension.PointJASPER, FileExtension.PointJRXML));
		if (f == null) {
			f = super.findFile(file, str);
			if (f != null) {
				try {
					Object obj = JRLoader.loadObject(jrConfig, f);
					if (obj != null && obj instanceof JasperReport) {
						JasperReport jrReport = (JasperReport) obj;
						f = getTmpFile(str);
						FileOutputStream fos = null;
						try {
							fos = new FileOutputStream(f);
							JRXmlWriter.writeReport(jrReport, fos, "UTF-8");
							return f;
						} catch (FileNotFoundException e) {
							FileUtils.closeStream(fos);
							e.printStackTrace();
						}

					}
				} catch (JRException e) {
					e.printStackTrace();
				}
			}
		}
		return f;
	}

	protected ResourceDescriptor createResource(MReportUnit mrunit) {
		return MJrxml.createDescriptor(mrunit);
	}

	protected JRDesignExpression getExpression(JRDesignElement img) {
		return (JRDesignExpression) ((JRDesignSubreport) img).getExpression();
	}
}
