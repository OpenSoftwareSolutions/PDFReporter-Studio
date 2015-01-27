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
package com.jaspersoft.studio.utils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import net.sf.jasperreports.eclipse.ui.util.UIUtils;
import net.sf.jasperreports.eclipse.util.FileExtension;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import com.jaspersoft.studio.compatibility.JRXmlWriterHelper;
import com.jaspersoft.studio.utils.jasper.JasperReportsConfiguration;

/**
 * This class maintains a list of utilities methods to manage JRXML files and streams.
 * 
 * @author Massimo Rabbi
 * 
 */
public class JRXMLUtils {

	/**
	 * Gets a JRXML input stream from an existing one, that can be either a .jasper file or .jrxml.
	 * <p>
	 * Others file extension are not meant to return a valid JRXML input stream.
	 * 
	 * @param in
	 *          the original input stream
	 * @param fileExtension
	 *          the file extension
	 * @param encoding
	 *          the file encoding
	 * @param version
	 *          the JR version
	 * @return a valid JRXML input stream, <code>null</code> if not possible
	 * @throws JRException
	 */
	public static InputStream getJRXMLInputStream(JasperReportsConfiguration jrContext, InputStream in,
			String fileExtension, String encoding, String version) throws JRException {
		if (fileExtension.equals(FileExtension.JASPER)) {
			// get JRXML from the .jasper
			JasperReport report = (JasperReport) JRLoader.loadObject(in);
			String str;
			try {
				str = JRXmlWriterHelper.writeReport(jrContext, report, JRXmlWriterHelper.fixencoding(encoding), version);
				return new ByteArrayInputStream(str.getBytes());
			} catch (Exception e) {
				UIUtils.showError("Something goes wrong while trying to create a JRXML input stream from a .jasper one.", e);
			}
		} else if (fileExtension.equals(FileExtension.JRXML)) {
			// the original one is already ok
			return in;
		}
		return null;
	}

	public static JasperDesign getJasperDesign(JasperReportsConfiguration jrContext, InputStream in,
			String fileExtension) throws JRException {
		if (fileExtension.equals(FileExtension.JASPER))
			return (JasperDesign) JRLoader.loadObject(in);
		if (fileExtension.equals(FileExtension.JRXML))
			return JRXmlLoader.load(jrContext, in);
		return null;
	}

}
